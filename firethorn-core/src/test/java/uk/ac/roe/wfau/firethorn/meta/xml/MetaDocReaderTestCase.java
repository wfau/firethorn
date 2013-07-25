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
package uk.ac.roe.wfau.firethorn.meta.xml;

import java.io.File;
import java.io.FileReader;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.meta.xml.MetaDocReader;
import uk.ac.roe.wfau.firethorn.test.TestBase;

/**
 *
 *
 */
@Slf4j
public class MetaDocReaderTestCase
//extends TestBase
    {

    @Test
    public void test000()
    throws Exception
        {
        MetaDocReader reader = new MetaDocReader();

        log.debug("PWD [{}]", new File("test").getCanonicalPath());
        reader.read(
            new FileReader(
                "src/test/data/metadoc/twomass.metadoc.xml"
                )
            );
        }
    }
