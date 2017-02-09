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

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueQuery;
import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueTaskLogEntry;
import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierFormatException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractController;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappIdentFactory;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;

/**
 * Spring MVC controller to handle {@link BlueTaskLogEntry} entities.
 * <br/>Controller path : [{@value BlueTaskLogEntryLinkFactory#ENTITY_PATH}]
 *
 */
@Controller
@RequestMapping(BlueTaskLogEntryController.ENTITY_PATH)
public class BlueTaskLogEntryController
extends AbstractController
    {
    /**
     * The URI path for a {@link BlueTaskLogEntry} service, [{@value}].
     *
     */
    public static final String SERVICE_PATH = "/blue/log-entry" ;

    /**
     * The URI path for a {@link BlueTaskLogEntry} entity, [{@value}].
     *
     */
    public static final String ENTITY_PATH = SERVICE_PATH + "/" + Entity.LinkFactory.IDENT_TOKEN ;

    /**
     * The type URI for {@link BlueTaskLogEntry} entities, [{@value}].
     * 
     */
    public static final URI TYPE_URI = URI.create(
        "http://data.metagrid.co.uk/wfau/firethorn/types/log-entry-1.0.json"
        );
    
    @Override
    public Path path()
        {
        return path(
            ENTITY_PATH
            );
        }
    
    /**
     * Our{@link BlueTaskLogEntry.IdentFactory} implementation.
     *
     */
    @Component
    public static class IdentFactory
    extends WebappIdentFactory<BlueTaskLogEntry>
    implements BlueTaskLogEntry.IdentFactory
        {
        }

    /**
     * Our{@link BlueTaskLogEntry.LinkFactory} implementation.
     *
     */
    @Component
    public static class LinkFactory
    extends WebappLinkFactory<BlueTaskLogEntry>
    implements BlueTaskLogEntry.LinkFactory
        {
        protected LinkFactory()
            {
            super(
                SERVICE_PATH
                );
            }

        @Override
        public String link(final BlueTaskLogEntry query)
            {
            return link(
                ENTITY_PATH,
                query
                );
            }

        @Autowired
        private BlueTaskLogEntry.EntityServices services ;

        @Override
        public BlueTaskLogEntry resolve(final String link)
            throws IdentifierFormatException, IdentifierNotFoundException,
            EntityNotFoundException
            {
            if (this.matches(link))
                {
                return services.entities().select(
                    this.ident(
                        link
                        )
                    );
                }
            else {
                throw new EntityNotFoundException(
                    "Unable to resolve [" + link + "]"
                    );
                }
            }
        }

    
    /**
     * Public constructor.
     *
     */
    public BlueTaskLogEntryController()
        {
        super();
        }

    /**
     * HTTP GET request for a {@link BlueTaskLogEntry}.
     * <br/>Request path : [{@value BlueTaskLogEntryLinkFactory#ENTITY_PATH}]
     * @param ident The {@link BlueTaskLogEntry} {@Identifier} from the URL path, [{@value WebappLinkFactory.IDENT_FIELD}].
     * @return A {@link BlueTaskLogEntryBean} wrapping the {@link BlueTaskLogEntry}.
     * @throws EntityNotFoundException If the {@link BlueTaskLogEntry} could not be found.
     * 
     */
    @ResponseBody
    @RequestMapping(method=RequestMethod.GET, produces=JSON_MIME)
    public BlueTaskLogEntryBean select(
        @PathVariable(WebappLinkFactory.IDENT_FIELD)
        final String ident
        ) throws EntityNotFoundException  {
        return new BlueTaskLogEntryBean(
            factories().logger().entities().select(
                factories().logger().idents().ident(
                    ident
                    )
                )
            );
        }
    }
