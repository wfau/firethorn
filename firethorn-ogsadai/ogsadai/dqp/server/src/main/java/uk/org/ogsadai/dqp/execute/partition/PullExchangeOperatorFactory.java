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
import uk.org.ogsadai.dqp.lqp.operators.PullExchangeConsumerOperator;
import uk.org.ogsadai.dqp.lqp.operators.PullExchangeProducerOperator;
import uk.org.ogsadai.resource.dataresource.dqp.DQPResourceAccessor;

/**
 * Operator factory for a pull exchange approach using data source resources.
 *
 * @author The OGSA-DAI Project Team
 */
public class PullExchangeOperatorFactory implements ExchangeOperatorFactory
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009.";

    /** Logger. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(PushExchangeOperatorFactory.class);

    /** DQP resource accessor. */
    private final DQPResourceAccessor mResourceAccessor;
    
    /**
     * Constructor.
     * 
     * @param resourceAccessor 
     *            DQP resource accessor, used to obtain some configuration
     *            properties.
     */
    public PullExchangeOperatorFactory(DQPResourceAccessor resourceAccessor)
    {
        LOG.debug("Using the PullExchangeOperatorFactory");
        mResourceAccessor = resourceAccessor;
    }
    
    /**
     * {@inheritDoc}
     */
    public Operator createExchangeConsumerOperator(String exchangeId)
    {
        return new PullExchangeConsumerOperator(
            exchangeId, 
            mResourceAccessor.getNumBlocks(), 
            mResourceAccessor.getTupleSerialiser());
    }

    /**
     * {@inheritDoc}
     */
    public Operator createExchangeProducerOperator(String exchangeId)
    {
        return new PullExchangeProducerOperator(
            exchangeId, mResourceAccessor.getTupleSerialiser());
    }
}
