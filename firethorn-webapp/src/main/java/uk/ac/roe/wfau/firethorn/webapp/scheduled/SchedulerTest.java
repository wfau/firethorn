/**
 * 
 */
package uk.ac.roe.wfau.firethorn.webapp.scheduled;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import uk.ac.roe.wfau.firethorn.spring.ComponentFactories;

/**
 *
 */
@Slf4j
@Service
public class SchedulerTest
	{

    /**
     * Autowired system services.
     *
     */
    @Autowired
    private ComponentFactories factories;

    /**
     * Our system services.
     *
     */
    public ComponentFactories factories()
        {
        return this.factories;
        }

	//@Scheduled(fixedDelay=10000)
	public void test()
		{
	    log.debug("Start");

	    log.debug("Open [{}]", factories().hibernate().factory().openSession());
	    log.debug("Session [{}]", factories().hibernate().session());
	    factories().hibernate().session().close();
	    log.debug("Done");
		}	
    
	//@Scheduled(fixedDelay=10000)
	public void sleeper()
		{
	    log.debug("Start");
	    try {
		    //log.debug("Sleeping");
	    	Thread.sleep(10000);
		    //log.debug("Awake");
	    	}
	    catch (Exception ouch)
	    	{
	    	log.debug("Exception [{}]", ouch.getMessage());
	    	}
	    log.debug("Done");
		}	
	}
