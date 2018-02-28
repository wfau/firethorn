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
import uk.ac.roe.wfau.firethorn.entity.exception.NameFormatException;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaTable;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityController;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.widgeon.name.IvoaTableLinkFactory;

/**
 * Spring MVC controller for <code>IvoaTables</code>.
 *
 */
@Controller
@RequestMapping(IvoaTableLinkFactory.TABLE_PATH)
public class IvoaTableController
extends AbstractEntityController<IvoaTable, IvoaTableBean>
implements IvoaTableModel, IvoaColumnModel 
    {

    @Override
    public Path path()
        {
        return path(
            IvoaTableLinkFactory.TABLE_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public IvoaTableController()
        {
        super();
        }

    @Override
    public Iterable<IvoaTableBean> bean(final Iterable<IvoaTable> iter)
        {
        return new IvoaTableBean.Iter(
            iter
            );
        }

    @Override
    public IvoaTableBean bean(final IvoaTable entity)
        {
        return new IvoaTableBean(
            entity
            );
        }

    /**
     * Get the target table based on the identifier in the request.
     * @throws EntityNotFoundException
     * @throws ProtectionException 
     * @throws IdentifierFormatException 
     *
     */
    @ModelAttribute(IvoaTableModel.TARGET_ENTITY)
    public IvoaTable entity(
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
     * GET request to select all the {@link IvoaTable}s.
     *
     */
    @ResponseBody
    @RequestMapping(method=RequestMethod.GET, produces=JSON_MIME)
    public ResponseEntity<IvoaTableBean> select(
        @ModelAttribute(IvoaTableModel.TARGET_ENTITY)
        final IvoaTable entity
        ){
        return selected(
            entity
            );
        }

    /**
     * POST update name request.
     * @throws ProtectionException 
     * @throws NameFormatException 
     *
     */
    @ResponseBody
    @RequestMapping(method=RequestMethod.POST, params={TABLE_NAME_PARAM}, produces=JSON_MIME)
    public ResponseEntity<IvoaTableBean> update(
        @ModelAttribute(IvoaTableModel.TARGET_ENTITY)
        final IvoaTable entity,
        @RequestParam(value=TABLE_NAME_PARAM, required=true)
        final String name
        )
    throws NameFormatException, ProtectionException
        {
        //
        // Needs a transaction ..
        if (null != name)
            {
            entity.name(
                name
                );
            }
        return selected(
            entity
            );
        }
    }
