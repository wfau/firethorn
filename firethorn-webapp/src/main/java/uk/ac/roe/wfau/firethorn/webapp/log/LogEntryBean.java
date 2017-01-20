/*
 *  Copyright (C) 2017 Royal Observatory, University of Edinburgh, UK
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

package uk.ac.roe.wfau.firethorn.webapp.log;

import uk.ac.roe.wfau.firethorn.entity.log.LogEntry;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityBeanImpl;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityBeanIter;
import uk.ac.roe.wfau.firethorn.webapp.control.EntityBean;

/**
 * An {@link EntityBean} wrapper for a {@link LogEntry}.
 *
 */
public class LogEntryBean
extends AbstractEntityBeanImpl<LogEntry>
implements EntityBean<LogEntry>
    {
    /**
     * An {@link EntityBean.Iter} wrapper for a {@link LogEntry} {@link Iterable}.
     *
     */
    public static class Iter
    extends AbstractEntityBeanIter<LogEntry, LogEntryBean>
        {
        /**
         * Public constructor.
         * @param iterable The {@link LogEntry} {@link Iterable} to wrap.
         *
         */
        public Iter(final Iterable<LogEntry> iterable)
            {
            super(
                iterable
                );
            }
        @Override
        public LogEntryBean bean(final LogEntry entity)
            {
            return new LogEntryBean(
                entity
                );
            }
        }

    /**
     * Public constructor.
     * @param entity The {@link LogEntry} to wrap.
     *
     */
    public LogEntryBean(final LogEntry entity)
        {
        super(
            LogEntryIdentFactory.TYPE_URI,
            entity
            );
        }

    /**
     * Get a URL for the {@link LogEntry} subject.
     * @return A URL for the {@link LogEntry} subject.
     * @see LogEntry#subject()
     *
     */
    public String getSubject()
        {
        if (entity().subject() != null)
            {
            return entity().subject().link();
            }
        else {
            return null ;
            }
        }

    /**
     * Get the {@link LogEntry} message.
     * @return The {@link LogEntry} message.
     * @see LogEntry#message()
     *
     */
    public String getMessage()
        {
        if (entity().message() != null)
            {
            return entity().message();
            }
        else {
            return null ;
            }
        }

    /**
     * Get the {@link LogEntry} level.
     * @return The {@link LogEntry} level.
     * @see LogEntry#level()
     *
     */
    public String getLevel()
        {
        if (entity().level() != null)
            {
            return entity().level().name();
            }
        else {
            return null ;
            }
        }
    }