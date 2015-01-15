package uk.org.ogsadai.activity.astro;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
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
import uk.org.ogsadai.common.msgs.DAILogger;


/**
 * Executes an asynchronous ADQL query at a TAP service.
 *
 */
public class TAPService 
{
    private static final DAILogger LOG = DAILogger.getLogger(TAPService.class);
    
    private HttpClient mClient;
    private String mTAPServiceURL;
    private ExecutorService mService = Executors.newSingleThreadExecutor();
    private long mPollInterval = 500;
    private long mPollTimeout = 0;

    public TAPService(String tapService) 
    {
        mTAPServiceURL = tapService;
        mClient = new DefaultHttpClient();
    }
    
    public void setPollInterval(long pollInterval)
    {
        mPollInterval = pollInterval;
    }
    
    public void setPollTimeout(long timeout)
    {
        mPollTimeout = timeout;
    }
    
    public void release()
    {
        mService.shutdownNow();
    }
    
    public StarTable runQuery(String expression) 
    throws ClientProtocolException, 
           IOException, 
           QueryFailedException, 
           TimeoutException
    {
        String asyncTAPServiceURL = mTAPServiceURL + "async";
        String jobID = submitJob(asyncTAPServiceURL, expression);
        
        String jobServiceURL = asyncTAPServiceURL + "/" + jobID;

        // start job: post PHASE=RUN
        startJob(jobServiceURL);
            
        // check for errors
        String state = waitForJobComplete(jobServiceURL);
        LOG.debug("Job completed. Status: " + state);
            
        // wait for results
        final InputStream result = getResults(jobServiceURL);
        final VOTableBuilder builder = new VOTableBuilder();
        final OnceRowPipe sink = new OnceRowPipe();
        Callable<Void> starTable = new Callable<Void>()
        {
            public Void call() 
            {
             
                try 
                {
                    builder.streamStarTable(result, sink, null);
                }
                catch(IOException e)
                {
                    sink.setError(e);
                }
                return null;
            }
        };
        Future<Void> future = mService.submit(starTable);
        try 
        {
            future.get();
            return sink.waitForStarTable();
        }
        catch (InterruptedException e)
        {
            return null;
        } 
        catch (ExecutionException e) 
        {
             QueryFailedException exc = 
                     new QueryFailedException("Could not read results.");
             exc.initCause(e.getCause());
             throw exc;
        }

    }


    private String submitJob(String asyncTAPServiceURL, String expression) 
    throws ClientProtocolException, IOException, QueryFailedException 
    {
        HttpPost httpPost = prepareHttpPost(asyncTAPServiceURL, expression);
        LOG.debug("Submitting job to " + asyncTAPServiceURL);
        String jobResult = readInputStream(getStreamFromRequest(httpPost));
        LOG.debug(jobResult);
        UWSJobStatus job = UWSJobStatus.parseJob(jobResult);
        String jobID = job.getJobID();
        if (jobID == null)
        {
            throw new QueryFailedException("Failed to submit job: " + jobResult);
        }
        LOG.debug("job id = " + jobID);
        return jobID;
    }

    private void startJob(String jobServiceURL)
    throws ClientProtocolException, IOException
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
           QueryFailedException, 
           TimeoutException
    {
        final String url = jobServiceURL + "/phase";
        
        Callable<String> checkStatus = new Callable<String>() 
        {

            @Override
            public String call()
            throws ClientProtocolException,
                   IOException, 
                   QueryFailedException
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
                        return null;
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
                        throw new QueryFailedException(status.getErrorSummary());
                    }
                    catch (IllegalArgumentException e)
                    {
                        // XML cannot be parsed
                        throw new QueryFailedException("Unknown error.");
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
            return null;
        }
        catch (ExecutionException e) 
        {
            if (e.getCause() instanceof QueryFailedException)
            {
                throw (QueryFailedException)e.getCause();
            }
            if (e.getCause() instanceof IOException)
            {
                throw (IOException)e.getCause();
            }
            if (e.getCause() instanceof ClientProtocolException)
            {
                throw (ClientProtocolException)e.getCause();
            }
            throw new RuntimeException(e.getCause());
        } 
        finally
        {
            future.cancel(true);
        }
        
    }
    
    private InputStream getResults(String jobServiceURL)
    throws ClientProtocolException, IOException
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

    /**
     * Returns a stream for a given http URL.
     * 
     * @param url
     *     http URL
     * @return
     *     stream
     * @throws ClientProtocolException
     * @throws IOException
     */
    private InputStream getStreamFromRequest(HttpUriRequest httpRequest)
        throws ClientProtocolException,
               IOException
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
            // throw some exception
            throw new RuntimeException(
                    "No response document to request " + httpRequest);
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
        postParameters.add(new BasicNameValuePair("REQUEST", "doQuery"));
        postParameters.add(new BasicNameValuePair("VERSION", "1.0"));
        postParameters.add(new BasicNameValuePair("LANG", "ADQL"));
        postParameters.add(new BasicNameValuePair("QUERY", adql));
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
        post.setEntity(formEntity);
        return post;
    }

}
