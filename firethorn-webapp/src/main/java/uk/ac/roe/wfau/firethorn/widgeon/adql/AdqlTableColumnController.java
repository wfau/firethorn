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

import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityController;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;

/**
 * Spring MVC controller for <code>AdqlSchema</code> tables.
 *
 */
@Slf4j
@Controller
@RequestMapping(AdqlTableLinkFactory.TABLE_COLUMN_PATH)
public class AdqlTableColumnController
extends AbstractEntityController<AdqlColumn, AdqlColumnBean>
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
     * MVC property for the Resource name.
     *
     */
    public static final String SELECT_NAME = "adql.table.column.select.name" ;

    @Override
    public AdqlColumnBean bean(final AdqlColumn entity)
        {
        return new AdqlColumnBean(
            entity
            );
        }

    @Override
    public Iterable<AdqlColumnBean> bean(final Iterable<AdqlColumn> iter)
        {
        return new AdqlColumnBean.Iter(
            iter
            );
        }

    /**
     * Get the parent entity based on the request ident.
     * @throws IdentifierNotFoundException
     *
     */
    @ModelAttribute(AdqlTableController.TARGET_ENTITY)
    public AdqlTable parent(
        @PathVariable(WebappLinkFactory.IDENT_FIELD)
        final String ident
        ) throws IdentifierNotFoundException {
        log.debug("parent() [{}]", ident);
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
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.GET, produces=JSON_MIME)
    public Iterable<AdqlColumnBean> select(
        @ModelAttribute(AdqlTableController.TARGET_ENTITY)
        final AdqlTable table
        ){
        log.debug("select()");
        return bean(
            table.columns().select()
            );
        }

    /**
     * JSON request to select by name.
     * @throws NameNotFoundException
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, params=SELECT_NAME, produces=JSON_MIME)
    public AdqlColumnBean select(
        @ModelAttribute(AdqlTableController.TARGET_ENTITY)
        final AdqlTable table,
        @RequestParam(SELECT_NAME)
        final String name
        ) throws NameNotFoundException{
        log.debug("select(String) [{}]", name);
        return bean(
            table.columns().select(
                name
                )
            );
        }
    }
