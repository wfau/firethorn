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

import uk.ac.roe.wfau.firethorn.common.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.tuesday.AdqlSchema;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractController;
import uk.ac.roe.wfau.firethorn.webapp.control.RedirectHeader;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.webapp.paths.PathImpl;

/**
 * Spring MVC controller for <code>AdqlSchema</code> tables.
 *
 */
@Slf4j
@Controller
@RequestMapping(AdqlSchemaLinkFactory.SCHEMA_TABLE_PATH)
public class AdqlSchemaTableController
extends AbstractController
    {
    @Override
    public Path path()
        {
        return new PathImpl(
            AdqlSchemaLinkFactory.SCHEMA_TABLE_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public AdqlSchemaTableController()
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
     * URL path for the import method.
     *
     */
    public static final String IMPORT_PATH = "import" ;

    /**
     * MVC property for the Resource name.
     *
     */
    public static final String SELECT_NAME = "adql.schema.table.select.name" ;

    /**
     * MVC property for the select results.
     *
     */
    public static final String SELECT_RESULT = "adql.schema.table.select.result" ;

    /**
     * MVC property for the search text.
     *
     */
    public static final String SEARCH_TEXT = "adql.schema.table.search.text" ;

    /**
     * MVC property for the search results.
     *
     */
    public static final String SEARCH_RESULT = "adql.schema.table.search.result" ;

    /**
     * MVC property for the import base.
     *
     */
    public static final String IMPORT_BASE = "adql.schema.table.import.base" ;

    /**
     * MVC property for the import name.
     *
     */
    public static final String IMPORT_NAME = "adql.schema.table.import.name" ;


    /**
     * Get the parent entity based on the request ident.
     * @throws NotFoundException
     *
     */
    @ModelAttribute(AdqlSchemaController.SCHEMA_ENTITY)
    public AdqlSchema schema(
        @PathVariable("ident")
        final String ident
        ) throws NotFoundException {
        log.debug("schema() [{}]", ident);
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
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.GET, produces=JSON_MAPPING)
    public AdqlTableBean.Iter jsonSelect(
        @ModelAttribute(AdqlSchemaController.SCHEMA_ENTITY)
        final AdqlSchema schema
        ){
        log.debug("jsonSelect()");
        return new AdqlTableBean.Iter(
            schema.tables().select()
            );
        }

    /**
     * JSON request to select by name.
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, params=SELECT_NAME, produces=JSON_MAPPING)
    public AdqlTableBean jsonSelect(
        @ModelAttribute(AdqlSchemaController.SCHEMA_ENTITY)
        final AdqlSchema schema,
        @RequestParam(SELECT_NAME)
        final String name
        ){
        log.debug("jsonSelect(String) [{}]", name);
        return new AdqlTableBean(
            schema.tables().select(
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
    public AdqlTableBean.Iter jsonSearch(
        @ModelAttribute(AdqlSchemaController.SCHEMA_ENTITY)
        final AdqlSchema schema,
        @RequestParam(SEARCH_TEXT)
        final String text
        ){
        log.debug("jsonSearch(String) [{}]", text);
        return new AdqlTableBean.Iter(
            schema.tables().search(
                text
                )
            );
        }

    /**
     * JSON request to create a new table.
     * @throws NotFoundException
     *
     */
    @ResponseBody
    @RequestMapping(value=IMPORT_PATH, method=RequestMethod.POST, produces=JSON_MAPPING)
    public ResponseEntity<AdqlTableBean> jsonInport(
        @ModelAttribute(AdqlSchemaController.SCHEMA_ENTITY)
        final AdqlSchema schema,
        @RequestParam(value=IMPORT_BASE, required=true)
        final String base,
        @RequestParam(value=IMPORT_NAME, required=false)
        final String name
        ) throws NotFoundException {
        log.debug("jsonCreate()");
        log.debug("  base [{}]", base);
        log.debug("  name [{}]", name);
        final AdqlTableBean bean = new AdqlTableBean(
            schema.tables().create(
                factories().base().tables().select(
                    factories().base().tables().links().parse(
                        base
                        )
                    ),
                name
                )
            );
        return new ResponseEntity<AdqlTableBean>(
            bean,
            new RedirectHeader(
                bean
                ),
            HttpStatus.CREATED
            );
        }
    }
