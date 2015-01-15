package uk.org.ogsadai.activity.astro;

import java.io.InputStream;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import uk.org.ogsadai.util.xml.XML;

public class UWSJobStatus 
{
    public static final String PHASE_QUEUED = "QUEUED";
    public static final String PHASE_PENDING = "PENDING";
    public static final String PHASE_EXECUTING = "EXECUTING";
    public static final String PHASE_COMPLETED = "COMPLETED";
    public static final String PHASE_ERROR = "ERROR";
    public static final String PHASE_UNKNOWN = "UNKNOWN";
    public static final String PHASE_HELD = "HELD";
    public static final String PHASE_SUSPENDED = "SUSPENDED";
    public static final String PHASE_ABORTED = "ABORTED";
    
    // workaround for WFAU TAP services
    public static final String UWS_RC3_NS = "http://www.ivoa.net/xml/UWS/v1.0rc3";
    public static final String UWS_NS = "http://www.ivoa.net/xml/UWS/v1.0";
    private String mStatus;
    private String mJobID;
    private List<String> mResults = new LinkedList<String>();
    private String mErrorType;
    private String mErrorSummary;
    
    public static UWSJobStatus parseJob(String input) 
    {
        Document document = XML.toDocument(new InputSource(new StringReader(input)));
        return parseJob(document);
    }
    
    public static UWSJobStatus parseJob(InputStream input) 
    {
        Document document = XML.toDocument(new InputSource(input));
        return parseJob(document);
    }
    
    public static UWSJobStatus parseJob(Document document) 
    {
        UWSJobStatus result = new UWSJobStatus();

        // find the job ID
        result.mJobID = getContent(document.getDocumentElement(), UWS_RC3_NS, "jobId");
        if (result.mJobID == null)
        {
            result.mJobID = getContent(document.getDocumentElement(), UWS_NS, "jobId");
        }
        
        // phase - QUEUED, PENDING, EXECUTING, COMPLETED, ERROR,
        //         UNKNOWN, HELD, SUSPENDED, ABORTED
        result.mStatus = getContent(document.getDocumentElement(), UWS_RC3_NS, "phase");
        if (result.mStatus == null)
        {
            result.mStatus = getContent(document.getDocumentElement(), UWS_NS, "phase");
        }
        
        // result links
        parseResults(document, result.mResults);

        // error message
        NodeList nodes = document.getElementsByTagNameNS(UWS_RC3_NS, "errorSummary");
        if (nodes == null || nodes.getLength() == 0)
        {
            nodes = document.getElementsByTagNameNS(UWS_NS, "errorSummary");
        }
        if (nodes != null && nodes.getLength() > 0)
        {
            Element element = (Element) nodes.item(0);
            result.mErrorType = element.getAttribute("type");
            result.mErrorSummary = getContent(element, UWS_NS, "message");
            if (result.mErrorSummary == null)
            {
                result.mErrorSummary = getContent(element, UWS_RC3_NS, "status"); 
            }
        }
        
        return result;
    }
    
    public static List<String> parseResults(InputStream input) 
    {
        List<String> results = new LinkedList<String>();
        Document document = XML.toDocument(new InputSource(input));
        parseResults(document, results);
        return results;
    }
    
    public static void parseResults(Document document, List<String> results)
    {
        NodeList nodes = document.getElementsByTagNameNS(UWS_RC3_NS, "results");
        if (nodes == null || nodes.getLength() == 0)
        {
            nodes = document.getElementsByTagNameNS(UWS_NS, "results");
        }
        if (nodes != null && nodes.getLength() > 0)
        {
            Element element = (Element) nodes.item(0);
            NodeList resultNodes = element.getElementsByTagNameNS(UWS_NS, "result");
            if (resultNodes == null || resultNodes.getLength() == 0)
            {
                resultNodes = element.getElementsByTagNameNS(UWS_RC3_NS, "result");
            }
            for (int i=0; i<resultNodes.getLength(); i++)
            {
                Element resultEl = (Element) resultNodes.item(i);
                results.add(resultEl.getAttribute("id"));
            }
        }

    }
    
    public String getStatus()
    {
        return mStatus;
    }
    
    public String getJobID()
    {
        return mJobID;
    }
    
    public String getErrorType()
    {
        return mErrorType;
    }
    
    public String getErrorSummary()
    {
        return mErrorSummary;
    }
    
    public List<String> getResults()
    {
        return mResults;
    }
    
    @Override
    public String toString()
    {
        StringBuilder result = new StringBuilder();
        result.append("UWSJobStatus[");
        result.append("jobId=").append(mJobID);
        result.append(", phase=").append(mStatus);
        result.append(", results=").append(mResults);
        if (mErrorType != null && !mErrorType.isEmpty())
        {
            result.append(", errorType=").append(mErrorType);
        }
        if (mErrorSummary != null && !mErrorSummary.isEmpty())
        {
            result.append(", errorSummary=").append(mErrorSummary);
        }
        result.append("]");
        return result.toString();
    }
    
    private static String getContent(Element element, String namespace, String tag)
    {
        String result = null;
        NodeList nodes = element.getElementsByTagNameNS(namespace, tag);
        if (nodes != null && nodes.getLength() > 0)
        {
            result = nodes.item(0).getTextContent();
        }
        return result;
    }

}
