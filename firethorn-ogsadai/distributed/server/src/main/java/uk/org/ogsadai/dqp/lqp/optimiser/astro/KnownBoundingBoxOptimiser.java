// Copyright (c) The University of Edinburgh, 2011.
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

package uk.org.ogsadai.dqp.lqp.optimiser.astro;

import java.util.LinkedList;
import java.util.List;

import uk.org.ogsadai.dqp.common.CompilerConfiguration;
import uk.org.ogsadai.dqp.common.RequestDetails;
import uk.org.ogsadai.dqp.lqp.CommonPredicate;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.operators.SelectOperator;
import uk.org.ogsadai.dqp.lqp.optimiser.Optimiser;
import uk.org.ogsadai.dqp.lqp.optimiser.OptimiserUtils;
import uk.org.ogsadai.dqp.lqp.optimiser.join.EqualsValidatingExpressionVisitor;
import uk.org.ogsadai.expression.ArithmeticExpressionOperand;
import uk.org.ogsadai.expression.Expression;
import uk.org.ogsadai.expression.GreaterThanOrEqualExpression;
import uk.org.ogsadai.expression.LessThanOrEqualExpression;
import uk.org.ogsadai.expression.arithmetic.Minus;
import uk.org.ogsadai.expression.arithmetic.Plus;
import uk.org.ogsadai.expression.arithmetic.visitors.AnyArithmeticExpressionValidatingVisitor;
import uk.org.ogsadai.expression.arithmetic.visitors.ConstantArithmeticExpressionValidatingVisitor;
import uk.org.ogsadai.expression.arithmetic.visitors.FunctionArithmeticExpressionValidatingVisitor;
import uk.org.ogsadai.resource.dataresource.dqp.RequestDQPFederation;

/**
 * Optimiser that looks at the predicates of SELECT operators to see if they
 * specify a constraining bounding box that can be added as additional
 * predicates in additional SELECT operators and possibly pushed down to the
 * table scan in places where the original predicated cannot.
 * <p>
 * The optimiser looks for predicates like:
 * <pre>
 * CONTAINS(POINT('ICRS', ra, dec),CIRCLE('ICRS', 145.8, 35.2, 1)) = 1
 * </pre>
 * or
 * <pre>
 * CONTAINS(POINT('ICRS', ra, dec),BOX('ICRS', 145.8, 35.2, 1, 1)) = 1
 * </pre>
 * 
 * @author The OGSA-DAI Project Team
 */
public class KnownBoundingBoxOptimiser implements Optimiser
{
    /** Copyright notice */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2011.";

    @Override
    public Operator optimise(Operator lqpRoot,
        RequestDQPFederation requestFederation,
        CompilerConfiguration compilerConfiguration,
        RequestDetails requestDetails) throws LQPException
    {
        for (Operator op : OptimiserUtils.findOccurrences(
            lqpRoot, OperatorID.SELECT))
        {
            SelectOperator selectOp = (SelectOperator) op;
            
            List<Expression> boundingBoxExpressions = findBoundingBox(
                selectOp.getPredicate().getExpression());
            
            for( Expression expr : boundingBoxExpressions)
            {
                addSelectOperator(selectOp, expr);
            }
        }
        
        return lqpRoot;
    }

    /**
     * Adds a new SELECT operator with the given expression and the given
     * parent.
     * 
     * @param parent  parent for new SELECT operator
     * @param expr    expression for new SELECT operator
     * 
     * @throws LQPException if an invalid query plan is built
     */
    private void addSelectOperator(SelectOperator parent, Expression expr) 
        throws LQPException
    {
        SelectOperator selectOp = new SelectOperator(new CommonPredicate(expr));
        selectOp.setChild(0, parent.getChild(0));
        parent.replaceChild(parent.getChild(0), selectOp);
        parent.update();
    }

