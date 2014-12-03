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

package uk.org.ogsadai.dqp.lqp.cardinality;

import java.util.List;
import java.util.Set;

import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.dqp.lqp.Annotation;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.Heading;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.OperatorVisitor;
import uk.org.ogsadai.dqp.lqp.Predicate;
import uk.org.ogsadai.dqp.lqp.operators.AntiSemiJoinOperator;
import uk.org.ogsadai.dqp.lqp.operators.ApplyOperator;
import uk.org.ogsadai.dqp.lqp.operators.BinaryOperator;
import uk.org.ogsadai.dqp.lqp.operators.BinaryRelFunctionOperator;
import uk.org.ogsadai.dqp.lqp.operators.DifferenceOperator;
import uk.org.ogsadai.dqp.lqp.operators.DuplicateEliminationOperator;
import uk.org.ogsadai.dqp.lqp.operators.ExchangeOperator;
import uk.org.ogsadai.dqp.lqp.operators.FullOuterJoinOperator;
import uk.org.ogsadai.dqp.lqp.operators.GroupByOperator;
import uk.org.ogsadai.dqp.lqp.operators.InnerThetaJoinOperator;
import uk.org.ogsadai.dqp.lqp.operators.IntersectionOperator;
import uk.org.ogsadai.dqp.lqp.operators.LeftOuterJoinOperator;
import uk.org.ogsadai.dqp.lqp.operators.OneRowOnlyOperator;
import uk.org.ogsadai.dqp.lqp.operators.ProductOperator;
import uk.org.ogsadai.dqp.lqp.operators.ProjectOperator;
import uk.org.ogsadai.dqp.lqp.operators.RenameOperator;
import uk.org.ogsadai.dqp.lqp.operators.RightOuterJoinOperator;
import uk.org.ogsadai.dqp.lqp.operators.ScalarGroupByOperator;
import uk.org.ogsadai.dqp.lqp.operators.ScanRelFunctionOperator;
import uk.org.ogsadai.dqp.lqp.operators.SelectOperator;
import uk.org.ogsadai.dqp.lqp.operators.SemiJoinOperator;
import uk.org.ogsadai.dqp.lqp.operators.SortOperator;
import uk.org.ogsadai.dqp.lqp.operators.TableScanOperator;
import uk.org.ogsadai.dqp.lqp.operators.UnaryOperator;
import uk.org.ogsadai.dqp.lqp.operators.UnaryRelFunctionOperator;
import uk.org.ogsadai.dqp.lqp.operators.UnionOperator;
import uk.org.ogsadai.expression.visitors.PredicateTypeExprVisitor;
import uk.org.ogsadai.expression.visitors.PredicateTypeExprVisitor.PredicateType;

