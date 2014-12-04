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

import uk.org.ogsadai.tuple.TupleTypes;
import uk.org.ogsadai.tuple.TypeMismatchException;

/**
 * Numeric evaluators.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class NumericEvaluators
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2007.";

    private final static Evaluator SHORT_EVAL = new ShortEvaluator();
    private final static Evaluator INTEGER_EVAL = new IntegerEvaluator();
    private final static Evaluator LONG_EVAL = new LongEvaluator();
    private final static Evaluator FLOAT_EVAL = new FloatEvaluator();
    private final static Evaluator DOUBLE_EVAL = new DoubleEvaluator();
    private final static Evaluator BIGDECIMAL_EVAL = new BigDecimalEvaluator();

    /**
     * Returns an evaluator for the given numeric type.
     * 
     * @param tupleType
     *            tuple type
     * @return an evaluator
     * @throws TypeMismatchException
     *             when evaluator for a given type is not supported
     */
    public static Evaluator getEvaluator(int tupleType)
        throws TypeMismatchException
    {
        switch (tupleType)
        {
            case TupleTypes._SHORT:
                return SHORT_EVAL;
            case TupleTypes._INT:
                return INTEGER_EVAL;
            case TupleTypes._LONG:
                return LONG_EVAL;
            case TupleTypes._FLOAT:
                return FLOAT_EVAL;
            case TupleTypes._DOUBLE:
                return DOUBLE_EVAL;
            case TupleTypes._BIGDECIMAL:
                return BIGDECIMAL_EVAL;
            default:
                throw new TypeMismatchException(tupleType);
        }
    }

}
