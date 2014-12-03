package uk.org.ogsadai.dqp.lqp.cardinality;

import java.util.Arrays;
import java.util.List;

public class ScalarCardinalityUtils
{

    public static AttributeStatistics difference(
        AttributeStatistics attrStats0, AttributeStatistics attrStats1)
    {
        return new ScalarAttributeStatistics(
                attrStats0.getNumRows() - attrStats1.getNumRows(),
                attrStats0.getNumValues() - attrStats1.getNumValues());
    }

    public static List<AttributeStatistics> processSelectAttrOperatorAttr(
        AttributeStatistics lhsStats, 
        ArithmeticOperator op,
        AttributeStatistics rhsStats)
    {
        double inputNumRows = lhsStats.getNumRows();
        double lhsNumValues = lhsStats.getNumValues();
        double rhsNumValues = rhsStats.getNumValues();
        AttributeStatistics lhsOutputStats, rhsOutputStats;
        switch (op)
        {
        case EQUAL:
        {
            // below is the same as 
            // 1 / Math.min(lhsNumValues, rhsNumValues) *
            // inputNumRows * Math.min(lhsNumValues, rhsNumValues) /
            // Math.max(lhsNumValues, rhsNumValues)
            double numRows = inputNumRows / Math.max(lhsNumValues, rhsNumValues);
            double numValues = Math.min(lhsNumValues, rhsNumValues);
            rhsOutputStats = lhsOutputStats = 
                new ScalarAttributeStatistics(numRows, numValues);
            break;
        }
        case GREATER_THAN:
        case GREATER_THAN_OR_EQUAL:
        case LESS_THAN:
        case LESS_THAN_OR_EQUAL:
        {
            double numRows = inputNumRows * .5;
            
            lhsOutputStats = 
                new ScalarAttributeStatistics(numRows, lhsNumValues * .5);
            rhsOutputStats = 
                new ScalarAttributeStatistics(numRows, rhsNumValues * .5);
            break;
        }
        case NOT_EQUAL:
        {
            double numRows = inputNumRows / Math.max(lhsNumValues, rhsNumValues);
            double numValues = Math.min(lhsNumValues, rhsNumValues);
            rhsOutputStats = lhsOutputStats = 
                new ScalarAttributeStatistics(1-numRows, 1-numValues);
            break;

        }
        default: throw new RuntimeException("Unsupported comparison operator : " + op);
        }
        return Arrays.asList(lhsOutputStats, rhsOutputStats);
    }

    public static AttributeStatistics processSelectAttrOperatorBoundAttr(
        AttributeStatistics lhsStats, 
        ArithmeticOperator operator,
        AttributeStatistics rhsStats)
    {
        if (operator == ArithmeticOperator.EQUAL)
        {
            return processEquiJoin(lhsStats, rhsStats);
        }
        else
        {
            throw new RuntimeException(
                    "Unsupported select condition operator " + operator);
        }
    }

    public static AttributeStatistics processProduct(
            AttributeStatistics attrStats,
            double rhsCard)
    {
        return new ScalarAttributeStatistics(
                attrStats.getNumRows() * rhsCard, 
                attrStats.getNumValues());
    }

    public static AttributeStatistics processEquiJoin(
            AttributeStatistics lhsStats, AttributeStatistics rhsStats)
    {
        // we create a product and then apply a select
        double productNumRows = lhsStats.getNumRows() * rhsStats.getNumRows();
        double lhsNumValues = lhsStats.getNumValues();
        double rhsNumValues = rhsStats.getNumValues();
        double numRows = productNumRows / Math.max(lhsNumValues, rhsNumValues);
        double numValues = Math.min(lhsNumValues, rhsNumValues);
        return new ScalarAttributeStatistics(numRows, numValues);
    }

    public static AttributeStatistics processEquiSemiJoin(
            AttributeStatistics lhsStats, AttributeStatistics rhsStats) 
    {
        double numRows = Math.min(lhsStats.getNumRows(), rhsStats.getNumRows());
        return new ScalarAttributeStatistics(numRows);
    }

    public static AttributeStatistics processDuplicateElimination(
            AttributeStatistics attrStats)
    {
        return new ScalarAttributeStatistics(
                attrStats.getNumValues(), 
                attrStats.getNumValues());
    }

    public static AttributeStatistics sum(
            AttributeStatistics attrStats0,
            AttributeStatistics attrStats1) 
    {
        // is this the correct approach ... ? adding up values
        return new ScalarAttributeStatistics(
                attrStats0.getNumRows() + attrStats1.getNumRows(),
                attrStats0.getNumValues() + attrStats1.getNumValues());
    }

}
