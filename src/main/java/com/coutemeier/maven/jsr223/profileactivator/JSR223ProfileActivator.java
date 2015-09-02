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

import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.maven.model.Activation;
import org.apache.maven.model.ActivationProperty;
import org.apache.maven.model.Profile;
import org.apache.maven.model.building.ModelProblemCollector;
import org.apache.maven.model.building.ModelProblemCollectorRequest;
import org.apache.maven.model.building.ModelProblem.Severity;
import org.apache.maven.model.building.ModelProblem.Version;
import org.apache.maven.model.profile.ProfileActivationContext;
import org.apache.maven.model.profile.activation.ProfileActivator;
import org.apache.maven.model.profile.activation.PropertyProfileActivator;

import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;

/**
 * JSR223 profile activator.
 *
 */
@Component(role = ProfileActivator.class, hint = "property")
public class JSR223ProfileActivator implements ProfileActivator {
    private static final String DEFAULT_SCRIPT_ENGINE_NAME = "JavaScript";

    @Requirement
    private final Logger logger = LoggerFactory.getLogger( JSR223ProfileActivator.class );

    @Override
    public boolean isActive( Profile profile, ProfileActivationContext context, ModelProblemCollector problemCollector ) {
        Activation activation = profile.getActivation();

        if (activation != null) {
            ActivationProperty property = activation.getProperty();

            if (property != null) {
                String name = property.getName();
                if ( name != null && name.startsWith( "=" ) ) {
                    return evaluateExpression( profile, context, problemCollector );
                }
            }
        }

        // Call original implementation if JSR223 expression was not found
        return new PropertyProfileActivator().isActive( profile, context, problemCollector );
    }

    @Override
    public boolean presentInConfig( Profile profile, ProfileActivationContext context, ModelProblemCollector problems ) {
        Activation activation = profile.getActivation();

        if ( activation == null ) {
            return false;
        }

        return activation.getProperty() != null;
    }

    private boolean evaluateExpression( Profile profile, ProfileActivationContext context, ModelProblemCollector problemCollector ) {
        Activation activation = profile.getActivation();
        ActivationProperty property = activation.getProperty();
        String engineName = property.getName().substring(1);
        String script = property.getValue();
        if ( engineName == null || engineName.trim().length() == 0 ) {
            engineName = DEFAULT_SCRIPT_ENGINE_NAME;
        }

        // create a JavaScript engine
        ScriptEngine engine = ScriptEngineHelper.getEngine( engineName );
        // We haven't foud a script engine, so we can't evaluate the script
        if ( engine == null ) {
            final String message = "[JSR223ProfileActivator] Script engine unknown " + profile.getId();
            logger.debug(message);
            problemCollector.add( new ModelProblemCollectorRequest( Severity.ERROR, Version.BASE )
                    .setMessage( message )
                    .setLocation( property.getLocation( "" ) ) );
            return false;
        }
        logger.debug( "[JSR223ProfileActivator] ScriptEngine {}. Trying to evaluate the expression {}", engineName, script );

        String name = ScriptEngineHelper.getPropertyNameForJSR223( context );
        engine.put(
            name,
            new ExpressionManager(
                context.getSystemProperties(),
                context.getProjectProperties(),
                context.getUserProperties()
            )
        );

        try {
            Object value = engine.eval( script );
            logger.debug( "[JSR223ProfileActivator] Expression evaluated value: {}", value );
            return true;

        } catch ( ScriptException cause ) {
            final String message = "[JSR223ProfileActivator] There has been an error evaluating the expression in the profile "
                    + profile.getId() + ": " + cause.getMessage();
            logger.debug( message );
            problemCollector.add( new ModelProblemCollectorRequest( Severity.ERROR, Version.BASE )
                    .setMessage( message )
                    .setLocation( property.getLocation( "" ) ) );
        }
        return false;
    }
}
