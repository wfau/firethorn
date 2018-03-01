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
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.base.BaseComponent;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcProductType;
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
implements JdbcResourceModel
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
     * GET request to select all the {@link JdbcResource}s.
     * @return An {@Iterable} set of {@link JdbcResourceBean}s.
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
     * POST request to select a {@link JdbcResource} by {@link Identifier}.
     * @throws ProtectionException 
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.POST, params=RESOURCE_IDENT_PARAM, produces=JSON_MIME)
    public ResponseEntity<JdbcResourceBean> select_by_ident(
        @RequestParam(RESOURCE_IDENT_PARAM)
        final String ident
        )
    throws IdentifierNotFoundException, ProtectionException
        {
        return selected(
            factories().jdbc().resources().entities().select(
                factories().jdbc().resources().idents().ident(
                    ident
                    )
                )
            );
        }
    
    /**
     * POST request to select a {@link JdbcResource} by name.
     * @throws ProtectionException 
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.POST, params=RESOURCE_NAME_PARAM, produces=JSON_MIME)
    public ResponseEntity<JdbcResourceBean> select_by_name(
        @RequestParam(RESOURCE_NAME_PARAM)
        final String name
        )
    throws NameNotFoundException, ProtectionException
        {
        return selected(
            factories().jdbc().resources().entities().select(
                name
                )
            );
        }
    
    /**
     * POST request to create a new resource.
     * @throws ProtectionException 
     *
     */
    @ResponseBody
    @RequestMapping(value=CREATE_PATH, method=RequestMethod.POST, produces=JSON_MIME)
    public ResponseEntity<JdbcResourceBean> create(

        @RequestParam(value=RESOURCE_NAME_PARAM, required=false)
        final String name,
        @RequestParam(value=RESOURCE_STATUS_PARAM, required=false)
        final BaseComponent.Status status,

        @RequestParam(value=CONNECTION_TYPE_PARAM, required=false)
        final JdbcProductType type,
        
        @RequestParam(value=CONNECTION_HOST_PARAM, required=false)
        final String host,
        @RequestParam(value=CONNECTION_PORT_PARAM, required=false)
        final Integer port,

        @RequestParam(value=CONNECTION_DATABASE_PARAM, required=false)
        final String database,
        @RequestParam(value=CONNECTION_CATALOG_PARAM, required=false)
        final String catalog,

        @RequestParam(value=CONNECTION_USER_PARAM, required=false)
        final String user,
        @RequestParam(value=CONNECTION_PASS_PARAM, required=false)
        final String pass
            
        )
    throws ProtectionException
        {
        return created(
            factories().jdbc().resources().entities().create(
                name,
                type,
                database,
                catalog,
                host,
                port,
                user,
                pass
                )
            );
        }
    }
