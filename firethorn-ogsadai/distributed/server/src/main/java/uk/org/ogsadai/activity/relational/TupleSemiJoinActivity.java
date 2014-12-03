// Copyright (c) The University of Edinburgh, 2010.
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

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.TupleListIterator;
import uk.org.ogsadai.expression.Expression;
import uk.org.ogsadai.metadata.MetadataWrapper;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.join.Join;

/**
 * This activity performs a semi-join on the input tuple lists.
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 * <li> <code>data1</code> Type: OGSA-DAI list of
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object.
 * This is a mandatory input.</li>
 * <li> <code>data2</code> Type: OGSA-DAI list of
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object.
 * This is a mandatory input.</li>
 * <li> <code>condition</code> Type: String. This is a mandatory
 * input. The join condition.</li>
 * </ul>
 * <p>
 * Activity outputs:
 * </p>
 * <ul>
 * <li> <code>result</code>. Type: Type: OGSA-DAI list of
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object.</li>
 * </ul>
 * <p>
 * Configuration parameters:
 * </p>
 * <ul>
 * <li>
 * <code>join.implementation</code> - a mandatory configuration
 * setting providing the name of a join implementation.
 * </li>
 * </ul>
 * <p>
 * Activity input/output ordering:
 * </p>
 * <ul>
 * <li>The right side of the join (<code>data2</code>) input is read in full 
 * before the left side (<code>data1</code>) is streamed through.
 * </li>
 * </ul>
 * <p>
 * Activity contracts:
 * </p>
 * <ul>
 * <li> None. </li>
 * </ul>
 * <p>
 * Behaviour:
 * </p>
 * <ul>
 * <li>
 * This activity produces a semi-join of two tuple lists. The result
 * of a semi-join is the set of all tuples t from the left input for which there 
 * is at least one tuple s from the right input so that the joined tuple (t, s)
 * matches the condition.
 * </li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team.
 */
public class TupleSemiJoinActivity 
    extends TupleSemiJoinBase
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2010";

    /**
     * {@inheritDoc}
     */
    @Override
    protected void processIteration(Object[] iterationData)
        throws ActivityProcessingException, 
               ActivityTerminatedException,
               ActivityUserException
    {
        // get join condition
        Expression condition = getCondition((String)iterationData[0]);
        // store all tuples from the left
        TupleListIterator left = (TupleListIterator)iterationData[1];
        TupleListIterator right = (TupleListIterator)iterationData[2];
        TupleMetadata metadataLeft = 
            (TupleMetadata)left.getMetadataWrapper().getMetadata();
        TupleMetadata metadataRight = 
            (TupleMetadata)right.getMetadataWrapper().getMetadata();
        Join join = loadJoin(mJoinName);
        join.setCondition(condition);
        join.storeRightTuples(true);
        try
        {
            join.configure(metadataLeft, metadataRight);
        }
        catch (Exception e)
        {
            // ColumnNotFoundException
            // UnsupportedOperandTypeException
            // IncomparableTypesException
            throw new ActivityUserException(e);
        }
        join.storeTuples(right);
        writeBlock(ControlBlock.LIST_BEGIN);
        writeBlock(new MetadataWrapper(metadataLeft));
        Tuple tuple;
        while ((tuple = (Tuple)left.nextValue()) != null)
        {
            if (join.join(tuple).iterator().hasNext())
            {
                writeBlock(tuple);
            }
        }
        writeBlock(ControlBlock.LIST_END);
        join.close();
    }

}
