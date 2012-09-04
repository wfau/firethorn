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
import uk.ac.roe.wfau.firethorn.widgeon.BaseResource;
import uk.ac.roe.wfau.firethorn.widgeon.JdbcResource;
import uk.ac.roe.wfau.firethorn.widgeon.ResourceStatusEntity;

/**
 * BaseResource.BaseTable implementation.
 *
 */
@Slf4j
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = JdbcTableEntity.DB_TABLE_NAME,
    uniqueConstraints=
        @UniqueConstraint(
            columnNames = {
                AbstractEntity.DB_NAME_COL,
                JdbcTableEntity.DB_PARENT_COL,
                }
            )
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "jdbc.table-select-parent",
            query = "FROM JdbcTableEntity WHERE parent = :parent ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "jdbc.table-select-parent.name",
            query = "FROM JdbcTableEntity WHERE parent = :parent AND name = :name ORDER BY ident desc"
            )
        }
    )
public class JdbcTableEntity
extends ResourceStatusEntity
implements JdbcResource.JdbcTable
    {

    /**
     * Our persistence table name.
     * 
     */
    public static final String DB_TABLE_NAME = "jdbc_table" ;

    /**
     * The persistence column name for our parent schema.
     * 
     */
    public static final String DB_PARENT_COL = "parent" ;

    /**
     * Our Entity Factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<JdbcResource.JdbcTable>
    implements JdbcResource.JdbcTable.Factory
        {

        @Override
        public Class<?> etype()
            {
            return JdbcTableEntity.class ;
            }

        /**
         * Insert a table into the database and update all the parent views.
         *
         */
        @CascadeEntityMethod
        protected JdbcResource.JdbcTable insert(final JdbcTableEntity entity)
            {
            super.insert(
                entity
                );
            for (AdqlResource.AdqlSchema view : entity.parent().views().select())
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
        public JdbcResource.JdbcTable create(final JdbcResource.JdbcSchema parent, final String name)
            {
            return this.insert(
                new JdbcTableEntity(
                    parent,
                    name
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<JdbcResource.JdbcTable> select(final JdbcResource.JdbcSchema parent)
            {
            return super.iterable(
                super.query(
                    "jdbc.table-select-parent"
                    ).setEntity(
                        "parent",
                        parent
                        )
                );
            }

        @Override
        @SelectEntityMethod
        public JdbcResource.JdbcTable select(final JdbcResource.JdbcSchema parent, final String name)
        throws NameNotFoundException
            {
            JdbcResource.JdbcTable result = this.search(
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
        public JdbcResource.JdbcTable search(final JdbcResource.JdbcSchema parent, final String name)
            {
            return super.first(
                super.query(
                    "jdbc.table-select-parent.name"
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
        protected AdqlResource.AdqlTable.Factory views ;

        @Override
        public AdqlResource.AdqlTable.Factory views()
            {
            return this.views ;
            }

        /**
         * Our Autowired column factory.
         * 
         */
        @Autowired
        protected JdbcResource.JdbcColumn.Factory columns ;

        @Override
        public JdbcResource.JdbcColumn.Factory columns()
            {
            return this.columns ;
            }
        }

    @Override
    public BaseResource.BaseTable.Views views()
        {
        return new BaseResource.BaseTable.Views()
            {
            @Override
            public Iterable<AdqlResource.AdqlTable> select()
                {
                return womble().resources().jdbc().views().catalogs().schemas().tables().select(
                    JdbcTableEntity.this
                    );
                }

            @Override
            public AdqlResource.AdqlTable search(AdqlResource.AdqlSchema parent)
                {
                return womble().resources().jdbc().views().catalogs().schemas().tables().search(
                    parent,
                    JdbcTableEntity.this
                    );
                }
            };
        }

    @Override
    public JdbcResource.JdbcTable.Columns columns()
        {
        return new JdbcResource.JdbcTable.Columns()
            {
            @Override
            public JdbcResource.JdbcColumn create(String name)
                {
                return womble().resources().jdbc().catalogs().schemas().tables().columns().create(
                    JdbcTableEntity.this,
                    name
                    );
                }

            @Override
            public Iterable<JdbcResource.JdbcColumn> select()
                {
                return womble().resources().jdbc().catalogs().schemas().tables().columns().select(
                    JdbcTableEntity.this
                    ) ;
                }

            @Override
            public JdbcResource.JdbcColumn select(String name)
            throws NameNotFoundException
                {
                return womble().resources().jdbc().catalogs().schemas().tables().columns().select(
                    JdbcTableEntity.this,
                    name
                    ) ;
                }

            @Override
            public JdbcResource.JdbcColumn search(String name)
                {
                return womble().resources().jdbc().catalogs().schemas().tables().columns().search(
                    JdbcTableEntity.this,
                    name
                    ) ;
                }
            };
        }

    /**
     * Default constructor needs to be protected not private.
     * http://kristian-domagala.blogspot.co.uk/2008/10/proxy-instantiation-problem-from.html
     *
     */
    protected JdbcTableEntity()
        {
        super();
        }

    /**
     * Create a new catalog.
     *
     */
    protected JdbcTableEntity(final JdbcResource.JdbcSchema parent, final String name)
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
        targetEntity = JdbcSchemaEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private JdbcResource.JdbcSchema parent ;

    @Override
    public JdbcResource.JdbcSchema parent()
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
        return this.parent.catalog().resource();
        }

    @Override
    public JdbcResource.JdbcCatalog catalog()
        {
        return this.parent.catalog();
        }

    @Override
    public JdbcResource.JdbcSchema schema()
        {
        return this.parent;
        }
    }

