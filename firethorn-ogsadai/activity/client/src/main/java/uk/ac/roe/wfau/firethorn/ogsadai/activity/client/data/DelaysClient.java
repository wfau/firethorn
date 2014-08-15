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

import uk.ac.roe.wfau.firethorn.ogsadai.activity.common.data.DelaysParam;
import uk.org.ogsadai.activity.ActivityName;
import uk.org.ogsadai.client.toolkit.Activity;
import uk.org.ogsadai.client.toolkit.ActivityOutput;
import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.activity.ActivityInput;
import uk.org.ogsadai.client.toolkit.activity.BaseActivity;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityInput;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityOutput;
import uk.org.ogsadai.client.toolkit.exception.ActivityIOIllegalStateException;
import uk.org.ogsadai.data.IntegerData;

/**
 * Client for our Delays Activity.
 *
 */
public class DelaysClient
extends BaseActivity implements Activity
    {

    /**
     * Public interface for the Activity parameters.
     * @todo Move this to the common package.
     *
     */
    public static interface Param
        {
        /**
         * Delay before the first row (0 = no delay).
         * @return The delay before the first row.
         *
         */
        public Integer first();

        /**
         * Delay after the last row (0 = no delay).
         * @return The delay after the last row.
         *
         */
        public Integer last();

        /**
         * Delay between each row (0 = no delay).
         * @return The delay between each row.
         *
         */
        public Integer every();

        }

    /**
     * The delay before the first row.
     *
     */
    private final ActivityInput first;

    /**
     * The delay after the last row.
     *
     */
    private final ActivityInput last;

    /**
     * The delay between every row.
     *
     */
    private final ActivityInput every;
    
    /**
     * The input tuples
     *
     */
    private final ActivityInput input;

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
    public DelaysClient(final SingleActivityOutput source, final Param param)
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
    public DelaysClient(final Param param)
        {
        this();
        if (param != null)
            {
            this.first(
                param.first()
                );
            this.every(
                param.every()
                );
            this.last(
                param.last()
                );
            }
        }

    /**
     * Public constructor.
     *
     */
    public DelaysClient()
        {
        super(
            new ActivityName(
                DelaysParam.ACTIVITY_NAME
                )
            );
        first = new SimpleActivityInput(
            DelaysParam.FIRST_DELAY,
            true
            );
        last = new SimpleActivityInput(
            DelaysParam.LAST_DELAY,
            true
            );
        every = new SimpleActivityInput(
            DelaysParam.EVERY_DELAY,
            true
            );
        input = new SimpleActivityInput(
            DelaysParam.TUPLE_INPUT,
            false
            );
        output = new SimpleActivityOutput(
            DelaysParam.TUPLE_OUTPUT,
            false
            );
        }

    /**
     * Set the start delay.
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
     * Set the end delay.
     *
     */
    public void last(final Integer value)
        {
        if (value != null)
            {
            last.add(
                new IntegerData(
                    value
                    )
                );
            }
        }

    /**
     * Set the row delay.
     *
     */
    public void every(final Integer value)
        {
        if (value != null)
            {
            every.add(
                new IntegerData(
                    value
                    )
                );
            }
        }

    /**
     * Add the tuples input.
     * @param source The tuple input source.
     *
     */
    public void input(final SingleActivityOutput source)
        {
        input.connect(
            source
            );
        }

    /**
     * Get the tuples output.
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
    protected ActivityInput[] getInputs()
        {
        return new ActivityInput[]{
            first,
            last,
            every,
            input
            };
        }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ActivityOutput[] getOutputs()
        {
        return new ActivityOutput[]{
            output
            };
        }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateIOState()
    throws ActivityIOIllegalStateException
        {
        }
    }

