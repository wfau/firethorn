// Copyright (c) The University of Edinburgh,  2010-2012.
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

package uk.org.ogsadai.activity.astro;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import uk.ac.starlink.table.OnceRowPipe;
import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.votable.VOTableBuilder;
import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.MatchedIterativeActivity;
import uk.org.ogsadai.activity.astro.util.TupleUtilities;
import uk.org.ogsadai.activity.extension.ActivityInitialisationException;
import uk.org.ogsadai.activity.extension.ConfigurableActivity;
import uk.org.ogsadai.activity.extension.ResourceActivity;
import uk.org.ogsadai.activity.extension.ServiceAddresses;
import uk.org.ogsadai.activity.extension.ServiceAddressesActivity;
import uk.org.ogsadai.activity.io.ActivityInput;
import uk.org.ogsadai.activity.io.ActivityPipeProcessingException;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.activity.io.TypedActivityInput;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.config.ConfigurationValueMissingException;
import uk.org.ogsadai.config.Key;
import uk.org.ogsadai.config.KeyValueProperties;
import uk.org.ogsadai.resource.ResourceAccessor;
import uk.org.ogsadai.resource.generic.GenericResourceAccessor;

/**
 * An activity that executes an ADQL query against a TAP service. It produces a
 * list of tuples containing the results of the query.
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 * <li>
 * <code>expression</code>. Type: {@link java.lang.String}. ADQL query</li>
 * </ul>
 * <p>
 * Activity outputs:
 * </p>
 * <ul>
 * <li>
 * <code>data</code>. Type: OGSA-DAI list of {@link uk.org.ogsadai.tuple.Tuple}
 * with the first item in the list an instance of
 * {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object. The tuples produced by the
 * query.</li>
 * </ul>
 * <p>
 * Configuration parameters:
 * </p>
 * <ul>
 * <li>
 * <code>tap.parameter.XXXXX</code>. Any parameter with the prefix is added as a
 * TAP request parameter with the provided key and value. These parameters are
 * also read from the resource configuration if provided. See below for an
 * example.</li>
 * </ul>
 * <p>
 * Activity input/output ordering: none.
 * </p>
 * <p>
 * Activity contracts:
 * </p>
 * <ul>
 * <li>
 * <code>uk.org.ogsadai.activity.contract.ADQLQuery</code></li>
 * </ul>
 * <p>
 * Target data resource:
 * <ul>
 * <li>
 * {@link uk.org.ogsadai.resource.generic.GenericResource}</li>
 * </ul>
 * </p>
 * </p>
 * <p>
 * Behaviour:
 * </p>
 * <ul>
 * <li>
 * This activity accepts a sequence of ADQL query expressions as input and is
 * targeted at a TAP service. In each iteration one input query is processed by
 * executing the query across the target data resource. The results of each
 * iteration is a OGSA-DAI list of tuples with a metadata header block.</li>
 * <li>
 * Partial data may be produced if an error occurs at any stage of processing.</li>
 * <li>
 * The following TAP request parameters are configured by default:
 * <ul>
 * <li>REQUEST=doQuery</li>
 * <li>VERSION=1.0</li>
 * <li>LANG=ADQL</li>
 * </ul>
 * These values can be overwritten by specifying new values in the resource or
 * activity configuration.</li>
 * <li>
 * The order in which configuration parameters are read is as follows:
 * <ol>
 * <li>Default parameter values (see above).</li>
 * <li>Resource configuration.</li>
 * <li>Activity configuration.</li>
 * </ol>
 * Any parameter values provided overwrites previously defined ones. For
 * example, if the VERSION parameter is specified in the resource configuration,
 * it overrides the default. Similarly, if a VERSION parameter is provided in
 * the activity configuration, it overrides the resource config value.</li>
 * <li>Configuration example: This example shows how to configure the activity
 * or the resource with a FORMAT parameter to retrieve results in binary format 
 * from a DSA server. (Note that this FORMAT parameter only applies to DSA TAP
 * implementations.)
 * 
 * <pre>
 * CONFIG
 * tap.parameter.FORMAT=application/x-votable+xml; encoding="BINARY"
 * END
 * </pre>
 * 
 * </li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team.
 */
