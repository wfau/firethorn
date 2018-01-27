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

import uk.ac.roe.wfau.firethorn.identity.Authentication;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.identity.Operation;

/**
 * Public interface to protect an {@link Entity}.
 *
 */
public interface Protector
    {

    /**
     * Check if the {@link Identity} from the current {@link Authentication} is allowed to perform an {@link Action} on the protected {@link Entity}.
     * @param action The {@Action} to check for.
     * @returns true if the Identity from the {@link Authentication} is allowed to perform an {@link Action}. 
     *
     */
    public boolean check(final Action action); 

    /**
     * Check if an {@link Identity} is allowed to perform an {@link Action} on the protected {@link Entity}.
     * @param identity The {@link Identity} to check.
     * @param action The {@Action} to check for.
     * @returns true if the Identity from the {@link Authentication} is allowed to perform an {@link Action}. 
     *
     */
    public boolean check(final Identity identity, final Action action); 

    /**
     * Check if the {@link Identity} from an {@link Authentication} is allowed to perform an {@link Action} on the protected {@link Entity}.
     * @param authentication The {@link Authentication} to check.
     * @param action The {@Action} to check for.
     * @returns true if the Identity from the {@link Authentication} is allowed to perform an {@link Action}. 
     *
     */
    public boolean check(final Authentication authentication, final Action action); 

    /**
     * Check if an {@link Authentication} from an {@link Iterable} of {@link Authentication}s is allowed to perform an {@link Action} on the protected {@link Entity}.
     * @param authentications The {@link Iterable} of {@link Authentication}s to check.
     * @param action The {@Action} to check for.
     * @returns true if the Identity from the {@link Authentication} is allowed to perform an {@link Action}. 
     *
     */
    public boolean check(final Iterable<Authentication> authentications, final Action action); 

    /**
     * Check if one of an {@link Operation} {@link Authentication}s is allowed to perform an {@link Action} on the protected {@link Entity}.
     * @param authentications The {@link Operation} {@link Authentications} to check.
     * @param action The {@Action} to check for.
     * @returns true if the Identity from the {@link Authentication} is allowed to perform an {@link Action}. 
     *
     */
    public boolean check(final Operation.Authentications authentications, final Action action); 

    /**
     * Check if the {@link Identity} from the current {@link Authentication} is allowed to perform an {@link Action} on the protected {@link Entity}.
     * @param authentication The {@link Authentication} to check.
     * @param action The {@Action} to check for.
     * @return A reference to the {@link Protector) that accepted the {@link Action}.
     * @throws {@link ProtectionException} if the {@link Identity} is NOT allowed to perform the {@link Action}. 
     * 
     */
    public Protector affirm(final Action action)
    throws ProtectionException ; 

    /**
     * Check that an {@link Identity} is allowed to perform an {@link Action}, or throw an {@link Exception}.
     * @param identity The {@link Identity} to check.
     * @param action The {@Action} to check for.
     * @return A reference to the {@link Protector) that accepted the {@link Action}.
     * @throws {@link ProtectionException} if the {@link Identity} is NOT allowed to perform the {@link Action}. 
     * 
     */
    public Protector affirm(final Identity identity, final Action action)
    throws ProtectionException ; 

    /**
     * Check that the {@link Identity} from an {@link Authentication} is allowed to perform an {@link Action}, or throw an {@link Exception}.
     * @param authentication The {@link Authentication} to check.
     * @param action The {@Action} to check for.
     * @return A reference to the {@link Protector) that accepted the {@link Action}.
     * @throws {@link ProtectionException} if the {@link Identity} is NOT allowed to perform the {@link Action}. 
     * 
     */
    public Protector affirm(final Authentication authentication, final Action action)
    throws ProtectionException ; 

    /**
     * Check if an {@link Authentication} from an {@link Iterable} of {@link Authentication}s is allowed to perform an {@link Action} on the protected {@link Entity}.
     * @param authentication The {@link Authentication} to check.
     * @param action The {@Action} to check for.
     * @return A reference to the {@link Protector) that accepted the {@link Action}.
     * @throws {@link ProtectionException} if the {@link Identity} is NOT allowed to perform the {@link Action}. 
     * 
     */
    public Protector affirm(final Iterable<Authentication> authentications, final Action action)
    throws ProtectionException ; 

    /**
     * Check if one of an {@link Operation} {@link Authentication}s is allowed to perform an {@link Action} on the protected {@link Entity}.
     * @param authentications The {@link Operation} {@link Authentications} to check.
     * @param action The {@Action} to check for.
     * @return A reference to the {@link Protector) that accepted the {@link Action}.
     * @throws {@link ProtectionException} if the {@link Identity} is NOT allowed to perform the {@link Action}. 
     * 
     */
    public Protector affirm(final Operation.Authentications authentications, final Action action)
    throws ProtectionException ; 
    
    }
