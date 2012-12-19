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

import uk.ac.roe.wfau.firethorn.common.entity.annotation.UpdateAtomicMethod;
import uk.ac.roe.wfau.firethorn.common.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayJdbcTable;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractController;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.webapp.paths.PathImpl;

/**
 * Spring MVC controller for <code>JdbcTables</code>.
 *
 */
@Slf4j
@Controller
@RequestMapping(JdbcTableLinkFactory.TABLE_PATH)
public class JdbcTableController
    extends AbstractController
    {

    @Override
    public Path path()
        {
        return new PathImpl(
            JdbcTableLinkFactory.TABLE_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public JdbcTableController()
        {
        super();
        }

    /**
     * MVC property for the target entity.
     *
     */
    public static final String TABLE_ENTITY = "urn:jdbc.table.entity" ;

    /**
     * MVC property for updating the name.
     *
     */
    public static final String UPDATE_NAME = "jdbc.table.update.name" ;

    /**
     * Wrap an entity as a bean.
     *
     */
    public JdbcTableBean bean(
        final TuesdayJdbcTable entity
        ){
        return new JdbcTableBean(
            entity
            );
        }

    /**
     * Get the target entity based on the ident in the path.
     * @throws NotFoundException
     *
     */
    @ModelAttribute(JdbcTableController.TABLE_ENTITY)
    public TuesdayJdbcTable entity(
        @PathVariable("ident")
        final String ident
        ) throws NotFoundException {
        log.debug("table() [{}]", ident);
        return factories().jdbc().tables().select(
            factories().jdbc().tables().idents().ident(
                ident
                )
            );
        }

    /**
     * JSON GET request.
     *
     */
    @ResponseBody
    @RequestMapping(method=RequestMethod.GET, produces=JSON_MAPPING)
    public JdbcTableBean jsonSelect(
        @ModelAttribute(TABLE_ENTITY)
        final TuesdayJdbcTable entity
        ){
        log.debug("jsonSelect()");
        return bean(
            entity
            );
        }

    /**
     * JSON POST update.
     *
     */
    @ResponseBody
    @UpdateAtomicMethod
    @RequestMapping(method=RequestMethod.POST, produces=JSON_MAPPING)
    public JdbcTableBean jsonUpdate(
        @RequestParam(value=UPDATE_NAME, required=false)
        final String name,
        @ModelAttribute(TABLE_ENTITY)
        final TuesdayJdbcTable entity
        ){

        if (name != null)
            {
            if (name.length() > 0)
                {
                entity.name(
                    name
                    );
                }
            }

        return bean(
            entity
            );
        }
    }
