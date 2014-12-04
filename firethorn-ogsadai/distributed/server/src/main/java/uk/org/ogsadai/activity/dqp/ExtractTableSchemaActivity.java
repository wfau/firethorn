// Copyright (c) The University of Edinburgh, 2008-2010.
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

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.MatchedIterativeActivity;
import uk.org.ogsadai.activity.extension.RequestActivity;
import uk.org.ogsadai.activity.extension.ResourceActivity;
import uk.org.ogsadai.activity.extension.SecureActivity;
import uk.org.ogsadai.activity.io.ActivityInput;
import uk.org.ogsadai.activity.io.ActivityPipeProcessingException;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.activity.io.TypedActivityInput;
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
 * An activity that returns the schema information of a table
 * of a DQP resource. This supports the same inputs as the
 * relational ExtractTableSchema activity and so can be exposed with
 * the same name as that activity so client workflows can work 
 * transparently on relational or DQP resources.
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 * <li>
 * <code>name</code>. Type: {@link java.lang.String}.  Name of the table whose 
 * schema is to be returned or a pattern possibly specifying multiple tables. 
 * If the value is a pattern then '<code>%</code>' can be used to match any 
 * substring and '<code>_</code>' can be used to match a single character.
 * </li>
 * </ul>
 * <p>
 * Activity outputs:
 * </p>
 * <ul>
 * <li>
 * <code>data</code>. Type: OGSA-DAI list of 
 * {@link uk.org.ogsadai.converters.databaseschema.TableMetaData}.
 * </li>
 * </ul>
 * <p>
 * Configuration parameters:
 * </p>
 * <ul>
 * <li>
 * None.
 * </li>
 * </ul>
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
 * {@link uk.org.ogsadai.resource.dataresource.dqp.DQPResourceAccessor}
 * </li>
 * </ul>
 * <p>
 * Behaviour: 
 * </p>
 * <ul>
 * <li>
 * Gets the requested table's schema. Certain input patterns can inquire for 
 * more than one table, thus the result is a list. If no tables are found an 
 * empty list will be returned.
 * </li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team.
 */
public class ExtractTableSchemaActivity 
    extends MatchedIterativeActivity
    implements ResourceActivity,
               SecureActivity,
               RequestActivity
{
    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008-2010.";
    
    /** Logger. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(ExtractTableSchemaActivity.class);
    
    /** Activity input name - table name pattern. */
    public static final String INPUT_TABLE_NAME = "name";
    
    /** Activity output name - <code>TableMetaData</code>. */
    public static final String OUTPUT_DATA = "data";
 
    /** DQP resource accessor. */
    private DQPResourceAccessor mResource;
    
    /** DQP resource data dictionary. */
    private DataDictionary mDataDictionary;
    
    /** Output block writer. */
    private BlockWriter mOutput; 
    
    /** Security context. */
    private SecurityContext mSecurityContext;

    /** Request resource. */
    private RequestResource mRequestResource;

    /**
     * {@inheritDoc}
     */
    protected ActivityInput[] getIterationInputs()
    {
        return new ActivityInput[] { 
            new TypedActivityInput(INPUT_TABLE_NAME, String.class)
        };
    }

    /**
     * {@inheritDoc}
     */
    protected void preprocess() throws ActivityUserException,
            ActivityProcessingException, ActivityTerminatedException
    {
        validateOutput(OUTPUT_DATA);
        mOutput = getOutput();
        
        RequestDetails requestDetails = 
            new SimpleRequestDetails(
                mResource.getResource().getResourceID(),
                mRequestResource,
                mSecurityContext,
                getActivityDescriptor().getInstanceName());
        mDataDictionary = mResource.getFederation().getDataDictionary(
            requestDetails);
    }

    /**
     * {@inheritDoc}
     */
    protected void processIteration(Object[] iterationData)
        throws ActivityProcessingException, 
               ActivityTerminatedException,
               ActivityUserException
    {
        String tableName = (String)iterationData[0];
        LOG.debug(tableName);
        // Replace JDBC wild-cards with regular expression equivalents.
        tableName = tableName.replaceAll("_", ".").replaceAll("%", ".*");
        LOG.debug(tableName);
        Set<TableSchema> schemas = mDataDictionary.getTableSchemas();
        try
        {
            mOutput.write(ControlBlock.LIST_BEGIN);
            for (TableSchema schema : schemas)
            {
                if (schema.getTableName().matches(tableName))
                {
                    mOutput.write(schema.getSchema());
                }
            }
            mOutput.write(ControlBlock.LIST_END);
        }
        catch (PipeIOException e)
        {
            throw new ActivityPipeProcessingException(e);
        }
        catch (PipeTerminatedException e)
        {
            throw new ActivityTerminatedException();
        }
        catch (PipeClosedException e)
        {
            // No more output wanted, so finish early.
            iterativeStageComplete();
        }
    }

    /**
     * {@inheritDoc}
     */
    protected void postprocess() 
        throws ActivityUserException,
               ActivityProcessingException, 
               ActivityTerminatedException
    {
        // no post-processing
    }

    @SuppressWarnings("unchecked")
    public Class getTargetResourceAccessorClass()
    {
        return DQPResourceAccessor.class;
    }

    /**
     * {@inheritDoc}
     */
    public void setTargetResourceAccessor(ResourceAccessor resourceAccessor)
    {
        mResource = (DQPResourceAccessor)resourceAccessor;
    }

    /**
     * {@inheritDoc}
     */
    public void setSecurityContext(SecurityContext securityContext)
    {
        mSecurityContext = securityContext;
    }

    /**
     * {@inheritDoc}
     */
    public void setRequest(RequestResource requestResource)
    {
        mRequestResource = requestResource;
    }

}
