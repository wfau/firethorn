/*
 *  Copyright (C) 2012 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.webapp.control;

import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import uk.ac.roe.wfau.firethorn.mallard.AdqlService;
import uk.ac.roe.wfau.firethorn.webapp.mallard.AdqlServiceController;
import uk.ac.roe.wfau.firethorn.widgeon.DataResource;

/**
 *
 *
 */
public class UrlBuilderImpl
implements UrlBuilder
    {

    private PathBuilder paths ;
    private HttpServletRequest request ;
    
    public UrlBuilderImpl(final HttpServletRequest request, final PathBuilder paths)
        {
        this.paths = paths ;
        this.request = request ;
        }

    @Override
    public URL url()
        {
        return null ;
        }

    @Override
    public URL url(AdqlService target)
        {
        try {
            return new URL(
                "http",
                request.getServerName(),
                request.getServerPort(),
                paths.path(
                    AdqlServiceController.CONTROLLER_PATH,
                    target
                    ).toString()
                );
            }
        catch(Exception ouch)
            {
            return null ;
            }
        }

    @Override
    public URL url(DataResource target)
        {
        return null;
        }

    }
