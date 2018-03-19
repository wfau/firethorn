package uk.org.ogsadai.dqp.lqp.cardinality;

import java.util.List;

import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.AttributeImpl;
import uk.org.ogsadai.dqp.lqp.operators.SelectOperator;
import uk.org.ogsadai.expression.AndExpression;
import uk.org.ogsadai.expression.ArithmeticExpressionOperand;
import uk.org.ogsadai.expression.ComparisonExpression;
import uk.org.ogsadai.expression.EqualExpression;
import uk.org.ogsadai.expression.Expression;
import uk.org.ogsadai.expression.ExpressionEvaluationException;
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
import uk.org.ogsadai.expression.Operand;
import uk.org.ogsadai.expression.OrExpression;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpression;
import uk.org.ogsadai.expression.arithmetic.TableColumn;
import uk.org.ogsadai.expression.arithmetic.visitors.OperandTypeArithmeticExprVisitor.OperandType;

public class CardinalitySelectVisitor implements ExpressionVisitor
{
    private CardinalityStatistics mInputStats;
    private CardinalityStatistics mResult;
    private final SelectOperator mSelectOperator;

    public CardinalitySelectVisitor(
        SelectOperator operator,
        CardinalityStatistics cardStats)
    {
        mSelectOperator = operator;
        mInputStats = cardStats;
    }
    
    public CardinalityStatistics getResult()
    {
        return mResult;
    }
    
    @Override
    public void visitAndExpression(AndExpression expression)
    {
        expression.getLeftExpression().accept(this);
        mInputStats = mResult;
        expression.getRightExpression().accept(this);
    }

    @Override
    public void visitOrExpression(OrExpression expression)
    {
        CardinalityStatistics before = mInputStats;
        new AndExpression(
            new NotExpression(expression.getLeftExpression()),
            new NotExpression(expression.getRightExpression())).accept(this);
        mResult = CardinalityUtils.difference(before, mResult);
    }
    
    @Override
    public void visitNotExpression(NotExpression expression)
    {
        Expression child = expression.getChildExpression();
        CardinalitySelectNotExpressionVisitor visitor = 
            new CardinalitySelectNotExpressionVisitor(
                mSelectOperator, mInputStats);
        child.accept(visitor);
        mResult = visitor.getResult();
    }

    @Override
    public void visitLessThanExpression(LessThanExpression expression)
    {
        mResult = processArithmeticOperator(
            mInputStats, expression, ArithmeticOperator.LESS_THAN);
    }
    
    @Override
    public void visitLessThanOrEqualExpression(
        LessThanOrEqualExpression expression)
    {
        mResult = processArithmeticOperator(
            mInputStats, expression, ArithmeticOperator.LESS_THAN_OR_EQUAL);
    }
    
    @Override
    public void visitGreaterThanExpression(GreaterThanExpression expression)
    {
        mResult = processArithmeticOperator(
            mInputStats, expression, ArithmeticOperator.GREATER_THAN);
    }
    
    @Override
    public void visitGreaterThanOrEqualExpression(
        GreaterThanOrEqualExpression expression)
    {
        mResult = processArithmeticOperator(
            mInputStats, expression, ArithmeticOperator.GREATER_THAN_OR_EQUAL);
    }
    
    @Override
    public void visitEqualExpression(EqualExpression expression)
    {
        mResult = 
            processArithmeticOperator(
                mInputStats, expression, ArithmeticOperator.EQUAL);
    }
    
    @Override
    public void visitNotEqualExpression(NotEqualExpression expression)
    {
        mResult = 
            processArithmeticOperator(
                mInputStats, expression, ArithmeticOperator.NOT_EQUAL);
    }
        
    @Override
    public void visitIsNullExpression(IsNullExpression expression)
    {
        ArithmeticExpression operand = 
            ((ArithmeticExpressionOperand) expression.getOperand()).getExpression();
        OperandType operandType = ExpressionUtilities.getOperandType(operand);
        switch (operandType)
        {
        case ATTR:
            // ATTR is null
            mResult = processAttrIsNull(mInputStats, (TableColumn)operand);
            break;
        case CONST:
            // CONST is null
            mResult = CardinalityUtils.rescaleToZero(mInputStats);
            break;
        case FUNC:
            // leave statistics as they are?
        default:
            mResult = mInputStats;
        }
    }
    
