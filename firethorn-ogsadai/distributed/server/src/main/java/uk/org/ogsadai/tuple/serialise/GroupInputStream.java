// Copyright (c) The University of Edinburgh,  2002 - 2008.
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

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import uk.org.ogsadai.dqp.lqp.udf.ExecutableFunction;
import uk.org.ogsadai.expression.arithmetic.ExecutableFunctionExpression;
import uk.org.ogsadai.tuple.ColumnMetadata;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.sort.GroupTuple;

/**
 * Reads group tuples from a file.
 *
 * @author The OGSA-DAI Project Team.
 */
public class GroupInputStream 
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";
    
    /** Input stream to read from. */ 
    private final DataInputStream mInput;
    /** Column deserialisers. */
    private ObjectDeserialiser[] mColumnDeserialisers;
    /** Aggregate functions. */
    private ExecutableFunctionExpression[] mFunctions;
    /** Indexes of grouping columns. */
    private int[] mColumns;

    /**
     * Constructs a new tuple input stream for the given type of tuples.
     * 
     * @param input
     *            the input stream to read from
     * @param metadata
     *            tuple types
     * @param columns
     *            grouping columns
     * @param functions
     *            aggregate functions
     * @throws UnsupportedTupleTypeException 
     */
    public GroupInputStream(
            InputStream input, 
            TupleMetadata metadata, 
            int[] columns,
            ExecutableFunctionExpression[] functions) 
        throws UnsupportedTupleTypeException
    {
        mColumns = columns;
        mInput = new DataInputStream(input);
        mFunctions = functions;
        mColumnDeserialisers = new ObjectDeserialiser[columns.length]; 
        for (int i=0; i<columns.length; i++)
        {
            ColumnMetadata column = metadata.getColumnMetadata(i);
            mColumnDeserialisers[i] = 
                ObjectDeserialiserFactory.createDeserialiser(column.getType());
        }
    }
    
    /**
     * Reads a tuple from the input stream.
     * 
     * @return the next tuple in the stream
     * @throws IOException
     *             if an I/O error occurs
     * @throws EOFException
     *             if the input stream reaches the end before reading the
     *             complete tuple
     */
    public GroupTuple readTuple() 
        throws IOException, 
               EOFException
    {
        Object[] values = new Object[mColumnDeserialisers.length];
        for(int i=0; i<mColumns.length; i++)
        {
            values[mColumns[i]] = mColumnDeserialisers[i].readObject(mInput);
        }

        ExecutableFunctionExpression[] aggregates = 
            new ExecutableFunctionExpression[mFunctions.length];
        for(int i=0; i<mFunctions.length; i++)
        {
            SerialisableFunction serialisable = 
                (SerialisableFunction)mFunctions[i].getExecutable();
            SerialisableFunction function = serialisable.deserialise(mInput);
            ExecutableFunctionExpression exp = new ExecutableFunctionExpression(
                (ExecutableFunction) function);
            aggregates[i] = exp;
        }
        
        return new GroupTuple(values, mColumns, aggregates);
    }
    
    /**
     * Closes the input stream.
     */
    public void close()
    {
        try
        {
            mInput.close();
        } 
        catch (IOException e)
        {
            // nothing we can do
        }
    }
}
