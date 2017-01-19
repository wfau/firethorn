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
package uk.ac.roe.wfau.firethorn.webapp.log;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.log.LogEntry;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractController;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityBeanImpl;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityBeanIter;
import uk.ac.roe.wfau.firethorn.webapp.control.EntityBean;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;

/**
 * Spring MVC controller to handle {@link LogEntry} entities.
 * <br/>Controller path : [{@value LogEntryLinkFactory#ENTITY_PATH}]
 *
 */
@Controller
@RequestMapping(LogEntryLinkFactory.ENTITY_PATH)
public class LogEntryController
extends AbstractController
    {

    @Override
    public Path path()
        {
        return path(
            LogEntryLinkFactory.ENTITY_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public LogEntryController()
        {
        super();
        }

    /**
     * An {@link EntityBean} wrapper for a {@link LogEntry}.
     *
     */
    public static class LogEntryBean
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
        }

    /**
     * HTTP GET request for a {@link LogEntry}.
     * <br/>Request path : [{@value LogEntryLinkFactory#ENTITY_PATH}]
     * @param ident The {@link LogEntry} {@Identifier} from the URL path, [{@value WebappLinkFactory.IDENT_FIELD}].
     * @return A {@link LogEntryBean} wrapping the {@link LogEntry}.
     * @throws EntityNotFoundException If the {@link LogEntry} could not be found.
     * 
     */
    @ResponseBody
    @RequestMapping(method=RequestMethod.GET, produces=JSON_MIME)
    public LogEntryBean select(
        @PathVariable(WebappLinkFactory.IDENT_FIELD)
        final String ident
        ) throws EntityNotFoundException  {
        return new LogEntryBean(
            factories().logger().entities().select(
                factories().logger().idents().ident(
                    ident
                    )
                )
            );
        }
    }
