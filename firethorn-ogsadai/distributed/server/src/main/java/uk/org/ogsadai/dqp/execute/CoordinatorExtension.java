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

package uk.org.ogsadai.dqp.execute;

import java.util.Set;

import uk.org.ogsadai.dqp.common.RequestDetails;
import uk.org.ogsadai.dqp.execute.partition.Partition;

/**
 * Interface used to provide coordinator extensions. The DQP coordinator
 * processes extensions of type <code>PRE</code> just before query plan
 * partitions are submitted for execution. Extensions of type <code>POST</code>
 * are processed after <b>successful</b> execution of all partitions. If
 * exceptions are thrown during partition execution then the <code>POST</code>
 * extensions will not be processed.
 * 
 * Coordinator extensions are registered in the compiler configuration.
 * 
 * @author The OGSA-DAI Project Team.
 */
public interface CoordinatorExtension
{
    /**
     * Enumeration of extension types.
     * 
     * @author The OGSA-DAI Project Team.
     */
    static enum Type
    {
        /** Pre execution extension. */
        PRE,
        /** Post execution extension. */
        POST
    };
    
    /**
     * Processes partitions.
     * 
     * @param partitions
     *            a set of partitions
     * @param requestDetails
     *            request details
     * @throws ExtensionProcessingException
     */
    void process(Set<Partition> partitions, RequestDetails requestDetails)
        throws ExtensionProcessingException;
    
    /**
     * Returns coordinator extension type.
     * 
     * @return the extension type
     */
    Type getType();
}
