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
package uk.ac.roe.wfau.firethorn.webapp.oper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.identity.Operation;
import uk.ac.roe.wfau.firethorn.spring.ComponentFactories;

/**
 * TODO Move this to a OperationModel
 *
 */
@Slf4j
public class OperationInterceptor
implements HandlerInterceptor
    {
    /**
     * Autowired system services.
     *
     */
    @Autowired
    private ComponentFactories factories;

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
        {
        log.debug("OperationInterceptor.preHandle()");
        log.debug("Handler   [{}]", (handler != null) ? handler.getClass().getName() : "null");

        final Operation oper = factories.operations().entities().create(
            request.getRequestURL().toString(),
            request.getMethod(),
            request.getRemoteAddr(),
            request.getServerPort()
            );

        log.debug("Operation [{}]", oper.ident());
        log.debug("  url  [{}]", oper.url());
        log.debug("  port [{}]", oper.port());

        //
        // Add the port number as a request attribute.
        request.setAttribute("adql.query.callback.port", new Integer(request.getServerPort()));

        return true ;
        }

    @Override
    public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final ModelAndView model)
        {
        log.debug("OperationInterceptor.postHandle()");
        final Operation oper = factories.operations().entities().current();
        log.debug("Operation [{}][{}]", oper.ident(), oper.url());
        log.debug("Handler   [{}]", handler.getClass().getName());
        }

    @Override
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final Exception ouch)
        {
        log.debug("OperationInterceptor.afterCompletion()");
        final Operation oper = factories.operations().entities().current();
        log.debug("Operation [{}][{}]", oper.ident(), oper.url());
        log.debug("Handler   [{}]", (handler != null) ? handler.getClass().getName() : "null");
        if (ouch != null)
            {
            log.debug("Operation threw an exception");
            log.debug(" type [{}]", ouch.getClass().getName());
            // Include the stack trace in the message
            // http://slf4j.org/faq.html#paramException
            log.debug(" text [{}]", ouch.getMessage(), ouch);
            }
        }
    }
