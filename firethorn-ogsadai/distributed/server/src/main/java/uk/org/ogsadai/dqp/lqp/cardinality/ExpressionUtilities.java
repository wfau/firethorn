package uk.org.ogsadai.dqp.lqp.cardinality;

import uk.org.ogsadai.expression.ArithmeticExpressionOperand;
import uk.org.ogsadai.expression.ComparisonExpression;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpression;
import uk.org.ogsadai.expression.arithmetic.visitors.OperandTypeArithmeticExprVisitor;
import uk.org.ogsadai.expression.arithmetic.visitors.OperandTypeArithmeticExprVisitor.OperandType;

public class ExpressionUtilities
{
    
    private ExpressionUtilities() 
    {
        // only static methods
    }
    
    public static ArithmeticExpression[] getArithmeticExpressions(ComparisonExpression expression)
    {
        ArithmeticExpression left = 
            ((ArithmeticExpressionOperand) expression.getLeftOperand()).getExpression();
        ArithmeticExpression right = 
            ((ArithmeticExpressionOperand) expression.getRightOperand()).getExpression();
        
        return new ArithmeticExpression[]{left, right};
    }
    
    public static OperandType[] getOperandTypes(ArithmeticExpression[] expressions)
    {
        OperandType[] result = new OperandType[expressions.length];
        
        for (int i=0; i<expressions.length; ++i)
        {
            result[i] = getOperandType(expressions[i]);
        }
        return result;
        
    }
    
    public static OperandType getOperandType(ArithmeticExpression expression)
    {
        OperandTypeArithmeticExprVisitor operandTypeVisitor = 
            new OperandTypeArithmeticExprVisitor();
        expression.accept(operandTypeVisitor);
        return operandTypeVisitor.getOperandType();
    }
    
}
