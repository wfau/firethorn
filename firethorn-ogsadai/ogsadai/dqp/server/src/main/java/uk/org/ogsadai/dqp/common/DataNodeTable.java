// Copyright (c) The University of Edinburgh, 2009.
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

/**
 * Details relating to a table on a single data node.
 *
 * @author The OGSA-DAI Project Team
 */
public interface DataNodeTable
{
    /**
     * Returns the data node that accesses the table.
     * 
     * @return data node
     */
    public DataNode getDataNode();
    
    /**
     * Returns the original table name.
     * 
     * @return table name
     */
    public String getOriginalTableName();
}
