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
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;
import uk.ac.roe.wfau.firethorn.meta.base.TreeComponent;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityController;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.widgeon.name.AdqlSchemaLinkFactory;

/**
 * Spring MVC controller to handle the {@link AdqlTable}s in an {@link AdqlSchema}.
 * <br/>Controller path : [{@value AdqlSchemaLinkFactory#SCHEMA_TABLE_PATH}]
 *
 */
@Controller
@RequestMapping(AdqlSchemaLinkFactory.SCHEMA_TABLE_PATH)
public class AdqlSchemaTableController
extends AbstractEntityController<AdqlTable, AdqlTableBean>
implements AdqlSchemaModel, AdqlTableModel
    {
    @Override
    public Path path()
        {
        return path(
            AdqlSchemaLinkFactory.SCHEMA_TABLE_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public AdqlSchemaTableController()
        {
        super();
        }

    @Override
    public AdqlTableBean bean(final AdqlTable entity)
        {
        return new AdqlTableBean(
            entity
            );
        }

    @Override
    public Iterable<AdqlTableBean> bean(final Iterable<AdqlTable> iter)
        {
        return new AdqlTableBean.Iter(
            iter
            );
        }

    /**
     * Get the parent {@link AdqlSchema} based on the {@Identifier} in the request path.
     * @param ident The {@link AdqlSchema} {@Identifier} from the URL path, [{@value WebappLinkFactory.IDENT_FIELD}].
     * @return The parent {@link AdqlSchema}.
     * @throws IdentifierNotFoundException If the {@link AdqlSchema} could not be found.
     * @throws ProtectionException 
     * @throws IdentifierFormatException 
     *
     */
    @ModelAttribute(AdqlSchemaModel.TARGET_ENTITY)
    public AdqlSchema entity(
        @PathVariable(WebappLinkFactory.IDENT_FIELD)
        final String ident
        )
    throws IdentifierNotFoundException, IdentifierFormatException, ProtectionException
        {
        return factories().adql().schemas().entities().select(
            factories().adql().schemas().idents().ident(
                ident
                )
            );
        }

    /**
     * {@link RequestMethod#POST} request to select all the {@link AdqlTable}s in this {@link AdqlSchema}.
     * @param schema The parent {@link AdqlSchema} selected using the {@Identifier} in the request path.
     * @return An {@Iterable} set of {@link AdqlTableBean}.
     * @throws ProtectionException 
     * 
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.GET, produces=JSON_MIME)
    public ResponseEntity<Iterable<AdqlTableBean>> select(
        @ModelAttribute(AdqlSchemaModel.TARGET_ENTITY)
        final AdqlSchema schema
        )
    throws ProtectionException
        {
        return selected(
            schema.tables().select()
            );
        }

    /**
     * {@link RequestMethod#POST} request to select an {@link AdqlTable} by {@link Identifier}.
     * @throws ProtectionException 
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.POST, params=TABLE_IDENT_PARAM, produces=JSON_MIME)
    public ResponseEntity<AdqlTableBean> select_by_ident(
        @ModelAttribute(AdqlSchemaModel.TARGET_ENTITY)
        final AdqlSchema schema,
        @RequestParam(TABLE_IDENT_PARAM)
        final String ident
        )
    throws IdentifierNotFoundException, ProtectionException
        {
        return selected(
            schema.tables().select(
                factories().adql().tables().idents().ident(
                    ident
                    )
                )
            );
        }

    /**
     * {@link RequestMethod#POST} request to select an {@link AdqlTable} by name.
     * @param schema The parent {@link AdqlSchema} selected using the {@Identifier} in the request path.
     * @param name The {@link AdqlTable} name to look for, [{@value #TABLE_NAME_PARAM}].
     * @return The matching {@link AdqlTable} wrapped in an {@link AdqlTableBean}.
     * @throws NameNotFoundException If a matching {@link AdqlTable} could not be found.
     * @throws ProtectionException 
     * 
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.POST, params=TABLE_NAME_PARAM, produces=JSON_MIME)
    public ResponseEntity<AdqlTableBean> select(
        @ModelAttribute(AdqlSchemaModel.TARGET_ENTITY)
        final AdqlSchema schema,
        @RequestParam(TABLE_NAME_PARAM)
        final String name
        )
    throws NameNotFoundException, ProtectionException
        {
        return selected(
            schema.tables().select(
                name
                )
            );
        }

    
    /**
     * {@link RequestMethod#POST} request to create a new {@link AdqlTable} copying the columns from a {@link BaseTable}.
     * <br/>Request path : [{@value #IMPORT_PATH}]
     * <br/>Content type : [{@value #JSON_MIME}]
     * @param schema The parent {@link AdqlSchema} selected using the {@Identifier} in the request path.
     * @param base   The The {@Identifier} of the {@link BaseTable} to copy, [{@value #IMPORT_TABLE_BASE}].
     * @param depth  The {@link TreeComponent.CopyDepth} of the new {@link AdqlTable}, [{@value #COPY_DEPTH_PARAM}].
     * @return The new {@link AdqlTable} wrapped in an {@link AdqlTableBean}.
     * @throws EntityNotFoundException 
     * @throws IdentifierFormatException 
     * @throws ProtectionException 
     * @todo Rejects duplicate names.
     * 
     */
    @ResponseBody
    @RequestMapping(value=IMPORT_PATH, method=RequestMethod.POST, params={BASE_TABLE_PARAM}, produces=JSON_MIME)
    public ResponseEntity<AdqlTableBean > inport(
        @ModelAttribute(AdqlSchemaModel.TARGET_ENTITY)
        final AdqlSchema schema,
        @RequestParam(value=COPY_DEPTH_PARAM, required=false)
        final TreeComponent.CopyDepth type,
        @RequestParam(value=BASE_TABLE_PARAM, required=true)
        final String base
        )
    throws IdentifierFormatException, EntityNotFoundException, ProtectionException
        {
        return created(
            schema.tables().create(
                ((type != null) ? type : TreeComponent.CopyDepth.FULL),
                factories().base().tables().resolve(
                    base
                    )
                )
            );
        }

    /**
     * {@link RequestMethod#POST} request to create a new {@link AdqlTable} copying the columns from a {@link BaseTable}.
     * <br/>Request path : [{@value #IMPORT_PATH}]
     * <br/>Content type : [{@value #JSON_MIME}]
     * @param schema The parent {@link AdqlSchema} selected using the {@Identifier} in the request path.
     * @param base   The The {@Identifier} of the {@link BaseTable} to copy, [{@value #IMPORT_TABLE_BASE}].
     * @param depth  The {@link TreeComponent.CopyDepth} of the new {@link AdqlTable}, [{@value #COPY_DEPTH_PARAM}].
     * @param name   The name of the new {@link AdqlTable}, [{@value #IMPORT_TABLE_NAME}].
     * @return The new {@link AdqlTable} wrapped in an {@link AdqlTableBean}.
     * @throws EntityNotFoundException 
     * @throws IdentifierFormatException 
     * @throws ProtectionException 
     * @todo Rejects duplicate names.
     * @todo Make name optional, default to the base name.
     * 
     */
    @ResponseBody
    @RequestMapping(value=IMPORT_PATH, method=RequestMethod.POST, params={BASE_TABLE_PARAM, TABLE_NAME_PARAM}, produces=JSON_MIME)
    public ResponseEntity<AdqlTableBean> inport(
        @ModelAttribute(AdqlSchemaModel.TARGET_ENTITY)
        final AdqlSchema schema,
        @RequestParam(value=COPY_DEPTH_PARAM, required=false)
        final TreeComponent.CopyDepth type,
        @RequestParam(value=BASE_TABLE_PARAM, required=true)
        final String base,
        @RequestParam(value=TABLE_NAME_PARAM, required=true)
        final String name
        )
    throws IdentifierFormatException, EntityNotFoundException, ProtectionException
        {
        return created(
            schema.tables().create(
                type,
                factories().base().tables().resolve(
                    base
                    ),
                name
                )
            );
        }
    }
