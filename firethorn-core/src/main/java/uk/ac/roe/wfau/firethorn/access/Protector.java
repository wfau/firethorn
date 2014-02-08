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
 * Public interface for an Entity Protector.
 *
 */
public interface Protector
    {

    /**
     * The list of Actions that this Protector handles. 
     *  
     */
    public Iterable<Action> actions(); 
    
    /**
     * Ask if an Identity is allowed to perform an Action on the protected object.
     * 
     * @param identity
     * @param action
     * @returns true if the Identity is allowed to perform the Action. 
     *
     */
    public boolean allow(final Identity identity, final Action action); 

    /**
     * Check that an Identity is allowed to perform an Action on the protected object.
     * 
     * @param identity
     * @param action
     * @throws ProtectorException if the Identity is NOT allowed to perform the Action. 
     * 
     */
    public Protector check(final Identity identity, final Action action)
    throws ProtectorException ; 

    }
