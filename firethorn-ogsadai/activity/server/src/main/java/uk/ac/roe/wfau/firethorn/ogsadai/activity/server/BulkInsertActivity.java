// Copyright (c) The University of Edinburgh,  2007.
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

package uk.ac.roe.wfau.firethorn.ogsadai.activity.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.MatchedIterativeActivity;
import uk.org.ogsadai.activity.extension.ResourceActivity;
import uk.org.ogsadai.activity.io.ActivityInput;
import uk.org.ogsadai.activity.io.ActivityPipeProcessingException;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.activity.io.TupleListActivityInput;
import uk.org.ogsadai.activity.io.TupleListIterator;
import uk.org.ogsadai.activity.io.TypedActivityInput;
import uk.org.ogsadai.activity.sql.ActivitySQLException;
import uk.org.ogsadai.activity.sql.ActivitySQLUserException;
import uk.org.ogsadai.activity.sql.SQLUtilities;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.metadata.MetadataWrapper;
import uk.org.ogsadai.resource.ResourceAccessor;
import uk.org.ogsadai.resource.dataresource.jdbc.JDBCConnectionProvider;
import uk.org.ogsadai.resource.dataresource.jdbc.JDBCConnectionUseException;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;

/**
 * Copy of the OGSA-DAI SQLBulkLoadTupleActivity.
 * Modified to use batch insert.
 * http://viralpatel.net/blogs/batch-insert-in-java-jdbc/
 * @author The OGSA-DAI Project Team.
 * 
 */
public class BulkInsertActivity extends MatchedIterativeActivity
    implements ResourceActivity
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE =
        "Copyright (c) The University of Edinburgh,  2007-2008";
    
    /** Logger. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(BulkInsertActivity.class);
    
    /** 
     * Activity input name(<code>data</code>) - 
     * Tuples to be bulk loaded. 
     * (OGSA-DAI tuple list).
     */
    public static final String INPUT_TUPLES = "data";

    /** 
     * Activity input name(<code>tableName</code>) - 
     * Table name. 
     * ({@link java.lang.String}).
     */
    public static final String INPUT_TABLE_NAME = "tableName";
    
    /** 
     * Activity output name(<code>result</code>) - 
     * Number of inserted tuples. 
     * ({@link java.lang.Integer}).
     */
    public static final String OUTPUT_RESULT = "result";

    /** The JDBC connection provider */
    private JDBCConnectionProvider mResource;
    
    /** The database connection. */
    private Connection mConnection;

    /** The statement to be used for the execution of updates. */
    private PreparedStatement mStatement;

    /** The only output of the activity. */
    private BlockWriter mOutput;
    
    /**
     * {@inheritDoc}
     */ 
    public Class getTargetResourceAccessorClass()
    {
        return JDBCConnectionProvider.class;
    }

    /**
     * {@inheritDoc}
     */
    public void setTargetResourceAccessor(final ResourceAccessor targetResource)
    {
        mResource = (JDBCConnectionProvider) targetResource;
    }

    /**
     * {@inheritDoc}
     */
    protected ActivityInput[] getIterationInputs()
    {
        return new ActivityInput[] { 
            new TypedActivityInput(INPUT_TABLE_NAME, String.class),
            new TupleListActivityInput(INPUT_TUPLES)
        };
    }
    
    /**
     * {@inheritDoc}
     */
    protected void preprocess() throws ActivityUserException,
        ActivityProcessingException, ActivityTerminatedException
    {
        validateOutput(OUTPUT_RESULT);
        mOutput = getOutput();
        try
        {
            mConnection = mResource.getConnection();
            mConnection.setAutoCommit(false);
        }
        catch (SQLException e)
        {
            throw new ActivitySQLException(e);
        }
        catch (JDBCConnectionUseException e)
        {
            throw new ActivitySQLException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    protected void processIteration(final Object[] iterationData)
        throws ActivityProcessingException, ActivityTerminatedException,
        ActivityUserException
    {
        final String tableName = (String) iterationData[0];
        final TupleListIterator tuples = (TupleListIterator) iterationData[1];
        
        final MetadataWrapper metadataWrapper = tuples.getMetadataWrapper();
        final TupleMetadata metadata = (TupleMetadata) metadataWrapper.getMetadata();
        final String sql = 
            SQLUtilities.createInsertStatementSQL(tableName, metadata);
            
        try
        {

            LOG.debug("Preparing statement ...");

        	mStatement = mConnection.prepareStatement(sql);
            //int totalCount = 0;
            Tuple tuple = null;


            long batchsize  = 1000 ;
            long batchcount = 0 ;
            
            //LOG.debug("Starting loop...");
            while ((tuple = (Tuple) tuples.nextValue()) != null)
            {
                SQLUtilities.setStatementParameters(mStatement, tuple, metadata);
                mStatement.addBatch();
                if ((batchcount++ % batchsize) == 0)
                	{	
                    //LOG.debug("Executing batch [" + batchcount + "]");
                	mStatement.executeBatch();
                	}
            }
            //LOG.debug("Executing final batch [" + batchcount + "]");
        	mStatement.executeBatch();

            mOutput.write(new Long(batchcount));
            mConnection.commit();
            mStatement.close();
        }
        catch (SQLException e)
        {
            rollback();
            throw new ActivitySQLUserException(e);
        }
        catch (PipeClosedException e)
        {
            rollback();
            // no more output wanted
            iterativeStageComplete();
        }
        catch (PipeIOException e)
        {
            rollback();
            throw new ActivityPipeProcessingException(e);
        }
        catch (PipeTerminatedException e)
        {
            rollback();
            throw new ActivityTerminatedException();
        }
        catch (ActivityUserException e)
        {
            rollback();
            throw e;
        }
        catch (ActivityProcessingException e)
        {
            rollback();
            throw e;
        }
        catch (ActivityTerminatedException e)
        {
            rollback();
            throw e;
        }
        catch (Throwable e)
        {
            rollback();
            throw new ActivityProcessingException(e);
        }
        
    }

    /**
     * {@inheritDoc}
     */
    protected void postprocess() throws ActivityUserException,
        ActivityProcessingException, ActivityTerminatedException
    {

        try
        {
            mConnection.setAutoCommit(true);
        }
        catch (SQLException e)
        {
            throw new ActivitySQLUserException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    protected void cleanUp() throws Exception
    {
        super.cleanUp();  // clean up the superclass
        
        if (mStatement != null)
        {
            mStatement.close();
        }

        if (mConnection != null && !mConnection.getAutoCommit())
        {
            mConnection.rollback();
            mConnection.setAutoCommit(true);
        }
        if (mResource != null)
        {
            mResource.releaseConnection(mConnection);
        }
    }
    
    /**
     * Rolls back the database state in case of an error.
     */
    private void rollback()
    {
        try
        {
            mConnection.rollback();
            mConnection.setAutoCommit(true);
        } 
        catch (SQLException e)
        {
            LOG.warn(e);
        }

    }
    
}
