// Copyright (c) The University of Edinburgh, 2010-2012.
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

package uk.org.ogsadai.dqp.lqp.optimiser.implosion;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.dqp.common.CompilerConfiguration;
import uk.org.ogsadai.dqp.common.DataNode;
import uk.org.ogsadai.dqp.common.RequestDetails;
import uk.org.ogsadai.dqp.lqp.Annotation;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.AttributeImpl;
import uk.org.ogsadai.dqp.lqp.AttributeMatchMode;
import uk.org.ogsadai.dqp.lqp.AttributeUtils;
import uk.org.ogsadai.dqp.lqp.Branch;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.Predicate;
import uk.org.ogsadai.dqp.lqp.RenameMap;
import uk.org.ogsadai.dqp.lqp.SimpleRenameMap;
import uk.org.ogsadai.dqp.lqp.cardinality.CardinalityEstimator;
import uk.org.ogsadai.dqp.lqp.cardinality.CardinalityEstimatorOptimiser;
import uk.org.ogsadai.dqp.lqp.cardinality.CardinalityStatistics;
import uk.org.ogsadai.dqp.lqp.cardinality.CardinalityUtils;
import uk.org.ogsadai.dqp.lqp.exceptions.AmbiguousAttributeException;
import uk.org.ogsadai.dqp.lqp.exceptions.AmbiguousMappingException;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.operators.AbstractJoinOperator;
import uk.org.ogsadai.dqp.lqp.operators.ApplyOperator;
import uk.org.ogsadai.dqp.lqp.operators.BinaryOperator;
import uk.org.ogsadai.dqp.lqp.operators.DuplicateEliminationOperator;
import uk.org.ogsadai.dqp.lqp.operators.FullOuterJoinOperator;
import uk.org.ogsadai.dqp.lqp.operators.GroupByOperator;
import uk.org.ogsadai.dqp.lqp.operators.InnerThetaJoinOperator;
import uk.org.ogsadai.dqp.lqp.operators.LeftOuterJoinOperator;
import uk.org.ogsadai.dqp.lqp.operators.OneRowOnlyOperator;
import uk.org.ogsadai.dqp.lqp.operators.ProductOperator;
import uk.org.ogsadai.dqp.lqp.operators.ProjectOperator;
import uk.org.ogsadai.dqp.lqp.operators.RenameOperator;
import uk.org.ogsadai.dqp.lqp.operators.RightOuterJoinOperator;
import uk.org.ogsadai.dqp.lqp.operators.ScalarGroupByOperator;
import uk.org.ogsadai.dqp.lqp.operators.SelectOperator;
import uk.org.ogsadai.dqp.lqp.operators.SetOperator;
import uk.org.ogsadai.dqp.lqp.operators.TableScanOperator;
import uk.org.ogsadai.dqp.lqp.optimiser.Optimiser;
import uk.org.ogsadai.dqp.lqp.optimiser.OptimiserUtils;
import uk.org.ogsadai.dqp.lqp.optimiser.partitioner.PartitioningOptimiser;
import uk.org.ogsadai.dqp.lqp.optimiser.rename.RenamePullUpOptimiser;
import uk.org.ogsadai.dqp.lqp.udf.Function;
import uk.org.ogsadai.expression.ExpressionUtils;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpression;
import uk.org.ogsadai.resource.dataresource.dqp.RequestDQPFederation;

