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

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import uk.org.ogsadai.activity.ActivityName;
import uk.org.ogsadai.client.toolkit.ActivityOutput;
import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.Utilities;
import uk.org.ogsadai.client.toolkit.activity.ActivityInput;
import uk.org.ogsadai.client.toolkit.activity.BaseActivity;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityInput;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityOutput;
import uk.org.ogsadai.client.toolkit.exception.ActivityIOIllegalStateException;
import uk.org.ogsadai.client.toolkit.exception.DataSourceUsageException;
import uk.org.ogsadai.client.toolkit.exception.DataStreamErrorException;
import uk.org.ogsadai.client.toolkit.exception.UnexpectedDataValueException;
import uk.org.ogsadai.data.ListBegin;
import uk.org.ogsadai.data.ListEnd;
import uk.org.ogsadai.data.StringData;
import uk.org.ogsadai.resource.ResourceID;

import uk.ac.roe.wfau.firethorn.ogsadai.activity.common.dqp.CreateFTDQPParam ;

/**
 * An activity to create a FireThorn DQP resource.
 * 
 */
public class CreateFTDQPResource
extends BaseActivity
    {

    /**
     * Resource name input.
     *
     */
    private final ActivityInput mResourceIdInput;
    
    /**
     * Resource name output.
     *
     */
    private final ActivityOutput mResultOutput;

    /**
     * Public constructor.
     *
     */
    public CreateFTDQPResource()
        {
        super(
            new ActivityName(
                CreateFTDQPParam.ACTIVITY_NAME
                )
            );
        mResourceIdInput = new SimpleActivityInput(
            CreateFTDQPParam.INPUT_ID,
            SimpleActivityInput.OPTIONAL
            );
        mResultOutput = new SimpleActivityOutput(
            CreateFTDQPParam.OUTPUT_ID
            );
        }
    
    /**
     * Adds a resource ID input.
     * 
     * @param resourceID
     *     ID of the new resource.
     */
    public void addResourceId(final ResourceID resourceID)
        {
        mResourceIdInput.add(
            new StringData(
                resourceID.toString()
                )
            );
        }
    
    /**
     * Connects the resource ID input to the given output.
     * 
     * @param output 
     *     Output to connect the input to.
     */
    public void connectResourceIdInput(final SingleActivityOutput output)
        {
        mResourceIdInput.connect(
            output
            );
        }
    
    /**
     * Gets the activity's result output.  This method is use to
     * connect the output to other activities.
     * 
     * @return the activity's output.
     */
    public SingleActivityOutput getResultOutput()
        {
        return mResultOutput.getSingleActivityOutputs()[0];
        }
    
    /**
     * Gets whether or not there are any unread output values.
     * 
     * @return <code>true</code> if there are unread output value, 
     *         <code>false</code> if there are none.
     * @throws DataStreamErrorException 
     *     If there is an error on the data stream.
     * @throws UnexpectedDataValueException
     *     If there is an unexpected data value on the data stream.
     * @throws DataSourceUsageException
     *     If there is an error reading from a data source.
     */
    public boolean hasNextResult() 
    throws DataStreamErrorException, 
           UnexpectedDataValueException, 
           DataSourceUsageException
        {
        return mResultOutput.getDataValueIterator().hasNext();
        }
    
    /**
     * Gets the next resource ID from the output.
     * 
     * @return resource ID.
     * @throws DataStreamErrorException 
     *     If there is an error on the data stream.
     * @throws UnexpectedDataValueException
     *     If there is an unexpected data value on the data stream.
     * @throws DataSourceUsageException
     *     If there is an error reading from a data source.
     */
    public ResourceID nextResult() 
    throws DataStreamErrorException, 
           UnexpectedDataValueException, 
           DataSourceUsageException
        {
        return new ResourceID(
            mResultOutput.getDataValueIterator().nextAsString()
            );
        }
    
    /**
     * {@inheritDoc}
     *
     * No-op.
     */ 
    protected void validateIOState()
    throws ActivityIOIllegalStateException
        {
        // Empty method.
        }

    /**
     * {@inheritDoc}
     *
     */ 
    protected ActivityInput[] getInputs()
        {
        return new ActivityInput[]
            {
            mResourceIdInput
            };
        }

    /**
     * {@inheritDoc}
     */ 
    protected ActivityOutput[] getOutputs()
        {
        return new ActivityOutput[]{
            mResultOutput
            };
        }
    }

