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
package uk.ac.roe.wfau.firethorn.ogsadai.activity.client.blue;

import uk.ac.roe.wfau.firethorn.ogsadai.activity.common.blue.CallbackParam;
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
 * Client for our CallbackActivity.
 * @todo Move a lot of this to a generic TupleProcessingActivity base class.
 * 
 *
 */
public class CallbackClient
extends BaseActivity implements Activity
    {

    /**
     * Public interface for the Activity parameters.
     *
     */
    public static interface Param
        {
		/**
		 * The query identifier.
		 * 
		 */
        public String ident();
        }

    /**
     * The query identifier.
     *
     */
    private final ActivityInput ident;

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
    public CallbackClient(final SingleActivityOutput source, final Param param)
        {
        this();
        param(
        	param
            );
        input(
            source
            );
        }

    /**
     * Public constructor.
     *
     */
    public CallbackClient()
        {
        super(
            new ActivityName(
        		CallbackParam.ACTIVITY_NAME
                )
            );
        this.ident = new SimpleActivityInput(
    		CallbackParam.QUERY_IDENT,
            false
            );
        this.input = new SimpleActivityInput(
    		CallbackParam.TUPLE_INPUT,
            false
            );
        this.output = new SimpleActivityOutput(
    		CallbackParam.TUPLE_OUTPUT,
            false
            );
        }

    /**
     * Set the Activity parameters. 
     * @param param The Activity parameters.
     * 
     */
    public void param(final Param param)
        {
        ident.add(
            new StringData(
                param.ident()
                )
            );
        }

    /**
     * Add the tuples input.
     * @param source The tuple input source.
     *
     */
    public void input(final SingleActivityOutput source)
        {
        this.input.connect(
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

    @Override
    protected ActivityInput[] getInputs()
        {
        return new ActivityInput[]{
            ident,
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

