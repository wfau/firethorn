// Copyright (c) The University of Edinburgh, 2012-2013.
//
// LICENCE-START
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// LICENCE-END

package uk.org.ogsadai.activity.astro;

import java.net.URI;
import java.util.Properties;

import junit.framework.TestCase;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.MockInputPipe;
import uk.org.ogsadai.activity.MockOutputPipe;
import uk.org.ogsadai.activity.extension.SimpleServiceAddresses;
import uk.org.ogsadai.authorization.NullSecurityContext;
import uk.org.ogsadai.config.KeyValueProperties;
import uk.org.ogsadai.config.SimpleKeyValueProperties;
import uk.org.ogsadai.resource.generic.GenericResource;

/**
 * Test for ADQLQueryActivity class.
 * 
 * @author The OGSA-DAI Project Team.
 *
 */
public class ADQLAsyncQueryActivityTest extends TestCase
{
    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE =
        "Copyright (c) The University of Edinburgh, 2012-2013.";
    
    public ADQLAsyncQueryActivityTest(final String name) throws Exception
    {
        super(name);
    }
    
    protected ADQLAsyncQueryActivity createActivity(String tapURL) throws Exception
    {
        super.setUp();
        GenericResource resource = new GenericResource();
        Properties properties = new Properties();
        properties.setProperty("dai.astro.tapurl", tapURL);
        
        MockGenericResourceState state = 
            new MockGenericResourceState(properties);
        resource.initialize(state);
        ADQLAsyncQueryActivity activity = new ADQLAsyncQueryActivity();
        
        KeyValueProperties keyValue = new SimpleKeyValueProperties();
        
        activity.configureActivity(keyValue);

        activity.setTargetResourceAccessor(
            resource.createResourceAccessor(new NullSecurityContext()));
        
        SimpleServiceAddresses serviceAddresses = 
                new SimpleServiceAddresses(
                new URI("http://myhost.com/DRES"),
                new URI("http://myhost.com/DRIS"),
                new URI("http://myhost.com/DSiS"),
                new URI("http://myhost.com/DSoS"),
                new URI("http://myhost.com/RMS"),
                new URI("http://myhost.com/SMS"));
        activity.setServiceAddresses(serviceAddresses);
        return activity;
    }

    public void testUKIDSSQuery() throws Exception
    {
//        String query = "SELECT TOP 100 ra, dec FROM lasSource";
        String query = "SELECT count(*) FROM lasSource";

        MockInputPipe input = new MockInputPipe(query);
        MockOutputPipe output = new MockOutputPipe(new Object[] {});

        ADQLAsyncQueryActivity activity = 
                createActivity("http://wfaudata.roe.ac.uk/ukidssDR6-dsa/TAP/");
        activity.addInput(ADQLQueryActivity.INPUT_ADQL_EXPRESSION, input);     
        activity.addOutput(ADQLQueryActivity.OUTPUT_SQL_RESULTS, output);
        
        activity.process();
        input.verify();
        assertEquals(4, output.getActualBlocks().size());
    }
    
    public void testUKIDSSError() throws Exception
    {
        String query = "SELECT xygty FROM lasSource";

        MockInputPipe input = new MockInputPipe(query);
        MockOutputPipe output = new MockOutputPipe(new Object[] {});
        
        ADQLAsyncQueryActivity activity = 
                createActivity("http://wfaudata.roe.ac.uk/ukidssDR6-dsa/TAP/");
        activity.addInput(ADQLQueryActivity.INPUT_ADQL_EXPRESSION, input);     
        activity.addOutput(ADQLQueryActivity.OUTPUT_SQL_RESULTS, output);
        
        try
        {
            activity.process();
            fail("Exception expected.");
        }
        catch (ActivityUserException e)
        {
            assertTrue(e.getCause() instanceof QueryFailedException);
        }
        
        input.verify();
        output.verify();
    }
    
