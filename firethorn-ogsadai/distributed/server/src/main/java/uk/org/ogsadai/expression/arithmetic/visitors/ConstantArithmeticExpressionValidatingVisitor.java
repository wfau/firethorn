package uk.org.ogsadai.expression.arithmetic.visitors;

import uk.org.ogsadai.expression.arithmetic.ArithmeticExpressionValidatingVisitor;
import uk.org.ogsadai.expression.arithmetic.Constant;
import uk.org.ogsadai.expression.arithmetic.Div;
import uk.org.ogsadai.expression.arithmetic.ExecutableFunctionExpression;
import uk.org.ogsadai.expression.arithmetic.Minus;
import uk.org.ogsadai.expression.arithmetic.Mult;
import uk.org.ogsadai.expression.arithmetic.Plus;
import uk.org.ogsadai.expression.arithmetic.Star;
import uk.org.ogsadai.expression.arithmetic.TableColumn;

public class ConstantArithmeticExpressionValidatingVisitor 
    implements ArithmeticExpressionValidatingVisitor
{
    private boolean mResult = false;
    
    private final boolean mValidateObject;
    private Object mObject;
    private Constant mConstant;
    
    public ConstantArithmeticExpressionValidatingVisitor()
    {
        mValidateObject = false;
    }
    
    public ConstantArithmeticExpressionValidatingVisitor(Object obj)
    {
        mValidateObject = true;
        mObject = obj;
    }
    
    public Constant getConstant()
    {
        return mConstant;
    }
    
    @Override
    public void visitConstant(Constant expression)
    {
        if (mValidateObject)
        {
            mResult = expression.getResult().equals(mObject);
        }
        else
        {
            mResult = true;
        }
        if (mResult) mConstant = expression;
    }

    @Override
    public void visitPlus(Plus expression)
    {
    }

    @Override
    public void visitMinus(Minus expression)
    {
    }

    @Override
    public void visitDiv(Div expression)
    {
    }

    @Override
    public void visitMult(Mult expression)
    {
    }

    @Override
    public void visitTableColumn(TableColumn tableColumn)
    {
    }

    @Override
    public void visitFunction(ExecutableFunctionExpression function)
    {
    }

    @Override
    public void visitStar(Star expression)
    {
    }

    @Override
    public boolean isValid()
    {
        return mResult;
    }
}
