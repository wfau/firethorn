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

package uk.org.ogsadai.dqp.lqp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import uk.org.ogsadai.dqp.lqp.exceptions.AmbiguousAttributeException;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.expression.Expression;
import uk.org.ogsadai.expression.ExpressionUtils;
import uk.org.ogsadai.expression.visitors.AttrExtrExpressionVisitor;
import uk.org.ogsadai.expression.visitors.ConjunctionSplittingVisitor;
import uk.org.ogsadai.expression.visitors.PredicateTypeExprVisitor;
import uk.org.ogsadai.expression.visitors.PredicateTypeExprVisitor.PredicateType;

/**
 * A class for representing predicates.
 * 
 * @author The OGSA-DAI Project Team
 */
public abstract class Predicate
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";
   
    /** Predicate expression. */
    protected Expression mPredicateExpression;
        
    /**
     * Returns an expression associated with this predicate.
     * 
     * @return predicate expression
     */
    public Expression getExpression()
    {
        return mPredicateExpression;
    }

    /**
     * Gets a set of attributes used on the left hand side of the predicate root
     * expression operator.
     * 
     * @return a set of attributes
     */
    public Set<Attribute> getLHSAttributes()
    {
        AttrExtrExpressionVisitor v = new AttrExtrExpressionVisitor();
        mPredicateExpression.accept(v);
        
        return v.getLHSAttributes();
    }

    /**
     * Gets a set of attributes used on the right hand side of the predicate
     * root expression operator.
     * 
     * @return a set of attributes
     */
    public Set<Attribute> getRHSAttributes()
    {
        AttrExtrExpressionVisitor v = new AttrExtrExpressionVisitor();
        mPredicateExpression.accept(v);
        
        return v.getRHSAttributes();
    }

    /**
     * Returns attributes used in the predicate.
     * 
     * @return a set of attributes
     */
    public Set<Attribute> getAttributes()
    {
        return ExpressionUtils.getUsedAttributes(mPredicateExpression);
    }

    /**
     * For each attribute used by the predicate a matching attribute is found in
     * the heading and added to the result set. Attributes used in predicates
     * have no type and often no source information. This method is a way of
     * obtaining predicate attributes with defined source and type.
     * 
     * @param heading
     *            heading
     * @return a set of attributes
     * @throws IllegalArgumentException
     *             if some of the attributes used by the predicate could not be
     *             found in the heading (or there was ambiguity)
     */
    public Set<Attribute> getMatchingAttributes(Heading heading)
    {
        Set<Attribute> attrSet = new HashSet<Attribute>();

        try
        {
            for (Attribute attr : getAttributes())
            {
                attrSet.add(heading.getMatchingAttribute(attr));
            }
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException(
                "Heading with matching attributes expected: " + e.getMessage());
        }
        return attrSet;
    }
    
    /**
     * Splits predicates liked by <code>AND</code>.
     * 
     * @return a list of split predicates
     */
    public List<Predicate> splitConjunction()
    {
        ConjunctionSplittingVisitor v = new ConjunctionSplittingVisitor();
        mPredicateExpression.accept(v);
        
        List<Predicate> splitPredicateList = new ArrayList<Predicate>();
        for(Expression e : v.getSplitExpressions())
        {
            splitPredicateList.add(new CommonPredicate(e));
        }
        
        return splitPredicateList;
    }

    /**
     * Checks whether the predicate predicate represents equality test between
     * two attributes (<code>attr = attr'</code>).
     * 
     * @return <code>true</code> if the predicate represents equality test
     *         between two attributes.
     */
    public boolean isAttrEqAttr()
    {
        PredicateTypeExprVisitor ptev = new PredicateTypeExprVisitor();
        mPredicateExpression.accept(ptev);
        
        return ptev.getPredicateType() == PredicateType.EQ_ATTR_ATTR;
    }

    /**
     * Returns a clone of a predicate.
     * 
     * @return
     *         cloned predicate
     */
    public abstract Predicate getClone();

    /**
     * Update attributes used by the predicate to use fully qualified names
     * obtained form the collection of attributes.
     * 
     * @param attributes
     *            a collection of attributes with fully qualified names
     * @throws LQPException
     *             in case of ambiguities
     */
    public void updateAttributes(Collection<Attribute> attributes)
	    throws LQPException
    {
	RenameMap renameMap = new SimpleRenameMap();
	renameMap.setAttributeMatchMode(AttributeMatchMode.STRICT);

	for (Attribute a : getAttributes())
	{
	    List<Attribute> matching = AttributeUtils.getMatching(a,
		    attributes, AttributeMatchMode.NAME_AND_NULL_SOURCE);

	    if (matching.size() > 1)
	    {
		throw new AmbiguousAttributeException(a);
	    }
	    else if (matching.size() == 1)
	    {
		renameMap.add(a, matching.get(0));
	    }
	}
	ExpressionUtils.renameUsedAttributes(mPredicateExpression, renameMap);
    }
    
}
