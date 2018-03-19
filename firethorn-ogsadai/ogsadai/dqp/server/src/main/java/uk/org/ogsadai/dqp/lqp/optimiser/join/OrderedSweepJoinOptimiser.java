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

package uk.org.ogsadai.dqp.lqp.optimiser.join;

import uk.org.ogsadai.dqp.common.CompilerConfiguration;
import uk.org.ogsadai.dqp.common.RequestDetails;
import uk.org.ogsadai.dqp.lqp.Annotation;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.AttributeImpl;
import uk.org.ogsadai.dqp.lqp.AttributeMatchMode;
import uk.org.ogsadai.dqp.lqp.AttributeUtils;
import uk.org.ogsadai.dqp.lqp.CommonPredicate;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.Predicate;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.operators.InnerThetaJoinOperator;
import uk.org.ogsadai.dqp.lqp.operators.SelectOperator;
import uk.org.ogsadai.dqp.lqp.optimiser.Optimiser;
import uk.org.ogsadai.dqp.lqp.optimiser.OptimiserUtils;
import uk.org.ogsadai.expression.Expression;
import uk.org.ogsadai.expression.arithmetic.Constant;
import uk.org.ogsadai.expression.arithmetic.TableColumn;
import uk.org.ogsadai.expression.arithmetic.visitors.AnyArithmeticExpressionValidatingVisitor;
import uk.org.ogsadai.expression.arithmetic.visitors.ConstantArithmeticExpressionValidatingVisitor;
import uk.org.ogsadai.expression.arithmetic.visitors.FunctionArithmeticExpressionValidatingVisitor;
import uk.org.ogsadai.expression.arithmetic.visitors.MinusArithmeticExpressionValidatingVisitor;
import uk.org.ogsadai.expression.arithmetic.visitors.TableColumnArithmeticExpressionValidatingVisitor;
import uk.org.ogsadai.resource.dataresource.dqp.RequestDQPFederation;

public class OrderedSweepJoinOptimiser implements Optimiser
{
    private TableColumn[] mTableColumns = new TableColumn[2];
    private Constant mOffset;
    private Constant mSize;
    
    
    @Override
    public Operator optimise(Operator lqpRoot,
        RequestDQPFederation requestFederation,
        CompilerConfiguration compilerConfiguration,
        RequestDetails requestDetails) throws LQPException
    {
        // Find all the inner-theta joins
        for (Operator op : OptimiserUtils.findOccurrences(
            lqpRoot, OperatorID.INNER_THETA_JOIN))
        {
            InnerThetaJoinOperator joinOperator = (InnerThetaJoinOperator) op;
            
            if (findSweepPredicate(joinOperator) != null)
            {
                // Check that the two attributes are in different children
                Attribute attr0 = new AttributeImpl(mTableColumns[0]);
                Attribute attr1 = new AttributeImpl(mTableColumns[1]);
                boolean matched = false;
                int attr0ChildIndex = -1;
                for (int i=0; i<2; ++i)
                {
                    Operator child0 = joinOperator.getChild(i);
                    Operator child1 = joinOperator.getChild(1-i);
                    
                    if (AttributeUtils.containsMatching(
                            attr0,
                            child0.getHeading().getAttributes(),
                            AttributeMatchMode.NAME_AND_NULL_SOURCE) &&
                        AttributeUtils.containsMatching(
                            attr1,
                            child1.getHeading().getAttributes(),
                            AttributeMatchMode.NAME_AND_NULL_SOURCE))
                    {
                        matched = true;
                        attr0ChildIndex = i;
                        break;
                    }
                }

                if (!matched) break;
                
                // Find if children can be sorted by the attributes
                if (OptimiserUtils.canQueryPlanBeOrdered(
                        joinOperator.getChild(attr0ChildIndex), attr0) &&
                    OptimiserUtils.canQueryPlanBeOrdered(
                        joinOperator.getChild(1-attr0ChildIndex), attr1))
                {
                    OptimiserUtils.orderQueryPlan(
                        joinOperator.getChild(attr0ChildIndex), attr0);
                    OptimiserUtils.orderQueryPlan(
                        joinOperator.getChild(1-attr0ChildIndex), attr1);
                
                    // Move the join predicate to a select above the join
                    Operator joinParent = joinOperator.getParent();
                    SelectOperator select = new SelectOperator(
                        joinOperator.getPredicate());
                    OptimiserUtils.copyPlacementAnnotations(
                        joinOperator,select);
                    
                    joinParent.replaceChild(joinOperator, select);
                    joinOperator.setParent(select);
                    select.setChild(0, joinOperator);
                    joinParent.update();
                    
                    // Annotate to say we are using the PLANE_SWEEP 
                    // implementation
                    Annotation.addImplementationAnnotation(
                        joinOperator, "PLANE_SWEEP");

                    // Add annotations to pass the parameters required by the
                    // builder
                    joinOperator.addAnnotation("PLANE_SWEEP_size", 
                        mSize.getResult());
                    joinOperator.addAnnotation("PLANE_SWEEP_offset", 
                        mOffset.getResult());
                    joinOperator.addAnnotation("PLANE_SWEEP_attr0", attr0);
                    joinOperator.addAnnotation("PLANE_SWEEP_attr1", attr1);
                    joinOperator.addAnnotation("PLANE_SWEEP_attr0ChildIndex", 
                        attr0ChildIndex);
                }
            }
        }

        return lqpRoot;
    }

