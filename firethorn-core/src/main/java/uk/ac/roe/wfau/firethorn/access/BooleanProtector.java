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
package uk.ac.roe.wfau.firethorn.access;

import uk.ac.roe.wfau.firethorn.identity.Identity;

/**
 * A simple boolean based Protector.
 *
 */
public class BooleanProtector
extends AbstractProtector
implements Protector
    {

    /**
     * Public constructor.
     * 
     */
    public BooleanProtector(boolean value)
        {
        this.value = value ;
        }
    private boolean value ;

    @Override
    public boolean allow(Identity identity, Action action)
        {
        return this.value;
        }
    }
