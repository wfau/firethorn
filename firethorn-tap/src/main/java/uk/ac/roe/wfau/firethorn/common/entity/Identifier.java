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
package uk.ac.roe.wfau.firethorn.common.entity ;

import java.io.Serializable;
import java.net.URI;

/**
 * Public interface for all identifiers.
 *
 */
public interface Identifier
    {

    /**
     * Access to the Serializable value.
     * @return
     *      The Serializable value.
     *
     */
    public Serializable value();

    /**
     * Generate a String representation of the identifier.
     * @return
     *      A String representation.
     *
     */
    public String toString();
    
    /**
     * Generate a URI representation of the identifier.
     * @return
     *      A URI representation.
     *
    public URI toUri();
     */
    
    }

