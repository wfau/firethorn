// Copyright (c) The University of Edinburgh, 2009.
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

package uk.org.ogsadai.dqp.lqp.optimiser.project.pushdown;

import java.util.List;

import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.dqp.common.CompilerConfiguration;
import uk.org.ogsadai.dqp.common.RequestDetails;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.operators.ApplyOperator;
import uk.org.ogsadai.dqp.lqp.operators.ProjectOperator;
import uk.org.ogsadai.dqp.lqp.optimiser.Optimiser;
import uk.org.ogsadai.dqp.lqp.optimiser.OptimiserUtils;
import uk.org.ogsadai.resource.dataresource.dqp.RequestDQPFederation;

/**
 * Optimiser to push projects down so that the redundant rows are not extracted
 * from the databases, transfered over the wire etc.
 *
 * @author The OGSA-DAI Project Team
 */
public class ProjectPushDownOptimiser implements Optimiser
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009.";

    /** Logger. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(ProjectPushDownOptimiser.class);

    /** Strategy factory. */
    private ProjectPushDownStrategyFactory mStrategyFactory;
    /** Ignore annotation key. */
    public static final String IGNORE = "ProjectPushDownOptimiser.IGNORE";

    /**
     * Constructor.  Creates a PROJECT push down optimiser that uses the default
     * strategies for pushing PROJECT past each operator.
     */
    public ProjectPushDownOptimiser()
    {
        mStrategyFactory = new ProjectPushDownStrategyFactoryImpl();
    }
    
    /**
     * Constructor.  Creates a PROJECT push down optimiser that uses uses the 
     * given strategy factory to obtain the strategies for push PROJECT past 
     * each operator.
     * 
     * @param strategyFactory PROJECT push down strategy factory.
     */
    public ProjectPushDownOptimiser(
        ProjectPushDownStrategyFactory strategyFactory)
    {
        mStrategyFactory = strategyFactory;
    }
    
    /**
     * {@inheritDoc}
     */
    public void addProperty(String key, String value)
    {
        // NOOP
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
        LOG.debug("In ProjectPushDownOptimiser#optimise");
        
        List<Operator> candidateOperators;
        
        // TODO: I don't believe I need to iterate until not candidates as
        // push up did.  But I could be wrong.
        
        candidateOperators = OptimiserUtils.findOccurrences(
            lqpRoot, OperatorID.PROJECT);

        if (LOG.isDebugEnabled())
        {
            LOG.debug("Number of PROJECT occurences is: " + 
                      candidateOperators.size());
        }

        for (Operator op : candidateOperators)
        {
            if(op.getAnnotation(IGNORE) == null)
            {
                projectPushDown((ProjectOperator) op);
            }
        }

        // TODO: How can I guarantee the root has not been changed?
        return lqpRoot;
    }
    
    /**
     * Pushes the project operator down as far is it can go.
     * 
     * @param op project operator
     */
    private void projectPushDown(ProjectOperator op)
    {
        Operator projectChild = op.getChild(0);
                
        if (projectChild.getID() != OperatorID.NIL)
        {
            ProjectPushDownStrategy strategy; 
            if (projectChild.getID() == OperatorID.APPLY)
            {
                strategy = mStrategyFactory.getStrategy(
                        ((ApplyOperator)projectChild).getOperator());
            }
            else
            {
                strategy = mStrategyFactory.getStrategy(projectChild);
            }
            
            List<ProjectOperator> pushedDownOperator = strategy.pushDown(op);
            for (ProjectOperator newOp : pushedDownOperator)
            {
                projectPushDown(newOp);
            }
        }
    }
}
