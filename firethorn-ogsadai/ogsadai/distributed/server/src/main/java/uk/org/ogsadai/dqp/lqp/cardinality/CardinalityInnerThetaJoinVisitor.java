package uk.org.ogsadai.dqp.lqp.cardinality;

import java.util.Map.Entry;

import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.AttributeImpl;
import uk.org.ogsadai.expression.AndExpression;
import uk.org.ogsadai.expression.ComparisonExpression;
import uk.org.ogsadai.expression.EqualExpression;
import uk.org.ogsadai.expression.Expression;
import uk.org.ogsadai.expression.ExpressionVisitor;
import uk.org.ogsadai.expression.GreaterThanExpression;
import uk.org.ogsadai.expression.GreaterThanOrEqualExpression;
import uk.org.ogsadai.expression.InExpression;
import uk.org.ogsadai.expression.IsNullExpression;
import uk.org.ogsadai.expression.LessThanExpression;
import uk.org.ogsadai.expression.LessThanOrEqualExpression;
import uk.org.ogsadai.expression.LikeExpression;
import uk.org.ogsadai.expression.NotEqualExpression;
import uk.org.ogsadai.expression.NotExpression;
import uk.org.ogsadai.expression.OrExpression;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpression;
import uk.org.ogsadai.expression.arithmetic.TableColumn;
import uk.org.ogsadai.expression.arithmetic.visitors.OperandTypeArithmeticExprVisitor.OperandType;

public class CardinalityInnerThetaJoinVisitor implements ExpressionVisitor
{
    private CardinalityStatistics mLeftChild;
    private CardinalityStatistics mRightChild;
    private CardinalityStatistics mResult;

    public CardinalityInnerThetaJoinVisitor(
            CardinalityStatistics lhsStats,
            CardinalityStatistics rhsStats)
    {
        mLeftChild = lhsStats;
        mRightChild = rhsStats;
        mResult = null;
    }

    public CardinalityStatistics getResult()
    {
        if (mResult == null)
        {
            // TODO: Handle more things here - or have a better default
            throw new RuntimeException(
                "Not handling some type of Inner Theta Join properly");
        }
        return mResult;
    }

    @Override
    public void visitAndExpression(AndExpression expression)
    {
        mResult = processProductSelect(mLeftChild, mRightChild, expression);
    }

    @Override
    public void visitOrExpression(OrExpression expression) 
    {
        mResult = processProductSelect(mLeftChild, mRightChild, expression);
    }

    @Override
    public void visitNotExpression(NotExpression expression) 
    {
        mResult = processProductSelect(mLeftChild, mRightChild, expression);
    }

    @Override
    public void visitLessThanExpression(LessThanExpression expression) 
    {
        mResult = processProductSelect(mLeftChild, mRightChild, expression);
    }

    @Override
    public void visitLessThanOrEqualExpression(
            LessThanOrEqualExpression expression) 
    {
        mResult = processProductSelect(mLeftChild, mRightChild, expression);
    }

    @Override
    public void visitGreaterThanExpression(GreaterThanExpression expression) 
    {
        mResult = processProductSelect(mLeftChild, mRightChild, expression);
    }

    @Override
    public void visitGreaterThanOrEqualExpression(
            GreaterThanOrEqualExpression expression) 
    {
        mResult = processProductSelect(mLeftChild, mRightChild, expression);
    }

    @Override
    public void visitEqualExpression(EqualExpression expression)
    {
        mResult = processArithmeticOperator(
                mLeftChild, mRightChild,
                expression,
                ArithmeticOperator.EQUAL);
        
        if (mResult == null)
        {
            mResult = processProductSelect(mLeftChild, mRightChild, expression);
        }
    }

    @Override
    public void visitNotEqualExpression(NotEqualExpression expression) 
    {
        mResult = processProductSelect(mLeftChild, mRightChild, expression);
    }


    @Override
    public void visitIsNullExpression(IsNullExpression expression) 
    {
        mResult = processProductSelect(mLeftChild, mRightChild, expression);
    }

