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
package uk.ac.roe.wfau.firethorn.tuesday;

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
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.common.entity.AbstractFactory;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.SelectEntityMethod;

/**
 *
 *
 */
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = TuesdayAdqlColumnEntity.DB_TABLE_NAME
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "TuesdayAdqlColumn-select-parent",
            query = "FROM TuesdayAdqlColumnEntity WHERE parent = :parent ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "TuesdayAdqlColumn-select-parent.name",
            query = "FROM TuesdayAdqlColumnEntity WHERE ((parent = :parent) AND (name = :name)) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "TuesdayAdqlColumn-search-parent.text",
            query = "FROM TuesdayAdqlColumnEntity WHERE ((parent = :parent) AND (name LIKE :text)) ORDER BY ident desc"
            )
        }
    )
public class TuesdayAdqlColumnEntity
    extends TuesdayBaseColumnEntity<TuesdayAdqlColumn>
    implements TuesdayAdqlColumn
    {
    protected static final String DB_TABLE_NAME = "TuesdayAdqlColumnEntity";

    /**
     * Column factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<TuesdayAdqlColumn>
    implements TuesdayAdqlColumn.Factory
        {

        @Override
        public Class<?> etype()
            {
            return TuesdayAdqlColumnEntity.class ;
            }

        @Override
        @CreateEntityMethod
        public TuesdayAdqlColumn create(final TuesdayAdqlTable parent, final TuesdayBaseColumn<?> base)
            {
            return this.insert(
                new TuesdayAdqlColumnEntity(
                    parent,
                    base
                    )
                );
            }

        @Override
        @CreateEntityMethod
        public TuesdayAdqlColumn create(final TuesdayAdqlTable parent, final TuesdayBaseColumn<?> base, final String name)
            {
            return this.insert(
                new TuesdayAdqlColumnEntity(
                    parent,
                    base,
                    name
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<TuesdayAdqlColumn> select(final TuesdayAdqlTable parent)
            {
            return super.list(
                super.query(
                    "TuesdayAdqlColumn-select-parent"
                    ).setEntity(
                        "parent",
                        parent
                        )
                );
            }

        @Override
        @SelectEntityMethod
        public TuesdayAdqlColumn select(final TuesdayAdqlTable parent, final String name)
            {
            return super.first(
                super.query(
                    "TuesdayAdqlColumn-select-parent.name"
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
        public Iterable<TuesdayAdqlColumn> search(final TuesdayAdqlTable parent, final String text)
            {
            return super.iterable(
                super.query(
                    "TuesdayAdqlColumn-search-parent.text"
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
        protected TuesdayAdqlColumn.IdentFactory idents;
        @Override
        public TuesdayAdqlColumn.IdentFactory idents()
            {
            return this.idents;
            }

        @Autowired
        protected TuesdayAdqlColumn.LinkFactory links;
        @Override
        public TuesdayAdqlColumn.LinkFactory links()
            {
            return this.links;
            }
        }

    protected TuesdayAdqlColumnEntity()
        {
        super();
        }

    protected TuesdayAdqlColumnEntity(final TuesdayAdqlTable table, final TuesdayBaseColumn<?> base)
        {
        super(table, base.name());
        this.base  = base ;
        this.table = table;
        }

    protected TuesdayAdqlColumnEntity(final TuesdayAdqlTable table, final TuesdayBaseColumn<?> base, final String name)
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
    @Override
    public String type()
        {
        if (super.type() == null)
            {
            return base().type();
            }
        else {
            return super.type();
            }
        }
    @Override
    public Integer size()
        {
        if (super.size() == null)
            {
            return base().size();
            }
        else {
            return super.size();
            }
        }
    @Override
    public String ucd()
        {
        if (super.ucd() == null)
            {
            return base().ucd();
            }
        else {
            return super.ucd();
            }
        }

    @Index(
        name=DB_TABLE_NAME + "IndexByParent"
        )
    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = TuesdayAdqlTableEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = true
        )
    private TuesdayAdqlTable table;
    @Override
    public TuesdayAdqlTable table()
        {
        return this.table;
        }
    @Override
    public TuesdayAdqlSchema schema()
        {
        return this.table().schema();
        }
    @Override
    public TuesdayAdqlResource resource()
        {
        return this.table().resource();
        }

    @Index(
        name=DB_TABLE_NAME + "IndexByBase"
        )
    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = TuesdayBaseColumnEntity.class
        )
    @JoinColumn(
        name = DB_BASE_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private TuesdayBaseColumn<?> base ;
    @Override
    public TuesdayBaseColumn<?> base()
        {
        return this.base ;
        }
    @Override
    public TuesdayOgsaColumn<?> ogsa()
        {
        return base().ogsa();
        }

    @Override
    public String alias()
        {
        return "adql_column_" + ident();
        }

    @Override
    public String link()
        {
        // TODO Auto-generated method stub
        return null;
        }
    }
