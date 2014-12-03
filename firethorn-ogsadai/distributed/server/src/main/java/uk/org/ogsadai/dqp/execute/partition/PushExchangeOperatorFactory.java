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

import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.operators.PushExchangeConsumerOperator;
import uk.org.ogsadai.dqp.lqp.operators.PushExchangeProducerOperator;
import uk.org.ogsadai.resource.dataresource.dqp.DQPResourceAccessor;

/**
 * Operator factory for a push exchange approach using data sink resources.
 *
 * @author The OGSA-DAI Project Team
 */
public class PushExchangeOperatorFactory implements ExchangeOperatorFactory
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009.";

    /** Logger. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(PushExchangeOperatorFactory.class);
    
    /** Resource accessor, used for some configuration parameters. */
    private final DQPResourceAccessor mResourceAccessor;
    
    /**
     * Constructor.
     * 
     * @param resourceAccessor 
     *            DQP resource accessor, used to obtain some configuration
     *            properties.
     */
    public PushExchangeOperatorFactory(DQPResourceAccessor resourceAccessor)
    {
        LOG.debug("Using the PushExchangeOperatorFactory");
        mResourceAccessor = resourceAccessor;
    }
    
    /**
     * {@inheritDoc}
     */
    public Operator createExchangeConsumerOperator(String exchangeId)
    {
        return new PushExchangeConsumerOperator(
                exchangeId,
                mResourceAccessor.getTupleSerialiser());
    }

    /**
     * {@inheritDoc}
     */
    public Operator createExchangeProducerOperator(String exchangeId)
    {
        return new PushExchangeProducerOperator(
            exchangeId,
            mResourceAccessor.getTupleSerialiser(),
            mResourceAccessor.getNumBlocks());
    }
}
