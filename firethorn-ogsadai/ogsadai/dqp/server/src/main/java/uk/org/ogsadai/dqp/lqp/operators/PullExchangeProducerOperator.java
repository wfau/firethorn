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

package uk.org.ogsadai.dqp.lqp.operators;

import uk.org.ogsadai.dqp.execute.workflow.TupleSerialiser;
import uk.org.ogsadai.dqp.lqp.HeadingImpl;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.OperatorVisitor;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;

/**
 * Exchanges data between OGSA-DAI services.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class PullExchangeProducerOperator extends UnaryOperator
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE =
        "Copyright (c) The University of Edinburgh, 2008";

    /** Data source name for exchanging data. */
    private final String mDataSource;

    /** Size of each block is chars or bytes (serialisation dependent). **/
    private final int mBlockSize = 100;
    
    /** Tuple serialiser. */
    private final TupleSerialiser mTupleSerialiser;

    /**
     * Constructor.
     * 
     * @param dataSourceID
     *          ID of data source resource
     * @param tupleSerialiser
     *           tuple serialiser
     */
    public PullExchangeProducerOperator(String dataSourceID,
            TupleSerialiser tupleSerialiser)    
    {
        mID = OperatorID.PULL_EXCHANGE_PRODUCER;
        mDataSource = dataSourceID;
        mTupleSerialiser = tupleSerialiser;
    }
    
    /**
     * Returns the ID of the data source.
     * 
     * @return data source ID
     */
    public String getDataSourceID()
    {
        return mDataSource;
    }

    /**
     * Gets the blocks size to use when exchanging data.
     * 
     * @return block size to use, if either characters or bytes depending on
     *         the chosen serialisation.
     */
    public int getBlockSize()
    {
        return mBlockSize;
    }
    
    /**
     * Gets the tuple serialiser.
     * 
     * @return tuple serialiser
     */
    public TupleSerialiser getTupleSerialiser()
    {
        return mTupleSerialiser;
    }
   
    /**
     * {@inheritDoc}
     */
    public void update() throws LQPException
    {
        super.update();
        
        mOperatorHeading = new HeadingImpl(mChildOperator.getHeading());
    }
    
    /**
     * {@inheritDoc}
     */
    public void accept(OperatorVisitor visitor)
    {
        visitor.visit(this);
    }

}
