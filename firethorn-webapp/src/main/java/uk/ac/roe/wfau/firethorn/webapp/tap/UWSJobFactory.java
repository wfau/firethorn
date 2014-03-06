package uk.ac.roe.wfau.firethorn.webapp.tap;
import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.entity.annotation.UpdateAtomicMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.UpdateMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.job.Job.Status;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.spring.ComponentFactories;
import uk.ac.roe.wfau.firethorn.webapp.tap.UWSJob;

@Slf4j
@Component
@Transactional(
    readOnly=false
    )
public
class UWSJobFactory {
	/**
     * Autowired ComponentFactories instance.
     *
     */
    @Autowired
    private ComponentFactories factories;

    /**
     * Our system services.
     *
     */
    public ComponentFactories factories(){
        return this.factories;
    }
    
    public UWSJob create(AdqlResource resource) throws Exception {
       return new UWSJob(this, resource);
      }
 
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
				query = schema.queries().create("");
			
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
				query = schema.queries().create(querystring);
			
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

}

