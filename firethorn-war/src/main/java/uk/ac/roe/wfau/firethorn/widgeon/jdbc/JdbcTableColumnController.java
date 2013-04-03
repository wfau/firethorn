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

import uk.ac.roe.wfau.firethorn.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTable;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractController;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;

/**
 * Spring MVC controller for <code>JdbcTable</code> columns.
 *
 */
@Slf4j
@Controller
@RequestMapping(JdbcTableLinkFactory.TABLE_COLUMN_PATH)
public class JdbcTableColumnController
extends AbstractController
    {
    @Override
    public Path path()
        {
        return path(
            JdbcTableLinkFactory.TABLE_COLUMN_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public JdbcTableColumnController()
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
    public static final String SELECT_NAME = "jdbc.table.column.select.name" ;

    /**
     * MVC property for the select results.
     *
     */
    public static final String SELECT_RESULT = "jdbc.table.column.select.result" ;

    /**
     * MVC property for the search text.
     *
     */
    public static final String SEARCH_TEXT = "jdbc.table.column.search.text" ;

    /**
     * MVC property for the search results.
     *
     */
    public static final String SEARCH_RESULT = "jdbc.table.column.search.result" ;

    /**
     * Get the parent table based on the identifier in the request.
     * @throws NotFoundException
     *
     */
    @ModelAttribute(JdbcTableController.TARGET_ENTITY)
    public JdbcTable table(
        @PathVariable("ident")
        final String ident
        ) throws NotFoundException {
        log.debug("schema() [{}]", ident);
        return factories().jdbc().tables().select(
            factories().jdbc().tables().idents().ident(
                ident
                )
            );
        }

    /**
     * Select all.
     *
     */
    public JdbcColumnBean.Iter select(
        @ModelAttribute(JdbcTableController.TARGET_ENTITY)
        final JdbcTable table
        ){
        log.debug("select()");
        return new JdbcColumnBean.Iter(
            table.columns().select()
            );
        }

    /**
     * JSON GET request to select all.
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.GET, produces=JSON_MAPPING)
    public JdbcColumnBean.Iter jsonSelect(
        @ModelAttribute(JdbcTableController.TARGET_ENTITY)
        final JdbcTable table,
        final ModelAndView model
        ){
        log.debug("jsonSelect()");
        return select(
            table
            );
        }

    /**
     * Select by name.
     *
     */
    public JdbcColumnBean select(
        @ModelAttribute(JdbcTableController.TARGET_ENTITY)
        final JdbcTable table,
        final String name
        ) throws NotFoundException {
        log.debug("select(String) [{}]", name);
        return new JdbcColumnBean(
            table.columns().select(
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
    public JdbcColumnBean jsonSelect(
        @ModelAttribute(JdbcTableController.TARGET_ENTITY)
        final JdbcTable table,
        @RequestParam(SELECT_NAME)
        final String name,
        final ModelAndView model
        ) throws NotFoundException {
        log.debug("jsonSelect(String) [{}]", name);
        return select(
            table,
            name
            );
        }

    /**
     * Search by text.
     *
     */
    public JdbcColumnBean.Iter search(
        @ModelAttribute(JdbcTableController.TARGET_ENTITY)
        final JdbcTable table,
        final String text
        ){
        log.debug("search(String) [{}]", text);
        return new JdbcColumnBean.Iter(
            table.columns().search(
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
    public JdbcColumnBean.Iter jsonSearch(
        @ModelAttribute(JdbcTableController.TARGET_ENTITY)
        final JdbcTable table,
        @RequestParam(SEARCH_TEXT)
        final String text,
        final ModelAndView model
        ){
        log.debug("jsonSearch(String) [{}]", text);
        return search(
            table,
            text
            );
        }
    }
