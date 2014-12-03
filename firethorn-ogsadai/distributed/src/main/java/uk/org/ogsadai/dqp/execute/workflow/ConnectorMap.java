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

package uk.org.ogsadai.dqp.execute.workflow;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import uk.org.ogsadai.client.toolkit.activities.generic.TraversableSingleActivityInput;
import uk.org.ogsadai.client.toolkit.activities.generic.TraversableSingleActivityOutput;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.resource.dataresource.dqp.DQPResourceAccessor;

/**
 * Maps pairs of operators to PipelineConnectors.
 *
 * @author The OGSA-DAI Project Team.
 */
public class ConnectorMap
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009";

    /** Connectors. */
    private final Map<List<Operator>, PipelineConnector> mConnectors =
        new ConcurrentHashMap<List<Operator>, PipelineConnector>();

    /** DQP resource accessor, used to get configuration parameters. */
    private final DQPResourceAccessor mResourceAccessor;

    /**
     * Constructor.
     * 
     * @param resourceAccessor
     *           DQP resource access that provides access to configuration
     *           parameters.
     */
    public ConnectorMap(DQPResourceAccessor resourceAccessor)
    {
        mResourceAccessor = resourceAccessor;
    }
    
    
    /**
     * Adds the given input to the connection between the operators.
     * 
     * @param source
     *            source operator
     * @param target
     *            target operator
     * @param input
     *            activity input
     * @param builder
     *            pipeline workflow builder
     */
    public void addInput(
            Operator source, 
            Operator target, 
            TraversableSingleActivityInput input, 
            PipelineWorkflowBuilder builder)
    {
        List<Operator> key = Arrays.asList(source, target);
        PipelineConnector connector = mConnectors.get(key);
        if (connector == null)
        {
            connector = new PipelineConnector(mResourceAccessor);
            mConnectors.put(key, connector);
        }
        connector.setInput(input, builder);
    }
    
    /**
     * Adds the given output to the connection between the operators.
     * 
     * @param source
     *            source operator
     * @param target
     *            target operator
     * @param output
     *            activity output
     * @param builder
     *            pipeline workflow builder
     */
    public void addOutput(
            Operator source, 
            Operator target, 
            TraversableSingleActivityOutput output,
            PipelineWorkflowBuilder builder)
    {
        List<Operator> key = Arrays.asList(source, target);
        PipelineConnector connector = mConnectors.get(key);
        if (connector == null)
        {
            connector = new PipelineConnector(mResourceAccessor);
            mConnectors.put(key, connector);
        }
        connector.setOutput(output, builder);
    }
}
