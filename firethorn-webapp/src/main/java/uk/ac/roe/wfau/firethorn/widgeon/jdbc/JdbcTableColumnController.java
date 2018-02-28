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
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierFormatException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameFormatException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcColumn;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTable;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityController;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.widgeon.name.JdbcTableLinkFactory;

/**
 * Spring MVC controller for <code>JdbcTable</code> columns.
 *
 */
@Controller
@RequestMapping(JdbcTableLinkFactory.COLUMN_PATH)
public class JdbcTableColumnController
extends AbstractEntityController<JdbcColumn, JdbcColumnBean>
implements JdbcTableModel, JdbcColumnModel 
    {
    @Override
    public Path path()
        {
        return path(
            JdbcTableLinkFactory.COLUMN_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public JdbcTableColumnController()
        {
        super();
        }

    @Override
    public JdbcColumnBean bean(final JdbcColumn entity)
        {
        return new JdbcColumnBean(
            entity
            );
        }

    @Override
    public Iterable<JdbcColumnBean> bean(final Iterable<JdbcColumn> iter)
        {
        return new JdbcColumnBean.Iter(
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
    @ModelAttribute(JdbcTableModel.TARGET_ENTITY)
    public JdbcTable parent(
        @PathVariable(WebappLinkFactory.IDENT_FIELD)
        final String ident
        )
    throws EntityNotFoundException, IdentifierFormatException, ProtectionException
        {
        return factories().jdbc().tables().entities().select(
            factories().jdbc().tables().idents().ident(
                ident
                )
            );
        }

    /**
     * GET request to select all the {@link JdbcColumn}s.
     * @throws ProtectionException 
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.GET, produces=JSON_MIME)
    public ResponseEntity<Iterable<JdbcColumnBean>> select(
        @ModelAttribute(JdbcTableModel.TARGET_ENTITY)
        final JdbcTable table
        )
    throws ProtectionException
        {
        return selected(
            table.columns().select()
            );
        }

    /**
     * POST request to select a {@link JdbcColumn} by {@link Identifier}.
     * @throws ProtectionException 
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.POST, params=COLUMN_IDENT_PARAM, produces=JSON_MIME)
    public ResponseEntity<JdbcColumnBean> select_by_ident(
        @ModelAttribute(JdbcTableModel.TARGET_ENTITY)
        final JdbcTable table,
        @RequestParam(COLUMN_IDENT_PARAM)
        final String ident
        )
    throws IdentifierNotFoundException, IdentifierFormatException, ProtectionException
        {
        return selected(
            table.columns().select(
                factories().jdbc().columns().idents().ident(
                    ident
                    )
                )
            );
        }
    
    /**
     * POST request to select a {@link JdbcColumn} by name.
     * @throws ProtectionException 
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.POST, params=COLUMN_NAME_PARAM, produces=JSON_MIME)
    public ResponseEntity<JdbcColumnBean> select_by_name(
        @ModelAttribute(JdbcTableModel.TARGET_ENTITY)
        final JdbcTable table,
        @RequestParam(COLUMN_NAME_PARAM)
        final String name
        )
    throws NameNotFoundException, NameFormatException, ProtectionException
        {
        return selected(
            table.columns().select(
                name
                )
            );
        }
    }
