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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcConnector;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcConnectionEntity;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcSchema;

/**
 * TODO experiment with this
 * http://static.springsource.org/spring/docs/3.1.x/javadoc-api/org/springframework/jdbc/datasource/embedded/EmbeddedDatabaseBuilder.html
 *
 */
@Slf4j
public class JdbcResourceInportTestCase
    extends JdbcResourceTestThing
    {

    public JdbcResource create(final String catalog)
    throws Exception
        {
        log.debug("Catalog [{}]",catalog);

        final JdbcResource resource = factories().jdbc().resources().create(
            "test:ogsa",
            "test:resource",
            "spring:RoeATLAS"
            );
        resource.connection().user(
            config().property(
                "firethorn.atlas.user"
                )
            );
        resource.connection().pass(
            this.config().property(
                "firethorn.atlas.pass"
                )
            );
        try {
            resource.catalog(catalog);
            }
        catch(final Exception ouch)
            {
            log.debug("Failed to import resource catalog [{}][{}]", catalog, ouch.getMessage());
            }
        finally {
            resource.connection().close();
            }
        return resource;
        }

    @Test
    public void test001()
    throws Exception
        {
        display(
            create(
                "TWOMASS"
                )
            );
        }

    @Test
    public void test002()
    throws Exception
        {
        final JdbcConnector connection = new JdbcConnectionEntity(
            "spring:RoeATLAS"
            );
        final List<JdbcResource> resources = new ArrayList<JdbcResource>();

        for (final String catalog : connection.catalogs())
            {
            resources.add(
                create(
                    catalog
                    )
                );
            }
        for (final JdbcResource resource : resources)
            {
                display(
                    resource
                    );
            }
        }

    @Test
    public void test003()
    throws Exception
        {
        final JdbcResource created = factories().jdbc().resources().create(
            "TWOMASS",
            "test:atlas",
            "spring:RoeATLAS"
            );
        assertNotNull(
            created
            );

        final JdbcSchema s1 = created.schemas().select(
            "TWOMASS.dbo"
            );
        assertNotNull(
            s1
            );

        final JdbcSchema s2 = created.schemas().select(
            "TWOMASS.dbo"
            );
        assertNotNull(
            s2
            );
        }
    }
