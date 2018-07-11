/*
 *  Copyright (C) 2015 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.webapp.blue;

import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryBase;
import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueQuery;
import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueTask;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.webapp.control.EntityBean;
import uk.ac.roe.wfau.firethorn.webapp.control.NamedEntityBeanImpl;

/**
 * A mini {@link EntityBean} wrapper for an {@link BlueQuery}.
 *
 */
public class BlueQueryMini
    extends NamedEntityBeanImpl<BlueQuery>
    {
    
    /**
     * Public constructor.
     * @param entity The {@link BlueQuery} to wrap.
     *
     */
    public BlueQueryMini(final BlueQuery entity)
        {
        super(
            BlueQuery.TYPE_URI,
            entity
            );
        }

    /**
     * Get the {@link BlueQuery} {@link AdqlResource} workspace.
     *
     */
    public String getResource()
        {
        if (entity().source() != null)
            {
            return entity().source().link();
            }
        return null ;
        }

    /**
     * The {@link BlueQuery} {@link AdqlQueryBase.Mode} mode.
     *
     */
    public AdqlQueryBase.Mode getMode()
        {
        return entity().mode();
        }

    /**
     * The {@link BlueQuery} {@link BlueTask.TaskState} status.
     * @throws ProtectionException 
     *
     */
    public BlueTask.TaskState getStatus()
    throws ProtectionException
        {
        return entity().state();
        }

    /**
     * The {@link BlueQuery} callback URL.
     * 
     */
    public String getCallback()
        {
        return entity().callback();
        }
    }
