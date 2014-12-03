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

import java.util.List;

import uk.org.ogsadai.dqp.execute.ActivityConstructionException;

/**
 * Interface used to build the final part of pipeline workflows that require
 * some cross pipeline interaction.  Any classes that implement this interface
 * are add added to <tt>PipelineWorkflowBuilder</tt> will be invoked after
 * all the pipelines have been built but before they are executed.  The aim
 * purpose of this interface is to support complex apply functionality where
 * the apply operator alters how its children are built.  If the apply operator
 * and its children are in different partitions the order or building does not
 * guarantee that the child will have been build first.
 *
 * @author The OGSA-DAI Project Team.
 */
public interface CrossPipelineWorkflowBuilder
{
    /**
     * Alters the pipelines build for each partition in a way that requires
     * some cross pipeline interaction.
     * 
     * @param builders list of builders, one for each partition.
     * 
     * @throws ActivityConstructionException 
     *     if there is an error constructing an activity.
     */
    public void build(List<PipelineWorkflowBuilder> builders)
        throws ActivityConstructionException;
}
