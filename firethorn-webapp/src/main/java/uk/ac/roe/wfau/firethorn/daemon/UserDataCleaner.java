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
package uk.ac.roe.wfau.firethorn.daemon;

import lombok.extern.slf4j.Slf4j;

import org.joda.time.DateTime;
import org.joda.time.MutablePeriod;
import org.joda.time.ReadablePeriod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import uk.ac.roe.wfau.firethorn.entity.AbstractComponent;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcSchema;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTable;

/**
 *
 *
 */
@Slf4j
@Component
public class UserDataCleaner
extends AbstractComponent
    {

    private long count = 0L ;

    @Autowired
    private TaskScheduler scheduler;

    public TaskScheduler scheduler()
        {
        return this.scheduler;
        }

    public UserDataCleaner()
        {
        log.debug("UserDataCleaner()");
        }

    /*
    public void init()
        {
        log.debug("init()");
        scheduler.scheduleWithFixedDelay(
            new Runnable()
                {
                public void run()
                    {
                    log.debug("run()");
                    something();
                    }
                },
            new Long(60 * 1000)
            );
        }
    */

    /**
     * The action to take to clean up a table.
     *
     */
    public static enum Action
        {
        DELETE(),
        DROP();
        }

    /**
     * The action to take to clean up a table, DROP or DELETE.
     * 
     * Property : firethorn.cleaner.action
     * Default  : DROP
     *
     */
    @Value("${firethorn.cleaner.action:DROP}")
    Action action;

    /**
     * The interval between each run.
     * Expressed as a ISO_8601 duration.
     * https://en.wikipedia.org/wiki/ISO_8601#Durations
     *
     * Property : firethorn.cleaner.lifetime
     * Default  : PT24H
     *
     */
    @Value("${firethorn.cleaner.lifetime:PT24H}")
    String lifetime ;

    /**
     * The number of rows to delete on each run.
     * Property : firethorn.cleaner.pagesize
     * Default  : 10
     *
     */
    @Value("${firethorn.cleaner.pagesize:10}")
    int pagesize ;

    /**
     * The number of runs to skip at the start.
     * Property : firethorn.cleaner.skipfirst
     * Default : 5
     *
     */
    @Value("${firethorn.cleaner.skipfirst:5}")
    int skipfirst ;

    /**
     * The Spring Scheduled cron expression.
     * Property : firethorn.cleaner.cron
     * Default  : '0 0/10 * * * ?'
     *
     */
    @Scheduled(cron="${firethorn.cleaner.cron:0 0/10 * * * ?}")
    public void process()
        {
        log.debug("");
        log.debug("process()");
        log.debug("  count [{}]", count);

        /*
         * try/catch IllegalArgumentException
         * java.lang.IllegalArgumentException: Invalid format: "MUMBLE"
         *
         */

        /*
         * PT12H
         * https://en.wikipedia.org/wiki/ISO_8601#Durations
         * @todo Catch and handle syntax errors.
         * 
         */
        final ReadablePeriod period = MutablePeriod.parse(lifetime);
        log.debug(" pagesize [{}]", pagesize);
        log.debug(" lifetime [{}]", lifetime);
        log.debug(" period   [{}]", period);

        //
        // Skip the first few iterations.
        // Allow time for startup.
        if (count++ < skipfirst)
            {
            log.debug("skipping");
            return ;
            }

        factories().spring().transactor().update(
            new Runnable()
                {
                @Override
                public void run()
                    {
                    try {
                        final DateTime date = new DateTime().minus(
                            period
                            );
                        log.debug("  cutoff   [{}]", date);
                        final JdbcResource resource = factories().jdbc().resources().userdata();
                        log.debug("  resource [{}][{}]", resource.ident(), resource.name());

                        for (final JdbcSchema schema : resource.schemas().select())
                            {
                            log.debug("  schema [{}][{}]", schema.ident(), schema.name());
                            for (final JdbcTable table : schema.tables().pending(date, pagesize))
                                {
                                log.debug("  table [{}][{}][{}]", table.ident(), table.name(), table.created());
                                //table.drop();
                                table.meta().jdbc().status(
                                    JdbcTable.TableStatus.DROPPED
                                    );
                                }
                            }
                        }
                    catch (final Exception ouch)
                        {
                        log.warn("Exception in processing() [{}]", ouch.getMessage());
                        }
                    }
                }
            );
        log.debug("");
        }
    }
