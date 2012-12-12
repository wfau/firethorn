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

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
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

        @Override
        @CreateEntityMethod
        public TuesdayJdbcResource create(final String name, final String url)
            {
            return super.insert(
                new TuesdayJdbcResourceEntity(
                    name,
                    url
                    )
                );
            }

		@Override
		public TuesdayJdbcResource create(final String name, final String url, final String user, final String pass) {
            return super.insert(
                new TuesdayJdbcResourceEntity(
                    name,
                    url,
                    user,
                    pass
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
        public TuesdayJdbcResource.IdentFactory idents()
            {
            return this.idents ;
            }

        @Autowired
        protected TuesdayJdbcResource.LinkFactory links;
        @Override
        public TuesdayJdbcResource.LinkFactory links()
            {
            return this.links;
            }
        }
    
    protected TuesdayJdbcResourceEntity()
        {
        super();
        }

    protected TuesdayJdbcResourceEntity(final String name)
        {
        super(name);
        this.connection = new TuesdayJdbcConnectionEntity(
            this
            );
        }

    protected TuesdayJdbcResourceEntity(final String name, final  String url)
        {
        super(name);
        this.connection = new TuesdayJdbcConnectionEntity(
            this,
            url
            );
        }

    protected TuesdayJdbcResourceEntity(final String name, final String url, final String user, final String pass)
	    {
	    super(name);
	    this.connection = new TuesdayJdbcConnectionEntity(
	        this,
	        url,
	        user,
	        pass
	        );
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
            @Override
            public Iterable<TuesdayJdbcSchema> search(String text)
                {
                return factories().jdbc().schemas().search(
                    TuesdayJdbcResourceEntity.this,
                    text
                    );
                }
            };
        }

    @Embedded
    private TuesdayJdbcConnectionEntity connection;

    @Override
    public TuesdayJdbcConnection connection()
        {
        return this.connection;
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

                if ((schema != null) && (schema.name().equals(schemaname)))
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

	@Override
	public String alias()
		{
		return "jdbc_resource_" + ident();
		}

	/**
	 * The the OGSA-DAI resource ID.
	 * @todo Move to a common base class.
	 * 
	 */
    protected static final String DB_OGSA_ID_COL = "ogsa_id";
    @Column(
        name = DB_OGSA_ID_COL,
        unique = false,
        nullable = true,
        updatable = false
        )
	private String ogsaid;
	@Override
	public String ogsaid()
		{
		return this.ogsaid;
		}
	@Override
	public void ogsaid(String ogsaid)
		{
		this.ogsaid = ogsaid;
		}

    @Override
    public String link()
        {
        return factories().jdbc().resources().links().link(
            this
            );
        }
    }
