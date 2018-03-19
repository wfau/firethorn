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


package uk.org.ogsadai.dqp.presentation.common;

import java.util.Set;

import uk.org.ogsadai.dqp.common.DataNode;
import uk.org.ogsadai.dqp.common.EvaluationNode;

/**
 * DQP resource configuration.
 *
 * @author The OGSA-DAI Project Team.
 */
public interface DQPResourceConfiguration
{
    
    /**
     * Returns the set of evaluation nodes from the configuration document. The
     * method <code>readConfiguration</code> must be called before this method,
     * otherwise it will return <code>null</code>.
     * 
     * @return evaluation nodes
     */
    public Set<EvaluationNode> getEvaluationNodes();
    
    /**
     * Returns the set of data nodes in the configuration document.
     * 
     * @return data nodes
     */
    public Set<DataNode> getDataNodes();

    /**
     * Returns the local evaluation node of this DQP resource.
     * 
     * @return local node
     */
    public EvaluationNode getLocalNode();

    /**
     * Returns the table schema fetcher.
     * 
     * @return table schema fetcher
     */
    public TableSchemaFetcher getTableSchemaFetcher();
    

}
