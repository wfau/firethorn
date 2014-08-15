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
package uk.ac.roe.wfau.firethorn.ogsadai.activity.client.data;

import uk.ac.roe.wfau.firethorn.ogsadai.activity.common.data.RownumParam;
import uk.org.ogsadai.activity.ActivityName;
import uk.org.ogsadai.client.toolkit.Activity;
import uk.org.ogsadai.client.toolkit.ActivityOutput;
import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.activity.ActivityInput;
import uk.org.ogsadai.client.toolkit.activity.BaseActivity;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityInput;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityOutput;
import uk.org.ogsadai.client.toolkit.exception.ActivityIOIllegalStateException;
import uk.org.ogsadai.data.StringData;

/**
 * Client for our Rownum Activity.
 *
 */
public class RownumClient
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
         * The target column name.
         * @return The target column name.
         *
         */
        public String column();
        
        }

    /**
     * Activity input - column name.
     *
     */
    private final ActivityInput column;

    /**
     * The input tuples
     *
     */
    private final ActivityInput tuples;

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
    public RownumClient(final SingleActivityOutput source, final Param param)
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
    public RownumClient(final Param param)
        {
        this();
        if (param != null)
            {
            column(
                param.column()
                );
            }
        }
    
    /**
     * Public constructor.
     *
     */
    public RownumClient()
        {
        super(
            new ActivityName(
                RownumParam.ACTIVITY_NAME
                )
            );
        column = new SimpleActivityInput(
            RownumParam.COLUMN_NAME
            );
        tuples = new SimpleActivityInput(
            RownumParam.TUPLE_INPUT
            );
        output = new SimpleActivityOutput(
            RownumParam.TUPLE_OUTPUT
            );
        }

    /**
     * Set the column name.
     *
     */
    public void column(final String name)
        {
        column.add(
            new StringData(
                name
                )
            );
        }

    /**
     * Connect the tuple input.
     *
     */
    public void input(final SingleActivityOutput source)
        {
        tuples.connect(
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
    protected ActivityInput[] getInputs()
        {
        return new ActivityInput[]{
            column,
            tuples
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


