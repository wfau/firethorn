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

import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityController;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.widgeon.name.JdbcResourceLinkFactory;

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
    public static final String RESOURCE_NAME_PARAM = "jdbc.resource.name" ;

    /**
     * MVC property for the initial name.
     *
    public static final String CREATE_NAME = "jdbc.resource.name" ;
     */

    /**
     * MVC property for the initial catalog name.
     *
     */
    public static final String CATALOG_NAME_PARAM = "jdbc.resource.catalog" ;

    /**
     * MVC property for the JDBC driver name.
     * TODO Use value from JdbcResourceController 
     *
     */
    public static final String CONNECTION_DRIVER_PARAM = "jdbc.connection.driver" ;

    /**
     * MVC property for the initial connection URL.
     * TODO Use value from JdbcResourceController 
     *
     */
    public static final String CONNECTION_URL_PARAM = "jdbc.connection.url" ;

    /**
     * MVC property for the initial connection user name.
     * TODO Use value from JdbcResourceController 
     *
     */
    public static final String CONNECTION_USER_PARAM = "jdbc.connection.user" ;

    /**
     * MVC property for the initial connection password.
     *
     */
    public static final String CONNECTION_PASS_PARAM = "jdbc.connection.pass" ;

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
     * @throws ProtectionException 
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.GET, produces=JSON_MIME)
    public ResponseEntity<Iterable<JdbcResourceBean>> select(
        final ModelAndView model
        )
    throws ProtectionException
        {
        return selected(
            factories().jdbc().resources().entities().select()
            );
        }

    /**
     * JSON POST request to create a new resource.
     * @throws ProtectionException 
     *
     */
    @ResponseBody
    @RequestMapping(value=CREATE_PATH, method=RequestMethod.POST, produces=JSON_MIME)
    public ResponseEntity<JdbcResourceBean> create(
        @RequestParam(value=CATALOG_NAME_PARAM, required=false)
        final String catalog,
        @RequestParam(value=RESOURCE_NAME_PARAM, required=true)
        final String name,
        @RequestParam(value=CONNECTION_URL_PARAM, required=false)
        final String url,
        @RequestParam(value=CONNECTION_USER_PARAM, required=false)
        final String user,
        @RequestParam(value=CONNECTION_PASS_PARAM, required=false)
        final String pass,
        @RequestParam(value=CONNECTION_DRIVER_PARAM, required=false)
        final String driver
        )
    throws ProtectionException
        {
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
