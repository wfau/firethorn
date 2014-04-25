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
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.base.BaseComponent.CopyDepth;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityController;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;

/**
 * Spring MVC controller to handle the {@link AdqlSchema} in an {@link AdqlResource}.
 * <br/>Controller path : [{@value AdqlResourceLinkFactory#RESOURCE_SCHEMA_PATH}]
 *
 */
@Slf4j
@Controller
@RequestMapping(AdqlResourceLinkFactory.RESOURCE_SCHEMA_PATH)
public class AdqlResourceSchemaController
extends AbstractEntityController<AdqlSchema, AdqlSchemaBean>
    {
    @Override
    public Path path()
        {
        return path(
            AdqlResourceLinkFactory.RESOURCE_SCHEMA_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public AdqlResourceSchemaController()
        {
        super();
        }

    /**
     * MVC property for the {@link AdqlSchema} name, [{@value}].
     * @todo Merge create, select and import.
     *
     */
    public static final String SELECT_NAME = "adql.resource.schema.select.name" ;

    /**
     * MVC property for the {@link AdqlSchema} name, [{@value}].
     * @todo Merge create, select and import.
     *
     */
    public static final String CREATE_NAME = "adql.resource.schema.create.name" ;

    /**
     * MVC property for the {@Identifier} of the {@link BaseSchema} to copy, [{@value}].
     * 
     */
    public static final String IMPORT_SCHEMA_BASE = "adql.resource.schema.import.base" ;

    /**
     * MVC property for the {@link AdqlSchema} name, [{@value}].
     * @todo Merge create, select and import.
     *
     */
    public static final String IMPORT_SCHEMA_NAME = "adql.resource.schema.import.name" ;

    @Override
    public AdqlSchemaBean bean(final AdqlSchema entity)
        {
        return new AdqlSchemaBean(
            entity
            );
        }

    @Override
    public Iterable<AdqlSchemaBean > bean(final Iterable<AdqlSchema> iter)
        {
        return new AdqlSchemaBean.Iter(
            iter
            );
        }

    /**
     * Get the parent {@link AdqlResource} based on the {@Identifier} in the request path.
     * @param ident The {@link AdqlResource} {@Identifier} from the URL path, [{@value WebappLinkFactory.IDENT_FIELD}].
     * @return The parent {@link AdqlResource}.
     * @throws IdentifierNotFoundException If the {@link AdqlResource} could not be found.
     *
     */
    @ModelAttribute(AdqlResourceController.TARGET_ENTITY)
    public AdqlResource entity(
        @PathVariable(WebappLinkFactory.IDENT_FIELD)
        final String ident
        ) throws IdentifierNotFoundException {
        log.debug("entity() [{}]", ident);
        return factories().adql().resources().select(
            factories().adql().resources().idents().ident(
                ident
                )
            );
        }

    /**
     * {@link RequestMethod#GET} request to select all the {@link AdqlSchema} in this {@link AdqlResource}.
     * <br/>Request path : [{@value #SELECT_PATH}]
     * <br/>Content type : [{@value #JSON_MIME}]
     * @param resource The parent {@link AdqlResource} selected using the {@Identifier} in the request path.
     * @return An {@Iterable} set of {@link AdqlSchemaBean}.
     * 
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.GET, produces=JSON_MIME)
    public Iterable<AdqlSchemaBean> select(
        @ModelAttribute(AdqlResourceController.TARGET_ENTITY)
        final AdqlResource resource
        ){
        log.debug("select()");
        return bean(
            resource.schemas().select()
            );
        }

    /**
     * {@link RequestMethod#GET} request to select a specific {@link AdqlSchema} by name.
     * <br/>Request path : [{@value #SELECT_PATH}]
     * <br/>Content type : [{@value #JSON_MIME}]
     * @param resource The parent {@link AdqlResource} selected using the {@Identifier} in the request path.
     * @param name The {@link AdqlSchema} name to look for, [{@value #SELECT_NAME}].
     * @return The matching {@link AdqlSchema} wrapped in an {@link AdqlSchemaBean}.
     * @throws NameNotFoundException If a matching {@link AdqlSchema} could not be found.
     * 
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, params=SELECT_NAME, produces=JSON_MIME)
    public AdqlSchemaBean select(
        @ModelAttribute(AdqlResourceController.TARGET_ENTITY)
        final AdqlResource resource,
        @RequestParam(SELECT_NAME)
        final String name
        ) throws NameNotFoundException {
        log.debug("select(String) [{}]", name);
        return bean(
            resource.schemas().select(
                name
                )
            );
        }

    /**
     * {@link RequestMethod#POST} request to create a new {@link AdqlSchema}.
     * <br/>Request path : [{@value #CREATE_PATH}]
     * <br/>Content type : [{@value #JSON_MIME}]
     * @param resource The parent {@link AdqlResource} selected using the {@Identifier} in the request path.
     * @param name The {@link AdqlSchema} name, [{@value #CREATE_NAME}].
     * @return The new {@link AdqlSchema} wrapped in an {@link AdqlSchemaBean}.
     * @todo Rejects duplicate names.
     * 
     */
    @ResponseBody
    @RequestMapping(value=CREATE_PATH, params={CREATE_NAME}, method=RequestMethod.POST, produces=JSON_MIME)
    public ResponseEntity<AdqlSchemaBean> create(
        @ModelAttribute(AdqlResourceController.TARGET_ENTITY)
        final AdqlResource resource,
        @RequestParam(value=CREATE_NAME, required=true)
        final String name
        ){
        log.debug("create(String) [{}][{}]", name);
        return created(
            resource.schemas().create(
                name
                )
            );
        }

    /**
     * {@link RequestMethod#POST} request to create a new {@link AdqlSchema} copying the name and tables from a {@link BaseSchema}.
     * <br/>Request path : [{@value #IMPORT_PATH}]
     * <br/>Content type : [{@value #JSON_MIME}]
     * @param resource The parent {@link AdqlResource} selected using the {@Identifier} in the request path.
     * @param base     The {@Identifier} of the {@link BaseSchema} to copy, [{@value #IMPORT_SCHEMA_BASE}].
     * @param depth    The {@link CopyDepth} of the new {@link AdqlSchema}, [{@value #ADQL_COPY_DEPTH_URN}].
     * @return The new {@link AdqlSchema} wrapped in an {@link AdqlSchemaBean}.
     * @throws IdentifierNotFoundException If the {@link BaseSchema} could not be found.
     * @todo Rejects duplicate names.
     * @todo Merge with next method.
     * 
     */
    @ResponseBody
    @RequestMapping(value=IMPORT_PATH, params={IMPORT_SCHEMA_BASE}, method=RequestMethod.POST, produces=JSON_MIME)
    public ResponseEntity<AdqlSchemaBean> inport(
        @ModelAttribute(AdqlResourceController.TARGET_ENTITY)
        final AdqlResource resource,
        @RequestParam(value=ADQL_COPY_DEPTH_URN, required=false)
        final CopyDepth depth,
        @RequestParam(value=IMPORT_SCHEMA_BASE, required=true)
        final String base
        ) throws IdentifierNotFoundException {
        log.debug("inport(CopyDepth, String) [{}][{}]", depth, base);
        return created(
            resource.schemas().create(
                ((depth != null) ? depth : CopyDepth.FULL),
                factories().base().schema().select(
                    factories().base().schema().links().ident(
                        base
                        )
                    )
                )
            );
        }

    /**
     * {@link RequestMethod#POST} request to create a new {@link AdqlSchema} copying the tables from a {@link BaseSchema}.
     * <br/>Request path : [{@value #IMPORT_PATH}]
     * <br/>Content type : [{@value #JSON_MIME}]
     * @param resource The parent {@link AdqlResource} selected using the {@Identifier} in the request path.
     * @param base     The The {@Identifier} of the {@link BaseSchema} to copy, [{@value #IMPORT_SCHEMA_BASE}].
     * @param depth    The {@link CopyDepth} of the new {@link AdqlSchema}, [{@value #ADQL_COPY_DEPTH_URN}].
     * @param name     The name of the new {@link AdqlSchema}, [{@value #IMPORT_SCHEMA_NAME}].
     * @return The new {@link AdqlSchema} wrapped in an {@link AdqlSchemaBean}.
     * @throws IdentifierNotFoundException If the {@link BaseSchema} could not be found.
     * @todo Rejects duplicate names.
     * @todo Make name optional, default to the base name.
     * 
     */
    @ResponseBody
    @RequestMapping(value=IMPORT_PATH, params={IMPORT_SCHEMA_BASE, IMPORT_SCHEMA_NAME}, method=RequestMethod.POST, produces=JSON_MIME)
    public ResponseEntity<AdqlSchemaBean> inport(
        @ModelAttribute(AdqlResourceController.TARGET_ENTITY)
        final AdqlResource resource,
        @RequestParam(value=ADQL_COPY_DEPTH_URN, required=false)
        final CopyDepth depth,
        @RequestParam(value=IMPORT_SCHEMA_BASE, required=true)
        final String base,
        @RequestParam(value=IMPORT_SCHEMA_NAME, required=true)
        final String name
        ) throws IdentifierNotFoundException {
        log.debug("inport(CopyDepth, String, String) [{}][{}][{}]", depth, base, name);
        return created(
            resource.schemas().create(
                ((depth != null) ? depth : CopyDepth.FULL),
                name,
                factories().base().schema().select(
                    factories().base().schema().links().ident(
                        base
                        )
                    )
                )
            );
        }
    }
