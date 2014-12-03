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

package uk.org.ogsadai.dqp.lqp.optimiser.select;

import uk.org.ogsadai.dqp.common.CompilerConfiguration;
import uk.org.ogsadai.dqp.common.RequestDetails;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.operators.SelectOperator;
import uk.org.ogsadai.dqp.lqp.optimiser.Optimiser;
import uk.org.ogsadai.dqp.lqp.optimiser.OptimiserUtils;
import uk.org.ogsadai.resource.dataresource.dqp.RequestDQPFederation;

/**
 * Optimiser trying to push SELECT operators down the LQP.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class SelectPushDownOptimiser implements Optimiser
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";
    
    /** Strategy factory. */
    private SelectPushDownStrategyFactory mStrategyFactory; 

    /** Local annotation key used to tag pushed down operators. This variable 
     * is reset on each call to optimise. */
    private String mOperatorPushedDownKey;
    
    /**
     * Constructor.  Creates a SELECT push down optimiser that uses the default
     * strategies for pushing SELECT past each operator.
     */
    public SelectPushDownOptimiser()
    {
        mStrategyFactory = new SelectPushDownStrategyFactoryImpl();
    }
    
    /**
     * Constructor.  Creates a SELECT push down optimiser that uses the given
     * strategy factory to obtain the strategies for pushing SELECT past each
     * operator.
     * 
     * @param strategyFactory SELECT push down strategy factory.
     */
    public SelectPushDownOptimiser(
        SelectPushDownStrategyFactory strategyFactory)
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
        // Set new annotation name
        mOperatorPushedDownKey =
            SelectPushDownOptimiser.class.getSimpleName()
                + System.currentTimeMillis();
        
        selectPushDown(lqpRoot);

        // Remove temporary annotations
        for (Operator op : OptimiserUtils.findOccurrences(lqpRoot,
            OperatorID.SELECT))
        {
            op.removeAnnotation(mOperatorPushedDownKey);
        }
        
        return lqpRoot;
    }

    /**
     * Pushes down select operators.
     * 
     * @param lqpRoot
     *            root operator of the query plan on which to select push down
     *            will be performed
     * @throws LQPException
     */
    private void selectPushDown(final Operator lqpRoot) throws LQPException
    {
        Operator root = lqpRoot;

        // if select - try to push down
        if (root.getID() == OperatorID.SELECT
            && root.getAnnotation(mOperatorPushedDownKey) == null)
        {
            Operator selectParent = root.getParent();

            SelectOperator splitSelect = ((SelectOperator) root).expolde();
            pushDown(splitSelect);
            selectPushDown(selectParent);
        }
        // if binary - continue scan for merge in each branch
        else if (root.isBinary())
        {
            selectPushDown(root.getChild(0));
            selectPushDown(root.getChild(1));
        }
        // unary - continue scan for merge
        else
        {
            root = root.getChild(0);
            if (root != null)
                selectPushDown(root);
            else
                return;
        }
    }

    /**
     * Push down select operator.
     * 
     * @param selectOperator
     *            select operator to be pushed down
     * @throws LQPException
     */
    private void pushDown(final SelectOperator selectOperator)
        throws LQPException
    {
        while (true)
        {
            SelectPushDownStrategy strategy = mStrategyFactory
                .getStrategy(selectOperator.getChild(0));
            
            if(strategy.pushDown(selectOperator) == null)
            {
                break;
            }
        }
        selectOperator.addAnnotation(mOperatorPushedDownKey, true);
    }

}
