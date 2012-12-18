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

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import uk.ac.roe.wfau.firethorn.tuesday.TuesdayJdbcConnection;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractController;
import uk.ac.roe.wfau.firethorn.webapp.control.RedirectHeader;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.webapp.paths.PathImpl;

/**
 * Spring MVC controller for <code>JdbcResource</code>.
 * TODO better exception handling.
 * http://blog.newsplore.com/2010/08/04/restful-error-handling-with-tomcat-springmvc
 * http://static.springsource.org/spring/docs/3.1.x/javadoc-api/org/springframework/util/AntPathMatcher.html
 * 
 */
@Controller
@RequestMapping(JdbcResourceLinkFactory.SERVICE_PATH)
public class JdbcResourcesController
extends AbstractController
    {

    @Override
    public Path path()
        {
        return new PathImpl(
            JdbcResourceLinkFactory.SERVICE_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public JdbcResourcesController()
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
    public static final String SELECT_NAME = "jdbc.resource.select.name" ;

    /**
     * MVC property for the select results.
     *
     */
    public static final String SELECT_RESULT = "jdbc.resource.select.result" ;

    /**
     * MVC property for the search text.
     *
     */
    public static final String SEARCH_TEXT = "jdbc.resource.search.text" ;

    /**
     * MVC property for the search results.
     *
     */
    public static final String SEARCH_RESULT = "jdbc.resource.search.result" ;

    /**
     * MVC property for the create name.
     *
     */
    public static final String CREATE_NAME = "jdbc.resource.create.name" ;

    /**
     * MVC property for setting the connection URL.
     *
     */
    public static final String CREATE_URL = "jdbc.resource.create.url" ;

    /**
     * MVC property for setting the connection user name.
     *
     */
    public static final String CREATE_USER = "jdbc.resource.create.user" ;

    /**
     * MVC property for setting the connection password.
     *
     */
    public static final String CREATE_PASS = "jdbc.resource.create.pass" ;

    /**
     * HTML GET request to display the index page.
     *
     */
    @RequestMapping(value="", method=RequestMethod.GET)
    public ModelAndView htmlIndex(
        final ModelAndView model
        ){
        model.setViewName(
            "jdbc/resource/index"
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
        final ModelAndView model
        ){
        model.addObject(
            SELECT_RESULT,
            new JdbcResourceBeanIter(
                factories().jdbc().resources().select()
                )
            );
        model.setViewName(
            "jdbc/resource/select"
            );
        return model ;
        }

    /**
     * JSON GET request to select all.
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.GET, produces=JSON_MAPPING)
    public JdbcResourceBeanIter jsonSelect(
        final ModelAndView model
        ){
        return new JdbcResourceBeanIter(
            factories().jdbc().resources().select()
            );
        }

    /**
     * HTML GET request to display the search form.
     *
     */
    @RequestMapping(value=SEARCH_PATH, method=RequestMethod.GET)
    public ModelAndView htmlSearch(
        final ModelAndView model
        ){
        model.setViewName(
            "jdbc/resource/search"
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
            new JdbcResourceBeanIter(
                factories().jdbc().resources().search(
                    text
                    )
                )
            );
        model.setViewName(
            "jdbc/resource/search"
            );
        return model ;
        }

    /**
     * JSON GET or POST request to search by text.
     *
     */
    @ResponseBody
    @RequestMapping(value=SEARCH_PATH, params=SEARCH_TEXT, produces=JSON_MAPPING)
    public JdbcResourceBeanIter jsonSearch(
        @RequestParam(SEARCH_TEXT)
        final String text,
        final ModelAndView model
        ){
        return new JdbcResourceBeanIter(
            factories().jdbc().resources().search(
                text
                )
            );
        }

    /**
     * HTML GET request to display the create form.
     *
     */
    @RequestMapping(value=CREATE_PATH, method=RequestMethod.GET)
    public ModelAndView htmlCreate(
        final ModelAndView model
        ){
        model.setViewName(
            "jdbc/resource/create"
            );
        return model ;
        }

    /**
     * HTML POST request to create a new resource.
     *
     */
    @RequestMapping(value=CREATE_PATH, method=RequestMethod.POST)
    public ResponseEntity<String>  htmlCreate(
        @RequestParam(CREATE_NAME)
        final String name,
        final ModelAndView model
        ){
        try {
            final JdbcResourceBean bean = new JdbcResourceBean(
                factories().jdbc().resources().create(
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
     * JSON POST request to create a new resource.
     *
     */
    @RequestMapping(value=CREATE_PATH, method=RequestMethod.POST, produces=JSON_MAPPING)
    public ResponseEntity<JdbcResourceBean> jsonCreate(
        @RequestParam(value=CREATE_NAME, required=true)
        final String name,
        @RequestParam(value=CREATE_URL, required=false)
        final String url,
        @RequestParam(value=CREATE_USER, required=false)
        final String user,
        @RequestParam(value=CREATE_PASS, required=false)
        final String pass,

        final ModelAndView model
        ){

        final JdbcResourceBean bean = new JdbcResourceBean(
            factories().jdbc().resources().create(
                name,
                url,
                user,
                pass
                )
            );
        //
        // TODO - temp fix to trigger scan
        bean.entity().connection().status(
                TuesdayJdbcConnection.Status.ENABLED
                );
        return new ResponseEntity<JdbcResourceBean>(
            bean,
            new RedirectHeader(
                bean
                ),
            HttpStatus.CREATED
            );
        }
    }

