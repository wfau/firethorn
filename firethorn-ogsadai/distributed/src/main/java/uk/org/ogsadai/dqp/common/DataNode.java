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

package uk.org.ogsadai.dqp.common;

import uk.org.ogsadai.resource.ResourceID;

/**
 * A node that contains data to be included in the federation.
 *
 * @author The OGSA-DAI Project Team.
 */
public interface DataNode 
    extends OperatorSupport, ArithmeticExpressionSupport, ExpressionSupport
{
    /**
     * Gets the corresponding evaluation node.
     * 
     * @return evaluation node
     */
    EvaluationNode getEvaluationNode();

    /**
     * Gets the prefix that will be added to each table in this data node to
     * form the table's name in the federation.  Typically this will be 
     * something like <tt>"MyResourceID_"</tt>.
     * 
     * @return the prefix to be added to each table name.
     */
    String getTableNamePrefix();
    
    /**
     * Gets the resource ID of this resource.
     * 
     * @return resource ID
     */
    ResourceID getResourceID();

}
