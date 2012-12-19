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

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import uk.ac.roe.wfau.firethorn.adql.AdqlDBTable;
import uk.ac.roe.wfau.firethorn.test.TestBase;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayAdqlColumn;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayAdqlResource;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayAdqlSchema;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayAdqlTable;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayJdbcResource;

/**
 * TODO experiment with DatabaseBuilder
 * http://static.springsource.org/spring/docs/3.1.x/javadoc-api/org/springframework/jdbc/datasource/embedded/EmbeddedDatabaseBuilder.html
 *
 */
@Slf4j
public class TuesdayJdbcAdqlTableTestCase
    extends TestBase
    {

	public TuesdayJdbcResource resource()
		{
        return factories().jdbc().resources().create(
            "test-resource",
            "spring:RoeTWOMASS"
            );
		}

	public TuesdayAdqlResource workspace()
		{
        return factories().adql().resources().create(
            "test-workspace"
            );
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
	            log.debug("---- Table [{}][{}][{}]", new Object[] {table.resource().name(), table.alias(), table.fullname()});
	            log.debug("---- Base  [{}][{}][{}]", new Object[] {table.base().resource().name(), table.base().alias(), table.base().fullname()});
	            for (final TuesdayAdqlColumn column : table.columns().select())
	                {
	                log.debug("----- Column [{}][{}][{}]", new Object[] {column.resource().name(),        column.alias(),        column.fullname()});
	                log.debug("----- Base   [{}][{}][{}]", new Object[] {column.base().resource().name(), column.base().alias(), column.base().fullname()});
	                }
	            }
	        }
	    }

	@Test
    public void test001()
    throws Exception
        {
        TuesdayJdbcResource resource = resource();
        TuesdayAdqlResource workspace = workspace();
        //
        // Import a JdbcTable into an AdqlSchema
        resource.inport();
        workspace.schemas().create(
            "test-schema"
            ).tables().create(
	    		resource.schemas().select("TWOMASS.dbo").tables().select("twomass_psc"),
	            "test-table"
	            ); 
        display(
    		workspace
    		);

        }

	@Test
    public void test002()
    throws Exception
        {
        TuesdayJdbcResource resource  = resource();
        TuesdayAdqlResource workspace = workspace();
        //
        // Import a JdbcSchema into our workspace.
        resource.inport();
        workspace.schemas().inport(
    		resource.schemas().select("TWOMASS.dbo"),
    		"test-schema"
            ); 
        display(
    		workspace
    		);
        }

    /**
     * Our autowired AdqlDBTable factory.
     *
     */
    @Autowired
    private AdqlDBTable.Factory tables;
    public AdqlDBTable.Factory tables()
    	{
    	return this.tables ;
    	}

    }
