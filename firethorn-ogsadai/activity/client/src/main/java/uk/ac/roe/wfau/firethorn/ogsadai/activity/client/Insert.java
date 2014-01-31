/**
 * Copyright (c) 2014, ROE (http://www.roe.ac.uk/)
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

package uk.ac.roe.wfau.firethorn.ogsadai.activity.client;

import uk.ac.roe.wfau.firethorn.ogsadai.activity.common.InsertParam;
import uk.org.ogsadai.activity.ActivityName;
import uk.org.ogsadai.client.toolkit.ActivityOutput;
import uk.org.ogsadai.client.toolkit.ResourceActivity;
import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.activity.ActivityInput;
import uk.org.ogsadai.client.toolkit.activity.BaseResourceActivity;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityInput;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityOutput;
import uk.org.ogsadai.client.toolkit.exception.ActivityIOIllegalStateException;
import uk.org.ogsadai.client.toolkit.exception.DataSourceUsageException;
import uk.org.ogsadai.client.toolkit.exception.DataStreamErrorException;
import uk.org.ogsadai.client.toolkit.exception.UnexpectedDataValueException;
import uk.org.ogsadai.data.IntegerData;
import uk.org.ogsadai.data.StringData;
import uk.org.ogsadai.resource.ResourceID;

/**
 * Copy of the OGSA-DAI SQLBulkLoadTupleActivity modified to use batch insert.
 * http://viralpatel.net/blogs/batch-insert-in-java-jdbc/
 *
 */
public class Insert
extends BaseResourceActivity
implements ResourceActivity
    {

    /**
     * Public interface for the Activity parameters.
     *
     */
    public static interface Param
        {
        public Integer first();
        public Integer block();
        public String  table();
        }
    
    /**
     * Tuple data input.
     *
     */
    private final ActivityInput input;

    /**
     * Table name input.
     *
     */
    private final ActivityInput table;

    /**
     * First block size.
     *
     */
    private final ActivityInput first;

    /**
     * Main block size.
     *
     */
    private final ActivityInput block;
    
    /**
     * Tuple data output.
     *
     */
    private final ActivityOutput output;

    /**
     * Public constructor.
     * 
     */
    public Insert(final Param param)
        {
        this();
        table(
            param.table()
            );
        first(
            param.first()
            );
        block(
            param.block()
            );
        }
    
    /**
     * Public constructor.
     * 
     */
    public Insert()
        {
        super(
            new ActivityName(
                InsertParam.ACTIVITY_NAME
                )
            );
        input = new SimpleActivityInput(
            InsertParam.TUPLE_INPUT
            );
        table = new SimpleActivityInput(
            InsertParam.TABLE_NAME
            );
        first = new SimpleActivityInput(
            InsertParam.FIRST_SIZE,
            true
            );
        block = new SimpleActivityInput(
            InsertParam.BLOCK_SIZE,
            true
            );
        output = new SimpleActivityOutput(
            InsertParam.TUPLE_OUTPUT);
        }

    /**
     * Set the target resource.
     *
     */
    public void resource(final ResourceID ident)
        {
        setResourceID(
            ident
            );
        }

    /**
     * Set the table name.
     *
     */
    public void table(final String value)
        {
        table.add(
            new StringData(
                value
                )
            );
        }

    /**
     * Set the first block size.
     *
     */
    public void first(final Integer value)
        {
        first.add(
            new IntegerData(
                value
                )
            );
        }

    /**
     * Set the main block size.
     *
     */
    public void block(final Integer value)
        {
        block.add(
            new IntegerData(
                value
                )
            );
        }

    /**
     * Connect the tuple input.
     *
     */
    public void input(final SingleActivityOutput output)
        {
        input.connect(
            output
            );
        }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateIOState() throws ActivityIOIllegalStateException
        {
        }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ActivityInput[] getInputs()
        {
        return new ActivityInput[]
            {
            input,
            first,
            block,
            table
            };
        }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ActivityOutput[] getOutputs()
        {
        return new ActivityOutput[]
          {
          output
          };
        }

    /**
     * Get the data output.
     *
     */
    public SingleActivityOutput output()
        {
        return output.getSingleActivityOutputs()[0];
        }

    /**
     * Determines if more results are available.
     *
     * @return
     *   true if more data is available, false otherwise
     *
     * @throws DataStreamErrorException
     *             if there is an error on the data stream.
     * @throws UnexpectedDataValueException
     *             if there is an unexpected data value on the data stream.
     * @throws DataSourceUsageException
     *             if there is an error reading from a data source.
     *
    public boolean hasNextResult() throws
        DataStreamErrorException,
        UnexpectedDataValueException,
        DataSourceUsageException
        {
        return output.getDataValueIterator().hasNext();
        }
     */

    /**
     * Gets the number of tuples loaded by the next bulk load.
     *
     * @return
     *   the number of tuples which were loaded, if more results are present
     *   -1 if no more results are available
     *
     * @throws DataStreamErrorException
     *             if there is an error on the data stream.
     * @throws UnexpectedDataValueException
     *             if there is an unexpected data value on the data stream.
     * @throws DataSourceUsageException
     *             if there is an error reading from a data source.
     *
    public int nextResult() throws
        DataStreamErrorException,
        UnexpectedDataValueException,
        DataSourceUsageException
        {
        if ( !hasNextResult())
            {
            return -1;
            }
        return output.getDataValueIterator().nextAsInt();
        }
     */
    }
