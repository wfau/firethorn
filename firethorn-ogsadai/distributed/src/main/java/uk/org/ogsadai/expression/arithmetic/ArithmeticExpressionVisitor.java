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
 * Visits arithmetic expression nodes. This interface follows the visitor 
 * pattern.
 *
 * @author The OGSA-DAI Project Team.
 */
public interface ArithmeticExpressionVisitor
{
    /**
     * Visits a constant expression.
     * 
     * @param expression
     *            constant expression
     */
    public void visitConstant(Constant expression);
    
    /**
     * Visits a plus expression.
     * 
     * @param expression
     *            plus expression
     */
    public void visitPlus(Plus expression);

    /**
     * Visits a minus expression.
     * 
     * @param expression
     *            minus expression
     */
    public void visitMinus(Minus expression);

    /**
     * Visits a div expression.
     * 
     * @param expression
     *            div expression
     */
    public void visitDiv(Div expression);
    
    /**
     * Visits a mult expression.
     * 
     * @param expression
     *            mult expression
     */
    public void visitMult(Mult expression);
    
    /**
     * Visits a plus expression.
     * 
     * @param tableColumn
     *            table column expression
     */
    public void visitTableColumn(TableColumn tableColumn);

    /**
     * Visits an executable function expression.
     * 
     * @param function
     *            function expression
     */
    public void visitFunction(ExecutableFunctionExpression function);

    /**
     * Visits a star expression.
     * 
     * @param expression
     *            star expression
     */
    public void visitStar(Star expression);

}
