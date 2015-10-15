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

import uk.ac.roe.wfau.firethorn.ogsadai.activity.common.blue.ContextParam;
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
import uk.org.ogsadai.data.StringData;

/**
 * Client for our ContextActivity.
 * 
 *
 */
public class ContextClient
extends BaseActivity
implements Activity
    {

    /**
     * Public interface for the Activity parameters.
     *
     */
    public static interface Param
        {
        /**
         * Get the protocol name.
         *
         */
        public String protocol();

    	/**
         * Get the host name.
         *
         */
        public String host();

        /**
         * Get the port number.
         *
         */
        public String port();
        
        /**
         * Get the base URL path.
         *
         */
        public String base();
        
        /**
		 * The query identifier.
		 * 
		 */
        public String ident();
        
        }

    /**
     * The protocol input.
     *
     */
    private final ActivityInput protocol;

    /**
     * The host name input.
     *
     */
    private final ActivityInput host;

    /**
     * The port number input.
     *
     */
    private final ActivityInput port;

    /**
     * The base URL path input.
     *
     */
    private final ActivityInput base;

    /**
     * The query identifier input.
     * 
     *
     */
    private final ActivityInput ident;

    /**
     * The pipeline input.
     * 
     *
     */
    private final ActivityInput input;

    /**
     * The pipeline output.
     *
     */
    private final ActivityOutput output;

    /**
     * Public constructor.
     * @param param The Activity parameters.
     * 
     */
    public ContextClient(final Param param)
        {
        this();
        this.param(
    		param
    		);
        }

    /**
     * Public constructor.
     *
     */
    public ContextClient()
        {
        super(
            new ActivityName(
        		ContextParam.ACTIVITY_NAME
                )
            );
        this.protocol = new SimpleActivityInput(
    		ContextParam.CALLBACK_PROTOCOL_INPUT,
            true
            );
        this.host = new SimpleActivityInput(
    		ContextParam.CALLBACK_HOST_INPUT,
            true
            );
        this.port = new SimpleActivityInput(
    		ContextParam.CALLBACK_PORT_INPUT,
            true
            );
        this.base = new SimpleActivityInput(
    		ContextParam.CALLBACK_BASE_INPUT,
            true
            );
        this.ident = new SimpleActivityInput(
    		ContextParam.CONTEXT_IDENT_INPUT,
            false
            );
        this.input = new SimpleActivityInput(
    		ContextParam.CONTEXT_PIPELINE_INPUT,
            false
            );
        this.output = new SimpleActivityOutput(
    		ContextParam.CONTEXT_PIPELINE_OUTPUT,
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
		if (param.protocol() != null)
			{
	        protocol.add(
	            new StringData(
	                param.protocol()
	                )
	            );
			}
		if (param.host() != null)
			{
	        host.add(
	            new StringData(
	                param.host()
	                )
	            );
			
			}
		if (param.port() != null)
			{
	        port.add(
	            new StringData(
	                param.port()
	                )
	            );
			
			}
		if (param.base() != null)
			{
	        base.add(
	            new StringData(
	                param.base()
	                )
	            );
			
			}
        ident.add(
            new StringData(
                param.ident()
                )
            );
        }

    /**
     * Set the pipeline inout. 
     * 
     */
    public void input(final String value)
    	{
    	input.add(
            new StringData(
                value
                )
			);
    	}

    /**
     * Get the pipeline output.
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
            protocol,
            host,
            port,
            base,
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
	protected void validateIOState() throws ActivityIOIllegalStateException
		{
		}
    }

