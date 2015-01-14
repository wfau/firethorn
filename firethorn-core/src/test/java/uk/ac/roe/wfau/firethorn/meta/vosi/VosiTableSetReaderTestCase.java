/*
 *  Copyright (C) 2014 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.meta.vosi;

import java.io.FileReader;
import java.net.URL;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaResource;
import uk.ac.roe.wfau.firethorn.meta.vosi.VosiTableSetReader;
import uk.ac.roe.wfau.firethorn.test.TestBase;

/**
 *
 *
 */
@Slf4j
public class VosiTableSetReaderTestCase
    extends TestBase
    {
    @Test
    public void test001()
    throws Exception
        {
        IvoaResource resource = factories().ivoa().resources().create(
            "cadc-tap",
            "cadc-tap"
            );
        
        VosiTableSetReader reader = new VosiTableSetReader(); 

        log.debug("Resource [{}][{}]", resource.name(), resource.ident());
        log.debug("Reading  [cadc-tableset.xml]");
        reader.inport(
            new FileReader(
                "src/test/data/vosi/original/cadc-tableset.xml"
                ),
            resource
            );
        }

    @Test
    public void test002()
    throws Exception
        {
        IvoaResource resource = factories().ivoa().resources().create(
            "gaia-tap",
            "gaia-tap"
            );
        
        VosiTableSetReader reader = new VosiTableSetReader(); 

        log.debug("Resource [{}][{}]", resource.name(), resource.ident());
        log.debug("Reading  [gaia-tableset.xml]");
        reader.inport(
            new FileReader(
                "src/test/data/vosi/original/gaia-tableset.xml"
                ),
            resource
            );
        }

    @Test
    public void test003()
    throws Exception
        {
        IvoaResource resource = factories().ivoa().resources().create(
            "gavo-tap",
            "gavo-tap"
            );
        
        VosiTableSetReader reader = new VosiTableSetReader(); 

        log.debug("Resource [{}][{}]", resource.name(), resource.ident());
        log.debug("Reading  [gavo-tableset.xml]");
        reader.inport(
            new FileReader(
                "src/test/data/vosi/original/gavo-tableset.xml"
                ),
            resource
            );
        }

    //@Test
    public void test004()
    throws Exception
        {
        IvoaResource resource = factories().ivoa().resources().create(
            "urn:test-4",
            "urn:test-4"
            );
        VosiTableSetReader reader = new VosiTableSetReader(); 
        log.debug("-- Reading [vizier-tableset.xml] --");
        reader.inport(
            new FileReader(
                "src/test/data/vosi/original/vizier-tableset.xml"
                ),
            resource
            );
        }

    @Test
    public void test005()
    throws Exception
        {
        IvoaResource resource = factories().ivoa().resources().create(
            "vizier-tap",
            "vizier-tap"
            );
        VosiTableSetReader reader = new VosiTableSetReader(); 

        log.debug("Resource [{}][{}]", resource.name(), resource.ident());
        log.debug("Reading  [vizier-tableset.xml]");
        reader.inport(
            new FileReader(
                "src/test/data/vosi/original/vizier-twomass.xml"
                ),
            resource
            );
        }
    
    //@Test
    public void test006()
    throws Exception
        {
        IvoaResource resource = factories().ivoa().resources().create(
            "urn:test-6",
            "urn:test-6"
            );
        VosiTableSetReader reader = new VosiTableSetReader(); 
        log.debug("-- Reading [vizier-tableset.xml] --");
        reader.inport(
            new FileReader(
                "src/test/data/vosi/original/vizier-tableset.xml"
                ),
            resource
            );
        log.debug("-- Reading [vizier-twomass.xml] --");
        reader.inport(
            new FileReader(
                "src/test/data/vosi/original/vizier-twomass.xml"
                ),
            resource
            );
        }

    /*
     * 
    @Test
    public void test007()
    throws Exception
        {
        IvoaResource resource = factories().ivoa().resources().create(
            "urn:test-7",
            "urn:test-7"
            );
        VosiTableSetReader reader = new VosiTableSetReader(); 
        log.debug("-- Reading [cadc-tableset.xml] --");
        reader.inport(
            new FileReader(
                "src/test/data/vosi/original/cadc-tableset.xml"
                ),
            resource
            );
 
        log.debug("-- Reading [gaia-tableset.xml] --");
        reader.inport(
            new FileReader(
                "src/test/data/vosi/original/gaia-tableset.xml"
                ),
            resource
            );
       
        log.debug("-- Reading [gavo-tableset.xml] --");
        reader.inport(
            new FileReader(
                "src/test/data/vosi/original/gavo-tableset.xml"
                ),
            resource
            );
        
        log.debug("-- Reading [vizier-tableset.xml] --");
        reader.inport(
            new FileReader(
                "src/test/data/vosi/original/vizier-tableset.xml"
                ),
            resource
            );

        log.debug("-- Reading [vizier-twomass.xml] --");
        reader.inport(
            new FileReader(
                "src/test/data/vosi/original/vizier-twomass.xml"
                ),
            resource
            );
        }
     * 
     */
    }
