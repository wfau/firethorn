/*
 *  Copyright (C) 2013 Royal Observatory, University of Edinburgh, UK
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package uk.ac.roe.wfau.firethorn.tap;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.naming.NameNotFoundException;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.job.Job;
import uk.ac.roe.wfau.firethorn.job.Job.Status;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.test.TestBase;
import uk.ac.roe.wfau.firethorn.test.TestRoot;
import uk.ac.roe.wfau.firethorn.webapp.tap.TapJobParams;
import uk.ac.roe.wfau.firethorn.webapp.tap.UWSJob;

/**
 *
 *
 */
@Slf4j
public class TapTestCase
extends TestBase
    {
	
	
	
    @Test
    public void test001()
    throws Exception
        {
    	String ident = "32771";
    	String query = "Select top 10 * from Filter";
    	String phase = "RUNNING";
    	
    	AdqlResource resource =  factories().adql().resources().select(
    	                factories().adql().resources().idents().ident(
    	                    ident
    	                    )
    	                );
    	
        log.debug("Creating new UWSJob with resource: [{}]" , ident);

    	UWSJob uwsjob = new UWSJob(resource);
        log.debug("New Empty Query ID: [{}]" , uwsjob.getQueryId());

    	uwsjob.setQuery(query);
        log.debug("Setting QUERY input to: [{}]" , query);
        
        
    	uwsjob.setPhase(phase);
        log.debug("Setting PHASE to: [{}]" , phase);

        
    	log.debug("Getting results of job",  uwsjob.getResults());
    	
    	
        }
    }
