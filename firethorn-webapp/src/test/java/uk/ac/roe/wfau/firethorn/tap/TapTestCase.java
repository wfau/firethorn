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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.naming.NameNotFoundException;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import sun.net.www.URLConnection;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.job.Job;
import uk.ac.roe.wfau.firethorn.job.Job.Status;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.test.TestBase;
import uk.ac.roe.wfau.firethorn.webapp.tap.TapJobParams;
import uk.ac.roe.wfau.firethorn.webapp.tap.UWSJob;
import uk.ac.roe.wfau.firethorn.webapp.tap.AdqlTapAsyncController;
import uk.ac.roe.wfau.firethorn.webapp.tap.UWSJobFactory;

/**
 *
 *
 */
@Slf4j
public class TapTestCase
extends TestBase
    {
	
	String ident = "32771";
	String querystring = "Select top 10 * from atlas.Filter";
	String phase = "RUNNING";
	static AdqlQuery query;
	
	@Autowired
	 private UWSJobFactory uwsfactory = new UWSJobFactory();
	
	public void readFromURL(final String surl){
		
		URL url = null;
		String inputLine;

		try {
		    url = new URL(surl);
		} catch (MalformedURLException e) {
		    e.printStackTrace();
		}
		BufferedReader in;
		try {
		    java.net.URLConnection con = url.openConnection();
		    con.setReadTimeout( 100000 ); //1 second
		    in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		    while ((inputLine = in.readLine()) != null) {
		        System.out.println(inputLine);
		    }
		    in.close();

		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
	
    @Test
    public void test000()
    throws Exception
        {

    	AdqlResource resource =  factories().adql().resources().select(
    	                factories().adql().resources().idents().ident(
    	                    ident
    	                    )
    	                );
    	
        log.debug("Creating new UWSJob with resource: [{}]" , ident);

    	UWSJob uwsjob = uwsfactory.create(resource);
        String queryid = uwsjob.getFullQueryURL();
        query = uwsjob.getQuery();
        log.debug("New Empty Query URL: [{}]" , queryid);
      

        }
    
    
    
	
    @Test
    public void test001()
    throws Exception
        {
    	
     	AdqlResource resource =  factories().adql().resources().select(
                factories().adql().resources().idents().ident(
                    ident
                    )
                );

     	log.debug("Creating new UWSJob with resource: [{}]" , ident);
    	UWSJob uwsjob = uwsfactory.create(resource, query);
		uwsjob.setQuery(querystring);
		
        log.debug("Setting QUERY input to: [{}]" , querystring);
        
        

    	
    	
        }
    
    
	
    @Test
    public void test002()
    throws Exception
        {
  
    	
    	AdqlResource resource =  factories().adql().resources().select(
    	                factories().adql().resources().idents().ident(
    	                    ident
    	                    )
    	                );
    	
        log.debug("Creating new UWSJob with resource: [{}]" , ident);

    	UWSJob uwsjob = uwsfactory.create(resource, query);     
    	uwsjob.setPhase(phase);
        log.debug("Setting PHASE to: [{}]" , phase);
    

    	
    	
        }
    
    
	
    @Test
    public void test003()
    throws Exception
        {
    	
    	
    	AdqlResource resource =  factories().adql().resources().select(
    	                factories().adql().resources().idents().ident(
    	                    ident
    	                    )
    	                );
    	
      
    	UWSJob uwsjob = uwsfactory.create(resource, query);     
      
    	log.debug("Getting results of job: [{}] ", uwsjob.getResults());
        readFromURL( uwsjob.getFullQueryURL());


    	
    	
        }
    }
