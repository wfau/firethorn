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

import uk.ac.roe.wfau.firethorn.job.Job.Status;
import uk.ac.roe.wfau.firethorn.test.TestBase;

/**
 *
 *
 */
@Slf4j
public class TestJobTestCase
    extends TestBase
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
        {
        log.debug("Creating test job");
        
        TestJob job = services().factory().create(
            "fred",
            new Integer(
                20
                )
            );
        log.debug("  Status [{}]", job.status());

        log.debug("Preparing test job");
        job.prepare();
        log.debug("  Status [{}]", job.status());

        log.debug("Executing test job");
        Future<Status> future = job.execute();
        log.debug("  Status [{}]", job.status());

        Status result = job.status();
        while ((result != Status.ERROR) && (future.isDone() == false))
            {
            try {
                log.debug("Checking future");
                result = future.get(
                    2,
                    TimeUnit.SECONDS
                    );
                log.debug("Result [{}]", result);
                log.debug("Status [{}]", job.status());
                }
            catch (TimeoutException ouch)
                {
                log.debug("Future timeout");
                }
            catch (InterruptedException ouch)
                {
                log.debug("Future interrupted [{}]", ouch.getMessage());
                }
            catch (ExecutionException ouch)
                {
                log.debug("ExecutionException [{}]", ouch.getMessage());
                result = Status.ERROR;
                }
            }
        log.debug("-- Test completed [{}][{}]", result, job.status());
        }
    }