/**
 * Extended table scan implosion optimiser. Tries to push as much processing as
 * possible down to the scanned resource. The query plan is collapsed by
 * modifying the SQL query embedded in the TABLE_SCAN operator. The algorithm
 * makes extensive use of derived tables and can deal with correlated
 * subqueries. This optimiser can only be used after partitioning.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class ExtendedTableScanImplosionOptimiser implements Optimiser
{
    /** ID generator counter. */
    private static long mID = 0;
    /** Logger. */
    private static final DAILogger LOG = DAILogger
            .getLogger(ExtendedTableScanImplosionOptimiser.class);
    
    /** Cardinality estimator. */
    private CardinalityEstimator mCardinalityEstimator;
    private PartitioningOptimiser mPartitioner;
    private Optimiser mSimpleTableScanImplosion;
    
    /**
     * Sets the cardinality estimator to use.
     * 
     * @param cardinalityEstimator
     */
    public void setCardinalityEstimator(
        CardinalityEstimator cardinalityEstimator)
    {
        mCardinalityEstimator = cardinalityEstimator;
    }

    /**
     * Sets the partitioner.
     * 
     * @param partitioner  partitioner
     */
    public void setPartitioner(
        PartitioningOptimiser partitioner)
    {
        mPartitioner = partitioner;
    }
    @Override
    public Operator optimise(Operator lqpRoot,
            RequestDQPFederation requestFederation,
            CompilerConfiguration compilerConfiguration,
            RequestDetails requestDetails) throws LQPException 
    {
        mCardinalityEstimator.setDataDictionary(
                requestFederation.getDataDictionary());
        
        // Perform simple SPJ implosion. This is useful as the algorithm used
        // in TableScanImplosionOptimiser produces flat queries.
        //
        if (mSimpleTableScanImplosion != null)
        {
            mSimpleTableScanImplosion.optimise(
                    lqpRoot, 
                    requestFederation,
                    compilerConfiguration, 
                    requestDetails);
        }
        
        // Implosion algorithm is easier to implement when we do not have to
        // track aliases of derived tables (or in other words - when there are
        // no RENAME operators in the query plan). To ensure this we need to
        // give unique aliases to all the attributes produced by the TABLE_SCAN.
        // A RENAME operator with disambiguated attribute names can then be
        // safely pushed further up the tree.
        //
        for (Operator o : OptimiserUtils.getLeafs(Branch.LEFT, lqpRoot))
        {
            // TODO: If resource is select only - we can't improve - skip

            // Replace each TABLE_SCAN with a TABLE_SCAN using extended
            // ExtendedTableScanQuery object
            //
            TableScanOperator tsOperator = (TableScanOperator) o;
            ExtendedTableScanQuery newTSQuery = new ExtendedTableScanQuery(
                    tsOperator.getQuery(), tsOperator.getDataNode());

            // Generate disambiguating rename map
            //
            List<Attribute> originalAttrList = 
                tsOperator.getHeading().getAttributes();
            List<Attribute> renamedAttrList = new LinkedList<Attribute>();
            List<Attribute> newOrigAttrList = new LinkedList<Attribute>();
            for (Attribute a : originalAttrList) 
            {
                renamedAttrList.add(
                        new AttributeImpl(
                                a.getName(), a.getType(), a.getSource()));

                // No need for source. New attributes are "derived".
                String derivedName = nextID(a.getName());
                newOrigAttrList.add(
                        new AttributeImpl(derivedName, a.getType()));
            }
            RenameMap renameMap = 
                new SimpleRenameMap(newOrigAttrList, renamedAttrList);
            // Add disambiguating RENAME operator to the TABLE_SCAN query and
            // insert into the query plan.
            //
            RenameOperator renameOperator = new RenameOperator(renameMap);
            newTSQuery.addRename(renameMap);
            TableScanOperator newTSOperator = 
                new TableScanOperator(newTSQuery, tsOperator);
            newTSOperator.setDataDictionary(
                    requestFederation.getDataDictionary());
            
            // rename cardinality stats of table scan according to rename map
            CardinalityStatistics cardStats = 
                Annotation.getCardinalityStatisticsAnnotation(newTSOperator);
            if (cardStats != null)
            {
                Annotation.addCardinalityStatisticsAnnotation(
                    newTSOperator, 
                    CardinalityUtils.mapRenamedToOriginal(cardStats, renameMap));
            }

            renameOperator.setChild(0, newTSOperator);
            tsOperator.getParent().replaceChild(tsOperator, renameOperator);
            tsOperator.disconnect();
            renameOperator.getParent().update();
        }
        
        // Pull up disambiquating RENAME
        //
        RenamePullUpOptimiser renamePullUpOpt = new RenamePullUpOptimiser();
        renamePullUpOpt.optimise(lqpRoot, requestFederation,
                compilerConfiguration, requestDetails);

        // LQP pattern matching for correlated subqueries expects ONE_ROW_ONLY
        // operator to be a child of the APPLY operator. To ensure this we
        // pull ORO up.
        //
        OneRowOnlyPullUpOptimiser oroPullUpOpt = new OneRowOnlyPullUpOptimiser();
        oroPullUpOpt.optimise(lqpRoot, requestFederation,
                compilerConfiguration, requestDetails);
        
        // refresh cardinality estimates
        CardinalityEstimatorOptimiser cardOpt = new CardinalityEstimatorOptimiser();
        cardOpt.setCardinalityEstimator(mCardinalityEstimator);
        cardOpt.optimise(
                lqpRoot, 
                requestFederation,
                compilerConfiguration,
                requestDetails);

        // Run passes of the implosion algorithm until there is no change. The
        // implementation of the termination check is a bit hacky here as it
        // uses string representations to compare two query plans.
        //
        // TODO: Improve the termination check
        //
        boolean implode = true;
        while (implode) {
            String before = lqpRoot.toString();
            implode(lqpRoot);
            implode = !before.equals(lqpRoot.toString());
        }

        // This optimiser often introduces new operators - we need to refresh
        // the annotations after it is done
        
        mPartitioner.optimise(
                lqpRoot, 
                requestFederation, 
                compilerConfiguration,
                requestDetails);
        
        for (Operator o : OptimiserUtils.getLeafs(Branch.LEFT, lqpRoot))
        {
            Operator current = o;
            while ((current = current.getParent()) != null) 
            {
                if (current.getID() != OperatorID.EXCHANGE) 
                {
                    Annotation.addEvaluationNodeAnnotation(
                            current, 
                            Annotation.getEvaluationNodeAnnotation(
                                    current.getChild(0)));
                }
            }
        }
        
        return lqpRoot;
    }

    /**
     * Runs the implosion algorithm. First pass implodes non correlated
     * branches. Second pass implodes branches around APPLY operators. Third
     * pass pulls up any "freed" RENAME operators.
     * 
     * @param lqpRoot
     *            query plan to implode
     * @throws LQPException
     */
    private void implode(Operator lqpRoot) throws LQPException {
        // Implode non correlated. Right branches stop on binary operators. The
        // implosion of binary operators happens from the LHS.
        //
        for (Operator o : OptimiserUtils.getLeafs(Branch.RIGHT, lqpRoot)) {
            if (o instanceof TableScanOperator) {
                implodeNonCorrelated((TableScanOperator) o);
            }
        }

        // Implode APPLY where possible.
        //
        List<Operator> applyOperators = OptimiserUtils.findOccurrences(lqpRoot,
                OperatorID.APPLY);
        if (applyOperators.size() > 0) {
            for (Operator o : applyOperators) {
                implodeApply((ApplyOperator) o);
            }
        }

        // Some renames may be freed up after implosion. Pull these up.
        //
        RenamePullUpOptimiser pullupOpt = new RenamePullUpOptimiser();
        pullupOpt.optimise(lqpRoot, null, null, null);
    }

    /**
     * Implodes correlated branches around the APPLY operator.
     * 
     * @param applyOp
     *            apply operator to implode
     * @throws LQPException
     */
    private void implodeApply(ApplyOperator applyOp) throws LQPException {
        // We have several query plan patterns to match.
        //
        Operator parentOp = applyOp.getParent();
        Operator wrappedOp = applyOp.getOperator();
        Operator onerowonlyOp = applyOp.getChild(0);
        Operator renameOp = applyOp.getChild(1);
        
        // ORO operator can be on either LHS or RHS - swap if the initial guess
        // was wrong.
        //
        if (onerowonlyOp.getID() != OperatorID.ONE_ROW_ONLY) {
            Operator temp = onerowonlyOp;
            onerowonlyOp = renameOp;
            renameOp = temp;
        }

        // Look for patterns.
        //
        if (wrappedOp.getID() == OperatorID.LEFT_OUTER_JOIN
                && onerowonlyOp.getID() == OperatorID.ONE_ROW_ONLY
                && renameOp.getID() == OperatorID.RENAME
                && onerowonlyOp.getChild(0).getID() == OperatorID.TABLE_SCAN
                && renameOp.getChild(0).getID() == OperatorID.TABLE_SCAN) {
            // check if on the same resource
            if (!check((TableScanOperator) onerowonlyOp.getChild(0),
                    (TableScanOperator) renameOp.getChild(0))) {
                return;
            }

            // (SELECT{x} (APPLY{LOJ} (RENAME TS) (ORO{x} TS)))
            // (SELECT{x} (APPLY{LOJ} (ORO{x} TS) (RENAME TS)))
            // TODO:
            // (APPLY{LOJ} (RENAME TS) (ORO{x} TS))
            // (APPLY{LOJ} (ORO{x} TS) (RENAME TS))
            if (parentOp.getID() == OperatorID.SELECT) {
                implodeWhereCorrelation((SelectOperator) parentOp,
                        (ApplyOperator) applyOp,
                        (OneRowOnlyOperator) onerowonlyOp,
                        (RenameOperator) renameOp);
            }

            // (PROJECT{x} (APPLY{LOJ} (RENAME TS) (ORO{x} TS)))
            // (PROJECT{x} (APPLY{LOJ} (ORO{x} TS) (RENAME TS)))
            // TODO:
            // (APPLY{LOJ} (RENAME TS) (ORO{x} TS))
            // (APPLY{LOJ} (ORO{x} TS) (RENAME TS))
            if (parentOp.getID() == OperatorID.PROJECT) {
                implodeSelectListCorrelation((ProjectOperator) parentOp,
                        (ApplyOperator) applyOp,
                        (OneRowOnlyOperator) onerowonlyOp,
                        (RenameOperator) renameOp);
            }
        }

        // (APPLY{SJ, x} (RENAME TS) TS{x})
        // (APPLY{ASJ, x} (RENAME TS) TS{x})
        Operator tablescanOp = onerowonlyOp;
        if ((wrappedOp.getID() == OperatorID.SEMI_JOIN ||
                wrappedOp.getID() == OperatorID.ANTI_SEMI_JOIN)
                && tablescanOp.getID() == OperatorID.TABLE_SCAN
                && renameOp.getID() == OperatorID.RENAME
                && renameOp.getChild(0).getID() == OperatorID.TABLE_SCAN) {
            // check if on the same resource
            if (!check((TableScanOperator) tablescanOp,
                    (TableScanOperator) renameOp.getChild(0))) {
                return;
            }
            implodeExistCorrelation(
                    (ApplyOperator) applyOp,
                    (RenameOperator) renameOp,
                    (TableScanOperator) onerowonlyOp);

        }
        
        // (APPLY{SJ, x} TS TS{x})
        // (APPLY{ASJ, x} TS TS{x})
        if ((wrappedOp.getID() == OperatorID.SEMI_JOIN || 
                wrappedOp.getID() == OperatorID.ANTI_SEMI_JOIN)
                && tablescanOp.getID() == OperatorID.TABLE_SCAN
                && renameOp.getID() == OperatorID.TABLE_SCAN)
        {
            // check if on the same resource
            if (!check((TableScanOperator) tablescanOp,
                    (TableScanOperator) renameOp)) 
            {
                return;
            }
            implodeExistCorrelation(
                    (ApplyOperator) applyOp,
                    (TableScanOperator) renameOp, 
                    (TableScanOperator) onerowonlyOp);

        }
    }

    /**
     * Checks if TABLE_SCAN operators query the same resource (data node).
     * 
     * @param o1
     *            table scan operator
     * @param o2
     *            table scan operator
     * @return <code>true</code> if operators query the same resource
     */
    private boolean check(TableScanOperator o1, TableScanOperator o2) {
        return o1.getDataNode().equals(o2.getDataNode());
    }

    /**
     * Implodes correlated branch using the EXIST clause. Pattern:
     * <code>(APPLY{[A]SJ, x} (RENAME TS) TS{x})</code>.
     * 
     * @param apply
     *            APPLY operator
     * @param rename
     *            RENAME operator
     * @param corrScan
     *            correlated TABLE_SCAN operator
     * @throws LQPException
     */
    private void implodeExistCorrelation(ApplyOperator apply,
            RenameOperator rename, TableScanOperator corrScan)
            throws LQPException {
        // We need to rename attributes on the correlated side with information
        // extracted from non-correlated side.
        //
        Map<String, String> renameMap = getRenameMap(apply, rename);

        // Add correlated [NOT] EXISTS to the non-correlated query
        //
        TableScanOperator ncorScan = (TableScanOperator) rename.getChild(0);
        ((ExtendedTableScanQuery) ncorScan.getQuery()).addCorrelatedExists(
                apply.getOperator().getID(), corrScan.getQuery(), renameMap,
                ((AbstractJoinOperator) apply.getOperator()).getPredicate(),
                ncorScan.getHeading());

        replaceDisconnectImplode(apply, rename, ncorScan, corrScan);
    }

    private void implodeExistCorrelation(ApplyOperator apply,
            TableScanOperator ncorScan, TableScanOperator corrScan) 
    throws LQPException
    {
        // Add correlated [NOT] EXISTS to the non-correlated query
        //
        ((ExtendedTableScanQuery) ncorScan.getQuery()).addCorrelatedExists(
                apply.getOperator().getID(), 
                corrScan.getQuery(), 
                null,
                ((AbstractJoinOperator) apply.getOperator()).getPredicate(),
                ncorScan.getHeading());

        replaceDisconnectImplode(apply, ncorScan, corrScan);
    }

    /**
     * Implodes correlated branch using the WHERE clause. Patterns:
     * <code>(SELECT{x} (APPLY{LOJ} (RENAME TS) (ORO{x} TS)))</code>
     * <code>(SELECT{x} (APPLY{LOJ} (ORO{x} TS) (RENAME TS)))</code>.
     * 
     * @param select
     *            SELECT operator
     * @param apply
     *            APPLY operator
     * @param oro
     *            ONE_ROW_ONLY (ORO) operator
     * @param rename
     *            RENAME operator
     * @throws LQPException
     */
    private void implodeWhereCorrelation(SelectOperator select,
            ApplyOperator apply, OneRowOnlyOperator oro, RenameOperator rename)
            throws LQPException {
        // Get attribute that will be replaced with a subquery.
        //
        Attribute subqAttr = oro.getHeading().getAttributes().get(0);

        // If the result of ONE_ROW_ONLY branch is not used in the predicate
        // APPLY pattern does not match.
        //
        if (!AttributeUtils.containsMatching(subqAttr, select.getPredicate()
                .getAttributes(), AttributeMatchMode.NAME_AND_NULL_SOURCE)) {
            return;
        }

        Map<String, String> renameMap = getRenameMap(apply, rename);

        TableScanOperator corrScan = (TableScanOperator) oro.getChild(0);
        TableScanOperator ncorScan = (TableScanOperator) rename.getChild(0);

        // The attribute from the "non-correlated" side of the predicate needs
        // to be renamed.
        Predicate renamedPredicate = select.getPredicate().getClone();
        ExpressionUtils.renameUsedAttributes(
                renamedPredicate.getExpression(),
                rename.getRenameMap().getRenamedToOriginalMap(
                        renamedPredicate.getAttributes()));

        ((ExtendedTableScanQuery) ncorScan.getQuery()).addWhereSubquery(
                corrScan.getQuery(), ncorScan, renamedPredicate, renameMap,
                subqAttr);

        replaceDisconnectImplode(select, rename, ncorScan, oro, corrScan, apply);
    }

    /**
     * Implodes correlated branch as a subquery in the select list. Patterns:
     * <code>(PROJECT{x} (APPLY{LOJ} (RENAME TS) (ORO{x} TS)))</code>
     * <code>(PROJECT{x} (APPLY{LOJ} (ORO{x} TS) (RENAME TS)))</code>
     * 
     * @param project
     *            PROJECT opeartor
     * @param apply
     *            APPLY operatoe
     * @param oro
     *            ONE_ROW_ONLY (ORO) operator
     * @param rename
     *            RENAME opeartor
     * @throws LQPException
     */
    public void implodeSelectListCorrelation(ProjectOperator project,
            ApplyOperator apply, OneRowOnlyOperator oro, RenameOperator rename)
            throws LQPException {
        Attribute subqAttr = oro.getHeading().getAttributes().get(0);

        if (!AttributeUtils.containsMatching(subqAttr,
                project.getUsedAttributes(),
                AttributeMatchMode.NAME_AND_NULL_SOURCE)) {
            return;
        }

        Map<String, String> renameMap = getRenameMap(apply, rename);

        TableScanOperator corrScan = (TableScanOperator) oro.getChild(0);
        TableScanOperator ncorScan = (TableScanOperator) rename.getChild(0);

        // Rename project attributes. We explicitly pull the RENAME past PROJECT
        // here.
        RenameMap rnMap = new SimpleRenameMap();
        for (Attribute a : project.getUsedAttributes()) {
            Attribute rnAttr = rename.getRenameMap().getOriginalAttribute(a);
            if (rnAttr != null) {
                rnMap.add(a, rnAttr);
            }
        }
        project.renameUsedAttributes(rnMap);

        ((ExtendedTableScanQuery) ncorScan.getQuery()).addSelectListSubquery(
                corrScan.getQuery(), project, null, renameMap, subqAttr,
                ncorScan);

        // replaceDisconnectImplode(project, ncorScan, ncorScan, rename, oro,
        // corrScan, apply);
        corrScan.disconnect();
        oro.disconnect();
        apply.disconnect();
        implodeNonCorrelated(ncorScan);
    }

    /**
     * Generates string based rename map which contains names of correlated
     * attributes (extracted from APPLY) as keys and their renamed equivalents
     * (extracted from RENAME) as values. The RENAME operator comes from the non
     * correlated branch of the APPLY.
     * 
     * @param apply
     *            APPLY operator
     * @param rename
     *            RENAME operator
     * @return string rename map
     */
    private Map<String, String> getRenameMap(ApplyOperator apply,
            RenameOperator rename) {
        Map<String, String> renameMap = new HashMap<String, String>();
        for (Attribute a : apply.getAttributesToBind()) {
            try {
                renameMap.put(a.toString(), rename.getRenameMap()
                        .getOriginalAttribute(a).toString());
            } catch (Exception e) {
                // Something went really wrong
                throw new RuntimeException(e);
            }
        }
        for (Attribute a : ((AbstractJoinOperator) apply.getOperator())
                .getPredicate().getAttributes()) {
            try {
                if (rename.getRenameMap().getOriginalAttribute(a) != null)
                    renameMap.put(a.toString(), rename.getRenameMap()
                            .getOriginalAttribute(a).toString());
            } catch (AmbiguousMappingException e) {
                LOG.error(e);
                throw new RuntimeException(e);
            } catch (AmbiguousAttributeException e) {
                LOG.error(e);
            }
        }
        return renameMap;
    }

    /**
     * Implodes non-correlated query plan branches.
     * 
     * @param scan
     *            TABLE_SCAN operator
     * @throws LQPException
     */
    private void implodeNonCorrelated(TableScanOperator scan)
            throws LQPException {
        
        Operator parent = scan.getParent();

        // If data node does not support this operation then stop imploding
        DataNode dataNode = scan.getDataNode();
        if (!dataNode.supportsOperator(parent.getID())) {
            return;
        }

        // UNION / DIFFERENCE / INTERSECTION
        //
        // RENAME. It is usually impossible to implode a RENAME. However in case
        // of the set operators RENAME is used to enforce "union compatibility".
        // SQL has this check relaxed, so it is safe to implode.
        //
        if (parent.getID() == OperatorID.RENAME) {
            RenameOperator rename = (RenameOperator) parent;
            Operator setOp = rename.getParent();
            if (setOp instanceof SetOperator) {
                if (((BinaryOperator) setOp).getChildIndex(rename) == 1) {
                    // Skip - implode from LHS only
                    return;
                }
                Operator otherChild = setOp.getChild(1);
                if (otherChild.getID() == OperatorID.RENAME) {
                    Operator otherScan = otherChild.getChild(0);
                    if (otherScan.getID() == OperatorID.TABLE_SCAN) {
                        TableScanOperator otherTS = (TableScanOperator) otherScan;

                        // check if on the same resource
                        if (!check(scan, otherTS)) {
                            return;
                        }

                        ((ExtendedTableScanQuery) scan.getQuery()).addSetOp(
                                setOp.getID(),
                                ((SetOperator) setOp).isBagOperator(),
                                otherTS.getQuery(), setOp.getHeading());

                        replaceDisconnectImplode(setOp, rename, scan,
                                otherChild, otherScan);
                    }
                }
            }
        }

        // DUPLICATE_ELIMINATION
        //
        if (parent.getID() == OperatorID.DUPLICATE_ELIMINATION)
        {
            DuplicateEliminationOperator duplElim = 
                (DuplicateEliminationOperator) parent;
            ((ExtendedTableScanQuery) scan.getQuery())
                    .addDuplicateElim(duplElim.getHeading());

            replaceDisconnectImplode(parent, scan, scan);
        }

        // SCALAR_GROUP_BY
        //
        if (parent.getID() == OperatorID.SCALAR_GROUP_BY) 
        {
            ScalarGroupByOperator scalarGB = (ScalarGroupByOperator) parent;
            
            if (dataNodeSupports(dataNode, scalarGB.getAggregates())) 
            {
                ((ExtendedTableScanQuery) scan.getQuery())
                        .addScalarGroupBy(scalarGB);
                replaceDisconnectImplode(parent, scan, scan);
            }
        }

        // SELECT
        //
        if (parent.getID() == OperatorID.SELECT) {
            SelectOperator select = (SelectOperator) parent;
            
            if (dataNode.supportsExpression(
                select.getPredicate().getExpression()))
            {
                ((ExtendedTableScanQuery) scan.getQuery()).addSelect(select);
                replaceDisconnectImplode(parent, scan, scan);
            }
        }

        // PROJECT
        //
        if (parent.getID() == OperatorID.PROJECT) {
            
            ProjectOperator project = (ProjectOperator) parent;
            
            ProjectImplosionHelper helper = new ProjectImplosionHelper(
                dataNode, project, mCardinalityEstimator);

            helper.implodeProject(scan);

            if ( helper.implodedEverything() ) 
            {
                replaceDisconnectImplode(parent, scan, scan);
            }
        }

        // SEMI_JOIN, ANTI_SEMI_JOIN
        //
        if (parent.getID() == OperatorID.SEMI_JOIN
                || parent.getID() == OperatorID.ANTI_SEMI_JOIN) {
            if (((BinaryOperator) parent).getChildIndex(scan) == 1) {
                // Skip - implode from LHS only
                return;
            }
            Operator otherChild = parent.getChild(1);
            if (otherChild.getID() != OperatorID.TABLE_SCAN) {
                // We can implode only table scans
                return;
            }
            TableScanOperator otherTS = (TableScanOperator) otherChild;

            // check if on the same resource
            if (!check(otherTS, scan)) {
                return;
            }

            ((ExtendedTableScanQuery) scan.getQuery()).addExists(
                    (AbstractJoinOperator) parent, otherTS.getQuery());

            replaceDisconnectImplode(parent, scan, scan, otherTS);
        }

        // LEFT_OUTER_JOIN
        //
        if (parent.getID() == OperatorID.LEFT_OUTER_JOIN) {
            // We have several patterns to match
            if (((BinaryOperator) parent).getChildIndex(scan) == 1) {
                // Skip - implode from LHS only
                return;
            }
            Operator otherChild = parent.getChild(1);

            // (LOJ TS TS)
            if (otherChild.getID() == OperatorID.TABLE_SCAN) {
                LeftOuterJoinOperator lojOp = (LeftOuterJoinOperator) parent;
                TableScanOperator otherTS = (TableScanOperator) otherChild;

                // check if on the same resource
                if (!check(scan, otherTS)) {
                    return;
                }
                implodeOuterJoin(lojOp, scan, otherTS);
            }

            // (LOJ TS (ORO TS))
            if (otherChild.getID() == OperatorID.ONE_ROW_ONLY) {
                OneRowOnlyOperator oneRowOnlyOp = (OneRowOnlyOperator) otherChild;

                // When we can match the following rules we can reduce the
                // amount of nesting in a query
                // (PROJECT{x} (LOJ TS (ORO{x} TS)))
                // (SELECT{x} (LOJ TS (ORO{x} TS)))
                Operator lojParent = parent.getParent();

                TableScanOperator otherTS;
                if (oneRowOnlyOp.getChild(0) instanceof TableScanOperator) {
                    otherTS = (TableScanOperator) oneRowOnlyOp.getChild(0);
                } else {
                    // Skip - TS expected
                    return;
                }

                // check if on the same resource
                if (!check(scan, otherTS)) {
                    return;
                }

                if (lojParent.getID() == OperatorID.PROJECT) {
                    implodeSelectListSubquery((ProjectOperator) lojParent,
                            (LeftOuterJoinOperator) parent, oneRowOnlyOp, scan,
                            otherTS);
                } else if (lojParent.getID() == OperatorID.SELECT) {
                    implodeWhereSubquery((SelectOperator) lojParent,
                            (LeftOuterJoinOperator) parent, oneRowOnlyOp, scan,
                            otherTS);
                } else {
                    implodeSelectListSubquery(null,
                            (LeftOuterJoinOperator) parent, oneRowOnlyOp, scan,
                            otherTS);
                }
            }
        }

        // RIGHT_OUTER_JOIN
        //
        if (parent.getID() == OperatorID.RIGHT_OUTER_JOIN) {
            // We have several patterns to match
            if (((BinaryOperator) parent).getChildIndex(scan) == 1) {
                // Skip - implode from LHS only
                return;
            }
            Operator otherChild = parent.getChild(1);
            if (otherChild.getID() != OperatorID.TABLE_SCAN) {
                // We can implode only table scans
                return;
            }
            TableScanOperator otherTS = (TableScanOperator) otherChild;
            // check if on the same resource
            if (!check(scan, otherTS)) {
                return;
            }
            implodeOuterJoin((RightOuterJoinOperator) parent, scan, otherTS);
        }

        // FULL_OUTER_JOIN
        //
        if (parent.getID() == OperatorID.FULL_OUTER_JOIN) {
            // We have several patterns to match
            if (((BinaryOperator) parent).getChildIndex(scan) == 1) {
                // Skip - implode from LHS only
                return;
            }
            Operator otherChild = parent.getChild(1);
            if (otherChild.getID() != OperatorID.TABLE_SCAN) {
                // We can implode only table scans
                return;
            }
            TableScanOperator otherTS = (TableScanOperator) otherChild;
            // check if on the same resource
            if (!check(scan, otherTS)) {
                return;
            }
            implodeOuterJoin((FullOuterJoinOperator) parent, scan, otherTS);
        }

        // INNER_THETA_JOIN, PRODUCT
        //
        if (parent.getID() == OperatorID.INNER_THETA_JOIN
                || parent.getID() == OperatorID.PRODUCT) 
        {
            if (((BinaryOperator) parent).getChildIndex(scan) == 1) {
                // Skip - implode from LHS only
                return;
            }
            Operator otherChild = parent.getChild(1);
            if (otherChild.getID() != OperatorID.TABLE_SCAN) {
                // We can implode only table scans
                return;
            }
            TableScanOperator otherTS = (TableScanOperator) otherChild;

            // check if on the same resource
            if (!check(scan, otherTS))
            {
                return;
            }

            if (parent.getID() == OperatorID.INNER_THETA_JOIN) 
            {
                InnerThetaJoinOperator join = (InnerThetaJoinOperator) parent;

                ((ExtendedTableScanQuery) scan.getQuery()).addJoin(join,
                        otherTS);
            } 
            else if (parent.getID() == OperatorID.PRODUCT)
            {
                ((ExtendedTableScanQuery) scan.getQuery()).addProduct(
                        (ProductOperator) parent, otherTS);
            }

            // replaceDisconnectImplode(parent, scan, scan, otherTS);
            otherTS.disconnect();
            scan.getParent().update();
            implodeNonCorrelated(scan);

        }

        // GROUP_BY
        //
        if (parent.getID() == OperatorID.GROUP_BY) {
            GroupByOperator groupBy = (GroupByOperator) parent;
            
            if (dataNodeSupports(dataNode, groupBy.getAggregates()))
            {
                ((ExtendedTableScanQuery) scan.getQuery()).addGroupBy(groupBy);
                replaceDisconnectImplode(parent, scan, scan);
            }
        }

    }

    /**
     * Implodes [FULL | LEFT | RIGHT]_OUTER_JOIN.
     * 
     * @param joinOp
     *            join operator to implode
     * @param leftScanOp
     *            left scan operator
     * @param rightScanOp
     *            right scan operator
     * @throws LQPException
     */
    private void implodeOuterJoin(AbstractJoinOperator joinOp,
            TableScanOperator leftScanOp, TableScanOperator rightScanOp)
            throws LQPException {
        ((ExtendedTableScanQuery) leftScanOp.getQuery()).addOuterJoin(
                joinOp.getID(), joinOp.getPredicate(), rightScanOp.getQuery(),
                joinOp.getHeading());

        replaceDisconnectImplode(joinOp, leftScanOp, leftScanOp, rightScanOp);
    }

    /**
     * Implodes into where subquery.
     * 
     * @param selectOp
     *            select operator
     * @param lojOp
     *            left outer join operator
     * @param oneRowOnlyOp
     *            one row only operator
     * @param scanOp
     *            base table scan
     * @param subqScanOp
     *            subquery table scan
     * @throws LQPException
     */
    private void implodeWhereSubquery(SelectOperator selectOp,
            LeftOuterJoinOperator lojOp, OneRowOnlyOperator oneRowOnlyOp,
            TableScanOperator scanOp, TableScanOperator subqScanOp)
            throws LQPException 
    {
        Attribute subqAttr = oneRowOnlyOp.getHeading().getAttributes().get(0);

        ((ExtendedTableScanQuery) scanOp.getQuery()).addWhereSubquery(
                subqScanOp.getQuery(), scanOp, selectOp.getPredicate(), null,
                subqAttr);

        replaceDisconnectImplode(
                selectOp, scanOp, scanOp, lojOp, oneRowOnlyOp, subqScanOp);
    }

    /**
     * Implodes into select list subquery.
     * 
     * @param projectOp
     *            project opeartor
     * @param lojOp
     *            left outer join operator
     * @param oneRowOnlyOp
     *            one row only operator
     * @param scanOp
     *            base table scan
     * @param subqScanOp
     *            subquery table scan
     * @throws LQPException
     */
    private void implodeSelectListSubquery(ProjectOperator projectOp,
            LeftOuterJoinOperator lojOp, OneRowOnlyOperator oneRowOnlyOp,
            TableScanOperator scanOp, TableScanOperator subqScanOp)
            throws LQPException 
    {
        Attribute subqAttr = oneRowOnlyOp.getHeading().getAttributes().get(0);

        ((ExtendedTableScanQuery) scanOp.getQuery()).addSelectListSubquery(
                subqScanOp.getQuery(), projectOp, lojOp, null, subqAttr,
                lojOp.getChild(0));

        if (projectOp != null) 
        {
            projectOp.disconnect();
        }
        oneRowOnlyOp.disconnect();
        subqScanOp.disconnect();
        implodeNonCorrelated(scanOp);
    }

    /**
     * Replaces <code>oldOp</code> with <code>scanOp</code>, disconnects
     * <code>oldOp</code> and <code>operators</code>, updates parent of
     * <code>oldOp</code>.
     * 
     * @param oldOp
     *            operator to be replaced
     * @param newOp
     *            replacement operator
     * @param scan
     *            scan opertaror to continure
     * @param operators
     *            operators to be disconnected
     * @throws LQPException
     *             when update fails
     */
    private void replaceDisconnectImplode(Operator oldOp, Operator newOp,
            TableScanOperator scan, Operator... operators) throws LQPException 
    {
        Annotation.addCardinalityAnnotation(
                newOp, Annotation.getCardinalityAnnotation(oldOp));
        Annotation.addCardinalityStatisticsAnnotation(
                newOp, Annotation.getCardinalityStatisticsAnnotation(oldOp));
        
        oldOp.getParent().replaceChild(oldOp, newOp);
        oldOp.disconnect();

        for (Operator o : operators)
            o.disconnect();
        newOp.getParent().update();
        implodeNonCorrelated(scan);
    }

    /**
     * Generates an ID by appending _ and a next long number to a prefix.
     * 
     * @param prefix
     *            ID prefix
     * @return new ID
     */
    public static String nextID(String prefix) 
    {
        if (mID == Long.MAX_VALUE)
            mID = 0;

        return prefix + '_' + (++mID);
    }

    /**
     * Does the data node support the given functions.
     * 
     * @param dataNode
     *            data node
     * @param functions
     *            functions
     * 
     * @return <tt>true</tt> if all the functions are supported, <tt>false</tt>
     *         otherwise.
     */
    private boolean dataNodeSupports(
        DataNode dataNode, Collection<Function> functions) {
        
        for( Function f : functions) {
            if (!dataNode.supportsFunction(f)) return false;
            
            for (ArithmeticExpression expr : f.getParameters()) {
                if (!dataNode.supportsArithmeticExpression(expr)) return false;
            }
            
        }
        return true;
    }
    
    public void setTableScanImplosionOptimiser(Optimiser optimiser)
    {
        mSimpleTableScanImplosion = optimiser;
    }
    
}
