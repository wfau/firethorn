/*
 *  Copyright (C) 2013 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.job.test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.job.Job;
import uk.ac.roe.wfau.firethorn.job.Job.Status;
import uk.ac.roe.wfau.firethorn.test.TestRoot;

/**
 *
 *
 */
@Slf4j
public class TestJobTestCase
extends TestRoot
    {
    /**
     * Our local service implementations.
     *
     */
    protected TestJob.Services services()
        {
        return factories().tests();
        }


    @Test
    public void test000()
    throws Exception
        {
        log.debug("-- Creating test --");
        final Identifier ident  = services().factory().create(
            "fred",
            new Integer(
                20
                )
            ).ident();

        log.debug("Status [{}]", services().executor().status(ident));

        final TestJob job = services().resolver().select(
            ident
            );
        log.debug("Status  [{}]", job.status());
        log.debug("Length  [{}]", job.length());
        log.debug("Factory [{}]", job.factory());

        log.debug("-- Preparing test --");
        log.debug("Result [{}]", services().executor().prepare(ident));

        log.debug("-- Executing test --");
        final Future<Status> future = services().executor().execute(ident);
        log.debug("  Status [{}]", services().executor().status(ident));

        Status result = services().executor().status(ident);
        while ((result != Status.ERROR) && (future.isDone() == false))
            {
            try {
                log.debug("-- Checking future --");
                result = future.get(
                    2,
                    TimeUnit.SECONDS
                    );
                log.debug("Result [{}]", result);
                log.debug("Status [{}]", services().executor().status(ident));
                }
            catch (final TimeoutException ouch)
                {
                log.debug("Future timeout");
                }
            catch (final InterruptedException ouch)
                {
                log.debug("Future interrupted [{}]", ouch.getMessage());
                }
            catch (final ExecutionException ouch)
                {
                log.debug("ExecutionException [{}]", ouch.getMessage());
                result = Status.ERROR;
                }
            }
        log.debug("-- Test completed --");
        log.debug("Status [{}]", services().executor().status(ident));
        }

    @Test
    public void test001()
    throws Exception
        {
        log.debug("-- Creating test --");
        final TestJob job = services().factory().create(
            "fred",
            new Integer(
                5
                )
            );

        log.debug("Status  [{}]", job.status());
        log.debug("Length  [{}]", job.length());
        log.debug("Factory [{}]", job.factory());

        factories().tests().executor().update(
            job.ident(),
            Job.Status.RUNNING,
            10
            );

        Thread.sleep(20000);

        }
    }
