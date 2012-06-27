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
package uk.ac.roe.wfau.firethorn.widgeon ;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.*;

import uk.ac.roe.wfau.firethorn.test.TestBase;

import uk.ac.roe.wfau.firethorn.common.entity.Identifier;
import uk.ac.roe.wfau.firethorn.common.entity.Identifier;

/**
 *
 */
@Slf4j
public class CombinedTestCase
extends TestBase
    {

    private Widgeon.Base base  ;

    @Before
    public void before()
        {
        base = womble().widgeons().create(
            this.unique(
                "base"
                ),
            URI.create(
                "ivo://org.astrogrid.test/test-data"
                )
            );
        }

    @Test
    public void test000()
        {
        assertNotNull(
            base
            );
        }

    @Test
    public void test001()
        {
        assertNotNull(
            base.schemas().create(
                "schema-A"
                )
            );
        }

    @Test
    public void test002()
        {
        assertNotNull(
            base.schemas().create(
                "schema-A"
                )
            );
        assertNotNull(
            base.schemas().select(
                "schema-A"
                )
            );
        }

    @Test
    public void test003()
        {
        assertNotNull(
            base.schemas().create(
                "schema-A"
                )
            );
        assertNotNull(
            base.schemas().select(
                "schema-A"
                ).catalogs().create(
                    "catalog-A"
                    )
            );

        assertNotNull(
            base.schemas().select(
                "schema-A"
                )
            );
        assertNotNull(
            base.schemas().select(
                "schema-A"
                ).catalogs().select(
                    "catalog-A"
                    )
            );
        }
    }

