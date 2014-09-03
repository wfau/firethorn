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
package uk.ac.roe.wfau.firethorn.meta.jdbc;

import static org.junit.Assert.*;

import java.sql.SQLException;

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


    private void scan(final JdbcMetadataScanner.Catalog catalog)
    throws SQLException
        {
        log.debug("<catalog name='{}'>", catalog.name());
        for (JdbcMetadataScanner.Schema schema : catalog.schemas().select())
            {
            scan(schema);
            }
        log.debug("</catalog>");
        }

    private void scan(final JdbcMetadataScanner.Schema schema)
    throws SQLException
        {
        log.debug("  <schema name='{}'>", schema.name());
        for (JdbcMetadataScanner.Table table : schema.tables().select())
            {
            scan(table);
            }
        log.debug("  </schema>");
        }

    private void scan(final JdbcMetadataScanner.Table table)
    throws SQLException
        {
        log.debug("    <table name='{}'>", table.name());
        for (JdbcMetadataScanner.Column column : table.columns().select())
            {
            scan(column);
            }
        log.debug("    </table>");
        }
    
    private void scan(final JdbcMetadataScanner.Column column)
        {
        log.debug("      <column size='{}' type='{}' name='{}'/>", column.strlen(), column.type(), column.name());
        }

    @Test
    public void test001()
    throws Exception
        {

        JdbcResource resource = jdbcResource(
            "atlas.jdbc.resource",
            "atlas.jdbc.resource",
            "*",
            "spring:RoeATLAS"
            );

        MSSQLMetadataScanner meta = new MSSQLMetadataScanner(
            resource.connection()
            );

        JdbcMetadataScanner.Catalog catalog = meta.catalogs().select("AtLaSdR1");
        assertNotNull(
            catalog
            );
        log.debug("<catalog name='{}'>", catalog.name());

        JdbcMetadataScanner.Schema schema = catalog.schemas().select("dBo");
        assertNotNull(
            schema
            );
        log.debug("  <schema name='{}'>", schema.name());

        JdbcMetadataScanner.Table table = schema.tables().select("AtLaSsOuRcE");
        assertNotNull(
            table
            );
        log.debug("    <table name='{}'>", table.name());

        JdbcMetadataScanner.Column column = table.columns().select("rA");
        assertNotNull(
            column
            );

        log.debug("      <column size='{}' type='{}' name='{}'/>", column.strlen(), column.type(), column.name());
        log.debug("    </table>");
        log.debug("  </schema>");
        log.debug("</catalog>");
        
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
            resource.connection()
            );
        try {
            JdbcMetadataScanner.Catalog catalog = meta.catalogs().select("ATLASDR1");
            scan(catalog);
            }
        catch (SQLException ouch)
            {
            log.debug("<exception class='{}' code='{}' state='{}' text='{}'/>", ouch.getClass().getName(), ouch.getErrorCode(), ouch.getSQLState(), ouch.getMessage());
            }
        finally
            {
            resource.connection().close();
            }
        }

    @Test
    public void test003()
    throws Exception
        {

        JdbcResource resource = jdbcResource(
            "atlas.jdbc.resource",
            "atlas.jdbc.resource",
            "*",
            "spring:RoeATLAS"
            );

        MSSQLMetadataScanner meta = new MSSQLMetadataScanner(
            resource.connection()
            );
        
        log.debug("<catalogs>");
        for (JdbcMetadataScanner.Catalog catalog : meta.catalogs().select())
            {
            try {
                scan(catalog);
                }
            catch (SQLException ouch)
                {
                final int code = ouch.getErrorCode() ;
                final String state = ouch.getSQLState();
                final String text = ouch.getMessage();
                
                log.debug("<exception class='{}' code='{}' state='{}' text='{}'>", ouch.getClass().getName(), code, state, text);
                log.debug("Connection closed [{}]", meta.connection().isClosed());

/*
 * code='0'   state='HY010' text='Invalid state, the Connection object is closed.'
 * code='21'  state='S1000' text='Warning: Fatal error 823 occurred at Sep  1 2014  3:55AM. Note the error and time, and contact your system administrator.' 
 * code='916' state='S1000' text='The server principal "atlasro" is not able to access the database "BEDB" under the current security context.'                 
 *                 
 */
                meta.handle(ouch);
                log.debug("</exception>");
                }
            }
        log.debug("</catalogs>");
        
        resource.connection().close();
        }
    
    }
