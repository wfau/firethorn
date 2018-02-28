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
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierFormatException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityController;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.widgeon.name.AdqlTableLinkFactory;

/**
 * Spring MVC controller to handle the {@link AdqlColumn}s in an {@link AdqlTable}.
 * <br/>Controller path : [{@value AdqlTableLinkFactory#TABLE_COLUMN_PATH}]
 *
 */
@Controller
@RequestMapping(AdqlTableLinkFactory.TABLE_COLUMN_PATH)
public class AdqlTableColumnController
extends AbstractEntityController<AdqlColumn, AdqlColumnBean>
implements AdqlTableModel, AdqlColumnModel 
    {
    @Override
    public Path path()
        {
        return path(
            AdqlTableLinkFactory.TABLE_COLUMN_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public AdqlTableColumnController()
        {
        super();
        }

    @Override
    public AdqlColumnBean bean(final AdqlColumn entity)
        {
        return new AdqlColumnBean(
            entity
            );
        }

    @Override
    public Iterable<AdqlColumnBean> bean(final Iterable<AdqlColumn> iter)
        {
        return new AdqlColumnBean.Iter(
            iter
            );
        }

    /**
     * Get the parent {@link AdqlTable} based on the {@Identifier} in the request path.
     * @param ident The {@link AdqlTable} {@Identifier} from the URL path, [{@value WebappLinkFactory.IDENT_FIELD}].
     * @return The parent {@link AdqlTable}.
     * @throws IdentifierNotFoundException If the {@link AdqlTable} could not be found.
     * @throws ProtectionException 
     * @throws IdentifierFormatException 
     *
     */
    @ModelAttribute(AdqlTableModel.TARGET_ENTITY)
    public AdqlTable entity(
        @PathVariable(WebappLinkFactory.IDENT_FIELD)
        final String ident
        )
    throws IdentifierNotFoundException, IdentifierFormatException, ProtectionException
        {
        return factories().adql().tables().entities().select(
            factories().adql().tables().idents().ident(
                ident
                )
            );
        }

    /**
     * {@link RequestMethod#GET} request to select all the {@link AdqlColumn}s.
     * <br/>Request path : [{@value #SELECT_PATH}]
     * <br/>Content type : [{@value #JSON_MIME}]
     * @param table The parent {@link AdqlTable} selected using the {@Identifier} in the request path.
     * @return An {@Iterable} set of {@link AdqlColumnBean}.
     * @throws ProtectionException 
     * 
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.GET, produces=JSON_MIME)
    public ResponseEntity<Iterable<AdqlColumnBean>> select(
        @ModelAttribute(AdqlTableModel.TARGET_ENTITY)
        final AdqlTable table
        )
    throws ProtectionException
        {
        return selected(
            table.columns().select()
            );
        }

    /**
     * {@link RequestMethod#GET} request to select an{@link AdqlColumn} by {@link Identifier}.
     * <br/>Request path : [{@value #SELECT_PATH}]
     * <br/>Content type : [{@value #JSON_MIME}]
     * @param table The parent {@link AdqlTable} selected using the {@Identifier} in the request path.
     * @param ident The {@link AdqlColumn} name to look for, [{@value #COLUMN_NAME_PARAM}].
     * @return The matching {@link AdqlColumn} wrapped in an {@link AdqlColumnBean}.
     * @throws NameNotFoundException If a matching {@link AdqlColumn} could not be found.
     * @throws ProtectionException 
     * @throws IdentifierNotFoundException 
     * @throws IdentifierFormatException 
     * 
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.POST, params=COLUMN_NAME_PARAM, produces=JSON_MIME)
    public ResponseEntity<AdqlColumnBean> select_by_ident(
        @ModelAttribute(AdqlTableModel.TARGET_ENTITY)
        final AdqlTable table,
        @RequestParam(COLUMN_IDENT_PARAM)
        final String ident
        )
    throws ProtectionException, IdentifierFormatException, IdentifierNotFoundException
        {
        return selected(
            table.columns().select(
                factories().adql().columns().idents().ident(
                    ident
                    )
                )
            );
        }

    /**
     * {@link RequestMethod#GET} request to select an {@link AdqlColumn} by name.
     * <br/>Request path : [{@value #SELECT_PATH}]
     * <br/>Content type : [{@value #JSON_MIME}]
     * @param table The parent {@link AdqlTable} selected using the {@Identifier} in the request path.
     * @param name  The {@link AdqlColumn} name to look for, [{@value #COLUMN_NAME_PARAM}].
     * @return The matching {@link AdqlColumn} wrapped in an {@link AdqlColumnBean}.
     * @throws NameNotFoundException If a matching {@link AdqlColumn} could not be found.
     * @throws ProtectionException 
     * 
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.POST, params=COLUMN_NAME_PARAM, produces=JSON_MIME)
    public ResponseEntity<AdqlColumnBean> select_by_name(
        @ModelAttribute(AdqlTableModel.TARGET_ENTITY)
        final AdqlTable table,
        @RequestParam(COLUMN_NAME_PARAM)
        final String name
        )
    throws NameNotFoundException, ProtectionException
        {
        return selected(
            table.columns().select(
                name
                )
            );
        }
    }
