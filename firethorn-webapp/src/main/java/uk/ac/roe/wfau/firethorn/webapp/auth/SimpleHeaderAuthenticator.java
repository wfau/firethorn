/*
 *  Copyright (C) 2013 Royal Observatory, University of Edinburgh, UK
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package uk.ac.roe.wfau.firethorn.webapp.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.community.Community;
import uk.ac.roe.wfau.firethorn.community.UnauthorizedException;
import uk.ac.roe.wfau.firethorn.identity.Authentication;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.identity.Operation;
import uk.ac.roe.wfau.firethorn.spring.ComponentFactories;

/**
 * Authenticate our identity based on simple header fields.
 *
 */
@Slf4j
public class SimpleHeaderAuthenticator
implements HandlerInterceptor
    {
    @Autowired
    private ComponentFactories factories;

    public static final String METHOD_NAME = "http:header" ;

    public static final String COMMUNITY_ATTRIB = "firethorn.auth.community" ;
    public static final String USERNAME_ATTRIB  = "firethorn.auth.username"  ;
    public static final String PASSWORD_ATTRIB  = "firethorn.auth.password"  ;
    public static final String IDENTITY_ATTRIB  = "firethorn.auth.identity"  ;

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
    throws UnauthorizedException, ProtectionException
        {
        log.debug("preHandle()");

        final String commname = request.getHeader(COMMUNITY_ATTRIB);
        final String username = request.getHeader(USERNAME_ATTRIB);
        final String password = request.getHeader(PASSWORD_ATTRIB);

        final Operation operation = factories.operations().entities().current();
        log.debug("Operation [{}]", operation);

        log.debug("Community [{}]", commname);
        log.debug("Username  [{}]", username);
        log.debug("Password  [{}]", password);
        
        if (operation != null)
            {
            if ((commname != null) || (username != null) || (password != null))
                {
                operation.authentications().create(
                    factories.communities().entities().login(
                        commname,
                        username,
                        password
                        ),
                    METHOD_NAME
                    );
                }
            }

        log.debug("Primary   [{}]", operation.authentications().primary());
       
        return true ;
        }

    @Override
    public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final ModelAndView model)
        {
        log.debug("postHandle()");

        /*
         * postHandle() doesn't work with @ResponseBody controller methods.
         * https://stackoverflow.com/a/39994876
         * https://stackoverflow.com/questions/30702970/spring-modifying-headers-for-every-request-after-processing-in-posthandle
         * https://mtyurt.net/post/spring-modify-response-headers-after-processing.html
         * 
         */
        
        final Operation operation = factories.operations().entities().current();
        if (operation != null)
            {
            log.debug("Operation [{}]", operation.ident());
            final Authentication authentication = operation.authentications().primary();
            if (authentication != null)
                {
                log.debug("Authentication [{}]", authentication);
                final Identity identity = authentication.identity();

                if (identity != null)
                    {
                    log.debug("Identity  [{}][{}]", identity.ident(), identity.name());
                    response.addHeader(IDENTITY_ATTRIB, identity.link());
                    response.addHeader(USERNAME_ATTRIB, identity.name());

                    final Community community = identity.community();
                    log.debug("Community [{}][{}]", community.ident(), community.name());

                    if (community != null)
                        {
                        response.addHeader(COMMUNITY_ATTRIB, community.name());
                        }
                    }
                }
            }
        }

    @Override
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final Exception ouch)
        {
        log.debug("afterCompletion()");
        }
    }
