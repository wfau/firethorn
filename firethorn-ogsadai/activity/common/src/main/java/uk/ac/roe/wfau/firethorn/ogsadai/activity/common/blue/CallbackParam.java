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
package uk.ac.roe.wfau.firethorn.ogsadai.activity.common.blue;

import uk.ac.roe.wfau.firethorn.ogsadai.activity.common.base.TuplesParam;

/**
 * Common interface for the CallbackActivity.
 *
 */
public interface CallbackParam
extends TuplesParam
    {
    /**
     * The default Activity name, {@value}.
     * 
     */
    public static final String ACTIVITY_NAME = "uk.ac.roe.wfau.firethorn.Callback" ;

    /**
     * Activity input name for the query identifier.
     * 
     */
    public static final String QUERY_IDENT = "query.ident" ;

    /**
     * Public interface for a callback message bean.
     * 
     */
    public interface RequestBean
    	{
        /**
    	 * Get the query identifier.
    	 * 
    	 */
        public String getIdent();

        /**
    	 * Get the next {@link BlueTask.TaskState} as a String.
    	 * 
    	 */
        public String getTaskState();

        /**
    	 * Get the query row count.
    	 * 
    	 */
        public Long getCount();

        /**
         * The query {@link ResultState} as a String.
         * 
         */
        public String getResultState();

    	}

    /**
     * Public interface for a callback response bean.
     * 
     */
    public interface ResponseBean
    	{
        /**
    	 * Get the query identifier.
    	 * 
    	 */
        public String getIdent();

        /**
    	 * Get the query name.
    	 * 
    	 */
        public String getName();

        /**
    	 * Get the query {@link BlueTask.TaskState} as a String.
    	 * 
    	 */
        public String getTaskState();

        /**
    	 * Get the 'self' URL.
    	 * 
    	 */
        public String getSelf();

    	}
    }

