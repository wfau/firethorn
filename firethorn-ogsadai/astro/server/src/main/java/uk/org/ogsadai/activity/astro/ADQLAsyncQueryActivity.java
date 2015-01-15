// Copyright (c) The University of Edinburgh,  2012-2013.
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
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
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
 * An activity that executes an asynchronous ADQL query against a TAP service.
 * The query is executed asynchronously which means that the activity waits 
 * until the UWS job has completed.
 * It produces a list of tuples containing the results of the query.
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
public class ADQLAsyncQueryActivity
    extends MatchedIterativeActivity
    implements ResourceActivity, ServiceAddressesActivity, ConfigurableActivity
{
    
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh 2012-2013.";
    
    /** Logger object for logging in this class. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(ADQLAsyncQueryActivity.class);
    
    /** Activity input name - ADQL expression. */
    public static final String INPUT_ADQL_EXPRESSION = "expression";
    
    /** Activity output name - is a list of tuples. */
    public static final String OUTPUT_SQL_RESULTS = "data";
    
    /** 
     * TAP service URL is looked up from the resource configuration file
     * using this key.
     */
    public static final Key TAP_URL_KEY = new Key("dai.astro.tapurl");
    
    public static final Key UWS_POLL_INTERVAL_KEY = new Key("dai.astro.poll.interval");
    public static final Key UWS_POLL_TIMEOUT_KEY = new Key("dai.astro.poll.timeout");
    
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
    
    /** URL of the TAP service. */
    private String mTAPServiceURL;

    /** Delay in milliseconds between requests when polling for query status. */
    private long mPollInterval = 500;
    /** Timeout in milliseconds when polling for query status.
     *  No timeout if the value is <= 0. */
    private long mPollTimeout = 0;
    private ExecutorService mService = Executors.newSingleThreadExecutor();
    
    /**
     * Constructor.
     */
    public ADQLAsyncQueryActivity() {
        super();
        mClient = new DefaultHttpClient();
        // default TAP request parameters
        mParameters.put("REQUEST", "doQuery");
        mParameters.put("VERSION", "1.0");
        mParameters.put("LANG", "ADQL");
    }
    
    @Override
    public Class<GenericResourceAccessor> getTargetResourceAccessorClass() 
    {
        return GenericResourceAccessor.class;
    }

    @Override
    public void setTargetResourceAccessor(final ResourceAccessor targetResource) 
    {
        mResource = targetResource;
    }
    
    @Override
    public void setServiceAddresses(ServiceAddresses serviceAddresses)
    {
        mServiceAddresses = serviceAddresses;
    }
    
    @Override
    protected ActivityInput[] getIterationInputs() 
    {
        return new ActivityInput[] { 
            new TypedActivityInput(INPUT_ADQL_EXPRESSION, String.class),
        };
    }

    @Override
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
        if (mSettings.containsKey(TAP_URL_KEY))
        {
            mTAPServiceURL = (String)mSettings.get(TAP_URL_KEY);
        }
        else 
        {
            throw new ActivityProcessingException(
                new ConfigurationValueMissingException(TAP_URL_KEY));
        }
        
        // Use delay interval between status requests if configured
        if (mSettings.containsKey(UWS_POLL_INTERVAL_KEY))
        {
            mPollInterval = Long.valueOf(
                    (String)mSettings.get(UWS_POLL_INTERVAL_KEY));
        }
        // Use timeout for job completion if configured
        if (mSettings.containsKey(UWS_POLL_INTERVAL_KEY))
        {
            mPollTimeout = Long.valueOf((String)mSettings.get(UWS_POLL_TIMEOUT_KEY));
        }
    }
    
    @Override
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

        String asyncTAPServiceURL = mTAPServiceURL + "async";
        InputStream result = null;
        try 
        {
            String jobID = submitJob(asyncTAPServiceURL, expression);
            
            String jobServiceURL = asyncTAPServiceURL + "/" + jobID;

            // start job: post PHASE=RUN
            startJob(jobServiceURL);
            
            // check for errors
            String state = waitForJobComplete(jobServiceURL);
            LOG.debug("Job completed. Status: " + state);
            
            // wait for results
            result = getResults(jobServiceURL);
                        
        }       
        catch (ActivityProcessingException e)
        {
            throw e;
        }
        catch (ActivityUserException e)
        {
            throw e;
        }
        catch (Throwable e) 
        {
            throw new ActivityProcessingException(e);
        }
        
        Thread streamThread = null;
        try
        {
            // read results and transform into tuples
            final InputStream finalResult = result;
            final VOTableBuilder builder = new VOTableBuilder();
            final OnceRowPipe sink = new OnceRowPipe();
            streamThread = new Thread()
            {
                public void run() 
                {
                 
                    try 
                    {
                        builder.streamStarTable(finalResult, sink, null);
                    }
                    catch(IOException e)
                    {
                        sink.setError(e);
                    }
                }
            };
            streamThread.start();
            
            StarTable star = sink.waitForStarTable();
            BlockWriter writer = getOutput();
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
        catch (IOException e)
        {
            // Errors in the query sent to TAP come back as IOExceptions.
            // Wrap these as user exceptions so the error reaches the client
            throw new ActivityUserException(e);
        } 
        catch (SQLException e) 
        {
            throw new ActivityUserException(e);
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
            
            // Try and close the input stream
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
    
    private String submitJob(String asyncTAPServiceURL, String expression) 
    throws ClientProtocolException, IOException,  
           ActivityProcessingException, ActivityUserException 
    {
        HttpPost httpPost = prepareHttpPost(asyncTAPServiceURL, expression);
        LOG.debug("Submitting job to " + asyncTAPServiceURL);
        String jobResult = readInputStream(getStreamFromRequest(httpPost));
        LOG.debug(jobResult);
        UWSJobStatus job = UWSJobStatus.parseJob(jobResult);
        String jobID = job.getJobID();
        if (jobID == null)
        {
            throw new ActivityUserException(
                    new QueryFailedException(
                            "Failed to submit job: " + jobResult));
        }
        LOG.debug("job id = " + jobID);
        return jobID;
    }

    private void startJob(String jobServiceURL)
    throws ClientProtocolException, ActivityProcessingException, IOException
    {
        HttpPost startJob = new HttpPost(jobServiceURL + "/phase");
        List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
        postParameters.add(new BasicNameValuePair("PHASE", "RUN"));
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
        startJob.setEntity(formEntity);
        LOG.debug("Starting job.");
        String jobResult = readInputStream(getStreamFromRequest(startJob));
        LOG.debug(jobResult);
    }
    
    private String waitForJobComplete(final String jobServiceURL)
    throws ClientProtocolException, 
           IOException,
           ActivityUserException, 
           ActivityProcessingException, 
           ActivityTerminatedException 
    {
        final String url = jobServiceURL + "/phase";
        
        Callable<String> checkStatus = new Callable<String>() 
        {

            @Override
            public String call()
                    throws ClientProtocolException,
                    IOException, 
                    ActivityUserException,
                    ActivityProcessingException, 
                    ActivityTerminatedException 
            {
                String phase = null;
                // we can't read the results before the job is completed
                // so wait until the job has finished executing
                do 
                {
                    try
                    {
                        Thread.sleep(mPollInterval);
                    } 
                    catch (InterruptedException e) 
                    {
                        Thread.currentThread().interrupt();
                        throw new ActivityTerminatedException();
                    }
                    HttpGet getPhase = new HttpGet(url);
                    InputStream input = getStreamFromRequest(getPhase);
                    phase = readInputStream(input);
                }
                while (isPhase(phase, 
                        UWSJobStatus.PHASE_EXECUTING,
                        UWSJobStatus.PHASE_PENDING,
                        UWSJobStatus.PHASE_QUEUED));
                
                // reject any job that hasn't completed successfully
                if (!isPhase(phase, UWSJobStatus.PHASE_COMPLETED))
                {
                    HttpGet job = new HttpGet(jobServiceURL);
                    InputStream input = getStreamFromRequest(job);
                    try
                    {
                        UWSJobStatus status = UWSJobStatus.parseJob(input);
                        throw new ActivityUserException(
                                new QueryFailedException(status.getErrorSummary()));
                    }
                    catch (IllegalArgumentException e)
                    {
                        // XML cannot be parsed
                        throw new ActivityUserException(
                                new QueryFailedException("Unknown error."));
                    }
                }
                return phase;
            }
        };
        Future<String> future = mService.submit(checkStatus);
        try
        {
            LOG.debug("Waiting for job to complete");   
            LOG.debug("Poll interval: " + mPollInterval + "ms");
            if (mPollTimeout > 0)
            {
                LOG.debug("Timeout: " + mPollTimeout + "ms");
                return future.get(mPollTimeout, TimeUnit.MILLISECONDS);
            }
            else
            {
                LOG.debug("No timeout.");
                return future.get();
            }
        } 
        catch (InterruptedException e) 
        {
            throw new ActivityTerminatedException();
        }
        catch (ExecutionException e) 
        {
            if (e.getCause() instanceof ActivityUserException)
            {
                throw (ActivityUserException)e.getCause();
            }
            if (e.getCause() instanceof ActivityProcessingException)
            {
                throw (ActivityProcessingException)e.getCause();
            }
            if (e.getCause() instanceof ActivityTerminatedException)
            {
                throw (ActivityTerminatedException)e.getCause();
            }
            throw new ActivityProcessingException(e.getCause());
        } 
        catch (TimeoutException e) 
        {
            throw new ActivityUserException(e);
        }
        finally
        {
            future.cancel(true);
        }
        
    }
    
    private InputStream getResults(String jobServiceURL)
    throws ClientProtocolException, ActivityProcessingException, IOException
    {
        HttpGet getResultLinks = new HttpGet(jobServiceURL + "/results");
        List<String> results = 
            UWSJobStatus.parseResults(getStreamFromRequest(getResultLinks));
        // there should be only one result
        if (results != null && !results.isEmpty())
        {
            // we use the first one since we only expect on result set
            String resultId = results.iterator().next();
            String url = jobServiceURL + "/results/" + resultId;
            LOG.debug("Retrieving results from " + url);
            HttpGet getResults = new HttpGet(url);
            return getStreamFromRequest(getResults);
        }
        else
        {
            throw new RuntimeException("No results");
        }
    }
   
    private boolean isPhase(String phase, String... phases)
    {
        boolean result = false;
        for (String p : phases)
        {
            result |= p.equalsIgnoreCase(phase);
        }
        return result;
    }
    
    private String readInputStream(InputStream input) throws IOException
    {
        InputStreamReader reader = new InputStreamReader(input);
        char[] buffer = new char[1024];
        int length;
        StringBuilder builder = new StringBuilder();
        while ((length = reader.read(buffer)) >= 0)
        {
            builder.append(buffer, 0, length);
        }
        reader.close();
        return builder.toString();
    }

    @Override
    protected void postprocess() 
        throws ActivityUserException,
               ActivityProcessingException, 
               ActivityTerminatedException 
    {
        //no post-processing
    }
    
    @Override
    protected void cleanUp() throws Exception 
    {
        mService.shutdownNow();
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
    private InputStream getStreamFromRequest(HttpUriRequest httpRequest)
        throws ClientProtocolException,
               IOException,
               ActivityProcessingException 
    {
        HttpResponse response;
        response = mClient.execute(httpRequest);
        HttpEntity entity = response.getEntity();
        if (entity != null)
        {
            return new BufferedInputStream(entity.getContent());
        }
        else 
        {
            throw new ActivityProcessingException(
                    new Exception("No response document to request " + httpRequest));
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
