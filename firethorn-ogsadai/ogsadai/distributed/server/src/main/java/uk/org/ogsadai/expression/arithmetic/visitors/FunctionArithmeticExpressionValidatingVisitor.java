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

public class FunctionArithmeticExpressionValidatingVisitor implements ArithmeticExpressionValidatingVisitor
{
    private boolean mResult;
    
    private final boolean mValidateFunctionName;
    private final boolean mValidateFunctionParameters;
    private String mFunctionName;
    private ArithmeticExpressionValidatingVisitor[] mParameterVisitors;
    
    public FunctionArithmeticExpressionValidatingVisitor()
    {
        mValidateFunctionName = false;
        mValidateFunctionParameters = false;
    }
    
    public FunctionArithmeticExpressionValidatingVisitor(String functionName)
    {
        mValidateFunctionName = true;
        mValidateFunctionParameters = false;
        mFunctionName = functionName;
    }

    public FunctionArithmeticExpressionValidatingVisitor(
        String functionName, 
        ArithmeticExpressionValidatingVisitor... arithmeticExpressionValidatingVisitors )
    {
        mValidateFunctionName = true;
        mValidateFunctionParameters = true;
        mFunctionName = functionName;
        mParameterVisitors = arithmeticExpressionValidatingVisitors;
    }
    
    
    @Override
    public boolean isValid()
    {
        return mResult;
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
        mResult = false;
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
        if (!mValidateFunctionName)
        {
            mResult = true;
        }
        else 
        {
            mResult = function.getExecutable().getName().equals(mFunctionName);
            
            if (mResult && mValidateFunctionParameters)
            {
                ArithmeticExpression[] children = function.getChildren();
                
                if (mParameterVisitors.length != children.length)
                {
                    mResult = false;
                }
                else
                {
                    mResult = true;
                    for (int i=0; i<children.length; ++i)
                    {
                        children[i].accept(mParameterVisitors[i]);
                        
                        if (!mParameterVisitors[i].isValid())
                        {
                            mResult = false;
                            break;
                        }
                    }
                }
            }
        }
    }


    @Override
    public void visitStar(Star expression)
    {
        mResult = false;
    }
}
