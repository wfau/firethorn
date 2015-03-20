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

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import uk.ac.roe.wfau.firethorn.entity.NamedEntity;

/**
 * Base class interface for a metadata component.
 *
 */
public interface TreeComponent
extends BaseComponent
    {

    /**
     * The persistence level, or depth of copy, of a entity.
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
