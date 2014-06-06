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

import uk.ac.roe.wfau.firethorn.identity.Identity;

/**
 * An abstract base class for Protector implementations.
 *
 */
public abstract class AbstractProtector
implements Protector
    {

    private List<Action> actions = new ArrayList<Action>();
    protected void add(final Action action)
        {
        this.actions.add(
            action
            );
        }
    @Override
    public Iterable<Action> actions()
        {
        return this.actions;
        }

    @Override
    public Protector accept(final Identity identity, final Action action)
        throws ProtectorException
        {
        if (this.allow(identity, action))
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
    }
