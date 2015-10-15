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
package uk.ac.roe.wfau.firethorn.ogsadai.activity.common.data;

import uk.ac.roe.wfau.firethorn.ogsadai.activity.common.base.TuplesParam;

/**
 * Common interface for the LimitsActivity.
 *
 */
public interface LimitsParam
extends TuplesParam
    {
    /**
     * The default Activity name, {@value}.
     * 
     */
    public static final String ACTIVITY_NAME = "uk.ac.roe.wfau.firethorn.Limits" ;

    /**
     * Activity input name for the row limit.
     * 
     */
    public static final String ROW_LIMIT  = "limit.rows"  ;

    /**
     * Default row limit (0 = no limit).
     * 
     */
    public static final Long DEFAULT_ROWS = new Long(0);
    
    /**
     * Activity input name for the cell limit.
     * 
     */
    public static final String CELL_LIMIT = "limit.cells" ;

    /**
     * Default cell limit (0 = no limit).
     * 
     */
    public static final Long DEFAULT_CELLS = new Long(0);
    
    /**
     * Activity input name for the time limit.
     * 
     */
    public static final String TIME_LIMIT = "limit.time"  ;

    /**
     * Default time limit (0 = no limit).
     * 
     */
    public static final Long DEFAULT_TIME = new Long(0);
    
    }

