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
package uk.ac.roe.wfau.firethorn.entity;

import java.io.Serializable;


public class ProxyIdentifier
implements Identifier
    {
    private final Identifier parent;
    public Identifier parent()
        {
        return this.parent;
        }

    private final Identifier base;
    public Identifier base()
        {
        return this.base;
        }

    public ProxyIdentifier(final Identifier parent, final Identifier base)
        {
        this.parent = parent;
        this.base   = base  ;
        }

    @Override
    public Serializable value()
        {
        return toString();
        }

    @Override
    public String toString()
        {
        final StringBuilder builder = new StringBuilder();
        builder.append("(");
        builder.append(parent.toString());
        builder.append(":");
        builder.append(base.toString());
        builder.append(")");
        return builder.toString();
        }
    }