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
package uk.ac.roe.wfau.firethorn.adql.query.atlas;

import java.io.FileReader;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;
import uk.ac.roe.wfau.firethorn.meta.xml.MetaDocReader;

/**
 *
 *
 */
@Slf4j
public class MetaDocImportTestCase
extends AtlasQueryTestBase
    {

    @Test
    public void test001()
    throws Exception
        {
        log.debug("test ..");

        final JdbcResource jdbcspace = jdbcResource(
            "atlas.jdbc.resource"
            );         

        final AdqlResource adqlspace = factories().adql().resources().create(
            "temp"
            );
            
        final MetaDocReader reader = new MetaDocReader();

        
        final Iterable<AdqlSchema> one = reader.inport(
            new FileReader(
                "src/test/data/metadoc/twomass.003.xml"
                ),
            jdbcspace.schemas().select(
                "TWOMASS",
                "dbo"
                ),
                adqlspace
            );
        
        }
    }
