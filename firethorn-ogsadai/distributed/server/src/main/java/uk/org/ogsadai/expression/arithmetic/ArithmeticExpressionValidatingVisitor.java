package uk.org.ogsadai.expression.arithmetic;

public interface ArithmeticExpressionValidatingVisitor extends ArithmeticExpressionVisitor
{
    public boolean isValid();
}
