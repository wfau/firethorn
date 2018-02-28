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
package uk.ac.roe.wfau.firethorn.widgeon.adql;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierFormatException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.meta.base.BaseSchema;
import uk.ac.roe.wfau.firethorn.meta.base.TreeComponent;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityController;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.widgeon.name.AdqlResourceLinkFactory;

/**
 * Spring MVC controller to handle the {@link AdqlSchema} in an {@link AdqlResource}.
 * <br/>Controller path : [{@value AdqlResourceLinkFactory#RESOURCE_SCHEMAS_PATH}]
 *
 */
@Controller
@RequestMapping(AdqlResourceLinkFactory.RESOURCE_SCHEMAS_PATH)
public class AdqlResourceSchemaController
extends AbstractEntityController<AdqlSchema, AdqlSchemaBean>
implements AdqlResourceModel, AdqlSchemaModel
    {
    @Override
    public Path path()
        {
        return path(
            AdqlResourceLinkFactory.RESOURCE_SCHEMAS_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public AdqlResourceSchemaController()
        {
        super();
        }

    @Override
    public AdqlSchemaBean bean(final AdqlSchema entity)
        {
        return new AdqlSchemaBean(
            entity
            );
        }

    @Override
    public Iterable<AdqlSchemaBean > bean(final Iterable<AdqlSchema> iter)
        {
        return new AdqlSchemaBean.Iter(
            iter
            );
        }

    /**
     * Get the parent {@link AdqlResource} based on the {@Identifier} in the request path.
     * @param ident The {@link AdqlResource} {@Identifier} from the URL path, [{@value WebappLinkFactory.IDENT_FIELD}].
     * @return The parent {@link AdqlResource}.
     * @throws IdentifierNotFoundException If the {@link AdqlResource} could not be found.
     * @throws ProtectionException 
     * @throws IdentifierFormatException 
     *
     */
    @ModelAttribute(AdqlResourceController.TARGET_ENTITY)
    public AdqlResource entity(
        @PathVariable(WebappLinkFactory.IDENT_FIELD)
        final String ident
        )
    throws IdentifierNotFoundException, IdentifierFormatException, ProtectionException
        {
        return factories().adql().resources().entities().select(
            factories().adql().resources().idents().ident(
                ident
                )
            );
        }

    /**
     * GET request to select all the {@link AdqlSchema}.
     * @param resource The parent {@link AdqlResource} selected using the {@Identifier} in the request path.
     * @return An {@Iterable} set of {@link AdqlSchemaBean}.
     * @throws ProtectionException 
     * 
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.GET, produces=JSON_MIME)
    public ResponseEntity<Iterable<AdqlSchemaBean>> select(
        @ModelAttribute(AdqlResourceController.TARGET_ENTITY)
        final AdqlResource resource
        )
    throws ProtectionException
        {
        return selected(
            resource.schemas().select()
            );
        }

    /**
     * POST request to select an {@link AdqlSchema} by {@Identifier}.
     * @param resource The parent {@link AdqlResource} selected using the {@Identifier} in the request path.
     * @param ident The {@link AdqlSchema} name to look for, [{@value #SCHEMA_SELECT_NAME_PARAM}].
     * @return The matching {@link AdqlSchema} wrapped in an {@link AdqlSchemaBean}.
     * @throws NameNotFoundException If a matching {@link AdqlSchema} could not be found.
     * @throws ProtectionException 
     * @throws IdentifierNotFoundException 
     * @throws IdentifierFormatException 
     * 
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.POST, params=SCHEMA_IDENT_PARAM, produces=JSON_MIME)
    public ResponseEntity<AdqlSchemaBean> select_by_ident(
        @ModelAttribute(AdqlResourceController.TARGET_ENTITY)
        final AdqlResource resource,
        @RequestParam(SCHEMA_IDENT_PARAM)
        final String ident
        )
    throws NameNotFoundException, ProtectionException, IdentifierFormatException, IdentifierNotFoundException
        {
        return selected(
            resource.schemas().select(
                factories().adql().schemas().idents().ident(
                    ident
                    )
                )
            );
        }

    /**
     * POST request to select an {@link AdqlSchema} by name.
     * @param resource The parent {@link AdqlResource} selected using the {@Identifier} in the request path.
     * @param name The {@link AdqlSchema} name to look for, [{@value #SCHEMA_SELECT_NAME_PARAM}].
     * @return The matching {@link AdqlSchema} wrapped in an {@link AdqlSchemaBean}.
     * @throws NameNotFoundException If a matching {@link AdqlSchema} could not be found.
     * @throws ProtectionException 
     * 
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.POST, params=SCHEMA_NAME_PARAM, produces=JSON_MIME)
    public ResponseEntity<AdqlSchemaBean> select_by_name(
        @ModelAttribute(AdqlResourceController.TARGET_ENTITY)
        final AdqlResource resource,
        @RequestParam(SCHEMA_NAME_PARAM)
        final String name
        )
    throws NameNotFoundException, ProtectionException
        {
        return selected(
            resource.schemas().select(
                name
                )
            );
        }

    /**
     * {@link RequestMethod#POST} request to create a new {@link AdqlSchema}.
     * @param resource The parent {@link AdqlResource} selected using the {@Identifier} in the request path.
     * @param name The {@link AdqlSchema} name, [{@value #SCHEMA_SELECT_NAME_PARAM}].
     * @return The new {@link AdqlSchema} wrapped in an {@link AdqlSchemaBean}.
     * @throws ProtectionException 
     * @todo Rejects duplicate names.
     * 
     */
    @ResponseBody
    @RequestMapping(value=CREATE_PATH, params={SCHEMA_NAME_PARAM}, method=RequestMethod.POST, produces=JSON_MIME)
    public ResponseEntity<AdqlSchemaBean> create(
        @ModelAttribute(AdqlResourceController.TARGET_ENTITY)
        final AdqlResource resource,
        @RequestParam(value=SCHEMA_NAME_PARAM, required=true)
        final String name
        )
    throws ProtectionException
        {
        return created(
            resource.schemas().create(
                name
                )
            );
        }

    /**
     * {@link RequestMethod#POST} request to create a new {@link AdqlSchema} copying the name and tables from a {@link BaseSchema}.
     * @param resource The parent {@link AdqlResource} selected using the {@Identifier} in the request path.
     * @param base     The {@Identifier} of the {@link BaseSchema} to copy, [{@value #SCHEMA_IMPORT_BASE_PARAM}].
     * @param depth    The {@link TreeComponent.CopyDepth} of the new {@link AdqlSchema}, [{@value #COPY_DEPTH_PARAM}].
     * @return The new {@link AdqlSchema} wrapped in an {@link AdqlSchemaBean}.
     * @throws EntityNotFoundException 
     * @throws IdentifierFormatException 
     * @throws ProtectionException 
     * @todo Rejects duplicate names.
     * @todo Merge with next method.
     * 
     */
    @ResponseBody
    @RequestMapping(value=IMPORT_PATH, params={SCHEMA_IMPORT_BASE_PARAM}, method=RequestMethod.POST, produces=JSON_MIME)
    public ResponseEntity<AdqlSchemaBean> inport(
        @ModelAttribute(AdqlResourceController.TARGET_ENTITY)
        final AdqlResource resource,
        @RequestParam(value=COPY_DEPTH_PARAM, required=false)
        final TreeComponent.CopyDepth depth,
        @RequestParam(value=SCHEMA_IMPORT_BASE_PARAM, required=true)
        final String base
        )
    throws IdentifierFormatException, EntityNotFoundException, ProtectionException
        {
        return created(
            resource.schemas().create(
                ((depth != null) ? depth : TreeComponent.CopyDepth.FULL),
                factories().base().schema().resolve(
                    base
                    )
                )
            );
        }

    /**
     * {@link RequestMethod#POST} request to create a new {@link AdqlSchema} copying the tables from a {@link BaseSchema}.
     * <br/>Request path : [{@value #IMPORT_PATH}]
     * <br/>Content type : [{@value #JSON_MIME}]
     * @param resource The parent {@link AdqlResource} selected using the {@Identifier} in the request path.
     * @param base     The The {@Identifier} of the {@link BaseSchema} to copy, [{@value #SCHEMA_IMPORT_BASE_PARAM}].
     * @param depth    The {@link TreeComponent.CopyDepth} of the new {@link AdqlSchema}, [{@value #COPY_DEPTH_PARAM}].
     * @param name     The name of the new {@link AdqlSchema}, [{@value #IMPORT_SCHEMA_NAME}].
     * @return The new {@link AdqlSchema} wrapped in an {@link AdqlSchemaBean}.
     * @throws EntityNotFoundException 
     * @throws IdentifierFormatException 
     * @throws ProtectionException 
     * @todo Rejects duplicate names.
     * @todo Make name optional, default to the base name.
     * 
     */
    @ResponseBody
    @RequestMapping(value=IMPORT_PATH, params={SCHEMA_IMPORT_BASE_PARAM, SCHEMA_NAME_PARAM}, method=RequestMethod.POST, produces=JSON_MIME)
    public ResponseEntity<AdqlSchemaBean> inport(
        @ModelAttribute(AdqlResourceController.TARGET_ENTITY)
        final AdqlResource resource,
        @RequestParam(value=COPY_DEPTH_PARAM, required=false)
        final TreeComponent.CopyDepth depth,
        @RequestParam(value=SCHEMA_IMPORT_BASE_PARAM, required=true)
        final String base,
        @RequestParam(value=SCHEMA_NAME_PARAM, required=true)
        final String name
        )
    throws IdentifierFormatException, EntityNotFoundException, ProtectionException
        {
        return created(
            resource.schemas().create(
                ((depth != null) ? depth : TreeComponent.CopyDepth.THIN),
                name,
                factories().base().schema().resolve(
                    base
                    )
                )
            );
        }
    }
