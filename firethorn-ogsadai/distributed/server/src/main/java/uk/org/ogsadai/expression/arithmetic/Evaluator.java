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

/**
 * Evaluates arithmetic operations +, -, * and /.
 *
 * @author The OGSA-DAI Project Team.
 */
public interface Evaluator
{
    /**
     * Adds the given values.
     * 
     * @param value1
     *            left hand side value
     * @param value2
     *            right hand side value
     * @return <code>value1</code> + <code>value2</code>
     */
    public Number plus(Number value1, Number value2);
    
    /**
     * Subtracts value2 from value1.
     * 
     * @param value1
     *            left hand side value
     * @param value2
     *            right hand side value
     * @return <code>value1</code> - <code>value2</code>
     */
    public Number minus(Number value1, Number value2);
    
    /**
     * Divides value1 by value2.
     * 
     * @param value1
     *            left hand side value
     * @param value2
     *            right hand side value
     * @return <code>value1</code> / <code>value2</code>
     */
    public Number div(Number value1, Number value2);

    /**
     * Multiplies two values.
     * 
     * @param value1
     *            left hand side value
     * @param value2
     *            right hand side value
     * @return <code>value1</code> * <code>value2</code>
     */
    public Number mult(Number value1, Number value2);
    
    /**
     * Returns the minimum of two values.
     * 
     * @param value1
     *            left hand side value
     * @param value2
     *            right hand side value
     * @return the smaller of value1 and value2
     */
    public Number min(Number value1, Number value2);

    /**
     * Returns the maximum of two values.
     * 
     * @param value1
     *            left hand side value
     * @param value2
     *            right hand side value
     * @return the greater of value1 and value2
     */
    public Number max(Number value1, Number value2);
    
}
