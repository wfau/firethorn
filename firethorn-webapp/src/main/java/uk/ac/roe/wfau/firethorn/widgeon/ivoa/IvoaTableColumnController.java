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

import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierFormatException;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaColumn;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaTable;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityController;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.widgeon.name.IvoaTableLinkFactory;

/**
 * Spring MVC controller for <code>IvoaTable</code> columns.
 *
 */
@Controller
@RequestMapping(IvoaTableLinkFactory.TABLE_COLUMN_PATH)
public class IvoaTableColumnController
extends AbstractEntityController<IvoaColumn, IvoaColumnBean>
    {
    @Override
    public Path path()
        {
        return path(
            IvoaTableLinkFactory.TABLE_COLUMN_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public IvoaTableColumnController()
        {
        super();
        }

    /**
     * MVC property for the Resource name.
     *
     */
    public static final String COLUMN_NAME_PARAM = "urn:ivoa.column.name" ;

    @Override
    public IvoaColumnBean bean(final IvoaColumn entity)
        {
        return new IvoaColumnBean(
            entity
            );
        }

    @Override
    public Iterable<IvoaColumnBean> bean(final Iterable<IvoaColumn> iter)
        {
        return new IvoaColumnBean.Iter(
            iter
            );
        }

    /**
     * Get the parent table based on the identifier in the request.
     * @throws EntityNotFoundException
     * @throws ProtectionException 
     * @throws IdentifierFormatException 
     *
     */
    @ModelAttribute(IvoaTableController.TARGET_ENTITY)
    public IvoaTable parent(
        @PathVariable(WebappLinkFactory.IDENT_FIELD)
        final String ident
        )
    throws EntityNotFoundException, IdentifierFormatException, ProtectionException
        {
        return factories().ivoa().tables().entities().select(
            factories().ivoa().tables().idents().ident(
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
    public Iterable<IvoaColumnBean> select(
        @ModelAttribute(IvoaTableController.TARGET_ENTITY)
        final IvoaTable table
        )
    throws ProtectionException
        {
        return bean(
            table.columns().select()
            );
        }

    /**
     * JSON request to select by name.
     * @throws ProtectionException 
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, params=COLUMN_NAME_PARAM, produces=JSON_MIME)
    public IvoaColumnBean select(
        @ModelAttribute(IvoaTableController.TARGET_ENTITY)
        final IvoaTable table,
        @RequestParam(COLUMN_NAME_PARAM)
        final String name
        )
    throws EntityNotFoundException, ProtectionException
        {
        return bean(
            table.columns().select(
                name
                )
            );
        }
    }
