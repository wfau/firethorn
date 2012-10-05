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

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import uk.ac.roe.wfau.firethorn.common.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractController;
import uk.ac.roe.wfau.firethorn.webapp.control.RedirectHeader;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.webapp.paths.PathImpl;

/**
 * Spring MVC controller for AdqlServices.
 *
 */
@Slf4j
@Controller
@RequestMapping(JdbcResourceIdentFactory.CATALOGS_PATH)
public class JdbcResourceCatalogsController
extends AbstractController
    {

    @Override
    public Path path()
        {
        return new PathImpl(
            JdbcResourceIdentFactory.CATALOGS_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public JdbcResourceCatalogsController()
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
     * URL path for the create method.
     * 
     */
    public static final String CREATE_PATH = "create" ;

    /**
     * MVC property for the select name.
     *
     */
    public static final String SELECT_NAME = "jdbc.resource.catalogs.select.name" ;

    /**
     * MVC property for the select results.
     *
     */
    public static final String SELECT_RESULT = "jdbc.resource.catalogs.select.result" ;

    /**
     * MVC property for the search text.
     *
     */
    public static final String SEARCH_TEXT = "jdbc.resource.catalogs.search.text" ;

    /**
     * MVC property for the search results.
     *
     */
    public static final String SEARCH_RESULT = "jdbc.resource.catalogs.search.result" ;

    /**
     * MVC property for the create name.
     *
     */
    public static final String CREATE_NAME = "jdbc.resource.catalogs.create.name" ;

    /**
     * Get the parent JdbcResource based on the request ident.
     *
     */
    @ModelAttribute(JdbcResourceController.RESOURCE_ENTITY)
    public JdbcResource resource(
        @PathVariable("ident")
        final String ident
        ){
        try {
            return womble().resources().jdbc().resources().select(
                womble().resources().jdbc().resources().ident(
                    ident
                    )
                );
            }
        catch (IdentifierNotFoundException e)
            {
            log.error("Unable to locate resource [{}]", ident);
            return null ;
            }
        }

    /**
     * Wrap the JdbcResource as a bean.
     * 
    @ModelAttribute(JdbcResourceController.RESOURCE_BEAN)
    public JdbcResourceBean bean(
        @ModelAttribute(JdbcResourceController.RESOURCE_ENTITY)
        final JdbcResource resource
        ){
        return new JdbcResourceBean(
            resource
            );
        }
     */

    /**
     * Select all.
     * 
     */
    public JdbcCatalogBeanIter select(
        final JdbcResource resource
        ){
        return new JdbcCatalogBeanIter(
            resource.catalogs().select()
            );
        }

    /**
     * Default HTML GET request (select all).
     *
     */
    @RequestMapping(value="", method=RequestMethod.GET)
    public ModelAndView htmlIndex(
        @ModelAttribute(JdbcResourceController.RESOURCE_ENTITY)
        final JdbcResource resource,
        final ModelAndView model
        ){
        model.addObject(
            JdbcResourceCatalogsController.SELECT_RESULT,
            select(
                resource
                )
            );
        model.setViewName(
            "jdbc/catalog/select"
            );
        return model ;
        }

    /**
     * HTML GET request to select all.
     *
     */
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.GET)
    public ModelAndView htmlSelect(
        @ModelAttribute(JdbcResourceController.RESOURCE_ENTITY)
        final JdbcResource resource,
        final ModelAndView model
        ){
        model.addObject(
            JdbcResourceCatalogsController.SELECT_RESULT,
            select(
                resource
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
    public JdbcCatalogBeanIter jsonSelect(
        @ModelAttribute(JdbcResourceController.RESOURCE_ENTITY)
        final JdbcResource resource,
        final ModelAndView model
        ){
        return select(
            resource
            );
        }

    /**
     * Select by name.
     * 
     */
    public JdbcCatalogBean select(
        JdbcResource resource,
        String name
        ){
        return new JdbcCatalogBean(
            resource.catalogs().select(
                name
                )
            );
        }
    
    /**
     * HTML GET or POST request to select by name.
     *
     */
    @RequestMapping(value=SELECT_PATH, params=SELECT_NAME)
    public ModelAndView htmlSelect(
        @ModelAttribute(JdbcResourceController.RESOURCE_ENTITY)
        final JdbcResource resource,
        @RequestParam(SELECT_NAME)
        final String name,
        final ModelAndView model
        ){
        model.addObject(
            SELECT_NAME,
            name
            );
        model.addObject(
            SELECT_RESULT,
            select(
                resource,
                name
                )
            );
        model.setViewName(
            "jdbc/catalog/select"
            );
        return model ;
        }

    /**
     * JSON GET or POST request to select by name.
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, params=SELECT_NAME, produces=JSON_MAPPING)
    public JdbcCatalogBean jsonSelect(
        @ModelAttribute(JdbcResourceController.RESOURCE_ENTITY)
        final JdbcResource resource,
        @RequestParam(SELECT_NAME)
        final String name,
        final ModelAndView model
        ){
        return select(
            resource,
            name
            );
        }

    /**
     * Search by text.
     * 
     */
    public JdbcCatalogBeanIter search(
        JdbcResource resource,
        String text
        ){
        return new JdbcCatalogBeanIter(
            resource.catalogs().search(
                text
                )
            );
        }
    
    /**
     * HTML GET request to display the search form.
     *
     */
    @RequestMapping(value=SEARCH_PATH, method=RequestMethod.GET)
    public ModelAndView htmlSearch(
        @ModelAttribute(JdbcResourceController.RESOURCE_ENTITY)
        final JdbcResource resource,
        final ModelAndView model
        ){
        model.setViewName(
            "jdbc/catalog/search"
            );
        return model ;
        }

    /**
     * HTML GET or POST request to search by text.
     * @todo Wrap the entities as beans (with URI) 
     *
     */
    @RequestMapping(value=SEARCH_PATH, params=SEARCH_TEXT)
    public ModelAndView htmlSearch(
        @ModelAttribute(JdbcResourceController.RESOURCE_ENTITY)
        final JdbcResource resource,
        @RequestParam(SEARCH_TEXT)
        final String text,
        final ModelAndView model
        ){
        model.addObject(
            SEARCH_TEXT,
            text
            );
        model.addObject(
            SEARCH_RESULT,
            search(
                resource,
                text
                )
            );
        model.setViewName(
            "jdbc/catalog/search"
            );
        return model ;
        }

    /**
     * JSON GET or POST request to search by text.
     * @todo change the server side search return a list
     *  
     */
    @ResponseBody
    @RequestMapping(value=SEARCH_PATH, params=SEARCH_TEXT, produces=JSON_MAPPING)
    public JdbcCatalogBeanIter jsonSearch(
        @ModelAttribute(JdbcResourceController.RESOURCE_ENTITY)
        final JdbcResource resource,
        @RequestParam(SEARCH_TEXT)
        final String text,
        final ModelAndView model
        ){
        return search(
            resource,
            text
            );
        }

    /**
     * HTML GET request to display the create form.
     *
     */
    @RequestMapping(value=CREATE_PATH, method=RequestMethod.GET)
    public ModelAndView htmlCreate(
        @ModelAttribute(JdbcResourceController.RESOURCE_ENTITY)
        final JdbcResource resource,
        final ModelAndView model
        ){
        model.setViewName(
            "jdbc/catalog/create"
            );
        return model ;
        }

    /**
     * Create a new catalog.
     *
     */
    public JdbcCatalogBean create(
        final JdbcResource resource,
        final String name
        ){
        return new JdbcCatalogBean(
            resource.catalogs().create(
                name
                )
            );
        }

    /**
     * HTML POST request to create a new AdqlService.
     *
     */
    @RequestMapping(value=CREATE_PATH, method=RequestMethod.POST)
    public ResponseEntity<String>  htmlCreate(
        @ModelAttribute(JdbcResourceController.RESOURCE_ENTITY)
        final JdbcResource resource,
        @RequestParam(CREATE_NAME)
        final String name,
        final ModelAndView model
        ){
        return new ResponseEntity<String>(
            new RedirectHeader(
                create(
                    resource,
                    name
                    )
                ),
            HttpStatus.SEE_OTHER
            ); 
        }

    /**
     * JSON POST request to create a new AdqlService.
     *
     */
    @RequestMapping(value=CREATE_PATH, method=RequestMethod.POST, produces=JSON_MAPPING)
    public ResponseEntity<JdbcCatalogBean> jsonCreate(
        @ModelAttribute(JdbcResourceController.RESOURCE_ENTITY)
        final JdbcResource resource,
        @RequestParam(CREATE_NAME)
        final String name,
        final ModelAndView model
        ){
        JdbcCatalogBean catalog = create(
            resource,
            name
            );
        return new ResponseEntity<JdbcCatalogBean>(
            catalog,
            new RedirectHeader(
                catalog 
                ),
            HttpStatus.CREATED
            ); 
        }
    }
