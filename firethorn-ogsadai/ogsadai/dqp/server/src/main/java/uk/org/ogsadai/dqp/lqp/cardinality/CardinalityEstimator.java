// Copyright (c) The University of Edinburgh, 2012.
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
import java.util.Map.Entry;

import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.dqp.common.DataDictionary;
import uk.org.ogsadai.dqp.common.PhysicalSchema;
import uk.org.ogsadai.dqp.lqp.Annotation;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.OperatorVisitor;
import uk.org.ogsadai.dqp.lqp.RenameMap;
import uk.org.ogsadai.dqp.lqp.exceptions.AmbiguousAttributeException;
import uk.org.ogsadai.dqp.lqp.exceptions.AmbiguousMappingException;
import uk.org.ogsadai.dqp.lqp.exceptions.TableNotFoundException;
import uk.org.ogsadai.dqp.lqp.operators.AntiSemiJoinOperator;
import uk.org.ogsadai.dqp.lqp.operators.ApplyOperator;
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
import uk.org.ogsadai.dqp.lqp.operators.UnaryRelFunctionOperator;
import uk.org.ogsadai.dqp.lqp.operators.UnionOperator;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpression;

/**
 * An operator visitor that estimates cardinalities after the visited operator
 * has been applied.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class CardinalityEstimator implements OperatorVisitor
{
    /** Copyright notice */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2012.";
    
    private static final DAILogger LOG = 
        DAILogger.getLogger(CardinalityEstimator.class);

    private DataDictionary mDataDictionary;
    private ApplyOperator mWrapperApplyOperator;

    public CardinalityEstimator()
    {
    }

    /**
     * Set the wrapper operator for the visited operator. The wrapper (if not
     * <code>null</code>) must be used to access child operators.
     * 
     * @param applyOp
     *            wrapper operator
     */
    public void setApplyWrapper(ApplyOperator applyOp)
    {
        mWrapperApplyOperator = applyOp;
    }

    /**
     * Set the data dictionary. The data dictionary is only used when visiting
     * table scan operators. It must contain physical schemas for the visited
     * table scans.
     * 
     * @param dataDictionary
     *            data dictionary including physical schemas
     */
    public void setDataDictionary(DataDictionary dataDictionary)
    {
        mDataDictionary = dataDictionary;
    }

    @Override
    public void visit(SelectOperator operator)
    {
        CardinalityStatistics stats = getChildStatistics(operator, 0);
        
        CardinalitySelectVisitor visitor = 
            new CardinalitySelectVisitor(operator, stats);
        operator.getPredicate().getExpression().accept(visitor);
        
        addStatistics(operator, visitor.getResult());
    }

    @Override
    public void visit(ProjectOperator operator)
    {
        CardinalityStatistics stats = getChildStatistics(operator, 0);

        SimpleCardinalityStatistics result = new SimpleCardinalityStatistics();
        List<Attribute> attributes = operator.getHeading().getAttributes();
        for (int i=0; i<attributes.size(); i++)
        {
            Attribute attr = attributes.get(i);
            ArithmeticExpression expression = 
                operator.getAttributeExpressions().get(i);
            // generate statistics depending on the expression
            AttributeStatistics attrStats = 
                ProjectExpressionVisitor.getAttributeStatistics(
                        stats, expression);
            result.addAttributeStatistics(attr, attrStats);
        }
        addStatistics(operator, result);
    }

    @Override
    public void visit(RenameOperator operator)
    {
        CardinalityStatistics stats = getChildStatistics(operator, 0);
        RenameMap renameMap = operator.getRenameMap();
        
        SimpleCardinalityStatistics result = new SimpleCardinalityStatistics();
        for( Entry<Attribute, AttributeStatistics> entry :
             stats.getStatistics().entrySet())
        {
            try 
            {
                Attribute newAttr = renameMap.getRenamedAttribute(entry.getKey());
                result.addAttributeStatistics(newAttr, entry.getValue());
            } 
            catch (AmbiguousMappingException e) 
            {
                // this shouldn't happen
                throw new RuntimeException(e);
            } 
            catch (AmbiguousAttributeException e)
            {
                // this shouldn't happen
                throw new RuntimeException(e);
            }
        }
        
        addStatistics(operator, result);
    }

    @Override
    public void visit(DuplicateEliminationOperator operator)
    {
        CardinalityStatistics stats = getChildStatistics(operator, 0);
        CardinalityStatistics result = 
            CardinalityUtils.eliminateDuplicates(stats);
        addStatistics(operator, result);
    }

    @Override
    public void visit(SortOperator operator)
    {
        CardinalityStatistics stats = getChildStatistics(operator, 0);
        addStatistics(operator, stats);
    }

    @Override
    public void visit(OneRowOnlyOperator operator)
    {
        CardinalityStatistics stats = getChildStatistics(operator, 0);
        CardinalityStatistics result = CardinalityUtils.rescale(stats, 1);
        addStatistics(operator, result);
    }

    @Override
    public void visit(GroupByOperator operator)
    {
        CardinalityStatistics stats = getStatistics(operator.getChild(0));
        SimpleCardinalityStatistics result = new SimpleCardinalityStatistics();
        
        if (operator.getGroupingAttributes().size() == 1)
        {
            Attribute groupingAttr = operator.getGroupingAttributes().get(0);
            AttributeStatistics attrStats = stats.getStatistics(groupingAttr);
            double numValues = attrStats.getNumValues();
            // If there is only one grouping attribute the cardinality of the 
            // output relation is the number of values in the grouping column.
            // The grouping attribute is the only attribute that retains its
            // distribution - no information about other attributes.
            for (Attribute attribute : operator.getHeading().getAttributes())
            {
                if (groupingAttr != attribute)
                {
                    result.addAttributeStatistics(
                            attribute, 
                            new ScalarAttributeStatistics(numValues, numValues));
                }
                else
                {
                    result.addAttributeStatistics(
                            attribute, 
                            CardinalityUtils.groupByAttribute(attrStats));
                }
            }
        }
        else
        {
            double max = 1;
            for (Attribute attr : operator.getGroupingAttributes())
            {
                AttributeStatistics attrStats = stats.getStatistics(attr);
                max *= attrStats.getNumValues();
            }
            // product might be too large
            double numRows = Math.min(max, stats.getCardinality());

            for (Attribute attribute : operator.getHeading().getAttributes())
            {
                result.addAttributeStatistics(
                        attribute, 
                        new ScalarAttributeStatistics(numRows));
            }
        }
        addStatistics(operator, result);
    }

    @Override
    public void visit(ScalarGroupByOperator operator)
    {
        CardinalityStatistics stats = 
            CardinalityUtils.makeStatisticsFromHeading(
                operator.getHeading().getAttributes(), 1, 1);
        addStatistics(operator, stats);
    }

    @Override
    public void visit(TableScanOperator operator)
    {
        // if there already is an annotation then we don't touch it
        // it means that the table scan has been imploded
        if (Annotation.getCardinalityStatisticsAnnotation(operator) != null)
        {
            return;
        }
            
        try
        {
            // Go to physical data dictionary and get table scan statistics
            PhysicalSchema physicalSchema = 
                mDataDictionary.getTableSchema(
                        operator.getTableName()).getPhysicalSchema();

            if (physicalSchema == null)
            {
                LOG.debug("No physical schema found for table " + 
                        operator.getTableName());
                // make up a schema?
                CardinalityStatistics cardStats = 
                    CardinalityUtils.makeStatisticsFromHeading(
                            operator.getHeading().getAttributes(), 
                            100000, 10000);
                addStatistics(operator, cardStats);
//                throw new RuntimeException(
//                    "No physical schema data for table : " + 
//                    operator.getTableName());
            }
            else if (!(physicalSchema instanceof StatisticsPhysicalSchema))
            {
                LOG.debug("Simple physical schema found for table " + 
                        operator.getTableName());
                LOG.debug("Type = " + physicalSchema.getClass().getName());
                long cardinality = physicalSchema.getCardinality();
                LOG.debug("Cardinality = " + cardinality);
                // Convert simple physical schema into statistics schema
                CardinalityStatistics cardStats = 
                    CardinalityUtils.makeStatisticsFromHeading(
                            operator.getHeading().getAttributes(), 
                            cardinality, 
                            cardinality/10.0);
                addStatistics(operator, cardStats);
            }
            else
            {
                StatisticsPhysicalSchema statsPhysicalSchema =
                    (StatisticsPhysicalSchema) physicalSchema;
                
                CardinalityStatistics cardStats = 
                    statsPhysicalSchema.getCardinalityStatistics();
                
//                SimpleCardinalityStatistics renamedStatistics =
//                    new SimpleCardinalityStatistics();
//                for (Attribute attr : operator.getHeading().getAttributes())
//                {
//                    Attribute physAttr = operator.getPhysicalAttribute(attr);
//                    AttributeStatistics attrStats = cardStats.getStatistics(physAttr);
//                    renamedStatistics.addAttributeStatistics(attr, attrStats);
//                }
                addStatistics(operator, cardStats);
            }
            
        }
        catch(TableNotFoundException ex)
        {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void visit(ExchangeOperator operator)
    {
        CardinalityStatistics stats = getChildStatistics(operator, 0);
        addStatistics(operator, stats);
    }

    @Override
    public void visit(ProductOperator operator)
    {
        CardinalityStatistics lhsStats = getChildStatistics(operator, 0);
        CardinalityStatistics rhsStats = getChildStatistics(operator, 1);
        CardinalityStatistics result = 
            CardinalityUtils.product(lhsStats, rhsStats);
        addStatistics(operator, result);
    }

    @Override
    public void visit(InnerThetaJoinOperator operator)
    {
        CardinalityStatistics lhsStats = getChildStatistics(operator, 0);
        CardinalityStatistics rhsStats = getChildStatistics(operator, 1);
        CardinalityInnerThetaJoinVisitor visitor = 
            new CardinalityInnerThetaJoinVisitor(lhsStats, rhsStats);
        operator.getPredicate().getExpression().accept(visitor);
        addStatistics(operator, visitor.getResult());
    }

    @Override
    public void visit(SemiJoinOperator operator)
    {
        CardinalityStatistics lhsStats = getChildStatistics(operator, 0);
        CardinalityStatistics rhsStats = getChildStatistics(operator, 1);
        CardinalitySemiJoinVisitor visitor = 
            new CardinalitySemiJoinVisitor(lhsStats, rhsStats);
        operator.getPredicate().getExpression().accept(visitor);
        addStatistics(operator, visitor.getResult());
    }

    @Override
    public void visit(AntiSemiJoinOperator operator)
    {
        CardinalityStatistics lhsStats = getChildStatistics(operator, 0);
        CardinalityStatistics rhsStats = getChildStatistics(operator, 1);
        CardinalitySemiJoinVisitor visitor = 
            new CardinalitySemiJoinVisitor(lhsStats, rhsStats);
        operator.getPredicate().getExpression().accept(visitor);
        CardinalityStatistics result = 
            CardinalityUtils.difference(lhsStats, visitor.getResult());
        addStatistics(operator, result);
    }

    @Override
    public void visit(FullOuterJoinOperator operator)
    {
        CardinalityStatistics lhsStats = getChildStatistics(operator, 0);
        CardinalityStatistics rhsStats = getChildStatistics(operator, 1);
        CardinalityInnerThetaJoinVisitor visitor = 
            new CardinalityInnerThetaJoinVisitor(lhsStats, rhsStats);
        operator.getPredicate().getExpression().accept(visitor);
        CardinalityStatistics result = 
            CardinalityUtils.sum(visitor.getResult(), lhsStats);
        result = CardinalityUtils.sum(result, rhsStats);
        addStatistics(operator, result);
    }

    @Override
    public void visit(LeftOuterJoinOperator operator)
    {
        // cardinality estimate is the size of LEFT INNER_JOIN RIGHT
        // plus size of LEFT
        
        CardinalityStatistics lhsStats = getChildStatistics(operator, 0);
        CardinalityStatistics rhsStats = getChildStatistics(operator, 1);
        CardinalityInnerThetaJoinVisitor visitor = 
            new CardinalityInnerThetaJoinVisitor(lhsStats, rhsStats);
        operator.getPredicate().getExpression().accept(visitor);
        CardinalityStatistics result = 
            CardinalityUtils.sum(visitor.getResult(), lhsStats);
        addStatistics(operator, result);
    }

    @Override
    public void visit(RightOuterJoinOperator operator)
    {
        // cardinality estimate is the size of LEFT INNER_JOIN RIGHT
        // plus size of RIGHT
        
        CardinalityStatistics lhsStats = getChildStatistics(operator, 0);
        CardinalityStatistics rhsStats = getChildStatistics(operator, 1);
        CardinalityInnerThetaJoinVisitor visitor = 
            new CardinalityInnerThetaJoinVisitor(lhsStats, rhsStats);
        operator.getPredicate().getExpression().accept(visitor);
        CardinalityStatistics result = 
            CardinalityUtils.sum(visitor.getResult(), rhsStats);
        addStatistics(operator, result);
    }

    @Override
    public void visit(ApplyOperator operator)
    {
        CardinalityStatistics lhsStats = getChildStatistics(operator, 0);
        CardinalityStatistics rhsStats = getChildStatistics(operator, 1);

        // Get wrapped operator
        Operator wrappedOperator = operator.getOperator();
        
        // Estimate statistics for wrapped operator
        CardinalityEstimator visitor = new CardinalityEstimator();
        visitor.setDataDictionary(mDataDictionary);
        visitor.setApplyWrapper(operator);
        wrappedOperator.accept(visitor);

        CardinalityStatistics joinStats = getStatistics(wrappedOperator);
        int index = operator.getOuterRelationChildIndex();

        double totalNumRows = joinStats.getCardinality() * 
                                (index == 0 ? lhsStats.getCardinality() : 
                                              rhsStats.getCardinality() );
        
        CardinalityStatistics result = 
            CardinalityUtils.rescale(joinStats, totalNumRows);
        addStatistics(operator, result);
    }

    @Override
    public void visit(UnionOperator operator)
    {
        CardinalityStatistics lhsStats = getChildStatistics(operator, 0);
        CardinalityStatistics rhsStats = getChildStatistics(operator, 1);
        addStatistics(operator, CardinalityUtils.sum(lhsStats, rhsStats));
    }

    @Override
    public void visit(IntersectionOperator operator)
    {
        // upper bound - one input is a subset of the other
        CardinalityStatistics lhsStats = getChildStatistics(operator, 0);
        CardinalityStatistics rhsStats = getChildStatistics(operator, 1);
        double lhsCards = lhsStats.getCardinality();
        double rhsCards = rhsStats.getCardinality();
        CardinalityStatistics result = 
            (lhsCards < rhsCards) ? lhsStats : rhsStats;
        addStatistics(operator, result);
    }

    @Override
    public void visit(DifferenceOperator operator)
    {
        CardinalityStatistics lhsStats = getChildStatistics(operator, 0);
        CardinalityStatistics rhsStats = getChildStatistics(operator, 1);
        CardinalityStatistics result = 
            CardinalityUtils.difference(lhsStats, rhsStats);
        addStatistics(operator, result);
    }

    @Override
    public void visit(UnaryRelFunctionOperator operator)
    {
        throw new UnsupportedOperationException(
                "Operator type " + operator + " is not supported.");
    }

    @Override
    public void visit(BinaryRelFunctionOperator operator)
    {
        throw new UnsupportedOperationException(
                "Operator type " + operator + " is not supported.");
    }

    @Override
    public void visit(ScanRelFunctionOperator operator)
    {
        throw new UnsupportedOperationException(
                "Operator type " + operator + " is not supported.");
    }

    @Override
    public void visit(Operator operator)
    {
        throw new UnsupportedOperationException(
                "Operator type " + operator + " is not supported.");
    }

    /**
     * Adds cardinality statistics and cardinality annotations to the given
     * operator.
     * 
     * @param operator
     *            operator to be annotated
     * @param stats
     *            cardinality statistics
     */
    private void addStatistics(
        Operator operator, CardinalityStatistics stats)
    {
        Annotation.addCardinalityStatisticsAnnotation(operator, stats);
        Annotation.addCardinalityAnnotation(operator, stats.getCardinality());
    }

    /**
     * Gets the statistics annotation of the child at the given index.
     * 
     * @param operator
     *            operator
     * @param index
     *            child index
     * @return cardinality statistics of the child
     * @throws RuntimeException
     *             if the child operator has no statistics annotation
     */
    private CardinalityStatistics getChildStatistics(
            Operator operator, int index)
    {
        Operator child = getChild(operator, index);
        if (child.getID()== OperatorID.TOP){
        	child = getChild(child, index);
        }
        return getStatistics(child);
    }

    /**
     * Returns the cardinality statistics annotation of the given operator or
     * raises an exception if the annotation is not set.
     * 
     * @param operator
     *            operator
     * @return cardinality statistics for the operator
     * @throws RuntimeException
     *             if the operator has no statistics annotation
     */
    private CardinalityStatistics getStatistics(Operator operator)
    {
        CardinalityStatistics cardStats = 
            Annotation.getCardinalityStatisticsAnnotation(operator);
        
        if (cardStats == null)
        {
            throw new RuntimeException(
                "Operator " + operator + 
                " has no CardinalityStatistics annotation");
            
        }
        else
        {
            return cardStats;
        }
    }

    /**
     * Retrieves the child of the given operator within the LQP. If the operator
     * is wrapped by an apply operator then the apply operator's child is
     * returned.
     * 
     * @param operator
     *            parent operator
     * @param index
     *            child index
     * @return child of the operator in the query plan
     */
    private Operator getChild(Operator operator, int index)
    {
        if (mWrapperApplyOperator!= null)
        {
            return mWrapperApplyOperator.getChild(index);
        }
        return operator.getChild(index);
    }


}
