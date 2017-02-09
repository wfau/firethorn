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

package uk.ac.roe.wfau.firethorn.webapp.blue;

import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueTaskLogEntry;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityBeanImpl;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityBeanIter;
import uk.ac.roe.wfau.firethorn.webapp.control.EntityBean;

/**
 * An {@link EntityBean} wrapper for a {@link BlueTaskLogEntry}.
 *
 */
public class BlueTaskLogEntryBean
extends AbstractEntityBeanImpl<BlueTaskLogEntry>
implements EntityBean<BlueTaskLogEntry>
    {
    /**
     * An {@link EntityBean.Iter} wrapper for a {@link BlueTaskLogEntry} {@link Iterable}.
     *
     */
    public static class Iter
    extends AbstractEntityBeanIter<BlueTaskLogEntry, BlueTaskLogEntryBean>
        {
        /**
         * Public constructor.
         * @param iterable The {@link BlueTaskLogEntry} {@link Iterable} to wrap.
         *
         */
        public Iter(final Iterable<BlueTaskLogEntry> iterable)
            {
            super(
                iterable
                );
            }
        @Override
        public BlueTaskLogEntryBean bean(final BlueTaskLogEntry entity)
            {
            return new BlueTaskLogEntryBean(
                entity
                );
            }
        }

    /**
     * Public constructor.
     * @param entity The {@link BlueTaskLogEntry} to wrap.
     *
     */
    public BlueTaskLogEntryBean(final BlueTaskLogEntry entity)
        {
        super(
            BlueTaskLogEntryController.TYPE_URI,
            entity
            );
        }

    /**
     * Get a URL for the {@link BlueTaskLogEntry} subject.
     * @return A URL for the {@link BlueTaskLogEntry} subject.
     * @see BlueTaskLogEntry#task()
     *
     */
    public String getSubject()
        {
        if (entity().task() != null)
            {
            return entity().task().link();
            }
        else {
            return null ;
            }
        }

    /**
     * Get the {@link BlueTaskLogEntry} message.
     * @return The {@link BlueTaskLogEntry} message.
     * @see BlueTaskLogEntry#message()
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
     * Get the {@link BlueTaskLogEntry} level.
     * @return The {@link BlueTaskLogEntry} level.
     * @see BlueTaskLogEntry#level()
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