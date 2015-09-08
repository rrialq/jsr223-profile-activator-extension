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

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.maven.model.profile.ProfileActivationContext;

/**
 * A simple helper for some logic associated to the ScriptEngine.
 *
 * @since 0.1.0
 */
public final class ScriptEngineHelper {
    /**
     * The name of the property to supply the interpolator variable name.
     */
    private static final String JSR223_PROPERTY_NAME = "jsr223.profile.activator.name";

    /**
     * The default name of the interpolator variable, if none supplied.
     */
    private static final String JSR223_PROPERTY_NAME_DEFAULT = "aitor";

    private ScriptEngineHelper() { }

    /**
     * Returns the engine acoording to the <code>engineName</code>. The priority of the search is as following:
     * <ol>
     *      <li>By name.</li>
     *      <li>By extensión.</li>
     *      <li>By mime type.</li>
     * </ol>
     *
     * @param engineName the name, extension or mime type of the engine to search for.
     * @return the script engine matching the <code>engine</code> or <code>null</code> if we can't found any.
     * @since 0.1.0
     */
    public static final ScriptEngine getEngine( String engineName ) {
        if ( engineName != null ) {
            ScriptEngineManager factory = new ScriptEngineManager();
            // We'll try to get the engine by name (i.e. JavaScript)
            ScriptEngine engine = factory.getEngineByName( engineName );
            if ( engine != null ) {
                return engine;
            }
            // We'll try to get the engine by extension (i.e. javascript)
            engine = factory.getEngineByExtension( engineName );
            if ( engine != null ) {
                return engine;
            }
            // We'll try to get the engine by MimeType (i.e. application/javascript)
            return factory.getEngineByMimeType( engineName );
        }
        // We've just not found any engine matching "engine", so we return null
        return null;
    }

    /**
     * Returns the name of the variable which will be created to use the interpolator in the script.
     * The priority of the search is as following:
     * <ol>
     *      <li>If <code>JSR223_PROPERTY_NAME</code> exists in system properties, then return it.</li>
     *      <li>If <code>JSR223_PROPERTY_NAME</code> exists in project properties, then return it.</li>
     *      <li>Return <code>JSR223_PROPERTY_NAME_DEFAULT (<b>aitor</b>) as the name of the variable.</li>
     * <ol>
     *
     * @param context the context of the profile activation section of the pom.
     * @return the name of the variable present in system properties or project properties, or <code>aitor</code> if
     * doesn't exists none of them.
     * @since 0.1.0
     */
    public static final String getPropertyNameForJSR223( ProfileActivationContext context ) {
        String name = context.getSystemProperties().get( JSR223_PROPERTY_NAME );
        if ( name == null || name.trim().length() == 0 ) {
            name = context.getProjectProperties().get( JSR223_PROPERTY_NAME );
        }

        if ( name == null || name.trim().length() == 0 ) {
            name = JSR223_PROPERTY_NAME_DEFAULT;
        }
        return name;
    }
}
