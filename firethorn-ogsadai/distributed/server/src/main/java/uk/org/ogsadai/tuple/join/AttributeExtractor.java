// Copyright (c) The University of Edinburgh, 2008-2012.
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


package uk.org.ogsadai.tuple.join;

import uk.org.ogsadai.expression.arithmetic.ArithmeticExpression;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpressionVisitor;
import uk.org.ogsadai.expression.arithmetic.Constant;
import uk.org.ogsadai.expression.arithmetic.Div;
import uk.org.ogsadai.expression.arithmetic.ExecutableFunctionExpression;
import uk.org.ogsadai.expression.arithmetic.Minus;
import uk.org.ogsadai.expression.arithmetic.Mult;
import uk.org.ogsadai.expression.arithmetic.Plus;
import uk.org.ogsadai.expression.arithmetic.Star;
import uk.org.ogsadai.expression.arithmetic.TableColumn;
import uk.org.ogsadai.tuple.ColumnNotFoundException;
import uk.org.ogsadai.tuple.TupleMetadata;

/**
 * Extracts the attributes used in an expression.
 *
 * @author The OGSA-DAI Project Team.
 */
public class AttributeExtractor implements ArithmeticExpressionVisitor
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008-2012";
    
    /** Tuple metadata. */
    private TupleMetadata mMetadata;

    /**
     * Extract attributes from the given arithmetic expression.
     * 
     * @param expression
     *            expression
     * @param metadata
     *            tuple metadata
     * @return the indexes of used attributes in the tuple metadata
     */
    public boolean containsAttributes(
            ArithmeticExpression expression, 
            TupleMetadata metadata)
    {
        mMetadata = metadata;
        try
        {       
            expression.accept(this);
            return true;
        }
        catch (ColumnNotFoundException e)
        {
            return false;
        }
    }

    @Override
    public void visitConstant(Constant expression)
    {
    }

    @Override
    public void visitDiv(Div expression)
    {
        for (ArithmeticExpression child : expression.getChildren())
        {
            child.accept(this);
        }
    }

    @Override
    public void visitFunction(ExecutableFunctionExpression function)
    {
        for (ArithmeticExpression child : function.getChildren())
        {
            child.accept(this);
        }
    }

    @Override
    public void visitMinus(Minus expression)
    {
        for (ArithmeticExpression child : expression.getChildren())
        {
            child.accept(this);
        }
    }

    @Override
    public void visitMult(Mult expression)
    {
        for (ArithmeticExpression child : expression.getChildren())
        {
            child.accept(this);
        }
    }

    @Override
    public void visitPlus(Plus expression)
    {
        for (ArithmeticExpression child : expression.getChildren())
        {
            child.accept(this);
        }
    }

    @Override
    public void visitTableColumn(TableColumn tableColumn)
    {
        if (mMetadata != null)
        {
            // throws a ColumnNotFoundException if the column is not found in the
            // metadata
            tableColumn.getColumnIndex(mMetadata);
        }
    }

    @Override
    public void visitStar(Star expression)
    {
    }

}
