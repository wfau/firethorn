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

package uk.org.ogsadai.dqp.lqp;

import java.util.List;

import uk.org.ogsadai.dqp.common.DataDictionary;
import uk.org.ogsadai.dqp.common.DataNode;

/**
 * Interface that needs to implemented by tuple producing SCAN operators.
 * 
 * @author The OGSA-DAI Project Team.
 */
public interface ScanOperator extends Operator
{
    /**
     * Sets data dictionary.
     * 
     * @param dataDictionary
     *            data dictionary
     */
    public void setDataDictionary(DataDictionary dataDictionary);

    /**
     * Returns the data node of a scan operator. If a
     * <code>data.node->DataNode</code> annotation is present - a matching data
     * node will be returned. If <code>evaluation.node->EvaluationNode</code>
     * annotation is present - a first matching data node will be returned. If
     * none of the above is defined the first data node from the call to
     * getDataNodes() will be returned.
     * 
     * @return data node
     */
    public DataNode getDataNode();

    /**
     * Returns a list of data nodes on which a scan can be executed. If there is
     * more than one data node in the list it indicates that data replicas
     * exist.
     * 
     * @return list of data nodes
     */
    public List<DataNode> getDataNodes();
}
