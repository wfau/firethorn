/*
 *  Copyright (C) 2014 Royal Observatory, University of Edinburgh, UK
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package uk.ac.roe.wfau.firethorn.ogsadai.activity.client.ivoa;

import uk.ac.roe.wfau.firethorn.ogsadai.activity.common.ivoa.IvoaCreateResourceParam;
import uk.org.ogsadai.activity.ActivityName;
import uk.org.ogsadai.client.toolkit.Activity;
import uk.org.ogsadai.client.toolkit.ActivityOutput;
import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.activity.ActivityInput;
import uk.org.ogsadai.client.toolkit.activity.BaseActivity;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityInput;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityOutput;
import uk.org.ogsadai.client.toolkit.exception.ActivityIOIllegalStateException;
import uk.org.ogsadai.data.BooleanData;
import uk.org.ogsadai.data.IntegerData;
import uk.org.ogsadai.data.StringData;
import uk.org.ogsadai.resource.ResourceID;

/**
 * Client for the IvoaCreateResource Activity.
 *
 */
public class IvoaCreateResourceClient
extends BaseActivity implements Activity
    {
    /**
     * Public interface for the Activity params.
     *
     */
    public interface Param
        {
        /**
         * The TAP service endpoint URL, as a String.
         *
         */
        public String endpoint();

        /**
         * The UWS service quickstart flag, as a Boolean.
         *
         */
        public Boolean quickstart();
        
        /**
         * The UWS service poll interval, as an Integer.
         *
         */
        public Integer interval();
        
        /**
         * The UWS service timeout, as an Integer.
         *
         */
        public Integer timeout();

        }

    /**
     * Simple {@link Param}implementation.
     * 
     */
    public static class SimpleParam
    implements Param
        {
        public SimpleParam(final String endpoint)
            {
            this(
                Boolean.FALSE,
                endpoint,
                null,
                null
                );
            }
        public SimpleParam(final Boolean quickstart, final String endpoint)
            {
            this(
                quickstart,
                endpoint,
                null,
                null
                );
            }
        public SimpleParam(final Boolean quickstart, final String endpoint, final Integer interval, final Integer timeout)
            {
            this.endpoint   = endpoint;
            this.quickstart = quickstart;
            this.interval   = interval;
            this.timeout    = timeout;
            }
        
        private String endpoint;
        @Override
        public String endpoint()
            {
            return this.endpoint;
            }

        private Boolean quickstart;
        @Override
        public Boolean quickstart()
            {
            return this.quickstart;
            }

        private Integer interval;
        @Override
        public Integer interval()
            {
            return this.interval;
            }

        private Integer timeout;
        @Override
        public Integer timeout()
            {
            return this.timeout;
            }
        }
    
    /**
     * The TAP service endpoint URL, as a String.
     *
     */
    private final ActivityInput endpoint;

    /**
     * The UWS quickstart flag, as a Boolean.
     *
     */
    private final ActivityInput quickstart;

    /**
     * The UWS polling interval, as an Integer.
     *
     */
    private final ActivityInput interval;

    /**
     * The UWS polling timeout, as an Integer.
     *
     */
    private final ActivityInput timeout;

    /**
     * Our result output.
     *
     */
    private final ActivityOutput results;

    /**
     * Get our results output.
     * @return Our results output.
     *
     */
    public SingleActivityOutput result()
        {
        return results.getSingleActivityOutputs()[0];
        }

    /**
     * Public constructor.
     * @param param The Activity parameters.
     * 
     */
    public IvoaCreateResourceClient(final Param param)
        {
        super(
            new ActivityName(
                IvoaCreateResourceParam.ACTIVITY_NAME
                )
            );

        this.endpoint = new SimpleActivityInput(
            IvoaCreateResourceParam.IVOA_TAP_ENDPOINT,
            true
            );
        if (param.endpoint() != null)
            {
            this.endpoint.add(
                new StringData(
                    param.endpoint()
                    )
                );
            }

        this.quickstart= new SimpleActivityInput(
            IvoaCreateResourceParam.IVOA_UWS_QUICKSTART,
            true
            );
        if (param.quickstart() != null)
            {
            this.quickstart.add(
                new BooleanData(
                    param.quickstart()
                    )
                );
            }
        
        this.interval = new SimpleActivityInput(
            IvoaCreateResourceParam.IVOA_UWS_INTERVAL,
            true
            );
        if (param.interval() != null)
            {
            this.interval.add(
                new IntegerData(
                    param.interval()
                    )
                );
            }

        this.timeout = new SimpleActivityInput(
            IvoaCreateResourceParam.IVOA_UWS_TIMEOUT,
            true
            );
        if (param.timeout() != null)
            {
            this.timeout.add(
                new IntegerData(
                    param.timeout()
                    )
                );
            }
        
        this.results = new SimpleActivityOutput(
            IvoaCreateResourceParam.ACTIVITY_RESULTS
            );
        }
    
    @Override
    protected ActivityInput[] getInputs()
        {
        return new ActivityInput[]{
            endpoint,
            quickstart,
            interval,
            timeout
            };
        }

    @Override
    protected ActivityOutput[] getOutputs()
        {
        return new ActivityOutput[]{
            results
            };
        }

    @Override
    protected void validateIOState()
    throws ActivityIOIllegalStateException
        {
        }

    public ResourceID resource()
    throws Exception
        {
        return new ResourceID(
            this.results.getDataValueIterator().nextAsString()
            );
        }
    }
