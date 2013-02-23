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
package uk.ac.roe.wfau.firethorn.metadata.server.table;

import java.util.Iterator;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import uk.ac.roe.wfau.firethorn.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumn;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcColumn;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractController;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityBeanIter;
import uk.ac.roe.wfau.firethorn.webapp.control.EntityBean;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcColumnBean;

/**
 * Spring MVC controller for our Attribute mapping service.
 *
 */
@Slf4j
@Controller
@RequestMapping(AttribServiceController.CONTROLLER_PATH)
public class AttribServiceController
    extends AbstractController
    {
    /**
     * The URI path field for the table alias.
     *
     */
    public static final String TABLE_ALIAS_FIELD = "table"  ;

    /**
     * The URI path field for the column name.
     *
     */
    public static final String COLUMN_NAME_FIELD = "column" ;

    /**
     * The URI path for the controller.
     *
     */
    public static final String CONTROLLER_PATH = "/meta/table/{" + TABLE_ALIAS_FIELD + "}" ;

    /**
     * The URI path for a list of columns.
     *
     */
    public static final String COLUMN_LIST_PATH = "columns" ;

    /**
     * The URI path for a specific column.
     *
     */
    public static final String COLUMN_NAME_PATH = "column/{" + COLUMN_NAME_FIELD + "}" ;

    @Override
    public Path path()
        {
        return path(
            AttribServiceController.CONTROLLER_PATH 
            );
        }

    /**
     * Public constructor.
     *
     */
    public AttribServiceController()
        {
        super();
        }

    /**
     * JSON GET request for a specific column.
     *
     */
    @ResponseBody
    @RequestMapping(value=COLUMN_NAME_PATH, method=RequestMethod.GET, produces=JSON_MAPPING)
    public ColumnBean jsonSelect(
        @PathVariable(AttribServiceController.TABLE_ALIAS_FIELD)
        final String alias,
        @PathVariable(AttribServiceController.COLUMN_NAME_FIELD)
        final String name
        ) throws NotFoundException {
        return new ColumnBean(
            factories().base().tables().resolve(
                alias
                ).root().columns().select(
                    name
                    )
            );
        }

    /**
     * JSON GET request for a list of columns.
     *
     */
    @ResponseBody
    @RequestMapping(value=COLUMN_LIST_PATH, method=RequestMethod.GET, produces=JSON_MAPPING)
    public ColumnBean.Iter jsonSelect(
        @PathVariable(AttribServiceController.TABLE_ALIAS_FIELD)
        final String alias
        ) throws NotFoundException {

        // I HATE GENERICS
        return new ColumnBean.Iter(
            (Iterable<BaseColumn<?>>) factories().base().tables().resolve(
                alias
                ).root().columns().select()
            );
        }

    /**
     * 
     * 
     */
    public static class ColumnBean
        {
        public static class Iter
        implements Iterable<ColumnBean>
            {
            private Iterable<BaseColumn<?>> iterable ;
            protected Iterable<BaseColumn<?>> iterable()
                {
                return this.iterable;
                }
            public Iter(final Iterable<BaseColumn<?>> iterable)
                {
                this.iterable = iterable  ;
                }
            @Override
            public Iterator<ColumnBean> iterator()
                {
                return new Iterator<ColumnBean>()
                    {
                    Iterator<BaseColumn<?>> iter = iterable().iterator();
                    @Override
                    public boolean hasNext()
                        {
                        return this.iter.hasNext();
                        }
                    @Override
                    public ColumnBean next()
                        {
                        return new ColumnBean(
                            this.iter.next()
                            );
                        }
                    @Override
                    public void remove()
                        {
                        this.iter.remove();
                        }
                    };
                }
            }
        
        public ColumnBean(BaseColumn<?> column)
            {
            this.column = column ;
            }

        private BaseColumn<?> column ;
        
        /**
         * The parent table alias.
         * <br/>
         * This is the table alias used in SQL queries passed into OGSA-DAI,
         * before the mapping from table alias to fully qualified resource table name.
         * @return The parent table alias.
         *
         */
        public String getSource()
            {
            return this.column.table().alias();
            }

        /**
         * Get the column name.
         * @return The column name.
         *
         */
        public String getName()
            {
            return this.column.name();
            }
        
        /**
         * Get the column type.
         * @return The column type.
         *
         */
        public int getType()
            {
            return 0;
            }

        /**
         *
         *
         */
        public boolean isKey()
            {
            return false ;
            }
        }
    }
