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
package uk.ac.roe.wfau.firethorn.tuesday;

/**
 *
 *
 */
public class TuesdayAdqlTableEntity
extends TuesdayBaseTableEntity
    implements TuesdayAdqlTable
    {
    @Override
    public String name()
        {
        if (super.name() == null)
            {
            return base().name();
            }
        else {
            return super.name();
            }
        }
    @Override
    public String text()
        {
        if (super.text() == null)
            {
            return base().text();
            }
        else {
            return super.text();
            }
        }
    @Override
    public String type()
        {
        if (super.type() == null)
            {
            return base().type();
            }
        else {
            return super.type();
            }
        }
    @Override
    public Integer size()
        {
        if (super.size() == null)
            {
            return base().size();
            }
        else {
            return super.size();
            }
        }
    @Override
    public String ucd()
        {
        if (super.ucd() == null)
            {
            return base().ucd();
            }
        else {
            return super.ucd();
            }
        }
    
    private TuesdayAdqlSchema schema;
    @Override
    public TuesdayAdqlSchema schema()
        {
        return schema;
        }
    @Override
    public TuesdayAdqlResource resource()
        {
        return schema.resource();
        }

    @Override
    public Columns columns()
        {
        return new Columns()
            {
            @Override
            public Iterable<TuesdayAdqlColumn> select()
                {
                return null;
                }
            @Override
            public TuesdayAdqlColumn select(String name)
                {
                return null;
                }
            };
        }

    // Reference (TuesdayBaseTableEntity)
    private TuesdayBaseTableEntity base ;
    public TuesdayBaseTable base()
        {
        return base ;
        }
    @Override
    public TuesdayAdqlTable adql()
        {
        return this ;
        }
    @Override
    public TuesdayOgsaTable<?> ogsa()
        {
        return base.ogsa();
        }

    public Children children()
        {
        return new Children()
            {
            @Override
            public Iterable<TuesdayAdqlTable> select()
                {
                return null;
                }
            };
        }
    }
