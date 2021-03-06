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

package uk.ac.roe.wfau.firethorn.ogsadai.activity.common.chaos;

import java.sql.SQLException;

/**
 * 
 * 
 */
public abstract class MonkeyParamBase
implements MonkeyParam
    {
    
    /**
     * Public constructor.
     * 
     */
    public MonkeyParamBase()
        {
        this(
            DEFAULT_NAME,
            DEFAULT_DATA
            );
        }

    /**
     * Public constructor.
     * 
     */
    public MonkeyParamBase(final MonkeyParam param)
        {
        this(
            ((param != null) ? param.name() : DEFAULT_NAME),
            ((param != null) ? param.data() : DEFAULT_DATA)
            );
        }

    /**
     * Public constructor.
     * 
     */
    public MonkeyParamBase(final Object name, final Object data)
        {
        super();
        this.name(name);
        this.data(data);
        }

    public abstract void name(final Object name);
    public abstract void data(final Object data);
    
    @Override
    public String toString()
        {
        final StringBuilder builder = new StringBuilder();
        builder.append("ChaosMonkey [");
        builder.append(this.name());
        builder.append("][");
        builder.append(this.data());
        builder.append("]");
        return builder.toString();
        }

    @Override
    public boolean test(final Object owner, final Object value)
        {
        if ((this.name() != null) && (owner != null))
            {
            if (this.name().equals(owner.getClass().getName()))
                {
                if (this.data() != null)
                    {
                    return this.data().equals(value);
                    }
                else {
                    return (value == null);
                    }
                }
            }
        return false;
        }

    @Override
    public void sqlException(Object owner, Object value) throws SQLException
        {
        if (test(owner, value))
            {
            throw new SQLException(
                this.toString()
                ); 
            }
        }

    @Override
    public void sqlException(Object owner, Object value, String message) throws SQLException
        {
        if (test(owner, value))
            {
            throw new SQLException(
                message
                ); 
            }
        }
    }
