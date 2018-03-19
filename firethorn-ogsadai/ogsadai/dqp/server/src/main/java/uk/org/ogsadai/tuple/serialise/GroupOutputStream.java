// Copyright (c) The University of Edinburgh,  2008.
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


package uk.org.ogsadai.tuple.serialise;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import uk.org.ogsadai.expression.arithmetic.ExecutableFunctionExpression;
import uk.org.ogsadai.tuple.ColumnMetadata;
import uk.org.ogsadai.tuple.ColumnNotFoundException;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.sort.GroupTuple;

/**
 * A custom serialiser for tuple groups.
 *
 * @author The OGSA-DAI Project Team.
 */
public class GroupOutputStream 
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";
    
    /** Output stream to write to. */
    private final DataOutputStream mOutput;
    /** Column serialisers. */
    private ObjectSerialiser[] mSerialisers;

    /**
     * Constructs a new tuple output stream that writes to the given Java output
     * stream.
     * 
     * @param output
     *            output stream to write to
     */
    public GroupOutputStream(OutputStream output)
    {
        mOutput = new DataOutputStream(output);
    }

    /**
     * Sets the metadata for the tuple stream. This method must be called before
     * any tuples are written.
     * 
     * @param metadata
     *            tuple metadata
     * @param columns
     *            indexes of the grouping columns
     * @param aggregates
     *            aggregate functions
     * @throws UnsupportedTupleTypeException
     */
    public void write(
            TupleMetadata metadata, 
            int[] columns, 
            ExecutableFunctionExpression[] aggregates) 
        throws UnsupportedTupleTypeException
    {
        mSerialisers = new ObjectSerialiser[columns.length]; 
        for (int i=0; i<columns.length; i++)
        {
            ColumnMetadata column = metadata.getColumnMetadata(i);
            int type = column.getType();
            mSerialisers[i] = 
                ObjectSerialiserFactory.createObjectSerialiser(type);
        }
    }
    
    /**
     * Writes a tuple to the output.
     * 
     * @param group
     *            group to write
     * @throws ColumnNotFoundException
     * @throws IOException
     */
    public void writeTuple(GroupTuple group) 
        throws ColumnNotFoundException,
               IOException
    {
        Object[] columns = group.getGroupColumns();
        for(int i=0; i<mSerialisers.length; i++)
        {
            mSerialisers[i].writeObject(mOutput, columns[i]);
        }
        ExecutableFunctionExpression[] aggregates = group.getAggregates();
        for (ExecutableFunctionExpression function : aggregates)
        {
            SerialisableFunction serialisable =
                (SerialisableFunction)function.getExecutable();
            serialisable.serialise(mOutput);
        }
    }
    
    /**
     * Closes the output stream.
     */
    public void close()
    {
        try
        {
            mOutput.flush();
            mOutput.close();
        } 
        catch (IOException e)
        {
            // nothing we can do
        }
    }
    
}
