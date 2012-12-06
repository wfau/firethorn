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
    name = TuesdayAdqlSchemaEntity.DB_TABLE_NAME
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "TuesdayAdqlSchema-select-parent",
            query = "FROM TuesdayAdqlSchemaEntity WHERE parent = :parent ORDER BY name asc, ident desc"
            ),
        @NamedQuery(
            name  = "TuesdayAdqlSchema-select-parent.name",
            query = "FROM TuesdayAdqlSchemaEntity WHERE ((parent = :parent) AND (name = :name)) ORDER BY name asc, ident desc"
            ),
        @NamedQuery(
            name  = "TuesdayAdqlSchema-search-parent.text",
            query = "FROM TuesdayAdqlSchemaEntity WHERE ((parent = :parent) AND (name LIKE :text)) ORDER BY name asc, ident desc"
            )
        }
    )
public class TuesdayAdqlSchemaEntity
    extends TuesdayBaseSchemaEntity<TuesdayAdqlSchema, TuesdayAdqlTable>
    implements TuesdayAdqlSchema
    {
    protected static final String DB_TABLE_NAME = "TuesdayAdqlSchemaEntity";

    /**
     * Schema factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<TuesdayAdqlSchema>
    implements TuesdayAdqlSchema.Factory
        {

        @Override
        public Class<?> etype()
            {
            return TuesdayAdqlSchemaEntity.class ;
            }

        @Override
        @CreateEntityMethod
        public TuesdayAdqlSchema create(final TuesdayAdqlResource parent, final String name)
            {
            return this.insert(
                new TuesdayAdqlSchemaEntity(
                    parent,
                    name
                    )
                );
            }

		@Override
		public TuesdayAdqlSchema create(final TuesdayAdqlResourceEntity parent, final TuesdayBaseSchema<?, ?> base, final String name)
			{
			TuesdayAdqlSchema schema = this.create(
					parent,
					name
					);
			for (TuesdayBaseTable<?,?> table : base.tables().select())
				{
				schema.tables().create(
						table
						);
				}
			return schema;
			}

        @Override
        @SelectEntityMethod
        public Iterable<TuesdayAdqlSchema> select(TuesdayAdqlResource parent)
            {
            return super.list(
                super.query(
                    "TuesdayAdqlSchema-select-parent"
                    ).setEntity(
                        "parent",
                        parent
                        )
                );
            }

        @Override
        @SelectEntityMethod
        public TuesdayAdqlSchema select(TuesdayAdqlResource parent, String name)
            {
            return super.first(
                super.query(
                    "TuesdayAdqlSchema-select-parent.name"
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
        public Iterable<TuesdayAdqlSchema> search(TuesdayAdqlResource parent, String text)
            {
            return super.iterable(
                super.query(
                    "TuesdayAdqlSchema-search-parent.text"
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
        protected TuesdayAdqlTable.Factory tables;

        @Override
        public TuesdayAdqlTable.Factory tables()
            {
            return this.tables;
            }

        @Autowired
        protected TuesdayAdqlSchema.IdentFactory identifiers ;

        @Override
        public TuesdayAdqlSchema.IdentFactory identifiers()
            {
            return this.identifiers ;
            }

        }

    protected TuesdayAdqlSchemaEntity()
        {
        super();
        }

    protected TuesdayAdqlSchemaEntity(TuesdayAdqlResource resource, String name)
        {
        super(resource, name);
        this.resource = resource;
        }

    @Index(
        name=DB_TABLE_NAME + "IndexByParent"
        )
    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = TuesdayAdqlResourceEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = true
        )
    private TuesdayAdqlResource resource;
    @Override
    public TuesdayAdqlResource resource()
        {
        return this.resource;
        }

    @Override
    public TuesdayAdqlSchema.Tables tables()
        {
        return new TuesdayAdqlSchema.Tables()
            {
            @Override
            public Iterable<TuesdayAdqlTable> select()
                {
                return factories().adql().tables().select(
                    TuesdayAdqlSchemaEntity.this
                    );
                }
            @Override
            public TuesdayAdqlTable select(final String name)
                {
                return factories().adql().tables().select(
                    TuesdayAdqlSchemaEntity.this,
                    name
                    );
                }
            @Override
            public TuesdayAdqlTable create(final TuesdayBaseTable<?,?> base)
                {
                return factories().adql().tables().create(
                    TuesdayAdqlSchemaEntity.this,
                    base
                    );
                }
            @Override
            public TuesdayAdqlTable create(final TuesdayBaseTable<?,?> base, final String name)
                {
                return factories().adql().tables().create(
                    TuesdayAdqlSchemaEntity.this,
                    base,
                    name
                    );
                }
            @Override
            public Iterable<TuesdayAdqlTable> search(String text)
                {
                return factories().adql().tables().search(
                    TuesdayAdqlSchemaEntity.this,
                    text
                    );
                }
            };
        }

    @Override
    public String link()
        {
        // TODO Auto-generated method stub
        return null;
        }

	@Override
	public String alias() {
		return "adql_schema_" + ident();
		}
    }
