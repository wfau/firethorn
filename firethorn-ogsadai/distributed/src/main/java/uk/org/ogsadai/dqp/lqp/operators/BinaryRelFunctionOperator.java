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
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.OperatorVisitor;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.udf.LogicalFunction;

/**
 * Generic operator encapsulating binary relation valued functions.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class BinaryRelFunctionOperator extends BinaryOperator
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /** Encapsulated function. */
    private LogicalFunction mRelFunction;

    /**
     * Constructor.
     */
    protected BinaryRelFunctionOperator()
    {
        mID = OperatorID.BINARY_REL_FUNCTION;
    }

    /**
     * Creates a disconnected BINARY REL FUNCTION operator.
     * 
     * @param logicalFunction
     *            encapsulated function
     */
    public BinaryRelFunctionOperator(LogicalFunction logicalFunction)
    {
        this();
        mRelFunction = logicalFunction;
    }

    /**
     * {@inheritDoc}
     */
    public void update() throws LQPException
    {
        super.update();

        if (mLeftChildOperator != null)
        {
            mRelFunction.setInputHeading(0, mLeftChildOperator.getHeading());
        }

        if (mRightChildOperator != null)
        {
            mRelFunction.setInputHeading(1, mRightChildOperator.getHeading());
        }
        mOperatorHeading = new HeadingImpl(mRelFunction.getHeading());
    }

    /**
     * {@inheritDoc}
     */
    public void validate() throws LQPException
    {
        super.validate();

        mRelFunction.setInputHeading(0, mLeftChildOperator.getHeading());
        mRelFunction.setInputHeading(1, mRightChildOperator.getHeading());

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
