/*
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
 * ----------------------------------------------------------------------
 * Original source licence
 *
 * Copyright (c) The University of Edinburgh,  2012-2013.
 *
 * LICENCE-START
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * LICENCE-END
 * 
 */
package uk.ac.roe.wfau.firethorn.ogsadai.activity.server.ivoa;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.roe.wfau.firethorn.ogsadai.activity.common.ivoa.IvoaSelectDataParam;
import uk.ac.starlink.table.OnceRowPipe;
import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.votable.VOTableBuilder;
import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.MatchedIterativeActivity;
import uk.org.ogsadai.activity.astro.ADQLAsyncQueryActivity;
import uk.org.ogsadai.activity.astro.QueryFailedException;
import uk.org.ogsadai.activity.astro.UWSJobStatus;
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
import uk.org.ogsadai.config.ConfigurationValueIllegalException;
import uk.org.ogsadai.config.ConfigurationValueMissingException;
import uk.org.ogsadai.config.KeyValueProperties;
import uk.org.ogsadai.resource.ResourceAccessor;
import uk.org.ogsadai.resource.generic.GenericResourceAccessor;

/**
 * Activity to query a TAP service.
 * Based on the {@link ADQLAsyncQueryActivity} developed by the OGSA-DAI Project Team.
 * 
 */
