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
 * Spring MVC controller for JdbcResources.
 *
 */
@Slf4j
@Controller
@RequestMapping(JdbcSchemaIdentFactory.SCHEMA_PATH)
public class JdbcSchemaController
    extends AbstractController
    {

    @Override
    public Path path()
        {
        return new PathImpl(
            JdbcSchemaIdentFactory.SCHEMA_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public JdbcSchemaController()
        {
        super();
        }

    /**
     * MVC property for the target JdbcSchema entity.
     *
     */
    public static final String SCHEMA_ENTITY = "urn:jdbc.schema.entity" ;

    /**
     * MVC property for the target bean.
     *
     */
    public static final String SCHEMA_BEAN = "urn:jdbc.schema.bean" ;

    /**
     * Get the target JdbcSchema based on the ident in the path.
     *
    @ModelAttribute(JdbcSchemaController.SCHEMA_ENTITY)
     */
    public JdbcSchema schema(
        @PathVariable("ident")
        final String ident
        ){
        log.debug("JdbcSchema schema() [{}]", ident);
        try {
            return womble().resources().jdbc().schemas().select(
                womble().resources().jdbc().schemas().ident(
                    ident
                    )
                );
            }
        catch (IdentifierNotFoundException ouch)
            {
            log.debug("JdbcSchema not found [{}]", ouch);
            return null ;
            }
        }

    /**
     * Wrap the JdbcSchema as a bean.
     * 
     */
    @ModelAttribute(JdbcSchemaController.SCHEMA_BEAN)
    public JdbcSchemaBean bean(
        @PathVariable("ident")
        final String ident
        ){
        return new JdbcSchemaBean(
            schema(
                ident
                )
            );
        }
    
    /**
     * HTML GET request for a schema.
     *
     */
    @RequestMapping(method=RequestMethod.GET)
    public ModelAndView htmlSelect(
        @ModelAttribute(JdbcSchemaController.SCHEMA_BEAN)
        final JdbcSchemaBean bean,
        final ModelAndView model
        ){
        log.debug("htmlSelect()");
        model.setViewName(
            "jdbc/catalog/display"
            );
        model.addObject(
            JdbcCatalogController.CATALOG_BEAN,
            new JdbcCatalogBean(
                bean.entity().parent()
                )
            );

        return model ;
        }

    /**
     * JSON GET request for a schema.
     *
     */
    @ResponseBody
    @RequestMapping(method=RequestMethod.GET, produces=JSON_MAPPING)
    public JdbcSchemaBean jsonSelect(
        @ModelAttribute(JdbcSchemaController.SCHEMA_BEAN)
        final JdbcSchemaBean bean
        ){
        log.debug("jsonSelect()");
        return bean ;
        }
    }
