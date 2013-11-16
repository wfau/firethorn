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
import uk.ac.roe.wfau.firethorn.adql.query.QueryProcessingException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityController;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;

/**
 * Spring MVC controller for <code>AdqlResource</code> schema.
 *
 */
@Slf4j
@Controller
@RequestMapping(AdqlSchemaLinkFactory.SCHEMA_QUERY_PATH)
public class AdqlSchemaQueryController
extends AbstractEntityController<AdqlQuery, AdqlQueryBean>
    {
    @Override
    public Path path()
        {
        return path(
            AdqlSchemaLinkFactory.SCHEMA_QUERY_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public AdqlSchemaQueryController()
        {
        super();
        }

    /**
     * MVC property for the create name.
     *
     */
    public static final String CREATE_NAME = "adql.schema.query.create.name" ;

    /**
     * MVC property for the create rowid.
     *
     */
    public static final String CREATE_ROWID = "adql.schema.query.create.rowid" ;

    /**
     * MVC property for the create query.
     *
     */
    public static final String CREATE_QUERY = "adql.schema.query.create.query" ;

    /**
     * MVC property for the create store.
     *
     */
    public static final String CREATE_STORE = "adql.schema.query.create.store" ;

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
     * Get the parent schema based on the request ident.
     * @throws NotFoundException
     *
     */
    @ModelAttribute(AdqlSchemaController.TARGET_ENTITY)
    public AdqlSchema parent(
        @PathVariable(WebappLinkFactory.IDENT_FIELD)
        final String ident
        ) throws IdentifierNotFoundException {
        log.debug("parent() [{}]", ident);
        return factories().adql().schemas().select(
            factories().adql().schemas().idents().ident(
                ident
                )
            );
        }

    /**
     * JSON GET request to select all.
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.GET, produces=JSON_CONTENT)
    public Iterable<AdqlQueryBean> select(
        @ModelAttribute(AdqlSchemaController.TARGET_ENTITY)
        final AdqlSchema schema
        ){
        return bean(
            schema.queries().select()
            );
        }

    /**
     * JSON POST request to create a new query.
     * @throws QueryProcessingException  
     *
     */
    @ResponseBody
    @RequestMapping(value=CREATE_PATH, method=RequestMethod.POST, produces=JSON_CONTENT)
    public ResponseEntity<AdqlQueryBean> create(
        @ModelAttribute(AdqlSchemaController.TARGET_ENTITY)
        final AdqlSchema schema,
        @RequestParam(value=CREATE_QUERY, required=true)
        final String query,
        @RequestParam(value=CREATE_NAME, required=false)
        final String name
        ) throws QueryProcessingException {
        return created(
            schema.queries().create(
                query,
                name
                )
            );
        }
    }
