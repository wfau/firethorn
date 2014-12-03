package uk.org.ogsadai.dqp.lqp.cardinality;

public enum ArithmeticOperator {
    EQUAL, 
    LESS_THAN, 
    GREATER_THAN, 
    LESS_THAN_OR_EQUAL, 
    GREATER_THAN_OR_EQUAL, 
    NOT_EQUAL;

    /**
     * Returns the operator that should be used if the operands were to swap
     * places and the predicate to retain its meaning. For example: if the
     * predicate is a op b then the operator returned is op' such that b op' a
     * has the same meaning as a op b.
     * 
     * @param op
     *            operator
     * 
     * @return the operator needed if the operands are swapped.
     */
    static ArithmeticOperator swapOperands(ArithmeticOperator op)
    {
        switch(op)
        {
        case EQUAL:
            return EQUAL;
        case LESS_THAN:
            return GREATER_THAN_OR_EQUAL;
        case LESS_THAN_OR_EQUAL:
            return GREATER_THAN;
        case GREATER_THAN:
            return LESS_THAN_OR_EQUAL;
        case GREATER_THAN_OR_EQUAL:
            return LESS_THAN;
        case NOT_EQUAL:
            return NOT_EQUAL;
        default:
            throw new RuntimeException("Unhandled operator : " + op);
        }
    }
}
