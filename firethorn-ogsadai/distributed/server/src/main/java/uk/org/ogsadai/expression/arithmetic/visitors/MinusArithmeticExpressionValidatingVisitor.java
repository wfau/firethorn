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

public class MinusArithmeticExpressionValidatingVisitor
    implements ArithmeticExpressionValidatingVisitor
{
    private boolean mResult;
    private final boolean mValidateChildren;
    private ArithmeticExpressionValidatingVisitor mLhsValidatingVisitor;
    private ArithmeticExpressionValidatingVisitor mRhsValidatingVisitor;
    
    
    public MinusArithmeticExpressionValidatingVisitor()
    {
        mValidateChildren = false;
    }
    
    public MinusArithmeticExpressionValidatingVisitor(
        ArithmeticExpressionValidatingVisitor lhsValidatingVisitor,
        ArithmeticExpressionValidatingVisitor rhsValidatingVisitor)
        
    {
        mValidateChildren = true;
        mLhsValidatingVisitor = lhsValidatingVisitor;
        mRhsValidatingVisitor = rhsValidatingVisitor;
    }
    
    
    @Override
    public void visitConstant(Constant expression)
    {
        mResult = false;
    }

    @Override
    public void visitPlus(Plus expression)
    {
        mResult = false;
    }

    @Override
    public void visitMinus(Minus expression)
    {
        if (!mValidateChildren)
        {
            mResult = true;
        }
        else
        {
            ArithmeticExpression[] children = expression.getChildren();
            
            children[0].accept(mLhsValidatingVisitor);
            children[1].accept(mRhsValidatingVisitor);
            
            mResult = mLhsValidatingVisitor.isValid() && 
                      mRhsValidatingVisitor.isValid();
        }
    }

    @Override
    public void visitDiv(Div expression)
    {
        mResult = false;
    }

    @Override
    public void visitMult(Mult expression)
    {
        mResult = false;
    }

    @Override
    public void visitTableColumn(TableColumn tableColumn)
    {
        mResult = false;
    }

    @Override
    public void visitFunction(ExecutableFunctionExpression function)
    {
        mResult = false;
        
    }

    @Override
    public void visitStar(Star expression)
    {
        mResult = false;
    }

    @Override
    public boolean isValid()
    {
        return mResult;
    }
}
