package uk.org.ogsadai.dqp.lqp.cardinality;

import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import uk.org.ogsadai.dqp.lqp.Annotation;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.AttributeImpl;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.RenameMap;
import uk.org.ogsadai.dqp.lqp.exceptions.AmbiguousAttributeException;
import uk.org.ogsadai.dqp.lqp.exceptions.AmbiguousMappingException;
import uk.org.ogsadai.dqp.lqp.operators.ApplyOperator;
import uk.org.ogsadai.expression.ComparisonExpression;
import uk.org.ogsadai.expression.ExpressionEvaluationException;

public class CardinalityUtils
{
    public static CardinalityStatistics makeRenamedCopy(
        CardinalityStatistics stats,
        String tableName)
    {
        SimpleCardinalityStatistics result = 
            new SimpleCardinalityStatistics();
        for (Entry<Attribute,AttributeStatistics> entry : 
             stats.getStatistics().entrySet())
        {
            result.addAttributeStatistics(
                new AttributeImpl(entry.getKey().getName(), tableName ),
                entry.getValue());
        }
        return result;
    }

    /**
     * Maps renamed attributes in the input cardinality statistics to the
     * original names and creates a copy of the statistics with the original
     * attribute names.
     * 
     * @param stats
     *            input statistics
     * @param renameMap
     *            rename map
     * @return a copy of the input statistics with the original attribute names
     * @throws AmbiguousMappingException
     * @throws AmbiguousAttributeException
     */
    public static CardinalityStatistics mapRenamedToOriginal(
            CardinalityStatistics stats, RenameMap renameMap)
    throws AmbiguousMappingException, AmbiguousAttributeException
    {
        SimpleCardinalityStatistics result = 
            new SimpleCardinalityStatistics();
        for (Entry<Attribute, AttributeStatistics> entry : 
            stats.getStatistics().entrySet())
        {
            result.addAttributeStatistics(
                    renameMap.getOriginalAttribute(
                            entry.getKey()), entry.getValue());
        }
        return result;
    }

    public static CardinalityStatistics difference(
        CardinalityStatistics stats0,
        CardinalityStatistics stats1)
    {
        SimpleCardinalityStatistics result = new SimpleCardinalityStatistics();
        
        for (Entry<Attribute, AttributeStatistics> entry : 
            stats0.getStatistics().entrySet())
        {
            AttributeStatistics attrStats = 
                stats1.getStatistics(entry.getKey());
            
            AttributeStatistics resultAttrStats =
                difference(entry.getValue(), attrStats);
            result.addAttributeStatistics(entry.getKey(), resultAttrStats);
        }
        return result;
    }    
    
    private static AttributeStatistics difference(
        AttributeStatistics attrStats0,
        AttributeStatistics attrStats1)
    {
        if (areBothHistogramStatistics(attrStats0, attrStats1))
        {
            return HistogramCardinalityUtils.difference(
                (HistogramBasedAttributeStatistics) attrStats0,
                (HistogramBasedAttributeStatistics) attrStats1);
        }
        else
        {
            return ScalarCardinalityUtils.difference(attrStats0, attrStats1);
        }
    }
    
    public static CardinalityStatistics sum(
            CardinalityStatistics stats0,
            CardinalityStatistics stats1)
    {
        SimpleCardinalityStatistics result = new SimpleCardinalityStatistics();
            
        for (Entry<Attribute, AttributeStatistics> entry : 
            stats0.getStatistics().entrySet())
        {
            if (stats1.contains(entry.getKey()))
            {
                AttributeStatistics attrStats = 
                    stats1.getStatistics(entry.getKey());
                
                AttributeStatistics resultAttrStats =
                    sum(entry.getValue(), attrStats);
                result.addAttributeStatistics(entry.getKey(), resultAttrStats);
            }
            else
            {
                // add NULL for each other attribute
                result.addAttributeStatistics(
                        entry.getKey(),
                        new ScalarAttributeStatistics(entry.getValue().getNumRows(), 1));
            }
        }
        return result;
    }
        
    private static AttributeStatistics sum(
            AttributeStatistics attrStats0,
            AttributeStatistics attrStats1)
    {
        if (areBothHistogramStatistics(attrStats0, attrStats1))
        {
            return HistogramCardinalityUtils.sum(
                (HistogramBasedAttributeStatistics) attrStats0,
                (HistogramBasedAttributeStatistics) attrStats1);
        }
        else
        {
            return ScalarCardinalityUtils.sum(attrStats0, attrStats1);
        }
        
    }

