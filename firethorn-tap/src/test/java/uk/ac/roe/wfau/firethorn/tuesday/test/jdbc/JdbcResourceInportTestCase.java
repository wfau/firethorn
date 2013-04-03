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

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcConnection;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcConnectionEntity;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;

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
            unique(
                "resource"
                ),
            catalog
            );
        resource.connection().url(
            config().getProperty(
                "firethorn.wfau.base"
                ) + "/" + catalog
            );
        resource.connection().user(
            config().getProperty(
                "firethorn.wfau.user"
                )
            );
        resource.connection().pass(
            this.config().getProperty(
                "firethorn.wfau.pass"
                )
            );
        try {
            resource.scan();
            }
        catch(final Exception ouch)
            {
            log.debug("Failed to import resource [{}]", ouch.getMessage());
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
                "WORLDR2"
                )
            );
        }

    //@Test
    public void test002()
    throws Exception
        {
        final JdbcConnection connection = new JdbcConnectionEntity(
            "spring:RoeTWOMASS"
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
            try {
                display(
                    resource
                    );
                }
            catch(final Exception ouch)
                {

                }
            }
        }
    }
