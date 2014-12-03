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

package uk.org.ogsadai.dqp.lqp.operators.extra;

import java.util.List;

import uk.org.ogsadai.dqp.lqp.HeadingImpl;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.OperatorVisitor;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.operators.SetOperator;

/**
 * Operator OUTER UNION.
 * 
 * TODO:Incomplete implementation.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class OuterUnionOperator extends SetOperator
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE =
            "Copyright (c) The University of Edinburgh, 2008";

    /** Not a bag operator. */
    private boolean mBagOperator = false;

    /** Parameter list. */
    private List<String> mParameterList;
    
    /**
     * Constructs a new operator.
     */
    protected OuterUnionOperator()
    {
        mID = OperatorID.getInstance("OUTER_UNION");
    }
    
    /**
     * Constructor.
     * 
     * @param parameterList
     *            a list function parameters
     */
    public OuterUnionOperator(List<String> parameterList) 
    {
        this();
        mParameterList = parameterList;
    }
        
    /**
     * {@inheritDoc}
     */
    public void update() throws LQPException
    {
        mOperatorHeading = new HeadingImpl(mLeftChildOperator.getHeading());
        // TODO: proper
    }
    
    /**
     * {@inheritDoc}
     */
    public void validate() throws LQPException
    {
        super.validate();        
        // TODO: Check for union compatibility
    }
    
    /**
     * {@inheritDoc}
     */
    public void accept(OperatorVisitor visitor)
    {
        visitor.visit(this);
    }

}
