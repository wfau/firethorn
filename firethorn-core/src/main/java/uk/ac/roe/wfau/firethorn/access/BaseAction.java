/*
 *  Copyright (C) 2018 Royal Observatory, University of Edinburgh, UK
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

package uk.ac.roe.wfau.firethorn.access;

import java.net.URI;

/**
 * Simple select {@link Action}.
 * 
 */
public class BaseAction implements Action
    {
    /**
     * Public constructor.
     * @param type The type of {@link Action}.
     * @param uri  The {@link Action} {@link URI}.
     * @param name The {@link Action} name.
     * 
     */
    public BaseAction(final ActionType type, final String uri, final String name)
        {
        this(
            type,
            URI.create(uri),
            name
            );
        }
    
    /**
     * Public constructor.
     * @param type The type of {@link Action}.
     * @param uri  The {@link Action} {@link URI}.
     * @param name The {@link Action} name.
     * 
     */
    public BaseAction(final ActionType type, final URI uri, final String name)
        {
        this.uri  = uri;
        this.type = type;
        this.name = name;
        }

    private String name;
    @Override
    public String name()
        {
        return this.name;
        }

    private URI uri;
    @Override
    public URI uri()
        {
        return this.uri;
        }

    private ActionType type;
    @Override
    public ActionType type()
        {
        return this.type;
        }
    }
