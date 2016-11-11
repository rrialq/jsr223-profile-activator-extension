package com.coutemeier.maven.jsr223.profileactivator;

/*
 * #%L
 * JSR223 Profile Activator Maven Extension
 * %%
 * Copyright (C) 2015 - 2016 RRQ
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

import org.apache.maven.AbstractMavenLifecycleParticipant;
import org.apache.maven.MavenExecutionException;
import org.apache.maven.execution.MavenSession;

import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component( role = AbstractMavenLifecycleParticipant.class, hint = "jsr223ProfileActivator" )
public class JSR223ProfileActivatorLifecycleParticipant extends AbstractMavenLifecycleParticipant {
    @Requirement
    private final Logger logger = LoggerFactory.getLogger( JSR223ProfileActivator.class );

    @Override
    public void afterSessionStart( MavenSession session )
    throws MavenExecutionException {
      logger.info( "JSR223ProfileActivator core extension is ready" );
    }

}
