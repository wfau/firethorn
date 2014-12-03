// Copyright (c) The University of Edinburgh, 2008.
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

package uk.org.ogsadai.activity.relational;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.metadata.MetadataWrapper;
import uk.org.ogsadai.tuple.ColumnMetadata;
import uk.org.ogsadai.tuple.ColumnNotFoundException;
import uk.org.ogsadai.tuple.Null;
import uk.org.ogsadai.tuple.SimpleColumnMetadata;
import uk.org.ogsadai.tuple.SimpleTuple;
import uk.org.ogsadai.tuple.SimpleTupleMetadata;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.TupleTypes;
import uk.org.ogsadai.tuple.serialise.UnsupportedTupleTypeException;

/**
 * Random tuple generator.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class RandomTableScanTupleGenerator
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /** Tuple metadata object. */
    private TupleMetadata mTupleMetadata;
    /** Random number generator. */
    private Random mRandom;
    /** Current short value. */
    private short mShortCounter = 0;
    /** Current long value. */
    private long mLongCounter = 0;
    /** Current int value. */
    private int mIntCounter = 0;

    /**
     * Constructor.
     * 
     * @param metadata
     *            string encoded tuple metadata
     */
    public RandomTableScanTupleGenerator(String metadata)
    {
        int seed = 0;
        String[] columns = metadata.split(",");

        List<ColumnMetadata> columnMetadataList = new ArrayList<ColumnMetadata>();
        for (String col : columns)
        {
            String[] colDefs = col.split(":");

            columnMetadataList
                .add(new SimpleColumnMetadata(
                    colDefs[0],
                    colDefs[1],
                    null,
                    null,
                    Integer.parseInt(colDefs[2]),
                    1,
                    Boolean.parseBoolean(colDefs[3]) ? ColumnMetadata.COLUMN_NO_NULLS
                        : ColumnMetadata.COLUMN_NULLABLE_UNKNOWN, 1));

            seed += Integer.parseInt(colDefs[2]);
        }

        mTupleMetadata = new SimpleTupleMetadata(columnMetadataList);
        mRandom = new Random(seed);
    }

    /**
     * Generates a stream of random tuples.
     * 
     * @param writer
     *            output writer
     * @param count
     *            number of tuples to be generated
     * @throws PipeClosedException
     * @throws PipeIOException
     * @throws PipeTerminatedException
     * @throws ColumnNotFoundException
     * @throws UnsupportedTupleTypeException
     */
    public void generateTuples(BlockWriter writer, int count)
        throws PipeClosedException, PipeIOException, PipeTerminatedException,
        ColumnNotFoundException, UnsupportedTupleTypeException
    {
        writer.write(ControlBlock.LIST_BEGIN);
        writer.write(new MetadataWrapper(mTupleMetadata));

        for (int i = 0; i < count; i++)
        {
            List<Object> tupleContents = new ArrayList<Object>();
            for (int j = 0; j < mTupleMetadata.getColumnCount(); j++)
            {
                tupleContents.add(getNext(mTupleMetadata.getColumnMetadata(j)
                    .getType()));
            }
            writer.write(new SimpleTuple(tupleContents));
        }
        writer.write(ControlBlock.LIST_END);
    }

    /**
     * Generates next value for a given type. Behavior of this function depends
     * on the type.
     * <dl>
     * <dt>BIGDECIMAL</dt>
     * <dd>random long</dd>
     * <dt>BOOLEAN</dt>
     * <dd>random boolean</dd>
     * <dt>CHAR</dt>
     * <dd>random [A-Z]</dd>
     * <dt>DATE</dt>
     * <dd>random date between 1970-01-01 and NOW</dd>
     * <dt>DOUBLE</dt>
     * <dd>random double [0--1]</dd>
     * <dt>FLOAT</dt>
     * <dd>random float [0--1]</dd>
     * <dt>INT</dt>
     * <dd>strictly increasing positive integer</dd>
     * <dt>LONG</dt>
     * <dd>strictly increasing positive long</dd>
     * <dt>ODNULL</dt>
     * <dd><code>Null</code> object</dd>
     * <dt>SHORT</dt>
     * <dd>strictly increasing positive short</dd>
     * <dt>STRING</dt>
     * <dd>random string</dd>
     * <dt>TIME</dt>
     * <dd>random time</dd>
     * <dt>TIMESTAMP</dt>
     * <dd>random date between 1970-01-01 and NOW</dd>
     * </dl>
     * 
     * @param type
     *            column type
     * @return next value
     * @throws UnsupportedTupleTypeException
     *             when next value for unsupported type is requested
     */
    private Object getNext(int type) throws UnsupportedTupleTypeException
    {
        switch (type)
        {
            case TupleTypes._BIGDECIMAL:
                return new BigDecimal(mRandom.nextLong());
            case TupleTypes._BOOLEAN:
                return mRandom.nextBoolean();
            case TupleTypes._CHAR:
                return getNextChar();
            case TupleTypes._DATE:
                return getNextDate();
            case TupleTypes._DOUBLE:
                return new Double(mRandom.nextDouble());
            case TupleTypes._FLOAT:
                return new Float(mRandom.nextFloat());
            case TupleTypes._INT:
                return new Integer(++mIntCounter);
            case TupleTypes._LONG:
                return new Long(++mLongCounter);
            case TupleTypes._ODNULL:
                return Null.VALUE;
            case TupleTypes._SHORT:
                return new Short(++mShortCounter);
            case TupleTypes._STRING:
                return getNextString();
            case TupleTypes._TIME:
                return new Time(getNextDate().getTime());
            case TupleTypes._TIMESTAMP:
                return new Timestamp(getNextDate().getTime());
            case TupleTypes._OBJECT:
            case TupleTypes._ODBLOB:
            case TupleTypes._ODCLOB:
            case TupleTypes._FILE:
            case TupleTypes._BYTEARRAY:
                throw new UnsupportedTupleTypeException(type);
            default:
                throw new UnsupportedTupleTypeException(type);
        }
    }

    /**
     * Generates random string.
     * 
     * @return random string
     */
    private String getNextString()
    {
        String[] keyWords = "The quick brown fox jumps over the lazy dog"
            .split(" ");

        return "'" + keyWords[mRandom.nextInt(keyWords.length)] + ' '
            + keyWords[mRandom.nextInt(keyWords.length)] + ' '
            + keyWords[mRandom.nextInt(keyWords.length)] + "'";
    }

    /**
     * Generates random character.
     * 
     * @return random character
     */
    private Character getNextChar()
    {
        return (char) ('A' + mRandom.nextInt('Z' - 'A'));
    }

    /**
     * Generates random date.
     * 
     * @return random date
     */
    private Date getNextDate()
    {
        return new Date(mRandom.nextLong() % System.currentTimeMillis());
    }

}
