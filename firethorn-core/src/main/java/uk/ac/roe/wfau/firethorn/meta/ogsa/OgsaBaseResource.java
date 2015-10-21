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

import uk.ac.roe.wfau.firethorn.meta.base.BaseComponent;
import uk.ac.roe.wfau.firethorn.meta.base.BaseResource;

/**
 *
 *
 */
public interface OgsaBaseResource
    extends BaseComponent
    {

    /**
     * {@link OgsaBaseResource.EntityFactory} interface.
     *
     */
    public static interface EntityFactory<ComponentType extends OgsaBaseResource>
    extends BaseComponent.EntityFactory<ComponentType>
        {
        }

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
    public BaseResource<?> resource();

    /**
     * Get the OGSA-DAI resource identifier.
     * @return The identifier
     *
     */
    public String ogsaid();

    /**
     * Set the OGSA-DAI resource identifier.
     * @return The resource status.
     *
     */
    public OgsaStatus ogsaid(final OgsaStatus status, final String ogsaid);

    /**
     * OGSA-DAI resource status.
     *
     */
    public static enum OgsaStatus
        {
        CREATED(true),
        ACTIVE(true),
        INACTIVE(false),
        ERROR(false),
        UNKNOWN(false);
        
        private boolean active ;
        public boolean active()
            {
            return this.active;
            }
        
        private OgsaStatus(boolean active)
            {
            this.active = active ;
            }
        }

    /**
     * Get the resource status.
     * @return The resource status.
     *
     */
    public OgsaStatus ogstatus();

    /**
     * Set the resource status.
     * @return The resource status.
     *
     */
    public OgsaStatus ogstatus(final OgsaStatus status);

    }
