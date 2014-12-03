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

import java.util.HashSet;
import java.util.Set;

import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.HeadingImpl;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.OperatorVisitor;
import uk.org.ogsadai.dqp.lqp.RenameMap;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;

/**
 * Operator APPLY. Used in correlated queries.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class ApplyOperator extends BinaryOperator
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE =
        "Copyright (c) The University of Edinburgh, 2008";

    /** Wrapped operator. */
    protected Operator mOperator;
    /** Attributes to bind. */
    protected Set<Attribute> mAttributesToBind;

    /**
     * Default constructor.
     */
    protected ApplyOperator()
    {
        mID = OperatorID.APPLY;
    }

    /**
     * Constructor.
     * 
     * @param operator
     *      operator to wrap
     * @param attributesToBind
     *      a collection of attributes to bind
     */
    public ApplyOperator(Operator operator, Set<Attribute> attributesToBind)
    {
        this();
        mOperator = operator;
        mAttributesToBind = attributesToBind;
    }
    
    @Override
    public void update() throws LQPException
    {
        super.update();
        
        mOperator.setChild(0, mLeftChildOperator);
        mOperator.setChild(1, mRightChildOperator);
        mOperator.update();
        
        mOperatorHeading = new HeadingImpl(mOperator.getHeading());
        
        setChild(0, mLeftChildOperator);
        setChild(1, mRightChildOperator);
        mOperator.disconnect();
        
        // TODO: This may depend on logic of the wrapped operator.
        mOperatorHeading.invalidateKeys();
    }
    
    @Override
    public Set<Attribute> getUsedAttributes() 
    {
        Set<Attribute> result = new HashSet<Attribute>();
        result.addAll(mOperator.getUsedAttributes());
        result.addAll(mAttributesToBind);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public void validate() throws LQPException
    {
        super.validate();
        // TODO: Validate by temporarily connecting wrapped operator?
    }

    /**
     * Returns the wrapped operator.
     * 
     * @return wrapped operator
     */
    public Operator getOperator() 
    {
        return mOperator;
    }

    /**
     * Returns a collection of attributes to bind.
     * 
     * @return collection of attributes
     */
    public Set<Attribute> getAttributesToBind() 
    {
        return mAttributesToBind;
    }
    
    public void renameAttributesToBind(RenameMap renameMap)
    {
        try
        {
            Set<Attribute> newAttrToBind = new HashSet<Attribute>(mAttributesToBind.size());
            for (Attribute attr : mAttributesToBind)
            {
                newAttrToBind.add(renameMap.getOriginalAttribute(attr));
            }
            mAttributesToBind = newAttrToBind;
        }
        catch (LQPException e)
        {
            // this shouldn't happen
            throw new RuntimeException("Unexpected exception", e);
        }

    }
    
    /**
     * {@inheritDoc}
     */
    public void accept(OperatorVisitor visitor)
    {
        visitor.visit(this);
    }
    
    /**
     * Gets the index of the child that is the outer relation. This is the child
     * that provides the values for the bound attributes and hence is the 
     * outer loop of the apply.
     * 
     * @return index of outer relation child
     */
    public int getOuterRelationChildIndex()
    {
        for (int i=0; i<2; ++i)
        {
            Operator child = getChild(i);
            
            if (child.getHeading().containsAllUnambiguous(mAttributesToBind))
            {
                return i;
            }
        }
        throw new RuntimeException("Failed to find the attributes to bind");
    }

}
