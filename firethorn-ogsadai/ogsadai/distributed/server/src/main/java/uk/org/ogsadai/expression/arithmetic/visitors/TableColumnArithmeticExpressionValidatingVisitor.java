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

public class TableColumnArithmeticExpressionValidatingVisitor
    implements ArithmeticExpressionValidatingVisitor
{
    private boolean mResult;
    private TableColumn mTableColumn;
    
    public TableColumn getTableColumn()
    {
        return mTableColumn;
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
        mResult = true;
        mTableColumn = tableColumn;
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
