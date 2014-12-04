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


package uk.org.ogsadai.client.toolkit.activities.generic;

import java.util.ArrayList;

import uk.org.ogsadai.client.toolkit.ActivityOutput;
import uk.org.ogsadai.client.toolkit.DataSourceResource;
import uk.org.ogsadai.client.toolkit.DataValueIterator;
import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.exception.ActivityIOIllegalStateException;
import uk.org.ogsadai.client.toolkit.exception.ActivityOutputUnreadableException;

/**
 * Class used to pass details of activity outputs from the 
 * <code>GenericActivity</code> class to the <code>BaseActivity</code> class.
 * Not all methods of the <code>ActivityOutput</code> interface are implemented
 * here as only a few methods of this interface are called by the 
 * <code>BaseActivity</code> class.  The functionality provided by the other
 * methods in the interface is handled within the 
 * <code>SimpleTraversableSingleActivityOutput</code> class that is used 
 * internally by <code>GenericActivity</code>.
 *
 * @author The OGSA-DAI Project Team
 */
public class GenericActivityActivityOutput implements ActivityOutput
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009.";

    /** All the outputs with a common input name. */
    protected ArrayList<TraversableSingleActivityOutput> mOutputs;
    
    /**
     * Constructor.
     * 
     * @param outputName output name
     * @param outputs    all the single outputs corresponding to the output name
     */
    public GenericActivityActivityOutput(
        String outputName,
        ArrayList<TraversableSingleActivityOutput> outputs)
    {
        mOutputs = outputs;
    }
    
    /**
     * {@inheritDoc}
     */
    public DataValueIterator getDataValueIterator()
        throws ActivityOutputUnreadableException
    {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public DataValueIterator getDataValueIterator(int index)
        throws ActivityOutputUnreadableException,
        ArrayIndexOutOfBoundsException
    {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public int getNumberOfOutputs()
    {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public String getOutputName()
    {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public String getPipeName()
    {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public String getPipeName(int index)
    {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public SingleActivityOutput[] getSingleActivityOutputs()
    {
        return mOutputs.toArray(new SingleActivityOutput[]{});
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasData()
    {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasData(int index)
    {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public void setDataSourceResource(DataSourceResource dataSourceResource)
    {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public void setDataSourceResource(int index,
        DataSourceResource dataSourceResource)
    {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public void setDataSourceResourceBlocksSize(int numBlocks)
    {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public void setDataSourceResourceBlocksSize(int index, int numBlocks)
    {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public void setDataValueIterator(DataValueIterator dataValueIterator)
    {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public void setDataValueIterator(int index,
        DataValueIterator dataValueIterator)
    {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public void setNumberOfOutputs(int count)
    {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public void validateState() throws ActivityIOIllegalStateException
    {
        for( TraversableSingleActivityOutput output : mOutputs)
        {
            output.validateState();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        int i=0;
        for( TraversableSingleActivityOutput output : mOutputs)
        {
            buffer.append("Occurrence with index "+ (i++) + ":\n");
            buffer.append(output);
        }
        return buffer.toString();
    }
}
