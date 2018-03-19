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

import java.util.List;

import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.exceptions.AttributeNotFoundException;
import uk.org.ogsadai.dqp.lqp.exceptions.TableNotFoundException;

/**
 * Interface for table scan queries that implement SELECT, PROJECT and JOIN
 * operations.
 * 
 * @author The OGSA-DAI Project Team
 */
public interface SelectProjectJoinTableScanQuery extends TableScanQuery 
{

    public void setProjectAttributes(List<Attribute> attributes);

    /**
     * Sets the expressions and attributes to the included in the PROJECT list.
     * 
     * @param expressions
     *            project expressions to the added to the PROJECT part of the
     *            query.
     */
    public void setProjectExpressions(List<ProjectExpression> expressions);

    /**
     * Merges the given query into this query to add a JOIN operation to the
     * query.
     * 
     * @param query
     *            query to merge. The concrete class of parameter will be the
     *            same as the implementation class.
     * 
     * @throws TableNotFoundException
     *             if the query argument specifies a table that is not in the
     *             data dictionary.
     * @throws AttributeNotFoundException
     *             if the query specifies an attribute that is not in the data
     *             dictionary
     */
    public void merge(SelectProjectJoinTableScanQuery query)
            throws TableNotFoundException, AttributeNotFoundException;
}
