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

import uk.ac.roe.wfau.firethorn.ogsadai.activity.common.data.LimitsParam;
import uk.org.ogsadai.activity.ActivityName;
import uk.org.ogsadai.client.toolkit.Activity;
import uk.org.ogsadai.client.toolkit.ActivityOutput;
import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.activity.ActivityInput;
import uk.org.ogsadai.client.toolkit.activity.BaseActivity;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityInput;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityOutput;
import uk.org.ogsadai.client.toolkit.exception.ActivityIOIllegalStateException;
import uk.org.ogsadai.data.LongData;

/**
 * Client for our Limits Activity.
 *
 */
public class LimitsClient
extends BaseActivity implements Activity
    {

    /**
     * Public interface for the Activity parameters.
     *
     */
    public static interface Param
        {
        /**
         * The row limit.
         * @return The row limit.
         *
         */
        public Long rows();

        /**
         * The cells limit.
         * @return The cells limit.
         *
         */
        public Long cells();

        /**
         * The time limit.
         * @return The time limit.
         *
         */
        public Long time();
        }

    /**
     * The row limit.
     *
     */
    private final ActivityInput rows;

    /**
     * The cell limit.
     *
     */
    private final ActivityInput cells;

    /**
     * The time limit.
     *
     */
    private final ActivityInput time;
    
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
    public LimitsClient(final SingleActivityOutput source, final Param param)
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
    public LimitsClient(final Param param)
        {
        this();
        if (param != null)
            {
            this.rows(
                param.rows()
                );
            this.cells(
                param.cells()
                );
            this.time(
                param.time()
                );
            }
        }

    /**
     * Public constructor.
     *
     */
    public LimitsClient()
        {
        super(
            new ActivityName(
                LimitsParam.ACTIVITY_NAME
                )
            );
        rows = new SimpleActivityInput(
            LimitsParam.ROW_LIMIT,
            true
            );
        cells = new SimpleActivityInput(
            LimitsParam.CELL_LIMIT,
            true
            );
        time = new SimpleActivityInput(
            LimitsParam.TIME_LIMIT,
            true
            );
        input = new SimpleActivityInput(
            LimitsParam.TUPLE_INPUT,
            false
            );
        output = new SimpleActivityOutput(
            LimitsParam.TUPLE_OUTPUT,
            false
            );
        }

    /**
     * Set the row delay.
     *
     */
    public void rows(final Long value)
        {
        if (value != null)
            {
            rows.add(
                new LongData(
                    value
                    )
                );
            }
        }

    /**
     * Set the cells limit.
     *
     */
    public void cells(final Long value)
        {
        if (value != null)
            {
            cells.add(
                new LongData(
                    value
                    )
                );
            }
        }

    /**
     * Set the time limit.
     *
     */
    public void time(final Long value)
        {
        if (value != null)
            {
            time.add(
                new LongData(
                    value
                    )
                );
            }
        }

    /**
     * Connect the tuples input.
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
     * @return The tuples output
     *
     */
    public SingleActivityOutput output()
        {
        return output.getSingleActivityOutputs()[0];
        }

    @Override
    protected ActivityInput[] getInputs()
        {
        return new ActivityInput[]{
            rows,
            cells,
            time,
            input
            };
        }

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

