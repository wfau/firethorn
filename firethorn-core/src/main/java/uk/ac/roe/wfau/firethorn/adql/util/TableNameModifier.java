/*
 *  Copyright (C) 2018 Royal Observatory, University of Edinburgh, UK
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

package uk.ac.roe.wfau.firethorn.adql.util;

import java.util.Arrays;
import java.util.List;

/**
 * Modify table names to escape ADQL reserved names.  
 * 
 */
public class TableNameModifier
extends AdqlNameModifier
    {

    /**
     * Global array of reserved names.
     * 
     */
    protected static final List<String> RESERVED_NAMES = Arrays.asList(
        "first",
        "diagnostics",
        "match",
        "region",
        "zone"
        );

    /**
     * Public constructor.
     * 
     */
    public TableNameModifier()
        {
        super(RESERVED_NAMES);
        }
    }
