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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import uk.ac.roe.wfau.firethorn.common.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractController;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.webapp.paths.PathImpl;

/**
 * Spring MVC controller for <code>JdbcTables</code>.
 *
 */
@Slf4j
@Controller
@RequestMapping(JdbcTableIdentFactory.TABLE_PATH)
public class JdbcTableController
    extends AbstractController
    {

    @Override
    public Path path()
        {
        return new PathImpl(
            JdbcTableIdentFactory.TABLE_PATH
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
     * MVC property for the target bean.
     *
     */
    public static final String TABLE_BEAN = "urn:jdbc.table.bean" ;

    /**
     * Get the target entity based on the ident in the path.
     *
     */
    @ModelAttribute(JdbcTableController.TABLE_ENTITY)
    public JdbcTable table(
        @PathVariable("ident")
        final String ident
        ){
        log.debug("table() [{}]", ident);
        try {
            return womble().resources().jdbc().tables().select(
                womble().resources().jdbc().tables().ident(
                    ident
                    )
                );
            }
        catch (final IdentifierNotFoundException ouch)
            {
            log.debug("JdbcTable not found [{}]", ouch);
            return null ;
            }
        }

    /**
     * Wrap the entity as a bean.
     *
     */
    @ModelAttribute(JdbcTableController.TABLE_BEAN)
    public JdbcTableBean bean(
        @ModelAttribute(JdbcTableController.TABLE_ENTITY)
        final JdbcTable entity
        ){
        return new JdbcTableBean(
            entity
            );
        }

    /**
     * HTML GET request.
     *
     */
    @RequestMapping(method=RequestMethod.GET)
    public ModelAndView htmlSelect(
        @ModelAttribute(JdbcTableController.TABLE_BEAN)
        final JdbcTableBean bean,
        final ModelAndView model
        ){
        log.debug("htmlSelect()");
        model.setViewName(
            "jdbc/catalog/display"
            );
        model.addObject(
            JdbcSchemaController.SCHEMA_BEAN,
            new JdbcSchemaBean(
                bean.entity().parent()
                )
            );

        return model ;
        }

    /**
     * JSON GET request.
     *
     */
    @ResponseBody
    @RequestMapping(method=RequestMethod.GET, produces=JSON_MAPPING)
    public JdbcTableBean jsonSelect(
        @ModelAttribute(JdbcTableController.TABLE_BEAN)
        final JdbcTableBean bean
        ){
        log.debug("jsonSelect()");
        return bean ;
        }
    }
