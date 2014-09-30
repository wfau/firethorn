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
package uk.ac.roe.wfau.firethorn.adql.query.atlas ;

import lombok.extern.slf4j.Slf4j;

import org.junit.Before;
import org.junit.Ignore;

import uk.ac.roe.wfau.firethorn.adql.query.AbstractQueryTestBase;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;

/**
 *
 *
 */
@Slf4j
@Ignore
public abstract class AtlasQueryTestBase
extends AbstractQueryTestBase
    {

    protected static final String ATLAS_VERSION = "ATLASDR1" ;

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
            "atlas.jdbc.resource",
            "*",
            "spring:RoeATLAS"
            );

        AdqlResource adqlspace = adqlResource(
            "atlas.adql.resource",
            "atlas.adql.resource"
            );

        testSchema(adqlspace, jdbcspace, ATLAS_VERSION);
        testSchema(adqlspace, jdbcspace, "ROSAT");
        testSchema(adqlspace, jdbcspace, "BestDR9");
        testSchema(adqlspace, jdbcspace, "TWOMASS");

        }
    }
