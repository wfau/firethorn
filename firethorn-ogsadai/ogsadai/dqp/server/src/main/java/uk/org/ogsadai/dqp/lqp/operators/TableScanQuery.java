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

package uk.org.ogsadai.dqp.lqp.operators;

import uk.org.ogsadai.dqp.common.DataDictionary;
import uk.org.ogsadai.dqp.common.DataNode;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.Heading;
import uk.org.ogsadai.dqp.lqp.Predicate;
import uk.org.ogsadai.dqp.lqp.exceptions.AttributeNotFoundException;
import uk.org.ogsadai.dqp.lqp.exceptions.TableNotFoundException;

/**
 * Interface for an SQL query associated with a table scan operator.  
 *
 * @author The OGSA-DAI Project Team
 */
public interface TableScanQuery
{
    /**
     * Sets the name of the table to be scanned.
     * 
     * @param tableName table name in the federation schema.
     */
    public void setTableName(String tableName);
    
    /**
     * Sets the data dictionary.
     * 
     * @param dataDictionary data dictionary.
     */
    public void setDataDictionary(DataDictionary dataDictionary);
    
    /**
     * Gets the table name (in the federation schema) of the original table 
     * associated with the table scan operator.
     * <p>
     * If more operators are imploded into the query then the query may be 
     * associated with more than one table.  This method will always return the
     * name of that first table.
     * 
     * @return table name (in federation schema) of the original table being
     *         scanned.
     */
    public String getTableName();

    /**
     * Gets the heading returned by the query. The <code>setTableName</code> and
     * <code>setDataDictionary</code> methods must be called before this method
     * is called.
     * 
     * @return the heading returning by the query.
     * 
     * @throws TableNotFoundException
     *             if the query contains a table that is not in the data
     *             dictionary.
     * @throws AttributeNotFoundException
     *             if the query contains an attribute that is not in the data
     *             dictionary
     * 
     */
    public Heading getHeading()
        throws TableNotFoundException, AttributeNotFoundException;

    /**
     * Gets the SQL query for the table scan operator in the federation schema.
     * <p>
     * The query in the federation schema is not required for anything other
     * than display purposes when the physical database schema query should
     * remain hidden.
     * 
     * @return SQL query for the table scan in the federation schema.
     */
    public String getSQLQuery();

    /**
     * Gets the SQL query for the table scan operator in the physical database
     * schema.
     * 
     * @param dataNode
     *            data node on which the query will be executed.
     * 
     * @return the SQL query in the physical database schema.
     */
    public String getPhysicalDatabaseSQLQuery(DataNode dataNode);

    /**
     * Gets the physical name of the given attribute. The physical name is the
     * name that will be used in the query sent to the physical database.
     * 
     * @param attribute
     *            logical attribute name
     * @param dataNode
     *            data node on which the query will be executed
     * 
     * @return the physical attribute name (including source where possible), or
     *         <code>null</code> if the attribute does not match.
     */
    public Attribute getPhysicalAttribute(Attribute attribute, DataNode dataNode);

    /**
     * Return whether this query supports being used as part of a filtered table
     * scan. Filtered table scan requires adding extra 'where' clauses to the
     * query at execution time. It is hard to support this with very complex
     * queries so this flag indicates if the filter table scan strategy can be
     * used.
     * 
     * @return <code>true</code> if filtered table scan strategy can be used,
     *         <code>false</code> otherwise.
     */
    public boolean supportsFilteredTableScan();

    /**
     * Adds a predicate to the WHERE clause of the outer query. If the query has
     * multiple predicates they will be combined using an AND operator.
     * 
     * @param predicate
     *            predicate to be added to the query
     */
    public void addPredicate(Predicate predicate);

    /**
     * Sets the sort attribute. The generated query will have an ORDER BY clause
     * on the sort attribute.
     * 
     * @param sortAttribute
     *            sort attribute
     * @throws UnsupportedOperationException
     */
    public void setSort(Attribute sortAttribute)
        throws UnsupportedOperationException;
}
