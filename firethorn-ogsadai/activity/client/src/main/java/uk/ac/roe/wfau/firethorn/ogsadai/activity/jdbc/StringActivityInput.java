/*
 *  Copyright (C) 2014 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.ogsadai.activity.jdbc;

import uk.org.ogsadai.client.toolkit.activity.SimpleActivityInput;
import uk.org.ogsadai.data.StringData;


/**
 *
 *
 */
public class StringActivityInput
    extends SimpleActivityInput
    {
    /**
     * Public constructor.
     * @param name
     *
     */
    public StringActivityInput(final String name)
        {
        this(
            name,
            null,
            false
            );
        }

    /**
     * Public constructor.
     * @param name
     * @param optional
     *
     */
    public StringActivityInput(final String name, final boolean optional)
        {
        this(
            name,
            null,
            optional
            );
        }

    /**
     * Public constructor.
     * @param name
     * @param value
     *
     */
    public StringActivityInput(final String name, final String value)
        {
        this(
            name,
            value,
            false
            );
        }

    /**
     * Public constructor.
     * @param name
     * @param value
     * @param optional
     *
     */
    public StringActivityInput(final String name, final String value, final boolean optional)
        {
        super(
            name,
            optional
            );
        this.add(
            new StringData(
                value
                )
            );
        }
    }
