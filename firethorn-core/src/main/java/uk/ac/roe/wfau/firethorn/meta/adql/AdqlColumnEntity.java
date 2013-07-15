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
package uk.ac.roe.wfau.firethorn.meta.adql;

import java.util.UUID;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumn;
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumnEntity;

/**
 *
 *
 */
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = AdqlColumnEntity.DB_TABLE_NAME,
    uniqueConstraints={
        }
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "AdqlColumn-select-parent",
            query = "FROM AdqlColumnEntity WHERE parent = :parent ORDER BY ident asc"
            ),
        @NamedQuery(
            name  = "AdqlColumn-select-parent.name",
            query = "FROM AdqlColumnEntity WHERE ((parent = :parent) AND (name = :name)) ORDER BY ident asc"
            ),
        @NamedQuery(
            name  = "AdqlColumn-search-parent.text",
            query = "FROM AdqlColumnEntity WHERE ((parent = :parent) AND (name LIKE :text)) ORDER BY ident asc"
            )
        }
    )
public class AdqlColumnEntity
    extends BaseColumnEntity<AdqlColumn>
    implements AdqlColumn
    {
    /**
     * Hibernate table mapping.
     *
     */
    protected static final String DB_TABLE_NAME = "AdqlColumnEntity";

    /**
     * Hibernate column mapping.
     *
     */

    /**
     * Alias factory implementation.
     * @todo Move to a separate package.
     *
     */
    @Component
    public static class AliasFactory
    implements AdqlColumn.AliasFactory
        {
        @Override
        public String alias(final AdqlColumn column)
            {
            return "ADQL_".concat(
                column.ident().toString()
                );
            }
        }

    /**
     * Column factory implementation.
     *
     */
    @Repository
    public static class EntityFactory
    extends AbstractEntityFactory<AdqlColumn>
    implements AdqlColumn.EntityFactory
        {

        @Override
        public Class<?> etype()
            {
            return AdqlColumnEntity.class ;
            }

        @Override
        @CreateEntityMethod
        public AdqlColumn create(final AdqlTable parent, final BaseColumn<?> base)
            {
            return this.insert(
                new AdqlColumnEntity(
                    parent,
                    base
                    )
                );
            }

        @Override
        @CreateEntityMethod
        public AdqlColumn create(final AdqlTable parent, final BaseColumn<?> base, final String name)
            {
            return this.insert(
                new AdqlColumnEntity(
                    parent,
                    base,
                    name
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<AdqlColumn> select(final AdqlTable parent)
            {
            return super.list(
                super.query(
                    "AdqlColumn-select-parent"
                    ).setEntity(
                        "parent",
                        parent
                        )
                );
            }

        @Override
        @SelectEntityMethod
        public AdqlColumn select(final AdqlTable parent, final String name)
            {
            return super.first(
                super.query(
                    "AdqlColumn-select-parent.name"
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
        public Iterable<AdqlColumn> search(final AdqlTable parent, final String text)
            {
            return super.iterable(
                super.query(
                    "AdqlColumn-search-parent.text"
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
        protected AdqlColumn.IdentFactory idents;
        @Override
        public AdqlColumn.IdentFactory idents()
            {
            return this.idents;
            }

        @Autowired
        protected AdqlColumn.LinkFactory links;
        @Override
        public AdqlColumn.LinkFactory links()
            {
            return this.links;
            }

        @Autowired
        protected AdqlColumn.AliasFactory aliases;
        @Override
        public AdqlColumn.AliasFactory aliases()
            {
            return this.aliases;
            }

        @Override
        public AdqlColumn select(UUID uuid) throws NotFoundException
            {
            // TODO Auto-generated method stub
            return null;
            }
        }

    protected AdqlColumnEntity()
        {
        super();
        }

    protected AdqlColumnEntity(final AdqlTable table, final BaseColumn<?> base)
        {
        super(table, base.name());
        this.base  = base ;
        this.table = table;
        }

    protected AdqlColumnEntity(final AdqlTable table, final BaseColumn<?> base, final String name)
        {
        super(table, ((name != null) ? name : base.name()));
        this.base  = base ;
        this.table = table;
        }

    @Override
    public String text()
        {
        if (super.text() == null)
            {
            return base().text();
            }
        else {
            return super.text();
            }
        }

    @Index(
        name=DB_TABLE_NAME + "IndexByParent"
        )
    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = AdqlTableEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = true
        )
    private AdqlTable table;
    @Override
    public AdqlTable table()
        {
        return this.table;
        }
    @Override
    public AdqlSchema schema()
        {
        return this.table().schema();
        }
    @Override
    public AdqlResource resource()
        {
        return this.table().resource();
        }

    @Index(
        name=DB_TABLE_NAME + "IndexByBase"
        )
    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = BaseColumnEntity.class
        )
    @JoinColumn(
        name = DB_BASE_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private BaseColumn<?> base ;
    @Override
    public BaseColumn<?> base()
        {
        return this.base ;
        }
    @Override
    public BaseColumn<?> root()
        {
        return base().root();
        }

    @Override
    public String alias()
        {
        return factories().adql().columns().aliases().alias(
            this
            );
        }

    @Override
    public String link()
        {
        return factories().adql().columns().links().link(
            this
            );
        }

    @Override
    protected void scanimpl()
        {
        // TODO Auto-generated method stub
        }
    }
