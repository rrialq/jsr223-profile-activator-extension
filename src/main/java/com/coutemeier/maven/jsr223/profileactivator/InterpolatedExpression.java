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

/**
 * This class wraps the interpolated expression, so we can manipulate its content in a confortable way.
 * We can recovery directly a Boolean value, an Integer value or a String, with or without default value.
 * <p>
 * We consider that an expression was resolved when the expression and the interpolated expression are different,
 * although the expression had not been resolved completely.
 * <p>
 * Consider the following expression:
 * <p>
 * <code>${my.system.property}/path/${my.project.property}</code>
 * <p>
 * Suposse that we forget to supply the system property <code>my.system.property</code>, and the value of
 * <code>my.project.property</code> is <code>alfa.0001.tmp</code>, so the interpolator resolves the expression as:
 * <p>
 * <code>${my.system.property}/path/alfa.0001.tmp</code>
 * <p>
 * The original expression and the interpolated expression are different, so no default value is returned.
 *
 * @since 0.1.0
 */
public final class InterpolatedExpression {
    /**
     * The resolved expression.
     *
     * @since 0.1.0
     */
    private final String expression;

    /**
     * It says that if expression was resolved or not.
     * This is useful in methods with accepts a default value (parameter).
     *
     * @since 0.1.0
     */
    private final boolean resolved;

    /**
     * Builds the interpolated expression class with the resolved expression and a value that shows if the
     * expression was resolved.
     *
     * @param expression the resolved expression returned by the interpolator.
     * @param resolved if an expression was resolved <code>true</code>, <code>false</code> if it wasn't.
     *
     * @since 0.1.0
     */
    public InterpolatedExpression( String expression, boolean resolved ) {
        this.expression = expression;
        this.resolved = resolved;
    }

    /**
     * Reports if an expression was resolved by the interpolator.
     * @return <code>true</code> if the interpolator resolved the expression, at least parcially,
     * <code>false</code> in other case.
     *
     * @since 0.1.0
     */
    public boolean isResolved() {
        return this.resolved;
    }

    /**
     * Reports if an expression was not resolved by the interpolator.
     * @return <code>true</code> if the interpolator could not resolved the expression, even partially,
     * <code>false</code> if the interpolator resolved the expression, at least parcially.
     *
     * @since 0.1.0
     */
    public boolean isUnResolved() {
        return ! this.resolved;
    }

    /**
     * Returns the value of the expression as String.
     * @return a text with the expression resolved or the same expression unresolved.
     * @since 0.1.0
     */
    public String getString() {
        return this.getString( expression );
    }

    /**
     * Returns the value of the expression as String.
     * @param defaultValue a value to return when the expression was not resolved.
     * @return a text with the expression resolved, or the default value when unresolved.
     * @since 0.1.0
     */
    public String getString( String defaultValue ) {
        if ( this.isResolved() ) {
            return this.expression;
        }
        return this.isResolved() ? this.expression : defaultValue;
    }

    /**
     * Returns the value of the expression as boolean.
     * @return <code>true</code> or <code>false</code>. If the expression is not resolved then returns <code>false</code>.
     *
     * @see Boolean#parseBoolean( String ) to know the parsing details.
     * @since 0.1.0
     */
    public boolean getBoolean() {
        return this.getBoolean( false );
    }

    /**
     * Returns the value of the expression as boolean, or the default value when the expressoin was not resolved.
     * @return <code>true</code> or <code>false</code>. If the expression is not resolved then returns the default value.
     * @param defaultValue a value to return when the expression was not resolved.
     *
     * @see Boolean#parseBoolean( String ) to know the parsing details.
     * @since 0.1.0
     */
    public boolean getBoolean( boolean defaultValue ) {
        return this.isResolved() ? Boolean.parseBoolean( this.expression ) : defaultValue;
    }

    /**
     * Returns the value of the expression as int when it was resolved.
     * @return the number corresponding to the parsing of the expression.
     * If the expression is not resolved then returns <code>0</code>.
     *
     * @see Integer#parseInt( String ) to know the parsing details.
     * @since 0.1.0
     */
    public int getInt( ) {
        return this.getInt( 0 );
    }

    /**
     * Returns the value of the expression as int.
     * @return the number corresponding to the parsing of the expression.
     * If the expression is not resolved then returns the default value.
     * @param defaultValue a value to return when the expression was not resolved.
     *
     * @see Integer#parseInt( String ) to know the parsing details.
     * @since 0.1.0
     */
    public int getInt( int defaultValue ) {
        return this.isResolved() ? Integer.parseInt( this.expression ) : defaultValue;
    }
}
