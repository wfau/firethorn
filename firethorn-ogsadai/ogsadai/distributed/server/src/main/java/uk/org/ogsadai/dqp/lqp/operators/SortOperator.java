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
import java.util.List;
import java.util.Set;

import org.antlr.runtime.tree.CommonTree;

import uk.org.ogsadai.client.toolkit.activities.relational.SortOrder;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.AttributeImpl;
import uk.org.ogsadai.dqp.lqp.HeadingImpl;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.OperatorVisitor;
import uk.org.ogsadai.dqp.lqp.RenameMap;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.parser.sql92query.SQL92QueryLexer;

/**
 * Operator SORT.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class SortOperator extends UnaryOperator
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /** Sort attributes. */
    protected List<Attribute> mSortAttributes;
    
    /** Sort orders. */
    protected List<SortOrder> mSortOrders;

    /**
     * Creates a new sort operator.
     */
    protected SortOperator()
    {
        mID = OperatorID.SORT;
    }

    /**
     * Constructs a disconnected SORT operator.
     * 
     * @param sortAST
     *            SORT abstract syntax tree branch (SQL)
     */
    public SortOperator(final CommonTree sortAST)
    {
        this();

        mSortAttributes = new ArrayList<Attribute>();
        mSortOrders = new ArrayList<SortOrder>();
        
        // Sample AST: (ORDER (DESC (TABLECOLUMN t a)) (ASC (TABLECOLUMN b)))
        for (int i = 0; i < sortAST.getChildCount(); i++)
        {
        	// (DESC or (ASC
            CommonTree child = (CommonTree) sortAST.getChild(i);
			mSortOrders
					.add((child.getType() == SQL92QueryLexer.ASC) ? SortOrder.ASC
							: SortOrder.DESC);
            
			// (TABLECOLUMN
			child = (CommonTree) child.getChild(0);
			
            String source;
            String name;
            if (child.getChildCount() > 1)
            {
                name = child.getChild(1).getText();
                source = child.getChild(0).getText();
            }
            else
            {
                name = child.getChild(0).getText();
                source = null;
            }

            mSortAttributes.add(new AttributeImpl(name, -1, source));
        }
    }

    /**
     * Constructs a SORT operator, connects child and updates.
     * 
     * @param childOperator
     *            child operator
     * @param sortAST
     *            SORT abstract syntax tree branch (SQL)
     * @throws LQPException
     *             if update fails
     */
    public SortOperator(final Operator childOperator, final CommonTree sortAST)
        throws LQPException
    {
        this(sortAST);
        setChild(0, childOperator);
        update();
    }

    /**
     * Returns a list of sort attributes/
     * 
     * @return sort attributes
     */
    public List<Attribute> getSortAttributes()
    {
        return mSortAttributes;
    }
    
    public List<SortOrder> getSortOrders()
    {
    	return mSortOrders;
    }

    /**
     * {@inheritDoc}
     */
    public Set<Attribute> getUsedAttributes()
    {
        return new HashSet<Attribute>(mSortAttributes);
    }

    /**
     * {@inheritDoc}
     */
    public void renameUsedAttributes(RenameMap renameMap) throws LQPException
    {
	for (int i = 0; i < mSortAttributes.size(); i++)
	{
	    Attribute attr = renameMap.getRenamedAttribute(mSortAttributes
		    .get(i));
	    if (attr != null)
	    {
		mSortAttributes.set(i, attr);
	    }
	}
    }

    /**
     * {@inheritDoc}
     */
    public void update() throws LQPException
    {
        super.update();
        mOperatorHeading = new HeadingImpl(mChildOperator.getHeading());
    }

    /**
     * {@inheritDoc}
     */
    public void validate() throws LQPException
    {
        super.validate();

        mChildOperator.getHeading().containsAll(mSortAttributes);
    }

    /**
     * {@inheritDoc}
     */
    public void accept(OperatorVisitor visitor)
    {
        visitor.visit(this);
    }

}
