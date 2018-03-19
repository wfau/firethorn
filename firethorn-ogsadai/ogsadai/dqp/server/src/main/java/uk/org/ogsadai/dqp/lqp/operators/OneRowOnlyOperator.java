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

import java.util.Arrays;

import uk.org.ogsadai.dqp.lqp.Annotation;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.HeadingImpl;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.OperatorVisitor;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;

/**
 * Operator ONE ROW ONLY.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class OneRowOnlyOperator extends UnaryOperator
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
	"Copyright (c) The University of Edinburgh, 2008";
    
    /** If attribute should be projected out. */
    boolean mTemporary;

    /**
     * Constructs a disconnected ONE_ROW_ONLY operator.
     * 
     * @param isTemporary
     *            if the result should be projected out
     */
    public OneRowOnlyOperator(boolean isTemporary)
    {
	mID = OperatorID.ONE_ROW_ONLY;
	mTemporary = isTemporary;
    }

    /**
     * Constructs a connected ONE_ROW_ONLY operator.
     * 
     * @param child
     *            child operator
     * @param isTemporary
     *            if the result should be projected out
     */
    public OneRowOnlyOperator(Operator child, boolean isTemporary)
    {
	this(isTemporary);
	setChild(0, child);
    }

    /**
     * {@inheritDoc}
     */
    public void update() throws LQPException
    {
	super.update();

	Attribute childAttr = 
	    mChildOperator.getHeading().getAttributes().get(0);
	if(mTemporary)
	{
	    Annotation.addTempAttrAnnotation(childAttr, true);
	}
	
	mOperatorHeading = new HeadingImpl(Arrays
		.asList(new Attribute[] { childAttr }));
    }

    /**
     * {@inheritDoc}
     */
    public void accept(OperatorVisitor visitor)
    {
	visitor.visit(this);
    }

}
