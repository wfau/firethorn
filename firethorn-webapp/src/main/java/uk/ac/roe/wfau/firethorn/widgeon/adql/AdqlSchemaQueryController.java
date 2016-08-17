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

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.adql.query.QueryProcessingException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityController;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.widgeon.name.AdqlSchemaLinkFactory;

/**
 * Spring MVC controller to handle the {@link AdqlQuery} for an {@link AdqlSchema}.
 * <br/>Controller path : [{@value AdqlSchemaLinkFactory.SCHEMA_QUERY_PATH}]
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
     * MVC property for the {@link AdqlQuery} name, [{@value}].
     *
     */
    public static final String CREATE_NAME = "adql.schema.query.create.name" ;

    /**
     * MVC property for the {@link AdqlQuery} input, [{@value}].
     *
     */
    public static final String CREATE_QUERY = "adql.schema.query.create.query" ;

    /**
     * MVC property for the {@link AdqlQuery} store, [{@value}].
     *
     */
    public static final String CREATE_STORE = "adql.schema.query.create.store" ;

    /**
     * MVC property for the {@link AdqlQuery.Mode} mode, [{@value}].
     *
     */
    public static final String CREATE_MODE = "adql.schema.query.create.mode" ;

    /**
     * MVC property for the {@link AdqlQuery.Syntax.Level} level, [{@value}].
     *
     */
    public static final String CREATE_LEVEL = "adql.schema.query.create.level" ;

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
     * Get the parent {@link AdqlSchema} based on the {@Identifier} in the request path.
     * @param ident The {@link AdqlSchema} {@Identifier} from the URL path, [{@value WebappLinkFactory.IDENT_FIELD}].
     * @return The parent {@link AdqlSchema}.
     * @throws IdentifierNotFoundException If the {@link AdqlSchema} could not be found.
     *
     */
    @ModelAttribute(AdqlSchemaController.TARGET_ENTITY)
    public AdqlSchema parent(
        @PathVariable(WebappLinkFactory.IDENT_FIELD)
        final String ident
        ) throws IdentifierNotFoundException {
        log.debug("parent() [{}]", ident);
        return factories().adql().schemas().entities().select(
            factories().adql().schemas().idents().ident(
                ident
                )
            );
        }

    /**
     * {@link RequestMethod#GET} request to select all the {@link AdqlQuery} linked to this {@link AdqlSchema}.
     * <br/>Request path : [{@value #SELECT_PATH}]
     * <br/>Content type : [{@value #JSON_MIME}]
     * @param schema The parent {@link AdqlSchema} selected using the {@Identifier} in the request path.
     * @return An {@Iterable} set of {@link AdqlQueryBean}.
     * 
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.GET, produces=JSON_MIME)
    public Iterable<AdqlQueryBean> select(
        @ModelAttribute(AdqlSchemaController.TARGET_ENTITY)
        final AdqlSchema schema
        ){
        return bean(
            schema.queries().select()
            );
        }

    /**
     * {@link RequestMethod#POST} request to create a new {@link AdqlQuery}.
     * <br/>Request path : [{@value #CREATE_PATH}]
     * <br/>Content type : [{@value #JSON_MIME}]
     * @param schema The parent {@link AdqlSchema} selected using the {@Identifier} in the request path.
     * @param name The {@link AdqlQuery} name, [{@value #CREATE_NAME}].
     * @param name The {@link AdqlQuery.Mode} mode, [{@value #CREATE_MODE}].
     * @param name The {@link AdqlQuery.Syntax.Level} level, [{@value #CREATE_LEVEL}].
     * @return An {@link AdqlQueryBean} wrapping the new {@link AdqlQuery}.
     * @todo Rejects duplicate names.
     * 
     */
    @ResponseBody
    @RequestMapping(value=CREATE_PATH, method=RequestMethod.POST, produces=JSON_MIME)
    public ResponseEntity<AdqlQueryBean> create(
        @ModelAttribute(AdqlSchemaController.TARGET_ENTITY)
        final AdqlSchema schema,
        @RequestParam(value=CREATE_QUERY, required=true)
        final String query,
        @RequestParam(value=CREATE_NAME, required=false)
        final String name,
        @RequestParam(value=CREATE_MODE, required=false)
        final AdqlQuery.Mode mode,
        @RequestParam(value=CREATE_LEVEL, required=false)
        final AdqlQuery.Syntax.Level level
        ) throws QueryProcessingException {
        return created(
            schema.queries().create(
                factories().adql().queries().params().create(
                    level,
                    mode
                    ),
                query,
                name
                )
            );
        }
    }
