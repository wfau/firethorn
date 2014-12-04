// Copyright (c) The University of Edinburgh, 2008-2009.
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

package uk.org.ogsadai.resource.dataresource.dqp;

import uk.org.ogsadai.activity.dqp.preprocessor.PreProcessor;
import uk.org.ogsadai.authorization.SecurityContext;
import uk.org.ogsadai.common.ID;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.config.Key;
import uk.org.ogsadai.config.KeyValueProperties;
import uk.org.ogsadai.context.OGSADAIContext;
import uk.org.ogsadai.dqp.common.CompilerConfiguration;
import uk.org.ogsadai.dqp.common.EvaluationNode;
import uk.org.ogsadai.dqp.common.RequestDetails;
import uk.org.ogsadai.dqp.execute.workflow.ByteArrayTupleSerialiser;
import uk.org.ogsadai.dqp.execute.workflow.TupleSerialiser;
import uk.org.ogsadai.resource.Resource;
import uk.org.ogsadai.resource.ResourceAccessor;

/**
 * Implements a DQP resource accessor. The security context is being ignored in
 * this implementation.
 *
 * @author The OGSA-DAI Project Team.
 */
public class DQPResourceAccessor implements ResourceAccessor
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";
    
    /** Logger. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(DQPResourceAccessor.class);
    
    /** Key to specify the change use mode, push or pull. */
    private static final Key USE_PUSH_MODEL_KEY = 
        new Key("dqp.exchange.usePushModel");
    
    /** Key to specify the exchange block size. */
    private static final Key BLOCK_SIZE_KEY = 
        new Key("dqp.exchange.blockSize");

    /** Key to specify the number of blocks per message during exchange. */
    private static final Key NUM_BLOCKS_KEY = 
        new Key("dqp.exchange.numBlocks");

    /** Key to specify the poll interval for checking request status. */
    private static final Key POLL_INTERVAL_KEY = 
        new Key("dqp.remote.pollInterval");

    /** Key to specify the tuple serialiser class. */
    private static final Key TUPLE_SERIALISER_KEY = 
        new Key("dqp.serialise.tuple");
    
    /** Key to specify the DQP preprocessor. */
    private static final Key DQP_PREPROCESSOR =
        new Key("dai.dqp.preprocessor");

    /** DQP resource. */
    private final DQPResource mResource;
    
    /** Should be use a push model when exchanging data? */
    private boolean mUsePushModel;
    
    /** Number of blocks to pass in each call when exchanging data. */
    private int mNumBlocks;
    
    /** A class used to serialise tuples. */
    private TupleSerialiser mTupleSerialiser;

    /** Preprocessor. */
    private PreProcessor mPreProcessor=null;
    
    /** 
     * Size of each block in a data exchange, in chars if character data,
     * if bytes if using byte data.
     */
    private int mBlockSize;
    
    /**
     * Poll interval (in milliseconds) between checks of a remote request's
     * execution status.
     */
    private int mPollInterval;
    
    /**
     * Constructs a DQP resource accessor which points back to the resource.
     * 
     * @param parent
     *            resource
     * @param securityContext
     *            security context of the request
     */
    public DQPResourceAccessor(
            DQPResource parent, SecurityContext securityContext)
    {
        mResource = parent;
        
        KeyValueProperties config = parent.getState().getConfiguration();
        
        // Check for preprocessor
        mPreProcessor = null;
        if (config.containsKey(DQP_PREPROCESSOR))
        {
            Object value = config.get(DQP_PREPROCESSOR);
            if (value != null)
            {
                mPreProcessor =
                    (PreProcessor) OGSADAIContext.getInstance().get(
                        new ID(value.toString()));
            }
        }

        // Should we use push or pull exchange model
        mUsePushModel = true;
        if (config.containsKey(USE_PUSH_MODEL_KEY))
        {
            Object value = config.get(USE_PUSH_MODEL_KEY);
            if (value != null)
            {
                if (value.toString().equalsIgnoreCase("false"))
                {
                    mUsePushModel = false;
                }
            }
        }

        // Block size of exchanging data
        mBlockSize = 5000;
        if (config.containsKey(BLOCK_SIZE_KEY))
        {
            Object value = config.get(BLOCK_SIZE_KEY);
            try
            {
                int i = Integer.parseInt(value.toString());
                if (i > 0)
                {
                    mBlockSize = i;
                }
            }
            catch(Throwable t)
            {
                // Ignore this
            }
        }
        
        // Number of blocks per web service call
        mNumBlocks = 100;
        if (config.containsKey(NUM_BLOCKS_KEY))
        {
            Object value = config.get(NUM_BLOCKS_KEY);
            try
            {
                int i = Integer.parseInt(value.toString());
                if (i > 0)
                {
                    mNumBlocks = i;
                }
            }
            catch(Throwable t)
            {
                // Ignore this
            }
        }
        
        // Poll interval
        mPollInterval = 100;
        if (config.containsKey(POLL_INTERVAL_KEY))
        {
            Object value = config.get(POLL_INTERVAL_KEY);
            try
            {
                int i = Integer.parseInt(value.toString());
                if (i > 0)
                {
                    mPollInterval = i;
                }
            }
            catch(Throwable t)
            {
                // Ignore this
            }
        }

        // Tuple serialiser
        mTupleSerialiser = new ByteArrayTupleSerialiser();
        if (config.containsKey(TUPLE_SERIALISER_KEY))
        {
            Object value = config.get(TUPLE_SERIALISER_KEY);
            try
            {
                String className = (String)value;
                Class<? extends TupleSerialiser> c = Class.forName(className).asSubclass(TupleSerialiser.class);
                mTupleSerialiser = c.newInstance();
            }
            catch(Throwable t)
            {
                // Ignore this
                LOG.debug("Error instantiating TupleSerialiser. " 
                        + t.getClass().getName() + ": " + t.getMessage());
            }
        }
        mTupleSerialiser.setBlockSize(mBlockSize);
        
        if (LOG.isDebugEnabled())
        {
            LOG.debug("DQP config: Use push model  : " + mUsePushModel);
            LOG.debug("DQP config: Block size      : " + mBlockSize);
            LOG.debug("DQP config: Num blocks      : " + mNumBlocks);
            LOG.debug("DQP config: Poll interval   : " + mPollInterval);
            LOG.debug("DQP config: Tuple serialiser: " + mTupleSerialiser.getClass().getName());
        }
    }

    /**
     * {@inheritDoc}
     */
    public Resource getResource()
    {
        return mResource;
    }

    /**
     * Gets if a push model should be used when exchanging data.  A push model
     * will use data sink resources.  A pull model will use data source 
     * resources.
     * 
     * @return <tt>true</tt> if a push model is to be used, <tt>false</tt> for
     *         a pull model.
     */
    public boolean getUsePushModel()
    {
        return mUsePushModel;
    }
    
    /**
     * Gets the size of each block when exchanging data.  If data in a character
     * format then this will be the number of characters per block.  If data is
     * in a byte format this will be the number of bytes per block.
     * 
     * @return the exchange block size.
     */
    public int getBlockSize()
    {
        return mBlockSize;
    }
    
    /**
     * Gets the number of blocks in include in each exchange message.
     * 
     * @return number of blocks to include in each exchange message.
     */
    public int getNumBlocks()
    {
        return mNumBlocks;
    }
    
    /**
     * Gets the poll interval (in milliseconds) between checks to a remote
     * server to see the status of a request.
     * 
     * @return poll interval (in milliseconds).
     */
    public int getPollInterval()
    {
        return mPollInterval;
    }

    /**
     * Gets the tuple serialiser. The default serialiser is set to
     * <code>ByteArrayTupleSerialiser</code>. The default serialiser can be
     * changed by setting the <code>dqp.serialise.tuple</code> resource
     * property.
     * 
     * @return a tuple serialiser object
     */
    public TupleSerialiser getTupleSerialiser()
    {
        return mTupleSerialiser;
    }

    /**
     * Gets the details of the DQP federation.
     * 
     * @return details of the DQP federation.
     */
    public DQPFederation getFederation()
    {
        return mResource.getFederation();
    }
    
    /**
     * Returns the local evaluation node.
     * 
     * @return local node
     */
    public EvaluationNode getLocalNode()
    {
        return mResource.getLocalNode();
    }
    
    /**
     * Returns the compiler configuration.
     * 
     * @return compiler configuration
     */
    public CompilerConfiguration getCompilerConfiguration()
    {
        return mResource.getCompilerConfiguration();
    }
    
    /**
     * Returns a query preprocessor if it has been configured.
     * 
     * @return
     *      query preprocessor or <code>null</code> if not set
     */
    public PreProcessor getPreProcessor()
    {
        return mPreProcessor;
    }
    
   /**
     * Reloads the data dictionary.
     * 
     * @param requestDetails
     *           details of the parent request
     */
    public void refreshDataDictionary(RequestDetails requestDetails) 
    {
        mResource.getFederation().refreshDataDictionary(requestDetails);
    }

}
