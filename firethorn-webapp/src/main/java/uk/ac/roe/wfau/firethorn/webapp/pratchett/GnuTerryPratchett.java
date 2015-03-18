/*
 *  Copyright (C) 2015 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.webapp.pratchett;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * HTTP response header filter.
 * http://www.gnuterrypratchett.com/
 * http://www.theguardian.com/books/shortcuts/2015/mar/17/terry-pratchetts-name-lives-on-in-the-clacks-with-hidden-web-code
 *
 */
public class GnuTerryPratchett
implements Filter
    {
    public static final String HEADER_NAME  = "X-Clacks-Overhead"   ;
    public static final String HEADER_VALUE = "GNU Terry Pratchett" ;
    
    @Override
    public void init(final FilterConfig config)
        {
        }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
    throws IOException, ServletException
        {
        if (response instanceof HttpServletResponse)
            {
            ((HttpServletResponse) response).addHeader(
                HEADER_NAME,
                HEADER_VALUE
                );
            }
        chain.doFilter(
            request,
            response
            );
        }

    @Override
    public void destroy()
        {
        }
    }