    public void testUKIDSSNoADQL() throws Exception
    {
        String query = "This is not ADQL.";

        MockInputPipe input = new MockInputPipe(query);
        MockOutputPipe output = new MockOutputPipe(new Object[] {});
        
        ADQLAsyncQueryActivity activity = 
                createActivity("http://wfaudata.roe.ac.uk/ukidssDR6-dsa/TAP/");
        activity.addInput(ADQLQueryActivity.INPUT_ADQL_EXPRESSION, input);     
        activity.addOutput(ADQLQueryActivity.OUTPUT_SQL_RESULTS, output);
        
        try
        {
            activity.process();
            fail("Exception expected.");
        }
        catch (ActivityUserException e)
        {
            assertTrue(e.getCause() instanceof QueryFailedException);
        }
        
        input.verify();
        output.verify();
    }
    
    public void testCAOM() throws Exception
    {
        String query = "select TOP 10 * from caom.Observation";

        MockInputPipe input = new MockInputPipe(query);
        MockOutputPipe output = new MockOutputPipe(new Object[] {});
        
        ADQLAsyncQueryActivity activity = 
                createActivity("http://www.cadc-ccda.hia-iha.nrc-cnrc.gc.ca/tap/");
        activity.addInput(ADQLQueryActivity.INPUT_ADQL_EXPRESSION, input);     
        activity.addOutput(ADQLQueryActivity.OUTPUT_SQL_RESULTS, output);
        
        activity.process();
        input.verify();
        // LIST_BEGIN, metadata, 10 tuple blocks, LIST_END
        assertEquals(13, output.getActualBlocks().size());
    }
    
    public void testCAOMNoADQL() throws Exception
    {
        String query = "This is not ADQL.";

        MockInputPipe input = new MockInputPipe(query);
        MockOutputPipe output = new MockOutputPipe(new Object[] {});
        
        ADQLAsyncQueryActivity activity = 
                createActivity("http://www.cadc-ccda.hia-iha.nrc-cnrc.gc.ca/tap/");
        activity.addInput(ADQLQueryActivity.INPUT_ADQL_EXPRESSION, input);     
        activity.addOutput(ADQLQueryActivity.OUTPUT_SQL_RESULTS, output);
        
        try
        {
            activity.process();
            fail("Exception expected.");
        }
        catch (ActivityUserException e)
        {
            assertTrue(e.getCause() instanceof QueryFailedException);
        }
        
        input.verify();
        output.verify();
    }
        
    public void testGAVO() throws Exception
    {
        String query = "Select TOP 10 * From ohmaser.masers";

        MockInputPipe input = new MockInputPipe(query);
        MockOutputPipe output = new MockOutputPipe(new Object[] {});
        
        ADQLAsyncQueryActivity activity = 
                createActivity("http://dc.zah.uni-heidelberg.de/__system__/tap/run/tap/");
        activity.addInput(ADQLQueryActivity.INPUT_ADQL_EXPRESSION, input);     
        activity.addOutput(ADQLQueryActivity.OUTPUT_SQL_RESULTS, output);
        
        activity.process();
        input.verify();
        // LIST_BEGIN, metadata, 10 tuple blocks, LIST_END
        assertEquals(13, output.getActualBlocks().size());
    }
    
    public void testGAVOTableUnknown() throws Exception
    {
        String query = "SELECT TOP 10 * FROM xyz";

        MockInputPipe input = new MockInputPipe(query);
        MockOutputPipe output = new MockOutputPipe(new Object[] {});
        
        ADQLAsyncQueryActivity activity = 
                createActivity("http://dc.zah.uni-heidelberg.de/__system__/tap/run/tap/");
        activity.addInput(ADQLQueryActivity.INPUT_ADQL_EXPRESSION, input);     
        activity.addOutput(ADQLQueryActivity.OUTPUT_SQL_RESULTS, output);
        
        try
        {
            activity.process();
            fail("Exception expected.");
        }
        catch (ActivityUserException e)
        {
            assertTrue(e.getCause() instanceof QueryFailedException);
        }
        
        input.verify();
        output.verify();
    }

}