public class ADQLQueryActivity
    extends MatchedIterativeActivity
    implements ResourceActivity, ServiceAddressesActivity, ConfigurableActivity
{
    
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh 2010-2012.";
    
    /** Logger object for logging in this class. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(ADQLQueryActivity.class);
    
    /** Activity input name - ADQL expression. */
    public static final String INPUT_ADQL_EXPRESSION = "expression";
    
    /** Activity output name - is a list of tuples. */
    public static final String OUTPUT_SQL_RESULTS = "data";
    
    /** 
     * TAP service URL is looked up from the resource configuration file
     * using this key.
     */
    public static final String TAP_URL_KEY = "dai.astro.tapurl";
    
    /** Prefix for TAP request parameters. */
    public static final String PREFIX_TAP_REQUEST_PARAMETER = "tap.parameter";

    /** 
     * Expression rewrite key regular expression.  Used to retrieve the keys
     * that specify the query expression rewrites.
     */
    public static final String EXPRESSION_REWRITE_KEY_REGEXP = 
        "dai\\.expression\\.rewrite.*";

    /** The resource this activity is targeted at. */
    private ResourceAccessor mResource;
    
    /** Resource settings. */
    private KeyValueProperties mSettings;
    
    /** Service addresses. */
    private ServiceAddresses mServiceAddresses;

    /** HttpClient used to contact with TAP service. */
    private HttpClient mClient;
    
    private List<String> mExpressionRewrites = new LinkedList<String>();

    /** Parameters for the HTTP POST request. */
    private Map<String, String> mParameters = new HashMap<String, String>();

    /** Configuration properties of the activity. */
    private KeyValueProperties mProperties;

    private String mTAPServiceURL;
    
    /**
     * Constructor.
     */
    public ADQLQueryActivity() {
        super();
        mClient = new DefaultHttpClient();
        // default TAP request parameters
        mParameters.put("REQUEST", "doQuery");
        mParameters.put("VERSION", "1.0");
        mParameters.put("LANG", "ADQL");
    }
    
    /**
     * {@inheritDoc}
     */
    public Class<GenericResourceAccessor> getTargetResourceAccessorClass() 
    {
        return GenericResourceAccessor.class;
    }

    /**
     * {@inheritDoc}
     */
    public void setTargetResourceAccessor(final ResourceAccessor targetResource) 
    {
        mResource = targetResource;
    }
    
    /**
     * {@inheritDoc}
     */
    public void setServiceAddresses(ServiceAddresses serviceAddresses)
    {
        mServiceAddresses = serviceAddresses;
    }
    
    /**
     * {@inheritDoc}
     */
    protected ActivityInput[] getIterationInputs() {
        return new ActivityInput[] { 
            new TypedActivityInput(INPUT_ADQL_EXPRESSION, String.class),
        };
    }

    /**
     * {@inheritDoc}
     */
    protected void preprocess() throws ActivityUserException,
        ActivityProcessingException, ActivityTerminatedException 
    {
        validateOutput(OUTPUT_SQL_RESULTS);
        mSettings = mResource.getResource().getState().getConfiguration();
   
        Key[] expressionRewriteKeys = 
            mSettings.getKeysRegexp(EXPRESSION_REWRITE_KEY_REGEXP);
            
        for( Key key : expressionRewriteKeys)
        {
            mExpressionRewrites.add((String)mSettings.get(key));            
        }
        
        // add the properties defined in the resource config
        // overwrites the default parameters if they are redefined
        for (Key key : mSettings.getKeys())
        {
            if (PREFIX_TAP_REQUEST_PARAMETER.equals(key.getNamespace()))
            {
                String parameter = key.getLocalPart();
                String value = (String)mSettings.get(key);
                mParameters.put(parameter, value);
            }
        }

        // add the properties defined in the activity config
        // overwrites any parameters that are redefined
        for (Key key : mProperties.getKeys())
        {
            if (PREFIX_TAP_REQUEST_PARAMETER.equals(key.getNamespace()))
            {
                String parameter = key.getLocalPart();
                String value = (String)mProperties.get(key);
                mParameters.put(parameter, value);
            }
        }

        // Get TAP service URL
        Key tapURLKey = new Key(TAP_URL_KEY);
        if (mSettings.containsKey(tapURLKey))
        {
            mTAPServiceURL = (String)mSettings.get(tapURLKey);
        }
        else 
        {
            throw new ActivityProcessingException(
                new ConfigurationValueMissingException(TAP_URL_KEY));
        }
}
    
    /**
     * {@inheritDoc}
     */
    protected void processIteration(final Object[] iterationData)
        throws ActivityProcessingException,
               ActivityTerminatedException,
               ActivityUserException 
    {
        String expression = (String) iterationData[0];
        
        for (String rewrite : mExpressionRewrites)
        {
            String[] parts = rewrite.split(":");
            
            if (parts.length == 2)
            {
                expression = expression.replace(parts[0], parts[1]);
            }
        }
        
        if (LOG.isDebugEnabled())
        {
            LOG.debug("ADQL QUERY: " + expression);
            LOG.debug("TAP URL: " + mTAPServiceURL);
        }

        InputStream result = null;
        Thread streamThread = null;
        try {
            BlockWriter writer = getOutput();
            HttpPost httpPost = prepareHttpPost(mTAPServiceURL, expression);
            result = getStreamFromRequest(httpPost);
            final InputStream finalResult = result;
            final VOTableBuilder builder = new VOTableBuilder();
            final OnceRowPipe sink = new OnceRowPipe();
            streamThread = new Thread()
            {
                public void run() {
                    try {
                        builder.streamStarTable(finalResult, sink, null);
                    }
                    catch(IOException e) {
                        sink.setError(e);
                    }
                }
            };
            streamThread.start();
            
            StarTable star = sink.waitForStarTable();
            TupleUtilities.createTupleList(
                    star, mResource.getResource(),
                    mServiceAddresses.getDataRequestExecutionService(),
                    true, true, writer);
        }
        catch (PipeClosedException e) {
            // no more output wanted so we can finish processing
            iterativeStageComplete();
        }
        catch (PipeIOException e) {
            throw new ActivityPipeProcessingException(e);
        }
        catch (PipeTerminatedException e) {
            throw new ActivityTerminatedException();
        }
        catch (IOException e) {
            // Errors in the query sent to TAP come back as IOExceptions.
            // May these as user exceptions to the error reaches the client
            throw new ActivityUserException(e);
        }
        catch (Throwable e) {
            throw new ActivityProcessingException(e);
        }
        finally
        {
            if (streamThread != null)
            {
                try
                {
                    streamThread.join();
                }
                catch(Throwable t)
                {
                    // Ignore this
                }
            }
            
            // Close the input stream
            if (result != null)
            {
                try
                {
                    result.close();
                }
                catch(Throwable t)
                {
                    // Just gobble this
                }
            }
        }

    }
    
    /**
     * {@inheritDoc}
     */
    protected void postprocess() 
        throws ActivityUserException,
               ActivityProcessingException, 
               ActivityTerminatedException 
    {
        //no post-processing
    }
    
    /**
     * {@inheritDoc}
     */
    protected void cleanUp() throws Exception {
        super.cleanUp();
    }
    
    /**
     * Returns a stream for a given http URL.
     * 
     * @param url
     *     http URL
     * @return
     *     stream
     * @throws ClientProtocolException
     * @throws IOException
     * @throws ActivityProcessingException
     */
    private InputStream getStreamFromRequest(HttpPost httpPost)
        throws ClientProtocolException, IOException,
               ActivityProcessingException 
    {
        HttpResponse response;
        response = mClient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        try 
        {
            return new BufferedInputStream(entity.getContent());
        }
        catch (NullPointerException e) 
        {
            throw new ActivityProcessingException(e);
        }
    }
    
    /**
     * Prepares the HTTP post request to a TAP service with the ADQL query.
     * 
     * @param tapURL
     *     TAP service base URL
     * @param adql
     *     ADQL query
     * @return
     * @throws UnsupportedEncodingException 
     */
    private HttpPost prepareHttpPost(final String tapURL, final String adql) 
        throws UnsupportedEncodingException
    {
        HttpPost post = new HttpPost(tapURL);
        List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
        for (Entry<String, String> entry : mParameters.entrySet())
        {
            if (LOG.isDebugEnabled())
            {
                LOG.debug("TAP PARAMETER: " + entry.getKey() + 
                        "=" + entry.getValue());
            }
            postParameters.add(
                    new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        postParameters.add(new BasicNameValuePair("QUERY", adql));
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
        post.setEntity(formEntity);
        return post;
    }

    @Override
    public void configureActivity(KeyValueProperties properties)
            throws ActivityInitialisationException
    {
        mProperties = properties;
    }

}
