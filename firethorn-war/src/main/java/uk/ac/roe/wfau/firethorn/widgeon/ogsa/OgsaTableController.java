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
package uk.ac.roe.wfau.firethorn.widgeon.ogsa;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import uk.ac.roe.wfau.firethorn.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.meta.ogsa.OgsaTable;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractController;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;

/**
 * Spring MVC controller for <code>JdbcTables</code>.
 *
 */
@Slf4j
@Controller
@RequestMapping(OgsaTableLinkFactory.TABLE_PATH)
public class OgsaTableController
    extends AbstractController
    {

    @Override
    public Path path()
        {
        return path(
            OgsaTableLinkFactory.TABLE_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public OgsaTableController()
        {
        super();
        }

    /**
     * MVC property for the target entity.
     *
     */
    public static final String TABLE_ENTITY = "urn:jdbc.table.entity" ;

    /**
     * Wrap an entity as a bean.
     *
     */
    public OgsaTableBean bean(
        final OgsaTable<?,?> entity
        ){
        return new OgsaTableBean(
            entity
            );
        }

    /**
     * Get the target entity based on the alias in the path.
     * @throws NotFoundException
     *
     */
    @ModelAttribute(OgsaTableController.TABLE_ENTITY)
    public OgsaTable<?,?> entity(
        @PathVariable("alias")
        final String alias
        ) throws NotFoundException {
        log.debug("table() [{}]", alias);
        return factories().base().tables().resolve(
            alias
            ).ogsa();
        }

    /**
     * JSON GET request.
     *
     */
    @ResponseBody
    @RequestMapping(method=RequestMethod.GET, produces=JSON_MAPPING)
    public OgsaTableBean jsonSelect(
        @ModelAttribute(TABLE_ENTITY)
        final OgsaTable<?,?> entity
        ){
        log.debug("jsonSelect()");
        return bean(
            entity
            );
        }
    }
