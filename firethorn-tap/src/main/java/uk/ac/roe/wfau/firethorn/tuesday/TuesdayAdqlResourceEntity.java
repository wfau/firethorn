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
import javax.persistence.Table;

import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.NamedQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.common.entity.AbstractFactory;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.SelectEntityMethod;

@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = TuesdayAdqlResourceEntity.DB_TABLE_NAME
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "TuesdayAdqlResource-select-all",
            query = "FROM TuesdayAdqlResourceEntity ORDER BY name asc, ident desc"
            ),
        @NamedQuery(
            name  = "TuesdayAdqlResource-search-text",
            query = "FROM TuesdayAdqlResourceEntity WHERE (name LIKE :text) ORDER BY ident desc"
            )
        }
    )
public class TuesdayAdqlResourceEntity
extends TuesdayBaseResourceEntity<TuesdayAdqlSchema>
    implements TuesdayAdqlResource
    {
    protected static final String DB_TABLE_NAME = "TuesdayAdqlResourceEntity";

    /**
     * Our Entity Factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<TuesdayAdqlResource>
    implements TuesdayAdqlResource.Factory
        {

        @Override
        public Class<?> etype()
            {
            return TuesdayAdqlResourceEntity.class ;
            }

        @Override
        @SelectEntityMethod
        public Iterable<TuesdayAdqlResource> select()
            {
            return super.iterable(
                super.query(
                    "TuesdayAdqlResource-select-all"
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<TuesdayAdqlResource> search(final String text)
            {
            return super.iterable(
                super.query(
                    "TuesdayAdqlResource-search-text"
                    ).setString(
                        "text",
                        searchParam(
                            text
                            )
                        )
                );
            }

        @Override
        @CreateEntityMethod
        public TuesdayAdqlResource  create(final String name)
            {
            return super.insert(
                new TuesdayAdqlResourceEntity(
                    name
                    )
                );
            }

        @Autowired
        protected TuesdayAdqlSchema.Factory schemas;

        @Override
        public TuesdayAdqlSchema.Factory schemas()
            {
            return this.schemas;
            }

        @Autowired
        protected TuesdayAdqlResource.IdentFactory idents ;

        @Override
        public TuesdayAdqlResource.IdentFactory identifiers()
            {
            return this.idents ;
            }
        }

    protected TuesdayAdqlResourceEntity()
        {
        super();
        }

    protected TuesdayAdqlResourceEntity(String name)
        {
        super(name);
        }

    @Override
    public TuesdayAdqlResource.Schemas schemas()
        {
        return new TuesdayAdqlResource.Schemas()
            {
            @Override
            public Iterable<TuesdayAdqlSchema> select()
                {
                return factories().adql().schemas().select(
                    TuesdayAdqlResourceEntity.this
                    );
                }
            @Override
            public TuesdayAdqlSchema select(String name)
                {
                return factories().adql().schemas().select(
                    TuesdayAdqlResourceEntity.this,
                    name
                    );
                }
            @Override
            public TuesdayAdqlSchema create(String name)
                {
                return factories().adql().schemas().create(
                    TuesdayAdqlResourceEntity.this,
                    name
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
    }
