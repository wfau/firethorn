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

package uk.org.ogsadai.dqp.lqp.optimiser.project;

import java.util.List;

import uk.org.ogsadai.dqp.common.CompilerConfiguration;
import uk.org.ogsadai.dqp.common.RequestDetails;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.operators.ProjectOperator;
import uk.org.ogsadai.dqp.lqp.optimiser.Optimiser;
import uk.org.ogsadai.dqp.lqp.optimiser.OptimiserUtils;
import uk.org.ogsadai.resource.dataresource.dqp.RequestDQPFederation;

/**
 * Optimiser trying to pull projects up the query plan tree.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class ProjectPullUpOptimiser implements Optimiser
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /** Strategy factory. */
    private ProjectPullUpStrategyFactory mStrategyFactory;

    /** Skip annotation key - reset on every call to optimise. */
    private String mSkipAnnotationKey;

    /**
     * Constructor.  Creates a PROJECT pull up optimiser that uses the default
     * strategies for pulling PROJECT past each operator.
     */
    public ProjectPullUpOptimiser()
    {
        mStrategyFactory = new ProjectPullUpStrategyFactoryImpl();
    }
    
    /**
     * Constructor.  Creates a PROJECT pull up optimiser that uses uses the 
     * given strategy factory to obtain the strategies for pull PROJECT past 
     * each operator.
     * 
     * @param strategyFactory PROJECT push down strategy factory.
     */
    public ProjectPullUpOptimiser(
        ProjectPullUpStrategyFactory strategyFactory)
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
        // Reset key
        mSkipAnnotationKey =
            ProjectPullUpOptimiser.class.getSimpleName()
                + System.currentTimeMillis();
        
        List<Operator> candidateOperators;
        do
        {
            candidateOperators = OptimiserUtils.findPullUpCandidates(lqpRoot,
                OperatorID.PROJECT, mSkipAnnotationKey);

            for (Operator op : candidateOperators)
            {
                ProjectOperator projectOperator =
                    projectPullUp((ProjectOperator) op);
                projectOperator.addAnnotation(mSkipAnnotationKey, true);
            }
        }
        while (candidateOperators.size() > 0);

        // Remove temporary annotations
        for (Operator op : OptimiserUtils.findOccurrences(lqpRoot,
            OperatorID.PROJECT))
        {
            op.removeAnnotation(mSkipAnnotationKey);
        }

        return lqpRoot;
    }
    
    /**
     * Pull up loop.
     * 
     * @param projectOperator
     *            project operator to be pulled up
     * @return pulled up project operator
     * @throws LQPException
     */
    private ProjectOperator projectPullUp(final ProjectOperator projectOperator)
        throws LQPException
    {
        ProjectOperator currentProjectOperator = projectOperator;
        Operator projectParent;

        while ((projectParent = currentProjectOperator.getParent()).getID() != OperatorID.NIL)
        {
            ProjectPullUpStrategy strategy = mStrategyFactory
                .getStrategy(projectParent);

            ProjectOperator pushedUpOperator = strategy
                .pullUp(currentProjectOperator);
            if (pushedUpOperator == null)
            {
                break;
            }
            currentProjectOperator = pushedUpOperator;
        }

        return currentProjectOperator;
    }
}
