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
@RequestMapping(JdbcCatalogIdentFactory.SCHEMAS_PATH)
public class JdbcCatalogSchemasController
extends AbstractController
    {
    @Override
    public Path path()
        {
        return new PathImpl(
            JdbcCatalogIdentFactory.SCHEMAS_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public JdbcCatalogSchemasController()
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
     * MVC property for the Resource name.
     *
     */
    public static final String SELECT_NAME = "jdbc.catalog.schemas.select.name" ;

    /**
     * MVC property for the select results.
     *
     */
    public static final String SELECT_RESULT = "jdbc.catalog.schemas.select.result" ;

    /**
     * MVC property for the search text.
     *
     */
    public static final String SEARCH_TEXT = "jdbc.catalog.schemas.search.text" ;

    /**
     * MVC property for the search results.
     *
     */
    public static final String SEARCH_RESULT = "jdbc.catalog.schemas.search.result" ;

    /**
     * MVC property for the create name.
     *
     */
    public static final String CREATE_NAME = "jdbc.catalog.schemas.create.name" ;

    /**
     * Get the parent JdbcCatalog based on the request ident.
     *
     */
    @ModelAttribute(JdbcCatalogController.CATALOG_ENTITY)
    public JdbcCatalog catalog(
        @PathVariable("ident")
        final String ident
        ){
        log.debug("catalog() [{}]", ident);
        try {
            return womble().resources().jdbc().catalogs().select(
                womble().resources().jdbc().catalogs().ident(
                    ident
                    )
                );
            }
        catch (IdentifierNotFoundException e)
            {
            log.error("Unable to locate catalog [{}]", ident);
            return null ;
            }
        }

    /**
     * Wrap the JdbcCatalog as a bean.
     * 
    @ModelAttribute(JdbcCatalogController.CATALOG_BEAN)
    public JdbcCatalogBean bean(
        @ModelAttribute(JdbcCatalogController.CATALOG_ENTITY)
        final JdbcCatalog catalog
        ){
        return new JdbcCatalogBean(
            catalog
            );
        }
     */

    /**
     * Select all.
     * 
     */
    public JdbcSchemaBeanIter select(
        final JdbcCatalog catalog
        ){
        log.debug("select()");
        return new JdbcSchemaBeanIter(
            catalog.schemas().select()
            );
        }

    /**
     * Default HTML GET request (select all).
     *
     */
    @RequestMapping(value="", method=RequestMethod.GET)
    public ModelAndView htmlIndex(
        @ModelAttribute(JdbcCatalogController.CATALOG_ENTITY)
        final JdbcCatalog catalog,
        final ModelAndView model
        ){
        log.debug("htmlIndex()");
        model.addObject(
            JdbcCatalogSchemasController.SELECT_RESULT,
            select(
                catalog
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
        @ModelAttribute(JdbcCatalogController.CATALOG_ENTITY)
        final JdbcCatalog catalog,
        final ModelAndView model
        ){
        log.debug("htmlSelect()");
        model.addObject(
            JdbcCatalogSchemasController.SELECT_RESULT,
            select(
                catalog
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
    public JdbcSchemaBeanIter jsonSelect(
        @ModelAttribute(JdbcCatalogController.CATALOG_ENTITY)
        final JdbcCatalog catalog,
        final ModelAndView model
        ){
        log.debug("jsonSelect()");
        return select(
            catalog
            );
        }

    /**
     * Select by name.
     * 
     */
    public JdbcSchemaBean select(
        JdbcCatalog catalog,
        String name
        ){
        log.debug("select(String) [{}]", name);
        return new JdbcSchemaBean(
            catalog.schemas().select(
                name
                )
            );
        }
    
    /**
     * HTML GET or POST request to select by name.
     * @todo Wrap the entities as beans (with URI) 
     *
     */
    @RequestMapping(value=SELECT_PATH, params=SELECT_NAME)
    public ModelAndView htmlSelect(
        @ModelAttribute(JdbcCatalogController.CATALOG_ENTITY)
        final JdbcCatalog catalog,
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
                catalog,
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
    public JdbcSchemaBean jsonSelect(
        @ModelAttribute(JdbcCatalogController.CATALOG_ENTITY)
        final JdbcCatalog catalog,
        @RequestParam(SELECT_NAME)
        final String name,
        final ModelAndView model
        ){
        log.debug("jsonSelect(String) [{}]", name);
        return select(
            catalog,
            name
            );
        }

    /**
     * Search by text.
     * 
     */
    public JdbcSchemaBeanIter search(
        JdbcCatalog catalog,
        String text
        ){
        log.debug("search()");
        return new JdbcSchemaBeanIter(
            catalog.schemas().search(
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
        @ModelAttribute(JdbcCatalogController.CATALOG_ENTITY)
        final JdbcCatalog catalog,
        final ModelAndView model
        ){
        log.debug("htmlSearch");
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
        @ModelAttribute(JdbcCatalogController.CATALOG_ENTITY)
        final JdbcCatalog catalog,
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
                catalog,
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
    public JdbcSchemaBeanIter jsonSearch(
        @ModelAttribute(JdbcCatalogController.CATALOG_ENTITY)
        final JdbcCatalog catalog,
        @RequestParam(SEARCH_TEXT)
        final String text,
        final ModelAndView model
        ){
        log.debug("jsonSearch(String) [{}]", text);
        return search(
            catalog,
            text
            );
        }

    /**
     * HTML GET request to display the create form.
     *
     */
    @RequestMapping(value=CREATE_PATH, method=RequestMethod.GET)
    public ModelAndView htmlCreate(
        @ModelAttribute(JdbcCatalogController.CATALOG_ENTITY)
        final JdbcCatalog catalog,
        final ModelAndView model
        ){
        log.debug("create()");
        model.setViewName(
            "jdbc/catalog/create"
            );
        return model ;
        }

    /**
     * Create a new catalog.
     *
     */
    public JdbcSchemaBean create(
        final JdbcCatalog catalog,
        final String name
        ){
        log.debug("create(String) [{}]", name);
        return new JdbcSchemaBean(
            catalog.schemas().create(
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
        @ModelAttribute(JdbcCatalogController.CATALOG_ENTITY)
        final JdbcCatalog catalog,
        @RequestParam(CREATE_NAME)
        final String name,
        final ModelAndView model
        ){
        log.debug("htmlCreate(String) [{}]", name);
        return new ResponseEntity<String>(
            new RedirectHeader(
                create(
                    catalog,
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
    public ResponseEntity<JdbcSchemaBean> jsonCreate(
        @ModelAttribute(JdbcCatalogController.CATALOG_ENTITY)
        final JdbcCatalog catalog,
        @RequestParam(CREATE_NAME)
        final String name,
        final ModelAndView model
        ){
        log.debug("jsonCreate(String) [{}]", name);
        JdbcSchemaBean schema = create(
            catalog,
            name
            );
        return new ResponseEntity<JdbcSchemaBean>(
            schema,
            new RedirectHeader(
                schema
                ),
            HttpStatus.CREATED
            ); 
        }
    }
