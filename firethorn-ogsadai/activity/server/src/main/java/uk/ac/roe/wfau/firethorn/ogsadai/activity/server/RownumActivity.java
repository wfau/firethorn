/**
 * Copyright (c) 2013, ROE (http://www.roe.ac.uk/)
 * All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package uk.ac.roe.wfau.firethorn.ogsadai.activity.server;

import java.util.ArrayList;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.roe.wfau.firethorn.ogsadai.activity.common.RownumParam;
import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.MatchedIterativeActivity;
import uk.org.ogsadai.activity.io.ActivityInput;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.TupleListActivityInput;
import uk.org.ogsadai.activity.io.TupleListIterator;
import uk.org.ogsadai.activity.io.TypedActivityInput;
import uk.org.ogsadai.activity.sql.ActivitySQLException;
import uk.org.ogsadai.metadata.MetadataWrapper;
import uk.org.ogsadai.tuple.ColumnMetadata;
import uk.org.ogsadai.tuple.SimpleColumnMetadata;
import uk.org.ogsadai.tuple.SimpleTuple;
import uk.org.ogsadai.tuple.SimpleTupleMetadata;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.TupleTypes;

/**
 * Activity to add a row number column.
 *
 */
public class RownumActivity
extends MatchedIterativeActivity
    {

    /**
     * Our debug logger.
     *
     */
    private static Logger logger = LoggerFactory.getLogger(
        RownumActivity.class
        );

    /**
     * Public constructor.
     *
     */
    public RownumActivity()
        {
        super();
        }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActivityInput[] getIterationInputs()
        {
        return new ActivityInput[] {
            new TypedActivityInput(
                RownumParam.COLUMN_NAME,
                String.class
                ),
            new TupleListActivityInput(
                RownumParam.TUPLE_INPUT
                )
            };
        }

    /**
     * Block writer for output.
     *
     */
    private BlockWriter writer;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void preprocess()
    throws ActivitySQLException, ActivityProcessingException
        {
        logger.debug("preprocess()");
    	try {
            validateOutput(
                RownumParam.TUPLE_OUTPUT
                );
            writer = getOutput(
                RownumParam.TUPLE_OUTPUT
                );
            }
        catch (final Exception ouch)
            {
            logger.warn("Exception validating outputs", ouch);
            throw new ActivityProcessingException(
                ouch
                );
            }
        }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void processIteration(final Object[] iteration)
    throws ActivityProcessingException,
           ActivityTerminatedException,
           ActivityUserException
        {
        logger.debug("processIteration(Object[])");
        try {

            //
            // Get the column name.
            final String colname = (String) iteration[0];
            logger.debug("Column name [{}]", colname);
            //
            // Get the tuple iterator.
            final TupleListIterator tuples = (TupleListIterator) iteration[1];

            //
            // Create the metadata for our new column and combine it with the original.
            final MetadataWrapper metadata = new MetadataWrapper(
	            new SimpleTupleMetadata(
	                "Firethorn result set",
	                new SimpleTupleMetadata(
                		Arrays.asList(
            				(ColumnMetadata) new SimpleColumnMetadata(
    							colname,
                                TupleTypes._LONG,
                                0,
                                ColumnMetadata.COLUMN_NO_NULLS,
                                0
                                )
                    		)
        	            ),
	                (TupleMetadata) tuples.getMetadataWrapper().getMetadata()
	                )
                );

            //
            // Write the LIST_BEGIN marker and metadata
            writer.write(ControlBlock.LIST_BEGIN);
            writer.write(metadata);

            //
            // Process the tuples.
            Tuple tuple ;
            for(long rownum = 0 ; ((tuple = (Tuple) tuples.nextValue()) != null) ; rownum++)
                {
                //
                // Create a new list of elements.
                final ArrayList<Object> elements = new ArrayList<Object>(
                    tuple.getColumnCount() + 1
                    );
                //
                // Add our row number.
                elements.add(
                    new Long(
                        rownum
                        )
                    );
                //
                // Add the rest of the columns from the tuple.
                final int count = tuple.getColumnCount();
                for (int colnum = 0; colnum < count ; colnum++)
                    {
                    elements.add(
                        tuple.getObject(
                            colnum
                            )
                        );
                    }
                //
                // Write the new tuple to our output.
                writer.write(
                    new SimpleTuple(
                        elements
                        )
                    );
                }

            //
            // Write the list end marker
            done();
            }
        catch (final ActivityProcessingException ouch)
            {
            throw ouch ;
            }
        catch (final PipeClosedException ouch)
            {
            logger.warn("PipeClosed during processing");
            iterativeStageComplete();
            done();
            }
        catch (final Throwable ouch)
            {
            logger.warn("Exception during processing", ouch);
            throw new ActivityProcessingException(
                ouch
                );
            }
        }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void postprocess()
    throws ActivityProcessingException
        {
        logger.debug("postprocess()");
        }

    // Common base class ?
    private void done()
    throws ActivityProcessingException
        {
        try {
            writer.write(
                ControlBlock.LIST_END
                );
            }
        catch (final PipeClosedException ouch)
            {
            logger.warn("PipeClosed during done");
            }
        catch (final Throwable ouch)
            {
            logger.warn("Exception during closing", ouch);
            throw new ActivityProcessingException(
                ouch
                );
            }
        }
    }

