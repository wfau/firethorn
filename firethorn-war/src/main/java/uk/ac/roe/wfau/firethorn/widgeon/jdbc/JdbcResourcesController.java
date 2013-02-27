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

import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcConnection;
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
        return path(
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
     * MVC property for the initial name.
     *
     */
    public static final String CREATE_NAME = "jdbc.resource.create.name" ;

    /**
     * MVC property for the initial OGSA-DAI resource.
     *
     */
    public static final String CREATE_OGSADAI = "jdbc.resource.create.ogsadai" ;

    /**
     * MVC property for the initial catalog name.
     *
     */
    public static final String CREATE_CATALOG = "jdbc.resource.create.catalog" ;

    /**
     * MVC property for the initial connection URL.
     *
     */
    public static final String CREATE_URL = "jdbc.resource.create.url" ;

    /**
     * MVC property for the initial connection user name.
     *
     */
    public static final String CREATE_USER = "jdbc.resource.create.user" ;

    /**
     * MVC property for the initial connection password.
     *
     */
    public static final String CREATE_PASS = "jdbc.resource.create.pass" ;

    /**
     * JSON GET request to select all.
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.GET, produces=JSON_MAPPING)
    public JdbcResourceBean.Iter jsonSelect(
        final ModelAndView model
        ){
        return new JdbcResourceBean.Iter(
            factories().jdbc().resources().select()
            );
        }

    /**
     * JSON GET or POST request to search by text.
     *
     */
    @ResponseBody
    @RequestMapping(value=SEARCH_PATH, params=SEARCH_TEXT, produces=JSON_MAPPING)
    public JdbcResourceBean.Iter jsonSearch(
        @RequestParam(SEARCH_TEXT)
        final String text,
        final ModelAndView model
        ){
        return new JdbcResourceBean.Iter(
            factories().jdbc().resources().search(
                text
                )
            );
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
        @RequestParam(value=CREATE_CATALOG, required=false)
        final String catalog,
        @RequestParam(value=CREATE_OGSADAI, required=false)
        final String ogsadai,

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
        // TODO - set the initial catalog
        if (catalog != null)
            {
            bean.entity().catalog(
                catalog
                );
            bean.entity().update();
            }
        //
        // TODO - set the initial resource
        if (ogsadai != null)
            {
            bean.entity().ogsaid(
                ogsadai
                );
            bean.entity().update();
            }
        return new ResponseEntity<JdbcResourceBean>(
            bean,
            new RedirectHeader(
                bean
                ),
            HttpStatus.CREATED
            );
        }
    }
