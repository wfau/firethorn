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

import uk.ac.roe.wfau.firethorn.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTable;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityController;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;

/**
 * Spring MVC controller for <code>JdbcTables</code>.
 *
 */
@Slf4j
@Controller
@RequestMapping(JdbcTableLinkFactory.TABLE_PATH)
public class JdbcTableController
    extends AbstractEntityController<JdbcTable, JdbcTableBean>
    {

    @Override
    public Path path()
        {
        return path(
            JdbcTableLinkFactory.TABLE_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public JdbcTableController()
        {
        super();
        }

    /**
     * MVC property for the target entity.
     *
     */
    public static final String TARGET_ENTITY = "urn:jdbc.table.entity" ;

    /**
     * MVC property for updating the name.
     *
     */
    public static final String UPDATE_NAME = "jdbc.table.update.name" ;


    @Override
    public Iterable<JdbcTableBean> bean(final Iterable<JdbcTable> iter)
        {
        return new JdbcTableBean.Iter(
            iter
            );
        }

    @Override
    public JdbcTableBean bean(final JdbcTable entity)
        {
        return new JdbcTableBean(
            entity
            );
        }

    /**
     * Get the target table based on the identifier in the request.
     * @throws NotFoundException
     *
     */
    @ModelAttribute(JdbcTableController.TARGET_ENTITY)
    public JdbcTable entity(
        @PathVariable(WebappLinkFactory.IDENT_FIELD)
        final String ident
        ) throws NotFoundException {
        log.debug("table() [{}]", ident);
        return factories().jdbc().tables().select(
            factories().jdbc().tables().idents().ident(
                ident
                )
            );
        }

    /**
     * JSON GET request.
     *
     */
    @ResponseBody
    @RequestMapping(method=RequestMethod.GET, produces=JSON_CONTENT)
    public JdbcTableBean select(
        @ModelAttribute(TARGET_ENTITY)
        final JdbcTable entity
        ){
        log.debug("select()");
        return bean(
            entity
            );
        }

    /**
     * POST DELETE request.
     * 
     */
    @ResponseBody
    @RequestMapping(method=RequestMethod.POST, params={"action"}, produces=JSON_CONTENT)
    public JdbcTableBean action(
        @ModelAttribute(TARGET_ENTITY)
        final JdbcTable entity,
        @RequestParam(value="action", required=true)
        final String action
        ){
        log.debug("action()");
        log.debug("  action [{}]", action);

        log.debug(" status [{}]", entity.meta().jdbc().status());

        if ("delete".equals(action))
            {
            entity.meta().jdbc().delete();
            }
        if ("drop".equals(action))
            {
            entity.meta().jdbc().drop();
            }

        log.debug(" status [{}]", entity.meta().jdbc().status());
        
        return bean(
            entity
            );
        }

    }
