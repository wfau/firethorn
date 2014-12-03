//Copyright (c) The University of Edinburgh 2007.
//
// LICENCE-START
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// LICENCE-END

package uk.org.ogsadai.expression.arithmetic;

import java.util.Set;

import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.expression.ExpressionEvaluationException;
import uk.org.ogsadai.tuple.ColumnMetadata;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.TypeMismatchException;

/**
 * An arithmetic expression.
 * 
 * @author The OGSA-DAI Project Team.
 */
public interface ArithmeticExpression
{
    /**
     * Evaluates the arithmetic expression on the given input tuple.
     * 
     * @param tuple
     *            input tuple
     * @throws ExpressionEvaluationException
     *             if expression can not be evaluated
     */
    public void evaluate(Tuple tuple) throws ExpressionEvaluationException;

    /**
     * Returns the result of the evaluation. In the expression is an aggregation
     * function the result is aggregated from all input tuples up to this point.
     * 
     * @return result of the evaluation
     */
    public Object getResult();

    /**
     * Configures the expression with the metadata. This method must be called
     * before evaluating the expression or calling <code>getMetadata()</code>.
     * This method is also used for type validation.
     * 
     * @param metadata
     *            tuple metadata
     * @throws TypeMismatchException
     *             when the operand types are incompatible
     */
    public void configure(TupleMetadata metadata)
        throws TypeMismatchException;

    /**
     * Configures the expression with the metadata. This method must be called
     * before evaluating the expression or calling <code>getMetadata()</code>.
     * This method is also used for type validation.  A set of correlated
     * attributes can be passed in to support type validation of correlated
     * attributes.
     * 
     * @param metadata
     *            tuple metadata
     * @param correlatedAttributes
     *            set of correlated attributes, containing their types.
     * @throws TypeMismatchException
     *             when the operand types are incompatible
     */
    public void configure(
        TupleMetadata metadata, 
        Set<Attribute> correlatedAttributes)
        throws TypeMismatchException;

    /**
     * Get the result type that is returned when evaluating this expression with
     * tuples of the given type.
     * 
     * @return result type
     */
    public ColumnMetadata getMetadata();

    /**
     * Returns the children of this arithmetic expression.
     * 
     * @return child expressions
     */
    public ArithmeticExpression[] getChildren();

    /**
     * Accept an arithmetic expression visitor.
     * 
     * @param visitor
     */
    public void accept(ArithmeticExpressionVisitor visitor);
    
    /**
     * Set context type for an expression.
     * 
     * @param type
     *            context tuple type
     */
    public void setContextType(int type);

    /**
     * Reset type to original.
     */
    public void resetType();
    
}
