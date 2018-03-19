package uk.org.ogsadai.dqp.lqp.cardinality;

import uk.org.ogsadai.dqp.lqp.operators.SelectOperator;
import uk.org.ogsadai.expression.AndExpression;
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

public class CardinalitySelectNotExpressionVisitor implements ExpressionVisitor 
{
    private CardinalityStatistics mInputStats;
    private CardinalityStatistics mResult;
    private SelectOperator mSelectOperator;

    public CardinalitySelectNotExpressionVisitor(
        SelectOperator selectOperator,
        CardinalityStatistics cardStats)
    {
        mSelectOperator = selectOperator;
        mInputStats = cardStats;
    }
    
    public CardinalityStatistics getResult()
    {
        return mResult;
    }

    @Override
    public void visitAndExpression(AndExpression expression) 
    {
        CardinalitySelectVisitor visitor = 
            new CardinalitySelectVisitor(mSelectOperator, mInputStats);
        new OrExpression(
                new NotExpression(expression.getLeftExpression()), 
                new NotExpression(expression.getRightExpression())).accept(visitor);
        mResult = visitor.getResult();
    }

    @Override
    public void visitOrExpression(OrExpression expression)
    {
        CardinalitySelectVisitor visitor = 
            new CardinalitySelectVisitor(mSelectOperator, mInputStats);
        new AndExpression(
                new NotExpression(expression.getLeftExpression()), 
                new NotExpression(expression.getRightExpression())).accept(visitor);
        mResult = visitor.getResult();
    }

    @Override
    public void visitLessThanExpression(LessThanExpression expression) 
    {
        CardinalitySelectVisitor visitor = 
            new CardinalitySelectVisitor(mSelectOperator, mInputStats);
        new GreaterThanOrEqualExpression(
                expression.getLeftOperand(),
                expression.getRightOperand()).accept(visitor);
        mResult = visitor.getResult();
    }

    @Override
    public void visitLessThanOrEqualExpression(
            LessThanOrEqualExpression expression)
    {
        CardinalitySelectVisitor visitor = 
            new CardinalitySelectVisitor(mSelectOperator, mInputStats);
        new GreaterThanExpression(
                expression.getLeftOperand(),
                expression.getRightOperand()).accept(visitor);
        mResult = visitor.getResult();
    }

    @Override
    public void visitGreaterThanExpression(GreaterThanExpression expression) 
    {
        CardinalitySelectVisitor visitor = 
            new CardinalitySelectVisitor(mSelectOperator, mInputStats);
        new LessThanOrEqualExpression(
                expression.getLeftOperand(),
                expression.getRightOperand()).accept(visitor);
        mResult = visitor.getResult();
    }

    @Override
    public void visitGreaterThanOrEqualExpression(
            GreaterThanOrEqualExpression expression)
    {
        CardinalitySelectVisitor visitor = 
            new CardinalitySelectVisitor(mSelectOperator, mInputStats);
        new LessThanExpression(
                expression.getLeftOperand(),
                expression.getRightOperand()).accept(visitor);
        mResult = visitor.getResult();
    }

    @Override
    public void visitEqualExpression(EqualExpression expression)
    {
        CardinalitySelectVisitor visitor = 
            new CardinalitySelectVisitor(mSelectOperator, mInputStats);
        new NotEqualExpression(
                expression.getLeftOperand(),
                expression.getRightOperand()).accept(visitor);
        mResult = visitor.getResult();
    }

    @Override
    public void visitNotEqualExpression(NotEqualExpression expression) 
    {
        CardinalitySelectVisitor visitor = 
            new CardinalitySelectVisitor(mSelectOperator, mInputStats);
        new EqualExpression(
                expression.getLeftOperand(),
                expression.getRightOperand()).accept(visitor);
        mResult = visitor.getResult();
    }

    @Override
    public void visitNotExpression(NotExpression expression)
    {
        CardinalitySelectVisitor visitor = 
            new CardinalitySelectVisitor(mSelectOperator, mInputStats);
        expression.getChildExpression().accept(visitor);
        mResult = visitor.getResult();
    }

    @Override
    public void visitIsNullExpression(IsNullExpression expression) 
    {
        visitExpressionAndGetDifference(mInputStats, expression);
    }

    @Override
    public void visitLikeExpression(LikeExpression expression) 
    {
        visitExpressionAndGetDifference(mInputStats, expression);
    }

    @Override
    public void visitInExpression(InExpression expression) 
    {
        visitExpressionAndGetDifference(mInputStats, expression);
    }
    
    private void visitExpressionAndGetDifference(
            CardinalityStatistics inputStats, Expression expression)
    {
        CardinalitySelectVisitor visitor = 
            new CardinalitySelectVisitor(mSelectOperator, mInputStats);
        expression.accept(visitor);
        mResult = CardinalityUtils.difference(mInputStats, visitor.getResult());

    }
    
}
