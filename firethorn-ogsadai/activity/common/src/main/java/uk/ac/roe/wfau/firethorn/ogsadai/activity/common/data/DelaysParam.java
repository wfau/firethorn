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

/**
 * Parameter names for the DelayActivity.
 *
 */
public class DelaysParam
    {

    /**
     * Parameter name for the delay before the first row.
     * 
     */
    public static final String FIRST_DELAY = "delay.first" ;

    /**
     * Default delay before the first row (0 = no delay).
     * 
     */
    public static final Integer DEFAULT_FIRST = new Integer(0);

    /**
     * Parameter name for the delay after the last row.
     * 
     */
    public static final String LAST_DELAY  = "delay.last"  ;

    /**
     * Default delay after the last row (0 = no delay).
     * 
     */
    public static final Integer DEFAULT_LAST = new Integer(0);
    
    /**
     * Parameter name for the delay between every row.
     * 
     */
    public static final String EVERY_DELAY = "delay.every" ;

    /**
     * Default delay for every row (0 = no delay).
     * 
     */
    public static final Integer DEFAULT_EVERY = new Integer(0);

    /**
     * Parameter name for the input tuples.
     * 
     */
    public static final String TUPLE_INPUT  = "tuples" ;

    /**
     * Parameter name for the output tuples.
     * 
     */
    public static final String TUPLE_OUTPUT = "tuples" ;

    /**
     * Default Activity name.
     * 
     */
    public static final String ACTIVITY_NAME = "uk.ac.roe.wfau.firethorn.Delays" ;

    }

