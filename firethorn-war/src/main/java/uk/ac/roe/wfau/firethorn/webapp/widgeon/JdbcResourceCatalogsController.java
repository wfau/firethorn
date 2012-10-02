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
package uk.ac.roe.wfau.firethorn.webapp.widgeon;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import uk.ac.roe.wfau.firethorn.webapp.control.ControllerBase;
import uk.ac.roe.wfau.firethorn.webapp.control.RedirectHeader;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.webapp.paths.PathImpl;

/**
 * Spring MVC controller for AdqlServices.
 *
 */
@Slf4j
@Controller
@RequestMapping(JdbcResourceCatalogsController.CONTROLLER_PATH)
public class JdbcResourceCatalogsController
extends ControllerBase
    {
    /**
     * URL path for this Controller.
     *
     */
    public static final String CONTROLLER_PATH = "jdbc/resource/{ident}/catalogs" ;

    @Override
    public Path path()
        {
        return new PathImpl(
            CONTROLLER_PATH
            );
        }

    /**
     * Autowired reference to our JdbcResourceCatalogController.
     * 
     */
    @Autowired
    private JdbcCatalogController catalogController ;

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
     * MVC property for the Resource name.
     *
     */
    public static final String SELECT_NAME = "jdbc.resource.catalogs.select.name" ;

    /**
     * MVC property for the selected Resource(s).
     *
     */
    public static final String SELECT_RESULT = "jdbc.resource.catalogs.select.result" ;

    /**
     * MVC property for the search text.
     *
     */
    public static final String SEARCH_TEXT = "jdbc.resource.catalogs.search.text" ;

    /**
     * MVC property for the selected Resource(s).
     *
     */
    public static final String SEARCH_RESULT = "jdbc.resource.catalogs.search.result" ;

    /**
     * MVC property for the Resource name.
     *
     */
    public static final String CREATE_NAME = "jdbc.resource.catalogs.create.name" ;

    /**
     * HTML GET request to display the index page.
     *
     */
    @RequestMapping(value="", method=RequestMethod.GET)
    public ModelAndView htmlIndex(
        @PathVariable("ident")
        final String ident,
        final ModelAndView model
        ){
        model.setViewName(
            "jdbc/catalog/index"
            );
        return model ;
        }

    /**
     * HTML GET request to select all.
     * @todo Wrap the entities as beans (with URI) 
     *
     */
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.GET)
    public ModelAndView htmlSelect(
        @PathVariable("ident")
        final String ident,
        final ModelAndView model
        ){
        try {
            model.addObject(
                SELECT_RESULT,
                womble().resources().jdbc().select(
                    womble().resources().jdbc().ident(
                        ident
                        )
                    ).catalogs().select()
                );
            model.setViewName(
                "jdbc/catalog/select"
                );
            return model ;
            }
        catch (Exception ouch)
            {
            return null ;
            }
        }

    /**
     * JSON GET request to select all.
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.GET, produces=JSON_MAPPING)
    public JdbcCatalogBeanIter jsonSelect(
        @PathVariable("ident")
        final String ident,
        final ModelAndView model,
        final HttpServletRequest request
        ){
        try {
            return new JdbcCatalogBeanIter(
                catalogController.builder(
                    request
                    ),
                womble().resources().jdbc().select(
                    womble().resources().jdbc().ident(
                        ident
                        )
                    ).catalogs().select()
                );
            }
        catch (Exception ouch)
            {
            return null ;
            }
        }
    
    /**
     * HTML GET or POST request to select by name.
     * @todo Wrap the entities as beans (with URI) 
     *
     */
    @RequestMapping(value=SELECT_PATH, params=SELECT_NAME)
    public ModelAndView htmlSelect(
        @PathVariable("ident")
        final String ident,
        @RequestParam(SELECT_NAME)
        final String name,
        final ModelAndView model
        ){
        try {
            model.addObject(
                SELECT_RESULT,
                womble().resources().jdbc().select(
                    womble().resources().jdbc().ident(
                        ident
                        )
                    ).catalogs().select(
                        name
                        )
                );
            model.setViewName(
                "jdbc/catalog/select"
                );
            return model ;
            }
        catch (Exception ouch)
            {
            return null ;
            }
        }

    /**
     * JSON GET or POST request to select by name.
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, params=SELECT_NAME, produces=JSON_MAPPING)
    public JdbcCatalogBean jsonSelect(
        @PathVariable("ident")
        final String ident,
        @RequestParam(SELECT_NAME)
        final String name,
        final ModelAndView model,
        final HttpServletRequest request
        ){
        try {
            return new JdbcCatalogBean(
                catalogController.builder(
                    request
                    ),
                womble().resources().jdbc().select(
                    womble().resources().jdbc().ident(
                        ident
                        )
                    ).catalogs().select(
                        name
                        )
                );
            }
        catch (Exception ouch)
            {
            return null ;
            }
        }
    
    /**
     * HTML GET request to display the search form.
     *
     */
    @RequestMapping(value=SEARCH_PATH, method=RequestMethod.GET)
    public ModelAndView htmlSearch(
        @PathVariable("ident")
        final String ident,
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
        @PathVariable("ident")
        final String ident,
        @RequestParam(SEARCH_TEXT)
        final String text,
        final ModelAndView model
        ){
        try {
            model.addObject(
                SEARCH_RESULT,
                womble().resources().jdbc().select(
                    womble().resources().jdbc().ident(
                        ident
                        )
                    ).catalogs().search(
                        text
                        )
                );
            model.setViewName(
                "jdbc/catalog/search"
                );
            return model ;
            }
        catch (Exception ouch)
            {
            return null ;
            }
        }

    /**
     * JSON GET or POST request to search by text.
     * @todo change the server side search return a list
     *  
     */
    @ResponseBody
    @RequestMapping(value=SEARCH_PATH, params=SEARCH_TEXT, produces=JSON_MAPPING)
    public JdbcCatalogBeanIter jsonSearch(
        @PathVariable("ident")
        final String ident,
        @RequestParam(SEARCH_TEXT)
        final String text,
        final ModelAndView model,
        final HttpServletRequest request
        ){
        try {
            return new JdbcCatalogBeanIter(
                catalogController.builder(
                    request
                    ),
                womble().resources().jdbc().select(
                    womble().resources().jdbc().ident(
                        ident
                        )
                    ).catalogs().search(
                        text
                        )
                );
            }
        catch (Exception ouch)
            {
            return null ;
            }
        }

    /**
     * HTML GET request to display the create form.
     *
     */
    @RequestMapping(value=CREATE_PATH, method=RequestMethod.GET)
    public ModelAndView htmlCreate(
        @PathVariable("ident")
        final String ident,
        final ModelAndView model
        ){
        model.setViewName(
            "jdbc/catalog/create"
            );
        return model ;
        }

    /**
     * HTML POST request to create a new AdqlService.
     *
     */
    @RequestMapping(value=CREATE_PATH, method=RequestMethod.POST)
    public ResponseEntity<String>  htmlCreate(
        @PathVariable("ident")
        final String ident,
        @RequestParam(CREATE_NAME)
        final String name,
        final ModelAndView model,
        final HttpServletRequest request
        ){
        try {
            JdbcCatalogBean bean = new JdbcCatalogBean(
                catalogController.builder(
                    request
                    ),
                womble().resources().jdbc().select(
                    womble().resources().jdbc().ident(
                        ident
                        )
                    ).catalogs().create(
                        name
                        )
                );
            return new ResponseEntity<String>(
                new RedirectHeader(
                    bean
                    ),
                HttpStatus.SEE_OTHER
                ); 
            }
        catch (final Exception ouch)
            {
            return null ;
            }
        }

    /**
     * JSON POST request to create a new AdqlService.
     *
     */
    @RequestMapping(value=CREATE_PATH, method=RequestMethod.POST, produces=JSON_MAPPING)
    public ResponseEntity<JdbcCatalogBean> jsonCreate(
        @PathVariable("ident")
        final String ident,
        @RequestParam(CREATE_NAME)
        final String name,
        final ModelAndView model,
        final HttpServletRequest request
        ){
        try {
            JdbcCatalogBean bean = new JdbcCatalogBean(
                catalogController.builder(
                    request
                    ),
                    womble().resources().jdbc().select(
                        womble().resources().jdbc().ident(
                            ident
                            )
                        ).catalogs().create(
                            name
                            )
                );
            return new ResponseEntity<JdbcCatalogBean>(
                bean,
                new RedirectHeader(
                    bean
                    ),
                HttpStatus.CREATED
                ); 
            }
        catch (final Exception ouch)
            {
            return null ;
            }
        }
    }

