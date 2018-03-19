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

package uk.org.ogsadai.dqp.lqp.optimiser.project.groupby;

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
 * Inserts PROJECT operators after GROUP BY operators so the PROJECTS can 
 * be pushed down as far as they can go.
 *
 * @author The OGSA-DAI Project Team
 */
public class InsertProjectAfterGroupByOptimiser implements Optimiser
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009.";

    /**
     * {@inheritDoc}
     */
    public Operator optimise(
        Operator lqpRoot, 
        RequestDQPFederation requestFederation,
        CompilerConfiguration compilerConfiguration, 
        RequestDetails requestDetails) throws LQPException
    {
        processOperators(
            OptimiserUtils.findOccurrences(lqpRoot, OperatorID.GROUP_BY));
        
        processOperators(
            OptimiserUtils.findOccurrences(
                lqpRoot, OperatorID.SCALAR_GROUP_BY));
        
        return lqpRoot;
    }
    
    /**
     * Adds a new PROJECT operator after each of the given operators.
     *
     * @param groupByOperators  operators to add a PROJECT after.
     */
    private void processOperators(List<Operator> groupByOperators)
    {
        for (Operator op : groupByOperators)
        {
            Operator child = op.getChild(0);
            
            try
            {
                ProjectOperator project = new ProjectOperator(
                    child, op.getUsedAttributes());
                op.replaceChild(child, project);
                child.setParent(project);
                op.update();
            }
            catch (LQPException e)
            {
                // Should not occur
            }
        }
    }
}
