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

import uk.ac.roe.wfau.firethorn.ogsadai.activity.common.RowDelayParam ;

/**
 * Client proxy for RowDelayActivity.
 *
 */
public class RowDelay
extends BaseActivity implements Activity
    {
    /**
     * Activity input - start delay.
     *
     */
    private final ActivityInput startDelay;

    /**
     * Activity input - end delay.
     *
     */
    private final ActivityInput endDelay;
    
    /**
     * Activity input - page size.
     *
     */
    private final ActivityInput pageSize;
    
    /**
     * Activity input - page delay.
     *
     */
    private final ActivityInput pageDelay;

    /**
     * Activity input - row delay.
     *
     */
    private final ActivityInput rowDelay;
    
    /**
     * Activity input - tuples.
     *
     */
    private final ActivityInput tupleInput;

    /**
     * Activity output - tuples.
     *
     */
    private final ActivityOutput tupleOutput;

    /**
     * Simple constructor with just a row delay.
     *
     */
    public RowDelay(final Integer delay)
        {
        this();
        this.setRowDelay(
            delay
            );
        }

    /**
     * Public constructor.
     *
     */
    public RowDelay()
        {
        super(
            new ActivityName(
                RowDelayParam.ACTIVITY_NAME
                )
            );
        startDelay = new SimpleActivityInput(
            RowDelayParam.START_DELAY_INPUT
            );
        endDelay   = new SimpleActivityInput(
            RowDelayParam.END_DELAY_INPUT
            );
        pageSize   = new SimpleActivityInput(
            RowDelayParam.PAGE_SIZE_INPUT
            );
        pageDelay  = new SimpleActivityInput(
            RowDelayParam.PAGE_DELAY_INPUT
            );
        rowDelay   = new SimpleActivityInput(
            RowDelayParam.ROW_DELAY_INPUT
            );
        tupleInput = new SimpleActivityInput(
            RowDelayParam.TUPLE_ITER_INPUT
            );
        tupleOutput = new SimpleActivityOutput(
            RowDelayParam.TUPLE_ITER_OUTPUT
            );
        }

    /**
     * Set the start delay.
     *
     */
    public void setStartDelay(final Integer value)
        {
        startDelay.add(
            new IntegerData(
                value
                )
            );
        }

    /**
     * Set the end delay.
     *
     */
    public void setEndDelay(final Integer value)
        {
        endDelay.add(
            new IntegerData(
                value
                )
            );
        }

    /**
     * Set the page size.
     *
     */
    public void setPageSize(final Integer value)
        {
        pageSize.add(
            new IntegerData(
                value
                )
            );
        }

    /**
     * Set the page delay.
     *
     */
    public void setpageDelay(final Integer value)
        {
        pageDelay.add(
            new IntegerData(
                value
                )
            );
        }

    /**
     * Set the row delay.
     *
     */
    public void setRowDelay(final Integer value)
        {
        rowDelay.add(
            new IntegerData(
                value
                )
            );
        }

    /**
     * Add the tuples input.
     *
     */
    public void connectDataInput(final SingleActivityOutput source)
        {
        tupleInput.connect(
            source
            );
        }

    /**
     * Get the tuples output.
     *
     */
    public SingleActivityOutput getDataOutput()
        {
        return tupleOutput.getSingleActivityOutputs()[0];
        }

    /**
     * Get the activity inputs.
     *
     */
    @Override
    protected ActivityInput[] getInputs()
        {
        return new ActivityInput[]{
            startDelay,
            endDelay,
            pageSize,
            pageDelay,
            rowDelay,
            tupleInput
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
            tupleOutput
            };
        }

    /**
     * Validate the inputs and outputs.
     *
     */
    @Override
    protected void validateIOState()
    throws ActivityIOIllegalStateException
        {
        }
    }


