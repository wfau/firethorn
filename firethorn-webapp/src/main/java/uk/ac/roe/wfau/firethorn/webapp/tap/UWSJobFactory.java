package uk.ac.roe.wfau.firethorn.webapp.tap;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.spring.ComponentFactories;
import uk.ac.roe.wfau.firethorn.webapp.tap.UWSJob;

@Slf4j
@Component
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

}

