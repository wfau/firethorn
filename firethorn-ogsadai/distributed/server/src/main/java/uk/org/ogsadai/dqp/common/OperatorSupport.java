// Copyright (c) The University of Edinburgh, 2010.
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

import uk.org.ogsadai.dqp.lqp.OperatorID;

/**
 * Interface to specify which operators a data node supports.  This is used
 * when imploding the query plan into SQL queries to be sent to the data node.
 * 
 * @author The OGSA-DAI Project Team
 */
public interface OperatorSupport
{
    /**
     * Does this data node support the specified operator.
     * 
     * @param id operator ID
     * 
     * @return <tt>true</tt> if the operator is supported, <tt>false</tt>
     *         otherwise.
     */
    boolean supportsOperator(OperatorID id);
}
