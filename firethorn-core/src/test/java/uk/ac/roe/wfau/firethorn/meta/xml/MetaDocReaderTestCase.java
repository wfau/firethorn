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

import javax.xml.stream.XMLEventReader;

import lombok.extern.slf4j.Slf4j;

import org.junit.Before;
import org.junit.Test;

import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.base.BaseSchema;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;
import uk.ac.roe.wfau.firethorn.meta.xml.MetaDocReader;
import uk.ac.roe.wfau.firethorn.test.TestBase;

/**
 *
 *
 */
@Slf4j
public class MetaDocReaderTestCase
extends TestBase
    {

    private JdbcResource resource  ;
    private AdqlResource workspace ;

    /**
     * Create our resources.
     *
     */
    @Before
    public void init()
    throws Exception
        {
        //
        // Create our JDBC resources.
        resource = factories().jdbc().resources().create(
            "twomass",
            "TWOMASS",
            "twomass",
            "spring:RoeTWOMASS"
            );
        //
        // Create our ADQL workspace.
        workspace = factories().adql().resources().create(
            "workspace"
            );
        }
    
    public void debug(Iterable<AdqlSchema> schemas)
        {
        log.debug("---------------");
        for(AdqlSchema schema : schemas)
            {
            log.debug("Schema [{}]", schema.namebuilder());
            for (AdqlTable table : schema.tables().select())
                {
                log.debug("  Table [{}]", table.namebuilder());
                log.debug("    Text [{}]", table.text());
                for (AdqlColumn column : table.columns().select())
                    {
                    log.debug("    Column [{}]", column.namebuilder());
                    log.debug("      Text  [{}]", column.text());
                    log.debug("      Type  [{}]", column.meta().adql().type());
                    log.debug("      Units [{}]", column.meta().adql().units());
                    log.debug("      Utype [{}]", column.meta().adql().utype());
                    log.debug("      UCD   [{}][{}]", column.meta().adql().ucd().type(), column.meta().adql().ucd().value());
                    }
                }
            }
        }

    //@Test
    public void test000()
    throws Exception
        {
        MetaDocReader reader = new MetaDocReader();

        Iterable<AdqlSchema> schemas = reader.inport(
            new FileReader(
                "src/test/data/metadoc/test/twomass.subset.xml"
                ),
            resource.schemas().select(
                "TWOMASS",
                "dbo"
                ),                
            workspace
            );

        debug(schemas);

        }

    @Test
    public void test001()
    throws Exception
        {
        MetaDocReader reader = new MetaDocReader();

        Iterable<AdqlSchema> one = reader.inport(
            new FileReader(
                "src/test/data/metadoc/test/twomass.001.xml"
                ),
            resource.schemas().select(
                "TWOMASS",
                "dbo"
                ),                
            workspace
            );

        debug(one);

        Iterable<AdqlSchema> two = reader.inport(
            new FileReader(
                "src/test/data/metadoc/test/twomass.002.xml"
                ),
            resource.schemas().select(
                "TWOMASS",
                "dbo"
                ),                
            workspace
            );

        debug(two);

        }
    }
