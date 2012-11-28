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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

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
import uk.ac.roe.wfau.firethorn.friday.api.FridayIvoaResource.Catalogs;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayJdbcTable.JdbcTableType;

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
    /**
     * Metadata database table name.
     * 
     */
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
        public TuesdayJdbcResource create(final String name)
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
            @Override
            public TuesdayJdbcSchema create(String name)
                {
                return factories().jdbc().schemas().create(
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


    @Override
    public void inport()
        {
        log.debug("inport()");
        try {
            DatabaseMetaData metadata = this.connection.metadata(); 
            JdbcProductType  product  = JdbcProductType.match(
                metadata
                );

            final ResultSet results = metadata.getTables(
                null,
                null,
                null,
                new String[]
                    {
                    JDBC_META_TABLE_TYPE_TABLE,
                    JDBC_META_TABLE_TYPE_VIEW
                    }
                );
            while (results.next())
                {
                String cname = results.getString(JDBC_META_TABLE_CAT);
                String sname = results.getString(JDBC_META_TABLE_SCHEM);
                String tname = results.getString(JDBC_META_TABLE_NAME);
                String ttype = results.getString(JDBC_META_TABLE_TYPE);
                log.debug("Found table [{}.{}.{}]", new Object[]{cname, sname, tname});

                //
                // In MySQL the schema name is always null, use the catalog name instead.
                if (product == JdbcProductType.MYSQL)
                    {
                    sname = cname ;
                    cname = null ;
                    }
                //
                // If the catalog name is null.
                if (cname == null)
                    {
                    log.debug("Catalog is null, whatever ..");
                    }
                //
                // If the schema name is null.
                if (sname == null)
                    {
                    log.debug("Schema is null, whatever ..");
                    }
                //
                // Skip if the schema is on our ignore list. 
                if (product.ignore().contains(sname))
                    {
                    log.debug("Schema is on the ignore list, skipping ...");
                    continue;
                    }

                TuesdayJdbcSchema schema = this.schemas().select(
                    sname
                    );
                if (schema == null)
                    {
                    schema = this.schemas().create(
                        sname
                        );
                    }
                //
                // If the table name is null.
                if (tname == null)
                    {
                    log.debug("Table is null, whatever ..");
                    }
                //
                // If the table name is not null.
                else {
                    log.debug("Table is [{}], processing", tname);
                    TuesdayJdbcTable table = schema.tables().select(
                        tname
                        );
                    if (table == null)
                        {
                        table = schema.tables().create(
                            tname,
                            TuesdayJdbcTable.JdbcTableType.match(
                                ttype
                                )
                            ); 
                        }
                    }
                }
            }
        catch (SQLException ouch)
            {
            log.error("SQLException reading database metadata [{}]", ouch);
            }
        }
    }
