package uk.org.ogsadai.expression.arithmetic.visitors;

import uk.org.ogsadai.expression.arithmetic.ArithmeticExpression;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpressionValidatingVisitor;
import uk.org.ogsadai.expression.arithmetic.Constant;
import uk.org.ogsadai.expression.arithmetic.Div;
import uk.org.ogsadai.expression.arithmetic.ExecutableFunctionExpression;
import uk.org.ogsadai.expression.arithmetic.Minus;
import uk.org.ogsadai.expression.arithmetic.Mult;
import uk.org.ogsadai.expression.arithmetic.Plus;
import uk.org.ogsadai.expression.arithmetic.Star;
import uk.org.ogsadai.expression.arithmetic.TableColumn;

public class AnyArithmeticExpressionValidatingVisitor implements ArithmeticExpressionValidatingVisitor
{
    private ArithmeticExpression mRoot = null;
    
    @Override
    public void visitConstant(Constant expression)
    {
        if (mRoot == null) mRoot = expression;
    }

    @Override
    public void visitPlus(Plus expression)
    {
        if (mRoot == null) mRoot = expression;
    }

    @Override
    public void visitMinus(Minus expression)
    {
        if (mRoot == null) mRoot = expression;
    }

    @Override
    public void visitDiv(Div expression)
    {
        if (mRoot == null) mRoot = expression;
    }

    @Override
    public void visitMult(Mult expression)
    {
        if (mRoot == null) mRoot = expression;
    }

    @Override
    public void visitTableColumn(TableColumn tableColumn)
    {
        if (mRoot == null) mRoot = tableColumn;
    }

    @Override
    public void visitFunction(ExecutableFunctionExpression function)
    {
        if (mRoot == null) mRoot = function;
    }

    @Override
    public void visitStar(Star expression)
    {
        if (mRoot == null) mRoot = expression;
    }

    @Override
    public boolean isValid()
    {
        return true;
    }

    public ArithmeticExpression getExpression()
    {
        return mRoot;
    }
}
