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

import uk.ac.roe.wfau.firethorn.common.entity.annotation.UpdateAtomicMethod;
import uk.ac.roe.wfau.firethorn.common.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.common.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractController;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.webapp.paths.PathImpl;

/**
 * Spring MVC controller for <code>JdbcCatalog</code>.
 *
 */
@Slf4j
@Controller
@RequestMapping(JdbcCatalogIdentFactory.CATALOG_PATH)
public class JdbcCatalogController
    extends AbstractController
    {
    @Override
    public Path path()
        {
        return new PathImpl(
            JdbcCatalogIdentFactory.CATALOG_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public JdbcCatalogController()
        {
        super();
        }

    /**
     * MVC property for the target entity.
     *
     */
    public static final String CATALOG_ENTITY = "urn:jdbc.catalog.entity" ;

    /**
     * MVC property for updating the name.
     *
     */
    public static final String UPDATE_NAME = "jdbc.catalog.update.name" ;

    /**
     * Wrap an entity as a bean.
     *
     */
    public JdbcCatalogBean bean(
        final JdbcCatalog entity
        ){
        log.debug("bean() [{}]", entity);
        return new JdbcCatalogBean(
            entity
            );
        }

    /**
     * Get the target entity based on the ident in the path.
     *
     */
    @ModelAttribute(JdbcCatalogController.CATALOG_ENTITY)
    public JdbcCatalog entity(
        @PathVariable("ident")
        final String ident
        ) throws NotFoundException {
        log.debug("catalog() [{}]", ident);
        return womble().resources().jdbc().catalogs().select(
            womble().resources().jdbc().catalogs().ident(
                ident
                )
            );
        }

    /**
     * HTML GET request.
     *
     */
    @RequestMapping(method=RequestMethod.GET)
    public ModelAndView htmlSelect(
        final ModelAndView model
        ){
        log.debug("htmlSelect()");
        model.setViewName(
            "jdbc/catalog/display"
            );
        return model ;
        }

    /**
     * JSON GET request.
     *
     */
    @ResponseBody
    @RequestMapping(method=RequestMethod.GET, produces=JSON_MAPPING)
    public JdbcCatalogBean jsonSelect(
        @ModelAttribute(CATALOG_ENTITY)
        final JdbcCatalog entity
        ){
        log.debug("jsonSelect()");
        return bean(
            entity
            ) ;
        }

    /**
     * JSON POST update.
     *
     */
    @ResponseBody
    @UpdateAtomicMethod
    @RequestMapping(method=RequestMethod.POST, produces=JSON_MAPPING)
    public JdbcCatalogBean jsonUpdate(
        @RequestParam(value=UPDATE_NAME, required=false)
        String name,
        @ModelAttribute(CATALOG_ENTITY)
        final JdbcCatalog entity
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
