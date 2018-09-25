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

import java.util.Collections;
import java.util.List;

/**
 * Modify table names to meet the ADQL specification.  
 * 
 */
public class AdqlNameModifier
    {

    /**
     * Public constructor.
     * 
     */
    public AdqlNameModifier()
        {
        }

    /**
     * Public constructor.
     * 
     */
    public AdqlNameModifier(final List<String> reserved)
        {
        this.reserved = reserved ;
        }

    /**
     * Our list of reserved names.
     * 
     */
    protected List<String> reserved = Collections.emptyList();

    /**
     * Check for a reserved name.
     * 
     */
    public boolean isReserved(final String name)
        {
        return this.reserved.contains(name);
        }

    /**
     * Regular expression for special characters.
     * 
     */
    protected final String SPECIALS = "a-zA-Z0-9_" ;
    
    /**
     * Check for special characters.
     * 
     */
    public boolean hasSpecials(final String name)
        {
        return name.matches(
            ".*[^" + SPECIALS + "]+.*"
            );
        }
    
    /**
     * Escape a reserved name.
     * 
     */
    public String escape(final String name)
        {
        return "\"" + name.replace("\"", "\\\"") + "\"";
        }
    
    /**
     * Check if a name is reserved and escape it.
     * 
     */
    public String process(final String name)
        {
        if (isReserved(name))
            {
            return escape(name);
            }
        else if (hasSpecials(name))
            {
            return escape(name);
            }
        else {
            return name;
            }
        }
    }
