//(c) University of Edinburgh, 2002 - 2007.
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

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.AttributeMatchMode;
import uk.org.ogsadai.dqp.lqp.RenameMap;
import uk.org.ogsadai.dqp.lqp.SimpleRenameMap;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpression;
import uk.org.ogsadai.expression.arithmetic.visitors.AttrExtrArithmeticExprVisitor;
import uk.org.ogsadai.expression.arithmetic.visitors.AttrRenameArithmeticExprVisitor;
import uk.org.ogsadai.expression.arithmetic.visitors.CloneArithmeticExprVisitor;
import uk.org.ogsadai.expression.arithmetic.visitors.SQLGenArithmeticExpressionVisitor;
import uk.org.ogsadai.expression.visitors.AttrExtrExpressionVisitor;
import uk.org.ogsadai.expression.visitors.AttrRenameExpressionVisitor;
import uk.org.ogsadai.expression.visitors.CloneExpressionVisitor;
import uk.org.ogsadai.expression.visitors.DerivedArithExtrExpressionVisitor;
import uk.org.ogsadai.expression.visitors.FuncExtrExpressionVisitor;
import uk.org.ogsadai.expression.visitors.SQLGenExpressionVisitor;

/**
 * Utility methods used to by expression classes.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class ExpressionUtils
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /** Logger. */
    private static final DAILogger LOG = DAILogger
        .getLogger(ExpressionUtils.class);

    /**
     * Private constructor. This class has only static methods.
     */
    private ExpressionUtils()
    {
        // Empty constructor.
    }

    /**
     * Renames attributes used in an expression with matching attributes from
     * the list. It is used to update attributes to their fully qualified names
     * using attribute lists extracted from child headings (actual attributes).
     * 
     * @param expr
     *            expression to be renamed
     * @param attributesToMatch
     *            a list of possibly matching attributes
     */
    public static void renameAttrWithMatching(Expression expr,
        List<Attribute> attributesToMatch)
    {
        RenameMap renameMap = new SimpleRenameMap();
        for (Attribute a : getUsedAttributes(expr))
        {
            Attribute match = a;
            for (Attribute h : attributesToMatch)
            {
                if (a.equals(h, AttributeMatchMode.NAME_AND_NULL_SOURCE))
                {
                    match = h;
                }
            }
            renameMap.add(a, match);
        }
        ExpressionUtils.renameUsedAttributes(expr, renameMap);
    }

    /**
     * Renames attributes used in an arithmetic expression with matching
     * attributes from the list. It is used to update attributes to their fully
     * qualified names using attribute lists extracted from child headings
     * (actual attributes).
     * 
     * @param expr
     *            expression to be renamed
     * @param attributesToMatch
     *            a list of possibly matching attributes
     */
    public static void renameAttrWithMatching(ArithmeticExpression expr,
        List<Attribute> attributesToMatch)
    {
        List<Attribute> origAttr = new LinkedList<Attribute>();
        List<Attribute> renmAttr = new LinkedList<Attribute>();

        for (Attribute a : getUsedAttributes(expr))
        {
            Attribute match = a;
            for (Attribute h : attributesToMatch)
            {
                if (a.equals(h, AttributeMatchMode.NAME_AND_NULL_SOURCE))
                {
                    match = h;
                }
            }
            origAttr.add(a);
            renmAttr.add(match);
        }
        ExpressionUtils.renameUsedAttributes(expr, new SimpleRenameMap(
                origAttr, renmAttr));
    }

    /**
     * Checks if functions are used in an expression.
     * 
     * @param expression
     *            expression to test
     * @return true if an expression uses functions
     */
    public static boolean usesFunctions(Expression expression)
    {
        FuncExtrExpressionVisitor v = new FuncExtrExpressionVisitor();
        expression.accept(v);
        return (v.getFunctions().size() > 0);
    }

    /**
     * Checks is the given arithmetic expression is derived. An arithmetic
     * expression is derived if it uses functions or arithmetic operators (i.e.
     * it is not simply a table column name).
     * 
     * @param expression
     *            expression to check.
     * 
     * @return <tt>true</tt> if the expression is derived, <tt>false</tt>
     *         otherwise.
     */
    public static boolean isDerived(ArithmeticExpression expression)
    {
        DerivedArithExtrExpressionVisitor v = 
            new DerivedArithExtrExpressionVisitor();
        expression.accept(v);
        return v.isDerived();
    }
    
    /**
     * Clones the expression.
     * 
     * @param expression
     *            expression to clone
     * @return cloned expression
     */
    public static Expression getClone(Expression expression)
    {
        CloneExpressionVisitor v = new CloneExpressionVisitor();
        expression.accept(v);
        return v.getClonedExpression();
    }
    
    /**
     * Clones the arithmetic expression.
     * 
     * @param expr
     *            expression to clone
     *            
     * @return cloned expression
     */
    public static ArithmeticExpression getClone(ArithmeticExpression expr)
    {
        return CloneArithmeticExprVisitor.cloneExpression(expr);
    }

    /**
     * Generates SQL compatible representation of an expression.
     * 
     * @param expression
     *            expression to convert
     * @return SQL expression
     */
    public static String generateSQL(Expression expression)
    {
        SQLGenExpressionVisitor v = new SQLGenExpressionVisitor();
        expression.accept(v);
        return v.toString();
    }
    
    /**
     * Generates the SQL compatible representation of an arithmetic expression.
     * 
     * @param expression expression to convert
     * 
     * @return SQL expression
     */
    public static String generateSQL(ArithmeticExpression expression)
    {
        SQLGenArithmeticExpressionVisitor v = 
            new SQLGenArithmeticExpressionVisitor();
        expression.accept(v);
        return v.getSQLString();
    }
    

    /**
     * Renames attributes used in an expression according to a specific rename
     * map.
     * 
     * @param expression
     *            expression to be renamed
     * @param renameMap
     *            rename map
     */
    public static void renameUsedAttributes(Expression expression,
            RenameMap renameMap)
    {
        AttrRenameExpressionVisitor v = new AttrRenameExpressionVisitor(
            renameMap);
        expression.accept(v);
    }

    /**
     * Renames attributes used in an arithmetic expression according to a
     * specific rename map.
     * 
     * @param expression
     *            expression to rename
     * @param renameMap
     *            rename map
     */
    public static void renameUsedAttributes(ArithmeticExpression expression,
            RenameMap renameMap)
    {
        AttrRenameArithmeticExprVisitor v = new AttrRenameArithmeticExprVisitor(
            renameMap);
        expression.accept(v);
    }

    /**
     * Returns a set of attributes used in the expression. This includes
     * attributes in function parameters. The type of extracted attributes will
     * be unknown (type code: <code>-1</code>).
     * 
     * @param expression
     *            expression to extract attributes from
     * @return a set of used attributes
     */
    public static Set<Attribute> getUsedAttributes(Expression expression)
    {
        AttrExtrExpressionVisitor v = new AttrExtrExpressionVisitor();
        expression.accept(v);
        return v.getAttributes();
    }

    /**
     * Returns a set of attributes used in the arithmetic expression. This
     * includes attributes in function parameters. The type of extracted
     * attributes will be unknown (type code: <code>-1</code>).
     * 
     * @param expression
     *            arithmetic expression
     * @return a set of used attributes.
     */
    public static Set<Attribute> getUsedAttributes(
        ArithmeticExpression expression)
    {
        AttrExtrArithmeticExprVisitor v = new AttrExtrArithmeticExprVisitor();
        expression.accept(v);
        return v.getAttributes();
    }
}
