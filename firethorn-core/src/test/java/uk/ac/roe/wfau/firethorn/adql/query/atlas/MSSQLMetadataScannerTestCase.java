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

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.adql.query.AbstractQueryTestBase;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcMetadataScanner;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;
import uk.ac.roe.wfau.firethorn.meta.jdbc.sqlserver.MSSQLMetadataScanner;

/**
 *
 *
 */
@Slf4j
public class MSSQLMetadataScannerTestCase
extends AbstractQueryTestBase
    {
    @Test
    public void test000()
    throws Exception
        {

        JdbcResource resource = jdbcResource(
            "atlas.jdbc.resource",
            "atlas.jdbc.resource",
            "*",
            "spring:RoeATLAS"
            );

        MSSQLMetadataScanner meta = new MSSQLMetadataScanner(
            resource.connection().open()
            );
        
        log.debug("<catalogs>");
        for (JdbcMetadataScanner.Catalog catalog : meta.catalogs().select())
            {
            log.debug("  <catalog name='{}'>", catalog.name());
            try {
                for (JdbcMetadataScanner.Schema schema : catalog.schema().select())
                    {
                    log.debug("    <schema name='{}'>", schema.name());
                    log.debug("    </schema>");
                    }
                }
            catch (Exception ouch)
                {
                log.debug("<exception class='{}' text='{}'>", ouch.getClass().getName(), ouch.getMessage());
                }
            log.debug("  </catalog>");
            }
        log.debug("</catalogs>");
        
        resource.connection().close();
        }

    @Test
    public void test002()
    throws Exception
        {

        JdbcResource resource = jdbcResource(
            "atlas.jdbc.resource",
            "atlas.jdbc.resource",
            "*",
            "spring:RoeATLAS"
            );

        MSSQLMetadataScanner meta = new MSSQLMetadataScanner(
            resource.connection().open()
            );
        
        JdbcMetadataScanner.Catalog catalog = meta.catalogs().select("ATLASDR1");
        log.debug("  <catalog name='{}'>", catalog.name());
        try {
            for (JdbcMetadataScanner.Schema schema : catalog.schema().select())
                {
                log.debug("    <schema name='{}'>", schema.name());
                try {
                    for (JdbcMetadataScanner.Table table : schema.tables().select())
                        {
                        log.debug("    <table name='{}'>", table.name());
                        try {
                            for (JdbcMetadataScanner.Column column : table.columns().select())
                                {
                                log.debug("      <column name='{}'/>", column.name());
                                }
                            }
                        catch (Exception ouch)
                            {
                            log.debug("<exception class='{}' text='{}'>", ouch.getClass().getName(), ouch.getMessage());
                            }
                        log.debug("    </table>");
                        }
                    }
                catch (Exception ouch)
                    {
                    log.debug("<exception class='{}' text='{}'>", ouch.getClass().getName(), ouch.getMessage());
                    }
                log.debug("    </schema>");
                }
            }
        catch (Exception ouch)
            {
            log.debug("<exception class='{}' text='{}'>", ouch.getClass().getName(), ouch.getMessage());
            }
        log.debug("  </catalog>");
        
        resource.connection().close();
        }
    }
