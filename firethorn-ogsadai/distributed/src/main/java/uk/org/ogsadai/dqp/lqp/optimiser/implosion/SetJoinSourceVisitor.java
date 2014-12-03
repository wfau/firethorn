package uk.org.ogsadai.dqp.lqp.optimiser.implosion;

import java.util.Collection;
import java.util.Collections;

import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpression;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpressionVisitor;
import uk.org.ogsadai.expression.arithmetic.Constant;
import uk.org.ogsadai.expression.arithmetic.Div;
import uk.org.ogsadai.expression.arithmetic.ExecutableFunctionExpression;
import uk.org.ogsadai.expression.arithmetic.Minus;
import uk.org.ogsadai.expression.arithmetic.Mult;
import uk.org.ogsadai.expression.arithmetic.Plus;
import uk.org.ogsadai.expression.arithmetic.Star;
import uk.org.ogsadai.expression.arithmetic.TableColumn;

public class SetJoinSourceVisitor implements ArithmeticExpressionVisitor
{
    private final String mSource;
    private final String mOtherSource;
    private final Collection<Attribute> mAttributes;

    public SetJoinSourceVisitor(String source)
    {
        mSource = null;
        mOtherSource = source;
        mAttributes = Collections.emptyList();
    }
    
    public SetJoinSourceVisitor(
            String source, 
            String otherSource,
            Collection<Attribute> attributes)
    {
        mSource = source;
        mOtherSource = otherSource;
        mAttributes = attributes;
    }
    
    @Override
    public void visitConstant(Constant expression)
    {
        // nothing to do
    }

    @Override
    public void visitDiv(Div expression)
    {
        expression.getLeftExpression().accept(this);
        expression.getRightExpression().accept(this);
    }

    @Override
    public void visitFunction(ExecutableFunctionExpression function)
    {
        for (ArithmeticExpression ae : function.getExecutable().getParameters())
        {
            ae.accept(this);
        }
    }

    @Override
    public void visitMinus(Minus expression)
    {
        expression.getLeftExpression().accept(this);
        expression.getRightExpression().accept(this);
    }

    @Override
    public void visitMult(Mult expression)
    {
        expression.getLeftExpression().accept(this);
        expression.getRightExpression().accept(this);
    }

    @Override
    public void visitPlus(Plus expression)
    {
        expression.getLeftExpression().accept(this);
        expression.getRightExpression().accept(this);
    }

    @Override
    public void visitStar(Star expression)
    {
        // do nothing
    }

    @Override
    public void visitTableColumn(TableColumn tableColumn)
    {
        boolean inAtt = false;
        for (Attribute attribute : mAttributes)
        {
            if (attribute.getName().equals(tableColumn.getName()))
            {
                inAtt = true;
            }
        }
        if (inAtt)
        {
            tableColumn.rename(tableColumn.getName(), mSource);
        }
        else
        {
            tableColumn.rename(tableColumn.getName(), mOtherSource);
        }
    }

}
