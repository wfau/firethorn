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

import static org.junit.Assert.assertEquals;

import java.util.Iterator;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import uk.ac.roe.wfau.firethorn.adql.query.AbstractQueryTestBase;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.adql.query.TestPropertiesBase;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.SelectField;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.Level;
import uk.ac.roe.wfau.firethorn.adql.query.atlas.AtlasQueryTestBase;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.base.BaseComponent;
import uk.ac.roe.wfau.firethorn.meta.base.BaseResource;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcColumn;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;

/**
 *
 *
 */
@Slf4j
//@Ignore
public class DistributedQueryTestBase
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
        testSchema(adqlspace, jdbcspace, "local_twomass", "TWOMASS");
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

        testSchema(adqlspace, jdbcspace, "remote_twomass", "TWOMASS");

        }
    }

