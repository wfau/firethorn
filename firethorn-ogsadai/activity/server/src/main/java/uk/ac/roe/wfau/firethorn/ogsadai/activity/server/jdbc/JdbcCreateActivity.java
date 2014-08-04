/*
 *  Copyright (C) 2014 Royal Observatory, University of Edinburgh, UK
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package uk.ac.roe.wfau.firethorn.ogsadai.activity.server.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.roe.wfau.firethorn.ogsadai.activity.jdbc.JdbcCreateParam;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.server.DelaysActivity;
import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.MatchedIterativeActivity;
import uk.org.ogsadai.activity.extension.ResourceFactoryActivity;
import uk.org.ogsadai.activity.extension.ResourceManagerActivity;
import uk.org.ogsadai.activity.extension.SecureActivity;
import uk.org.ogsadai.activity.io.ActivityInput;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.TypedOptionalActivityInput;
import uk.org.ogsadai.authorization.SecurityContext;
import uk.org.ogsadai.resource.ResourceFactory;
import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.resource.ResourceManager;

/**
 * OGSA-DAI Activity to create new JDBC resources. 
 *
 */
public class JdbcCreateActivity
extends MatchedIterativeActivity
implements ResourceManagerActivity, ResourceFactoryActivity, SecureActivity
    {

    /**
     * Our debug logger.
     *
     */
    private static Logger logger = LoggerFactory.getLogger(
        DelaysActivity.class
        );
    
    /**
     * Public constructor.
     * 
     * 
     */
    public JdbcCreateActivity()
        {
        super();
        }
    
    @Override
    protected ActivityInput[] getIterationInputs()
        {
        return new ActivityInput[]
             {
             new TypedOptionalActivityInput(
                 JdbcCreateParam.JDBC_DATABASE_URL,
                 String.class
                 ),
             new TypedOptionalActivityInput(
                 JdbcCreateParam.JDBC_DATABASE_USERNAME,
                 String.class
                 ),
             new TypedOptionalActivityInput(
                 JdbcCreateParam.JDBC_DATABASE_PASSWORD,
                 String.class
                 ),
             new TypedOptionalActivityInput(
                 JdbcCreateParam.JDBC_DATABASE_DRIVER,
                 String.class
                 ),
             };
        }

    /**
     * A block writer for our result.
     * 
     */
    private BlockWriter result;

    @Override
    protected void preprocess()
        throws ActivityUserException, ActivityProcessingException, ActivityTerminatedException
        {
        validateOutput(
            JdbcCreateParam.JDBC_CREATE_RESULT
            );
        this.result = getOutput();
        }

    @Override
    protected void postprocess()
        throws ActivityUserException, ActivityProcessingException,
        ActivityTerminatedException
        {
        }

    /**
     * The Security context for current request.
     *  
     */
    private SecurityContext context;

    @Override
    public void setSecurityContext(SecurityContext context)
        {
        this.context = context ;
        }
    /**
     * Our OGSA-DAI resource factory.
     * 
     */
    private ResourceFactory factory;

    @Override
    public void setResourceFactory(ResourceFactory factory)
        {
        this.factory = factory;
        }

    /**
     * Our OGSA-DAI resource manager.
     * 
     */
    private ResourceManager manager;

    @Override
    public void setResourceManager(ResourceManager manager)
        {
        this.manager = manager ;
        }

    @Override
    protected void processIteration(Object[] iterationData)
    throws ActivityProcessingException, ActivityTerminatedException, ActivityUserException
        {
        ResourceID resource = manager.createUniqueID();
        ResourceID template = new ResourceID(
            "uk.org.ogsadai.JDBC_RESOURCE_TEMPLATE"
            );        
        
        String url = null;
        if (iterationData[0] != null)
            {
            url = (String)iterationData[3];
            }

        String username = null;
        if (iterationData[1] != null)
            {
            username = (String)iterationData[4];
            }

        String password = null;
        if (iterationData[2] != null)
            {
            password = (String)iterationData[5];
            }
        
        String driver = null;
        if (iterationData[3] != null)
            {
            driver = (String)iterationData[2];
            }

        logger.debug("Resource ID: " + resourceID);
        logger.debug("Template ID: " + templateID);
        logger.debug("Database driver: " + dbDriver);
        logger.debug("Database URL: " + dbURL);
                LOG.debug("Database username: " + dbUser);
                LOG.debug("Database password: " + dbPassword);
            }

// do stuff ....

        }
    }
