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
package uk.ac.roe.wfau.firethorn.ogsadai.metadata;

/**
 * An interface for a mapping between table alias (source) and the fully qualified table name and target resource.
 *
 *
 */
public interface TableMapping
    {
    /**
     * Get the table alias.
     * <br/> 
     * This is the table alias used in SQL queries passed into OGSA-DAI,
     * before the mapping from table alias to fully qualified resource table name.
     * @return The table alias.
     *
     */
    public String tableAlias();

    /**
     * Get the fully qualified table name (catalog.schema.table) in the target resource.  
     * @return The fully qualified table name.
     *
     */
    public String tableName();
    
    /**
     * Get the target resource identifier.  
     * @return The target resource identifier.
     *
     */
    public String resourceIdent();
    
    }