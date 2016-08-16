/**
 * Copyright (c) 2014, ROE (http://www.roe.ac.uk/)
 * All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package uk.ac.roe.wfau.firethorn.ogsadai.activity.common.ivoa;

/**
 * Common interface for the IvoaSelectData Activity.
 *
 */
public interface IvoaSelectDataParam
extends IvoaResourceParam
    {
    /**
     * The default Activity name, {@value}.
     * 
     */
    public static final String ACTIVITY_NAME = "uk.ac.roe.wfau.firethorn.IvoaSelectData" ;

    /**
     * Activity input name for the ADQL query, {@value}.
     * 
     */
    public static final String IVOA_TAP_ADQL_QUERY_PARAM = "expression"  ;

    /**
     * Activity output name for the output tuples, {@value}.
     * 
     */
    public static final String TUPLE_OUTPUT = "data" ;
    
    
    }

