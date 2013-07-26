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

import uk.ac.roe.wfau.firethorn.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.base.BaseComponent.CopyDepth;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityController;
import uk.ac.roe.wfau.firethorn.webapp.control.EntityBean;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;

/**
 * Spring MVC controller for <code>AdqlSchema</code> tables.
 *
 */
@Slf4j
@Controller
@RequestMapping(AdqlSchemaLinkFactory.SCHEMA_TABLE_PATH)
public class AdqlSchemaTableController
extends AbstractEntityController<AdqlTable, AdqlTableBean>
    {
    @Override
    public Path path()
        {
        return path(
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
     * MVC property for the Resource name.
     *
     */
    public static final String SELECT_NAME = "adql.schema.table.select.name" ;

    /**
     * MVC property for the search text.
     *
     */
    public static final String SEARCH_TEXT = "adql.schema.table.search.text" ;

    /**
     * MVC property for the import table base.
     *
     */
    public static final String IMPORT_BASE = "adql.schema.table.import.base" ;

    /**
     * MVC property for the import table name.
     *
     */
    public static final String IMPORT_NAME = "adql.schema.table.import.name" ;

    @Override
    public AdqlTableBean bean(final AdqlTable entity)
        {
        return new AdqlTableBean(
            entity
            );
        }

    @Override
    public Iterable<AdqlTableBean> bean(final Iterable<AdqlTable> iter)
        {
        return new AdqlTableBean.Iter(
            iter
            );
        }

    /**
     * Get the parent entity based on the request ident.
     * @throws NotFoundException
     *
     */
    @ModelAttribute(AdqlSchemaController.TARGET_ENTITY)
    public AdqlSchema parent(
        @PathVariable(WebappLinkFactory.IDENT_FIELD)
        final String ident
        ) throws NotFoundException {
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
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.GET, produces=JSON_MAPPING)
    public Iterable<AdqlTableBean> select(
        @ModelAttribute(AdqlSchemaController.TARGET_ENTITY)
        final AdqlSchema schema
        ){
        log.debug("select()");
        return bean(
            schema.tables().select()
            );
        }

    /**
     * JSON request to select by name.
     * @throws NotFoundException
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, params=SELECT_NAME, produces=JSON_MAPPING)
    public AdqlTableBean select(
        @ModelAttribute(AdqlSchemaController.TARGET_ENTITY)
        final AdqlSchema schema,
        @RequestParam(SELECT_NAME)
        final String name
        ) throws NotFoundException {
        log.debug("select(String) [{}]", name);
        return bean(
            schema.tables().select(
                name
                )
            );
        }

    /**
     * JSON request to search by name.
     *
     */
    @ResponseBody
    @RequestMapping(value=SEARCH_PATH, params=SEARCH_TEXT, produces=JSON_MAPPING)
    public Iterable<AdqlTableBean > search(
        @ModelAttribute(AdqlSchemaController.TARGET_ENTITY)
        final AdqlSchema schema,
        @RequestParam(SEARCH_TEXT)
        final String text
        ){
        log.debug("search(String) [{}]", text);
        return bean(
            schema.tables().search(
                text
                )
            );
        }

    /**
     * JSON request to import a table.
     * @throws NotFoundException
     *
     */
    @ResponseBody
    @RequestMapping(value=IMPORT_PATH, params={IMPORT_BASE}, method=RequestMethod.POST, produces=JSON_MAPPING)
    public ResponseEntity<AdqlTableBean > inport(
        @ModelAttribute(AdqlSchemaController.TARGET_ENTITY)
        final AdqlSchema schema,
        @RequestParam(value=ADQL_COPY_DEPTH_URN, required=false)
        final CopyDepth type,
        @RequestParam(value=IMPORT_BASE, required=true)
        final String base
        ) throws NotFoundException {
        log.debug("inport(CopyDepth, String) [{}][{}]", type, base);
        return created(
            schema.tables().create(
                ((type != null) ? type : CopyDepth.FULL),
                factories().base().tables().select(
                    factories().base().tables().links().ident(
                        base
                        )
                    )
                )
            );
        }

    /**
     * JSON request to import a table.
     * @throws NotFoundException
     *
     */
    @ResponseBody
    @RequestMapping(value=IMPORT_PATH, params={IMPORT_BASE, IMPORT_NAME}, method=RequestMethod.POST, produces=JSON_MAPPING)
    public ResponseEntity<AdqlTableBean> inport(
        @ModelAttribute(AdqlSchemaController.TARGET_ENTITY)
        final AdqlSchema schema,
        @RequestParam(value=ADQL_COPY_DEPTH_URN, required=false)
        final CopyDepth type,
        @RequestParam(value=IMPORT_BASE, required=true)
        final String base,
        @RequestParam(value=IMPORT_NAME, required=true)
        final String name
        ) throws NotFoundException {
        log.debug("inport(CopyDepth, String, String) [{}][{}][{}]", type, base, name);
        return created(
            schema.tables().create(
                type,
                factories().base().tables().select(
                    factories().base().tables().links().ident(
                        base
                        )
                    ),
                name
                )
            );
        }
    }
