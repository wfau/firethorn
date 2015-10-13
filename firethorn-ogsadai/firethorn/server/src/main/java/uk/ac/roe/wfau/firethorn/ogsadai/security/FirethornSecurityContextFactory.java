/**
 * Copyright (c) 2015, ROE (http://www.roe.ac.uk/)
 * All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package uk.ac.roe.wfau.firethorn.ogsadai.security;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.org.ogsadai.authorization.SecurityContext;
import uk.org.ogsadai.dqp.firethorn.MetadataServiceDQPFederation;
import uk.org.ogsadai.service.rest.authorisation.SecurityContextFactory;

public class FirethornSecurityContextFactory
implements SecurityContextFactory 
    {
    /**
     * Debug logger.
     *
     */
    private static Log log = LogFactory.getLog(FirethornSecurityContextFactory.class);

    protected static final String CALLBACK_IDENT_KEY = "firethorn.callback.ident" ;

    protected static final String CALLBACK_ENDPOINT_KEY = "firethorn.callback.endpoint" ;

    protected static final String DEFAULT_ENDPOINT_URL = "http://localhost:8080/firethorn" ;

    public static String endpoint(final HttpServletRequest request)
        {
        log.debug("endpoint(HttpServletRequest)");
        final String endpoint = request.getHeader(
            CALLBACK_ENDPOINT_KEY
            );
        log.debug("Endpoint [" + endpoint + "]");
        if (endpoint != null)
            {
            return endpoint  ;
            }
        else {
            final String hostname = request.getRemoteHost();                
            log.debug("Hostname [" + hostname + "]");
            if (hostname != null)
                {
                return DEFAULT_ENDPOINT_URL.replace(
                    "localhost",
                    hostname
                    );
                }
            else {
                return DEFAULT_ENDPOINT_URL ;
                }
            }
        }
    
    @Override
    public SecurityContext createContext(final HttpServletRequest request) 
        {
        return new FirethornSecurityContext()
            {
            private String endpoint = FirethornSecurityContextFactory.endpoint(
                request
                );  
            @Override
            public String endpoint()
                {
                return endpoint;
                }
            };
        }
    }
