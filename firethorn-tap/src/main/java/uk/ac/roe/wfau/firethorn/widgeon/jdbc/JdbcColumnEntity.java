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
package uk.ac.roe.wfau.firethorn.widgeon.jdbc ;

import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.List;

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
import uk.ac.roe.wfau.firethorn.common.entity.Identifier;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.CascadeEntityMethod;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.SelectEntityMethod;
import uk.ac.roe.wfau.firethorn.common.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.widgeon.ResourceStatusEntity;
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlCatalog;
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlTable;

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
            query = "FROM JdbcColumnEntity WHERE ((parent = :parent) AND (name = :name)) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "jdbc.column-search-parent.text",
            query = "FROM JdbcColumnEntity WHERE ((parent = :parent) AND (name = :text)) ORDER BY ident desc"
            )
        }
    )
public class JdbcColumnEntity
extends ResourceStatusEntity
implements JdbcColumn
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
    extends AbstractFactory<JdbcColumn>
    implements JdbcColumn.Factory
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
        protected JdbcColumn insert(final JdbcColumnEntity entity)
            {
            super.insert(
                entity
                );
            for (final AdqlTable view : entity.parent().views().select())
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
        public JdbcColumn create(final JdbcTable parent, final String name)
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
        public Iterable<JdbcColumn> select(final JdbcTable parent)
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
        public JdbcColumn select(final JdbcTable parent, final String name)
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

        @Override
        @SelectEntityMethod
        public Iterable<JdbcColumn> search(final JdbcTable parent, final String text)
            {
            return super.iterable(
                super.query(
                    "jdbc.column-search-parent.text"
                    ).setEntity(
                        "parent",
                        parent
                    ).setString(
                        "text",
                        searchParam(
                            text
                            )
                        )
                );
            }

        @Autowired
        protected AdqlColumn.Factory views ;

        @Override
        public AdqlColumn.Factory views()
            {
            return this.views ;
            }

        @Autowired
        protected JdbcColumn.IdentFactory identFactory ;

        @Override
        public String link(JdbcColumn entity)
            {
            return identFactory.link(
                entity.ident()
                );
            }
        @Override
        public String link(Identifier ident)
            {
            return identFactory.link(
                ident
                );
            }
        @Override
        public Identifier ident(final String string)
            {
            return identFactory.ident(
                string
                );
            }
        }

    @Override
    public JdbcColumn.Views views()
        {
        return new JdbcColumn.Views()
            {
            @Override
            public Iterable<AdqlColumn> select()
                {
                return womble().resources().adql().tables().adqlColumns().select(
                    JdbcColumnEntity.this
                    );
                }

            @Override
            public AdqlColumn search(final AdqlResource parent)
                {
                return womble().resources().adql().tables().adqlColumns().select(
                    parent,
                    JdbcColumnEntity.this
                    );
                }

            @Override
            public AdqlColumn search(final AdqlCatalog parent)
                {
                return womble().resources().adql().tables().adqlColumns().select(
                    parent,
                    JdbcColumnEntity.this
                    );
                }

            @Override
            public AdqlColumn search(final AdqlSchema parent)
                {
                return womble().resources().adql().tables().adqlColumns().select(
                    parent,
                    JdbcColumnEntity.this
                    );
                }

            @Override
            public AdqlColumn search(final AdqlTable parent)
                {
                return womble().resources().adql().tables().adqlColumns().select(
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
    protected JdbcColumnEntity(final JdbcTable parent, final String name)
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
    private JdbcTable parent ;

    @Override
    public JdbcTable parent()
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
    public JdbcCatalog catalog()
        {
        return this.parent.schema().catalog();
        }

    @Override
    public JdbcSchema schema()
        {
        return this.parent.schema();
        }

    @Override
    public JdbcTable table()
        {
        return this.parent;
        }

    @Override
    public List<JdbcDiference> diff(final boolean push, final boolean pull)
        {
        return diff(
            resource().metadata(),
            new ArrayList<JdbcDiference>(),
            push,
            pull
            );
        }

    @Override
    public List<JdbcDiference> diff(final DatabaseMetaData metadata, final List<JdbcDiference> results, final boolean push, final boolean pull)
        {
        //
        // Check this column.
        // ....
        return results ;
        }

    @Override
    public String link()
        {
        return womble().resources().jdbc().catalogs().schemas().tables().columns().link(
            this.ident()
            );
        }
    }

