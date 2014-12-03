// Copyright (c) The University of Edinburgh, 2012.
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

package uk.org.ogsadai.activity.derby;

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.MatchedIterativeActivity;
import uk.org.ogsadai.activity.extension.ConfigurableActivity;
import uk.org.ogsadai.activity.extension.ResourceActivity;
import uk.org.ogsadai.activity.io.ActivityInput;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.activity.io.TupleListActivityInput;
import uk.org.ogsadai.activity.io.TupleListIterator;
import uk.org.ogsadai.activity.io.TypedActivityInput;
import uk.org.ogsadai.activity.io.TypedOptionalActivityInput;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.config.Key;
import uk.org.ogsadai.config.KeyValueProperties;
import uk.org.ogsadai.metadata.MetadataWrapper;
import uk.org.ogsadai.resource.ResourceAccessor;
import uk.org.ogsadai.resource.dataresource.jdbc.EnhancedJDBCConnectionProvider;
import uk.org.ogsadai.resource.dataresource.jdbc.JDBCConnectionProvider;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.join.DerbyJoin;

public class DerbyStreamingJoinActivity 
    extends MatchedIterativeActivity 
    implements ResourceActivity, ConfigurableActivity
{
    
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2012";

    private static final DAILogger LOG = 
        DAILogger.getLogger(DerbyStreamingJoinActivity.class);

    public static final String INPUT_DATA1 = "data1";
    public static final String INPUT_DATA2 = "data2";
    public static final String INPUT_CONDITION = "condition";
    public static final String INPUT_READ_FIRST = "readFirst";
    public static final String OUTPUT_DATA = "result";

    private static final Key JOIN_IMPLEMENTATION = 
            new Key("join.implementation");
    
    private BlockWriter mOutput;
    private EnhancedJDBCConnectionProvider mResource;

    private String mJoinName;

    @Override
    protected ActivityInput[] getIterationInputs() 
    {
        return new ActivityInput[] {
                new TupleListActivityInput(INPUT_DATA1),
                new TupleListActivityInput(INPUT_DATA2),
                // if join implementation is Product then there's no condition
                new TypedOptionalActivityInput(INPUT_CONDITION, String.class),
                new TypedActivityInput(INPUT_READ_FIRST, String.class),
        };
    }

    @Override
    protected void preprocess()
        throws ActivityUserException,
               ActivityProcessingException, 
               ActivityTerminatedException
    {
        validateOutput(OUTPUT_DATA);
        mOutput = getOutput();
    }

    @Override
    protected void processIteration(Object[] iterationData)
        throws ActivityProcessingException, 
               ActivityTerminatedException,
               ActivityUserException 
    {
        // this could be a join or an outer join
        DerbyJoin join = loadJoin(mJoinName);
        join.setResource(mResource);
        TupleListIterator data1 = (TupleListIterator) iterationData[0];
        TupleListIterator data2 = (TupleListIterator) iterationData[1];
        String condition = (String) iterationData[2];
        String readFirst = (String) iterationData[3];
        boolean readRightInputFirst = INPUT_DATA2.equals(readFirst);
        join.storeRightTuples(readRightInputFirst);
        if (condition != null)
        {
            join.setCondition(DerbyUtilities.getCondition(condition));
        }
        TupleMetadata metadata1 = 
                (TupleMetadata)data1.getMetadataWrapper().getMetadata();
        TupleMetadata metadata2 =
                (TupleMetadata)data2.getMetadataWrapper().getMetadata();
        try
        {
            join.configure(metadata1, metadata2);
        }
        catch (Exception e)
        {
            // ColumnNotFoundException
            // UnsupportedOperandTypeException
            // IncomparableTypesException
            // ConfigurationException
            throw new ActivityUserException(e);
        }

        TupleListIterator storedData, streamedData;
        if (readRightInputFirst)
        {
            streamedData = data1;
            storedData = data2;
        }
        else 
        {
            streamedData = data2;
            storedData = data1;
        }
        
        // store data 
        join.storeTuples(storedData);
        
        // now join with the streamed input
        Tuple tuple;
        try
        {
            mOutput.write(ControlBlock.LIST_BEGIN);
            mOutput.write(new MetadataWrapper(join.getJoinMetadata()));
            while ((tuple = (Tuple)streamedData.nextValue()) != null)
            {
                for (Tuple joined : join.join(tuple))
                {
                    mOutput.write(joined);
                }
            }
            mOutput.write(ControlBlock.LIST_END);
        }
        catch (PipeClosedException e)
        {
        } 
        catch (PipeIOException e)
        {
            throw new ActivityProcessingException(e);
        } 
        catch (PipeTerminatedException e) 
        {
            throw new ActivityTerminatedException();
        }
        finally
        {
            join.close();
        }
    }

    @Override
    protected void postprocess() throws ActivityUserException,
            ActivityProcessingException, ActivityTerminatedException 
    {
        // no post-processing
    }

    @Override
    public void setTargetResourceAccessor(ResourceAccessor targetResource) 
    {
        mResource = (EnhancedJDBCConnectionProvider) targetResource;
    }

    @Override
    public Class<? extends ResourceAccessor> getTargetResourceAccessorClass() 
    {
        return JDBCConnectionProvider.class;
    }   
    
    private DerbyJoin loadJoin(String impl) throws ActivityProcessingException
    {
        try
        {
            Class<? extends DerbyJoin> joinClass =
                    Class.forName(impl).asSubclass(DerbyJoin.class);
            return joinClass.newInstance();
        }
        catch (ClassNotFoundException e)
        {
            throw new ActivityProcessingException(e);
        }
        catch (InstantiationException e)
        {
            throw new ActivityProcessingException(e);
        }
        catch (IllegalAccessException e)
        {
            throw new ActivityProcessingException(e);
        }

    }
    
    @Override
    public void configureActivity(KeyValueProperties properties)
    {
        mJoinName = (String)properties.get(JOIN_IMPLEMENTATION);
        LOG.debug("Using join implementation: " + mJoinName);
    }


}
