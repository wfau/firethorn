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

import uk.ac.roe.wfau.firethorn.mallard.AdqlService;
import uk.ac.roe.wfau.firethorn.widgeon.DataResource;

/**
 *
 *
 */
public interface UrlBuilder
    {
    /**
     * Get the web service base '/' URL.
     *
     */
    public abstract URL url();

    /**
     * Create a URL for a AdqlService.
     *
     */
    public abstract URL url(final AdqlService target);

    /**
     * Create a URL for a DataResource.
     *
     */
    public abstract URL url(final DataResource target);

    }
