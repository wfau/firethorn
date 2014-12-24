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
package uk.ac.roe.wfau.firethorn.meta.ogsa;

import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.meta.base.BaseResource;

/**
 *
 *
 */
public interface OgsaBaseResource
    extends Entity
    {

    /**
     * The parent {@link OgsaService}.
     * @return The {@link OgsaService}.
     *
     */
    public OgsaService service();

    /**
     * The parent {@link BaseResource}.
     * @return The {@link BaseResource}.
     *
     */
    public BaseResource<?> source();

    /**
     * The OGSA-DAI resource identifier.
     * @return The identifier
     *
     */
    public String ogsaid();

    /**
     * OGSA-DAI resource status.
     *
     */
    public static enum Status
        {
        CREATED(),
        ACTIVE(),
        INACTIVE(),
        ERROR(),
        UNKNOWN();
        }

    /**
     * Get the resource status.
     * @return The resource status.
     *
     */
    public Status status();

    /**
     * Check the resource status.
     * @return The resource status.
     *
     */
    public Status ping();
    
    }
