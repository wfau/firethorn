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
package uk.ac.roe.wfau.firethorn.blue.test ;

import lombok.extern.slf4j.Slf4j;

import org.junit.Before;
import org.junit.Ignore;

import uk.ac.roe.wfau.firethorn.adql.query.AbstractQueryTestBase;
import uk.ac.roe.wfau.firethorn.blue.BlueQuery;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;

/**
 *
 *
 */
@Slf4j
@Ignore
public abstract class BlueQueryTestBase
extends AbstractQueryTestBase
    {
    /**
     * Target catalog name.
     *
     */
    protected static final String ATLAS_VERSION = "ATLASDR1" ;

    /**
     * Simple test query.
     *
     */
	protected static final String SIMPLE_QUERY = "SELECT TOP 10 ra, dec FROM atlasSource" ;

    /**
     * Invalid test query.
     *
     */
	protected static final String INVALID_QUERY = "SELECT FROM WHERE" ;

    /**
     * Load our resources.
     *
     */
    @Before
    public void loadAtlasResources()
    throws Exception
        {
        log.debug("loadAtlasResources()");

        replace(
            "{ATLAS_VERSION}",
            ATLAS_VERSION
            );

        JdbcResource jdbcspace = jdbcResource(
            "atlas.jdbc.resource",
            "*",
            "Atlas JDBC",
            config().property("firethorn.data.url"),
            config().property("firethorn.data.user"),
            config().property("firethorn.data.pass"),
            config().property("firethorn.data.driver")
            );

        AdqlResource adqlspace = adqlResource(
            "atlas.adql.resource",
            "atlas.adql.resource"
            );

        testSchema(adqlspace, jdbcspace, ATLAS_VERSION, ATLAS_VERSION, "dbo");
        testSchema(adqlspace, jdbcspace, "ROSAT",       "ROSAT",       "dbo");
        testSchema(adqlspace, jdbcspace, "BestDR9",     "BestDR9",     "dbo");
        testSchema(adqlspace, jdbcspace, "TWOMASS",     "TWOMASS",     "dbo");

        }

    public void debug(final BlueQuery query)
		{
		log.debug("debug(BlueQuery)");
		log.debug("  ident [{}]", query.ident());
		log.debug("  state [{}]", query.state());
		}
    }
