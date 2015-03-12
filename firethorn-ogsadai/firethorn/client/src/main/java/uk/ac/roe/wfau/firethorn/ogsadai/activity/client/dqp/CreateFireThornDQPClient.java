/*
 * Copyright (c) 2015, ROE (http://www.roe.ac.uk/)
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
package uk.ac.roe.wfau.firethorn.ogsadai.activity.client.dqp;

import uk.ac.roe.wfau.firethorn.ogsadai.activity.common.dqp.CreateFireThornDQPParam;
import uk.org.ogsadai.activity.ActivityName;
import uk.org.ogsadai.client.toolkit.ActivityOutput;
import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.activity.ActivityInput;
import uk.org.ogsadai.client.toolkit.activity.BaseActivity;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityInput;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityOutput;
import uk.org.ogsadai.client.toolkit.exception.ActivityIOIllegalStateException;
import uk.org.ogsadai.client.toolkit.exception.DataSourceUsageException;
import uk.org.ogsadai.client.toolkit.exception.DataStreamErrorException;
import uk.org.ogsadai.client.toolkit.exception.UnexpectedDataValueException;
import uk.org.ogsadai.data.StringData;
import uk.org.ogsadai.resource.ResourceID;

/**
 * An activity to create a FireThorn DQP resource.
 * 
 */
public class CreateFireThornDQPClient
extends BaseActivity
    {
    /**
     * Public interface for the Activity params.
     *
     */
    public interface Param
        {
        /**
         * Target resource(s).
         * 
         */
        public String target();
        }

    /**
     * Resource name input.
     *
     */
    private final ActivityInput input;
    
    /**
     * Resource name output.
     *
     */
    private final ActivityOutput output;

    /**
     * Public constructor.
     *
     */
    public CreateFireThornDQPClient(final Param param)
        {
        super(
            new ActivityName(
                CreateFireThornDQPParam.ACTIVITY_NAME
                )
            );
        input = new SimpleActivityInput(
            CreateFireThornDQPParam.TARGET_RESOURCES,
            SimpleActivityInput.REQUIRED
            );

        input.add(
            new StringData(
                param.target()
                )
            );
        
        output = new SimpleActivityOutput(
            CreateFireThornDQPParam.OUTPUT_ID
            );
        }
    
    /**
     * Adds a resource ID input.
     * 
     */
    public void add(final ResourceID resourceID)
        {
        input.add(
            new StringData(
                resourceID.toString()
                )
            );
        }
    
    /**
     * Connects the resource ID input to the given output.
     * 
     */
    public void connect(final SingleActivityOutput output)
        {
        input.connect(
            output
            );
        }
    
    /**
     * Get the activity's result output.
     * 
     */
    public SingleActivityOutput output()
        {
        return output.getSingleActivityOutputs()[0];
        }
    
    /**
     * Check if there are any unread output values.
     * 
     */
    public boolean hasNextResult() 
    throws DataStreamErrorException, 
           UnexpectedDataValueException, 
           DataSourceUsageException
        {
        return output.getDataValueIterator().hasNext();
        }
    
    /**
     * Get the next resource ID from the output.
     * 
     */
    public ResourceID getNextResult() 
    throws DataStreamErrorException, 
           UnexpectedDataValueException, 
           DataSourceUsageException
        {
        return new ResourceID(
            output.getDataValueIterator().nextAsString()
            );
        }

    @Override
    protected void validateIOState() throws ActivityIOIllegalStateException
        {
        }

    @Override
    protected ActivityInput[] getInputs()
        {
        return new ActivityInput[]
             {
             input
             };
        }

    @Override
    protected ActivityOutput[] getOutputs()
        {
        return new ActivityOutput[]
            {
            output
            };
        }
    }

