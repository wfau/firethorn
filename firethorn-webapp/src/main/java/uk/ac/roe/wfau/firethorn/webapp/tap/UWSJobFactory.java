package uk.ac.roe.wfau.firethorn.webapp.tap;
import java.io.IOException;
import java.io.PrintWriter;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import uk.ac.roe.wfau.firethorn.adql.query.blue.*;
import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueTask.TaskState;
import uk.ac.roe.wfau.firethorn.adql.query.green.GreenJob.Status;
import uk.ac.roe.wfau.firethorn.entity.AbstractComponent;
import uk.ac.roe.wfau.firethorn.entity.annotation.UpdateAtomicMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.webapp.tap.UWSJob;

   
@Slf4j
@Component
@Transactional(
    readOnly=false
    )
public
class UWSJobFactory extends AbstractComponent {

    /**
     * Public constructor.
     *
     */
    public UWSJobFactory()
        {
    	/**
    	 * Get baseurl from context
    	 * 
    	 * @return baseurl
    	 */
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
	 * Get baseurl from context
	 * 
	 * @return baseurl
	 */
	public String getBaseurlFromContext() {
		
	     UriComponentsBuilder builder ;
	     builder = ServletUriComponentsBuilder.fromCurrentContextPath();
		 return builder.build().toString();
	}
    
    /**
     * Get baseurl 
     * @return baseurl
     */
    public String getBaseurl() {
    	return getBaseurlFromContext();
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
    @UpdateAtomicMethod
    public BlueQuery createNewQuery(AdqlResource resource, String querystring) throws IdentifierNotFoundException, IOException {
			BlueQuery query = null;
			
			try {
				
				query = resource.blues().create(querystring);
			
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
   

}

