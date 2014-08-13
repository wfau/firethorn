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
         * The TAP service type, as a String.
         *
         */
        public String type();

        }

    /**
     * The TAP service endpoint URL, as a String.
     *
     */
    private final ActivityInput endpoint;

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

        this.results = new SimpleActivityOutput(
            IvoaCreateResourceParam.ACTIVITY_RESULTS
            );
        }
    
    @Override
    protected ActivityInput[] getInputs()
        {
        return new ActivityInput[]{
            endpoint
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
