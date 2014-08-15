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

import uk.ac.roe.wfau.firethorn.ogsadai.activity.common.jdbc.JdbcCreateResourceParam;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.server.SimpleProcessingException;
import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.MatchedIterativeActivity;
import uk.org.ogsadai.activity.extension.ResourceFactoryActivity;
import uk.org.ogsadai.activity.extension.ResourceManagerActivity;
import uk.org.ogsadai.activity.extension.SecureActivity;
import uk.org.ogsadai.activity.io.ActivityInput;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.activity.io.TypedOptionalActivityInput;
import uk.org.ogsadai.activity.management.ExtendedCreateRelationalResourceActivity;
import uk.org.ogsadai.authorization.Login;
import uk.org.ogsadai.authorization.LoginDeniedException;
import uk.org.ogsadai.authorization.LoginProvider;
import uk.org.ogsadai.authorization.LoginProviderException;
import uk.org.ogsadai.authorization.ManageableLoginProvider;
import uk.org.ogsadai.authorization.SecurityContext;
import uk.org.ogsadai.resource.ResourceCreationException;
import uk.org.ogsadai.resource.ResourceFactory;
import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.resource.ResourceIDAlreadyAssignedException;
import uk.org.ogsadai.resource.ResourceManager;
import uk.org.ogsadai.resource.ResourceState;
import uk.org.ogsadai.resource.ResourceTypeException;
import uk.org.ogsadai.resource.ResourceUnknownException;
import uk.org.ogsadai.resource.dataresource.jdbc.JDBCDataResource;
import uk.org.ogsadai.resource.dataresource.jdbc.JDBCDataResourceState;

/**
 * Activity to create new JDBC resources.
 * Based on the original {@link ExtendedCreateRelationalResourceActivity} in the OGSA-DAI source code.  
 *
 */