/**
 * LQP visitor which estimates cardinality and annotates operators with the
 * <code>result.cardinality->Long</code> annotation. It is not walking the query
 * plan it will only update the operator which accepted it.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class BARTEKS_CardinalityEstimatingOperatorVisitor implements OperatorVisitor
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /** Logger. */
    private static final DAILogger LOG =
        DAILogger.getLogger(BARTEKS_CardinalityEstimatingOperatorVisitor.class);

    /** Default cardinality for scan operators. */
    private static final double DEFAULT_SCAN_CARDINALITY = 1000;
    
    /**
     * Estimate and annotate generic unary operator.
     * 
     * @param operator
     *            unary operator
     */
    private void estimateAndAnnotateUnary(UnaryOperator operator)
    {
//        double resultCard = operator.getChild(0).getResultCardinality();
//        
//        Annotation.addCardinalityAnnotation(operator, resultCard);
    }

    /**
     * Estimate and annotate generic binary operator.
     * 
     * @param operator
     *            binary operator
     */
    private void estimateAndAnnotateBinary(BinaryOperator operator)
    {
//        double resultCard = Math.max(operator.getChild(0).getResultCardinality(),
//            operator.getChild(1).getResultCardinality());
//
//        Annotation.addCardinalityAnnotation(operator, resultCard);
    }

    /**
     * Estimate for predicate.
     * 
     * @param predicate
     *            predicate
     * @param inputHeading
     *            input heading with attribute key information
     * @param inputCard
     *            input cardinality
     * @return estimated cardinality
     */
    private double estimateForPredicate(final Predicate predicate,
        final Heading inputHeading, final double inputCard)
    {
        double currrentInputCard = inputCard;
        double selectCard = inputCard;

        for(Predicate p : predicate.splitConjunction())
        {
            PredicateType predicateType = getPredicateType(p);
            if (predicateType == PredicateType.NON_EQ)
            {
                selectCard = currrentInputCard / 3;
            }
            else if (predicateType == PredicateType.EQ_ATTR_CONST)
            {
                Attribute attr = 
                    p.getMatchingAttributes(inputHeading).iterator().next();
    
                if (attr.isKey())
                {
                    selectCard = 1;
                }
                else
                {
                    selectCard = currrentInputCard / 10;
                }
            }
            else if (predicateType == PredicateType.EQ_FUNC_ATTR)
            {
                selectCard = currrentInputCard / 5;
            }
            else if (predicateType == PredicateType.EQ_FUNC_CONST)
            {
                selectCard = currrentInputCard / 10;
            }
            else if (predicateType == PredicateType.EQ_ATTR_ATTR)
            {
                // Get the attributes.  Note that if the attributes either side
                // if the equals are identical then only one will be returned
                Set<Attribute> attributes = 
                    predicate.getMatchingAttributes(inputHeading);
                
                boolean allAttributesAreKeys = true;
                for (Attribute attr : attributes)
                {
                    if (!attr.isKey())
                    {
                        allAttributesAreKeys = false;
                        break;
                    }
                }

                if (allAttributesAreKeys)
                {
                    selectCard = currrentInputCard;
                }
                else
                {
                    selectCard = currrentInputCard / 5;
                }
            }
            else
            {
                // If non of the above applies just copy cardinality
                selectCard = currrentInputCard;
            }
            currrentInputCard = selectCard;
        }
        return selectCard;
    }

    /**
     * Returns predicate type for a given predicate.
     * 
     * @param predicate
     * @return predicate type
     */
    private PredicateType getPredicateType(Predicate predicate)
    {
        PredicateTypeExprVisitor v = new PredicateTypeExprVisitor();
        predicate.getExpression().accept(v);

        return v.getPredicateType();
    }

    /**
     * {@inheritDoc}
     */
    public void visit(SelectOperator operator)
    {
//        // Get input heading - information about key property of attributes used
//        // in a predicate can only be obtained from the input heading
//        Heading inputHeading = operator.getChild(0).getHeading();
//        double inputCard = operator.getChild(0).getResultCardinality();
//        double resultCard;
//        
//        try
//        {
//            resultCard = estimateForPredicate(
//                operator.getPredicate(), inputHeading, inputCard);
//        }
//        catch(IllegalArgumentException e)
//        {
//            // Here we catch missing attributes - this happens when we
//            // have a correlated subquery.  We need to handle this
//            // properly sometime but for the moment let's try to keep 
//            // going in some way.  See trac ticket #289.
//            resultCard = inputCard;
//        }
//
//        Annotation.addCardinalityAnnotation(operator, resultCard);
    }

    /**
     * {@inheritDoc}
     */
    public void visit(ProjectOperator operator)
    {
        estimateAndAnnotateUnary(operator);
    }

    /**
     * {@inheritDoc}
     */
    public void visit(RenameOperator operator)
    {
        estimateAndAnnotateUnary(operator);
    }

    /**
     * {@inheritDoc}
     */
    public void visit(DuplicateEliminationOperator operator)
    {
        estimateAndAnnotateUnary(operator);
    }

    /**
     * {@inheritDoc}
     */
    public void visit(SortOperator operator)
    {
        estimateAndAnnotateUnary(operator);
    }

    /**
     * {@inheritDoc}
     */
    public void visit(OneRowOnlyOperator operator)
    {
        estimateAndAnnotateUnary(operator);
    }

    /**
     * {@inheritDoc}
     */
    public void visit(GroupByOperator operator)
    {
//        double resultCard;
//        List<Attribute> groupingAttr = operator.getGroupingAttributes();
//
//        boolean containsKey = false;
//        for (Attribute attr : groupingAttr)
//        {
//            if (attr.isKey())
//            {
//                containsKey = true;
//            }
//        }
//
//        if (containsKey)
//        {
//            resultCard = operator.getChild(0).getResultCardinality();
//        }
//        else
//        {
//            resultCard = Math.max(4L, operator.getChild(0)
//                .getResultCardinality()
//                / (10 * groupingAttr.size()));
//        }
//        Annotation.addCardinalityAnnotation(operator, resultCard);
    }
    
    /**
     * {@inheritDoc}
     */
    public void visit(ScalarGroupByOperator operator)
    {
        Annotation.addCardinalityAnnotation(operator, 1.0);
    }

    /**
     * {@inheritDoc}
     */
    public void visit(TableScanOperator operator)
    {
        // NOOP
    }

    /**
     * {@inheritDoc}
     */
    public void visit(ExchangeOperator operator)
    {
        estimateAndAnnotateUnary(operator);
    }

    /**
     * {@inheritDoc}
     */
    public void visit(ProductOperator operator)
    {
//        double resultCard = operator.getChild(0).getResultCardinality()
//            * operator.getChild(1).getResultCardinality();
//        
//        Annotation.addCardinalityAnnotation(operator, resultCard);
    }

    /**
     * {@inheritDoc}
     */
    public void visit(InnerThetaJoinOperator operator)
    {
//        double resultCard = 0;
//        double currentCard = 0;
//        
//        double leftCard = operator.getChild(0).getResultCardinality();
//        double rightCard = operator.getChild(1).getResultCardinality();
//
//        // Get input heading - information about key property of attributes used
//        // in a predicate can only be obtained from the input heading
//        Heading inputHeading = operator.getChild(0).getHeading()
//            .createMerged(operator.getChild(1).getHeading());
//
//        List<Predicate> splitPredicateList = operator.getPredicate()
//            .splitConjunction();
//
//        // Treat first predicate as join predicate and the rest as select
//        // predicates
//        
//        for(int i=0; i<splitPredicateList.size(); i++)
//        {
//            Predicate p = splitPredicateList.get(i);
//            PredicateType predicateType = getPredicateType(p);
//            
//            if(i == 0)
//            {
//                if (predicateType == PredicateType.EQ_ATTR_ATTR)
//                {
//                    Set<Attribute> attrs = p.getMatchingAttributes(inputHeading);
//                    
//                    Attribute leftAttr = null;
//                    Attribute rightAttr = null;
//                    
//                    for (Attribute attr : attrs)
//                    {
//                        if (operator.getChild(0).getHeading().contains(attr))
//                        {
//                            leftAttr = attr;
//                        }
//                        
//                        if (operator.getChild(1).getHeading().contains(attr))
//                        {
//                            rightAttr = attr;
//                        }
//                    }
//                    
//                    if (leftAttr.isKey() && rightAttr.isKey())
//                    {
//                        resultCard = Math.max(leftCard, rightCard);
//                    }
//                    else if (leftAttr.isKey())
//                    {
//                        resultCard = Math.max(leftCard, rightCard / 5);
//                    }
//                    else if (rightAttr.isKey())
//                    {
//                        resultCard = Math.max(leftCard / 5, rightCard);
//                    }
//                    else
//                    {
//                        resultCard = Math.max(leftCard / 5, rightCard / 5);
//                    }
//                }
//                else
//                {
//                    // treat as a product followed by a selection
//                    resultCard = estimateForPredicate(p,
//                        inputHeading, leftCard * rightCard);
//                }
//                currentCard = resultCard;
//            }
//            else
//            {
//                resultCard = estimateForPredicate(p, inputHeading, currentCard);
//                currentCard = resultCard;
//            }
//        }
//        Annotation.addCardinalityAnnotation(operator, resultCard);
    }

    /**
     * {@inheritDoc}
     */
    public void visit(SemiJoinOperator operator)
    {
//        double resultCard = operator.getChild(0).getResultCardinality();
//
//        Annotation.addCardinalityAnnotation(operator, resultCard);
    }

    /**
     * {@inheritDoc}
     */
    public void visit(AntiSemiJoinOperator operator)
    {
//        double resultCard = operator.getChild(0).getResultCardinality();
//
//        Annotation.addCardinalityAnnotation(operator, resultCard);
    }

    /**
     * {@inheritDoc}
     */
    public void visit(FullOuterJoinOperator operator)
    {
//        // TODO: take predicate into account
//        double resultCard = operator.getChild(0).getResultCardinality()
//            + operator.getChild(1).getResultCardinality();
//
//        Annotation.addCardinalityAnnotation(operator, resultCard);
    }

    /**
     * {@inheritDoc}
     */
    public void visit(LeftOuterJoinOperator operator)
    {
//        // TODO: predicate
//        double resultCard = operator.getChild(0).getResultCardinality();
//
//        Annotation.addCardinalityAnnotation(operator, resultCard);
    }

    /**
     * {@inheritDoc}
     */
    public void visit(RightOuterJoinOperator operator)
    {
//        // TODO: predicate
//        double resultCard = operator.getChild(1).getResultCardinality();
//
//        Annotation.addCardinalityAnnotation(operator, resultCard);
    }

    /**
     * {@inheritDoc}
     */
    public void visit(ApplyOperator operator)
    {
        // TODO: use wrapped operator
        estimateAndAnnotateBinary(operator);
    }

    /**
     * {@inheritDoc}
     */
    public void visit(UnionOperator operator)
    {
//        double resultCard = operator.getChild(0).getResultCardinality()
//            + operator.getChild(1).getResultCardinality();
//
//        Annotation.addCardinalityAnnotation(operator, resultCard);
    }

    /**
     * {@inheritDoc}
     */
    public void visit(IntersectionOperator operator)
    {
//        double resultCard = Math.min(operator.getChild(0).getResultCardinality(),
//            operator.getChild(1).getResultCardinality());
//
//        Annotation.addCardinalityAnnotation(operator, resultCard);
    }

    /**
     * {@inheritDoc}
     */
    public void visit(DifferenceOperator operator)
    {
//        double resultCard = operator.getChild(0).getResultCardinality();
//
//        Annotation.addCardinalityAnnotation(operator, resultCard);
    }

    /**
     * {@inheritDoc}
     */
    public void visit(UnaryRelFunctionOperator operator)
    {
        // TODO: ask the function object?
        estimateAndAnnotateUnary(operator);
    }

    /**
     * {@inheritDoc}
     */
    public void visit(BinaryRelFunctionOperator operator)
    {
        // TODO: ask the function object?
        estimateAndAnnotateBinary(operator);
    }

    /**
     * {@inheritDoc}
     */
    public void visit(ScanRelFunctionOperator operator)
    {
        // TODO: ask the function object?
        Annotation.addCardinalityAnnotation(operator, DEFAULT_SCAN_CARDINALITY);
    }

    /**
     * {@inheritDoc}
     */
    public void visit(Operator operator)
    {
//        double resultCard = operator.getResultCardinality();
//
//        Annotation.addCardinalityAnnotation(operator, resultCard);
    }

}
