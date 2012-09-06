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

import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import uk.ac.roe.wfau.firethorn.test.TestBase;
import uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcResource;

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
        DataSource source = (DataSource) spring.getBean("TestData");
        
        resource = womble().resources().jdbc().create(
            this.unique(
                "base"
                ),
            source
            );

        log.debug("Runing diff ... ");

        resource.diff(true);        
        
        }
    
    }
