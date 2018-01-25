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
package uk.ac.roe.wfau.firethorn.widgeon.ivoa;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierFormatException;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaResource;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaSchema;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityController;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.widgeon.name.IvoaResourceLinkFactory;

/**
 * Spring MVC controller for <code>IvoaResource</code> schema.
 *
 */
@Slf4j
@Controller
@RequestMapping(IvoaResourceLinkFactory.RESOURCE_SCHEMA_PATH)
public class IvoaResourceSchemaController
extends AbstractEntityController<IvoaSchema, IvoaSchemaBean>
    {
    @Override
    public Path path()
        {
        return path(
            IvoaResourceLinkFactory.RESOURCE_SCHEMA_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public IvoaResourceSchemaController()
        {
        super();
        }

    /**
     * MVC property for the schema name.
     *
     */
    public static final String SCHEMA_NAME_PARAM = "ivoa.schema.name" ;

    @Override
    public IvoaSchemaBean bean(final IvoaSchema entity)
        {
        return new IvoaSchemaBean(
            entity
            );
        }

    @Override
    public Iterable<IvoaSchemaBean> bean(final Iterable<IvoaSchema> iter)
        {
        return new IvoaSchemaBean.Iter(
            iter
            );
        }

    /**
     * Get the parent resource based on the identifier in the request.
     * @throws EntityNotFoundException
     * @throws ProtectionException 
     * @throws IdentifierFormatException 
     *
     */
    @ModelAttribute(IvoaResourceController.TARGET_ENTITY)
    public IvoaResource parent(
        @PathVariable(WebappLinkFactory.IDENT_FIELD)
        final String ident
        )
    throws EntityNotFoundException, IdentifierFormatException, ProtectionException
        {
        return factories().ivoa().resources().entities().select(
            factories().ivoa().resources().idents().ident(
                ident
                )
            );
        }

    /**
     * JSON GET request to select all.
     * @throws ProtectionException 
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.GET, produces=JSON_MIME)
    public Iterable<IvoaSchemaBean> select(
        @ModelAttribute(IvoaResourceController.TARGET_ENTITY)
        final IvoaResource resource
        )
    throws ProtectionException
        {
        log.debug("select()");
        return bean(
            resource.schemas().select()
            );
        }

    /**
     * JSON request to select by name.
     * @throws ProtectionException 
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, params=SCHEMA_NAME_PARAM, produces=JSON_MIME)
    public IvoaSchemaBean select(
        @ModelAttribute(IvoaResourceController.TARGET_ENTITY)
        final IvoaResource resource,
        @RequestParam(SCHEMA_NAME_PARAM)
        final String name
        )
    throws EntityNotFoundException, ProtectionException
        {
        return bean(
            resource.schemas().select(
                name
                )
            );
        }
    }
