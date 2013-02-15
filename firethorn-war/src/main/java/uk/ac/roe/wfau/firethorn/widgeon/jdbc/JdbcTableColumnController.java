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
     * Get the parent entity based on the request ident.
     * @throws NotFoundException
     *
     */
    @ModelAttribute(JdbcTableController.TABLE_ENTITY)
    public JdbcTable table(
        @PathVariable("ident")
        final String ident
        ) throws NotFoundException{
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
        @ModelAttribute(JdbcTableController.TABLE_ENTITY)
        final JdbcTable table
        ){
        log.debug("select()");
        return new JdbcColumnBean.Iter(
            table.columns().select()
            );
        }

    /**
     * Default HTML GET request (select all).
     *
     */
    @RequestMapping(value="", method=RequestMethod.GET)
    public ModelAndView htmlIndex(
        @ModelAttribute(JdbcTableController.TABLE_ENTITY)
        final JdbcTable table,
        final ModelAndView model
        ){
        log.debug("htmlIndex()");
        model.addObject(
            JdbcTableColumnController.SELECT_RESULT,
            select(
                table
                )
            );
        model.setViewName(
            "jdbc/schema/select"
            );
        return model ;
        }

    /**
     * HTML GET request to select all.
     *
     */
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.GET)
    public ModelAndView htmlSelect(
        @ModelAttribute(JdbcTableController.TABLE_ENTITY)
        final JdbcTable table,
        final ModelAndView model
        ){
        log.debug("htmlSelect()");
        model.addObject(
            JdbcTableColumnController.SELECT_RESULT,
            select(
                table
                )
            );
        model.setViewName(
            "jdbc/catalog/select"
            );
        return model ;
        }

    /**
     * JSON GET request to select all.
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.GET, produces=JSON_MAPPING)
    public JdbcColumnBean.Iter jsonSelect(
        @ModelAttribute(JdbcTableController.TABLE_ENTITY)
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
        @ModelAttribute(JdbcTableController.TABLE_ENTITY)
        final JdbcTable table,
        final String name
        ){
        log.debug("select(String) [{}]", name);
        return new JdbcColumnBean(
            table.columns().select(
                name
                )
            );
        }

    /**
     * HTML request to select by name.
     *
     */
    @RequestMapping(value=SELECT_PATH, params=SELECT_NAME)
    public ModelAndView htmlSelect(
        @ModelAttribute(JdbcTableController.TABLE_ENTITY)
        final JdbcTable table,
        @RequestParam(SELECT_NAME)
        final String name,
        final ModelAndView model
        ){
        log.debug("htmlSelect(String) [{}]", name);
        model.addObject(
            SELECT_NAME,
            name
            );
        model.addObject(
            SELECT_RESULT,
            select(
                table,
                name
                )
            );
        model.setViewName(
            "jdbc/catalog/select"
            );
        return model ;
        }

    /**
     * JSON request to select by name.
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, params=SELECT_NAME, produces=JSON_MAPPING)
    public JdbcColumnBean jsonSelect(
        @ModelAttribute(JdbcTableController.TABLE_ENTITY)
        final JdbcTable table,
        @RequestParam(SELECT_NAME)
        final String name,
        final ModelAndView model
        ){
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
        @ModelAttribute(JdbcTableController.TABLE_ENTITY)
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
     * HTML GET request for the search form.
     *
     */
    @RequestMapping(value=SEARCH_PATH, method=RequestMethod.GET)
    public ModelAndView htmlSearch(
        @ModelAttribute(JdbcTableController.TABLE_ENTITY)
        final JdbcTable table,
        final ModelAndView model
        ){
        log.debug("htmlSearch");
        model.setViewName(
            "jdbc/catalog/search"
            );
        return model ;
        }

    /**
     * HTML request to search by text.
     *
     */
    @RequestMapping(value=SEARCH_PATH, params=SEARCH_TEXT)
    public ModelAndView htmlSearch(
        @ModelAttribute(JdbcTableController.TABLE_ENTITY)
        final JdbcTable table,
        @RequestParam(SEARCH_TEXT)
        final String text,
        final ModelAndView model
        ){
        log.debug("htmlSearch(String) [{}]", text);
        model.addObject(
            SEARCH_TEXT,
            text
            );
        model.addObject(
            SEARCH_RESULT,
            search(
                table,
                text
                )
            );
        model.setViewName(
            "jdbc/catalog/search"
            );
        return model ;
        }

    /**
     * JSON request to search by text.
     *
     */
    @ResponseBody
    @RequestMapping(value=SEARCH_PATH, params=SEARCH_TEXT, produces=JSON_MAPPING)
    public JdbcColumnBean.Iter jsonSearch(
        @ModelAttribute(JdbcTableController.TABLE_ENTITY)
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
