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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.OperatorVisitor;
import uk.org.ogsadai.dqp.lqp.Predicate;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.expression.ExpressionUtils;

/**
 * Operator FULL OUTER JOIN.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class FullOuterJoinOperator extends AbstractJoinOperator
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /**
     * Default constructor.
     */
    protected FullOuterJoinOperator()
    {
        mID = OperatorID.FULL_OUTER_JOIN;
    }

    /**
     * Creates a disconnected FULL OUTER JOIN operator.
     * 
     * @param predicate
     *            join predicate
     */
    public FullOuterJoinOperator(Predicate predicate)
    {
        this();
        mPredicate = predicate;

        mUsedAttributes = ExpressionUtils.getUsedAttributes(mPredicate
            .getExpression());
    }
    
    /**
     * {@inheritDoc}
     */
    public void accept(OperatorVisitor visitor)
    {
        visitor.visit(this);
    }

    /**
     * {@inheritDoc}
     */
    public void update() throws LQPException
    {
        super.update();
        
	// Check if there are ambiguities in the merged heading
	Set<Attribute> attrSet = new HashSet<Attribute>();
	for (Attribute attr : mOperatorHeading.getAttributes())
	{
	    attrSet.add(attr);
	}

	if (!mOperatorHeading.containsAllUnambiguous(new ArrayList<Attribute>(
		attrSet)))
	{
	    throw new LQPException(
		    "Ambiguous attributes in the heading of a JOIN operator");
	}

        mOperatorHeading.invalidateKeys();
    }
}
