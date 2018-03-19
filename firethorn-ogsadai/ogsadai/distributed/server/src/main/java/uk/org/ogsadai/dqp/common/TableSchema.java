// Copyright (c) The University of Edinburgh, 2008-2012.
//
// LICENCE-START
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// LICENCE-END

package uk.org.ogsadai.dqp.common;

import java.util.List;

/**
 * Contains information about a table in a federation.
 *
 * @author The OGSA-DAI Project Team.
 */
public interface TableSchema
{
    /**
     * Returns details of the table on all the data nodes that that access the
     * table.
     * 
     * @return details of how the table is accessed on each of the data nodes.
     */
    public List<DataNodeTable> getDataNodeTables();

    /**
     * Returns details of the table on the specified data node.
     * 
     * @param dataNode data node of interest.  This must be one of the data
     *      node returned by the <code>getDataNodeTables</code> method.
     * 
     * @return details of how the table is accessed on the specified data node.
     */
    public DataNodeTable getDataNodeTable(DataNode dataNode);
    
    /**
     * Returns the federation table name.
     * 
     * @return table name
     */
    public String getTableName();

    /**
     * Returns the schema of the table in the federation.
     * 
     * @return table schema
     */
    public LogicalSchema getSchema();

    /**
     * Returns the physical schema of the table in the federation.
     * 
     * @return physical table schema or <code>null</code> when non existent.
     */
    public PhysicalSchema getPhysicalSchema();
}
