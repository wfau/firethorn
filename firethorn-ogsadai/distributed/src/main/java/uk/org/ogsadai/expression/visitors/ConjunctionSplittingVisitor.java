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

package uk.org.ogsadai.expression.visitors;

import java.util.ArrayList;
import java.util.List;

import uk.org.ogsadai.expression.AndExpression;
import uk.org.ogsadai.expression.EqualExpression;
import uk.org.ogsadai.expression.Expression;
import uk.org.ogsadai.expression.ExpressionVisitor;
import uk.org.ogsadai.expression.GreaterThanExpression;
import uk.org.ogsadai.expression.GreaterThanOrEqualExpression;
import uk.org.ogsadai.expression.InExpression;
import uk.org.ogsadai.expression.IsNullExpression;
import uk.org.ogsadai.expression.LessThanExpression;
import uk.org.ogsadai.expression.LessThanOrEqualExpression;
import uk.org.ogsadai.expression.LikeExpression;
import uk.org.ogsadai.expression.NotEqualExpression;
import uk.org.ogsadai.expression.NotExpression;
import uk.org.ogsadai.expression.OrExpression;

/**
 * Conjunction splitting expression visitor.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class ConjunctionSplittingVisitor implements ExpressionVisitor
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /** List of split expressions. */
    private List<Expression> mSplitExpressionList = new ArrayList<Expression>();

    /**
     * Gets a list of split expressions.
     * 
     * @return split expressions list
     */
    public List<Expression> getSplitExpressions()
    {
        return mSplitExpressionList;
    }

    /**
     * {@inheritDoc}
     */
    public void visitAndExpression(AndExpression expression)
    {
        expression.getLeftExpression().accept(this);
        expression.getRightExpression().accept(this);
    }

    /**
     * {@inheritDoc}
     */
    public void visitEqualExpression(EqualExpression expression)
    {
        mSplitExpressionList.add(expression);
    }

    /**
     * {@inheritDoc}
     */
    public void visitGreaterThanExpression(GreaterThanExpression expression)
    {
        mSplitExpressionList.add(expression);
    }

    /**
     * {@inheritDoc}
     */
    public void visitGreaterThanOrEqualExpression(
        GreaterThanOrEqualExpression expression)
    {
        mSplitExpressionList.add(expression);
    }

    /**
     * {@inheritDoc}
     */
    public void visitIsNullExpression(IsNullExpression expression)
    {
        mSplitExpressionList.add(expression);
    }

    /**
     * {@inheritDoc}
     */
    public void visitLessThanExpression(LessThanExpression expression)
    {
        mSplitExpressionList.add(expression);
    }

    /**
     * {@inheritDoc}
     */
    public void visitLessThanOrEqualExpression(
        LessThanOrEqualExpression expression)
    {
        mSplitExpressionList.add(expression);
    }

    /**
     * {@inheritDoc}
     */
    public void visitLikeExpression(LikeExpression expression)
    {
        mSplitExpressionList.add(expression);
    }

    /**
     * {@inheritDoc}
     */
    public void visitNotEqualExpression(NotEqualExpression expression)
    {
        mSplitExpressionList.add(expression);
    }

    /**
     * {@inheritDoc}
     */
    public void visitNotExpression(NotExpression expression)
    {
        mSplitExpressionList.add(expression);
    }

    /**
     * {@inheritDoc}
     */
    public void visitOrExpression(OrExpression expression)
    {
        mSplitExpressionList.add(expression);
    }
    
    /**
     * {@inheritDoc}
     */
    public void visitInExpression(InExpression expression)
    {
        mSplitExpressionList.add(expression);
    }

}
