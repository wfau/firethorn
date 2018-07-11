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
package uk.ac.roe.wfau.firethorn.webapp.debug;

import java.util.Enumeration;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.community.UnauthorizedException;

/**
 * Servlet {@link HandlerInterceptor} to print out the request and response properties.
 *
 */
@Slf4j
public class HttpRequestDebug
implements HandlerInterceptor
    {
    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
    throws UnauthorizedException
        {
        log.debug("Request properties");
      //log.debug("  scheme      [{}]", request.getScheme());
      //log.debug("  serverName  [{}]", request.getServerName());
      //log.debug("  serverPort  [{}]", request.getServerPort());
      //log.debug("  serverPath  [{}]", request.getServletPath());
      //log.debug("  pathInfo    [{}]", request.getPathInfo());
      //log.debug("  pathTrans   [{}]", request.getPathTranslated());
      //log.debug("  requestURI  [{}]", request.getRequestURI());
        log.debug("  requestURL  [{}]", request.getRequestURL());
      //log.debug("  protocol    [{}]", request.getProtocol());
        log.debug("  queryString [{}]", request.getQueryString());
        log.debug("  authType    [{}]", request.getAuthType());        
        log.debug("  remoteAddr  [{}]", request.getRemoteAddr());        
      //log.debug("  remoteHost  [{}]", request.getRemoteHost());        

        log.debug("Request headers");
        final Enumeration<String> headers = request.getHeaderNames();
        if (headers != null)
            {
            while (headers.hasMoreElements())
                {
                final String name  = headers.nextElement();
                final String value = request.getHeader(name);
                log.debug("  [{}][{}]", name, StringUtils.abbreviate(value, 20));
                }
            }

/*
 * The attributes didn't tell us much.
        log.debug("----");
        log.debug("Attributes");
        final Enumeration<String> attribs = request.getAttributeNames(); 
        if (attribs != null)
            {
            while (attribs.hasMoreElements())
                {
                final String name  = attribs.nextElement();
                final Object value = request.getAttribute(name);
                log.debug("  [{}][{}]", name, StringUtils.abbreviate(value.toString(), 20));
                }
            }
        else {
            log.debug("-- null --");;
            }
 *         
 */
        log.debug("Request parameters");
        final Enumeration<String> params = request.getParameterNames(); 
        if (params != null)
            {
            while (params.hasMoreElements())
                {
                final String name  = params.nextElement();
                final Object value = request.getParameter(name);
                log.debug("  [{}][{}]", name, StringUtils.abbreviate(value.toString(), 20));
                }
            }
        log.debug("----");
        
        return true ;
        }

    @Override
    public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final ModelAndView model)
        {
        log.debug("Response headers");
        final Iterator<String> headers = response.getHeaderNames().iterator();
        if (headers != null)
            {
            while (headers.hasNext())
                {
                final String name  = headers.next();
                final String value = response.getHeader(name);
                log.debug("  [{}][{}]", name, StringUtils.abbreviate(value, 20));
                }
            }
        log.debug("----");
        }

    @Override
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final Exception ouch)
        {
        }
    }
