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

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityController;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;

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
extends AbstractEntityController<JdbcResource, JdbcResourceBean>
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
     * MVC property for the initial name.
     *
     */
    public static final String CREATE_NAME = "jdbc.resource.create.name" ;

    /**
     * MVC property for the initial catalog name.
     *
     */
    public static final String CREATE_CATALOG = "jdbc.resource.create.catalog" ;

    /**
     * MVC property for the JDBC driver name.
     *
     */
    public static final String CREATE_DRIVER = "jdbc.resource.create.driver" ;

    /**
     * MVC property for the initial connection URL.
     *
     */
    public static final String CREATE_CONN_URL = "jdbc.resource.create.url" ;

    /**
     * MVC property for the initial connection user name.
     *
     */
    public static final String CREATE_CONN_USER = "jdbc.resource.create.user" ;

    /**
     * MVC property for the initial connection password.
     *
     */
    public static final String CREATE_CONN_PASS = "jdbc.resource.create.pass" ;

    @Override
    public JdbcResourceBean bean(final JdbcResource entity)
        {
        return new JdbcResourceBean(
            entity
            );
        }

    @Override
    public Iterable<JdbcResourceBean> bean(final Iterable<JdbcResource> iter)
        {
        return new JdbcResourceBean.Iter(
            iter
            );
        }

    /**
     * JSON GET request to select all.
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.GET, produces=JSON_MIME)
    public Iterable<JdbcResourceBean> select(
        final ModelAndView model
        ){
        return bean(
            factories().jdbc().resources().entities().select()
            );
        }

    /**
     * JSON POST request to create a new resource.
     *
     */
    @ResponseBody
    @RequestMapping(value=CREATE_PATH, method=RequestMethod.POST, produces=JSON_MIME)
    public ResponseEntity<JdbcResourceBean> create(
        @RequestParam(value=CREATE_NAME, required=true)
        final String name,
        @RequestParam(value=CREATE_CONN_URL, required=false)
        final String url,
        @RequestParam(value=CREATE_CONN_USER, required=false)
        final String user,
        @RequestParam(value=CREATE_CONN_PASS, required=false)
        final String pass,
        @RequestParam(value=CREATE_DRIVER, required=false)
        final String driver,
        @RequestParam(value=CREATE_CATALOG, required=false)
        final String catalog
        ){
        return created(
            factories().jdbc().resources().entities().create(
                catalog,
                name,
                url,
                user,
                pass,
                driver
                )
            );
        }
    }
