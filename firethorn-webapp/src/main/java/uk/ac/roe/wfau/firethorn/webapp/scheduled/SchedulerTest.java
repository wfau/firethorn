/**
 * 
 */
package uk.ac.roe.wfau.firethorn.webapp.scheduled;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 *
 */
@Slf4j
@Service
public class SchedulerTest
	{

	@Scheduled(fixedDelay=5000)
	public void something() {
	    log.debug("Scheduled task");

	    try {
		    log.debug("Sleeping");
	    	Thread.sleep(1000);
		    log.debug("Awake");
	    	}
	    catch (Exception ouch)
	    	{
	    	log.debug("Exception [{}]", ouch.getMessage());
	    	}
		}	
	}
