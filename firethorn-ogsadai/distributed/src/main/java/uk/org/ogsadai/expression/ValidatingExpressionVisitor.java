package uk.org.ogsadai.expression;

public interface ValidatingExpressionVisitor extends ExpressionVisitor
{
    public boolean isValid();
}
