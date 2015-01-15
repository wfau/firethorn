// Copyright (c) The University of Edinburgh, 2002-2010.
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

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.sql.SQLException;
import java.util.Iterator;

import junit.framework.Assert;
import junit.framework.TestCase;
import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.StoragePolicy;
import uk.ac.starlink.util.URLDataSource;
import uk.ac.starlink.votable.VOTableBuilder;
import uk.org.ogsadai.activity.Activity;
import uk.org.ogsadai.activity.MockOutputPipe;
import uk.org.ogsadai.activity.astro.votable.VOTableMetaData;
import uk.org.ogsadai.activity.extension.ResourceActivity;
import uk.org.ogsadai.activity.extension.ServiceAddresses;
import uk.org.ogsadai.activity.extension.ServiceAddressesActivity;
import uk.org.ogsadai.activity.extension.SimpleServiceAddresses;
import uk.org.ogsadai.activity.io.BlockReader;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.LiteralBlockReader;
import uk.org.ogsadai.activity.pipeline.SimpleActivityDescriptor;
import uk.org.ogsadai.activity.pipeline.SimpleLiteral;
import uk.org.ogsadai.authorization.NullSecurityContext;
import uk.org.ogsadai.metadata.MetadataWrapper;
import uk.org.ogsadai.resource.generic.GenericResource;
import uk.org.ogsadai.test.TestProperties;
import uk.org.ogsadai.tuple.TupleMetadata;

/**
 * Test for ADQLQueryActivity class.
 * 
 * @author The OGSA-DAI Project Team.
 *
 */
public class ADQLQueryActivityTest extends TestCase
{
    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE =
        "Copyright (c) The University of Edinburgh, 2010.";
    
    /** Test properties. */
    private final TestProperties mTestProperties;
    /** Activity to be tested. */
    private Activity mActivity;
    /** Resource to be used by activity. */
    private GenericResource mResource;
    /** Addresses of the various OGSA-DAI services. */
    private ServiceAddresses mServiceAddresses;
    
    /**
     * Constructor.
     * @param name
     * @throws Exception
     */
    public ADQLQueryActivityTest(final String name)
        throws Exception
    {
        super(name);
        mTestProperties = new TestProperties();
    }
    
    /**
     * {@inheritDoc}
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        mResource = new GenericResource();
        MockGenericResourceState state = new MockGenericResourceState(mTestProperties);
        mResource.initialize(state);
        mActivity = new ADQLQueryActivity();
        mActivity.setActivityDescriptor(
            new SimpleActivityDescriptor("ADQLQueryActivityTest"));

        ((ResourceActivity) mActivity).setTargetResourceAccessor(
            mResource.createResourceAccessor(new NullSecurityContext()));
        
        mServiceAddresses = new SimpleServiceAddresses(
                new URI("http://myhost.com/DRES"),
                new URI("http://myhost.com/DRIS"),
                new URI("http://myhost.com/DSiS"),
                new URI("http://myhost.com/DSoS"),
                new URI("http://myhost.com/RMS"),
                new URI("http://myhost.com/SMS"));
            ServiceAddressesActivity serviceAddressesActivity = 
                (ServiceAddressesActivity) mActivity;
            serviceAddressesActivity.setServiceAddresses(mServiceAddresses);
    }

    /**
     * Test a query.
     * 
     * @throws Exception
     */
    public void testQuery() throws Exception
    {
        String query = "SELECT top 100 p.objID, p.sky_u, p.sky_g, p.sky_r, p.sky_i, p.sky_z FROM PhotoObjAll AS p ORDER BY p.objID";

        BlockReader input = new LiteralBlockReader(new SimpleLiteral(query));
        MockOutputPipe output = new MockOutputPipe(new Object[] {});
        
        mActivity.addInput(ADQLQueryActivity.INPUT_ADQL_EXPRESSION, input);     
        mActivity.addOutput(ADQLQueryActivity.OUTPUT_SQL_RESULTS, output);
        
        mActivity.process();
        verifyOutput(query, output);
    }
    
