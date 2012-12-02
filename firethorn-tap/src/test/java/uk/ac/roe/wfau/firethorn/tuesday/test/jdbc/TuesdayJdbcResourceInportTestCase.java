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
public class TuesdayJdbcResourceInportTestCase
    extends TuesdayJdbcResourceTestCase
    {

    public TuesdayJdbcResource inport(String catalog)
    throws Exception
        {
        log.debug("Catalog [{}]",catalog);
        String url = "jdbc:jtds:sqlserver://localhost:1433/" + catalog ;             

        TuesdayJdbcResource resource = factories().jdbc().resources().create(
            catalog
            );
        resource.connection().url(
            url
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
            resource.inport();
            }
        catch(Exception ouch)
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
            inport(
                "WORLDR2"
                )
            );            
        }

    //@Test
    public void test002()
    throws Exception
        {
        TuesdayJdbcConnection connection = new TuesdayJdbcConnectionEntity(
            "spring:RoeLiveData"
            );
        List<TuesdayJdbcResource> resources = new ArrayList<TuesdayJdbcResource>();

        for (String catalog : connection.catalogs())
            {
            resources.add(
                inport(
                    catalog
                    )
                );
            }
        for (TuesdayJdbcResource resource : resources)
            {
            display(
                resource
                );            
            }
        }
    }
