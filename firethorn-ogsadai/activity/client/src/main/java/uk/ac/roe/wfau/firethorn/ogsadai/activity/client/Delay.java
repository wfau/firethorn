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

import uk.ac.roe.wfau.firethorn.ogsadai.activity.common.DelayParam ;

/**
 * Client proxy for RowDelayActivity.
 *
 */
public class Delay
extends BaseActivity implements Activity
    {
    /**
     * Public interface for the Activity parameters.
     *
     */
    public static interface Param
        {
        public Integer first();
        public Integer last();
        public Integer every();
        }

    /**
     * Activity input - start delay.
     *
     */
    private final ActivityInput first;

    /**
     * Activity input - end delay.
     *
     */
    private final ActivityInput last;

    /**
     * Activity input - row delay.
     *
     */
    private final ActivityInput every;
    
    /**
     * Activity input - tuples.
     *
     */
    private final ActivityInput input;

    /**
     * Activity output - tuples.
     *
     */
    private final ActivityOutput output;

    /**
     * Simple constructor with just a row delay.
     *
     */
    public Delay(final Param param)
        {
        this();
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

    /**
     * Public constructor.
     *
     */
    public Delay()
        {
        super(
            new ActivityName(
                DelayParam.ACTIVITY_NAME
                )
            );
        first = new SimpleActivityInput(
            DelayParam.DELAY_FIRST_INPUT,
            true
            );
        last = new SimpleActivityInput(
            DelayParam.DELAY_LAST_INPUT,
            true
            );
        every = new SimpleActivityInput(
            DelayParam.DELAY_EVERY_INPUT,
            true
            );
        input = new SimpleActivityInput(
            DelayParam.TUPLE_ITER_INPUT,
            false
            );
        output = new SimpleActivityOutput(
            DelayParam.TUPLE_ITER_OUTPUT,
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
     * Get the activity inputs.
     *
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
     * Get the activity outputs.
     *
     */
    @Override
    protected ActivityOutput[] getOutputs()
        {
        return new ActivityOutput[]{
            output
            };
        }

    @Override
    protected void validateIOState()
    throws ActivityIOIllegalStateException
        {
        }
    }


