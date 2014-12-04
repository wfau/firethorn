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
 * Interface for operand.  Operands represent values that are compared
 * by the comparison expressions.
 *
 * @author The OGSA-DAI Project Team.
 */
public interface Operand
{
    /**
     * Configures the operand prior to obtaining the value.  This method must
     * be called prior to getting the value in order to tell the operand the
     * type of the tuples it will received.
     * 
     * @param tupleType  The type of the tuples that will be received.
     * 
     * @throws ColumnNotFoundException  
     *           If the operand contains a reference to a field (column) 
     *           that is not specified in the tuple type.
     * @throws TypeMismatchException 
     */
    void configure(TupleMetadata tupleType) 
        throws ColumnNotFoundException, TypeMismatchException;

    /**
     * Configures the operand prior to obtaining the value.  This method must
     * be called prior to getting the value in order to tell the operand the
     * type of the tuples it will received.  This version of the method is
     * used for type validation.  It takes the set of correlated attributes on 
     * order to support type validation using correlated attributes as otherwise
     * the type details of such attributes is unavailable.
     * 
     * @param tupleType  The type of the tuples that will be received.
     * 
     * @param correlatedAttributes set of correlated attributes
     * 
     * @throws ColumnNotFoundException  
     *           If the operand contains a reference to a field (column) 
     *           that is not specified in the tuple type.
     * @throws TypeMismatchException 
     */
    void configure(TupleMetadata tupleType, Set<Attribute> correlatedAttributes) 
        throws ColumnNotFoundException, TypeMismatchException;

    /**
     * Returns the type of operand. This call returns a type only if configure
     * has been invoked before.
     * 
     * @return type of the operand value
     */
    int getType();

    /**
     * Gets the operand value with respect to the give tuple.
     * 
     * @param tuple
     *            tuple being evaluated. This tuple must conform to the tuple
     *            type specified in the last call to <code>configure</code>.
     * 
     * @return the value of the operand.
     * @throws ExpressionEvaluationException
     *             when there is problem with operand evaluation
     */
    Comparable<Object> getValue(Tuple tuple)
        throws ExpressionEvaluationException;
}
