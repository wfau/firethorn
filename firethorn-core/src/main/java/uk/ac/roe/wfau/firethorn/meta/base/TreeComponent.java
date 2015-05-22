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
package uk.ac.roe.wfau.firethorn.meta.base;


/**
 * Base class interface for a metadata component.
 *
 */
public interface TreeComponent
extends BaseComponent
    {

    /**
     * {@link BaseComponent.EntityFactory} interface.
     *
     */
    public static interface EntityFactory<ComponentType extends BaseComponent>
    extends BaseComponent.EntityFactory<ComponentType>
        {
        }

    /**
     * The persistence level, or copy depth.
     *
     */
    enum CopyDepth
        {
        PROXY(),
        THIN(),
        PARTIAL(),
        FULL();
        }

    /**
     * The copy depth.
     *
     */
    public CopyDepth depth();

    /**
     * The copy depth.
     *
     */
    public void depth(final CopyDepth depth);

    }
