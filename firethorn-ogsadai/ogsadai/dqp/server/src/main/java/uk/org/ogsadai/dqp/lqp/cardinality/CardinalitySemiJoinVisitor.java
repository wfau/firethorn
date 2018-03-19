package uk.org.ogsadai.dqp.lqp.cardinality;

import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.AttributeImpl;
import uk.org.ogsadai.expression.AndExpression;
import uk.org.ogsadai.expression.ComparisonExpression;
import uk.org.ogsadai.expression.EqualExpression;
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

public class CardinalitySemiJoinVisitor implements ExpressionVisitor
{
    private CardinalityStatistics mLeftChild;
    private CardinalityStatistics mRightChild;
    private CardinalityStatistics mResult;

    public CardinalitySemiJoinVisitor(
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
                "Not handing some type of Semi Join properly");
        }
        return mResult;
    }

    @Override
    public void visitAndExpression(AndExpression expression)
    {
        mResult = mLeftChild;
    }

    @Override
    public void visitOrExpression(OrExpression expression) 
    {
        mResult = mLeftChild;
    }

    @Override
    public void visitLessThanExpression(LessThanExpression expression) 
    {
        mResult = processArithmeticOperator(
                mLeftChild, mRightChild, 
                expression, 
                ArithmeticOperator.LESS_THAN);
    }

    @Override
    public void visitLessThanOrEqualExpression(
            LessThanOrEqualExpression expression)
    {
        mResult = processArithmeticOperator(
                mLeftChild, mRightChild, 
                expression, 
                ArithmeticOperator.LESS_THAN_OR_EQUAL);
    }

    @Override
    public void visitGreaterThanExpression(GreaterThanExpression expression) 
    {
        mResult = processArithmeticOperator(
                mLeftChild, mRightChild, 
                expression, 
                ArithmeticOperator.GREATER_THAN);
    }

    @Override
    public void visitGreaterThanOrEqualExpression(
            GreaterThanOrEqualExpression expression) 
    {
        mResult = processArithmeticOperator(
                mLeftChild, mRightChild, 
                expression, 
                ArithmeticOperator.GREATER_THAN_OR_EQUAL);
    }

    @Override
    public void visitEqualExpression(EqualExpression expression)
    {
        mResult = processArithmeticOperator(
                mLeftChild, mRightChild, 
                expression, 
                ArithmeticOperator.EQUAL);
    }

    @Override
    public void visitNotEqualExpression(NotEqualExpression expression) 
    {
        mResult = processArithmeticOperator(
                mLeftChild, mRightChild, 
                expression, 
                ArithmeticOperator.NOT_EQUAL);
    }

    @Override
    public void visitNotExpression(NotExpression expression) 
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void visitIsNullExpression(IsNullExpression expression) 
    {
        mResult = mLeftChild;
    }

    @Override
    public void visitLikeExpression(LikeExpression expression)
    {
        mResult = mLeftChild;
    }

    @Override
    public void visitInExpression(InExpression expression)
    {
        mResult = mLeftChild;
    }

    private CardinalityStatistics processArithmeticOperator(
            CardinalityStatistics lhsStats,
            CardinalityStatistics rhsStats,
            ComparisonExpression expression, 
            ArithmeticOperator op)
    {
        ArithmeticExpression expressions[] = 
            ExpressionUtilities.getArithmeticExpressions(expression);
        OperandType[] operandTypes = 
            ExpressionUtilities.getOperandTypes(expressions);
            
        if (operandTypes[0] == OperandType.ATTR && 
                operandTypes[1] == OperandType.ATTR && 
                op == ArithmeticOperator.EQUAL)
        {   
            return processAttrEqualsAttr(
                    lhsStats, rhsStats,
                    (TableColumn)expressions[0], 
                    (TableColumn)expressions[1]);
        }
        if (operandTypes[0] == OperandType.CONST && 
                operandTypes[1] == OperandType.CONST)
        {
            return CardinalityUtils.processConstOperatorConst(
                    lhsStats,
                    expression);
        }
        
        // for any other expression return the left hand side - upper bound 
        return lhsStats;
    }

    private static CardinalityStatistics processAttrEqualsAttr(
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
            CardinalityUtils.equiSemiJoin(lhsAttrStats, rhsAttrStats);
        
        return CardinalityUtils.replaceAndRescale(
                    lhsStats, lhsAttrStats, resultHistogram);
    }
}
