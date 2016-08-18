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
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
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
@Slf4j
@Controller
@RequestMapping(JdbcTableLinkFactory.COLUMN_PATH)
public class JdbcTableColumnController
extends AbstractEntityController<JdbcColumn, JdbcColumnBean>
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

    /**
     * MVC property for the {@link JdbcColumn} name.
     * TODO Move this to a model class.
     *
     */
    public static final String COLUMN_NAME_PARAM = "jdbc.table.column.select.name" ;

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
     *
     */
    @ModelAttribute(JdbcTableController.TARGET_ENTITY)
    public JdbcTable parent(
        @PathVariable(WebappLinkFactory.IDENT_FIELD)
        final String ident
        ) throws EntityNotFoundException {
        log.debug("parent() [{}]", ident);
        return factories().jdbc().tables().entities().select(
            factories().jdbc().tables().idents().ident(
                ident
                )
            );
        }

    /**
     * JSON GET request to select all.
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.GET, produces=JSON_MIME)
    public Iterable<JdbcColumnBean> select(
        @ModelAttribute(JdbcTableController.TARGET_ENTITY)
        final JdbcTable table
        ){
        log.debug("select()");
        return bean(
            table.columns().select()
            );
        }

    /**
     * JSON request to select by name.
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, params=COLUMN_NAME_PARAM, produces=JSON_MIME)
    public JdbcColumnBean select(
        @ModelAttribute(JdbcTableController.TARGET_ENTITY)
        final JdbcTable table,
        @RequestParam(COLUMN_NAME_PARAM)
        final String name
        ) throws EntityNotFoundException {
        log.debug("select(String) [{}]", name);
        return bean(
            table.columns().select(
                name
                )
            );
        }
    }