public class IvoaSelectDataActivity
extends MatchedIterativeActivity
implements ResourceActivity, ServiceAddressesActivity, ConfigurableActivity
    {
    /**
     * Our resource template name.
     *
     */
    protected static final String IVOA_RESOURCE_TEMPLATE = "uk.ac.roe.wfau.firethorn.IVOA_RESOURCE_TEMPLATE" ;
    
    /**
     * Our debug logger.
     *
     */
    private static Logger logger = LoggerFactory.getLogger(
        IvoaSelectDataActivity.class
        );
    
    /**
     * Our HttpClient used to contact to the TAP service.
     * 
     */
    private HttpClient http;
    
    /**
     * List of parameters for the HTTP POST request.
     * 
     */
    private Map<String, String> parameters = new HashMap<String, String>();

    /**
     * Our activity properties.
     * 
     */
    private KeyValueProperties properties;
    @Override
    public void configureActivity(final KeyValueProperties properties)
    throws ActivityInitialisationException
        {
        this.properties = properties;
        }

    protected void configure(final KeyValueProperties properties)
    throws ActivityProcessingException
        {
        if (properties.containsKey(IvoaResourceKeys.IVOA_TAP_ENDPOINT))
            {
            try {
                Object object = properties.get(IvoaResourceKeys.IVOA_TAP_ENDPOINT);
                logger.debug("CFG endpoint [{}][{}]", object.getClass().getName(), object.toString());
                endpoint = (String) properties.get(IvoaResourceKeys.IVOA_TAP_ENDPOINT);
                }
            catch (Exception ouch)
                {
                throw new ActivityProcessingException(
                    new ConfigurationValueIllegalException(
                        IvoaResourceKeys.IVOA_TAP_ENDPOINT,
                        ouch
                        )
                    );
                }
            }

        if (properties.containsKey(IvoaResourceKeys.IVOA_UWS_QUICKSTART))
            {
            try {
                Object object = properties.get(IvoaResourceKeys.IVOA_UWS_QUICKSTART);
                logger.debug("CFG quickstart [{}][{}]", object.getClass().getName(), object.toString());
                quickstart = (Boolean) properties.get(
                    IvoaResourceKeys.IVOA_UWS_QUICKSTART
                    );
                }
            catch (Exception ouch)
                {
                throw new ActivityProcessingException(
                    new ConfigurationValueIllegalException(
                        IvoaResourceKeys.IVOA_UWS_QUICKSTART,
                        ouch
                        )
                    );
                }
            }

        if (properties.containsKey(IvoaResourceKeys.IVOA_UWS_INTERVAL))
            {
            try {
                Object object = properties.get(IvoaResourceKeys.IVOA_UWS_INTERVAL);
                logger.debug("CFG interval [{}][{}]", object.getClass().getName(), object.toString());
                interval = Integer.valueOf(
                    (String) properties.get(
                        IvoaResourceKeys.IVOA_UWS_INTERVAL
                        )
                    );
                }
            catch (Exception ouch)
                {
                throw new ActivityProcessingException(
                    new ConfigurationValueIllegalException(
                        IvoaResourceKeys.IVOA_UWS_INTERVAL,
                        ouch
                        )
                    );
                }
            }

        if (properties.containsKey(IvoaResourceKeys.IVOA_UWS_TIMEOUT))
            {
            try {
                Object object = properties.get(IvoaResourceKeys.IVOA_UWS_TIMEOUT);
                logger.debug("CFG timeout [{}][{}]", object.getClass().getName(), object.toString());
                timeout = Integer.valueOf(
                    (String) properties.get(
                        IvoaResourceKeys.IVOA_UWS_TIMEOUT
                        )
                    );
                }
            catch (Exception ouch)
                {
                throw new ActivityProcessingException(
                    new ConfigurationValueIllegalException(
                        IvoaResourceKeys.IVOA_UWS_TIMEOUT,
                        ouch
                        )
                    );
                }
            }
        }
    
    /**
     * Our TAP service endpoint.
     * 
     */
    private String endpoint;

    /**
     * Our TAP service version.
     * 
     */
    private String version;

    /**
     * Flag to indicate if the TAP implementation supports PHASE=RUN in the initial POST request.
     * 
     */
    private boolean quickstart = false ;

    /**
     * The delay in milliseconds for polling the query status.
     *
     */
    private int interval = 500;

    /**
     * Timeout in milliseconds for polling the query status.
     * No timeout if the value is <= 0.
     *
     */
    private int timeout = 0;

    /**
     * Our {@link ExecutorService}.
     * 
     */
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    
    /**
     * Constructor.
     */
    public IvoaSelectDataActivity()
        {
        super();
        http = new DefaultHttpClient();
        // default TAP request parameters
        parameters.put("REQUEST", "doQuery");
        parameters.put("VERSION", "1.0");
        parameters.put("LANG", "ADQL");
        }

    @Override
    protected void postprocess() 
    throws ActivityUserException, ActivityProcessingException, ActivityTerminatedException 
        {
        }
    
    @Override
    protected void cleanUp() throws Exception 
        {
        executor.shutdownNow();
        }

    @Override
    public Class<GenericResourceAccessor> getTargetResourceAccessorClass() 
        {
        return GenericResourceAccessor.class;
        }

    /**
     * Our target resource {@link ResourceAccessor}.
     *
     */
    private ResourceAccessor resource;

    @Override
    public void setTargetResourceAccessor(final ResourceAccessor resource) 
        {
        this.resource = resource;
        }

    /**
     * Our {@link ServiceAddresses}.
     * 
     */
    private ServiceAddresses addresses;
    
    @Override
    public void setServiceAddresses(ServiceAddresses addresses)
        {
        this.addresses = addresses;
        }
    
    @Override
    protected ActivityInput[] getIterationInputs() 
        {
        return new ActivityInput[] { 
            new TypedActivityInput(
                IvoaSelectDataParam.IVOA_TAP_ADQL_QUERY_PARAM,
                String.class
                )
            };
        }

    @Override
    protected void preprocess()
    throws ActivityUserException, ActivityProcessingException, ActivityTerminatedException 
        {
        validateOutput(
            IvoaSelectDataParam.TUPLE_OUTPUT
            );
        //
        // Read our Activity properties.
        configure(
            properties
            );
        //
        // Read our resource properties.
        configure(
            resource.getResource().getState().getConfiguration()
            );
        //
        // Check we have an endpoint.
        if (endpoint == null)
            {
            throw new ActivityProcessingException(
                new ConfigurationValueMissingException(
                    IvoaResourceKeys.IVOA_TAP_ENDPOINT
                    )
                );
            }

        }
    
    @Override
    protected void processIteration(final Object[] iterationData)
        throws ActivityProcessingException, ActivityTerminatedException, ActivityUserException 
        {
        final String adql = (String) iterationData[0];
        
        logger.debug("TAP query    [" + adql + "]");
        logger.debug("TAP endpoint [" + endpoint + "]");

        InputStream result = null;
        try {

            String jobid = submit(this.endpoint, adql);
            
            String jobServiceURL = this.endpoint + "/" + jobid;

            // start job: post PHASE=RUN
            start(jobServiceURL);
            
            // check for errors
            String state = poll(jobServiceURL);
            logger.debug("Job completed. Status: " + state);
            
            // wait for results
            result = getResults(jobServiceURL);
                        
            }       
        catch (ActivityProcessingException ouch)
            {
            throw ouch;
            }
        catch (ActivityUserException ouch)
            {
            throw ouch;
            }
        catch (Throwable ouch) 
            {
            throw new ActivityProcessingException(
                ouch
                );
            }
        
        Thread streamThread = null;
        try {
            // read results and transform into tuples
            final InputStream finalResult = result;
            final VOTableBuilder builder = new VOTableBuilder();
            final OnceRowPipe sink = new OnceRowPipe();
            streamThread = new Thread()
                {
                @Override
                public void run() 
                    {
                    try {
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
                    star,
                    resource.getResource(),
                    addresses.getDataRequestExecutionService(),
                    true,
                    true,
                    writer
                    );
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
        finally {

            if (streamThread != null)
                {
                try {
                    streamThread.join();
                    }
                catch(Throwable ouch)
                    {
                    logger.debug("Excepton joining stream thread [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
                    }
                }
            
            if (result != null)
                {
                try {
                    result.close();
                    }
                catch(Throwable ouch)
                    {
                    logger.debug("Excepton closing result stream [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
                    }
                }
            }
        }

    private String submit(String asyncTAPServiceURL, String expression) 
    throws ClientProtocolException, IOException, ActivityProcessingException, ActivityUserException 
        {
        HttpPost httpPost = prepareHttpPost(
            asyncTAPServiceURL,
            expression
            );

        logger.debug("Submitting job to " + asyncTAPServiceURL);

        String jobResult = readInputStream(getStreamFromRequest(httpPost));
        logger.debug(jobResult);
        UWSJobStatus job = UWSJobStatus.parseJob(jobResult);
        String jobID = job.getJobID();
        if (jobID == null)
            {
            throw new ActivityUserException(
                new QueryFailedException(
                    "Job failed [" + jobResult + "]"
                    )
                );
            }
        logger.debug("job id = " + jobID);
        return jobID;
        }

    private void start(String jobServiceURL)
    throws ClientProtocolException, ActivityProcessingException, IOException
        {
        logger.debug("Checking phase.");
        HttpGet getPhase = new HttpGet(jobServiceURL+ "/phase");
        InputStream input = getStreamFromRequest(getPhase);
        String phase = readInputStream(input);
        logger.debug(" phase [" + phase + "]");

        if (isPhase(phase, UWSJobStatus.PHASE_PENDING))
            {
            logger.debug("Phase is PENDING .. starting job.");
        	HttpPost startJob = new HttpPost(jobServiceURL + "/phase");
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("PHASE", "RUN"));
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
            startJob.setEntity(formEntity);
            String jobResult = readInputStream(getStreamFromRequest(startJob));
            logger.debug(jobResult);
        	}
        else {
            logger.debug("Phase is not PENDING .. skipping start");
            }
        }
    
    private String poll(final String jobServiceURL)
    throws ClientProtocolException, IOException, ActivityUserException, ActivityProcessingException, ActivityTerminatedException 
        {
        final String url = jobServiceURL + "/phase";
        
        Callable<String> callable = new Callable<String>() 
            {
            @Override
            public String call()
            throws ClientProtocolException, IOException, ActivityUserException, ActivityProcessingException, ActivityTerminatedException 
                {
                String phase = null;
                // we can't read the results before the job is completed
                // so wait until the job has finished executing
                do {
                    try {
                        Thread.sleep(interval);
                        } 
                    catch (InterruptedException ouch) 
                        {
                        Thread.currentThread().interrupt(); // ?
                        throw new ActivityTerminatedException();
                        }
                    HttpGet getPhase = new HttpGet(url);
                    InputStream input = getStreamFromRequest(getPhase);
                    phase = readInputStream(input);
                    }
                while (
                    isPhase(
                        phase, 
                        UWSJobStatus.PHASE_EXECUTING,
                        UWSJobStatus.PHASE_PENDING,
                        UWSJobStatus.PHASE_QUEUED
                        )
                    );
                
                // reject any job that hasn't completed successfully
                if (!isPhase(phase, UWSJobStatus.PHASE_COMPLETED))
                    {
                    HttpGet job = new HttpGet(jobServiceURL);
                    InputStream input = getStreamFromRequest(job);
                    try {
                        UWSJobStatus status = UWSJobStatus.parseJob(input);
                        throw new ActivityUserException(
                            new QueryFailedException(
                                status.getErrorSummary()
                                )
                            );
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

        Future<String> future = executor.submit(
            callable
            );

        try {
            logger.debug("Waiting for job to complete");   
            logger.debug("Poll interval [" + interval + "]");
            logger.debug("Timeout [" + timeout + "]");
            if (timeout > 0)
                {
                return future.get(timeout, TimeUnit.MILLISECONDS);
                }
            else {
                return future.get();
                }
            } 
        catch (ExecutionException ouch) 
            {
            if (ouch.getCause() instanceof ActivityUserException)
                {
                throw (ActivityUserException) ouch.getCause();
                }
            if (ouch.getCause() instanceof ActivityProcessingException)
                {
                throw (ActivityProcessingException) ouch.getCause();
                }
            if (ouch.getCause() instanceof ActivityTerminatedException)
                {
                throw (ActivityTerminatedException) ouch.getCause();
                }
            throw new ActivityProcessingException(
                ouch.getCause()
                );
            } 
        catch (InterruptedException ouch) 
            {
            throw new ActivityTerminatedException();
            }
        catch (TimeoutException ouch) 
            {
            throw new ActivityTerminatedException();
            }
        finally {
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
            logger.debug("Retrieving results from " + url);
            HttpGet getResults = new HttpGet(url);
            return getStreamFromRequest(getResults);
        }
        else
        {
            throw new RuntimeException("No results");
        }
    }
   
    private boolean isPhase(final String phase, final String... phases)
        {
        for (String test : phases)
            {
            if (test.equalsIgnoreCase(phase))
                {
                return true ;
                }
            }
        return false;
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
    throws ClientProtocolException, IOException, ActivityProcessingException 
        {
        HttpResponse response;
        response = http.execute(httpRequest);
        HttpEntity entity = response.getEntity();
        if (entity != null)
            {
            return new BufferedInputStream(entity.getContent());
            }
        else 
            {
            throw new ActivityProcessingException(
                new Exception(
                    "No response document to request " + httpRequest
                    )
                );
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
        for (Entry<String, String> entry : parameters.entrySet())
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("TAP PARAMETER: " + entry.getKey() + 
                        "=" + entry.getValue());
            }
            postParameters.add(
                    new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        //ZRQ
        // Most TAP services support this, apart from the CADC implementation.
        // Gaia TAP needs this, because it doesn't allow RUN after create.
        if (quickstart)
            {
            postParameters.add(new BasicNameValuePair("PHASE", "RUN"));
            }
        
        postParameters.add(new BasicNameValuePair("QUERY", adql));

        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
        post.setEntity(formEntity);
        return post;
    }

    }