    /**
     * Tries to find a bounding box that can be used to pre-filter those
     * tuples for which the expression can validate to true.
     * 
     * @param expr expression to process
     * 
     * @return the expressions that together define the bounding box.
     */
    private List<Expression> findBoundingBox(Expression expr)
    {
        List<Expression> result = new LinkedList<Expression>();

        ConstantArithmeticExpressionValidatingVisitor x =
            new ConstantArithmeticExpressionValidatingVisitor();
        ConstantArithmeticExpressionValidatingVisitor y =
            new ConstantArithmeticExpressionValidatingVisitor();
        ConstantArithmeticExpressionValidatingVisitor width =
            new ConstantArithmeticExpressionValidatingVisitor();
        ConstantArithmeticExpressionValidatingVisitor height =
            new ConstantArithmeticExpressionValidatingVisitor();
        ConstantArithmeticExpressionValidatingVisitor radius =
            new ConstantArithmeticExpressionValidatingVisitor();
        
        AnyArithmeticExpressionValidatingVisitor pointX = 
            new AnyArithmeticExpressionValidatingVisitor();
        AnyArithmeticExpressionValidatingVisitor pointY = 
            new AnyArithmeticExpressionValidatingVisitor();
        
        
        FunctionArithmeticExpressionValidatingVisitor boxValidator = 
            new FunctionArithmeticExpressionValidatingVisitor(
                "BOX",
                new ConstantArithmeticExpressionValidatingVisitor("ICRS"),
                x, 
                y, 
                width, 
                height);
                
        FunctionArithmeticExpressionValidatingVisitor pointValidator = 
            new FunctionArithmeticExpressionValidatingVisitor(
                "POINT",
                new ConstantArithmeticExpressionValidatingVisitor("ICRS"),
                pointX,
                pointY);
        
        EqualsValidatingExpressionVisitor equalsVisitor = 
            new EqualsValidatingExpressionVisitor(
                new ConstantArithmeticExpressionValidatingVisitor(
                    new Long(1)),
                new FunctionArithmeticExpressionValidatingVisitor("CONTAINS",
                    pointValidator, boxValidator));
        expr.accept(equalsVisitor);
        
        if (equalsVisitor.isValid())
        {
            // pointX <= boxX + width
            result.add(
                new LessThanOrEqualExpression(
                    new ArithmeticExpressionOperand(pointX.getExpression()),
                    new ArithmeticExpressionOperand(
                        new Plus(x.getConstant(), width.getConstant()))));
            
            // pointX >= boxX
            result.add(
                new GreaterThanOrEqualExpression(
                    new ArithmeticExpressionOperand(pointX.getExpression()),
                    new ArithmeticExpressionOperand(x.getConstant())));

            // pointY <= boxY + height
            result.add(
                new LessThanOrEqualExpression(
                    new ArithmeticExpressionOperand(pointY.getExpression()),
                    new ArithmeticExpressionOperand(
                        new Plus(y.getConstant(), height.getConstant()))));

            // pointY >= boxY
            result.add(
                new GreaterThanOrEqualExpression(
                    new ArithmeticExpressionOperand(pointY.getExpression()),
                    new ArithmeticExpressionOperand(y.getConstant())));
            
            return result;
        }
        else
        {
            // Handle circle
            FunctionArithmeticExpressionValidatingVisitor circleValidator = 
                new FunctionArithmeticExpressionValidatingVisitor(
                    "CIRCLE",
                    new ConstantArithmeticExpressionValidatingVisitor("ICRS"),
                    x, 
                    y, 
                    radius);

            equalsVisitor = new EqualsValidatingExpressionVisitor(
                new ConstantArithmeticExpressionValidatingVisitor(new Long(1)),
                new FunctionArithmeticExpressionValidatingVisitor("CONTAINS",
                    pointValidator, circleValidator));
            
            expr.accept(equalsVisitor);

            if (equalsVisitor.isValid())
            {
                // pointX <= x + radius
                result.add(
                    new LessThanOrEqualExpression(
                        new ArithmeticExpressionOperand(pointX.getExpression()),
                        new ArithmeticExpressionOperand(
                            new Plus(x.getConstant(), radius.getConstant()))));
                
                // pointX >= x - radius
                result.add(
                    new GreaterThanOrEqualExpression(
                        new ArithmeticExpressionOperand(pointX.getExpression()),
                        new ArithmeticExpressionOperand(
                            new Minus(x.getConstant(), radius.getConstant()))));

                // pointY <= y + radius
                result.add(
                    new LessThanOrEqualExpression(
                        new ArithmeticExpressionOperand(pointY.getExpression()),
                        new ArithmeticExpressionOperand(
                            new Plus(y.getConstant(), radius.getConstant()))));

                // pointY >= y - radius
                result.add(
                    new GreaterThanOrEqualExpression(
                        new ArithmeticExpressionOperand(pointY.getExpression()),
                        new ArithmeticExpressionOperand(
                            new Minus(y.getConstant(), radius.getConstant()))));
                
                return result;
            }
        }
        
        return result;
    }

}
