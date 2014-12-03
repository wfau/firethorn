// Copyright (c) The University of Edinburgh, 2008.
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

package uk.org.ogsadai.expression;

import java.util.Set;

import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.tuple.ColumnNotFoundException;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.TypeMismatchException;

/**
 * Expression interface. Expressions can be evaluated to true or false
 * with respect to a data tuple.
 *
 * @author The OGSA-DAI Project Team.
 */
public interface Expression
{

    /**
     * Configures the expression prior to evaluation. This method must be called
     * prior to evaluation in order to tell the expression the type of the
     * tuples it will evaluate. This method is also use for type validation.
     * 
     * @param metadata
     *            The type of the tuples that will be evaluated.
     * 
     * @throws ColumnNotFoundException
     *             If the expression contains a reference to a field (column)
     *             that is not specified in the tuple type.
     * @throws TypeMismatchException
     *             when expression operands types are incompatible
     */
    void configure(TupleMetadata metadata) 
        throws ColumnNotFoundException, 
               TypeMismatchException;
    
    /**
     * Configures the expression prior to evaluation. This method must be called
     * prior to evaluation in order to tell the expression the type of the
     * tuples it will evaluate. This method is mainly used for validation. It
     * takes the set of correlated attributes on order to support type 
     * validation using correlated attributes as otherwise the type details of
     * such attributes is unavailable.
     * 
     * @param metadata
     *            The type of the tuples that will be evaluated.
     *            
     * @param correlatedAttributes 
     *            set of correlated attributes
     * 
     * @throws ColumnNotFoundException
     *             If the expression contains a reference to a field (column)
     *             that is not specified in the tuple type.
     * @throws TypeMismatchException
     *             when expression operands types are incompatible
     */
    void configure(TupleMetadata metadata, Set<Attribute> correlatedAttributes)
        throws ColumnNotFoundException, 
               TypeMismatchException;
    
    /**
     * Evaluates the expression on the given tuple.
     * 
     * @param tuple
     *            Tuple to evaluate. This tuple must conform to the tuple type
     *            specified in the last call to <code>configure</code>.
     * @return The result of evaluating the expression on the tuple. If the
     *         result of the evaluation is unknown then <code>null</code> will 
     *         be returned.
     * @throws ExpressionEvaluationException
     *             when expression can not be evaluated.
     */
    Boolean evaluate(Tuple tuple) throws ExpressionEvaluationException;
    
    /**
     * Accept a visitor.  A call will me made back to the given visitor.
     * The method called will depend on the concrete implementation.
     * 
     * @param visitor The visitor object to call back to.
     */
    void accept(ExpressionVisitor visitor);
}
