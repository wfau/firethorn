// Copyright (c) The University of Edinburgh, 2008-2009.
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

import java.util.Set;

import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.Heading;
import uk.org.ogsadai.dqp.lqp.exceptions.AttributeNotFoundException;
import uk.org.ogsadai.dqp.lqp.exceptions.TableNotFoundException;
import uk.org.ogsadai.dqp.lqp.udf.FunctionRepository;

/**
 * This interface provides access to the data dictionary describing all tables
 * and their types that a DQP resource manages.
 * 
 * @author The OGSA-DAI Project Team.
 */
public interface DataDictionary
{
    /**
     * Returns the operator heading produced by a table scan of the given table.
     * 
     * @param tableName
     *            name of the table
     * @return operator heading containing types and names of attributes
     * 
     * @throws TableNotFoundException
     *      if the data dictionary does not contain the given table.
     */
    public Heading getHeading(String tableName) throws TableNotFoundException;

    /**
     * Returns the attribute produced by a table scan of the given table.
     * 
     * @param attribute
     *            attribute with name and source
     * @return attribute with the correct types
     * 
     * @throws TableNotFoundException
     *      if the data dictionary does not contain the given table.
     */
    public Attribute getAttribute(Attribute attribute) 
        throws AttributeNotFoundException;

    /**
     * Returns the table schema of the given table.
     * 
     * @param tableName
     *            name of the table
     * @return table schema
     * 
     * @throws TableNotFoundException
     *      if the data dictionary does not contain the given table.
     */
    public TableSchema getTableSchema(String tableName)
        throws TableNotFoundException;

    /**
     * Returns the set of table schemas available.
     * 
     * @return set of table schemas
     */
    public Set<TableSchema> getTableSchemas();

    /**
     * Returns function repository providing information on functions available
     * to this federation.
     * 
     * @return function repository, or <tt>null</tt> if functions are not
     *         supported.
     */
    public FunctionRepository getFunctionRepository();

    /**
     * Returns the original table name on the data node.
     * 
     * @param table
     *            federation table name
     * @param dataNode
     *            data node
     * @return original table name
     * @throws TableNotFoundException
     *             if the data dictionary does not contain the given table
     */
    public String getOriginalTableName(String table, DataNode dataNode) 
        throws TableNotFoundException;
}
