/*
 *  Copyright (C) 2014 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.webapp.identity;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcColumn;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTable;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityController;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlQueryBean;

/**
 * Spring MVC controller for {@link Identity} queries.
 *
 */
@Slf4j
@Controller
@RequestMapping(IdentityLinkFactory.QUERY_PATH)
public class IdentityQueryController
extends AbstractEntityController<AdqlQuery, AdqlQueryBean>
    {
    @Override
    public Path path()
        {
        return path(
            IdentityLinkFactory.QUERY_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public IdentityQueryController()
        {
        super();
        }

    /**
     * MVC property for the saved criteria.
     *
     */
    public static final String SELECT_SAVED = "identity.query.saved" ;

    @Override
    public AdqlQueryBean bean(final AdqlQuery entity)
        {
        return new AdqlQueryBean(
            entity
            );
        }

    @Override
    public Iterable<AdqlQueryBean> bean(final Iterable<AdqlQuery> iter)
        {
        return new AdqlQueryBean.Iter(
            iter
            );
        }

    /**
     * Get the parent identity based on the identifier in the request.
     * @throws EntityNotFoundException
     *
     */
    @ModelAttribute(IdentityController.TARGET_ENTITY)
    public Identity parent(
        @PathVariable(WebappLinkFactory.IDENT_FIELD)
        final String ident
        ) throws EntityNotFoundException {
        log.debug("identity() [{}]", ident);
        return factories().identities().select(
            factories().identities().idents().ident(
                ident
                )
            );
        }

    /**
     * JSON GET request to select all.
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.GET, produces=JSON_MIME)
    public Iterable<AdqlQueryBean> select(
        @ModelAttribute(IdentityController.TARGET_ENTITY)
        final Identity identity
        ){
        log.debug("select()");
        return bean(
            identity.queries().select()
            );
        }

    /**
     * JSON request to select by {@link AdqlQuery.Saved}.
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, params=SELECT_SAVED, produces=JSON_MIME)
    public Iterable<AdqlQueryBean> select(
        @ModelAttribute(IdentityController.TARGET_ENTITY)
        final Identity identity,
        @RequestParam(SELECT_SAVED)
        final AdqlQuery.Saved saved
        ){
        log.debug("select(Saved) [{}]", saved);
        return bean(
            identity.queries().select(
                saved
                )
            );
        }
    }
