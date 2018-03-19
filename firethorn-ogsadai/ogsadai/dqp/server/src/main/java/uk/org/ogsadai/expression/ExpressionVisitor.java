// Copyright (c) University of Edinburgh, 2008.
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

/**
 * Interface for expression visitors.  
 * <p>
 * See the Design Patterns book for more details of the Visitor pattern.
 *
 * @author The OGSA-DAI Project Team.
 */
public interface ExpressionVisitor
{
    /**
     * Call back method called by instances of <code>AndExpression</code>.
     * 
     * @param expression the expression to be visited.
     */
    public void visitAndExpression(AndExpression expression);

    /**
     * Call back method called by instances of <code>OrExpression</code>.
     * 
     * @param expression the expression to be visited.
     */
    public void visitOrExpression(OrExpression expression);
    
    /**
     * Call back method called by instances of <code>LessThanExpression</code>.
     * 
     * @param expression the expression to be visited.
     */
    public void visitLessThanExpression(LessThanExpression expression);

    /**
     * Call back method called by instances of 
     * <code>LessThanOrEqualExpression</code>.
     * 
     * @param expression the expression to be visited.
     */
    public void visitLessThanOrEqualExpression(LessThanOrEqualExpression expression);
    
    /**
     * Call back method called by instances of 
     * <code>GreaterThanExpression</code>.
     * 
     * @param expression the expression to be visited.
     */
    
    public void visitGreaterThanExpression(GreaterThanExpression expression);

    /**
     * Call back method called by instances of 
     * <code>GreaterThanOrEqualExpression</code>.
     * 
     * @param expression the expression to be visited.
     */
    public void visitGreaterThanOrEqualExpression(GreaterThanOrEqualExpression expression);
    
    /**
     * Call back method called by instances of <code>EqualExpression</code>.
     * 
     * @param expression the expression to be visited.
     */
    public void visitEqualExpression(EqualExpression expression);

    /**
     * Call back method called by instances of <code>NotEqualExpression</code>.
     * 
     * @param expression the expression to be visited.
     */
    public void visitNotEqualExpression(NotEqualExpression expression);

    /**
     * Call back method called by instances of <code>NotExpression</code>.
     * 
     * @param expression the expression to be visited.
     */
    public void visitNotExpression(NotExpression expression);

    /**
     * Call back method called by instances of <code>IsNullExpression</code>.
     * 
     * @param expression the expression to be visited.
     */
    public void visitIsNullExpression(IsNullExpression expression);

    /**
     * Call back method called by instances of <code>LikeExpression</code>.
     * 
     * @param expression the expression to be visited.
     */
    public void visitLikeExpression(LikeExpression expression);

    /**
     * Call back method called by instances of <code>InExpression</code>.
     * 
     * @param expression the expression to be visited.
     */
    public void visitInExpression(InExpression expression);
}
