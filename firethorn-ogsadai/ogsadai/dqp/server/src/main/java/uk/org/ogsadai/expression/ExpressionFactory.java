// Copyright (c) The University of Edinburgh 2009-2013.
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

package uk.org.ogsadai.expression;

import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.tree.CommonTree;

import uk.org.ogsadai.dqp.lqp.exceptions.UnsupportedTokenException;
import uk.org.ogsadai.dqp.lqp.udf.FunctionRepository;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpression;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpressionFactory;
import uk.org.ogsadai.expression.arithmetic.ExpressionException;
import uk.org.ogsadai.parser.sql92query.SQL92QueryParser;

/**
 * Constructs an expression tree from an abstract syntax tree.
 *
 * @author The OGSA-DAI Project Team.
 */
public class ExpressionFactory
{
    /** Copyright notice */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009-2013.";
    
    /**
     * Constructor.
     */
    private ExpressionFactory()
    {
        // private constructor - only static methods
    }

    /**
     * Constructs an expression from the given abstract syntax tree.
     * 
     * @param ast
     *            abstract syntax tree of an expression
     * @param functionRepository 
     *            function repository, or <tt>null</tt> if functions are not
     *            supported.
     * @return expression object
     * @throws ExpressionException
     *             if a problem occurred whilst building the expression tree
     * @throws UnsupportedTokenException 
     */
    public static Expression buildExpression(
        CommonTree ast, FunctionRepository functionRepository) 
        throws ExpressionException
    {
        Expression result = null;
        String token = ast.getText().toUpperCase();
        if (token.equals("OR"))
        {
            Expression lh = 
                buildExpression(
                    (CommonTree) ast.getChild(0), functionRepository);
            Expression rh = buildExpression(
                (CommonTree) ast.getChild(1), functionRepository);
            result = new OrExpression(lh, rh);
        }
        else if (token.equals("AND"))
        {
            Expression lh = buildExpression(
                (CommonTree) ast.getChild(0), functionRepository);
            Expression rh = buildExpression(
                (CommonTree) ast.getChild(1), functionRepository);
            result = new AndExpression(lh, rh);
        }
        else if (ast.getType() == SQL92QueryParser.NOT)
        {
            Expression child = buildExpression(
                (CommonTree) ast.getChild(0), functionRepository);
            result = new NotExpression(child);
        }
        else if (token.equals("IS_NULL"))
        {
            Operand child = buildOperand(
                (CommonTree)ast.getChild(0), functionRepository);
            result = new IsNullExpression(child);
        }
        else if (token.equals("LIKE"))
        {
            Operand lh = 
                buildOperand((CommonTree)ast.getChild(0), functionRepository);
            Operand rh = 
                buildOperand((CommonTree)ast.getChild(1), functionRepository);
            result = new LikeExpression(lh, rh);
        }
        else if (token.equals("BETWEEN"))
        {
            // create a copy for each subexpression of the replacement
            // because the expression is being modified later
            CommonTree child0 = (CommonTree)ast.getChild(0);
            CommonTree child1 = (CommonTree)ast.getChild(1);
            CommonTree child2 = (CommonTree)ast.getChild(2);

            Operand column1 = buildOperand(child0, functionRepository);
            Operand column2 = buildOperand(child0, functionRepository);
            
            Operand lhs = buildOperand(child1, functionRepository);
            Operand rhs = buildOperand(child2, functionRepository);
            
            // testing for LHS < x < RHS
            result = new AndExpression(
                            new GreaterThanExpression(column1, lhs), 
                            new LessThanExpression(column2, rhs));
        }
        else if (token.equals("IN"))
        {
            Operand lh = 
                buildOperand((CommonTree)ast.getChild(0), functionRepository);
            CommonTree setNode = (CommonTree)ast.getChild(1);
            
            List<Operand> rh = new ArrayList<Operand>(setNode.getChildCount());
            for(int i=0; i<setNode.getChildCount(); i++)
            {
                rh.add(buildOperand(
                    (CommonTree)setNode.getChild(i), functionRepository));
            }
            result = new InExpression(lh, rh);
        }
        else if (token.equals("EXISTS"))
        {
            // TODO implement this
            throw new ExpressionException(new UnsupportedTokenException(token));
        }
        else if (ast.getText().equals("="))
        {
            Operand lh = 
                buildOperand((CommonTree)ast.getChild(0), functionRepository);
            Operand rh = 
                buildOperand((CommonTree)ast.getChild(1), functionRepository);
            result = new EqualExpression(lh, rh);
        }
        else if (ast.getText().equals("<"))
        {
            Operand lh = 
                buildOperand((CommonTree)ast.getChild(0), functionRepository);
            Operand rh = 
                buildOperand((CommonTree)ast.getChild(1), functionRepository);
            result = new LessThanExpression(lh, rh);
        }
        else if (ast.getText().equals("<="))
        {
            Operand lh = 
                buildOperand((CommonTree)ast.getChild(0), functionRepository);
            Operand rh = 
                buildOperand((CommonTree)ast.getChild(1), functionRepository);
            result = new LessThanOrEqualExpression(lh, rh);
        }
        else if (ast.getText().equals(">"))
        {
            Operand lh = 
                buildOperand((CommonTree)ast.getChild(0), functionRepository);
            Operand rh = 
                buildOperand((CommonTree)ast.getChild(1), functionRepository);
            result = new GreaterThanExpression(lh, rh);
        }
        else if (ast.getText().equals(">="))
        {
            Operand lh = 
                buildOperand((CommonTree)ast.getChild(0), functionRepository);
            Operand rh = 
                buildOperand((CommonTree)ast.getChild(1), functionRepository);
            result = new GreaterThanOrEqualExpression(lh, rh);
        }
        else if (ast.getText().equals("!="))
        {
            Operand lh = 
                buildOperand((CommonTree)ast.getChild(0), functionRepository);
            Operand rh = 
                buildOperand((CommonTree)ast.getChild(1), functionRepository);
            result = new NotEqualExpression(lh, rh);
        }
        else if (ast.getText().equals("<>"))
        {
            Operand lh = 
                buildOperand((CommonTree)ast.getChild(0), functionRepository);
            Operand rh = 
                buildOperand((CommonTree)ast.getChild(1), functionRepository);
            result = new NotEqualExpression(lh, rh);
        }
        else
        {
            throw new ExpressionException(new UnsupportedTokenException(token));
        }
        return result;   
    }

    /**
     * Builds an arithmetic expression operand from the given abstract syntax
     * tree.
     * 
     * @param ast
     *            abstract syntax tree
     * @param functionRepository 
     *            function repository, or <tt>null</tt> if functions are not
     *            supported.
     * @return operand
     * @throws ExpressionException
     *             if an error occurred
     * @throws UnsupportedTokenException 
     */
    private static Operand buildOperand(
        CommonTree ast, FunctionRepository functionRepository)
        throws ExpressionException
    {
        ArithmeticExpression expression = 
            ArithmeticExpressionFactory.buildArithmeticExpression(
                ast, functionRepository);
        return new ArithmeticExpressionOperand(expression);
    }

}
