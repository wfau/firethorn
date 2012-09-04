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
package uk.ac.roe.wfau.firethorn.widgeon.entity.jdbc ;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.common.entity.AbstractEntity;
import uk.ac.roe.wfau.firethorn.common.entity.AbstractFactory;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.CascadeEntityMethod;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.SelectEntityMethod;
import uk.ac.roe.wfau.firethorn.common.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.widgeon.AdqlResource;
import uk.ac.roe.wfau.firethorn.widgeon.JdbcResource;
import uk.ac.roe.wfau.firethorn.widgeon.ResourceStatusEntity;

/**
 * BaseResource.BaseColumn implementation.
 *
 */
@Slf4j
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = JdbcColumnEntity.DB_TABLE_NAME,
    uniqueConstraints=
        @UniqueConstraint(
            columnNames = {
                AbstractEntity.DB_NAME_COL,
                JdbcColumnEntity.DB_PARENT_COL,
                }
            )
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "jdbc.column-select-parent",
            query = "FROM JdbcColumnEntity WHERE parent = :parent ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "jdbc.column-select-parent.name",
            query = "FROM JdbcColumnEntity WHERE parent = :parent AND name = :name ORDER BY ident desc"
            )
        }
    )
public class JdbcColumnEntity
extends ResourceStatusEntity
implements JdbcResource.JdbcColumn
    {

    /**
     * Our persistence table name.
     * 
     */
    public static final String DB_TABLE_NAME = "jdbc_column" ;

    /**
     * The persistence column name for our parent table.
     * 
     */
    public static final String DB_PARENT_COL = "parent" ;

    /**
     * Our Entity Factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<JdbcResource.JdbcColumn>
    implements JdbcResource.JdbcColumn.Factory
        {

        @Override
        public Class<?> etype()
            {
            return JdbcColumnEntity.class ;
            }

        /**
         * Insert a column into the database and update all the parent table views.
         *
         */
        @CascadeEntityMethod
        protected JdbcResource.JdbcColumn insert(final JdbcColumnEntity entity)
            {
            super.insert(
                entity
                );
            for (AdqlResource.AdqlTable view : entity.parent().views().select())
                {
                this.views().cascade(
                    view,
                    entity
                    );
                }
            return entity ;
            }

        @Override
        @CreateEntityMethod
        public JdbcResource.JdbcColumn create(final JdbcResource.JdbcTable parent, final String name)
            {
            return this.insert(
                new JdbcColumnEntity(
                    parent,
                    name
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<JdbcResource.JdbcColumn> select(final JdbcResource.JdbcTable parent)
            {
            return super.iterable(
                super.query(
                    "jdbc.column-select-parent"
                    ).setEntity(
                        "parent",
                        parent
                        )
                );
            }

        @Override
        @SelectEntityMethod
        public JdbcResource.JdbcColumn select(final JdbcResource.JdbcTable parent, final String name)
        throws NameNotFoundException
            {
            JdbcResource.JdbcColumn result = this.search(
                parent,
                name
                );
            if (result != null)
                {
                return result ;
                }
            else {
                throw new NameNotFoundException(
                    name
                    );
                }
            }

        @Override
        @SelectEntityMethod
        public JdbcResource.JdbcColumn search(final JdbcResource.JdbcTable parent, final String name)
            {
            return super.first(
                super.query(
                    "jdbc.column-select-parent.name"
                    ).setEntity(
                        "parent",
                        parent
                    ).setString(
                        "name",
                        name
                    )
                );
            }

        /**
         * Our Autowired view factory.
         * 
         */
        @Autowired
        protected AdqlResource.AdqlColumn.Factory views ;

        @Override
        public AdqlResource.AdqlColumn.Factory views()
            {
            return this.views ;
            }
        }

    @Override
    public JdbcResource.JdbcColumn.Views views()
        {
        return new JdbcResource.JdbcColumn.Views()
            {
            @Override
            public Iterable<AdqlResource.AdqlColumn> select()
                {
                return womble().resources().jdbc().views().catalogs().schemas().tables().adqlColumns().select(
                    JdbcColumnEntity.this
                    );
                }

            @Override
            public AdqlResource.AdqlColumn search(AdqlResource.AdqlTable parent)
                {
                return womble().resources().jdbc().views().catalogs().schemas().tables().adqlColumns().search(
                    parent,
                    JdbcColumnEntity.this
                    );
                }
            };
        }

    /**
     * Default constructor needs to be protected not private.
     * http://kristian-domagala.blogspot.co.uk/2008/10/proxy-instantiation-problem-from.html
     *
     */
    protected JdbcColumnEntity()
        {
        super();
        }

    /**
     * Create a new catalog.
     *
     */
    protected JdbcColumnEntity(final JdbcResource.JdbcTable parent, final String name)
        {
        super(name);
        log.debug("new([{}]", name);
        this.parent = parent ;
        }

    /**
     * Our parent column.
     *
     */
    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = JdbcTableEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private JdbcResource.JdbcTable parent ;

    @Override
    public JdbcResource.JdbcTable parent()
        {
        return this.parent ;
        }

    @Override
    public Status status()
        {
        if (this.parent().status() == Status.ENABLED)
            {
            return super.status();
            }
        else {
            return this.parent().status();
            }
        }

    @Override
    public JdbcResource resource()
        {
        return this.parent.schema().catalog().resource();
        }

    @Override
    public JdbcResource.JdbcCatalog catalog()
        {
        return this.parent.schema().catalog();
        }

    @Override
    public JdbcResource.JdbcSchema schema()
        {
        return this.parent.schema();
        }

    @Override
    public JdbcResource.JdbcTable table()
        {
        return this.parent;
        }
    }

