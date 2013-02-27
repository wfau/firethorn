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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import uk.ac.roe.wfau.firethorn.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractController;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.webapp.paths.PathImpl;

/**
 * Spring MVC controller for <code>AdqlSchema</code> tables.
 *
 */
@Slf4j
@Controller
@RequestMapping(AdqlTableLinkFactory.TABLE_COLUMN_PATH)
public class AdqlTableColumnController
extends AbstractController
    {
    @Override
    public Path path()
        {
        return path(
            AdqlTableLinkFactory.TABLE_COLUMN_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public AdqlTableColumnController()
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
    public static final String SELECT_NAME = "adql.table.column.select.name" ;

    /**
     * MVC property for the select results.
     *
     */
    public static final String SELECT_RESULT = "adql.table.column.select.result" ;

    /**
     * MVC property for the search text.
     *
     */
    public static final String SEARCH_TEXT = "adql.table.column.search.text" ;

    /**
     * MVC property for the search results.
     *
     */
    public static final String SEARCH_RESULT = "adql.table.column.search.result" ;

    /**
     * Get the parent entity based on the request ident.
     * @throws NotFoundException
     *
     */
    @ModelAttribute(AdqlTableController.TABLE_ENTITY)
    public AdqlTable table(
        @PathVariable("ident")
        final String ident
        ) throws NotFoundException {
        log.debug("table() [{}]", ident);
        return factories().adql().tables().select(
            factories().adql().tables().idents().ident(
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
    public AdqlColumnBean.Iter jsonSelect(
        @ModelAttribute(AdqlTableController.TABLE_ENTITY)
        final AdqlTable table
        ){
        log.debug("jsonSelect()");
        try {
            return new AdqlColumnBean.Iter(
                table.columns().select()
                );
            }
        catch (final Throwable ouch)
            {
            log.debug("Error executing JsonSelect()");
            log.debug("  Class [{}]", ouch.getClass());
            log.debug("  Error [{}]", ouch.getMessage());
            return null ;
            }
        }

    /**
     * JSON request to select by name.
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, params=SELECT_NAME, produces=JSON_MAPPING)
    public AdqlColumnBean jsonSelect(
        @ModelAttribute(AdqlTableController.TABLE_ENTITY)
        final AdqlTable table,
        @RequestParam(SELECT_NAME)
        final String name
        ) throws NotFoundException {
        log.debug("jsonSelect(String) [{}]", name);
        return new AdqlColumnBean(
            table.columns().select(
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
    public AdqlColumnBean.Iter jsonSearch(
        @ModelAttribute(AdqlTableController.TABLE_ENTITY)
        final AdqlTable table,
        @RequestParam(SEARCH_TEXT)
        final String text
        ){
        log.debug("jsonSearch(String) [{}]", text);
        return new AdqlColumnBean.Iter(
            table.columns().search(
                text
                )
            );
        }
    }
