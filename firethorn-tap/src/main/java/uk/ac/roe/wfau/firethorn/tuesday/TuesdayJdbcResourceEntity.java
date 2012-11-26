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

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.extern.slf4j.Slf4j;

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
@Slf4j
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = TuesdayJdbcResourceEntity.DB_TABLE_NAME
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "TuesdayJdbcResource-select-all",
            query = "FROM TuesdayJdbcResourceEntity ORDER BY name asc, ident desc"
            ),
        @NamedQuery(
            name  = "TuesdayJdbcResource-search-text",
            query = "FROM TuesdayJdbcResourceEntity WHERE (name LIKE :text) ORDER BY ident desc"
            )
        }
    )
public class TuesdayJdbcResourceEntity
    extends TuesdayBaseResourceEntity<TuesdayJdbcSchema>
    implements TuesdayJdbcResource
    {
    protected static final String DB_TABLE_NAME = "TuesdayJdbcResourceEntity";
    
    /**
     * Resource factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<TuesdayJdbcResource>
    implements TuesdayJdbcResource.Factory
        {

        @Override
        public Class<?> etype()
            {
            return TuesdayJdbcResourceEntity.class ;
            }

        @Override
        @SelectEntityMethod
        public Iterable<TuesdayJdbcResource> select()
            {
            return super.iterable(
                super.query(
                    "TuesdayJdbcResource-select-all"
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<TuesdayJdbcResource> search(final String text)
            {
            return super.iterable(
                super.query(
                    "TuesdayJdbcResource-search-text"
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
        public TuesdayJdbcResource  create(final String name)
            {
            return super.insert(
                new TuesdayJdbcResourceEntity(
                    name
                    )
                );
            }

        @Autowired
        protected TuesdayJdbcSchema.Factory schemas;

        @Override
        public TuesdayJdbcSchema.Factory schemas()
            {
            return this.schemas;
            }

        @Autowired
        protected TuesdayJdbcResource.IdentFactory idents ;

        @Override
        public TuesdayJdbcResource.IdentFactory identifiers()
            {
            return this.idents ;
            }
        }
    
    protected TuesdayJdbcResourceEntity()
        {
        super();
        }

    protected TuesdayJdbcResourceEntity(String name)
        {
        super(name);
        }

    @Override
    public TuesdayJdbcResource.Schemas schemas()
        {
        return new TuesdayJdbcResource.Schemas(){
            @Override
            public Iterable<TuesdayJdbcSchema> select()
                {
                return factories().jdbc().schemas().select(
                    TuesdayJdbcResourceEntity.this
                    );
                }
            @Override
            public TuesdayJdbcSchema select(String name)
                {
                return factories().jdbc().schemas().select(
                    TuesdayJdbcResourceEntity.this,
                    name
                    );
                }
            };
        }

    @Embedded
    private TuesdayJdbcConnectionEntity connection = new TuesdayJdbcConnectionEntity(
        this
        );
/*
    protected TuesdayJdbcConnectionEntity getConnection()
        {
        return this.connection;
        }
    protected void setConnection(TuesdayJdbcConnectionEntity connection)
        {
        this.connection = connection;
        }
 */
    @Override
    public TuesdayJdbcConnection connection()
        {
        return this.connection;
        }

    @Override
    public String link()
        {
        // TODO Auto-generated method stub
        return null;
        }

    public void inport(DatabaseMetaData metadata)
        {
        try {
            log.debug("inport(DatabaseMetaData)");
            log.debug("Database [{}]", metadata.getDatabaseProductName());
            }
        catch (final SQLException ouch)
            {
            log.error("Error reading database metadata", ouch);
            throw new RuntimeException(
                ouch
                );
            }
        }
    }
