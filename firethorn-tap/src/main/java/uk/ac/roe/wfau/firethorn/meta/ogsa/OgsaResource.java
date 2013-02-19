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
package uk.ac.roe.wfau.firethorn.meta.ogsa;

import uk.ac.roe.wfau.firethorn.meta.base.BaseResource;
import uk.ac.roe.wfau.firethorn.meta.base.BaseSchema;

/**
 *
 *
 */
public interface OgsaResource<SchemaType extends BaseSchema<SchemaType,?>>
extends BaseResource<SchemaType>
    {
	/**
	 * Get the OGSA-DAI resource ID.
	 *
	public String ogsaid();
     */

	/**
	 * Set the OGSA-DAI resource ID.
	 *
	public void ogsaid(final String ogsaid);
     */
    }
