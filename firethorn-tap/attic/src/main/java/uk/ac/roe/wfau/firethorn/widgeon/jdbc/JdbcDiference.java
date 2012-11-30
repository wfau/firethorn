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
package uk.ac.roe.wfau.firethorn.widgeon.jdbc;

/**
 * Class to represent differences between the our metadata and the database.
 *
 */
public class JdbcDiference
    {

    public enum Type
        {
        CATALOG(),
        SCHEMA(),
        TABLE(),
        COLUMN()
        };

    public JdbcDiference(final JdbcDiference.Type type, final String meta, final String real)
        {
        this.type = type;
        this.meta = meta;
        this.real = real;
        }

    private final JdbcDiference.Type type;
    public JdbcDiference.Type type()
        {
        return this.type;
        }

    private final String meta ;
    public String meta()
        {
        return this.meta;
        }

    private final String real ;
    public String real()
        {
        return this.real;
        }

    @Override
    public boolean equals(final Object that)
        {
        if (that !=null)
            {
            if (that instanceof JdbcDiference)
                {
                return this.equals(
                    (JdbcDiference) that
                    );
                }
            }
        return false ;
        }

    public boolean equals(final JdbcDiference that)
        {
        if (that == null)
            {
            return false ;
            }
        if ((this.type() != null) && (that.type() == null))
            {
            return false ;
            }
        if ((this.type() == null) && (that.type() != null))
            {
            return false ;
            }
        if ((this.meta() != null) && (that.meta() == null))
            {
            return false ;
            }
        if ((this.meta() == null) && (that.meta() != null))
            {
            return false ;
            }
        if ((this.real() != null) && (that.real() == null))
            {
            return false ;
            }
        if ((this.real() == null) && (that.real() != null))
            {
            return false ;
            }
        if (this.type() != null)
            {
            if (this.type().equals(that.type()) == false)
                {
                return false ;
                }
            }
        if (this.meta() != null)
            {
            if (this.meta().equals(that.meta()) == false)
                {
                return false ;
                }
            }
        if (this.real() != null)
            {
            if (this.real().equals(that.real()) == false)
                {
                return false ;
                }
            }
        return true ;
        }
    }