    public static CardinalityStatistics eliminateDuplicates(
            CardinalityStatistics stats) 
    {
        SimpleCardinalityStatistics result = new SimpleCardinalityStatistics();
        for (Entry<Attribute, AttributeStatistics> entry : 
            stats.getStatistics().entrySet())
        {
            result.addAttributeStatistics(
                    entry.getKey(),
                    eliminateDuplicates(entry.getValue()));
        }
        return result;
    }
    
    private static AttributeStatistics eliminateDuplicates(
            AttributeStatistics attrStats)
    {
        if (attrStats instanceof HistogramBasedAttributeStatistics)
        {
            return HistogramCardinalityUtils.processDuplicateElimination(
                    ((HistogramBasedAttributeStatistics)attrStats));
        }
        else
        {
            return ScalarCardinalityUtils.processDuplicateElimination(attrStats);
        }
    }


    /**
     * Processes attribute statistics involved in a SELECT operator of the
     * type attr op attr. 
     * 
     * @param lhsStats  statistics for the left hand side attribute
     * @param operator  the arithmetic operator
     * @param rhsStats  statistics for the right hand side attribute
     * 
     * @return a list containing two attribute statistics.  The first is the
     *         new statistics for the left hand attribute, the second is the new
     *         statistics for the right hand side attribute.
     */
    public static List<AttributeStatistics> processSelectAttrOperatorAttr(
        AttributeStatistics lhsStats,
        ArithmeticOperator op, 
        AttributeStatistics rhsStats)
    {
        if (areBothHistogramStatistics(lhsStats, rhsStats))
        {
            return HistogramCardinalityUtils.processSelectAttrOperatorAttr(
                (HistogramBasedAttributeStatistics) lhsStats,
                op,
                (HistogramBasedAttributeStatistics) rhsStats);
        }
        else
        {
            return ScalarCardinalityUtils.processSelectAttrOperatorAttr(
                lhsStats, op, rhsStats);
        }
    }
    
    public static AttributeStatistics processSelectAttrOperatorBoundAttr(
        AttributeStatistics lhsStats, 
        ArithmeticOperator operator,
        AttributeStatistics rhsStats)
    {
        // We only handle the equals operator using histograms, for everything
        // else we revert to basic scalar statistics.
        
        if (areBothHistogramStatistics(lhsStats, rhsStats) &&
            operator == ArithmeticOperator.EQUAL)
        {
            return HistogramCardinalityUtils.processSelectAttrEqualsBoundAttr(
                (HistogramBasedAttributeStatistics) lhsStats,
                (HistogramBasedAttributeStatistics) rhsStats);
        }
        else
        {
            return ScalarCardinalityUtils.processSelectAttrOperatorBoundAttr(
                lhsStats, operator, rhsStats);
        }
    }
    
    public static CardinalityStatistics product(
            CardinalityStatistics lhsStats,
            CardinalityStatistics rhsStats) 
    {
        double lhsCard = lhsStats.getCardinality();
        double rhsCard = rhsStats.getCardinality();
        SimpleCardinalityStatistics result = 
            new SimpleCardinalityStatistics();
        addProductStatistics(result, lhsStats, rhsCard);
        addProductStatistics(result, rhsStats, lhsCard);
        return result;
    }
    
    private static void addProductStatistics(
            SimpleCardinalityStatistics result, 
            CardinalityStatistics inputStats, 
            double cardinality)
    {
        for (Entry<Attribute, AttributeStatistics> entry : 
            inputStats.getStatistics().entrySet())
        {
            AttributeStatistics productAttrStats; 
            if (entry.getValue() instanceof HistogramBasedAttributeStatistics)
            {
                productAttrStats = 
                    HistogramCardinalityUtils.processProduct(
                        (HistogramBasedAttributeStatistics)entry.getValue(), 
                        cardinality);
            }
            else
            {
                productAttrStats = 
                    ScalarCardinalityUtils.processProduct(
                            entry.getValue(), cardinality);
            }
            result.addAttributeStatistics(entry.getKey(), productAttrStats);
        }

    }