    /**
     * Returns a predicate that specifies that a plane-sweep join algorithm
     * can be use to the given join. Such a predicate consists of the pattern
     * <pre>
     * Contains( Point(string, x1, y1), Box(string, x2, y2-offset, xSize, ySize).
     * </pre>
     * where y1 and y2 are from different children of the join.
     * <p>
     * This predicate need not necessarily be the predicate of the join
     * operator. It could be a predicate of a select operator above the join
     * operator, so long as it does join attributes from both sides of the
     * join.
     *   
     * @param op join operator
     * 
     * @return the plane-sweep predicate, or <code>null</code> if no appropriate
     * predicate is found.
     */
    private Predicate findSweepPredicate(InnerThetaJoinOperator op)
    {
        // First check the predicate attached to the op
        Predicate pred = op.getPredicate();
        if (isSweepPredicate(pred))
        {
            return pred;
        }
        
        // look for sweeps possibilities in SELECTs above
        Operator operator = op.getParent();
        while (operator.getID() != OperatorID.NIL && 
                operator.getID() == OperatorID.SELECT)
        {
            SelectOperator select = (SelectOperator) operator;
            pred = select.getPredicate();
            if (isSweepPredicate(pred))
            {
                return pred;
            }
            else
            {
                operator = operator.getParent();
            }
        }
        
        return null;
    }

    /**
     * Checks if given predicate is a sweep predicate.
     * 
     * @param pred
     *            predicate
     * 
     * @return <tt>true</tt> if predicate is a sweep predicate, <tt>false</tt>
     *         otherwise.
     */
    private boolean isSweepPredicate(Predicate pred)
    {
        if (! (pred instanceof CommonPredicate)) return false;
        Expression expr = ((CommonPredicate) pred).getExpression();
        
        TableColumnArithmeticExpressionValidatingVisitor attr0 = 
            new TableColumnArithmeticExpressionValidatingVisitor();
        TableColumnArithmeticExpressionValidatingVisitor attr1 = 
            new TableColumnArithmeticExpressionValidatingVisitor();
        ConstantArithmeticExpressionValidatingVisitor offset = 
            new ConstantArithmeticExpressionValidatingVisitor();
        ConstantArithmeticExpressionValidatingVisitor size =
            new ConstantArithmeticExpressionValidatingVisitor();

        FunctionArithmeticExpressionValidatingVisitor boxValidator = 
            new FunctionArithmeticExpressionValidatingVisitor(
                "Box",
                new AnyArithmeticExpressionValidatingVisitor(),
                new AnyArithmeticExpressionValidatingVisitor(),
                new MinusArithmeticExpressionValidatingVisitor(
                    attr1, offset),
                new AnyArithmeticExpressionValidatingVisitor(),
                size);
        
        FunctionArithmeticExpressionValidatingVisitor pointValidator = 
            new FunctionArithmeticExpressionValidatingVisitor(
                "Point",
                new AnyArithmeticExpressionValidatingVisitor(),
                new AnyArithmeticExpressionValidatingVisitor(),
                attr0);
        
        EqualsValidatingExpressionVisitor equalsVisitor = 
            new EqualsValidatingExpressionVisitor(
                new ConstantArithmeticExpressionValidatingVisitor(Boolean.TRUE),
                new FunctionArithmeticExpressionValidatingVisitor("Contains",
                    pointValidator, boxValidator));
        expr.accept(equalsVisitor);
        
        mTableColumns[0] = attr0.getTableColumn();
        mTableColumns[1] = attr1.getTableColumn();
        mOffset = offset.getConstant();
        mSize = size.getConstant();

        return equalsVisitor.isValid();
    }
}
