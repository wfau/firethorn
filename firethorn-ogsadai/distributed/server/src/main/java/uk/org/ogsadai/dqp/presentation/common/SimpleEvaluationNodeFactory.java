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

package uk.org.ogsadai.dqp.presentation.common;

import java.net.MalformedURLException;

import uk.org.ogsadai.dqp.common.EvaluationNode;
import uk.org.ogsadai.resource.ResourceState;

/**
 * A simple implementation of evaluation node factory that creates evaluation
 * nodes to which no security policies are applied.
 * <p>
 * This <code>EvaluationNode</code> objects returned use the OGSA-DAI client
 * toolkit ServerProxy that requires an extra request to determine the 
 * presentation layer of the server.  It is probably better to use a 
 * presentation layer specific evaluation node factory class.
 *
 * @author The OGSA-DAI Project Team
 */
public class SimpleEvaluationNodeFactory implements EvaluationNodeFactory
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009.";

    /**
     * {@inheritDoc}
     */
    public void configure(ResourceState resourceState)
        throws DQPResourceConfigurationException
    {
        // Empty method
    }
    
    /**
     * {@inheritDoc}
     */
    public EvaluationNode createEvaluationNode(
        String url, String drerID, String dsos, String dsis, boolean isLocal)
        throws MalformedURLException
    {
        return new SimpleEvaluationNode(
            url, 
            drerID, 
            dsos, 
            dsis, 
            isLocal, 
            new SimpleDQPWorkflowTransformer());
    }
}
