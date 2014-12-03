// Copyright (c) The University of Edinburgh, 2008.
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

import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.OperatorVisitor;
import uk.org.ogsadai.dqp.lqp.Predicate;

/**
 * Filtered table scan operator is used to represent table scans which are
 * parameterised during run time with attribute values or ranges coming from
 * another branch of the query plan (usually on join).
 * 
 * @author The OGSA-DAI Project Team.
 */
public class FilteredTableScanOperator extends TableScanOperator
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /** A list of predicate. */
    Predicate mPredicate;

    /**
     * Creates a new instance of the table scan operator. The operator head is
     * obtained from the physical schema stored in the data dictionary object.
     * 
     * @param tableScanOperator
     *            table scan operator to be replaced
     * @param predicate
     *            a filter predicate
     */
    public FilteredTableScanOperator(
        TableScanOperator tableScanOperator,
        Predicate predicate)
    {
        super(tableScanOperator.mQuery);
        mID = OperatorID.FILTERED_TABLE_SCAN;

        mDataDictionary = tableScanOperator.mDataDictionary;
        mAnnotations = tableScanOperator.mAnnotations;
        mPredicate = predicate;
    }

    /**
     * Returns a filter predicate.
     * 
     * @return filter predicate
     */
    public Predicate getPredicate()
    {
        return mPredicate;
    }

    /**
     * {@inheritDoc}
     */
    public void accept(OperatorVisitor visitor)
    {
        visitor.visit(this);
    }
}
