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
package uk.ac.roe.wfau.firethorn.ogsadai.activity.server.ivoa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.roe.wfau.firethorn.ogsadai.activity.common.ivoa.IvoaCreateResourceParam;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.common.jdbc.JdbcCreateResourceParam;
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
import uk.org.ogsadai.authorization.SecurityContext;
import uk.org.ogsadai.resource.ResourceCreationException;
import uk.org.ogsadai.resource.ResourceFactory;
import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.resource.ResourceIDAlreadyAssignedException;
import uk.org.ogsadai.resource.ResourceManager;
import uk.org.ogsadai.resource.ResourceTypeException;
import uk.org.ogsadai.resource.ResourceUnknownException;
import uk.org.ogsadai.resource.dataresource.jdbc.JDBCDataResource;
import uk.org.ogsadai.resource.dataresource.jdbc.JDBCDataResourceState;

/**
 * Activity to create new IVOA resources.
 *
 */
public class IvoaCreateResourceActivity
extends MatchedIterativeActivity
implements ResourceManagerActivity, ResourceFactoryActivity, SecureActivity
    {
    /**
     * Our resource template name.
     *
     */
    protected static final String IVOA_CREATE_TEMPLATE = "uk.ac.roe.wfau.firethorn.IVOA_RESOURCE_TEMPLATE" ;
    
    /**
     * Our debug logger.
     *
     */
    private static Logger logger = LoggerFactory.getLogger(
        IvoaCreateResourceActivity.class
        );
    
    /**
     * Public constructor.
     * 
     * 
     */
    public IvoaCreateResourceActivity()
        {
        super();
        }
    
    @Override
    protected ActivityInput[] getIterationInputs()
        {
        return new ActivityInput[]
             {
             new TypedOptionalActivityInput(
                 IvoaCreateResourceParam.IVOA_SERVICE_URL,
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
            IVOA_CREATE_TEMPLATE
            );        
        
        String ivoaurl  = (String) iterationData[0];

        logger.debug("Resource ["+ uniqueid  +"]");
        logger.debug("Template ["+ template +"]");

        logger.debug("Endpoint ["+ ivoaurl +"]");

        try {
            JDBCDataResource created = (JDBCDataResource) factory.createDataResource(
                template
                );

            JDBCDataResourceState jdbcstate = created.getJDBCDataResourceState();

            jdbcstate.getDataResourceState().setResourceID(
                uniqueid
                );

            if (ivoaurl != null)
                {
                jdbcstate.setDatabaseURL(
                    ivoaurl
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