public class JdbcCreateResourceActivity
extends MatchedIterativeActivity
implements ResourceManagerActivity, ResourceFactoryActivity, SecureActivity
    {
    /**
     * Our resource template name.
     *
     */
    protected static final String JDBC_RESOURCE_TEMPLATE = "uk.ac.roe.wfau.firethorn.JDBC_RESOURCE_TEMPLATE" ;
    
    /**
     * Our debug logger.
     *
     */
    private static Logger logger = LoggerFactory.getLogger(
        JdbcCreateResourceActivity.class
        );
    
    /**
     * Public constructor.
     * 
     * 
     */
    public JdbcCreateResourceActivity()
        {
        super();
        }
    
    @Override
    protected ActivityInput[] getIterationInputs()
        {
        return new ActivityInput[]
             {
             new TypedOptionalActivityInput(
                 JdbcCreateResourceParam.DATABASE_URL,
                 String.class
                 ),
             new TypedOptionalActivityInput(
                 JdbcCreateResourceParam.DATABASE_USERNAME,
                 String.class
                 ),
             new TypedOptionalActivityInput(
                 JdbcCreateResourceParam.DATABASE_PASSWORD,
                 String.class
                 ),
             new TypedOptionalActivityInput(
                 JdbcCreateResourceParam.DATABASE_DRIVER,
                 String.class
                 ),
             };
        }

    /**
     * Our results writer.
     * 
     */
    private BlockWriter writer;

    @Override
    protected void preprocess()
        throws ActivityUserException, ActivityProcessingException, ActivityTerminatedException
        {
        validateOutput(
            JdbcCreateResourceParam.ACTIVITY_RESULTS
            );
        this.writer = this.getOutput(
            JdbcCreateResourceParam.ACTIVITY_RESULTS
            );
        }

    @Override
    protected void postprocess()
        throws ActivityUserException, ActivityProcessingException,
        ActivityTerminatedException
        {
        }

    /**
     * The Security context for the current request.
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
        ResourceID uniqueid = manager.createUniqueID();
        ResourceID template = new ResourceID(
            JDBC_RESOURCE_TEMPLATE
            );        
        
        String jdbcurl  = (String) iterationData[0];
        String username = (String) iterationData[1];
        String password = (String) iterationData[2];
        String driver   = (String) iterationData[3];

        logger.debug("Resource ["+ uniqueid  +"]");
        logger.debug("Template ["+ template +"]");

        logger.debug("Database ["+ jdbcurl +"]");
        logger.debug("Username ["+ username +"]");
        logger.debug("Password ["+ password +"]");
        logger.debug("Driver   ["+ driver +"]");

        try {
            JDBCDataResource created = (JDBCDataResource) factory.createDataResource(
                template
                );
            final ResourceState state = created.getState() ;
            state.setResourceID(
                uniqueid
                );
            final JDBCDataResourceState jdbcstate = created.getJDBCDataResourceState();
            jdbcstate.getDataResourceState().setResourceID(
                uniqueid
                );
            //
            // Bug fix for uninitialised mResourceID in JDBCDataResource.
            // mResourceID = JDBC_RESOURCE_TEMPLATE 
            created.initialize(
                created.getState()
                );
            
            if (jdbcurl != null)
                {
                jdbcstate.setDatabaseURL(
                    jdbcurl
                    );
                }

            if (driver != null)
                {
                jdbcstate.setDriverClass(
                    driver
                    );
                }
            
            final LoginProvider provider = jdbcstate.getLoginProvider();
            Login login = null;

            if (username != null)
                {
                if (password == null)
                    {
                    password = "";
                    }
                login = new Login(
                    username,
                    password
                    );
                }
            else {
                try {
                    login = provider.getLogin(
                        template,
                        context
                        );
                    }
                catch (LoginDeniedException ouch)
                    {
                    throw new ActivityProcessingException(
                        ouch
                        );
                    }
                catch (LoginProviderException ouch)
                    {
                    throw new ActivityProcessingException(
                        ouch
                        );
                    }
                }

            if (provider instanceof ManageableLoginProvider)
                {
                ((ManageableLoginProvider) provider).permitLogin(
                    uniqueid, 
                    context, 
                    login
                    );
                }
            else {
                throw new ActivityProcessingException(
                    new SimpleProcessingException(
                        "LoginProvider does not implement ManageableLoginProvider"
                        )
                    );
                }

            logger.debug("Adding Resource to Factory [" + uniqueid + "][" + created.getResourceID().toString() + "]");
            factory.addResource(
                uniqueid,
                created
                );
            logger.debug("Writing ResourceID to results [" + uniqueid + "]");
            writer.write(
                uniqueid
                );
            }
        catch (ResourceCreationException ouch)
            {
            logger.warn("ResourceCreationExceptioncreating JDBCDataResource [" + ouch.getMessage() + "]");
            throw new ActivityProcessingException(
                ouch
                );
            }
        catch (ResourceTypeException ouch)
            {
            logger.warn("ResourceTypeExceptioncreating JDBCDataResource [" + ouch.getMessage() + "]");
            throw new ActivityProcessingException(
                ouch
                );
            }
        catch (ResourceUnknownException ouch)
            {
            logger.warn("Unable to locate JDBCDataResource template [" + ouch.getMessage() + "]");
            throw new ActivityProcessingException(
                ouch
                );
            }
        catch (ResourceIDAlreadyAssignedException ouch)
            {
            logger.warn("Resource ID already assigned [" + uniqueid + "][" + ouch.getMessage() + "]");
            throw new ActivityProcessingException(
                ouch
                );
            }
        catch (PipeClosedException ouch)
            {
            logger.warn("PipeClosedException [" + ouch.getMessage() + "]");
            throw new ActivityProcessingException(
                ouch
                );
            }
        catch (PipeIOException ouch)
            {
            logger.warn("PipeIOException [" + ouch.getMessage() + "]");
            throw new ActivityProcessingException(
                ouch
                );
            }
        catch (PipeTerminatedException ouch)
            {
            logger.warn("PipeTerminatedException [" + ouch.getMessage() + "]");
            throw new ActivityProcessingException(
                ouch
                );
            }
        }
    }
