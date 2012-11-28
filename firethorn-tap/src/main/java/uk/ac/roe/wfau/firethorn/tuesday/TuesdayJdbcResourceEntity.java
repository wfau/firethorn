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
import org.springframework.jdbc.support.SQLExceptionSubclassTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
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
     * Our SQLException translator.
     * 
     */
    protected static final SQLExceptionTranslator translator = new SQLExceptionSubclassTranslator();

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

            final ResultSet tables = metadata.getTables(
                null,
                null,
                null,
                new String[]
                    {
                    JDBC_META_TABLE_TYPE_TABLE,
                    JDBC_META_TABLE_TYPE_VIEW
                    }
                );

            TuesdayJdbcSchema schema = null ;
            TuesdayJdbcTable  table  = null ;
            TuesdayJdbcColumn column = null ;

            while (tables.next())
                {
                final String tcname = tables.getString(JDBC_META_TABLE_CAT);
                final String tsname = tables.getString(JDBC_META_TABLE_SCHEM);
                final String ttname = tables.getString(JDBC_META_TABLE_NAME);
                final String tttype = tables.getString(JDBC_META_TABLE_TYPE);
                log.debug("Found table [{}.{}.{}]", new Object[]{tcname, tsname, ttname});
                //
                // If the catalog name is null.
                if (tcname == null)
                    {
                    log.debug("Catalog is null, whatever ..");
                    }
                //
                // If the schema name is null.
                if (tsname == null)
                    {
                    log.debug("Schema is null, whatever ..");
                    }
                //
                // In MySQL the schema name is always null, use the catalog name instead.
                String schemaname = tsname ;
                if (product == JdbcProductType.MYSQL)
                    {
                    schemaname = tcname ;
                    }
                //
                // Skip if the schema is on our ignore list. 
                if (product.ignores().contains(schemaname))
                    {
                    log.debug("Schema is on the ignore list, skipping ...");
                    continue;
                    }

                boolean create = false ;

                if (schema == null)
                    {
                    table  = null ;
                    column = null ;
                    create = true ;
                    schema = this.schemas().create(
                        schemaname
                        );
                    }
                else {
                    if (schema.name().equals(schemaname))
                        {
                        create = false ;
                        }
                    else {
                        table  = null ;
                        column = null ;
                        schema = this.schemas().select(
                            schemaname
                            );
                        if (schema != null)
                            {
                            create = false ;
                            }
                        else {
                            create = true ;
                            schema = this.schemas().create(
                                schemaname
                                );
                        
                            }
                        }
                    }
                //
                // If the table name is null.
                if (ttname == null)
                    {
                    log.debug("Table is null, whatever ..");
                    }
                //
                // If the table name is not null.
                else {
                    log.debug("Table is [{}], processing", ttname);

                    if (table == null)
                        {
                        column = null ;
                        create = true ;
                        table  = schema.tables().create(
                            ttname,
                            TuesdayJdbcTable.JdbcTableType.match(
                                tttype
                                )
                            ); 
                        }
                    else {
                        if (create)
                            {
                            column = null ;
                            create = true ;
                            table  = schema.tables().create(
                                ttname,
                                TuesdayJdbcTable.JdbcTableType.match(
                                    tttype
                                    )
                                ); 
                            }
                        else {
                            if (table.name().equals(ttname))
                                {
                                create = false ;
                                }
                            else {
                                column = null ;
                                table = schema.tables().select(
                                    ttname
                                    );
                                if (table != null)
                                    {
                                    create = false ;
                                    }
                                else {
                                    create = true ;
                                    table  = schema.tables().create(
                                        ttname,
                                        TuesdayJdbcTable.JdbcTableType.match(
                                            tttype
                                            )
                                        ); 
                                    }
                                }
                            }
                        }
                    //
                    // Import the table columns.
                    try {
                        final ResultSet columns = metadata.getColumns(
                            tcname,
                            tsname,
                            ttname,
                            null
                            );
                        while (columns.next())
                            {
                            String ccname = columns.getString(JDBC_META_TABLE_CAT);
                            String csname = columns.getString(JDBC_META_TABLE_SCHEM);
                            String ctname = columns.getString(JDBC_META_TABLE_NAME);
                            String colname = columns.getString(JDBC_META_COLUMN_NAME);
                            int    coltype = columns.getInt(JDBC_META_COLUMN_TYPE_TYPE);
                            int    colsize = columns.getInt(JDBC_META_COLUMN_SIZE);
                            log.debug("Column result [{}.{}.{}.{}]", new Object[]{
                                ccname,
                                csname,
                                ctname,
                                colname
                                });

                            if (column == null)
                                {
                                create = true ;
                                column = table.columns().create(
                                    colname,
                                    coltype,
                                    colsize
                                    );
                                }
                            else {
                                if (create)
                                    {
                                    create = true ;
                                    column = table.columns().create(
                                        colname,
                                        coltype,
                                        colsize
                                        );
                                    }
                                else {
                                    if (column.name().equals(colname))
                                        {
                                        create = false ;
                                        column.sqlsize(
                                            colsize
                                            );
                                        column.sqltype(
                                            coltype
                                            );
                                        }
                                    else {
                                        column = table.columns().select(
                                                colname
                                                );
                                        if (column != null)
                                            {
                                            create = false ;
                                            column.sqlsize(
                                                colsize
                                                );
                                            column.sqltype(
                                                coltype
                                                );
                                            }
                                        else {
                                            create = true ;
                                            column = table.columns().create(
                                                colname,
                                                coltype,
                                                colsize
                                                );
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    catch (SQLException ouch)
                        {
                        log.error("Exception reading database metadata [{}]", ouch.getMessage());
/*
 * Do we want to continue ?
                        throw translator.translate(
                            "Reading database metadata",
                            null,
                            ouch
                            );
 */
                        }
                    }
                }
            }
        catch (SQLException ouch)
            {
            log.error("Exception reading database metadata [{}]", ouch.getMessage());
            throw translator.translate(
                "Reading database metadata",
                null,
                ouch
                );
            }
        }
    }
