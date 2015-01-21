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


package uk.org.ogsadai.resource.dataresource.dqp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.context.OGSADAIConstants;
import uk.org.ogsadai.context.OGSADAIContext;
import uk.org.ogsadai.dqp.lqp.udf.Function;
import uk.org.ogsadai.dqp.lqp.udf.FunctionRepository;
import uk.org.ogsadai.dqp.lqp.udf.repository.SimpleFunctionRepository;

/**
 * Reads the configuration file for the function repository and registers the
 * functions in a simple function repository.
 *
 * @author The OGSA-DAI Project Team.
 */
public class FunctionRepositoryConfig
{
    
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009";
    
    private static final DAILogger LOG =
        DAILogger.getLogger(FunctionRepositoryConfig.class);
    
    /** Configuration file path. */
    private String mConfig;
    
    /**
     * Sets the config file parameter, creates a function repository and 
     * registers it with the OGSA-DAI context.
     * If this class is registered as a resource within the JNDI configuration,
     * this method will be called by the JNDI service.  
     * 
     * @param config configuration file
     */
    public void setConfig(String config)
    {
        mConfig = config;
        OGSADAIContext.getInstance().put(
                FunctionRepository.FUNCTION_REPOSITORY_KEY, 
                createFunctionRepository(config));
    }

    /**
     * Returns the location of the configuration file.
     * 
     * @return configuration file path
     */
    public String getConfig()
    {
        return mConfig;
    }

    /**
     * Constructs a function repository and registers the functions in the
     * configuration file.
     * 
     * @param configFile
     *            path to the config file
     * @return function repository
     */
    public static FunctionRepository createFunctionRepository(String configFile) 
    {        
        SimpleFunctionRepository repository = new SimpleFunctionRepository();
        BufferedReader input = null;
        try
        {


            String fullPath = configFile;
            // check whether config file path is absolute
            if(!(new File(fullPath)).isAbsolute())
            {
                File odConfigDir = (File) OGSADAIContext.getInstance().get(
                    OGSADAIConstants.CONFIG_DIR);


                fullPath = (new File(odConfigDir, configFile)).getCanonicalPath();
            }

            input = new BufferedReader(new FileReader(fullPath));
            String line;
            while ((line = input.readLine()) != null)
            {
                try
                {
                    LOG.debug("Registering function class: " + line);
                    Class<? extends Function> cl = 
                        Class.forName(line).asSubclass(Function.class);
                    repository.register(cl);
                }
                catch (ClassCastException e)
                {
                    LOG.debug("Could not load function class.");
                    LOG.warn(e);

                }
                catch (ClassNotFoundException e)
                {
                    LOG.debug("Could not load function class.");
                    LOG.warn(e);
                }
            }

            input.close();
        }
        catch (IOException e)
        {
            LOG.debug("Problem loading function repository config: " + configFile);
            LOG.warn(e);
        }

        return repository;
    }

}
