/*
 *
 */
package uk.ac.roe.wfau.firethorn.mallard ;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URL;
import java.util.Iterator;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.*;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional; 

import uk.ac.roe.wfau.firethorn.test.TestBase;

import uk.ac.roe.wfau.firethorn.common.ident.Identifier;
import uk.ac.roe.wfau.firethorn.widgeon.Widgeon;

import uk.ac.roe.wfau.firethorn.common.entity.annotation.CreateAtomicMethod;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.CreateEntityMethod;

/**
 *
 */
@Slf4j
public class JobTestCase
extends TestBase
    {

    private static Identifier[] ident = new Identifier[10] ;

    private static final int NUM_JOBS = 1000 ;

    @Test
    public void test000()
        {
        ident[0] = womble().mallards().create(
            "test-mallard"
            ).ident();
        ident[1] = womble().mallards().create(
            "test-mallard"
            ).ident();
        }

    @Test
    public void test001()
        {
        assertNotNull(
            ident[0]
            );
        Mallard mallard = womble().mallards().select(
            ident[0]
            );
        assertFalse(
            womble().hibernate().session().isDirty()
            );
        assertEquals(
            0,
            count(
                mallard.jobs().select()
                )
            );
        mallard.jobs().create(
            "job-name",
            "job-text",
            "job-adql"            
            );
        assertEquals(
            1,
            count(
                mallard.jobs().select()
                )
            );
        }

    @Test
    public void test002()
        {
        assertNotNull(
            ident[0]
            );
        Mallard mallard = womble().mallards().select(
            ident[0]
            );
        assertFalse(
            womble().hibernate().session().isDirty()
            );
        assertEquals(
            1,
            count(
                mallard.jobs().select()
                )
            );
        mallard.jobs().create(
            "job-name",
            "job-text",
            "job-adql"            
            );
        assertEquals(
            2,
            count(
                mallard.jobs().select()
                )
            );
        }

    @Test
    public void test003()
        {
        assertNotNull(
            ident[0]
            );
        Mallard mallard = womble().mallards().select(
            ident[0]
            );
        assertFalse(
            womble().hibernate().session().isDirty()
            );
        assertEquals(
            2,
            count(
                mallard.jobs().select()
                )
            );
        }

    @Test
    public void test004()
        {
        assertNotNull(
            ident[1]
            );
        Mallard mallard = womble().mallards().select(
            ident[1]
            );
        assertFalse(
            womble().hibernate().session().isDirty()
            );
        assertEquals(
            0,
            count(
                mallard.jobs().select()
                )
            );
        for (int i = 0 ; i < NUM_JOBS ; i++)
            {
            mallard.jobs().create(
                "job-name",
                "job-text",
                "job-adql"            
                );
            }
        assertEquals(
            NUM_JOBS,
            count(
                mallard.jobs().select()
                )
            );
        }

    @Test
    public void test005()
        {
        assertNotNull(
            ident[1]
            );
        Mallard mallard = womble().mallards().select(
            ident[1]
            );
        assertFalse(
            womble().hibernate().session().isDirty()
            );
        assertEquals(
            NUM_JOBS,
            count(
                mallard.jobs().select()
                )
            );
        }
    }

