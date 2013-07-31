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

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import uk.ac.roe.wfau.firethorn.identity.Operation;
import uk.ac.roe.wfau.firethorn.spring.ComponentFactories;

/**
 *
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

    public Operation.EntityFactory operations()
        {
        return factories.operations() ; 
        }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
        {
        log.debug("preHandle()");
        log.debug("Handler   [{}]", (handler != null) ? handler.getClass().getName() : "null");

        Operation oper = operations().create(
            request.getRequestURL().toString(),
            request.getMethod(),
            request.getRemoteAddr()
            );

        log.debug("Operation [{}][{}]", oper.ident(), oper.target());
        log.debug("Handler   [{}]", handler.getClass().getName());

        return true ;
        }

    @Override
    public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final ModelAndView model)
        {
        log.debug("postHandle()");
        Operation oper = operations().current();
        log.debug("Operation [{}][{}]", oper.ident(), oper.target());
        log.debug("Handler   [{}]", handler.getClass().getName());
        }

    @Override
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final Exception ouch)
        {
        log.debug("after()");
        Operation oper = operations().current();
        log.debug("Operation [{}][{}]", oper.ident(), oper.target());
        log.debug("Handler   [{}]", (handler != null) ? handler.getClass().getName() : "null");
        if (ouch != null)
            {
            log.debug("Operation threw an exception");
            log.debug(" type [{}]", ouch.getClass().getName());
            log.debug(" text [{}]", ouch.getMessage());
            }
        }
    }
