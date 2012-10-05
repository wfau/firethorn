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
     * MVC property for the target JdbcCatalog entity.
     *
     */
    public static final String CATALOG_ENTITY = "urn:jdbc.catalog.entity" ;

    /**
     * MVC property for the target JdbcCatalogBean bean.
     *
     */
    public static final String CATALOG_BEAN = "urn:jdbc.catalog.bean" ;

    /**
     * Get the target JdbcCatalog based on the ident in the path.
     *
    @ModelAttribute(JdbcCatalogController.CATALOG_ENTITY)
     */
    public JdbcCatalog catalog(
        @PathVariable("ident")
        final String ident
        ){
        log.debug("JdbcCatalog catalog() [{}]", ident);
        try {
            return womble().resources().jdbc().catalogs().select(
                womble().resources().jdbc().catalogs().ident(
                    ident
                    )
                );
            }
        catch (IdentifierNotFoundException ouch)
            {
            log.debug("JdbcCatalog not found [{}]", ouch);
            return null ;
            }
        }

    /**
     * Wrap the JdbcCatalog as a JdbcCatalogBean.
     * ** this fails because Spring can't handle the nested interface ?
     * 
    @ModelAttribute(JdbcCatalogController.CATALOG_BEAN)
    public JdbcCatalogBean bean(
        @ModelAttribute(JdbcCatalogController.CATALOG_ENTITY)
        final JdbcCatalog catalog,
        final HttpServletRequest request
        ){
        log.debug("JdbcCatalogBean bean() [{}]", catalog.ident());
        return new JdbcCatalogBean(
            this.builder(
                request
                ),
            catalog
            );
        }
     */

    /**
     * Wrap the JdbcCatalog as a bean.
     * 
     */
    @ModelAttribute(JdbcCatalogController.CATALOG_BEAN)
    public JdbcCatalogBean bean(
        @PathVariable("ident")
        final String ident
        ){
        log.debug("JdbcCatalogBean bean()");
        return new JdbcCatalogBean(
            catalog(
                ident
                )
            );
        }

    /**
     * HTML GET request for a catalog.
     *
     */
    @RequestMapping(method=RequestMethod.GET)
    public ModelAndView htmlSelect(
        @ModelAttribute(JdbcCatalogController.CATALOG_BEAN)
        final JdbcCatalogBean bean,
        final ModelAndView model
        ){
        log.debug("htmlSelect()");
        model.setViewName(
            "jdbc/catalog/display"
            );
        model.addObject(
            JdbcResourceController.RESOURCE_BEAN,
            new JdbcResourceBean(
                bean.entity().parent()
                )
            );
        return model ;
        }

    /**
     * JSON GET request for a catalog.
     *
     */
    @ResponseBody
    @RequestMapping(method=RequestMethod.GET, produces=JSON_MAPPING)
    public JdbcCatalogBean jsonSelect(
        @ModelAttribute(JdbcCatalogController.CATALOG_BEAN)
        final JdbcCatalogBean bean
        ){
        log.debug("jsonSelect()");
        return bean ;
        }
    }