    @Override
    public void visitLikeExpression(LikeExpression expression) 
    {
        mResult = processProductSelect(mLeftChild, mRightChild, expression);
    }

    @Override
    public void visitInExpression(InExpression expression)
    {
        mResult = processProductSelect(mLeftChild, mRightChild, expression);
    }

    private CardinalityStatistics processArithmeticOperator(
            CardinalityStatistics lhsStats,
            CardinalityStatistics rhsStats,
            ComparisonExpression expression, ArithmeticOperator op)
    {
        ArithmeticExpression expressions[] = 
            ExpressionUtilities.getArithmeticExpressions(expression);
        OperandType[] operandTypes = ExpressionUtilities.getOperandTypes(expressions);
            
        if (operandTypes[0] == OperandType.ATTR && 
                operandTypes[1] == OperandType.ATTR && 
                op == ArithmeticOperator.EQUAL)
        {   
            return processAttrEqualsAttr(
                    lhsStats, rhsStats, 
                    (TableColumn)expressions[0], 
                    (TableColumn)expressions[1]);
        }
        else
        {
            return null;
        }
    }

    private CardinalityStatistics processAttrEqualsAttr(
            CardinalityStatistics lhsStats, 
            CardinalityStatistics rhsStats, 
            TableColumn tableColumnLeft, 
            TableColumn tableColumnRight)
    {
        // Get attribute
        Attribute attr1 = new AttributeImpl(
            tableColumnLeft.getName(), tableColumnLeft.getSource());
        Attribute attr2 = new AttributeImpl(
                tableColumnRight.getName(), tableColumnRight.getSource());
        
        // Find statistics
        AttributeStatistics lhsAttrStats;
        AttributeStatistics rhsAttrStats;
        
        if (lhsStats.contains(attr1))
        {
            lhsAttrStats = lhsStats.getStatistics(attr1);
            rhsAttrStats = rhsStats.getStatistics(attr2);
        }
        else
        {
            lhsAttrStats = lhsStats.getStatistics(attr2);
            rhsAttrStats = rhsStats.getStatistics(attr1);
        }
        
        AttributeStatistics resultHistogram = 
            CardinalityUtils.equiJoin(lhsAttrStats, rhsAttrStats);
       
        // Rescale all other attributes
        CardinalityStatistics lhsResults = 
            CardinalityUtils.replaceAndRescale(
                    lhsStats, lhsAttrStats, resultHistogram);
        CardinalityStatistics rhsResults = 
            CardinalityUtils.replaceAndRescale(
                    rhsStats, rhsAttrStats, resultHistogram);
        SimpleCardinalityStatistics result = new SimpleCardinalityStatistics();
        addAttributeStatistics(result, resultHistogram, lhsResults, rhsResults);
        return result;
    }

    private void addAttributeStatistics(
            SimpleCardinalityStatistics resultStats,
            AttributeStatistics joinAttribute,
            CardinalityStatistics... statsToAdd) 
    {
        boolean addedJoinAttr = false;
        for (CardinalityStatistics stats : statsToAdd)
        {
            for (Entry<Attribute, AttributeStatistics> entry : 
                stats.getStatistics().entrySet())
            {
                if (joinAttribute != entry.getKey())
                {
                    resultStats.addAttributeStatistics(
                            entry.getKey(), entry.getValue());
                }
                else if (!addedJoinAttr)
                {
                    resultStats.addAttributeStatistics(
                            entry.getKey(), entry.getValue());
                    addedJoinAttr = true;
                }
            }
        }
    }
    
    private static CardinalityStatistics processProductSelect(
            CardinalityStatistics leftChild,
            CardinalityStatistics rightChild,
            Expression expression)
    {
        // first estimate the statistics for a product
        CardinalityStatistics cardStats = 
            CardinalityUtils.product(leftChild, rightChild);
        
        // then apply a select with the given expression
        CardinalitySelectVisitor visitor = 
            new CardinalitySelectVisitor(null, cardStats);
        expression.accept(visitor);
        return visitor.getResult();

    }
}
