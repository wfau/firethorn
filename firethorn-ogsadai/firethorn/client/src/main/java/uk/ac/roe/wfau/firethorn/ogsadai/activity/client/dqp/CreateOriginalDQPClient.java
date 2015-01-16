// Copyright (c) The University of Edinburgh, 2007-2008.
//
// LICENCE-START
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// LICENCE-END

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

/**
 * An activity that creates a DQP data resource based upon a resource template
 * available on the server.
 * <p>
 * Activity inputs:
 * <ul>
 * <li>
 * <code>resourceId</code>. Type: {@link java.lang.String}. The ID for the new
 * resource. This is an optional input. If omitted then the server will generate
 * a new unique ID. If provided then the value must be parsable into a valid
 * {@link uk.org.ogsadai.resource.ResourceID} and must not already be assigned
 * to a server-side resource.</li>
 * <li>
 * <code>templateId</code>. Type: {@link java.lang.String}. The ID for the
 * server-side resource template to use as a basis for the new resource. This is
 * an optional input. If omitted then the server will use the default template
 * ID specified as part of the activity configuration. If provided then the
 * value must be parsable into a valid
 * {@link uk.org.ogsadai.resource.ResourceID}.</li>
 * <li>
 * <code>configuration</code>. Type: OGSA-DAI list of <code>char[]</code>. The configuration
 * for the new DQP resource. The value must be well-formed
 * <code>DQPResourceConfig</code> XML document.</li>
 * </li>
 * </ul>
 * <p>
 * Activity outputs:
 * <ul>
 * <li>
 * <code>result</code>. Type: {@link java.lang.String}. ID of the new resource.
 * This will be parsable into a {@link uk.org.ogsadai.resource.ResourceID}.</li>
 * </ul>
 * <p>
 * Expected name of activity on server:
 * </p>
 * <ul>
 * <li>
 * <code>uk.org.ogsadai.CreateDQPResource</code></li>
 * </ul>
 * <p>
 * Activity input/output ordering: none.
 * <p>
 * Activity contracts: none.
 * <p>
 * Target data resource: none.
 * <p>
 * Behaviour:
 * <ul>
 * <li>
 * If a resource ID hasn't been provided then the activity auto-generates a
 * unique one.</li>
 * <li>
 * If a template ID hasn't been provided then the activity gets the default one
 * specified in its configuration.</li>
 * <li>
 * It attempts to load the given resource template. If the template cannot be
 * found and the ID was provided by the client then this error is returned to
 * the client. If the default template ID is being used or any other loading
 * problems arise then a server-side error is logged.</li>
 * <li>
 * It then creates a resource using this template and checks that the resulting
 * resource implements <code>DQPResource</code>. If there is a creation problem
 * or the check fails then a server-side error is logged.</li>
 * <li>
 * It adds the resource to the resource manager.</li>
 * <li>
 * The ID of the new resource is then output.</li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team.
 */
public class CreateOriginalDQPClient extends BaseActivity
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2007-2008.";

    /**
     * Activity input name (<code>resourceId</code>)
     * - ID of the new resource to create (<code>String</code>). 
     */
    public static final String INPUT_ID = "resourceId";

    /**
     * Activity input name (<code>resourceId</code>)
     * - ID of resource template (<code>String</code>). 
     */
    public static final String INPUT_TEMPLATE_ID = "templateId";

    /**
     * Activity input name (<code>resourceId</code>)
     * - ID of resource template (<code>String</code>). 
     */
    public static final String INPUT_CONFIG = "configuration";

    /**
     * Activity output name (<code>result</code>)
     * - ID of the newly created resource(<code>String</code>). 
     */
    public static final String OUTPUT_RESULT = "result";

    /** Resource name input. */
    private final ActivityInput mResourceIdInput;
    
    /** Resource template name input. */
    private final ActivityInput mTemplateIdInput;
    
    /** Resource configuration input. */
    private final ActivityInput mConfigurationInput;
    
    /** Resource name output. */
    private final ActivityOutput mResultOutput;

    /** Default activity name. */
    private final static ActivityName DEFAULT_ACTIVITY_NAME = 
        new ActivityName("uk.org.ogsadai.CreateOriginalDQP");

    /** Block size for sending the blocks. */
    public static final int BLOCK_SIZE = 2048;
    
    /**
     * Constructor.
     */
    public CreateOriginalDQPClient()
    {
        super(DEFAULT_ACTIVITY_NAME);
        mResourceIdInput = new SimpleActivityInput(
            INPUT_ID, SimpleActivityInput.OPTIONAL);
        mTemplateIdInput = new SimpleActivityInput(
            INPUT_TEMPLATE_ID, SimpleActivityInput.OPTIONAL);
        mConfigurationInput = new SimpleActivityInput(
                INPUT_CONFIG, SimpleActivityInput.OPTIONAL);
        mResultOutput = new SimpleActivityOutput(OUTPUT_RESULT);
    }
    
    /**
     * Adds a resource ID input.
     * 
     * @param resourceID
     *     ID of the new resource.
     */
    public void addResourceId(final ResourceID resourceID)
    {
        mResourceIdInput.add(new StringData(resourceID.toString()));
    }
    
    /**
     * Connects the resource ID input to the given output.
     * 
     * @param output 
     *     Output to connect the input to.
     */
    public void connectResourceIdInput(final SingleActivityOutput output)
    {
        mResourceIdInput.connect(output);
        
    }
    
    /**
     * Adds a template ID input.
     * 
     * @param templateID
     *     Resource template ID.
     */
    public void addTemplateId(final ResourceID templateID)
    {
        mTemplateIdInput.add(new StringData(templateID.toString()));
    }
    
    /**
     * Connects the template ID input to the given output.
     * 
     * @param output 
     *     Output to connect the input to.
     */
    public void connectTemplateIdInput(final SingleActivityOutput output)
    {
        mTemplateIdInput.connect(output);
        
    }
    
    /**
     * Adds a resource configuration.
     * 
     * @param configuration
     *     Resource configuration
     * @throws IOException
     *      When there is a problem reading data.
     */
    public void addConfiguration(final String configuration) throws IOException
    {
        addConfiguration(new StringReader(configuration));
    }
    
    /**
     * Adds a resource configuration provided by a <code>java.io.Reader</code>.
     * 
     * @param reader
     *      Reader with XML data.
     * @throws IOException
     *      When there is a problem reading data.
     */
    public void addConfiguration(final Reader reader) throws IOException
    {
        mConfigurationInput.add(ListBegin.VALUE);
        Utilities.readCharData(mConfigurationInput, reader,BLOCK_SIZE);
        mConfigurationInput.add(ListEnd.VALUE);
    }
    
    
    /**
     * Connects the template ID input to the given output.
     * 
     * @param output 
     *     Output to connect the input to.
     */
    public void connectConfigurationInput(final SingleActivityOutput output)
    {
        mConfigurationInput.connect(output);
        
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
        String resourceID = mResultOutput.getDataValueIterator().nextAsString();
        return new ResourceID(resourceID);
    }
    
    /**
     * {@inheritDoc}
     *
     * No-op.
     */ 
    protected void validateIOState() throws ActivityIOIllegalStateException
    {
        // Empty method.
    }

    /**
     * {@inheritDoc}
     */ 
    protected ActivityInput[] getInputs()
    {
        return new ActivityInput[]{mResourceIdInput, mTemplateIdInput, mConfigurationInput};
    }

    /**
     * {@inheritDoc}
     */ 
    protected ActivityOutput[] getOutputs()
    {
        return new ActivityOutput[]{mResultOutput};
    }
}