    @Override
    public void visitLikeExpression(LikeExpression expression)
    {
        // heuristic guess
        double inputCard = mInputStats.getCardinality();
        mResult = CardinalityUtils.rescale(mInputStats, .5 * inputCard);
    }
    
    @Override
    public void visitInExpression(InExpression expression)
    {
        // treat as ((attr = op1) AND (attr = op2) AND ...)
        CardinalityStatistics inputStats = mInputStats;
        for (Operand operand : expression.getRightOperands())
        {
            EqualExpression equal = 
                new EqualExpression(expression.getLeftOperand(), operand);
            CardinalitySelectVisitor visitor = 
                new CardinalitySelectVisitor(mSelectOperator, inputStats);
            equal.accept(visitor);
            inputStats = visitor.getResult();
        }
        mResult = inputStats;
    }

    private CardinalityStatistics processArithmeticOperator(
        CardinalityStatistics inputStatistics,
        ComparisonExpression expression, 
        ArithmeticOperator op)
    {
        ArithmeticExpression expressions[] = 
            ExpressionUtilities.getArithmeticExpressions(expression);
        OperandType[] operandTypes = 
            ExpressionUtilities.getOperandTypes(expressions);
        
        for (int i=0; i<2; ++i)
        {
            if (operandTypes[i] == OperandType.ATTR && 
                operandTypes[1-i] == OperandType.CONST)
            {
                if (i == 1)
                {
                    op = ArithmeticOperator.swapOperands(op);
                }
                return processAttrOperatorConstant(
                    inputStatistics,
                    (TableColumn)expressions[i], 
                    op,
                    expressions[1-i]);
            }
        }
        
        if (operandTypes[0] == OperandType.ATTR && 
            operandTypes[1] == OperandType.ATTR)
        {
            return processAttrOperatorAttr(
                inputStatistics,
                (TableColumn)expressions[0], 
                op,
                (TableColumn)expressions[1]);
        }

        if (operandTypes[0] == OperandType.CONST && 
                operandTypes[1] == OperandType.CONST)
        {
            return CardinalityUtils.processConstOperatorConst(
                    inputStatistics,
                    expression);
        }
        
        for (int i=0; i<2; ++i)
        {
            if (operandTypes[i] == OperandType.FUNC && 
                operandTypes[1-i] == OperandType.CONST)
            {
                return processFunctionOperator(.1, inputStatistics, op);
            }
        }
        
        for (int i=0; i<2; ++i)
        {
            if (operandTypes[i] == OperandType.FUNC && 
                operandTypes[1-i] == OperandType.ATTR)
            {
                return processFunctionOperator(.2, inputStatistics, op);
            }
        }
        
        for (int i=0; i<2; ++i)
        {
            if (operandTypes[i] == OperandType.FUNC && 
                operandTypes[1-i] == OperandType.FUNC)
            {
                return processFunctionOperator(.2, inputStatistics, op);
            }
        }

        // Leaves the cardinality estimates as they are 
        return inputStatistics;
        
        // --------------------------------------------------------------
        // We don't handle any other combination at the moment.  We should
        // look at seeing what other ones we can handle and maybe for some
        // we want there is a better estimate available that keeping the
        // same histograms.
        
        // Below FUNC include arithmetic functions such as + - etc.
        // In theory we could do some algebra on these but we don't
        // do that just now
        
        //   ATTR = CONST      Done
        //   ATTR < CONST      Done
        //   ATTR > CONST      Done
        //   ATTR != CONST     Done
        //   ATTR <= CONST     Done
        //   ATTR >= CONST     Done
        
        //   ATTR = ATTR       Done
        //   ATTR < ATTR       Done
        //   ATTR > ATTR       Done
        //   ATTR != ATTR      Done
        //   ATTR <= ATTR      Done
        //   ATTR >= ATTR      Done
        
        //   CONST = CONST     Done
        //   CONST < CONST     Done
        //   CONST > CONST     Done
        //   CONST != CONST    Done
        //   CONST <= CONST    Done
        //   CONST >= CONST    Done
        
        //   FUNC  = CONST     Done - Heuristic guess
        //   FUNC  < CONST     Done - Heuristic guess
        //   FUNC  > CONST     Done - Heuristic guess
        //   FUNC  != CONST    Done - Heuristic guess
        //   FUNC  <= CONST    Done - Heuristic guess
        //   FUNC  >= CONST    Done - Heuristic guess

        //   FUNC  = ATTR      Done - Heuristic guess
        //   FUNC  < ATTR      Done - Heuristic guess
        //   FUNC  > ATTR      Done - Heuristic guess
        //   FUNC  != ATTR     Done - Heuristic guess
        //   FUNC  <= ATTR     Done - Heuristic guess
        //   FUNC  >= ATTR     Done - Heuristic guess
        
        //   FUNC  = FUNC      Done - Heuristic guess
        //   FUNC  < FUNC      Done - Heuristic guess
        //   FUNC  > FUNC      Done - Heuristic guess
        //   FUNC  != FUNC     Done - Heuristic guess
        //   FUNC  <= FUNC     Done - Heuristic guess
        //   FUNC  >= FUNC     Done - Heuristic guess

        // Leaves the cardinality estimates as they are 
    }

