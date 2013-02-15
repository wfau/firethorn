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

import uk.ac.roe.wfau.firethorn.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractController;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.webapp.paths.PathImpl;

/**
 * Spring MVC controller for <code>JdbcResource</code> schema.
 *
 */
@Slf4j
@Controller
@RequestMapping(JdbcResourceLinkFactory.RESOURCE_SCHEMA_PATH)
public class JdbcResourceSchemaController
extends AbstractController
    {
    @Override
    public Path path()
        {
        return path(
            JdbcResourceLinkFactory.RESOURCE_SCHEMA_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public JdbcResourceSchemaController()
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
    public static final String CREATE_PATH = "create" ;
     */

    /**
     * MVC property for the Resource name.
     *
     */
    public static final String SELECT_NAME = "jdbc.resource.schema.select.name" ;

    /**
     * MVC property for the select results.
     *
     */
    public static final String SELECT_RESULT = "jdbc.resource.schema.select.result" ;

    /**
     * MVC property for the search text.
     *
     */
    public static final String SEARCH_TEXT = "jdbc.resource.schema.search.text" ;

    /**
     * MVC property for the search results.
     *
     */
    public static final String SEARCH_RESULT = "jdbc.resource.schema.search.result" ;

    /**
     * MVC property for the create name.
     *
    public static final String CREATE_NAME = "jdbc.resource.schema.create.name" ;
     */

    /**
     * Get the parent entity based on the request ident.
     * @throws NotFoundException
     *
     */
    @ModelAttribute(JdbcResourceController.RESOURCE_ENTITY)
    public JdbcResource resource(
        @PathVariable("ident")
        final String ident
        ) throws NotFoundException{
        log.debug("schema() [{}]", ident);
        return factories().jdbc().resources().select(
            factories().jdbc().resources().idents().ident(
                ident
                )
            );
        }

    /**
     * Select all.
     *
     */
    public JdbcSchemaBean.Iter select(
        @ModelAttribute(JdbcResourceController.RESOURCE_ENTITY)
        final JdbcResource resource
        ){
        log.debug("select()");
        return new JdbcSchemaBean.Iter(
            resource.schemas().select()
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
        log.debug("htmlIndex()");
        model.addObject(
            JdbcResourceSchemaController.SELECT_RESULT,
            select(
                resource
                )
            );
        model.setViewName(
            "jdbc/resource/select"
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
        log.debug("htmlSelect()");
        model.addObject(
            JdbcResourceSchemaController.SELECT_RESULT,
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
    public JdbcSchemaBean.Iter jsonSelect(
        @ModelAttribute(JdbcResourceController.RESOURCE_ENTITY)
        final JdbcResource resource,
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
    public JdbcSchemaBean select(
        @ModelAttribute(JdbcResourceController.RESOURCE_ENTITY)
        final JdbcResource resource,
        final String name
        ){
        log.debug("select(String) [{}]", name);
        return new JdbcSchemaBean(
            resource.schemas().select(
                name
                )
            );
        }

    /**
     * HTML request to select by name.
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
        log.debug("htmlSelect(String) [{}]", name);
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
     * JSON request to select by name.
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, params=SELECT_NAME, produces=JSON_MAPPING)
    public JdbcSchemaBean jsonSelect(
        @ModelAttribute(JdbcResourceController.RESOURCE_ENTITY)
        final JdbcResource resource,
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
    public JdbcSchemaBean.Iter search(
        @ModelAttribute(JdbcResourceController.RESOURCE_ENTITY)
        final JdbcResource resource,
        final String text
        ){
        log.debug("search(String) [{}]", text);
        return new JdbcSchemaBean.Iter(
            resource.schemas().search(
                text
                )
            );
        }

    /**
     * HTML GET request for the search form.
     *
     */
    @RequestMapping(value=SEARCH_PATH, method=RequestMethod.GET)
    public ModelAndView htmlSearch(
        @ModelAttribute(JdbcResourceController.RESOURCE_ENTITY)
        final JdbcResource resource,
        final ModelAndView model
        ){
        log.debug("htmlSearch");
        model.setViewName(
            "jdbc/catalog/search"
            );
        return model ;
        }

    /**
     * HTML request to search by text.
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
        log.debug("htmlSearch(String) [{}]", text);
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
     * JSON request to search by text.
     *
     */
    @ResponseBody
    @RequestMapping(value=SEARCH_PATH, params=SEARCH_TEXT, produces=JSON_MAPPING)
    public JdbcSchemaBean.Iter jsonSearch(
        @ModelAttribute(JdbcResourceController.RESOURCE_ENTITY)
        final JdbcResource resource,
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
     * Create a new schema.
     *
    public JdbcSchemaBean create(
        @ModelAttribute(JdbcResourceController.RESOURCE_ENTITY)
        final JdbcResource resource,
        final String name
        ){
        log.debug("create(String) [{}]", name);
        return new JdbcSchemaBean(
            resource.schemas().create(
                name
                )
            );
        }
     */

    /**
     * JSON POST request to create a new schema.
     *
    @RequestMapping(value=CREATE_PATH, method=RequestMethod.POST, produces=JSON_MAPPING)
    public ResponseEntity<JdbcSchemaBean> jsonCreate(
        @ModelAttribute(JdbcResourceController.RESOURCE_ENTITY)
        final JdbcResource resource,
        @RequestParam(CREATE_NAME)
        final String name,
        final ModelAndView model
        ){
        log.debug("jsonCreate(String) [{}]", name);
        final JdbcSchemaBean schema = create(
            resource,
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
     */
    }
