/*
 *
 */
package uk.ac.roe.wfau.firethorn.widgeon ;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URL;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.*;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional; 

import uk.ac.roe.wfau.firethorn.common.ident.Identifier;
import uk.ac.roe.wfau.firethorn.test.TestBase;

/**
 *
 */
@Slf4j
public class WidgeonTestCase
extends TestBase
    {

    @Test
    public void simple()
        {
        Widgeon one = womble().widgeons().create(
            "albert",
            URI.create("ivo://org.astrogrid.test/0001")
            );
        assertNotNull(
            one
            );
        assertNotNull(
            one.name()
            );
        log.debug("One [{}][{}]", one.ident(), one.name());

        Widgeon two = womble().widgeons().create(
            "albert",
            URI.create("ivo://org.astrogrid.test/0001")
            );
        assertNotNull(
            two
            );
        assertNotNull(
            two.name()
            );
        log.debug("Two [{}][{}]", two.ident(), two.name());

        //womble().hibernate().flush();

        for (Widgeon widgeon : womble().widgeons().select())
            {
            log.debug("Widgeon [{}]", widgeon);
            }

        }

    @Test
    public void nested()
        {
        nested(
            womble().widgeons().create(
                "widgeon-0001",
                URI.create("ivo://org.astrogrid.test/0001")
                )
            );
        nested(
            womble().widgeons().create(
                "widgeon-0002",
                URI.create("ivo://org.astrogrid.test/0002")
                )
            );

        //womble().hibernate().flush();

        for (Widgeon widgeon : womble().widgeons().select())
            {
            display(
                widgeon
                );
            }
        }

    public void nested(Widgeon widgeon)
        {
        nested(
            widgeon.schemas().create(
                "schema-0001"
                )
            );
        nested(
            widgeon.schemas().create(
                "schema-0002"
                )
            );
        }

    public void nested(Widgeon.Schema schema)
        {
        nested(
            schema.catalogs().create(
                "catalog-0001"
                )
            );
        nested(
            schema.catalogs().create(
                "catalog-0002"
                )
            );
        }

    public void nested(Widgeon.Schema.Catalog catalog)
        {
        nested(
            catalog.tables().create(
                "table-0001"
                )
            );
        nested(
            catalog.tables().create(
                "table-0002"
                )
            );
        }

    public void nested(Widgeon.Schema.Catalog.Table table)
        {
        table.columns().create(
            "column-0001"
            );
        table.columns().create(
            "column-0002"
            );
        }

    public void display(Widgeon widgeon)
        {
        log.debug("-------");
        log.debug("Widgeon [{}]", widgeon);
        for (Widgeon.Schema schema : widgeon.schemas().select())
            {
            log.debug("  Schema [{}]", schema);
            for (Widgeon.Schema.Catalog catalog : schema.catalogs().select())
                {
                log.debug("  Catalog [{}]", catalog);
                for (Widgeon.Schema.Catalog.Table table : catalog.tables().select())
                    {
                    log.debug("  Table [{}]", table);
                    for (Widgeon.Schema.Catalog.Table.Column column : table.columns().select())
                        {
                        log.debug("  Column [{}]", column);
                        }
                    }
                }
            }
        }
    }

