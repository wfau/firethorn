// Copyright (c) The University of Edinburgh,  2007-2009.
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

package uk.org.ogsadai.activity.dqp;

import java.util.Set;

import uk.org.ogsadai.activity.ActivityBase;
import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.extension.RequestActivity;
import uk.org.ogsadai.activity.extension.ResourceActivity;
import uk.org.ogsadai.activity.extension.SecureActivity;
import uk.org.ogsadai.activity.io.ActivityPipeProcessingException;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.authorization.SecurityContext;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.dqp.common.DataDictionary;
import uk.org.ogsadai.dqp.common.RequestDetails;
import uk.org.ogsadai.dqp.common.TableSchema;
import uk.org.ogsadai.dqp.common.simple.SimpleRequestDetails;
import uk.org.ogsadai.resource.ResourceAccessor;
import uk.org.ogsadai.resource.dataresource.dqp.DQPResourceAccessor;
import uk.org.ogsadai.resource.request.RequestResource;

/**
 * An activity that retrieves the names of available tables from the DQP 
 * federation indicated by the resource.
 * <p>
 * Activity inputs: none.
 * </p>
 * <p>
 * Activity outputs:
 * </p>
 * <ul>
 * <li>
 * <code>data</code>. Type: OGSA-DAI list of {@link java.lang.String}. 
 * The names of the available tables. 
 * </li>
 * </ul>
  * <p>
 * Configuration parameters: none.
 * </p>
 * <p>
 * Activity input/output ordering: none.
 * </p>
 * <p>
 * Activity contracts: none.
 * </p>
 * <p>
 * Target data resource:
 * </p>
 * <ul>
 * <li>
 * {@link uk.org.ogsadai.resource.dataresource.dqp.DQPResourceAccessor}. 
 * </li>
 * </ul>
 * <p>
 * Behaviour: 
 * </p>
 * <ul>
 * <li>
 * The activity queries the target data resource for meta-data in order to
 * retrieve the names of all available tables within the DQP federation. 
 * </li>
 * <li>
 * If there are no available tables, an empty OGSA-DAI list will be returned.
 * </li>
 * </ul>
 *
 * @author The OGSA-DAI Project Team.
 */
public class GetAvailableTablesActivity extends ActivityBase
    implements ResourceActivity,
               SecureActivity,
               RequestActivity
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2007-2011.";
    
    /** Logger. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(GetAvailableTablesActivity.class);

    /** 
     * Activity output name <code>data</code> - the available tables
     * (OGSA-DAI list of {@link java.lang.String}). 
     */
    public static final String OUTPUT_DATA = "data";
   
    /** DQP resource accessor. */
    private DQPResourceAccessor mResource;
    
    /** Security context. */
    private SecurityContext mSecurityContext;

    /** Request resource. */
    private RequestResource mRequestResource;
    
    /**
     * {@inheritDoc}
     */
    public void process()
        throws ActivityUserException, 
               ActivityProcessingException, 
               ActivityTerminatedException
    {
        validateOutput(OUTPUT_DATA);
        BlockWriter output = getOutput(OUTPUT_DATA);  
        RequestDetails requestDetails = 
            new SimpleRequestDetails(
                mResource.getResource().getResourceID(),
                mRequestResource,
                mSecurityContext,
                getActivityDescriptor().getInstanceName());
        DataDictionary dataDictionary = 
            mResource.getFederation().getDataDictionary(requestDetails);
        Set<TableSchema> schemas = dataDictionary.getTableSchemas();

        try
        {
            output.write(ControlBlock.LIST_BEGIN);
            for (TableSchema schema : schemas)
            {
                output.write(schema.getTableName());
            }
            output.write(ControlBlock.LIST_END);
        }
        catch (PipeClosedException e)
        {
            // no more output wanted, so stop processing
        }
        catch (PipeIOException e)
        {
            throw new ActivityPipeProcessingException(e);
        }
        catch (PipeTerminatedException e)
        {
            throw new ActivityTerminatedException();
        }
    }
    
    @Override
    public Class<? extends ResourceAccessor> getTargetResourceAccessorClass()
    {
        return DQPResourceAccessor.class;
    }

    @Override
    public void setTargetResourceAccessor(final ResourceAccessor targetResource)
    {
        mResource = (DQPResourceAccessor) targetResource;
    }
    
    @Override
    public void setSecurityContext(SecurityContext securityContext)
    {
        mSecurityContext = securityContext;
    }

    @Override
    public void setRequest(RequestResource requestResource)
    {
        mRequestResource = requestResource;
    }

    
}
