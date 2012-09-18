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
package uk.ac.roe.wfau.firethorn.common.entity ;

import uk.ac.roe.wfau.firethorn.common.entity.exception.IdentifierFormatException ;

/**
 * Identifier based on a Long value.
 *
 */
public class LongIdentifier
extends AbstractIdentifier<Long>
    {

    public LongIdentifier(final String string)
        {
        this(
            parse(
                string
                )
            );
        }

    public LongIdentifier(final int value)
        {
        super(
            new Long(
                value
                )
            ) ;
        }

    public LongIdentifier(final long value)
        {
        super(
            new Long(
                value
                )
            ) ;
        }

    public LongIdentifier(final Long value)
        {
        super(value) ;
        }

    /**
     * Parse a string.
     *
     */
    public static Long parse(final String string)
        {
        try {
            return Long.valueOf(
                string
                );
            }
        catch (final NumberFormatException ouch)
            {
            throw new IdentifierFormatException(
                string,
                ouch
                );
            }
        }

    /**
     * Create an Identifier from a string.
     *
     */
    public static Identifier create(final String string)
        {
        try {
            return new LongIdentifier(
                LongIdentifier.parse(
                    string
                    )
                );
            }
        catch (final NumberFormatException ouch)
            {
            throw new IdentifierFormatException(
                string,
                ouch
                );
            }
        }
    }

