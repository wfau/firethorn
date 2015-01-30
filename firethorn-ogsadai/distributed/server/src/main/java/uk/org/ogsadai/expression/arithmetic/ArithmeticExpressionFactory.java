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

package uk.org.ogsadai.expression.arithmetic;

import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.tree.CommonTree;

import uk.org.ogsadai.context.OGSADAIContext;
import uk.org.ogsadai.dqp.lqp.exceptions.UnsupportedTokenException;
import uk.org.ogsadai.dqp.lqp.udf.ExecutableFunction;
import uk.org.ogsadai.dqp.lqp.udf.Function;
import uk.org.ogsadai.dqp.lqp.udf.FunctionRepository;
import uk.org.ogsadai.dqp.lqp.udf.repository.NoFunctionRepositoryException;
import uk.org.ogsadai.dqp.lqp.udf.repository.NoSuchFunctionException;
import uk.org.ogsadai.parser.sql92query.SQL92QueryParser;
import uk.org.ogsadai.tuple.Null;
import uk.org.ogsadai.tuple.TupleTypes;
import uk.org.ogsadai.tuple.converters.StringConversionException;

/**
 * Constructs an arithmetic expression from an abstract syntax tree.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class ArithmeticExpressionFactory
{
    /** Copyright notice */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2007.";

    /**
     * Private constructor. This class has only static methods.
     */
    private ArithmeticExpressionFactory()
    {
        // private constructor
    }

    /**
     * Parses the given abstract syntax tree and constructs the expression tree
     * from it.
     * 
     * @param expressionTree
     *            abstract syntax tree representing an arithmetic expression

     * @param functionRepository
     *            function repository, or <tt>null</tt> if functions are not
     *            to be supported.
     *            
     * @return an arithmetic expression tree
     * @throws ExpressionException
     *             if a problem occurs
     */
    public static ArithmeticExpression buildArithmeticExpression(
            CommonTree expressionTree,
            FunctionRepository functionRepository) 
        throws ExpressionException
    {
        try
        {
        

            if (expressionTree.getType() == SQL92QueryParser.TABLECOLUMN)
            {
                String source = null;
                String name;
                // 2 children - 1st is the table name, 2nd is column name
                if (expressionTree.getChildCount() == 2)
                {
                    source = expressionTree.getChild(0).getText();
                    name = expressionTree.getChild(1).getText();
                }
                else
                {
                    name = expressionTree.getChild(0).getText();
                }
                return new TableColumn(name, source);
            }
            else if (expressionTree.getType() == SQL92QueryParser.INT)
            {
                return new Constant(
                    TupleTypes._LONG, expressionTree.getText());
            }
            else if (expressionTree.getType() == SQL92QueryParser.FLOAT
                || expressionTree.getType() == SQL92QueryParser.NUMERIC)
            {
                return new Constant(
                    TupleTypes._DOUBLE, expressionTree.getText());
            }
            else if (expressionTree.getType() == SQL92QueryParser.STRING)
            {
                String quoted = expressionTree.getText();
                String content = quoted.substring(1, quoted.length() - 1);
                return new Constant(TupleTypes._STRING, content);
            }
            else if (expressionTree.getType() == SQL92QueryParser.FUNCTION)
            {
                // We need a function repository to process functions.  Throw
                // error if we do not have one.
                if (functionRepository == null)
                {
                    throw new ExpressionException(
                        new NoFunctionRepositoryException());
                }

                try
                {
                    String name = expressionTree.getChild(0).getText();
                    Function function = functionRepository
                        .getFunctionInstanceByName(name);

                    List<ArithmeticExpression> paramList = 
                        new ArrayList<ArithmeticExpression>();
                    for (int i = 1; i < expressionTree.getChildCount(); i++)
                    {

                        paramList.add(buildArithmeticExpression(
                            (CommonTree)expressionTree.getChild(i), 
                            functionRepository));
                    }

                    ExecutableFunction f = (ExecutableFunction) function;
                    f.initialise(paramList);

                    return new ExecutableFunctionExpression(f);
                }
                catch (NoSuchFunctionException e)
                {
                    throw new ExpressionException(e);
                }
            }
            else if (expressionTree.getText().equals("+"))
            {
                ArithmeticExpression n1 = buildArithmeticExpression(
                    (CommonTree) expressionTree.getChild(0), 
                    functionRepository);
                ArithmeticExpression n2 = buildArithmeticExpression(
                    (CommonTree) expressionTree.getChild(1), 
                    functionRepository);
                return new Plus(n1, n2);
            }
            else if (expressionTree.getText().equals("-"))
            {
                if (expressionTree.getChildCount() == 1)
                {
                    ArithmeticExpression n1 = new Constant(TupleTypes._LONG,
                        "0");
                    ArithmeticExpression n2 = buildArithmeticExpression(
                        (CommonTree) expressionTree.getChild(0), 
                        functionRepository);
                    return new Minus(n1, n2);
                }
                else
                {
                    ArithmeticExpression n1 = buildArithmeticExpression(
                        (CommonTree) expressionTree.getChild(0), 
                        functionRepository);
                    ArithmeticExpression n2 = buildArithmeticExpression(
                        (CommonTree) expressionTree.getChild(1), 
                        functionRepository);
                    return new Minus(n1, n2);
                }
            }
            else if (expressionTree.getText().equals("/"))
            {
                ArithmeticExpression n1 = buildArithmeticExpression(
                    (CommonTree) expressionTree.getChild(0), 
                    functionRepository);
                ArithmeticExpression n2 = buildArithmeticExpression(
                    (CommonTree) expressionTree.getChild(1), 
                    functionRepository);
                return new Div(n1, n2);

            }
            else if (expressionTree.getText().equals("*"))
            {

                if (expressionTree.getChildCount() == 0)
                {
                    // we have a function with parameter '*'
                    return new Star();
                }
                ArithmeticExpression n1 = buildArithmeticExpression(
                    (CommonTree) expressionTree.getChild(0), 
                    functionRepository);
                ArithmeticExpression n2 = buildArithmeticExpression(
                    (CommonTree) expressionTree.getChild(1), 
                    functionRepository);
                return new Mult(n1, n2);
            }
            else if (expressionTree.getText().equals("NULL"))
            {
                return new Constant(TupleTypes._ODNULL, Null.getValue()
                    .toString());
            }
            else if (expressionTree.getText().equalsIgnoreCase("TRUE"))
            {
                return new Constant(TupleTypes._BOOLEAN, Boolean.TRUE
                    .toString());
            }
            else if (expressionTree.getText().equalsIgnoreCase("FALSE"))
            {
                return new Constant(TupleTypes._BOOLEAN, Boolean.FALSE
                    .toString());
            }
            else if (expressionTree.getType() == SQL92QueryParser.COLUMN)
            {
                return buildArithmeticExpression(
                    (CommonTree) expressionTree.getChild(0), 
                    functionRepository);
            }
            else
            {
                throw new ExpressionException(
                        new UnsupportedTokenException(expressionTree.getText()));
            }
        }
        catch (StringConversionException e)
        {
            throw new ExpressionException(e);
        }
    }
}
