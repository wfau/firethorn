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

import uk.ac.roe.wfau.firethorn.common.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayJdbcResource;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractController;
import uk.ac.roe.wfau.firethorn.webapp.control.RedirectHeader;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.webapp.paths.PathImpl;
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlResourceBean;
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlResourceBeanIter;

/**
 * Spring MVC controller for <code>JdbcResource</code> catalogs.
 *
 */
@Slf4j
@Controller
@RequestMapping(JdbcResourceIdentFactory.ADQL_PATH )
public class JdbcResourceAdqlController
extends AbstractController
    {

    @Override
    public Path path()
        {
        return new PathImpl(
            JdbcResourceIdentFactory.ADQL_PATH 
            );
        }

    /**
     * Public constructor.
     *
     */
    public JdbcResourceAdqlController()
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
    public static final String SELECT_NAME = "jdbc.resource.adql.select.name" ;

    /**
     * MVC property for the select results.
     *
     */
    public static final String SELECT_RESULT = "jdbc.resource.adql.select.result" ;

    /**
     * MVC property for the search text.
     *
     */
    public static final String SEARCH_TEXT = "jdbc.resource.adql.search.text" ;

    /**
     * MVC property for the search results.
     *
     */
    public static final String SEARCH_RESULT = "jdbc.resource.adql.search.result" ;

    /**
     * MVC property for the create name.
     *
     */
    public static final String CREATE_NAME = "jdbc.resource.adql.create.name" ;

    /**
     * Get the base entity based on the request ident.
     * @throws NotFoundException  
     *
     */
    @ModelAttribute(JdbcResourceController.RESOURCE_ENTITY)
    public TuesdayJdbcResource resource(
        @PathVariable("ident")
        final String ident
        ) throws NotFoundException {
        log.debug("resource() [{}]", ident);
        return factories().jdbc().resources().select(
            factories().jdbc().resources().ident(
                ident
                )
            );
        }

    /**
     * Select all.
     *
     */
    public AdqlResourceBeanIter select(
        @ModelAttribute(JdbcResourceController.RESOURCE_ENTITY)
        final TuesdayJdbcResource resource
        ){
        log.debug("select()");
        return null ;
        /*
        return new AdqlResourceBeanIter(
            resource.views().select()
            );
        */
        }

    /**
     * JSON GET request to select all.
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.GET, produces=JSON_MAPPING)
    public AdqlResourceBeanIter jsonSelect(
        @ModelAttribute(JdbcResourceController.RESOURCE_ENTITY)
        final TuesdayJdbcResource resource,
        final ModelAndView model
        ){
        log.debug("jsonSelect()");
        return select(
            resource
            );
        }

    /**
     * Select by name.
     *
     */
    public AdqlResourceBean select(
        @ModelAttribute(JdbcResourceController.RESOURCE_ENTITY) final
        TuesdayJdbcResource resource,
        final String name
        ){
        log.debug("select(String) [{}]", name);
        return null ;
        /*
        return new AdqlResourceBean(
            resource.views().select(
                name
                )
            );
        */
        }

    /**
     * JSON request to select by name.
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, params=SELECT_NAME, produces=JSON_MAPPING)
    public AdqlResourceBean jsonSelect(
        @ModelAttribute(JdbcResourceController.RESOURCE_ENTITY)
        final TuesdayJdbcResource resource,
        @RequestParam(SELECT_NAME)
        final String name,
        final ModelAndView model
        ){
        log.debug("jsonSelect(String) [{}]", name);
        return select(
            resource,
            name
            );
        }

    /**
     * Search by text.
     *
     */
    public AdqlResourceBeanIter search(
        @ModelAttribute(JdbcResourceController.RESOURCE_ENTITY) final
        TuesdayJdbcResource resource,
        final String text
        ){
        log.debug("search(String) [{}]", text);
        return null ;
        /*
        return new AdqlResourceBeanIter(
            resource.views().search(
                text
                )
            );
        */
        }

    /**
     * JSON request to search by text.
     *
     */
    @ResponseBody
    @RequestMapping(value=SEARCH_PATH, params=SEARCH_TEXT, produces=JSON_MAPPING)
    public AdqlResourceBeanIter jsonSearch(
        @ModelAttribute(JdbcResourceController.RESOURCE_ENTITY)
        final TuesdayJdbcResource resource,
        @RequestParam(SEARCH_TEXT)
        final String text,
        final ModelAndView model
        ){
        log.debug("jsonSearch(String) [{}]", text);
        return search(
            resource,
            text
            );
        }

    /**
     * Create a new entity.
     *
     */
    public AdqlResourceBean create(
        @ModelAttribute(JdbcResourceController.RESOURCE_ENTITY)
        final TuesdayJdbcResource resource,
        final String name
        ){
        log.debug("create(String) [{}]", name);
        return null ;
        /*
        return new AdqlResourceBean(
            resource.views().create(
                name
                )
            );
        */
        }

    /**
     * JSON POST request to create a new entity.
     *
     */
    @RequestMapping(value=CREATE_PATH, method=RequestMethod.POST, produces=JSON_MAPPING)
    public ResponseEntity<AdqlResourceBean> jsonCreate(
        @ModelAttribute(JdbcResourceController.RESOURCE_ENTITY)
        final TuesdayJdbcResource resource,
        @RequestParam(CREATE_NAME)
        final String name,
        final ModelAndView model
        ){
        log.debug("jsonCreate(String) [{}]", name);
        final AdqlResourceBean created = create(
            resource,
            name
            );
        return new ResponseEntity<AdqlResourceBean>(
            created,
            new RedirectHeader(
                created
                ),
            HttpStatus.CREATED
            );
        }
    }
