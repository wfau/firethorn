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
package uk.ac.roe.wfau.firethorn.widgeon.jdbc;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import uk.ac.roe.wfau.firethorn.common.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayJdbcSchema;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractController;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.webapp.paths.PathImpl;

/**
 * Spring MVC controller for <code>JdbcSchema</code> tables.
 *
 */
@Slf4j
@Controller
@RequestMapping(JdbcSchemaLinkFactory.SCHEMA_TABLE_PATH)
public class JdbcSchemaTableController
extends AbstractController
    {
    @Override
    public Path path()
        {
        return new PathImpl(
            JdbcSchemaLinkFactory.SCHEMA_TABLE_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public JdbcSchemaTableController()
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
     * MVC property for the Resource name.
     *
     */
    public static final String SELECT_NAME = "jdbc.schema.table.select.name" ;

    /**
     * MVC property for the select results.
     *
     */
    public static final String SELECT_RESULT = "jdbc.schema.table.select.result" ;

    /**
     * MVC property for the search text.
     *
     */
    public static final String SEARCH_TEXT = "jdbc.schema.table.search.text" ;

    /**
     * MVC property for the search results.
     *
     */
    public static final String SEARCH_RESULT = "jdbc.schema.table.search.result" ;

    /**
     * Get the parent entity based on the request ident.
     * @throws NotFoundException
     *
     */
    @ModelAttribute(JdbcSchemaController.SCHEMA_ENTITY)
    public TuesdayJdbcSchema schema(
        @PathVariable("ident")
        final String ident
        ) throws NotFoundException{
        log.debug("schema() [{}]", ident);
        return factories().jdbc().schemas().select(
            factories().jdbc().schemas().idents().ident(
                ident
                )
            );
        }

    /**
     * Select all.
     *
     */
    public JdbcTableBean.Iter select(
        @ModelAttribute(JdbcSchemaController.SCHEMA_ENTITY)
        final TuesdayJdbcSchema schema
        ){
        log.debug("select()");
        return new JdbcTableBean.Iter(
            schema.tables().select()
            );
        }

    /**
     * JSON GET request to select all.
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.GET, produces=JSON_MAPPING)
    public JdbcTableBean.Iter jsonSelect(
        @ModelAttribute(JdbcSchemaController.SCHEMA_ENTITY)
        final TuesdayJdbcSchema schema,
        final ModelAndView model
        ){
        log.debug("jsonSelect()");
        return select(
            schema
            );
        }

    /**
     * Select by name.
     *
     */
    public JdbcTableBean select(
        @ModelAttribute(JdbcSchemaController.SCHEMA_ENTITY)
        final TuesdayJdbcSchema schema,
        final String name
        ){
        log.debug("select(String) [{}]", name);
        return new JdbcTableBean(
            schema.tables().select(
                name
                )
            );
        }

    /**
     * JSON request to select by name.
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, params=SELECT_NAME, produces=JSON_MAPPING)
    public JdbcTableBean jsonSelect(
        @ModelAttribute(JdbcSchemaController.SCHEMA_ENTITY)
        final TuesdayJdbcSchema schema,
        @RequestParam(SELECT_NAME)
        final String name,
        final ModelAndView model
        ){
        log.debug("jsonSelect(String) [{}]", name);
        return select(
            schema,
            name
            );
        }

    /**
     * Search by text.
     *
     */
    public JdbcTableBean.Iter search(
        @ModelAttribute(JdbcSchemaController.SCHEMA_ENTITY)
        final TuesdayJdbcSchema schema,
        final String text
        ){
        log.debug("search(String) [{}]", text);
        return new JdbcTableBean.Iter(
            schema.tables().search(
                text
                )
            );
        }

    /**
     * JSON request to search by text.
     *
     */
    @ResponseBody
    @RequestMapping(value=SEARCH_PATH, params=SEARCH_TEXT, produces=JSON_MAPPING)
    public JdbcTableBean.Iter jsonSearch(
        @ModelAttribute(JdbcSchemaController.SCHEMA_ENTITY)
        final TuesdayJdbcSchema schema,
        @RequestParam(SEARCH_TEXT)
        final String text,
        final ModelAndView model
        ){
        log.debug("jsonSearch(String) [{}]", text);
        return search(
            schema,
            text
            );
        }
    }
