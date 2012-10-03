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
package uk.ac.roe.wfau.firethorn.widgeon.jdbc ;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import lombok.extern.slf4j.Slf4j;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.common.entity.exception.NameNotFoundException;

/**
 *
 */
@Slf4j
public class JdbcResourceTestCase
extends JdbcResourceTestBase
    {

    @Test
    public void test000()
    throws Exception
        {
        assertNotNull(
            base()
            );
        }

    @Test
    public void test001()
    throws Exception
        {
        assertIsNull(
            base().catalogs().select(
                "catalog-A"
                )
            );
        }

    @Test
    public void test002()
    throws Exception
        {
        assertNotNull(
            base().catalogs().create(
                "catalog-A"
                )
            );
        }

    @Test
    public void test003()
    throws Exception
        {
        assertNotNull(
            base().catalogs().create(
                "catalog-A"
                )
            );
        assertNotNull(
            base().catalogs().select(
                "catalog-A"
                )
            );
        }

    @Test
    public void test004()
    throws Exception
        {
        assertNotNull(
            base().catalogs().create(
                "catalog-A"
                )
            );
        assertIsNull(
            base().catalogs().select(
                "catalog-A"
                ).schemas().select(
                    "schema-A"
                    )
            );
        }

    @Test
    public void test005()
    throws Exception
        {
        assertNotNull(
            base().catalogs().create(
                "catalog-A"
                ).schemas().create(
                    "schema-A"
                    )
            );

        assertNotNull(
            base().catalogs().select(
                "catalog-A"
                ).schemas().select(
                    "schema-A"
                    )
            );
        }

    @Test
    public void test006()
    throws Exception
        {
        assertNotNull(
            base().catalogs().create(
                "catalog-A"
                ).schemas().create(
                    "schema-A"
                    )
            );
        assertIsNull(
            base().catalogs().select(
                "catalog-A"
                ).schemas().select(
                    "schema-A"
                    ).tables().select(
                        "table-A"
                        )
            );
        }

    @Test
    public void test007()
    throws Exception
        {
        assertNotNull(
            base().catalogs().create(
                "catalog-A"
                ).schemas().create(
                    "schema-A"
                    ).tables().create(
                        "table-A"
                        )
            );

        assertNotNull(
            base().catalogs().select(
                "catalog-A"
                ).schemas().select(
                    "schema-A"
                    ).tables().select(
                        "table-A"
                        )
            );
        }

    @Test
    public void test008()
    throws Exception
        {
        assertNotNull(
            base().catalogs().create(
                "catalog-A"
                ).schemas().create(
                    "schema-A"
                    ).tables().create(
                        "table-A"
                        )
            );
        assertIsNull(
            base().catalogs().select(
                "catalog-A"
                ).schemas().select(
                    "schema-A"
                    ).tables().select(
                        "table-A"
                        ).columns().select(
                            "column-A"
                            )
            );
        }

    @Test
    public void test009()
    throws Exception
        {
        assertNotNull(
            base().catalogs().create(
                "catalog-A"
                ).schemas().create(
                    "schema-A"
                    ).tables().create(
                        "table-A"
                        ).columns().create(
                            "column-A"
                            )
            );

        assertNotNull(
            base().catalogs().select(
                "catalog-A"
                ).schemas().select(
                    "schema-A"
                    ).tables().select(
                        "table-A"
                        ).columns().select(
                            "column-A"
                            )
            );
        }

    @Test
    public void test010()
    throws Exception
        {
        assertNotNull(
            base().catalogs().create(
                "catalog-A"
                ).schemas().create(
                    "schema-A"
                    ).tables().create(
                        "table-A"
                        ).columns().create(
                            "column-A"
                            )
            );

        assertIsNull(
            base().catalogs().select(
                "catalog-A"
                ).schemas().select(
                    "schema-A"
                    ).tables().select(
                        "table-A"
                        ).columns().select(
                            "column-a"
                            )
            );
        }

    @Test
    public void test011()
    throws Exception
        {
        assertNotNull(
            base().catalogs().create(
                "catalog-A"
                ).schemas().create(
                    "schema-A"
                    ).tables().create(
                        "table-A"
                        ).columns().create(
                            "column-A"
                            )
            );

        assertIsNull(
            base().catalogs().select(
                "catalog-A"
                ).schemas().select(
                    "schema-A"
                    ).tables().select(
                        "table-A"
                        ).columns().select(
                            "column-a"
                            )
            );

        base().catalogs().select(
            "catalog-A"
            ).schemas().select(
                "schema-A"
                ).tables().select(
                    "table-A"
                    ).columns().select(
                        "column-A"
                        ).name(
                            "column-a"
                            );

        assertNotNull(
            base().catalogs().select(
                "catalog-A"
                ).schemas().select(
                    "schema-A"
                    ).tables().select(
                        "table-A"
                        ).columns().select(
                            "column-a"
                            )
            );

        assertIsNull(
            base().catalogs().select(
                "catalog-A"
                ).schemas().select(
                    "schema-A"
                    ).tables().select(
                        "table-A"
                        ).columns().select(
                            "column-A"
                            )
            );
        }
    }

