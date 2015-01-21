// Copyright (c) The University of Edinburgh, 2008-2011.
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

import java.io.File;

import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.config.ConfigurationValueException;
import uk.org.ogsadai.config.Key;
import uk.org.ogsadai.context.OGSADAIConstants;
import uk.org.ogsadai.context.OGSADAIContext;
import uk.org.ogsadai.dqp.common.CompilerConfiguration;
import uk.org.ogsadai.dqp.presentation.common.DQPResourceConfigurationException;
import uk.org.ogsadai.dqp.presentation.common.NoLocalNodeException;
import uk.org.ogsadai.resource.dataresource.DataResourceState;

/**
 * Implements the DQP resource state.
 *
 * @author The OGSA-DAI Project Team.
 */
public class SimpleDQPResourceState implements DQPResourceState
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008-2011";

    /** Logger. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(SimpleDQPResourceState.class);
    
    /** DQP configuration path key. */
    public static final Key CONFIG_PATH = new Key("dqp.config.path");
    
    /** Wrapped data resource state. */
    private final DataResourceState mState;
    /** Details of the DQP federation. */
    private DQPFederation mDQPFederation;
    /** Compiler configuration. */
    private CompilerConfiguration mCompilerConfiguration;

    /**
     * Constructs the resource state. The resource configuration is read from a
     * configuration file and the set of data nodes and evaluation nodes is
     * initialised.
     * 
     * @param state
     *            core resource state
     * @throws DQPResourceConfigurationException
     *             if an error occurred during initialisation
     */
    public SimpleDQPResourceState(DataResourceState state) 
        throws DQPResourceConfigurationException
    {   
        mState = state;
        String contextConfig;
          
        File ogsadaiConfigDir = (File) OGSADAIContext.getInstance().get(
                OGSADAIConstants.CONFIG_DIR
                );
        LOG.debug("ogsadaiConfigDir [" + ogsadaiConfigDir + "]");

        try
        {
            contextConfig = (String)state.getConfiguration().get(CONFIG_PATH);
            LOG.debug("contextConfig [" + contextConfig + "]");
        } 
        catch (ConfigurationValueException e)
        {
            LOG.error(e);
            throw new DQPResourceConfigurationException(e);
        } 
          
        // check whether relative or absolute paths are being used
        if( !(new File(contextConfig)).isAbsolute())
        {
            contextConfig = 
                (new File(ogsadaiConfigDir, contextConfig)).toURI().toASCIIString();
        }
        else
        {
            contextConfig = new File(contextConfig).toURI().toASCIIString();
        }
        LOG.debug("contextConfig [" + contextConfig + "]");
        
        try
        {
            DQPContext context = new DQPContext(contextConfig);
            mDQPFederation = context.getDQPFederation();
            if (mDQPFederation.getLocalNode() == null)
            {
                throw new DQPResourceConfigurationException(
                        new NoLocalNodeException());
            }
            mCompilerConfiguration = context.getCompilerConfiguration();
        }
        catch (DQPResourceConfigurationException e)
        {
            throw e;
        }
        catch (Throwable e)
        {
            throw new DQPResourceConfigurationException(e);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public DataResourceState getState()
    {
        return mState;
    }
    
    /**
     * {@inheritDoc}
     */
    public CompilerConfiguration getCompilerConfiguration()
    {
        return mCompilerConfiguration;
    }

    /**
     * {@inheritDoc}
     */
    public DQPFederation getFederation()
    {
        return mDQPFederation;
    }

}
