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

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaResource;
import uk.ac.roe.wfau.firethorn.meta.vosi.VosiTableSetReader;
import uk.ac.roe.wfau.firethorn.test.TestBase;

/**
 *
 *
 */
public class VizierTableSetReaderTestCase
    extends TestBase
    {

    @Test
    public void test001()
    throws Exception
        {
        IvoaResource resource = factories().ivoa().resources().entities().create(
            "urn:test-1",
            "urn:test-1"
            );
        
        VizierTableSetReader reader = new VizierTableSetReader(); 

        reader.inport(
            new URL("http://tapvizier.u-strasbg.fr/TAPVizieR/tap/tables"),
            resource
            );
        }
    }
