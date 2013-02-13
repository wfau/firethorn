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
    name = JdbcResourceEntity.DB_TABLE_NAME
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "JdbcResource-select-all",
            query = "FROM JdbcResourceEntity ORDER BY name asc, ident desc"
            ),
        @NamedQuery(
            name  = "JdbcResource-search-text",
            query = "FROM JdbcResourceEntity WHERE (name LIKE :text) ORDER BY ident desc"
            )
        }
    )
public class JdbcResourceEntity
    extends BaseResourceEntity<JdbcSchema>
    implements JdbcResource
    {
    /**
     * Metadata database table name.
     *
     */
    protected static final String DB_TABLE_NAME = "JdbcResourceEntity";

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
    extends AbstractFactory<JdbcResource>
    implements JdbcResource.Factory
        {

        @Override
        public Class<?> etype()
            {
            return JdbcResourceEntity.class ;
            }

        @Override
        @SelectEntityMethod
        public Iterable<JdbcResource> select()
            {
            return super.iterable(
                super.query(
                    "JdbcResource-select-all"
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<JdbcResource> search(final String text)
            {
            return super.iterable(
                super.query(
                    "JdbcResource-search-text"
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
        public JdbcResource create(final String name)
            {
            return super.insert(
                new JdbcResourceEntity(
                    name
                    )
                );
            }

        @Override
        @CreateEntityMethod
        public JdbcResource create(final String name, final String url)
            {
            return super.insert(
                new JdbcResourceEntity(
                    name,
                    url
                    )
                );
            }

		@Override
		public JdbcResource create(final String name, final String url, final String user, final String pass) {
            return super.insert(
                new JdbcResourceEntity(
                    name,
                    url,
                    user,
                    pass
                    )
                );
			}

        @Autowired
        protected JdbcSchema.Factory schemas;
        @Override
        public JdbcSchema.Factory schemas()
            {
            return this.schemas;
            }

        @Autowired
        protected JdbcResource.IdentFactory idents ;
        @Override
        public JdbcResource.IdentFactory idents()
            {
            return this.idents ;
            }

        @Autowired
        protected JdbcResource.LinkFactory links;
        @Override
        public JdbcResource.LinkFactory links()
            {
            return this.links;
            }
        }

    protected JdbcResourceEntity()
        {
        super();
        }

    protected JdbcResourceEntity(final String name)
        {
        super(name);
        this.connection = new JdbcConnectionEntity(
            this
            );
        }

    protected JdbcResourceEntity(final String name, final  String url)
        {
        super(name);
        this.connection = new JdbcConnectionEntity(
            this,
            url
            );
        }

    protected JdbcResourceEntity(final String name, final String url, final String user, final String pass)
	    {
	    super(name);
	    this.connection = new JdbcConnectionEntity(
	        this,
	        url,
	        user,
	        pass
	        );
	    }

    @Override
    public JdbcResource.Schemas schemas()
        {
        return new JdbcResource.Schemas(){
            @Override
            public Iterable<JdbcSchema> select()
                {
                return factories().jdbc().schemas().select(
                    JdbcResourceEntity.this
                    );
                }
            @Override
            public JdbcSchema select(final String name)
                {
                return factories().jdbc().schemas().select(
                    JdbcResourceEntity.this,
                    name
                    );
                }
            @Override
            public JdbcSchema create(final String name)
                {
                return factories().jdbc().schemas().create(
                    JdbcResourceEntity.this,
                    name
                    );
                }
            @Override
            public Iterable<JdbcSchema> search(final String text)
                {
                return factories().jdbc().schemas().search(
                    JdbcResourceEntity.this,
                    text
                    );
                }
            };
        }

    @Embedded
    private JdbcConnectionEntity connection;

    @Override
    public JdbcConnection connection()
        {
        return this.connection;
        }

    @Override
    public void inport()
        {
        log.debug("inport()");
        try {
            final DatabaseMetaData metadata = this.connection.metadata();
            final JdbcProductType  product  = JdbcProductType.match(
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

            JdbcSchema schema = null ;
            JdbcTable  table  = null ;
            JdbcColumn column = null ;

            while (tables.next())
                {
                final String tcname = tables.getString(JDBC_META_TABLE_CAT);
                final String tsname = tables.getString(JDBC_META_TABLE_SCHEM);
                final String ttname = tables.getString(JDBC_META_TABLE_NAME);
                final String tttype = tables.getString(JDBC_META_TABLE_TYPE);
                //log.debug("Found table [{}.{}.{}]", new Object[]{tcname, tsname, ttname});
                //
                // If the catalog name is null.
                if (tcname == null)
                    {
                    //log.debug("Catalog is null, whatever ..");
                    }
                //
                // If the schema name is null.
                if (tsname == null)
                    {
                    //log.debug("Schema is null, whatever ..");
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
                    //log.debug("Schema is on the ignore list, skipping ...");
                    continue;
                    }
                //
                // In SQLServer, include the catalog name.
                // (temp fix for ROE system)
                if (product == JdbcProductType.MSSQL)
                    {
                    schemaname = tcname + "." + tsname ;
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
                    //log.debug("Table is null, whatever ..");
                    }
                //
                // If the table name is not null.
                else {
                    log.debug("Processing table [{}][{}][{}]", new Object[]{tcname, tsname, ttname});
                    if (table == null)
                        {
                        column = null ;
                        create = true ;
                        table  = schema.tables().create(
                            ttname,
                            JdbcTable.JdbcTableType.match(
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
                                JdbcTable.JdbcTableType.match(
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
                                        JdbcTable.JdbcTableType.match(
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
                            final String ccname = columns.getString(JDBC_META_TABLE_CAT);
                            final String csname = columns.getString(JDBC_META_TABLE_SCHEM);
                            final String ctname = columns.getString(JDBC_META_TABLE_NAME);
                            final String colname = columns.getString(JDBC_META_COLUMN_NAME);
                            final int    coltype = columns.getInt(JDBC_META_COLUMN_TYPE_TYPE);
                            final int    colsize = columns.getInt(JDBC_META_COLUMN_SIZE);
                            log.debug("Processing column [{}][{}][{}][{}]", new Object[]{
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
                    catch (final SQLException ouch)
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
        catch (final SQLException ouch)
            {
            log.error("Exception reading database metadata [{}]", ouch.getMessage());
            throw translator.translate(
                "Reading database metadata",
                null,
                ouch
                );
            }
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
	public void ogsaid(final String ogsaid)
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
