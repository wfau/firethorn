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

import java.util.List;

import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.HeadingImpl;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;

/**
 * Abstract base class for set operators.
 * 
 * @author The OGSA-DAI Project Team.
 */
public abstract class SetOperator extends BinaryOperator
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /** Not a bag operator. */
    protected boolean mBagOperator = false;

    /**
     * Checks if operator is a bag operator.
     * 
     * @return <code>true</code> if operator is a bag operator,
     *         <code>false</code> if it is a set operator
     */
    public boolean isBagOperator()
    {
        return mBagOperator;
    }

    /**
     * {@inheritDoc}
     */
    public void update() throws LQPException
    {
        super.update();
        // One of the heads
        mOperatorHeading = new HeadingImpl(mLeftChildOperator.getHeading());
    }

    /**
     * {@inheritDoc}
     */
    public void validate() throws LQPException
    {
        super.validate();
        
        List<Attribute> lAttr = mLeftChildOperator.getHeading().getAttributes();
        List<Attribute> rAttr = mRightChildOperator.getHeading()
            .getAttributes();
        
        if(lAttr.size() != rAttr.size())
        {
            throw new LQPException("Relations incompatible. Number of attributes does not match");
        }
        
        for(int i=0; i<lAttr.size(); i++)
        {
            if(!lAttr.get(i).equals(rAttr.get(i)))
            {
                throw new LQPException(
                    "Relations incompatibilie. Attributes do not match at position: "
                        + i);
            }
        }
    }

}
