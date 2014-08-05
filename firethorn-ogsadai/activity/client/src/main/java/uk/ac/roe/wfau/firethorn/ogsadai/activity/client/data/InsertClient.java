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

package uk.ac.roe.wfau.firethorn.ogsadai.activity.client.data;

import uk.ac.roe.wfau.firethorn.ogsadai.activity.common.data.InsertParam;
import uk.org.ogsadai.activity.ActivityName;
import uk.org.ogsadai.client.toolkit.ActivityOutput;
import uk.org.ogsadai.client.toolkit.ResourceActivity;
import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.activity.ActivityInput;
import uk.org.ogsadai.client.toolkit.activity.BaseResourceActivity;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityInput;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityOutput;
import uk.org.ogsadai.client.toolkit.exception.ActivityIOIllegalStateException;
import uk.org.ogsadai.data.IntegerData;
import uk.org.ogsadai.data.StringData;
import uk.org.ogsadai.resource.ResourceID;

/**
 * Client for our Insert Activity.
 *
 */
public class InsertClient
extends BaseResourceActivity
implements ResourceActivity
    {

    /**
     * Public interface for the Activity parameters.
     * @todo Move this to the common package.
     *
     */
    public static interface Param
        {
        /**
         * The target resource name.
         * @return The target resource name.
         *
         */
        public String store();

        /**
         * The target table name.
         * @return The target table name.
         *
         */
        public String table();

        /**
         * The first block size.
         * @return The first block size.
         *
         */
        public Integer first();

        /**
         * The main block size.
         * @return The main block size.
         *
         */
        public Integer block();
        
        }
    
    /**
     * The input tuples
     *
     */
    private final ActivityInput input;

    /**
     * The target table name.
     *
     */
    private final ActivityInput table;

    /**
     * The first block size.
     *
     */
    private final ActivityInput first;

    /**
     * The main block size.
     *
     */
    private final ActivityInput block;
    
    /**
     * The output tuples
     *
     */
    private final ActivityOutput output;

    /**
     * Public constructor.
     * @param source The tuple input source.
     * @param param The Activity parameters.
     * 
     */
    public InsertClient(final SingleActivityOutput source, final Param param)
        {
        this(
            param
            );
        input(
            source
            );
        }

    /**
     * Public constructor.
     * @param param The Activity parameters.
     * 
     */
    public InsertClient(final Param param)
        {
        this();
        if (param != null)
            {
            resource(
                param.store()
                );
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
        }
    
    /**
     * Public constructor.
     * 
     */
    public InsertClient()
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
    public void resource(final String ident)
        {
        this.resource(
            new ResourceID(
                ident
                )
            );
        }

    /**
     * Set the target resource.
     *
     */
    public void resource(final ResourceID ident)
        {
        this.setResourceID(
            ident
            );
        }

    /**
     * Set the table name.
     *
     */
    public void table(final String value)
        {
        if (value != null)
            {
            table.add(
                new StringData(
                    value
                    )
                );
            }
        }

    /**
     * Set the first block size.
     *
     */
    public void first(final Integer value)
        {
        if (value != null)
            {
            first.add(
                new IntegerData(
                    value
                    )
                );
            }
        }

    /**
     * Set the main block size.
     *
     */
    public void block(final Integer value)
        {
        if (value != null)
            {
            block.add(
                new IntegerData(
                    value
                    )
                );
            }
        }

    /**
     * Connect the tuple input.
     * @param source The tuple input.
     *
     */
    public void input(final SingleActivityOutput source)
        {
        input.connect(
            source
            );
        }

    /**
     * Get the tuple output.
     * @return The tuples output
     *
     */
    public SingleActivityOutput output()
        {
        return output.getSingleActivityOutputs()[0];
        }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateIOState()
    throws ActivityIOIllegalStateException
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
    }
