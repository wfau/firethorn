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
import uk.org.ogsadai.activity.astro.votable.VOTableMetaData;
import uk.org.ogsadai.resource.Resource;
import uk.org.ogsadai.resource.dataresource.string.StringResource;
import uk.org.ogsadai.test.TestProperties;

public class VOTableMetadataTest extends TestCase
{
    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2010";
    
    /** Path to test VOTable file. */
    public static final String VOTABLE_FILE = "dai.astro.votablefile";
    
    /** Test properties. */
    private TestProperties mTestProperties;
    
    /** Test VOTable file. */
    private File mVOTableFile;
    
    /** Star table. */
    private StarTable mStar;
    
    /** Resource. */
    private Resource mResource;
    
    /** DRES URI. */
    private URI mDRES;
    
    /** Table meta data. */
    private VOTableMetaData mMeta;
    
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
        mMeta = new VOTableMetaData(mStar, mDRES, mResource);
    }
    
    /**
     * Test getColumnCount(). Make sure correct column count is returned.
     * @throws Exception
     */
    public void testGetColumnCount() throws Exception
    {
        assertEquals(6, mMeta.getColumnCount());
    }
    
    /**
     * Test getColumnMetaData().
     * @throws Exception
     */
    public void testGetColumnMetaData() throws Exception {
        // Get valid columns
        mMeta.getColumnMetadata(0);
        mMeta.getColumnMetadata(2);
        mMeta.getColumnMetadata(5);

        // Try to get invalid columns
        try {
            mMeta.getColumnMetadata(-1);
            fail("Position -1 should not have been available. It's out of bounds.");
        }catch(ArrayIndexOutOfBoundsException e) {}
        try {
            mMeta.getColumnMetadata(7);
            fail("Position 6 should not have been available. It's out of bounds.");
        }catch(ArrayIndexOutOfBoundsException e) {}
    }
}