    private CardinalityStatistics processAttrOperatorConstant(
        CardinalityStatistics inputStatistics,
        TableColumn tableColumn, 
        ArithmeticOperator op, 
        ArithmeticExpression constant)
    {
        try
        {
            // Evaluate the constant
            constant.evaluate(null);
            Object constantValue = constant.getResult();
            
            // Get attribute
            Attribute attr = new AttributeImpl(
                tableColumn.getName(), tableColumn.getSource());
            
            // Find statistics
            AttributeStatistics inputAttrStats = 
                getStatistics(inputStatistics, attr);
            
            // Update statistics
            AttributeStatistics outputAttrStats = 
                processOperatorConstant(
                        inputAttrStats, op, constantValue);
                
            return CardinalityUtils.replaceAndRescale(
                    inputStatistics,
                    inputAttrStats,
                    outputAttrStats);
        } 
        catch (ExpressionEvaluationException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    
    private static AttributeStatistics processOperatorConstant(
        AttributeStatistics inputAttrStats, 
        ArithmeticOperator op,
        Object constant)
    {
        switch(op)
        {
        case EQUAL:
            return inputAttrStats.processEqualConstant(constant);
        case LESS_THAN:
        case LESS_THAN_OR_EQUAL:
        case GREATER_THAN:
        case GREATER_THAN_OR_EQUAL:
            return inputAttrStats.processInequalityConstant(op, constant);
        case NOT_EQUAL:
            return inputAttrStats.processNotEqualConstant(constant);
        default:
            throw new RuntimeException("Unexpected operator type: " + op);
        }
    }
    
    
    /**
     * Processes selects of the type "attr Op attr" where attr is an attribute
     * name and Op is one of = <> < > <= >=.
     * 
     * @param inputStatistics input cardinality statistics
     * @param lhs   left hand side column name
     * @param op    operator
     * @param rhs   right hand side column name
     * 
     * @return output cardinality statistics
     */
    private CardinalityStatistics processAttrOperatorAttr(
        CardinalityStatistics inputStatistics,
        TableColumn lhs, 
        ArithmeticOperator op, 
        TableColumn rhs)
    {
        Attribute lhsAttr = new AttributeImpl(lhs.getName(), lhs.getSource());
        Attribute rhsAttr = new AttributeImpl(rhs.getName(), rhs.getSource());
        
        AttributeStatistics lhsStats = getStatistics(inputStatistics, lhsAttr);
        AttributeStatistics rhsStats = getStatistics(inputStatistics, rhsAttr);
        
        if (isAttributeBound(inputStatistics, lhsAttr))
        {
            AttributeStatistics outputAttrStats = 
                CardinalityUtils.processSelectAttrOperatorBoundAttr(
                    rhsStats, 
                    ArithmeticOperator.swapOperands(op),
                    lhsStats);
            
            return CardinalityUtils.replaceAndRescale(
                inputStatistics, 
                rhsStats, 
                outputAttrStats);
        }
        else if (isAttributeBound(inputStatistics, rhsAttr))
        {
            AttributeStatistics outputAttrStats = 
                CardinalityUtils.processSelectAttrOperatorBoundAttr(
                    lhsStats, 
                    op,
                    rhsStats);
            return CardinalityUtils.replaceAndRescale(
                inputStatistics, 
                lhsStats, 
                outputAttrStats);
        }
        else
        {
            List<AttributeStatistics> outputAttrStats = 
                CardinalityUtils.processSelectAttrOperatorAttr(
                    lhsStats, op, rhsStats);
            
            return CardinalityUtils.replaceAndRescale(
                inputStatistics,
                lhsStats,
                outputAttrStats.get(0),
                rhsStats,
                outputAttrStats.get(1));
        }
    }

    /**
     * Heuristic guess for statistics of FUNCTION op [CONST, ATTR, FUNC].
     * 
     * @param factor
     *            rescale factor for number of rows if the operator is
     *            <code>equals</code>
     * @param inputStatistics
     *            input statistics
     * @param op
     *            arithmetic operator
     * @return output statistics
     */
    private CardinalityStatistics processFunctionOperator(
            double factor,
            CardinalityStatistics inputStatistics,
            ArithmeticOperator op)
    {
        double numRows = inputStatistics.getCardinality();
        switch (op)
        {
        case EQUAL: 
            return CardinalityUtils.rescale(inputStatistics, factor * numRows);
        case NOT_EQUAL:
            return CardinalityUtils.rescale(inputStatistics, (1-factor) * numRows);
        case GREATER_THAN:
        case GREATER_THAN_OR_EQUAL:
        case LESS_THAN:
        case LESS_THAN_OR_EQUAL:
            return CardinalityUtils.rescale(inputStatistics, .5 * numRows);
        default:
            throw new RuntimeException("Cannot process operator " + op);
        }
    }

    private CardinalityStatistics processAttrIsNull(
            CardinalityStatistics inputStatistics,
            TableColumn tableColumn) 
    {
        Attribute attr = new AttributeImpl(tableColumn);
        AttributeStatistics attrStats = getStatistics(inputStatistics, attr);
        double numRows = attrStats.getNumNulls();
        ScalarAttributeStatistics newAttrStats = 
            new ScalarAttributeStatistics(numRows, 0, numRows);
        return CardinalityUtils.replaceAndRescale(
                inputStatistics, attrStats, newAttrStats);
    }

    /**
     * Gets the statistics for the given attribute.  If the statistics are
     * not local because this is a bound attribute associated with an apply
     * then the appropriate statistics will be found and rescaled to a row
     * count of 1.
     * 
     * @param attr  attribute
     * 
     * @return statistics for the attribute
     */
    private AttributeStatistics getStatistics(
        CardinalityStatistics inputStatistics,
        Attribute attr)
    {
        if (!inputStatistics.contains(attr))
        {
            // The attribute cannot found. This means it is a bound attribute
            // associated with an apply operator.
            
            AttributeStatistics stats = 
                CardinalityUtils.getStatsForBoundAttribute(
                    mSelectOperator, attr);

            // Rescale to a row count of one the apply operator effectively
            // executes the sub-query for each row of the outer relation that
            // is the for loop of the apply
            return stats.rescale(1);
        }
        else
        {
            return inputStatistics.getStatistics(attr);
        }
    }
    
    private static boolean isAttributeBound(
        CardinalityStatistics inputStats, Attribute attr)
    {
        return !inputStats.contains(attr);
    }
}
