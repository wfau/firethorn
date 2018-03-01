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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.entity.annotation.UpdateAtomicMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierFormatException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameFormatException;
import uk.ac.roe.wfau.firethorn.meta.base.BaseComponent;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcProductType;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityController;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.widgeon.name.JdbcResourceLinkFactory;

/**
 * Spring MVC controller for <code>JdbcResource</code>.
 *
 */
@Controller
@RequestMapping(JdbcResourceLinkFactory.RESOURCE_PATH)
public class JdbcResourceController
extends AbstractEntityController<JdbcResource, JdbcResourceBean>
implements JdbcResourceModel
    {

    @Override
    public Path path()
        {
        return path(
            JdbcResourceLinkFactory.RESOURCE_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public JdbcResourceController()
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
     * Get the target resource based on the identifier in the request.
     * @throws ProtectionException 
     * @throws IdentifierFormatException 
     *
     */
    @ModelAttribute(TARGET_ENTITY)
    public JdbcResource entity(
        @PathVariable(WebappLinkFactory.IDENT_FIELD)
        final String ident
        )
    throws EntityNotFoundException, IdentifierFormatException, ProtectionException
        {
        final JdbcResource entity = factories().jdbc().resources().entities().select(
            factories().jdbc().resources().idents().ident(
                ident
                )
            );
        return entity ;
        }

    /**
     * JSON GET request.
     *
     */
    @ResponseBody
    @RequestMapping(method=RequestMethod.GET, produces=JSON_MIME)
    public ResponseEntity<JdbcResourceBean> select(
        @ModelAttribute(TARGET_ENTITY)
        final JdbcResource entity
        ){
        return selected(
            entity
            );
        }

    /**
     * JSON POST update.
     * @throws ProtectionException 
     * @throws NameFormatException 
     *
     */
    @ResponseBody
    @UpdateAtomicMethod
    @RequestMapping(method=RequestMethod.POST, produces=JSON_MIME)
    public ResponseEntity<JdbcResourceBean> update(
        @ModelAttribute(TARGET_ENTITY)
        final JdbcResource entity,

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
    throws NameFormatException, ProtectionException
        {
        if (name != null)
            {
            if (name.length() > 0)
                {
                entity.name(
                    name
                    );
                }
            }

        if (status != null)
            {
            entity.status(
                status
                );
            }
        
        if (type != null)
            {
            entity.connection().type(
                type
                );
            }
        
        if (host != null)
            {
            if (host.length() > 0)
                {
                entity.connection().host(
                    host
                    );
                }
            }

        if (port != null)
            {
            if (port != 0)
                {
                entity.connection().port(
                    port
                    );
                }
            else {
                entity.connection().port(
                    null
                    );
                }
            }

        if (database != null)
            {
            if (database.length() > 0)
                {
                entity.connection().database(
                    database 
                    );
                }
            }

        if (catalog != null)
            {
            if (catalog.length() > 0)
                {
                entity.connection().catalog(
                    catalog
                    );
                }
            else {
                entity.connection().catalog(
                    null
                    );
                }
            }
        
        if (user != null)
            {
            if (user.length() > 0)
                {
                entity.connection().user(
                    user
                    );
                }
            }

        if (pass != null)
            {
            if (pass.length() > 0)
                {
                entity.connection().pass(
                    pass
                    );
                }
            }

        return selected(
            entity
            );
        }
    }
