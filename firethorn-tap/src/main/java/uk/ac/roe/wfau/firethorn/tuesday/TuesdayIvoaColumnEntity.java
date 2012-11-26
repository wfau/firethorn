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

import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.NamedQueries;
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
    name = TuesdayIvoaColumnEntity.DB_TABLE_NAME,
    uniqueConstraints={
        }
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "TuesdayIvoaColumn-select-parent",
            query = "FROM TuesdayIvoaColumnEntity WHERE parent = :parent ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "TuesdayIvoaColumn-parent.name",
            query = "FROM TuesdayIvoaColumnEntity WHERE ((parent = :parent) AND (name = :name)) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "TuesdayIvoaColumn-search-parent.text",
            query = "FROM TuesdayIvoaColumnEntity WHERE ((parent = :parent) AND (name LIKE :text)) ORDER BY ident desc"
            )
        }
    )
public class TuesdayIvoaColumnEntity
    extends TuesdayBaseColumnEntity<TuesdayIvoaColumn>
    implements TuesdayIvoaColumn
    {
    protected static final String DB_TABLE_NAME = "TuesdayIvoaColumnEntity";

    /**
     * Column factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<TuesdayIvoaColumn>
    implements TuesdayIvoaColumn.Factory
        {

        @Override
        public Class<?> etype()
            {
            return TuesdayIvoaColumnEntity.class ;
            }

        @Override
        @CreateEntityMethod
        public TuesdayIvoaColumn create(final TuesdayIvoaTable parent, final String name)
            {
            return this.insert(
                new TuesdayIvoaColumnEntity(
                    parent,
                    name
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<TuesdayIvoaColumn> select(final TuesdayIvoaTable parent)
            {
            return super.list(
                super.query(
                    "TuesdayIvoaColumn-select-parent"
                    ).setEntity(
                        "parent",
                        parent
                        )
                );
            }

        @Override
        @SelectEntityMethod
        public TuesdayIvoaColumn select(final TuesdayIvoaTable parent, final String name)
            {
            return super.first(
                super.query(
                    "TuesdayIvoaColumn-select-parent.name"
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
        public Iterable<TuesdayIvoaColumn> search(final TuesdayIvoaTable parent, final String text)
            {
            return super.iterable(
                super.query(
                    "TuesdayIvoaColumn-search-parent.text"
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
        protected TuesdayIvoaColumn.IdentFactory idents ;

        @Override
        public TuesdayIvoaColumn.IdentFactory identifiers()
            {
            return this.idents ;
            }
        }
    
    protected TuesdayIvoaColumnEntity()
        {
        }

    protected TuesdayIvoaColumnEntity(TuesdayIvoaTable table, String name)
        {
        super(name);
        this.table = table;
        }

    @Override
    public TuesdayIvoaColumn ogsa()
        {
        return this ;
        }

    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = TuesdayIvoaTableEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = true
        )
    private TuesdayIvoaTable table;
    @Override
    public TuesdayIvoaTable table()
        {
        return this.table;
        }
    @Override
    public TuesdayIvoaSchema schema()
        {
        return this.table().schema();
        }
    @Override
    public TuesdayIvoaResource resource()
        {
        return this.table().resource();
        }
    }
