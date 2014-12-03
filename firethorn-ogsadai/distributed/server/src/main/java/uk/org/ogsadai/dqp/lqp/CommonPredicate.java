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

import java.util.Collection;
import java.util.List;

import org.antlr.runtime.tree.CommonTree;

import uk.org.ogsadai.dqp.lqp.exceptions.AmbiguousAttributeException;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.udf.FunctionRepository;
import uk.org.ogsadai.expression.AndExpression;
import uk.org.ogsadai.expression.Expression;
import uk.org.ogsadai.expression.ExpressionFactory;
import uk.org.ogsadai.expression.ExpressionUtils;
import uk.org.ogsadai.expression.arithmetic.ExpressionException;
import uk.org.ogsadai.parser.sql92query.SQLQueryParser;

/**
 * Class for representing predicates.
 *  
 * @author The OGSA-DAI Project Team
 */
public class CommonPredicate extends Predicate
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";
        
    /**
     * Constructor.
     * 
     * @param predicateExpression
     */
    public CommonPredicate(Expression predicateExpression)
    {
        mPredicateExpression = predicateExpression;
    }
    
    /**
     * Constructor.
     * 
     * @param condString
     *            string representation of a predicate
     * @param functionRepository
     */
    public CommonPredicate(String condString,
	    FunctionRepository functionRepository)
    {
	try
	{
	    CommonTree condAST = SQLQueryParser.getInstance()
		    .parseSQLForCondition(condString);
	    mPredicateExpression = ExpressionFactory.buildExpression(condAST,
		    functionRepository);
	}
	catch (Exception e)
	{
	    throw new RuntimeException(e);
	}
    }
    
    /**
     * Constructor.
     * 
     * @param ast
     *            predicate abstract syntax tree (SQL grammar)
     * @param functionRepository
     *            function repository or <tt>null</tt> if functions are not
     *            supported.
     */
    public CommonPredicate(CommonTree ast, FunctionRepository functionRepository)
    {
        try
        {
            mPredicateExpression = 
                ExpressionFactory.buildExpression(ast, functionRepository);
        }
        catch (ExpressionException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates new predicate as a conjunction of several predicates.
     * 
     * @param predicates
     *            list of predicates
     */
    public CommonPredicate(List<Predicate> predicates)
    {
        if(predicates.size() == 0)
        {
            throw new IllegalArgumentException(
                "Non empty list of predicates expected.");
        }
        if(predicates.size() == 1)
        {
            mPredicateExpression = predicates.get(0).mPredicateExpression;
        }
        else
        {
            AndExpression and =
                new AndExpression(predicates.get(0).getExpression(),
                    predicates.get(1).getExpression());
            
            for (int i = 2; i < predicates.size(); i++)
            {
                AndExpression newAnd =
                    new AndExpression(and,
                        predicates.get(i).mPredicateExpression);
                and = newAnd;
            }
            mPredicateExpression = and;
        }
    }

    /**
     * Returns a clone of a predicate.
     * 
     * @return
     *         cloned predicate
     */
    public CommonPredicate getClone()
    {
	return new CommonPredicate(ExpressionUtils
		.getClone(mPredicateExpression));
    }

    /**
     * {@inheritDoc}
     */
    public boolean equals(Object obj)
    {
        if (obj instanceof CommonPredicate)
        {
            CommonPredicate p = (CommonPredicate) obj;

            return mPredicateExpression.toString().equals(
                p.mPredicateExpression.toString());
        }
        else
        {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        return ExpressionUtils.generateSQL(mPredicateExpression);
    }

    /**
     * Return a textual representation of a negated comparison operator.
     * 
     * @param cmpOp
     *            comparison operator
     * @return negation of a comparison operator
     */
    public static String getNegation(String cmpOp)
    {
	if (cmpOp.equals("="))
	{
	    return "!=";
	}
	else if (cmpOp.equals(">"))
	{
	    return "<=";
	}
	else if (cmpOp.equals("<"))
	{
	    return ">=";
	}
	else if (cmpOp.equals(">="))
	{
	    return "<";
	}
	else if (cmpOp.equals("<="))
	{
	    return ">";
	}
	else if (cmpOp.equals("!=") || cmpOp.equals("<>"))
	{
	    return "=";
	}
	else
	{
	    throw new IllegalArgumentException(
		    "Unexpected comparison operator: " + cmpOp);
	}
    }
}
