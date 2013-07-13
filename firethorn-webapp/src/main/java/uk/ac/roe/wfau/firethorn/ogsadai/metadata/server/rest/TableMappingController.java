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
package uk.ac.roe.wfau.firethorn.ogsadai.metadata.server.rest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import uk.ac.roe.wfau.firethorn.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractController;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;

/**
 * Spring MVC controller for our TableMapping service.
 *
 */
@Slf4j
@Controller
@RequestMapping(TableMappingController.CONTROLLER_PATH)
public class TableMappingController
    extends AbstractController
    {
    /**
     * The URI path field for the table alias.
     *
     */
    public static final String TABLE_ALIAS_FIELD = "table" ;

    /**
     * The URI path for the controller.
     *
     */
    public static final String CONTROLLER_PATH = "/meta/table/{" + TABLE_ALIAS_FIELD + "}" ;

    @Override
    public Path path()
        {
        return path(
            TableMappingController.CONTROLLER_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public TableMappingController()
        {
        super();
        }

    /**
     * JSON GET request.
     *
     */
    @ResponseBody
    @RequestMapping(method=RequestMethod.GET, produces=JSON_MAPPING)
    public TableMappingBean jsonSelect(
        @PathVariable(TableMappingController.TABLE_ALIAS_FIELD)
        final String alias
        ) throws NotFoundException {
        log.debug("jsonSelect()");
        return new TableMappingBean(
            factories().base().tables().resolve(
                alias
                ).root()
            );
        }

    /**
     * Bean wrapper for a table.
     *
     */
    public static class TableMappingBean
        {

        /**
         * Public constructor.
         *
         */
        public TableMappingBean(final BaseTable<?,?> table)
            {
            this.table = table;
            }

        private final BaseTable<?,?> table ;
        protected BaseTable<?,?> table()
            {
            return this.table;
            }

        /**
         * The table alias.
         * <br/>
         * This is the table alias used in SQL queries passed into OGSA-DAI,
         * before the mapping from table alias to fully qualified resource table name.
         * @return The table alias.
         *
         */
        public String getAlias()
            {
            return this.table.alias();
            }

        /**
         * Get the fully qualified table name (catalog.schema.table) in the target resource.
         * @return The fully qualified table name.
         *
         */
        public String getName()
            {
            return this.table.namebuilder().toString();
            }

        /**
         * Get the target resource identifier.
         * @return The target resource identifier.
         *
         */
        public String getResource()
            {
            return this.table.resource().ogsaid();
            }
        }
    }
