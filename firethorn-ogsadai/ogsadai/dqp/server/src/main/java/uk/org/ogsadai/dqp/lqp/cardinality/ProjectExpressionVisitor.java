package uk.org.ogsadai.dqp.lqp.cardinality;

import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.AttributeImpl;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpression;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpressionVisitor;
import uk.org.ogsadai.expression.arithmetic.Constant;
import uk.org.ogsadai.expression.arithmetic.Div;
import uk.org.ogsadai.expression.arithmetic.ExecutableFunctionExpression;
import uk.org.ogsadai.expression.arithmetic.Minus;
import uk.org.ogsadai.expression.arithmetic.Mult;
import uk.org.ogsadai.expression.arithmetic.Plus;
import uk.org.ogsadai.expression.arithmetic.Star;
import uk.org.ogsadai.expression.arithmetic.TableColumn;

public class ProjectExpressionVisitor implements ArithmeticExpressionVisitor
{
    private AttributeStatistics mAttributeStats;
    private CardinalityStatistics mStatistics;
    
    public ProjectExpressionVisitor(CardinalityStatistics stats) 
    {
        mStatistics = stats;
    }
    
    public static AttributeStatistics getAttributeStatistics(
            CardinalityStatistics inputStats, 
            ArithmeticExpression expression)
    {
        ProjectExpressionVisitor visitor = 
            new ProjectExpressionVisitor(inputStats);
        expression.accept(visitor);
        return visitor.mAttributeStats;
    }
    
    @Override
    public void visitTableColumn(TableColumn tableColumn) 
    {
        Attribute attribute = 
            new AttributeImpl(tableColumn.getName(), tableColumn.getSource());
        AttributeStatistics inputStats = mStatistics.getStatistics(attribute);
        mAttributeStats = inputStats;
    }

    @Override
    public void visitConstant(Constant expression) 
    {
        Object constant = expression.getResult();
        if (constant instanceof Number)
        {
            HistogramBasedAttributeStatistics attrStats = 
                new HistogramBasedAttributeStatistics();
            double value = ((Number)constant).doubleValue();
            AttributeHistogramBin bin = 
                new AttributeHistogramBin(
                        value, true,
                        value, true,
                        mStatistics.getCardinality(), 1);
            attrStats.addBin(bin);
            mAttributeStats = attrStats;
        }
        else
        {
            mAttributeStats = new ScalarAttributeStatistics(
                    mStatistics.getCardinality(), 1);
        }
    }

    @Override
    public void visitPlus(Plus expression) 
    {
        makeNumRowsOnlyAttributeStats();
    }

    @Override
    public void visitMinus(Minus expression) 
    {
        makeNumRowsOnlyAttributeStats();
    }

    @Override
    public void visitDiv(Div expression) 
    {
        makeNumRowsOnlyAttributeStats();
    }

    @Override
    public void visitMult(Mult expression) 
    {
        makeNumRowsOnlyAttributeStats();
    }

    @Override
    public void visitFunction(ExecutableFunctionExpression function) 
    {
        makeNumRowsOnlyAttributeStats();
    }

    @Override
    public void visitStar(Star expression) 
    {
    }
    
    private void makeNumRowsOnlyAttributeStats()
    {
        double numRows = mStatistics.getCardinality();
        mAttributeStats = new ScalarAttributeStatistics(numRows);
    }

}
