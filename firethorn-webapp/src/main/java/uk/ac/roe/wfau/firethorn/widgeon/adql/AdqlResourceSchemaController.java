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

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import uk.ac.roe.wfau.firethorn.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.base.BaseComponent.CopyDepth;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractController;
import uk.ac.roe.wfau.firethorn.webapp.control.RedirectHeader;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;

/**
 * Spring MVC controller for <code>AdqlResource</code> schema.
 *
 */
@Slf4j
@Controller
@RequestMapping(AdqlResourceLinkFactory.RESOURCE_SCHEMA_PATH)
public class AdqlResourceSchemaController
extends AbstractController
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
     * URL path for the import method.
     *
     */
    public static final String IMPORT_PATH = "import" ;

    /**
     * MVC property for the select name.
     *
     */
    public static final String SELECT_NAME = "adql.resource.schema.select.name" ;

    /**
     * MVC property for the search text.
     *
     */
    public static final String SEARCH_TEXT = "adql.resource.schema.search.text" ;

    /**
     * MVC property for the create name.
     *
     */
    public static final String CREATE_NAME = "adql.resource.schema.create.name" ;

    /**
     * MVC property for the copy depth (REAL or THIN).
     *
     */
    public static final String COPY_DEPTH = "adql.schema.depth" ;
    
    /**
     * MVC property for the import table.
     *
     */
    public static final String IMPORT_TABLE_BASE = "adql.resource.table.import.base" ;

    /**
     * MVC property for the import table.
     *
     */
    public static final String IMPORT_TABLE_NAME = "adql.resource.table.import.name" ;

    /**
     * MVC property for the import schema.
     *
     */
    public static final String IMPORT_SCHEMA_BASE = "adql.resource.schema.import.base" ;

    /**
     * MVC property for the import named schema.
     *
     */
    public static final String IMPORT_SCHEMA_NAME = "adql.resource.schema.import.name" ;


    /**
     * Get the parent entity based on the request ident.
     * @throws NotFoundException
     *
     */
    @ModelAttribute(AdqlResourceController.TARGET_ENTITY)
    public AdqlResource parent(
        @PathVariable(WebappLinkFactory.IDENT_FIELD)
        final String ident
        ) throws NotFoundException {
        log.debug("parent() [{}]", ident);
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
    public AdqlSchemaBean.Iter select(
        @ModelAttribute(AdqlResourceController.TARGET_ENTITY)
        final AdqlResource resource
        ){
        log.debug("select()");
        return new AdqlSchemaBean.Iter(
            resource.schemas().select()
            );
        }

    /**
     * JSON request to select by name.
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, params=SELECT_NAME, produces=JSON_MAPPING)
    public AdqlSchemaBean select(
        @ModelAttribute(AdqlResourceController.TARGET_ENTITY)
        final AdqlResource resource,
        @RequestParam(SELECT_NAME)
        final String name
        ) throws NotFoundException {
        log.debug("select(String) [{}]", name);
        return new AdqlSchemaBean(
            resource.schemas().select(
                name
                )
            );
        }

    /**
     * JSON request to search by text.
     *
     */
    @ResponseBody
    @RequestMapping(value=SEARCH_PATH, params=SEARCH_TEXT, produces=JSON_MAPPING)
    public AdqlSchemaBean.Iter search(
        @ModelAttribute(AdqlResourceController.TARGET_ENTITY)
        final AdqlResource resource,
        @RequestParam(SEARCH_TEXT)
        final String text
        ){
        log.debug("search(String) [{}]", text);
        return new AdqlSchemaBean.Iter(
            resource.schemas().search(
                text
                )
            );
        }

    /**
     * A 'created' response entity.
     *
     */
    public ResponseEntity<AdqlSchemaBean> response(final AdqlSchemaBean bean)
        {
        return new ResponseEntity<AdqlSchemaBean>(
            bean,
            new RedirectHeader(
                bean
                ),
            HttpStatus.CREATED
            );
        }

    /**
     * JSON POST request to create a new schema.
     *
     */
    @RequestMapping(value=CREATE_PATH, params={CREATE_NAME}, method=RequestMethod.POST, produces=JSON_MAPPING)
    public ResponseEntity<AdqlSchemaBean> create(
        @ModelAttribute(AdqlResourceController.TARGET_ENTITY)
        final AdqlResource resource,
        @RequestParam(value=COPY_DEPTH, required=false)
        final CopyDepth depth,
        @RequestParam(value=CREATE_NAME, required=true)
        final String name
        ){
        log.debug("create(EntityType, String) [{}][{}]", depth, name);
        return response(
            new AdqlSchemaBean(
                resource.schemas().create(
                    ((depth != null) ? depth : CopyDepth.FULL),
                    name
                    )
                )
            );
        }

    /**
     * JSON POST request to import all the tables from another schema.
     *
     */
    @RequestMapping(value=IMPORT_PATH, params={IMPORT_SCHEMA_BASE}, method=RequestMethod.POST, produces=JSON_MAPPING)
    public ResponseEntity<AdqlSchemaBean> inport(
        @ModelAttribute(AdqlResourceController.TARGET_ENTITY)
        final AdqlResource resource,
        @RequestParam(value=COPY_DEPTH, required=false)
        final CopyDepth depth,
        @RequestParam(value=IMPORT_SCHEMA_BASE, required=true)
        final String base
        ) throws NotFoundException {
        log.debug("inport(CopyDepth, String) [{}][{}]", depth, base);
        return response(
            new AdqlSchemaBean(
                resource.schemas().create(
                    ((depth != null) ? depth : CopyDepth.FULL),
                    factories().base().schema().select(
                        factories().base().schema().links().ident(
                            base
                            )
                        )
                    )
                )
            );
        }

    /**
     * JSON POST request to import all the tables from another schema.
     *
     */
    @RequestMapping(value=IMPORT_PATH, params={IMPORT_SCHEMA_BASE, IMPORT_SCHEMA_NAME}, method=RequestMethod.POST, produces=JSON_MAPPING)
    public ResponseEntity<AdqlSchemaBean> inport(
        @ModelAttribute(AdqlResourceController.TARGET_ENTITY)
        final AdqlResource resource,
        @RequestParam(value=COPY_DEPTH, required=false)
        final CopyDepth depth,
        @RequestParam(value=IMPORT_SCHEMA_BASE, required=true)
        final String base,
        @RequestParam(value=IMPORT_SCHEMA_NAME, required=true)
        final String name
        ) throws NotFoundException {
        log.debug("inport(CopyDepth, String, String) [{}][{}][{}]", depth, base, name);
        return response(
            new AdqlSchemaBean(
                resource.schemas().create(
                    ((depth != null) ? depth : CopyDepth.FULL),
                    name,
                    factories().base().schema().select(
                        factories().base().schema().links().ident(
                            base
                            )
                        )
                    )
                )
            );
        }
    }
