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
package uk.ac.roe.wfau.firethorn.ogsadai.activity.client.blue;

import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.WorkflowResult;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.data.DelaysClient;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.data.LimitsClient;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.data.RownumClient;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.jdbc.JdbcInsertDataClient;

/**
 * OGSA-DAI workflow to process a query.
 *
 */
public interface BlueWorkflow
    {
    /**
     * Workflow parameters.
     * 
     */
    public interface Param
    	{
    	/**
    	 * The data source identifier.
    	 * 
    	 */
    	public String source();
        
    	/**
    	 * The ADQL/SQL query.
    	 * 
    	 */
    	public String query();

        public RownumClient.Param rows();
        
        public JdbcInsertDataClient.Param insert();
        
        public DelaysClient.Param  delays();

        public LimitsClient.Param limits();
    	
    	}
    
    /**
     * Execute the workflow. 
     * 
     * @param param The workflow parameters
     * @return The {@link WorkflowResult} result.
     *
     */
    public WorkflowResult execute(final Param param);

    }

