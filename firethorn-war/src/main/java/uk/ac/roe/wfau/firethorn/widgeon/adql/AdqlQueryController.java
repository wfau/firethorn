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
package uk.ac.roe.wfau.firethorn.widgeon.adql;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import uk.ac.roe.wfau.firethorn.common.entity.annotation.UpdateAtomicMethod;
import uk.ac.roe.wfau.firethorn.common.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.tuesday.AdqlQuery;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityController;
import uk.ac.roe.wfau.firethorn.webapp.control.EntityBean;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.webapp.paths.PathImpl;

/**
 * Spring MVC controller for <code>AdqlQuery</code>.
 *
 */
@Slf4j
@Controller
@RequestMapping(AdqlQueryLinkFactory.QUERY_PATH)
public class AdqlQueryController
extends AbstractEntityController<AdqlQuery>
    {

    @Override
    public Path path()
        {
        return new PathImpl(
            AdqlQueryLinkFactory.QUERY_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public AdqlQueryController()
        {
        super();
        }

    /**
     * MVC property for the target entity.
     *
     */
    public static final String QUERY_ENTITY = "urn:adql.query.entity" ;

    /**
     * MVC property for updating the name.
     *
     */
    public static final String UPDATE_NAME = "adql.query.update.name" ;

    /**
     * MVC property for updating the query.
     *
     */
    public static final String UPDATE_QUERY = "adql.query.update.query" ;

    /**
     * MVC property for updating the status.
     *
     */
    public static final String UPDATE_STATUS = "adql.query.update.status" ;

    @Override
    public EntityBean<AdqlQuery> bean(final AdqlQuery entity)
        {
        return new AdqlQueryBean(
            entity
            );
        }

    @Override
    public Iterable<EntityBean<AdqlQuery>> bean(Iterable<AdqlQuery> iter)
        {
        return new AdqlQueryBean.Iter(
            iter
            );
        }

    /**
     * Get the target entity based on the ident in the path.
     * @throws NotFoundException
     *
     */
    @ModelAttribute(QUERY_ENTITY)
    public AdqlQuery entity(
        @PathVariable("ident")
        final String ident
        ) throws NotFoundException {
        return factories().adql().queries().select(
            factories().adql().queries().idents().ident(
                ident
                )
            );
        }

    /**
     * JSON GET request.
     *
     */
    @ResponseBody
    @RequestMapping(method=RequestMethod.GET, produces=JSON_MAPPING)
    public EntityBean<AdqlQuery> jsonSelect(
        @ModelAttribute(QUERY_ENTITY)
        final AdqlQuery entity
        ){
        return bean(
            entity
            );
        }

    /**
     * JSON POST update.
     *
     */
    @ResponseBody
    @UpdateAtomicMethod
    @RequestMapping(method=RequestMethod.POST, produces=JSON_MAPPING)
    public EntityBean<AdqlQuery> jsonUpdate(
        @RequestParam(value=UPDATE_NAME, required=false)
        final String name,
        @RequestParam(value=UPDATE_QUERY, required=false)
        final String query,
        @RequestParam(value=UPDATE_STATUS, required=false)
        final AdqlQuery.Status status,
        @ModelAttribute(QUERY_ENTITY)
        final AdqlQuery entity
        ){

        if (name != null)
            {
            if (name.length() > 0)
                {
                entity.name(
                    name
                    );
                }
            }

        if (query != null)
            {
            if (query.length() > 0)
                {
                entity.query(
                    query
                    );
                }
            }

        if (status != null)
            {
            entity.status(
                status
                );
            }

        return bean(
            entity
            );
        }
    }
