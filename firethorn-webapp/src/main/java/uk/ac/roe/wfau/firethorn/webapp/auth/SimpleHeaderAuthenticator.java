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
import uk.ac.roe.wfau.firethorn.community.Community;
import uk.ac.roe.wfau.firethorn.community.UnauthorizedException;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.identity.Operation;
import uk.ac.roe.wfau.firethorn.spring.ComponentFactories;

/**
 *
 *
 */
@Slf4j
public class SimpleHeaderAuthenticator
implements HandlerInterceptor
    {
    /**
     * Autowired system services.
     *
     */
    @Autowired
    private ComponentFactories factories;

    static final String METHOD_NAME = "http:header" ;

    static final String USERNAME_ATTRIB  = "firethorn.auth.simple.username"  ;
    static final String PASSWORD_ATTRIB  = "firethorn.auth.simple.password"  ;
    static final String COMMUNITY_ATTRIB = "firethorn.auth.simple.community" ;

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
    throws UnauthorizedException
        {
        log.debug("preHandle()");

        final String username = request.getHeader(USERNAME_ATTRIB);
        final String password = request.getHeader(PASSWORD_ATTRIB);
        final String comident = request.getHeader(COMMUNITY_ATTRIB);

        final Operation operation = factories.operations().entities().current();
        log.debug("Operation [{}]", operation);

        log.debug("Username  [{}]", username);
        log.debug("Password  [{}]", password);
        log.debug("Community [{}]", comident);
        
        if (operation != null)
            {
            if ((comident != null) || (username != null) || (password != null))
                {
                operation.authentications().create(
                    factories.communities().entities().login(
                        comident,
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
        }

    @Override
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final Exception ouch)
        {
        log.debug("afterCompletion()");
        }
    }
