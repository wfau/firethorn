package uk.ac.roe.wfau.firethorn.webapp.tap;
import java.io.IOException;
import java.io.PrintWriter;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.entity.AbstractComponent;
import uk.ac.roe.wfau.firethorn.entity.annotation.UpdateAtomicMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.job.Job.Status;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.webapp.tap.UWSJob;

@Slf4j
@Component
@Transactional(
    readOnly=false
    )
public
class UWSJobFactory extends AbstractComponent{

    /**
     * Public constructor.
     *
     */
    public UWSJobFactory()
        {
        super();
        }

    @Value("${firethorn.webapp.endpoint:null}")
    private String baseurl;
    
    /**
     * Public constructor.
     *
     */
    public UWSJobFactory(String baseurl)
        {
        super();
    	this.baseurl = baseurl;
        }
    
    /**
     * Get baseurl 
     * @return baseurl
     */
    public String getBaseurl() {
    	return baseurl;
    }
    
    /**
     * Set baseurl 
     * @return 
     */
    public void setBaseurl(String baseurl){
    	this.baseurl = baseurl;
    }
    
    /**
     * Create new UWSJob
     * @param resource
     * @return UWSJob
     * @throws Exception
     */
    public UWSJob create(AdqlResource resource) throws Exception {
       return new UWSJob(this, resource);
      }
    
    /**
     * Create new UWSJob
     * @param resource, query
     * @return UWSJob
     * @throws Exception
     */
    public UWSJob create(AdqlResource resource, AdqlQuery query) throws Exception {
       return new UWSJob(this, resource, query);
      }
    

   /**
    * 
    * Run an query job
    * 
    */
    @UpdateAtomicMethod
   	public void runQueryJob(final AdqlQuery query) throws IdentifierNotFoundException, IOException {
			
			try {
			
				if (query!=null){
					 
					this.factories().spring().transactor().update(
					           
			                new Runnable()
			                    {
			                    @Override
			                    public void run()
			                        {
			                      
			                    	query.execute();
			                      
			                        }
			                    }
			               
				    );
			
		
				}
			
			} catch (final Exception ouch) {
				ouch.printStackTrace();

	        }
				
       }
    
    
    /**
     * 
     * Create a query job
     * 
     */
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    @UpdateAtomicMethod
    public AdqlQuery createNewQuery(AdqlResource resource) throws IdentifierNotFoundException, IOException {
			AdqlSchema schema;
			AdqlQuery query = null;
			
			try {
				schema = resource.schemas().select(TapJobParams.DEFAULT_QUERY_SCHEMA);
			} catch (final NameNotFoundException ouch) {
				schema = resource.schemas().create(TapJobParams.DEFAULT_QUERY_SCHEMA);
			}
			
			try {
				query = schema.queries().create(null, null);
			
			} catch (final Exception ouch) {
				ouch.printStackTrace();

	        }
			
			return query;
				
        }
    
    /**
     * 
     * Create a query job
     * 
     */
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    @UpdateAtomicMethod
    public AdqlQuery createNewQuery(AdqlResource resource, String querystring) throws IdentifierNotFoundException, IOException {
			AdqlSchema schema;
			AdqlQuery query = null;
			
			try {
				schema = resource.schemas().select(TapJobParams.DEFAULT_QUERY_SCHEMA);
			} catch (final NameNotFoundException ouch) {
				schema = resource.schemas().create(TapJobParams.DEFAULT_QUERY_SCHEMA);
			}
			
			try {
				query = schema.queries().create(null, querystring);
			
			} catch (final Exception ouch) {
				ouch.printStackTrace();

	        }
			
			return query;
				
        }
    
    /**
     * 
     * Prepare a query job
     * 
     */
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    @UpdateAtomicMethod
	public void prepareQueryJob(AdqlResource resource, final AdqlQuery query, final String querystring) throws IdentifierNotFoundException, IOException {
			
    		AdqlSchema schema;

			if (resource!=null){
				
				try {
					schema = resource.schemas().select(TapJobParams.DEFAULT_QUERY_SCHEMA);
				} catch (final NameNotFoundException ouch) {
					schema = resource.schemas().create(TapJobParams.DEFAULT_QUERY_SCHEMA);
					ouch.printStackTrace();
				}
				
				try {
					this.factories().spring().transactor().update(
					           
			                new Runnable()
			                    {
			                    @Override
			                    public void run()
			                        {
			                      
			                        if (querystring != null)
			                            {
			                        	query.input(
			                        			querystring
			                                );
			                            }
			                        }
			                    }
			               
				    );
				
				
					if (query!=null){
						Status jobstatus = query.prepare();
					}
				
				} catch (final Exception ouch) {
					ouch.printStackTrace();
	
		        }
			}
				
        } 
    
	/**
	 * Write UWSJob in XML format
	 * @param errorMessage
	 * @param writer
	 */
	public static void writeUWSJobToXML (UWSJob uwsjob, PrintWriter writer){
		
	        writer.append("<?xml version='1.0' encoding='UTF-8'?>");
	        writer.append("	<uws:job xmlns:uws='http://www.ivoa.net/xml/UWS/v1.0' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:schemaLocation='http://www.ivoa.net/xml/UWS/v1.0 http://vo.ari.uni-heidelberg.de/docs/schemata/uws-1.0.xsd'>");
	     
	            writer.append("<uws:jobId>" + uwsjob.getJobId() + "</uws:jobId>");
	            writer.append("<uws:ownerId xsi:nil='true'>" + uwsjob.getOwnerId() + "</uws:ownerId>");
	            writer.append("<uws:phase>" + uwsjob.getPhase() + "</uws:phase>");
	            writer.append("<uws:startTime xsi:nil='true'>" + uwsjob.getStartTime() + "</uws:startTime>");
	            writer.append("<uws:endTime xsi:nil='true'>" + uwsjob.getEndTime() + "</uws:endTime>");
	            writer.append("<uws:executionDuration>" + uwsjob.getExecutionDuration() + "<uws:executionDuration>");
	            writer.append("<uws:destruction>" + uwsjob.getDestructionTime() + "</uws:destruction>");
	            writer.append("<uws:parameters>");
			        if (uwsjob.getRequest()!=null){
		            	writer.append("<uws:parameter id='request'>" + uwsjob.getRequest() + "</uws:parameter>");
			        }
			        if (uwsjob.getLang()!=null){
		            	writer.append("<uws:parameter id='lang'>" + uwsjob.getLang() + "</uws:parameter>");
			        }
			        if (uwsjob.getQuery()!=null){
		            	writer.append("<uws:parameter id='query'>" + uwsjob.getQuery().input() + "</uws:parameter>");
			        }
		        writer.append("</uws:parameters>");
		        
		        writer.append("<uws:results>");
			        if (uwsjob.getQuery() !=null){
		            	writer.append("<uws:result id='result'  xlink:href='" + uwsjob.getResults() + "'></uws:result>");
			        }
			    
		        writer.append("</uws:results>");
	       
	        writer.append("</uws:job>");
		
	}

}

