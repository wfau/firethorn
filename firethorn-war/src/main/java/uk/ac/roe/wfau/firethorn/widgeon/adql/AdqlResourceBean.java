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
package uk.ac.roe.wfau.firethorn.widgeon.adql;

import uk.ac.roe.wfau.firethorn.widgeon.data.DataComponentBean;
import uk.ac.roe.wfau.firethorn.widgeon.data.DataComponentBeanImpl;

/**
 * Bean wrapper for <code>JdbcResource</code>.
 *
 */
public class AdqlResourceBean
extends DataComponentBeanImpl<AdqlResource>
implements DataComponentBean<AdqlResource>
    {
    /**
     * Public constructor.
     *
     */
    public AdqlResourceBean(final AdqlResource entity)
        {
        super(
            AdqlResourceIdentFactory.TYPE_URI,
            entity
            );
        }

    }
