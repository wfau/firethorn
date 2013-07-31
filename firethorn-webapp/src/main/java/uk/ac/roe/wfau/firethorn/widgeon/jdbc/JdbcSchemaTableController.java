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
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcSchema;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTable;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityController;
import uk.ac.roe.wfau.firethorn.webapp.control.EntityBean;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;

/**
 * Spring MVC controller for <code>JdbcSchema</code> tables.
 *
 */
@Slf4j
@Controller
@RequestMapping(JdbcSchemaLinkFactory.SCHEMA_TABLE_PATH)
public class JdbcSchemaTableController
extends AbstractEntityController<JdbcTable, JdbcTableBean>
    {
    @Override
    public Path path()
        {
        return path(
            JdbcSchemaLinkFactory.SCHEMA_TABLE_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public JdbcSchemaTableController()
        {
        super();
        }

    /**
     * MVC property for the Resource name.
     *
     */
    public static final String SELECT_NAME = "jdbc.schema.table.select.name" ;

    /**
     * MVC property for the select results.
     *
     */
    public static final String SELECT_RESULT = "jdbc.schema.table.select.result" ;

    /**
     * MVC property for the search text.
     *
     */
    public static final String SEARCH_TEXT = "jdbc.schema.table.search.text" ;

    /**
     * MVC property for the search results.
     *
     */
    public static final String SEARCH_RESULT = "jdbc.schema.table.search.result" ;


    @Override
    public JdbcTableBean bean(final JdbcTable entity)
        {
        return new JdbcTableBean(
            entity
            );
        }

    @Override
    public Iterable<JdbcTableBean> bean(final Iterable<JdbcTable> iter)
        {
        return new JdbcTableBean.Iter(
            iter
            );
        }

    /**
     * Get the parent schema based on the identifier in the request.
     *
     */
    @ModelAttribute(JdbcSchemaController.TARGET_ENTITY)
    public JdbcSchema parent(
        @PathVariable(WebappLinkFactory.IDENT_FIELD)
        final String ident
        ) throws NotFoundException {
        log.debug("parent() [{}]", ident);
        return factories().jdbc().schemas().select(
            factories().jdbc().schemas().idents().ident(
                ident
                )
            );
        }

    /**
     * JSON GET request to select all.
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.GET, produces=JSON_MAPPING)
    public Iterable<JdbcTableBean> select(
        @ModelAttribute(JdbcSchemaController.TARGET_ENTITY)
        final JdbcSchema schema
        ){
        log.debug("select()");
        return bean(
            schema.tables().select()
            );
        }

    /**
     * JSON request to select by name.
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, params=SELECT_NAME, produces=JSON_MAPPING)
    public JdbcTableBean select(
        @ModelAttribute(JdbcSchemaController.TARGET_ENTITY)
        final JdbcSchema schema,
        @RequestParam(SELECT_NAME)
        final String name
        ) throws NotFoundException {
        log.debug("select(String) [{}]", name);
        return bean(
            schema.tables().select(
                name
                )
            );
        }
    }
