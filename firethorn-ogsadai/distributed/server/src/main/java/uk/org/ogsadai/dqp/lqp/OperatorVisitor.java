// Copyright (c) The University of Edinburgh, 2008.
// See OGSA-DAI-Licence.txt for licencing information.

package uk.org.ogsadai.dqp.lqp;

import uk.org.ogsadai.dqp.lqp.operators.AntiSemiJoinOperator;
import uk.org.ogsadai.dqp.lqp.operators.ApplyOperator;
import uk.org.ogsadai.dqp.lqp.operators.BinaryRelFunctionOperator;
import uk.org.ogsadai.dqp.lqp.operators.DifferenceOperator;
import uk.org.ogsadai.dqp.lqp.operators.DuplicateEliminationOperator;
import uk.org.ogsadai.dqp.lqp.operators.ExchangeOperator;
import uk.org.ogsadai.dqp.lqp.operators.FullOuterJoinOperator;
import uk.org.ogsadai.dqp.lqp.operators.GroupByOperator;
import uk.org.ogsadai.dqp.lqp.operators.InnerThetaJoinOperator;
import uk.org.ogsadai.dqp.lqp.operators.IntersectionOperator;
import uk.org.ogsadai.dqp.lqp.operators.LeftOuterJoinOperator;
import uk.org.ogsadai.dqp.lqp.operators.OneRowOnlyOperator;
import uk.org.ogsadai.dqp.lqp.operators.ProductOperator;
import uk.org.ogsadai.dqp.lqp.operators.ProjectOperator;
import uk.org.ogsadai.dqp.lqp.operators.RenameOperator;
import uk.org.ogsadai.dqp.lqp.operators.RightOuterJoinOperator;
import uk.org.ogsadai.dqp.lqp.operators.ScalarGroupByOperator;
import uk.org.ogsadai.dqp.lqp.operators.ScanRelFunctionOperator;
import uk.org.ogsadai.dqp.lqp.operators.SelectOperator;
import uk.org.ogsadai.dqp.lqp.operators.SemiJoinOperator;
import uk.org.ogsadai.dqp.lqp.operators.SortOperator;
import uk.org.ogsadai.dqp.lqp.operators.TableScanOperator;
import uk.org.ogsadai.dqp.lqp.operators.UnaryRelFunctionOperator;
import uk.org.ogsadai.dqp.lqp.operators.UnionOperator;

/**
 * Operator visitor interface.
 * 
 * @author The OGSA-DAI Project Team.
 */
public interface OperatorVisitor
{
    /**
     * Visits a <code>SELECT</code> operator.
     * 
     * @param operator
     *            operator to visit
     */
    public void visit(SelectOperator operator);
    
    /**
     * Visits a <code>PROJECT</code> operator.
     * 
     * @param operator
     *            operator to visit
     */
    public void visit(ProjectOperator operator);
    
    /**
     * Visits a <code>RENAME</code> operator.
     * 
     * @param operator
     *            operator to visit
     */
    public void visit(RenameOperator operator);
    
    /**
     * Visits a <code>DUPLICATE_ELIMINATION</code> operator.
     * 
     * @param operator
     *            operator to visit
     */
    public void visit(DuplicateEliminationOperator operator);
    
    /**
     * Visits a <code>SORT</code> operator.
     * 
     * @param operator
     *            operator to visit
     */
    public void visit(SortOperator operator);
    
    /**
     * Visits a <code>ONE_ROW_ONLY</code> operator.
     * 
     * @param operator
     *            operator to visit
     */
    public void visit(OneRowOnlyOperator operator);
    
    /**
     * Visits a <code>GROUP_BY</code> operator.
     * 
     * @param operator
     *            operator to visit
     */
    public void visit(GroupByOperator operator);
    
    /**
     * Visits a <code>SCALAR_GROUP_BY</code> operator.
     * 
     * @param operator
     *            operator to visit
     */
    public void visit(ScalarGroupByOperator operator);
    
    /**
     * Visits a <code>TABLE_SCAN</code> operator.
     * 
     * @param operator
     *            operator to visit
     */
    public void visit(TableScanOperator operator);
    
    /**
     * Visits a <code>EXCHANGE</code> operator.
     * 
     * @param operator
     *            operator to visit
     */
    public void visit(ExchangeOperator operator);
    
    /**
     * Visits a <code>PRODUCT</code> operator.
     * 
     * @param operator
     *            operator to visit
     */
    public void visit(ProductOperator operator);
    
    /**
     * Visits a <code>INNER_THETA_JOIN</code> operator.
     * 
     * @param operator
     *            operator to visit
     */
    public void visit(InnerThetaJoinOperator operator);
    
    /**
     * Visits a <code>SEMI_JOIN</code> operator.
     * 
     * @param operator
     *            operator to visit
     */
    public void visit(SemiJoinOperator operator);
    
    /**
     * Visits a <code>ANTI_SEMI_JOIN</code> operator.
     * 
     * @param operator
     *            operator to visit
     */
    public void visit(AntiSemiJoinOperator operator);
    
    /**
     * Visits a <code>FULL_OUTER_JOIN</code> operator.
     * 
     * @param operator
     *            operator to visit
     */
    public void visit(FullOuterJoinOperator operator);
    
    /**
     * Visits a <code>LEFT_OUTER_JOIN</code> operator.
     * 
     * @param operator
     *            operator to visit
     */
    public void visit(LeftOuterJoinOperator operator);
    
    /**
     * Visits a <code>RIGHT_OUTER_JOIN</code> operator.
     * 
     * @param operator
     *            operator to visit
     */
    public void visit(RightOuterJoinOperator operator);

    /**
     * Visits a <code>APPLY</code> operator.
     * 
     * @param operator
     *            operator to visit
     */
    public void visit(ApplyOperator operator);
    
    /**
     * Visits a <code>UNION</code> operator.
     * 
     * @param operator
     *            operator to visit
     */
    public void visit(UnionOperator operator);
    
    /**
     * Visits a <code>INTERSECTION</code> operator.
     * 
     * @param operator
     *            operator to visit
     */
    public void visit(IntersectionOperator operator);
    
    /**
     * Visits a <code>DIFFERENCE</code> operator.
     * 
     * @param operator
     *            operator to visit
     */
    public void visit(DifferenceOperator operator);
    
    /**
     * Visits a <code>UNARY_REL_FUNCTION</code> operator.
     * 
     * @param operator
     *            operator to visit
     */
    public void visit(UnaryRelFunctionOperator operator);
    
    /**
     * Visits a <code>BINARY_REL_FUNCTION</code> operator.
     * 
     * @param operator
     *            operator to visit
     */
    public void visit(BinaryRelFunctionOperator operator);
    
    /**
     * Visits a <code>SCAN_REL_FUNCTION</code> operator.
     * 
     * @param operator
     *            operator to visit
     */
    public void visit(ScanRelFunctionOperator operator);
    
    /**
     * Visits any operator. The default if more specific call does not exist.
     * 
     * @param operator
     *            operator to visit
     */
    public void visit(Operator operator);
}
