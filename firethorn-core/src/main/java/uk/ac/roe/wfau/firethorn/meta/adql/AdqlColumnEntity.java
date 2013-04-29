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

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.entity.AbstractFactory;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectEntityMethod;
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
    name = AdqlColumnEntity.DB_TABLE_NAME
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "AdqlColumn-select-parent",
            query = "FROM AdqlColumnEntity WHERE parent = :parent ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "AdqlColumn-select-parent.name",
            query = "FROM AdqlColumnEntity WHERE ((parent = :parent) AND (name = :name)) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "AdqlColumn-search-parent.text",
            query = "FROM AdqlColumnEntity WHERE ((parent = :parent) AND (name LIKE :text)) ORDER BY ident desc"
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
    protected static final String DB_BASE_TYPE_COL = "basetype" ;
    protected static final String DB_BASE_SIZE_COL = "basesize" ;

    /**
     * Column factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<AdqlColumn>
    implements AdqlColumn.Factory
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
        fetch = FetchType.EAGER,
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
        fetch = FetchType.EAGER,
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

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_BASE_TYPE_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    @Enumerated(
        EnumType.STRING
        )
    private AdqlColumn.Type basetype ;
    @Override
    protected AdqlColumn.Type basetype()
        {
        return basetype(
            false
            );
        }
    @Override
    protected AdqlColumn.Type basetype(boolean pull)
        {
        if ((this.basetype == null) || (pull))
            {
            if (base() != null)
                {
                this.basetype = base().meta().adql().type();
                }
            }
        return this.basetype;
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_BASE_SIZE_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private Integer basesize ;
    @Override
    protected Integer basesize()
        {
        return basesize(
            false
            );
        }
    @Override
    protected Integer basesize(boolean pull)
        {
        if ((this.basesize == null) || (pull))
            {
            if (base() != null)
                {
                this.basesize = base().meta().adql().size();
                }
            }
        return this.basesize;
        }

    @Override
    public String alias()
        {
        return "ADQL_" + ident();
        }

    @Override
    public String link()
        {
        return factories().adql().columns().links().link(
            this
            );
        }
    }
