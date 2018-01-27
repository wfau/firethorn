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
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierFormatException;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaSchema;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaTable;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityController;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.widgeon.name.IvoaSchemaLinkFactory;

/**
 * Spring MVC controller for <code>IvoaSchema</code> tables.
 *
 */
@Controller
@RequestMapping(IvoaSchemaLinkFactory.SCHEMA_TABLE_PATH)
public class IvoaSchemaTableController
extends AbstractEntityController<IvoaTable, IvoaTableBean>
    {
    @Override
    public Path path()
        {
        return path(
            IvoaSchemaLinkFactory.SCHEMA_TABLE_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public IvoaSchemaTableController()
        {
        super();
        }

    /**
     * MVC property for the {@link IvoaTable} name.
     *
     */
    public static final String TABLE_NAME_PARAM = "urn:ivoa.table.name" ;

    @Override
    public IvoaTableBean bean(final IvoaTable entity)
        {
        return new IvoaTableBean(
            entity
            );
        }

    @Override
    public Iterable<IvoaTableBean> bean(final Iterable<IvoaTable> iter)
        {
        return new IvoaTableBean.Iter(
            iter
            );
        }

    /**
     * Get the parent schema based on the identifier in the request.
     * @throws ProtectionException 
     * @throws IdentifierFormatException 
     *
     */
    @ModelAttribute(IvoaSchemaController.TARGET_ENTITY)
    public IvoaSchema parent(
        @PathVariable(WebappLinkFactory.IDENT_FIELD)
        final String ident
        )
    throws EntityNotFoundException, IdentifierFormatException, ProtectionException
        {
        return factories().ivoa().schemas().entities().select(
            factories().ivoa().schemas().idents().ident(
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
    public ResponseEntity<Iterable<IvoaTableBean>> select(
        @ModelAttribute(IvoaSchemaController.TARGET_ENTITY)
        final IvoaSchema schema
        )
    throws ProtectionException
        {
        return selected(
            schema.tables().select()
            );
        }

    /**
     * JSON request to select by name.
     * @throws ProtectionException 
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, params=TABLE_NAME_PARAM, produces=JSON_MIME)
    public ResponseEntity<IvoaTableBean> select(
        @ModelAttribute(IvoaSchemaController.TARGET_ENTITY)
        final IvoaSchema schema,
        @RequestParam(TABLE_NAME_PARAM)
        final String name
        )
    throws EntityNotFoundException, ProtectionException
        {
        return selected(
            schema.tables().select(
                name
                )
            );
        }
    }
