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

import uk.ac.roe.wfau.firethorn.entity.annotation.UpdateAtomicMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.meta.base.BaseComponent;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcConnection;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityController;
import uk.ac.roe.wfau.firethorn.webapp.control.EntityBean;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;

/**
 * Spring MVC controller for <code>JdbcResource</code>.
 *
 */
@Slf4j
@Controller
@RequestMapping(JdbcResourceLinkFactory.RESOURCE_PATH)
public class JdbcResourceController
    extends AbstractEntityController<JdbcResource, JdbcResourceBean>
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

    /**
     * MVC property for the target resource.
     *
     */
    public static final String TARGET_ENTITY = "urn:jdbc.resource.entity" ;

    /**
     * MVC property for updating the name.
     *
     */
    public static final String UPDATE_NAME = "jdbc.resource.update.name" ;

    /**
     * MVC property for updating the OGSA-DAI resource.
     *
     */
    public static final String UPDATE_OGSADAI = "jdbc.resource.update.ogsadai" ;

    /**
     * MVC property for updating the status.
     *
     */
    public static final String UPDATE_STATUS = "jdbc.resource.update.status" ;

    /**
     * MVC property for updating the connection URL.
     *
     */
    public static final String UPDATE_CONN_URL = "jdbc.resource.connection.url" ;

    /**
     * MVC property for updating the connection user name.
     *
     */
    public static final String UPDATE_CONN_USER = "jdbc.resource.connection.user" ;

    /**
     * MVC property for updating the connection password.
     *
     */
    public static final String UPDATE_CONN_PASS = "jdbc.resource.connection.pass" ;

    /**
     * MVC property for updating the connection status.
     *
     */
    public static final String UPDATE_CONN_STATUS = "jdbc.resource.connection.status" ;

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
     *
     */
    @ModelAttribute(TARGET_ENTITY)
    public JdbcResource entity(
        @PathVariable(WebappLinkFactory.IDENT_FIELD)
        final String ident
        ) throws NotFoundException  {
        log.debug("entity() [{}]", ident);
        final JdbcResource entity = factories().jdbc().resources().select(
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
    @RequestMapping(method=RequestMethod.GET, produces=JSON_CONTENT)
    public JdbcResourceBean select(
        @ModelAttribute(TARGET_ENTITY)
        final JdbcResource entity
        ){
        return bean(
            entity
            );
        }

    /**
     * JSON POST update.
     *
     */
    @ResponseBody
    @UpdateAtomicMethod
    @RequestMapping(method=RequestMethod.POST, produces=JSON_CONTENT)
    public EntityBean<JdbcResource> update(
        @ModelAttribute(TARGET_ENTITY)
        final JdbcResource entity,
        @RequestParam(value=UPDATE_NAME, required=false) final
        String name,
        @RequestParam(value=UPDATE_STATUS, required=false) final
        String status,
        @RequestParam(value=UPDATE_OGSADAI, required=false) final
        String ogsadai,
        @RequestParam(value=UPDATE_CONN_URL, required=false) final
        String url,
        @RequestParam(value=UPDATE_CONN_USER, required=false) final
        String user,
        @RequestParam(value=UPDATE_CONN_PASS, required=false) final
        String pass,
        @RequestParam(value=UPDATE_CONN_STATUS, required=false) final
        String action
        ){

        if (name != null)
            {
            if (name.length() > 0)
                {
                entity.name(
                    name
                    );
                }
            }

        if (ogsadai != null)
            {
            if (ogsadai.length() > 0)
                {
                entity.ogsaid(
                    ogsadai
                    );
                }
            }

        if (url != null)
            {
            if (url.length() > 0)
                {
                entity.connection().url(
                    url
                    );
                }
            else {
                entity.connection().url(
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
            else {
                entity.connection().user(
                    null
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
            else {
                entity.connection().pass(
                    null
                    );
                }
            }

        if (status != null)
            {
            entity.status(
                BaseComponent.Status.valueOf(
                    status
                    )
                );
            }

        if (action != null)
            {
            entity.connection().status(
                JdbcConnection.Status.valueOf(
                    action
                    )
                );
            }

        return new JdbcResourceBean(
            entity
            );
        }
    }