    public static AttributeStatistics equiJoin(
            AttributeStatistics lhsStats, 
            AttributeStatistics rhsStats)
    {
        if (areBothHistogramStatistics(lhsStats, rhsStats))
        {
            return HistogramCardinalityUtils.processEquiJoin(
                    (HistogramBasedAttributeStatistics) lhsStats,
                    (HistogramBasedAttributeStatistics) rhsStats);
        }
        else
        {
            return ScalarCardinalityUtils.processEquiJoin(
                    lhsStats, rhsStats);
        }
    }
    
    public static AttributeStatistics equiSemiJoin(
            AttributeStatistics lhsStats, AttributeStatistics rhsStats) 
    {
        if (areBothHistogramStatistics(lhsStats, rhsStats))
        {
            return HistogramCardinalityUtils.processEquiSemiJoin(
                    (HistogramBasedAttributeStatistics) lhsStats,
                    (HistogramBasedAttributeStatistics) rhsStats);
        }
        else
        {
            return ScalarCardinalityUtils.processEquiSemiJoin(
                    lhsStats, rhsStats);
        }
    }

    public static AttributeStatistics groupByAttribute(
            AttributeStatistics attrStats) 
    {
        if (attrStats instanceof HistogramBasedAttributeStatistics)
        {
            // eliminating duplicates is the same as group by  
            // for the grouping attribute
            return HistogramCardinalityUtils.processDuplicateElimination(
                    (HistogramBasedAttributeStatistics)attrStats);
        }
        else
        {
            double numValues = attrStats.getNumValues();
            return new ScalarAttributeStatistics(numValues, numValues);
        }
    }

