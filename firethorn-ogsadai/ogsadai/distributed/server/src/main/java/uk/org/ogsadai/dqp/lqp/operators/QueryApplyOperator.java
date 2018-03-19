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

import java.util.Map;
import java.util.Set;

import uk.org.ogsadai.dqp.lqp.Annotation;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.OperatorVisitor;

/**
 * Query apply operator is a special kind of APPLY which parameterises one query
 * branch with values from the other branch. It is different from conventional
 * APPLY in that the parameter set is extracted from the entire tuple stream and
 * not on a per tuple basis. The parameterised branch is executed only once. The
 * stream which provided parameters and the parameterised stream are joined
 * using the logic of an embedded binary operator. This operator is usually used
 * with a FILTERED_TABLE_SCAN operator.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class QueryApplyOperator extends ApplyOperator
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";
    
    private static final int DEFAULT_BATCH_SIZE = 2000;
    
    private int mBatchSize = DEFAULT_BATCH_SIZE;
    private String[] mAggregateNames;
    private String[] mAggregateExpressions;
    
    /**
     * Default constructor.
     */
    protected QueryApplyOperator()
    {
        mID = OperatorID.QUERY_APPLY;
    }

    /**
     * Constructor.
     * 
     * @param operator
     *            wrapped operator
     * @param attrToBind
     *            attributes to bind
     */
    public QueryApplyOperator(Operator operator,
        Set<Attribute> attrToBind)
    {
        this();
        mOperator = operator;
        Map<String, Object> annotations = ((AbstractOperator) operator).mAnnotations;
        for (String a : annotations.keySet())
        {
            // We don't want to inherit IMPLEMENTATION annotation
            if (!a.equals(Annotation.IMPLEMENTATION))
            {
                mAnnotations.put(a, annotations.get(a));
            }
        }
        mAttributesToBind = attrToBind;
    }

    /**
     * {@inheritDoc}
     */
    public void accept(OperatorVisitor visitor)
    {
        visitor.visit(this);
    }

    /**
     * Gets the batch size to be use for batch query execution.  This is
     * the maximum number of values that will be included in the IN clause of a
     * batch query.
     * 
     * @return batch size
     */
    public int getBatchSize()
    {
        return mBatchSize;
    }
    
    /**
     * Sets the batch size to be used for batch query execution.  This is
     * the maximum number of values that will be included in the IN clause of a
     * batch query.
     * 
     * @param batchSize batch size
     */
    public void setBatchSize(int batchSize)
    {
        mBatchSize = batchSize;
    }
    
    public void setAggregateExpressions(String[] aggregateExpressions)
    {
        mAggregateExpressions = aggregateExpressions;
    }
    
    public String[] getAggregateExpressions()
    {
        return mAggregateExpressions;
    }
    
    public void setAggregateNames(String[] aggregateNames)
    {
        mAggregateNames = aggregateNames;
        
    }

    public String[] getAggregateNames()
    {
        return mAggregateNames;
    }
}
