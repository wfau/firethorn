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
package uk.ac.roe.wfau.firethorn.tuesday.test.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import uk.ac.roe.wfau.firethorn.test.TestBase;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayAdqlColumn;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayAdqlResource;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayAdqlSchema;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayAdqlTable;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayFactories;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayJdbcColumn;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayJdbcConnection;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayJdbcConnectionEntity;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayJdbcResource;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayJdbcSchema;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayJdbcTable;

/**
 * TODO experiment with this
 * http://static.springsource.org/spring/docs/3.1.x/javadoc-api/org/springframework/jdbc/datasource/embedded/EmbeddedDatabaseBuilder.html
 *
 */
@Slf4j
public class TuesdayJdbcAdqlTableTestCase
    extends TuesdayJdbcResourceTestCase
    {

    @Test
    public void test001()
    throws Exception
        {
        TuesdayJdbcResource jdbcResource = factories().jdbc().resources().create(
            "test-resource",
            "spring:RoeLiveData"
            );
        jdbcResource.inport();

        display(jdbcResource);

        TuesdayAdqlResource adqlWorkspace = factories().adql().resources().create(
            "test-workspace"
            );
        
        TuesdayAdqlSchema adqlSchema = adqlWorkspace.schemas().create(
            "wednesday"
            ); 

        TuesdayJdbcSchema jdbcSchema = jdbcResource.schemas().select("dbo");
        TuesdayJdbcTable jdbcTable   = jdbcSchema .tables().select("twomass_psc");

        //
        // Import a JdbcTable into an AdqlSchema
        TuesdayAdqlTable adqlTable = adqlSchema.tables().create(
            jdbcTable,
            "albert"
            ); 
        
        display(adqlWorkspace);

        }

    public void display(final TuesdayAdqlResource resource)
        {
        log.debug("---");
        log.debug("- ADQL resource [{}]", resource.name());

        for (final TuesdayAdqlSchema schema : resource.schemas().select())
            {
            log.debug("--- Schema [{}][{}]", new Object[] {resource.name(), schema.name()});
            for (final TuesdayAdqlTable table : schema.tables().select())
                {
                log.debug("---- Table [{}][{}.{}]", new Object[] {resource.name(), schema.name(), table.name()});
                for (final TuesdayAdqlColumn column : table.columns().select())
                    {
                    log.debug("----- Column [{}][{}][{}]", new Object[] {column.resource().name(),        column.alias(),        column.fullname()});
                    log.debug("----- Base   [{}][{}][{}]", new Object[] {column.base().resource().name(), column.base().alias(), column.base().fullname()});
                    }
                }
            }
        }
    }
