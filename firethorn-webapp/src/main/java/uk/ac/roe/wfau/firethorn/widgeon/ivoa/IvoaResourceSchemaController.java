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

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
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
@Controller
@RequestMapping(IvoaResourceLinkFactory.RESOURCE_SCHEMA_PATH)
public class IvoaResourceSchemaController
extends AbstractEntityController<IvoaSchema, IvoaSchemaBean>
implements IvoaResourceModel, IvoaSchemaModel
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
    @ModelAttribute(IvoaResourceModel.TARGET_ENTITY)
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
     * GET request to select all the {@link IvoaSchema}.
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.GET, produces=JSON_MIME)
    public ResponseEntity<Iterable<IvoaSchemaBean>> select(
        @ModelAttribute(IvoaResourceModel.TARGET_ENTITY)
        final IvoaResource resource
        )
    throws ProtectionException
        {
        return selected(
            resource.schemas().select()
            );
        }

    /**
     * POST request to select an {@link IvoaSchema} by {@link Identifier}.
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.POST, params=SCHEMA_IDENT_PARAM, produces=JSON_MIME)
    public ResponseEntity<IvoaSchemaBean> ident(
        @ModelAttribute(IvoaResourceModel.TARGET_ENTITY)
        final IvoaResource resource,
        @RequestParam(SCHEMA_IDENT_PARAM)
        final String ident
        )
    throws EntityNotFoundException, ProtectionException
        {
        return selected(
            resource.schemas().select(
                factories().ivoa().schemas().idents().ident(
                    ident
                    )
                )
            );
        }
    
    /**
     * POST request to select an {@link IvoaSchema} by name.
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.POST, params=SCHEMA_NAME_PARAM, produces=JSON_MIME)
    public ResponseEntity<IvoaSchemaBean> select_by_name(
        @ModelAttribute(IvoaResourceModel.TARGET_ENTITY)
        final IvoaResource resource,
        @RequestParam(SCHEMA_NAME_PARAM)
        final String name
        )
    throws EntityNotFoundException, ProtectionException
        {
        return selected(
            resource.schemas().select(
                name
                )
            );
        }
    }
