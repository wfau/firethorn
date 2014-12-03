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

package uk.org.ogsadai.dqp.lqp.udf;

import java.util.List;

import uk.org.ogsadai.dqp.lqp.Annotatable;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpression;

/**
 * Function interface.
 * 
 * @author The OGSA-DAI Project Team.
 */
public interface Function extends Annotatable
{
    /**
     * Initialises function with a list of parameters represented as arithmetic
     * expressions.
     * 
     * @param parameters
     *            list of parameters
     */
    public void initialise(List<ArithmeticExpression> parameters);
    
    /**
     * Gets function name.
     * 
     * @return function name
     */
    public String getName();
    
    /**
     * Gets function type.
     * 
     * @return function type
     */
    public FunctionType getType();
    
    /**
     * Gets a list of parameters represented as arithmetic expressions.
     * 
     * @return list of parameters
     */
    public List<ArithmeticExpression> getParameters();
    
    /**
     * Provides SQL equivalent for the function.
     * 
     * @return SQL representation
     */
    public String toSQL();
    
}
