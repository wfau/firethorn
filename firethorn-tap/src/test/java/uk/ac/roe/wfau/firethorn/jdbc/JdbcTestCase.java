/*
 *  Copyright (C) 2012 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.jdbc;

import java.io.FileInputStream;
import java.sql.Connection;

import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import uk.ac.roe.wfau.firethorn.test.TestBase;
import uk.ac.roe.wfau.firethorn.votable.VOTableStarTableParser;
import uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcResource;
import uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcResource.JdbcCatalog;
import uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcResource.JdbcColumn;
import uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcResource.JdbcSchema;
import uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcResource.JdbcTable;
import uk.ac.starlink.table.ColumnInfo;
import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.jdbc.JDBCFormatter;
import uk.ac.starlink.table.jdbc.WriteMode;
import uk.ac.starlink.table.jdbc.JDBCFormatter.SqlColumn;

/**
 *
 *
 */
@Slf4j
public class JdbcTestCase
extends TestBase
    {
    @Autowired
    ApplicationContext spring ;
    
    private JdbcResource resource ;

    public JdbcResource resource ()
        {
        return this.resource ;
        }

    @Test
    public void testOne()
    throws Exception
        {
        String catname = "Francis"  ;
        String schname = "Albert"   ;
        String tabname = "Augustus" ;
        //
        // Connect to the test database.
        DataSource source = (DataSource) spring.getBean("MemData");
        Connection connection = source.getConnection();

        connection.createStatement().executeUpdate(
            "CREATE SCHEMA " + schname + " AUTHORIZATION DBA ;"
            );
        connection.createStatement().executeUpdate(
            "SET SCHEMA " + schname + " ;"
            );

        //
        // Load test data from file.
        Iterable<StarTable> iter = VOTableStarTableParser.iterable(
            new FileInputStream(
                "src/test/data/votable/random/random-10.xml"
                )
            ); 
        //
        // Extract the first table.
        StarTable stars = iter.iterator().next();
        //
        // Alter the column names (if required). 
        stars.getColumnInfo(0).setName("one");
        stars.getColumnInfo(1).setName("two");
        //
        // Wrap the table in a JDBCFormatter. 
        JDBCFormatter formatter = new JDBCFormatter(
            connection,
            stars
            );
        //
        // Write the data to the database. 
        formatter.createJDBCTable(
            tabname,
            WriteMode.DROP_CREATE
            );
        //
        // Create an empty resource tree.
        resource = womble().resources().jdbc().create(
            this.unique(
                "base"
                ),
            source
            );
        //
        // Pull the metadata from the database
        resource.diff(
            true
            );        
        //
        // Verify we got what we expected.
        for (JdbcCatalog catalog : resource.catalogs().select())
            {
            log.debug("- Catalog [{}]", catalog.name());
            for (JdbcSchema schema : catalog.schemas().select())
                {
                log.debug("-- Schema [{}]", schema.name());
                for (JdbcTable table : schema.tables().select())
                    {
                    log.debug("--- Table [{}]", table.name());
                    for (JdbcColumn column : table.columns().select())
                        {
                        log.debug("---- Column [{}]", column.name());
                        }
                    }
                }
            }
        }
    }
