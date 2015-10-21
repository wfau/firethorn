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
import uk.org.ogsadai.config.Key;
import uk.org.ogsadai.config.KeyValueProperties;
import uk.org.ogsadai.resource.ResourceCreationException;
import uk.org.ogsadai.resource.ResourceFactory;
import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.resource.ResourceIDAlreadyAssignedException;
import uk.org.ogsadai.resource.ResourceManager;
import uk.org.ogsadai.resource.ResourceState;
import uk.org.ogsadai.resource.ResourceTypeException;
import uk.org.ogsadai.resource.ResourceUnknownException;
import uk.org.ogsadai.resource.dataresource.DataResource;
import uk.org.ogsadai.resource.dataresource.DataResourceState;
import uk.org.ogsadai.resource.dataresource.jdbc.JDBCDataResource;
import uk.org.ogsadai.resource.dataresource.jdbc.JDBCDataResourceState;

/**
 * Activity to create new IVOA resources.
 *
 */
public class IvoaCreateResourceActivity
extends MatchedIterativeActivity
implements ResourceManagerActivity, ResourceFactoryActivity
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
                 IvoaCreateResourceParam.IVOA_TAP_ENDPOINT,
                 String.class
                 ),
             new TypedOptionalActivityInput(
                 IvoaCreateResourceParam.IVOA_UWS_QUICKSTART,
                 Boolean.class
                 ),
             new TypedOptionalActivityInput(
                 IvoaCreateResourceParam.IVOA_UWS_INTERVAL,
                 Integer.class
                 ),
             new TypedOptionalActivityInput(
                 IvoaCreateResourceParam.IVOA_UWS_TIMEOUT,
                 Integer.class
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
            IvoaCreateResourceParam.ACTIVITY_RESULT
            );
        this.writer = this.getOutput(
            IvoaCreateResourceParam.ACTIVITY_RESULT
            );
        }

    @Override
    protected void postprocess()
    throws ActivityUserException, ActivityProcessingException,ActivityTerminatedException
        {
        }

    /**
     * Our OGSA-DAI resource factory.
     * 
     */
    private ResourceFactory factory;
    @Override
    public void setResourceFactory(final ResourceFactory factory)
        {
        this.factory = factory;
        }

    /**
     * Our OGSA-DAI resource manager.
     * 
     */
    private ResourceManager manager;
    @Override
    public void setResourceManager(final ResourceManager manager)
        {
        this.manager = manager ;
        }

    @Override
    protected void processIteration(final Object[] iterationData)
    throws ActivityProcessingException, ActivityTerminatedException, ActivityUserException
        {
        final ResourceID uniqueid = manager.createUniqueID();
        final ResourceID template = new ResourceID(
            IVOA_CREATE_TEMPLATE
            );        
        
        final String  endpoint   = (String)  iterationData[0];
        final Boolean quickstart = (Boolean) iterationData[1];
        final Integer interval   = (Integer) iterationData[2];
        final Integer timeout    = (Integer) iterationData[3];
        
        logger.debug("Resource ["+ uniqueid +"]");
        logger.debug("Template ["+ template +"]");
        logger.debug("Endpoint ["+ endpoint +"]");
        logger.debug("Quick    ["+ quickstart +"]");
        logger.debug("Interval ["+ interval +"]");
        logger.debug("Timeout  ["+ timeout  +"]");

        try {
            final DataResource created = factory.createDataResource(
                template
                );
            final ResourceState state = created.getState() ;
            state.setResourceID(
                uniqueid
                );
            //
            // Avoid similar bug in JDBCResource
            // mResourceID = JDBC_RESOURCE_TEMPLATE 
            created.initialize(
                state
                );
            
            final KeyValueProperties properties = state.getConfiguration();
            if (endpoint != null)
                {
                properties.put(
                    IvoaResourceKeys.IVOA_TAP_ENDPOINT,
                    endpoint
                    );
                }

            if (quickstart != null)
                {
                properties.put(
                    IvoaResourceKeys.IVOA_UWS_QUICKSTART,
                    quickstart
                    );
                }

            if (interval != null)
                {
                properties.put(
                    IvoaResourceKeys.IVOA_UWS_INTERVAL,
                    interval
                    );
                }
            
            if (timeout != null)
                {
                properties.put(
                    IvoaResourceKeys.IVOA_UWS_TIMEOUT,
                    timeout
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
            logger.warn("ResourceCreationExceptioncreating IvoaDataResource [" + ouch.getMessage() + "]");
            throw new ActivityProcessingException(
                ouch
                );
            }
        catch (ResourceTypeException ouch)
            {
            logger.warn("ResourceTypeExceptioncreating IvoaDataResource [" + ouch.getMessage() + "]");
            throw new ActivityProcessingException(
                ouch
                );
            }
        catch (ResourceUnknownException ouch)
            {
            logger.warn("Unable to locate IvoaDataResource template [" + ouch.getMessage() + "]");
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
