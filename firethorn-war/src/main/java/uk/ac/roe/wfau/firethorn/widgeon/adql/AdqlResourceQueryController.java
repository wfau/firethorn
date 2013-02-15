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

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityController;
import uk.ac.roe.wfau.firethorn.webapp.control.EntityBean;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.webapp.paths.PathImpl;

/**
 * Spring MVC controller for <code>AdqlResource</code> schema.
 *
 */
@Slf4j
@Controller
@RequestMapping(AdqlResourceLinkFactory.RESOURCE_QUERY_PATH)
public class AdqlResourceQueryController
extends AbstractEntityController<AdqlQuery>
    {
    @Override
    public Path path()
        {
        return new PathImpl(
            AdqlResourceLinkFactory.RESOURCE_QUERY_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public AdqlResourceQueryController()
        {
        super();
        }

    /**
     * URL path for the select method.
     *
     */
    public static final String SELECT_PATH = "select" ;

    /**
     * URL path for the search method.
     *
     */
    public static final String SEARCH_PATH = "search" ;

    /**
     * URL path for the create method.
     *
     */
    public static final String CREATE_PATH = "create" ;

    /**
     * MVC property for the search text.
     *
     */
    public static final String SEARCH_TEXT = "adql.resource.query.search.text" ;

    /**
     * MVC property for the search results.
     *
     */
    public static final String SEARCH_RESULT = "adql.resource.query.search.result" ;

    /**
     * MVC property for the create name.
     *
     */
    public static final String CREATE_NAME = "adql.resource.query.create.name" ;

    /**
     * MVC property for the create query.
     *
     */
    public static final String CREATE_QUERY = "adql.resource.query.create.query" ;

    @Override
    public EntityBean<AdqlQuery> bean(final AdqlQuery entity)
        {
        return new AdqlQueryBean(
            entity
            );
        }

    @Override
    public Iterable<EntityBean<AdqlQuery>> bean(final Iterable<AdqlQuery> iter)
        {
        return new AdqlQueryBean.Iter(
            iter
            );
        }

    /**
     * Get the parent resource based on the request ident.
     * @throws NotFoundException
     *
     */
    @ModelAttribute(AdqlResourceController.RESOURCE_ENTITY)
    public AdqlResource resource(
        @PathVariable("ident")
        final String ident
        ) throws NotFoundException {
        return factories().adql().resources().select(
            factories().adql().resources().idents().ident(
                ident
                )
            );
        }

    /**
     * JSON GET request to select all.
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.GET, produces=JSON_MAPPING)
    public Iterable<EntityBean<AdqlQuery>> jsonSelect(
        @ModelAttribute(AdqlResourceController.RESOURCE_ENTITY)
        final AdqlResource resource
        ){
        return bean(
            resource.queries().select()
            );
        }

    /**
     * JSON request to search by text.
     *
     */
    @ResponseBody
    @RequestMapping(value=SEARCH_PATH, params=SEARCH_TEXT, produces=JSON_MAPPING)
    public Iterable<EntityBean<AdqlQuery>> jsonSearch(
        @ModelAttribute(AdqlResourceController.RESOURCE_ENTITY)
        final AdqlResource resource,
        @RequestParam(SEARCH_TEXT)
        final String text
        ){
        log.debug("jsonSearch(String) [{}]", text);
        return bean(
            resource.queries().search(
                text
                )
            );
        }

    /**
     * JSON POST request to create a new query.
     *
     */
    @RequestMapping(value=CREATE_PATH, method=RequestMethod.POST, produces=JSON_MAPPING)
    public ResponseEntity<EntityBean<AdqlQuery>> jsonCreate(
        @ModelAttribute(AdqlResourceController.RESOURCE_ENTITY)
        final AdqlResource resource,
        @RequestParam(value=CREATE_QUERY, required=true)
        final String query,
        @RequestParam(value=CREATE_NAME, required=false)
        final String name
        ){
        log.debug("jsonCreate(String) [{}]", name);
        return response(
            new AdqlQueryBean(
                resource.queries().create(
                    name,
                    query
                    )
                )
            );
        }
    }
