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

import uk.org.ogsadai.dqp.lqp.HeadingImpl;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.OperatorVisitor;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.udf.LogicalFunction;

/**
 * Operator UNARY REL FUNCTION.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class UnaryRelFunctionOperator extends UnaryOperator
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE =
        "Copyright (c) The University of Edinburgh, 2008";
    
    /** Encapsulated function. */
    private LogicalFunction mRelFunction;

    /**
     * Constructor.
     */
    private UnaryRelFunctionOperator()
    {
        mID = OperatorID.UNARY_REL_FUNCTION;
    }

    /**
     * Constructs a disconnected unary REL_FUNCTION operator.
     * 
     * @param logicalFunction
     *            encapsulated logical function
     */
    public UnaryRelFunctionOperator(LogicalFunction logicalFunction)
    {
        this();
        mRelFunction = logicalFunction;
    }

    /**
     * {@inheritDoc}
     */
    public void setChild(int position, Operator child)
    {
        super.setChild(position, child);
        
        mRelFunction.setInputHeading(position, child.getHeading());
    }
    
    /**
     * {@inheritDoc}
     */
    public void update() throws LQPException
    {
        super.update();
        
        if (mChildOperator != null)
        {
            mRelFunction.setInputHeading(0, mChildOperator.getHeading());
        }
        mOperatorHeading = new HeadingImpl(mRelFunction.getHeading());
        
        // TODO: ask function object
        mOperatorHeading.invalidateKeys();
    }
    
    /**
     * {@inheritDoc}
     */
    public void validate() throws LQPException
    {
        super.validate();
        mRelFunction.setInputHeading(0, mChildOperator.getHeading());
        
        mRelFunction.validate();
    }

    /**
     * {@inheritDoc}
     */
    public void accept(OperatorVisitor visitor)
    {
        visitor.visit(this);
    }

}