    /**
     * Verify output.
     * 
     * @param query
     * @param output
     * @throws SQLException
     * @throws IOException
     */
    private void verifyOutput(String query, MockOutputPipe output) 
        throws Exception
    {
        // Query TAP service directly
        String tapService = mTestProperties.getProperty(ADQLQueryActivity.TAP_URL_KEY);
        String queryURLStr = tapService + "/sync?REQUEST=doQuery&VERSION=1.0&LANG=ADQL&QUERY=" + query;
        queryURLStr = queryURLStr.replaceAll(" ", "%20");
        URL queryURL = new URL(queryURLStr);
        
        URLDataSource source = new URLDataSource(queryURL);
        VOTableBuilder builder = new VOTableBuilder();
        StarTable star = builder.makeStarTable(source, false, StoragePolicy.getDefaultPolicy());

        // Get output iterator
        Iterator<Object> actualIterator = output.getActualBlocks().iterator();
        
        // Verify BEGIN block
        assertTrue(actualIterator.hasNext());
        assertEquals(ControlBlock.LIST_BEGIN, actualIterator.next());
        
        // Verify table meta data
        assertTrue(actualIterator.hasNext());
        Object next = actualIterator.next();
        assertTrue(
            "First item in list must be a MetadataWrapper",
            next instanceof MetadataWrapper);
        MetadataWrapper metadataWrapper = (MetadataWrapper) next;
        assertTrue(
            "MetadataWrapper must contain a TupleMetadata object",
            metadataWrapper.getMetadata() instanceof TupleMetadata);
        TupleMetadata tupleMetadata = 
            (TupleMetadata) metadataWrapper.getMetadata();
        compareMetadata(
            star, 
            tupleMetadata,
            mResource,
            mServiceAddresses.getDataRequestExecutionService());
    }
    
    public static void compareMetadata(StarTable star, TupleMetadata actual,
                                       GenericResource resource,
                                       URI dres)        
    throws SQLException
    {
        VOTableMetaData expected = new VOTableMetaData(star, dres, resource);
        Assert.assertEquals("Column count",
                expected.getColumnCount(),
                actual.getColumnCount());
        int numColumns = actual.getColumnCount();
        
        for (int i = 0; i < numColumns; ++i)
        {
            // Names.
            Assert.assertEquals("Names for column " + i,
                    expected.getColumnMetadata(i).getName(),
                    actual.getColumnMetadata(i).getName());
            // Types.
            Assert.assertEquals("Types for column " + i,
                    expected.getColumnMetadata(i).getType(),
                    actual.getColumnMetadata(i).getType());
            // Nullable.
            Assert.assertEquals(
                    "Nullable values for column " + i,
                    expected.getColumnMetadata(i).isNullable(),
                    actual.getColumnMetadata(i).isNullable());
            // Precision.
            expected.getColumnMetadata(i).getPrecision();
            actual.getColumnMetadata(i).getPrecision();
            Assert.assertEquals(
                    "Precision values for column " + i,
                    expected.getColumnMetadata(i).getPrecision(),
                    actual.getColumnMetadata(i).getPrecision());
            // Display size.
            Assert.assertEquals(
                    "Display sizes for column " + i,
                    expected.getColumnMetadata(i).getColumnDisplaySize(),
                    expected.getColumnMetadata(i).getColumnDisplaySize());
            // Check data resource ID
            Assert.assertEquals(
                    "Data resource ID for column " + i,
                    resource.getResourceID(),
                    actual.getColumnMetadata(i).getResourceID());
            // Check DRES URI
            Assert.assertEquals(
                    "DRES URI for column " + i,
                    dres,
                    expected.getColumnMetadata(i).getDRES());
            
            /* TODO: Actual returns "" whereas expected is part of the URL 
             * after .../TAP/. Need to use STIL library correctly to get 
             * the right values here. See VOTableColumnMetadata#getTableName()
            */   
            // Check column name.
            System.out.println(actual.getColumnMetadata(i).getTableName());
            System.out.println(expected.getColumnMetadata(i).getTableName());
            Assert.assertEquals(
                    "Table name for column " + i,
                    actual.getColumnMetadata(i).getTableName(),
                    expected.getColumnMetadata(i).getTableName());
        }
    }
}
