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
    name = TuesdayIvoaSchemaEntity.DB_TABLE_NAME
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "TuesdayIvoaSchema-select-parent",
            query = "FROM TuesdayIvoaSchemaEntity WHERE parent = :parent ORDER BY name asc, ident desc"
            ),
        @NamedQuery(
            name  = "TuesdayIvoaSchema-select-parent.name",
            query = "FROM TuesdayIvoaSchemaEntity WHERE ((parent = :parent) AND (name = :name)) ORDER BY name asc, ident desc"
            ),
        @NamedQuery(
            name  = "TuesdayIvoaSchema-search-parent.text",
            query = "FROM TuesdayIvoaSchemaEntity WHERE ((parent = :parent) AND (name LIKE :text)) ORDER BY name asc, ident desc"
            )
        }
    )
public class TuesdayIvoaSchemaEntity
    extends TuesdayBaseSchemaEntity<TuesdayIvoaSchema, TuesdayIvoaTable>
    implements TuesdayIvoaSchema
    {
    protected static final String DB_TABLE_NAME = "TuesdayIvoaSchemaEntity";

    /**
     * Entity factory.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<TuesdayIvoaSchema>
    implements TuesdayIvoaSchema.Factory
        {

        @Override
        public Class<?> etype()
            {
            return TuesdayIvoaSchemaEntity.class ;
            }

        @Override
        @CreateEntityMethod
        public TuesdayIvoaSchema create(TuesdayIvoaResource parent, String name)
            {
            return this.insert(
                new TuesdayIvoaSchemaEntity(
                    parent,
                    name
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<TuesdayIvoaSchema> select(TuesdayIvoaResource parent)
            {
            return super.list(
                super.query(
                    "TuesdayIvoaSchema-select-parent"
                    ).setEntity(
                        "parent",
                        parent
                        )
                );
            }

        @Override
        @SelectEntityMethod
        public TuesdayIvoaSchema select(TuesdayIvoaResource parent, String name)
            {
            return super.first(
                super.query(
                    "TuesdayIvoaSchema-select-parent.name"
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
        public Iterable<TuesdayIvoaSchema> search(TuesdayIvoaResource parent, String text)
            {
            return super.iterable(
                super.query(
                    "TuesdayIvoaSchema-search-parent.text"
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
        protected TuesdayIvoaTable.Factory tables;

        @Override
        public TuesdayIvoaTable.Factory tables()
            {
            return this.tables;
            }

        @Autowired
        protected TuesdayIvoaSchema.IdentFactory identifiers ;

        @Override
        public TuesdayIvoaSchema.IdentFactory identifiers()
            {
            return this.identifiers ;
            }
        }
    
    protected TuesdayIvoaSchemaEntity()
        {
        super();
        }

    protected TuesdayIvoaSchemaEntity(TuesdayIvoaResource resource, String name)
        {
        super(resource, name);
        this.resource = resource;
        }

    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = TuesdayIvoaResourceEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private TuesdayIvoaResource resource;
    @Override
    public TuesdayIvoaResource resource()
        {
        return this.resource;
        }

    @Override
    public TuesdayIvoaSchema.Tables tables()
        {
        return new TuesdayIvoaSchema.Tables()
            {
            @Override
            public Iterable<TuesdayIvoaTable> select()
                {
                return factories().ivoa().tables().select(
                    TuesdayIvoaSchemaEntity.this
                    );
                }
            @Override
            public TuesdayIvoaTable select(String name)
                {
                return factories().ivoa().tables().select(
                    TuesdayIvoaSchemaEntity.this,
                    name
                    );
                }
            @Override
            public Iterable<TuesdayIvoaTable> search(String text)
                {
                return factories().ivoa().tables().search(
                    TuesdayIvoaSchemaEntity.this,
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
	public String alias()
		{
		return "ivoa_schema_" + ident();
		}
    }
