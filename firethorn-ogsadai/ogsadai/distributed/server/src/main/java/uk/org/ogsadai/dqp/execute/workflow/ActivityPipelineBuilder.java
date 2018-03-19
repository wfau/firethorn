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


package uk.org.ogsadai.dqp.execute.workflow;

import java.util.List;

import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.dqp.execute.ActivityConstructionException;
import uk.org.ogsadai.dqp.lqp.Operator;

/**
 * Builds activities corresponding to an LQP operator.
 * 
 * @author The OGSA-DAI Project Team.
 */
public interface ActivityPipelineBuilder
{
    /**
     * Build activities and add them to a pipeline.
     * 
     * @param operator
     *            LQP operator for which to build activities
     * @param outputs
     *            outputs from the children of the operator which have already
     *            been built
     * @param builder
     *            pipeline workflow builder
     * @return the unconnected activity output
     * @throws ActivityConstructionException
     */
    public SingleActivityOutput build(
            Operator operator, 
            List<SingleActivityOutput> outputs,
            PipelineWorkflowBuilder builder) 
        throws ActivityConstructionException;
}
