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

import java.util.ArrayList;
import java.util.List;

import uk.ac.roe.wfau.firethorn.identity.Authentication;
import uk.ac.roe.wfau.firethorn.identity.Identity;

/**
 * An abstract base class for Protector implementations.
 *
 */
public abstract class AbstractProtector
implements Protector
    {

    /**
     * Check if an {@link Identity} is allowed to perform an {@linkl Action}.
     * @param identity The {@link Identity}.
     * @param action The {@link Action}.
     * @return True if the {@link Identity} is allowed to perform the {@linkl Action}.
     * 
     */
    abstract
    public boolean check(final Identity identity, final Action action);

    @Override
    public boolean check(final Authentication authentication, final Action action)
        {
        return check(
            authentication.identity(),
            action
            );
        }

    public Protector accept(final Identity identity, final Action action)
    throws ProtectorException
        {
        if (this.check(identity, action))
            {
            return this;
            }
        else {
            throw new ProtectorException(
                identity,
                action
                );
            }
        }

    @Override
    public Protector accept(final Authentication authentication, final Action action)
    throws ProtectorException
        {
        return accept(
            authentication.identity(),
            action
            );
        }
    }
