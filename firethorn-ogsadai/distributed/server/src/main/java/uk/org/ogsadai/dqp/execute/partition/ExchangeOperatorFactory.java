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

package uk.org.ogsadai.dqp.execute.partition;

import uk.org.ogsadai.dqp.lqp.Operator;

/**
 * Factory that produces exchange operators.  Used to group push and pull
 * exchange operators together.
 *
 * @author The OGSA-DAI Project Team
 */
public interface ExchangeOperatorFactory
{
    /**
     * Creates a exchange consumer operator.
     * 
     * @param exchangeId
     *           exchange ID, typically the data source or data sink resource
     *           ID.
     *           
     * @return newly created exchange consumer operator
     */
    Operator createExchangeConsumerOperator(String exchangeId);
    
    /**
     * Creates a exchange producer operator.
     * 
     * @param exchangeId
     *           exchange ID, typically the data source or data sink resource
     *           ID.
     *           
     * @return newly created exchange producer operator
     */
    Operator createExchangeProducerOperator(String exchangeId);
}

