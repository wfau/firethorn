package uk.ac.roe.wfau.firethorn.webapp.tap;
import java.io.IOException;
import java.io.PrintWriter;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import uk.ac.roe.wfau.firethorn.blue.BlueTask.TaskState;
import uk.ac.roe.wfau.firethorn.entity.AbstractComponent;
import uk.ac.roe.wfau.firethorn.entity.annotation.UpdateAtomicMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.job.Job.Status;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.webapp.tap.UWSJob;
import uk.ac.roe.wfau.firethorn.blue.*;
import uk.ac.roe.wfau.firethorn.blue.BlueTask.TaskState;

   
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

    @Value("${firethorn.webapp.baseurl:null}")
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
    public UWSJob create(AdqlResource resource, String jobType) throws Exception {
       return new UWSJob(this, resource, jobType);
      }
    
    /**
     * Create new UWSJob
     * @param resource, query
     * @return UWSJob
     * @throws Exception
     */
    public UWSJob create(AdqlResource resource, BlueQuery query, String jobType) throws Exception {
       return new UWSJob(this, resource, query, jobType);
      }
    

   /**
    * 
    * Run an query job
    * 
    */
    @UpdateAtomicMethod
   	public void runQueryJob(final BlueQuery query) throws IdentifierNotFoundException, IOException {
			
			try {
			
				if (query!=null){
					 
					this.factories().spring().transactor().update(
					           
			                new Runnable()
			                    {
			                    @Override
			                    public void run()
			                        {
			                      
			                    	try {
										query.advance(
										        query.state(),
										        TaskState.RUNNING,
												Long.valueOf(0)
										        );
									} catch (InvalidStateRequestException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
			                      
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
     * Set a state for an query job
     * 
     */
     @UpdateAtomicMethod
    	public void setQueryJob(final BlueQuery query, final TaskState state) throws IdentifierNotFoundException, IOException {
 			
 			try {
 			
 				if (query!=null){
 					 
 					this.factories().spring().transactor().update(
 					           
 			                new Runnable()
 			                    {
 			                    @Override
 			                    public void run()
 			                        {
 			                      
 			                    	try {
 										query.advance(
 										        query.state(),
 										        state,
 												Long.valueOf(0)
 										        );
 									} catch (InvalidStateRequestException e) {
 										// TODO Auto-generated catch block
 										e.printStackTrace();
 									}
 			                      
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
    public BlueQuery createNewQuery(AdqlResource resource) throws IdentifierNotFoundException, IOException {
			BlueQuery query = null;
			
			try {
				query = resource.blues().create("");
			
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
    public BlueQuery createNewQuery(AdqlResource resource, String querystring) throws IdentifierNotFoundException, IOException {
			BlueQuery query = null;
			
			try {
				query = resource.blues().create(querystring);
				// query = schema.queries().create(null, querystring);
			
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
	public void prepareQueryJob(AdqlResource resource, final BlueQuery query, final String querystring) throws IdentifierNotFoundException, IOException {
			
			if (resource!=null){
				
				try {
					this.factories().spring().transactor().update(
					           
			                new Runnable()
			                    {
			                    @Override
			                    public void run()
			                        {
			                      
			                        if (querystring != null)
			                            {
			                        	try {
											query.update(
													querystring
											    );
										} catch (InvalidStateRequestException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
			                            }
			                        }
			                    }
			               
				    );
				
				
				} catch (final Exception ouch) {
					ouch.printStackTrace();
	
		        }
			}
				
        } 
    
    /**
	 * Write UWS Job results
	 * @param uwsjob
	 * @param writer
	 */
	public String writeUWSResultToXML (UWSJob uwsjob){
		StringBuilder writer = new StringBuilder();
		String resultsUrl = uwsjob.getJobURLResults();
		writer.append("<uws:results xsi:schemaLocation='http://www.ivoa.net/xml/UWS/v1.0 http://vo.ari.uni-heidelberg.de/docs/schemata/uws-1.0.xsd http://www.w3.org/1999/xlink http://vo.ari.uni-heidelberg.de/docs/schemata/xlink.xsd'>");
		writer.append("<uws:result id='result' xlink:href='" + resultsUrl +"'/></uws:results>");
		return writer.toString();
	}
	
	/**
	 * Write UWSJob in XML format
	 * @param uwsjob
	 * @param writer
	 */
	public String writeUWSJobToXML (UWSJob uwsjob){

			StringBuilder writer = new StringBuilder();
			
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
			        if (uwsjob.getFormat()!=null){
		            	writer.append("<uws:parameter id='format'>" + uwsjob.getFormat() + "</uws:parameter>");
			        }
			        if (uwsjob.getVersion()!=null){
		            	writer.append("<uws:parameter id='version'>" + uwsjob.getVersion() + "</uws:parameter>");
			        }
			        if (uwsjob.getMaxrec()!=null){
		            	writer.append("<uws:parameter id='maxrec'>" + uwsjob.getMaxrec() + "</uws:parameter>");
			        }
		        writer.append("</uws:parameters>");
		        
		        writer.append("<uws:results>");
			        if (uwsjob.getQuery() !=null){
		            	writer.append("<uws:result id='result'  xlink:href='" + uwsjob.getResults() + "'></uws:result>");
			        }
			    
		        writer.append("</uws:results>");
	       
	        writer.append("</uws:job>");
		
	        return writer.toString();
	}

}

