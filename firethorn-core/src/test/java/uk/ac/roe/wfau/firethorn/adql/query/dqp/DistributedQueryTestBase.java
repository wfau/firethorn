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
package uk.ac.roe.wfau.firethorn.adql.query.dqp ;

import lombok.extern.slf4j.Slf4j;

import org.junit.Before;
import org.junit.Ignore;

import uk.ac.roe.wfau.firethorn.adql.query.atlas.AtlasQueryTestBase;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;

/**
 *
 *
 */
@Slf4j
@Ignore
public abstract class DistributedQueryTestBase
extends AtlasQueryTestBase
    {

    /**
     * Load our resources.
     *
     */
    @Before
    public void loadLocalTwomass()
    throws Exception
        {
        log.debug("loadLocalTwomass()");
        AdqlResource adqlspace = adqlResource(
            "atlas.adql.resource"
            );
        JdbcResource jdbcspace = jdbcResource(
            "atlas.jdbc.resource"
            );
        testSchema(adqlspace, jdbcspace, "local_twomass", "TWOMASS", "dbo");
        }

    /**
     * Load our TWOMASS resources.
     *
     */
    @Before
    public void loadRemoteTwomass()
    throws Exception
        {
        log.debug("loadRemoteTwomass()");
        JdbcResource jdbcspace = jdbcResource(
            "twomass.jdbc.resource",
            "twomass.jdbc.resource",
            "*",
            "spring:RoeTWOMASS"
            );
        AdqlResource adqlspace = adqlResource(
            "twomass.adql.resource",
            "twomass.adql.resource"
            );
        testSchema(adqlspace, jdbcspace, "remote_twomass", "TWOMASS", "dbo");
        }
    }

