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

package uk.org.ogsadai.dqp.lqp.optimiser.rename;

import java.util.List;

import uk.org.ogsadai.dqp.common.CompilerConfiguration;
import uk.org.ogsadai.dqp.common.RequestDetails;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.operators.RenameOperator;
import uk.org.ogsadai.dqp.lqp.optimiser.Optimiser;
import uk.org.ogsadai.dqp.lqp.optimiser.OptimiserUtils;
import uk.org.ogsadai.resource.dataresource.dqp.RequestDQPFederation;

/**
 * Rename pull up optimiser.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class RenamePullUpOptimiser implements Optimiser
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /** Pull up strategy factory. */
    private RenamePullUpStrategyFactory mStrategyFactory;
    
    /** Skip annotation key - reset on every call to optimise. */
    private String mSkipAnnotationKey;
    
    /**
     * Constructor.  Creates a RENAME pull up optimiser that uses the default
     * strategies for pulling RENAME past each operator.
     */
    public RenamePullUpOptimiser()
    {
        mStrategyFactory = new RenamePullUpStrategyFactoryImpl();
    }
    
    /**
     * Constructor.  Creates a RENAME pull up optimiser that uses uses the given
     * strategy factory to obtain the strategies for pulling RENAME past each
     * operator.
     * 
     * @param strategyFactory RENAME pull up strategy factory.
     */
    public RenamePullUpOptimiser(RenamePullUpStrategyFactory strategyFactory)
    {
        mStrategyFactory = strategyFactory;
    }
    
    /**
     * {@inheritDoc}
     */
    public Operator optimise(
        Operator lqpRoot, 
        RequestDQPFederation requestFederation,
        CompilerConfiguration compilerConfiguration, 
        RequestDetails requestDetails) throws LQPException
    {
        // Reset annotation key
        mSkipAnnotationKey =
            RenamePullUpOptimiser.class.getSimpleName()
                + System.currentTimeMillis();
        
        List<Operator> candidateOperators;
        do
        {
            candidateOperators = OptimiserUtils.findPullUpCandidates(lqpRoot,
                OperatorID.RENAME, mSkipAnnotationKey);
            
            for(Operator op : candidateOperators)
            {
                RenameOperator renameOperator = renamePushUp((RenameOperator) op);
                renameOperator.addAnnotation(mSkipAnnotationKey, true);
            }
        }
        while(candidateOperators.size() > 0);

        // Remove temporary annotations
        for (Operator op : OptimiserUtils.findOccurrences(lqpRoot,
            OperatorID.RENAME))
        {
            op.removeAnnotation(mSkipAnnotationKey);
        }

        return lqpRoot;
    }

    /**
     * Main push up loop.
     * 
     * @param renameOperator
     *            operator to pull
     * @return pulled up RENAME operator
     * @throws LQPException
     *             if update after pull fails
     */
    private RenameOperator renamePushUp(final RenameOperator renameOperator)
        throws LQPException
    {
        RenameOperator currentRenameOperator = renameOperator;
        Operator renameParent;

        while ((renameParent = currentRenameOperator.getParent()).getID() != OperatorID.NIL)
        {
            RenamePullUpStrategy strategy = 
                mStrategyFactory.getStrategy(renameParent);
            RenameOperator pushedUpOperator = 
                strategy.pullUp(currentRenameOperator);
            if (pushedUpOperator == null)
            {
                break;
            }
            currentRenameOperator = pushedUpOperator;
        }

        return currentRenameOperator;
    }
    
}
