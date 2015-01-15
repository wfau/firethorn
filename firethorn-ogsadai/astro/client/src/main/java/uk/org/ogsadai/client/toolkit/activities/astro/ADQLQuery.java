// Copyright (c) The University of Edinburgh,  2010.
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


package uk.org.ogsadai.client.toolkit.activities.astro;

import uk.org.ogsadai.activity.ActivityName;
import uk.org.ogsadai.client.toolkit.ActivityOutput;
import uk.org.ogsadai.client.toolkit.ResourceActivity;
import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.activity.ActivityInput;
import uk.org.ogsadai.client.toolkit.activity.BaseResourceActivity;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityInput;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityOutput;
import uk.org.ogsadai.client.toolkit.exception.ActivityIOIllegalStateException;
import uk.org.ogsadai.data.StringData;

/**
 * An activity that executes an ADQL query against a TAP service. It
 * produces a list of tuples containing the results of the query.
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 * <li>
 * <code>expression</code>. Type: {@link java.lang.String}. ADQL query 
 * </li>
 * </ul>
 * <p>
 * Activity outputs:
 * </p>
 * <ul>
 * <li>
 * <code>data</code>. Type: OGSA-DAI list of 
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an 
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a 
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object.  The tuples produced
 * by the query.
 * </li>
 * </ul>
 * <p>
 * Configuration parameters: none.
 * </p>
 * <p>
 * Activity input/output ordering: none.
 * </p>
 * <p>
 * Activity contracts: 
 * </p>
 * <ul>
 * <li>
 * <code>uk.org.ogsadai.activity.contract.ADQLQuery</code>
 * </li>
 * </ul>
 * <p>
 * Target data resource:
 * <ul>
 * <li>
 * {@link uk.org.ogsadai.resource.generic.GenericResource}
 * </li>
 * </ul>
 * </p>
 * </p>
 * <p>
 * Behaviour: 
 * </p>
 * <ul>
 * <li>
 * This activity accepts a sequence of ADQL query expressions as input and is 
 * targeted at a TAP service. In each iteration one input query is
 * processed by executing the query across the target data resource. The results
 * of each iteration is a OGSA-DAI list of tuples with a metadata header block. 
 * </li>
 * <li>
 * Partial data may be produced if an error occurs at any stage of processing. 
 * </li>
 * </ul> 
 *
 * @author The OGSA-DAI Project Team.
 */
public class ADQLQuery extends BaseResourceActivity implements ResourceActivity
{
    /** Copyright notice */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2010.";

    /** Default activity name */
    private final static ActivityName DEFAULT_ACTIVITY_NAME = 
        new ActivityName("uk.org.ogsadai.ADQLQuery");
    
    /** Expression input name */
    public final static String EXPRESSION_INPUT = "expression";
    
    /** Data output name */
    public final static String DATA_OUTPUT = "data";
    
    /** Expression input */
    private final ActivityInput mExpressionInput;
    
    /** Data output */
    private final ActivityOutput mDataOutput;
    
    /**
     * Constructor.
     */
    public ADQLQuery()
    {
        super(DEFAULT_ACTIVITY_NAME);
        mExpressionInput = new SimpleActivityInput(EXPRESSION_INPUT);
        mDataOutput      = new SimpleActivityOutput(DATA_OUTPUT);
    }

    /**
     * Adds a new ADQL expression.
     * 
     * @param expression SQL expression.
     */
    public void addExpression(final String expression)
    {
        mExpressionInput.add(new StringData(expression));
    }
    
    /**
     * Connects the expression input to the given output.
     * 
     * @param output output to connect to.
     */
    public void connectExpressionInput(final SingleActivityOutput output)
    {
        mExpressionInput.connect(output);
    }
        
    /**
     * Gets the data output.  This output will contain the data returned
     * when the SQL query is executed.
     * 
     * @return data output.
     */
    public SingleActivityOutput getDataOutput()
    {
        return mDataOutput.getSingleActivityOutputs()[0];
    }

    /**
     * {@inheritDoc}
     */
    protected void validateIOState() throws ActivityIOIllegalStateException
    {
        // Empty method
    }
    
    /**
     * {@inheritDoc}
     */
    protected ActivityInput[] getInputs()
    {
        return new ActivityInput[]{ mExpressionInput };
    }

    /**
     * {@inheritDoc}
     */
    protected ActivityOutput[] getOutputs()
    {
        return new ActivityOutput[]{ mDataOutput };
    }
}