    public static CardinalityStatistics processConstOperatorConst(
            CardinalityStatistics inputStatistics,
            ComparisonExpression expression)
    {
        try
        {
            if (expression.evaluate(null))
            {
                return inputStatistics;
            }
            else
            {
                // if the expression is false then the result set is empty
                return CardinalityUtils.rescaleToZero(inputStatistics);
            }
        } 
        catch (ExpressionEvaluationException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    private static boolean areBothHistogramStatistics(
        AttributeStatistics attrStats0, AttributeStatistics attrStats1)
    {
        return (attrStats0 instanceof HistogramBasedAttributeStatistics &&
                attrStats1 instanceof HistogramBasedAttributeStatistics);
    }

    /**
     * Copies all the attributes of the given cardinality statistics to a new
     * cardinality statistics objects, rescaling them to align with the given
     * number of rows.
     * 
     * @param stats
     *            cardinality statistics
     * @param totalNumRows
     *            new number of rows
     */
    public static CardinalityStatistics rescale(
            CardinalityStatistics stats,
            double totalNumRows)
    {
        SimpleCardinalityStatistics result = new SimpleCardinalityStatistics();
        for (Entry<Attribute, AttributeStatistics> entry : 
            stats.getStatistics().entrySet())
        {
            result.addAttributeStatistics(
                    entry.getKey(),
                    entry.getValue().rescale(totalNumRows));
        }
        return result;
    }

    /**
     * Copies all the attributes of the given cardinality statistics to a new
     * cardinality statistics objects with zero rows.
     * 
     * @param stats
     *            cardinality statistics
     */
    public static CardinalityStatistics rescaleToZero(CardinalityStatistics stats)
    {
        SimpleCardinalityStatistics result = new SimpleCardinalityStatistics();
        for (Entry<Attribute, AttributeStatistics> entry : 
            stats.getStatistics().entrySet())
        {
            result.addAttributeStatistics(
                    entry.getKey(),
                    ScalarAttributeStatistics.ZERO);
        }
        return result;
    }

    /**
     * Copies all the attributes of the given cardinality statistics to a new
     * cardinality statistics replacing the specific old attribute statistics
     * to the given new attribute statistics and rescaling all others to align
     * with the given new attribute statistics.
     * 
     * @param cardinalityStatistics   cardinality statistics
     * @param oldAttributeStatistics  attribute statistics to be replaced
     * @param newAttributeStatistics  attribute statistics to add instead
     * 
     * @return resulting cardinality statistics
     */
    public static CardinalityStatistics replaceAndRescale(
        CardinalityStatistics cardinalityStatistics,
        AttributeStatistics oldAttributeStatistics,
        AttributeStatistics newAttributeStatistics)
    {
        SimpleCardinalityStatistics result = new SimpleCardinalityStatistics();
        
        double newNumRows = newAttributeStatistics.getNumRows();
        
        for (Entry<Attribute, AttributeStatistics> entry : 
            cardinalityStatistics.getStatistics().entrySet())
        {
            if (entry.getValue() == oldAttributeStatistics)
            {
                result.addAttributeStatistics(
                    entry.getKey(), newAttributeStatistics);
            }
            else
            {
                result.addAttributeStatistics(
                    entry.getKey(),
                    entry.getValue().rescale(newNumRows));
            }
        }
        
        return result;
    }

    /**
     * Copies all the attributes of the given cardinality statistics to a new
     * cardinality statistics replacing the specific old attribute statistics
     * to the given new attribute statistics and rescaling all others to align
     * with the given new attribute statistics.
     * 
     * @param cardinalityStatistics   cardinality statistics
     * @param oldAttributeStatistics0 attribute statistics to be replaced
     * @param newAttributeStatistics0 attribute statistics to add instead
     * @param oldAttributeStatistics1 attribute statistics to be replaced
     * @param newAttributeStatistics1 attribute statistics to add instead
     * 
     * @return resulting cardinality statistics
     */
    public static CardinalityStatistics replaceAndRescale(
        CardinalityStatistics cardinalityStatistics,
        AttributeStatistics oldAttributeStatistics0,
        AttributeStatistics newAttributeStatistics0,
        AttributeStatistics oldAttributeStatistics1,
        AttributeStatistics newAttributeStatistics1)
    {
        SimpleCardinalityStatistics result = new SimpleCardinalityStatistics();
        
        double newNumRows = newAttributeStatistics0.getNumRows();
        
        for (Entry<Attribute, AttributeStatistics> entry : 
            cardinalityStatistics.getStatistics().entrySet())
        {
            if (entry.getValue() == oldAttributeStatistics0)
            {
                result.addAttributeStatistics(
                    entry.getKey(), newAttributeStatistics0);
            }
            else if (entry.getValue() == oldAttributeStatistics1)
            {
                result.addAttributeStatistics(
                    entry.getKey(), newAttributeStatistics1);
            }
            else
            {
                result.addAttributeStatistics(
                    entry.getKey(),
                    entry.getValue().rescale(newNumRows));
            }
        }
        
        return result;
    }
    
    /**
     * Gets the attribute statistics for an attribute bound via an apply 
     * operator. The method walks up the query plan from the given operator
     * to find the apply operator and then gets the statistics for the 
     * required attribute from the outer relation child of the apply operator.
     * 
     * @param operator  operator that requires a bound attribute
     * @param attr      attribute for which statistics are to be returned
     * 
     * @return the attribute statistics
     */
    public static AttributeStatistics getStatsForBoundAttribute(
        Operator operator,
        Attribute attr)
    {
        Operator op = operator;
        
        // Run up until the APPLY
        while( op.getID() != OperatorID.APPLY)
        {
            op = op.getParent();
            
            if (op.getID() == OperatorID.NIL) 
                throw new RuntimeException(
                    "Failed to find an expected APPLY operator");
        }
        
        ApplyOperator applyOperator = (ApplyOperator) op;
        Operator outerRelationChild = 
            applyOperator.getChild(applyOperator.getOuterRelationChildIndex());
        
        CardinalityStatistics cardStats = 
            Annotation.getCardinalityStatisticsAnnotation(outerRelationChild);

        return cardStats.getStatistics(attr);
    }
    
    /**
     * Creates new scalar attribute statistics for all attributes in the
     * heading, with the given number of rows and number of values.
     * 
     * @param heading
     *            attributes
     * @param numRows
     *            number of rows in the scalar statistics
     * @param numValues
     *            number of values in the scalar statistics
     * @return cardinality statistics
     */
    public static CardinalityStatistics makeStatisticsFromHeading(
            Collection<Attribute> heading, double numRows, double numValues)
    {
        SimpleCardinalityStatistics result = 
            new SimpleCardinalityStatistics();
        ScalarAttributeStatistics stats = 
            new ScalarAttributeStatistics(numRows, numValues);
        for (Attribute attr: heading)
        {
            if (attr.isKey())
            {
                stats = new ScalarAttributeStatistics(numRows, numRows);
            }
            result.addAttributeStatistics(attr, stats);
        }
        return result;
    }

}
