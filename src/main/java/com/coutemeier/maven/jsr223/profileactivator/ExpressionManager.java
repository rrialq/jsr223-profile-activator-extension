package com.coutemeier.maven.jsr223.profileactivator;

/*
 * #%L
 * JSR223 Profile Activator Maven Extension
 * %%
 * Copyright (C) 2015 RRQ
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.Map;

import org.codehaus.plexus.interpolation.InterpolationException;
import org.codehaus.plexus.interpolation.MapBasedValueSource;
import org.codehaus.plexus.interpolation.RegexBasedInterpolator;
import org.codehaus.plexus.interpolation.ValueSource;

/**
 * Expression manager, that encapsulates the logic needed to resolve expressions in the script.
 * <p>
 * The expression manager has a single mission: interpolate the expression and return an object
 * of type {@link InterpolatedExpression}.
 *
 * @since 0.1.0
 */
public final class ExpressionManager {
    /**
     * The interpolator, used to interpolate expressions.
     */
    private final RegexBasedInterpolator interpolator;

    /**
     * Builds an expression manager.
     * @param maps an array of maps of strings with the values of the properties.
     *
     * @since 0.1.0
     */
    public ExpressionManager( Map< String, String > ... maps ) {
        final RegexBasedInterpolator interpolator = new RegexBasedInterpolator();
        for ( Map< String, String > map: maps ) {
            interpolator.addValueSource( new MapBasedValueSource( map ) );
        }
        this.interpolator = interpolator;
    }

    /**
     * Evaluates an expression, with the help of the interpolator of Maven.
     * @param expression the expression we want to resolve (interpolate).
     * @return an {@link InterpolatedExpression} object, with the interpolated value and a flag
     * that reports about if the expression could be interpolated (at least partially).
     *
     * @throws InterpolationException if the interpolation gets an error.
     * @since 0.1.0
     */
    public InterpolatedExpression eval( String expression )
    throws InterpolationException {
        String value = this.interpolator.interpolate( expression );
        return new InterpolatedExpression( value, !value.equals( expression ) );
    }
}
