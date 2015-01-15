// Copyright (c) The University of Edinburgh, 2010.
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

import java.io.File;
import java.net.URI;

import junit.framework.TestCase;
import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.StoragePolicy;
import uk.ac.starlink.util.FileDataSource;
import uk.ac.starlink.votable.VOTableBuilder;
import uk.org.ogsadai.activity.astro.votable.VOTableColumnMetadata;
import uk.org.ogsadai.resource.Resource;
import uk.org.ogsadai.resource.dataresource.string.StringResource;
import uk.org.ogsadai.test.TestProperties;
import uk.org.ogsadai.tuple.TupleTypes;

/***
 * Test class for VOTableColumnMetadata. Class uses a VOTable file defined by 
 * the VOTABLE_FILE constant.
 * 
 * @author The OGSA-DAI Project Team.
 *
 */
public class VOTableColumnMetadataTest extends TestCase
{
    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2010";

    /** Path to test VOTable file. */
    public static final String VOTABLE_FILE = "dai.astro.votablefile";
    
    /** Test properties. */
    private TestProperties mTestProperties;
    
    /** VOTable file. */
    private File mVOTableFile;
    
    /** Star table. */
    private StarTable mStar;
    
    /** Resource this file is targeted at. */
    private Resource mResource;
    
    /** DRES URI. */
    private URI mDRES;
    
    /**
     * {@inheritDoc}
     */
    public void setUp() throws Exception
    {
        mTestProperties = new TestProperties();
        // Prepare StarTable object
        mVOTableFile = new File(mTestProperties.getProperty(VOTABLE_FILE));
        FileDataSource source = new FileDataSource(mVOTableFile);
        VOTableBuilder builder = new VOTableBuilder();
        mStar = builder.makeStarTable(
            source, false, StoragePolicy.getDefaultPolicy());
        mDRES = new URI("http://ogsadai.sourceforge.net/");
        StringResource resource = new StringResource();
        mResource = resource;
    }
    
    /**
     * Test getDRES(). Function is a getter.
     * 
     * @throws Exception
     */
    public void testGetDRES() throws Exception
    {
        VOTableColumnMetadata meta = 
            new VOTableColumnMetadata(mStar, 1, mDRES, mResource);
        assertEquals(mDRES, meta.getDRES());
    }
    
    /**
     * Test getName().
     * 
     * @throws Exception
     */
    public void testGetName() throws Exception
    {
        VOTableColumnMetadata meta;
        meta = new VOTableColumnMetadata(mStar, 0, mDRES, mResource);
        assertEquals("sourceID", meta.getName());
        meta = new VOTableColumnMetadata(mStar, 5, mDRES, mResource);
        assertEquals("nFrames", meta.getName());
    }
    
    /**
     * Test getTableName().
     * 
     * @throws Exception
     */
    public void testGetTableName() throws Exception
    {
        VOTableColumnMetadata meta;
        meta = new VOTableColumnMetadata(mStar, 0, mDRES, mResource);
        assertEquals(mVOTableFile.getName(), meta.getTableName());
    }
    
    /**
     * Test getType().
     * 
     * @throws Exception
     */
    public void testGetType() throws Exception
    {
        VOTableColumnMetadata meta;
        meta = new VOTableColumnMetadata(mStar, 0, mDRES, mResource);
        assertEquals(TupleTypes._STRING, meta.getType());
        meta = new VOTableColumnMetadata(mStar, 1, mDRES, mResource);
        assertEquals(TupleTypes._INT, meta.getType());
        meta = new VOTableColumnMetadata(mStar, 2, mDRES, mResource);
        assertEquals(TupleTypes._LONG, meta.getType());
        meta = new VOTableColumnMetadata(mStar, 3, mDRES, mResource);
        assertEquals(TupleTypes._DOUBLE, meta.getType());
        meta = new VOTableColumnMetadata(mStar, 4, mDRES, mResource);
        assertEquals(TupleTypes._FLOAT, meta.getType());
        meta = new VOTableColumnMetadata(mStar, 5, mDRES, mResource);
        assertEquals(TupleTypes._SHORT, meta.getType());
    }
}
