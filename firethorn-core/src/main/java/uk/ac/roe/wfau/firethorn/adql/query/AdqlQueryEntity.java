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
package uk.ac.roe.wfau.firethorn.adql.query;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.adql.parser.AdqlParser;
import uk.ac.roe.wfau.firethorn.adql.parser.AdqlParserQuery;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.Level;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.State;
import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameFormatException;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.job.Job;
import uk.ac.roe.wfau.firethorn.job.JobEntity;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchemaEntity;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTableEntity;
import uk.ac.roe.wfau.firethorn.meta.base.BaseResource;
import uk.ac.roe.wfau.firethorn.meta.base.BaseResourceEntity;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcSchema;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTable;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTableEntity;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.data.DelaysClient;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.data.LimitsClient;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.data.PipelineClient;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.data.PipelineParam;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.data.PipelineResult;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.data.RownumClient;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.jdbc.JdbcInsertDataClient;

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
    name = AdqlQueryEntity.DB_TABLE_NAME,
    indexes={
        @Index(
            columnList=AdqlQueryEntity.DB_JDBC_TABLE_COL
            ),
        @Index(
            columnList=AdqlQueryEntity.DB_ADQL_TABLE_COL
            ),
        @Index(
            columnList=AdqlQueryEntity.DB_ADQL_SCHEMA_COL
            )
        }
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "AdqlQuery-select-all",
            query = "FROM AdqlQueryEntity ORDER BY name asc, ident desc"
            ),
        @NamedQuery(
            name  = "AdqlQuery-select-schema",
            query = "FROM AdqlQueryEntity WHERE schema= :schema ORDER BY name asc, ident desc"
            ),
        @NamedQuery(
            name  = "AdqlQuery-select-schema.name",
            query = "FROM AdqlQueryEntity WHERE ((schema = :schema) AND (name = :name)) ORDER BY name asc, ident desc"
            ),
        @NamedQuery(
            name  = "AdqlQuery-search-resource.text",
            query = "FROM AdqlQueryEntity WHERE ((schema = :schema) AND (name LIKE :text)) ORDER BY name asc, ident desc"
            )
        }
     )
public class AdqlQueryEntity
extends JobEntity
implements AdqlQuery, AdqlParserQuery
    {
    /**
     * Hibernate table mapping.
     *
     */
    protected static final String DB_TABLE_NAME   = DB_TABLE_PREFIX + "AdqlQueryEntity";
    protected static final String DB_JOIN_PREFIX  = DB_TABLE_PREFIX + "AdqlQueryJoinTo";

    /**
     * Hibernate column mapping.
     *
     */
    protected static final String DB_MODE_COL   = "mode";
    protected static final String DB_ADQL_COL   = "adql";
    protected static final String DB_OSQL_COL   = "osql";
    protected static final String DB_ROWID_COL  = "rowid";
    protected static final String DB_INPUT_COL  = "input";

    protected static final String DB_JDBC_TABLE_COL  = "jdbctable";
    protected static final String DB_ADQL_TABLE_COL  = "adqltable";
    protected static final String DB_ADQL_SCHEMA_COL = "adqlschema";

    /**
     * Hibernate column mapping.
     *
     */
    protected static final String DB_SYNTAX_STATE_COL   = "syntaxstate";
    protected static final String DB_SYNTAX_LEVEL_COL   = "syntaxlevel";
    protected static final String DB_SYNTAX_MESSAGE_COL = "syntaxmessage";

    /**
     * Hibernate column mapping.
     *
     */
    protected static final String DB_OGSADAI_DQP_COL      = "ogsadaidqp";
    protected static final String DB_OGSADAI_STORE_COL    = "ogsadaistore";
    protected static final String DB_OGSADAI_ENDPOINT_COL = "ogsadaiendpoint";

    /**
     * Param factory implementation.
     * @todo Move to a separate class.
     *
     */
    @Component
    public static class ParamFactory
    implements AdqlQuery.ParamFactory
        {
        @Value("${firethorn.adql.level}")
        private Level level ;

        @Value("${firethorn.ogsadai.dqp}")
        private String dqp ;

        @Value("${firethorn.ogsadai.store}")
        private String store ;

        @Value("${firethorn.ogsadai.endpoint}")
        private String endpoint ;

        @Override
        public AdqlQuery.QueryParam param()
            {
            return new AdqlQuery.QueryParam()
                {
                @Override
                public String endpoint()
                    {
                    return ParamFactory.this.endpoint;
                    }
                @Override
                public String dqp()
                    {
                    return ParamFactory.this.dqp;
                    }
                @Override
                public String store()
                    {
                    return ParamFactory.this.store;
                    }
                @Override
                public Level level()
                    {
                    return ParamFactory.this.level;
                    }
                };
            }

        @Override
        public QueryParam param(final Level change)
            {
            return new AdqlQuery.QueryParam()
                {
                @Override
                public String endpoint()
                    {
                    return ParamFactory.this.endpoint;
                    }
                @Override
                public String dqp()
                    {
                    return ParamFactory.this.dqp;
                    }
                @Override
                public String store()
                    {
                    return ParamFactory.this.store;
                    }
                @Override
                public Level level()
                    {
                    return change;
                    }
                };
            }
        }

    /**
     * Our local service implementations.
     *
     */
    @Component
    public static class Services
    implements AdqlQuery.Services
        {
        @Autowired
        public AdqlQuery.NameFactory names;
        @Override
        public AdqlQuery.NameFactory names()
            {
            return this.names;
            }

        @Autowired
        private AdqlQuery.LinkFactory links;
        @Override
        public AdqlQuery.LinkFactory links()
            {
            return this.links;
            }

        @Autowired
        private AdqlQuery.IdentFactory idents;
        @Override
        public AdqlQuery.IdentFactory idents()
            {
            return this.idents;
            }

        @Autowired
        private AdqlQuery.EntityFactory factory;
        @Override
        public AdqlQuery.EntityFactory factory()
            {
            return this.factory;
            }

        @Autowired
        private Job.Executor executor;
        @Override
        public Job.Executor executor()
            {
            return this.executor;
            }

        @Autowired
        private AdqlQuery.ParamFactory params;
        @Override
        public AdqlQuery.ParamFactory params()
            {
            return this.params;
            }
        }

    @Override
    public AdqlQuery.Services services()
        {
        return factories().queries();
        }

    /**
     * Resolver implementation.
     *
    @Repository
    public static class Resolver
    extends AbstractEntityFactory<AdqlQuery>
    implements AdqlQuery.Resolver
        {
        @Override
        public Class<?> etype()
            {
            return AdqlQueryEntity.class ;
            }

        @Autowired
        private AdqlQuery.IdentFactory idents;
        @Override
        public AdqlQuery.IdentFactory idents()
            {
            return this.idents;
            }
        @Autowired
        private AdqlQuery.LinkFactory links;
        @Override
        public AdqlQuery.LinkFactory links()
            {
            return this.links;
            }
        }
     */

    /**
     * Factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractEntityFactory<AdqlQuery>
    implements AdqlQuery.EntityFactory
        {
        @Override
        public Class<?> etype()
            {
            return AdqlQueryEntity.class ;
            }

        /*        
        @Override
        @CreateMethod
        public AdqlQuery create(final AdqlSchema schema, final String input)
        throws QueryProcessingException
            {
            return create(
                schema,
                input,
                null,
                names().name()
                );
            }
         */

        @Override
        @CreateMethod
        public AdqlQuery create(final AdqlSchema schema, final QueryParam params, final String input)
        throws QueryProcessingException
            {
            return create(
                schema,
                params,
                input,
                names().name()
                );
            }

        /*
        @Override
        @CreateMethod
        public AdqlQuery create(final AdqlSchema schema, final String input, final String rowid)
        throws QueryProcessingException
            {
            return create(
                schema,
                input,
                rowid,
                names().name()
                );
            }
        
        @Override
        @CreateMethod
        public AdqlQuery create(final AdqlSchema schema, final QueryParam params, final String input, final String name)
        throws QueryProcessingException
            {
            return create(
                schema,
                params,
                input,
                name
                );
            }
         *
         */

        @Override
        @CreateMethod
        public AdqlQuery create(final AdqlSchema schema, final QueryParam params, final String input, final String name)
        throws QueryProcessingException
            {
            log.debug("AdqlQuery create(AdqlSchema, QueryParam, String, String)");
            log.debug("  Schema [{}][{}]", schema.ident(), schema.name());
            log.debug("  Name   [{}]", name);
            //
            // Create the query entity.
            final AdqlQueryEntity entity = new AdqlQueryEntity(
                params,
                schema,
                input,
                names().name(
                    name
                    )
                );
            //
            // Make the query persistent.
            final AdqlQuery query = this.insert(
                entity
                );
            //
            // Create the query tables.
            // TODO make this automatic, triggered by tables().
            // TODO delete/create the tables when the input changes.
            entity.build();
            //
            // Return the entity.
            return query;
            }

        @Autowired
        public AdqlQuery.NameFactory names;
        public AdqlQuery.NameFactory names()
            {
            return this.names;
            }

        @Autowired
        private AdqlQuery.LinkFactory links;
        @Override
        public AdqlQuery.LinkFactory links()
            {
            return this.links;
            }

        @Autowired
        private AdqlQuery.IdentFactory idents;
        @Override
        public AdqlQuery.IdentFactory idents()
            {
            return this.idents;
            }

        @Autowired
        private AdqlQuery.ParamFactory params;
        @Override
        public AdqlQuery.ParamFactory params()
            {
            return this.params;
            }

        @Autowired
        private AdqlQuery.Limits.Factory limits ;
        @Override
        public AdqlQuery.Limits.Factory limits()
            {
            return this.limits;
            }

        @Override
        @SelectMethod
        public Iterable<AdqlQuery> select()
            {
            return super.list(
                super.query(
                    "AdqlQuery-select-all"
                    )
                );
            }

        @Override
        @SelectMethod
        public Iterable<AdqlQuery> select(final AdqlSchema schema)
            {
            return super.list(
                super.query(
                    "AdqlQuery-select-schema"
                    ).setEntity(
                        "schema",
                        schema
                        )
                );
            }

        @Override
        @SelectMethod
        public Iterable<AdqlQuery> search(final AdqlSchema schema, final String text)
            {
            return super.iterable(
                super.query(
                    "AdqlQuery-search-schema.text"
                    ).setEntity(
                        "schema",
                        schema
                    ).setString(
                        "text",
                        searchParam(
                            text
                            )
                        )
                );
            }
        }

    /**
     * Protected constructor, used by Hibernate.
     *
     */
    protected AdqlQueryEntity()
        {
        super();
        }

    /**
     * Protected constructor, used by factory.
     *
     */
    protected AdqlQueryEntity(final AdqlQuery.QueryParam params, final AdqlSchema schema, final String input, final String name)
    throws NameFormatException
        {
        super(
            name
            );
        this.schema = schema;
        this.delays = new AdqlQueryDelays();
        this.limits = new AdqlQueryLimits();
        this.params(
            params
            );
        this.input(
            input
            );
        }

    @Override
    public String link()
        {
        return factories().adql().queries().links().link(
            this
            );
        }

    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = AdqlSchemaEntity.class
        )
    @JoinColumn(
        name = DB_ADQL_SCHEMA_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private AdqlSchema schema;
    @Override
    public AdqlSchema schema()
        {
        return this.schema;
        }

    /*
    *
    * org.hsqldb.HsqlException: data exception: string data, right truncation
    *
    *    length=DB_INPUT_LEN,
    *    => query varchar(1000)
    *
    *    @org.hibernate.annotations.Type(
    *        type="org.hibernate.type.TextType"
    *        )
    *    => query longvarchar
    *
    *    @Lob
    *    => query clob
    *
    */
   @Type(
       type="org.hibernate.type.TextType"
       )
   @Column(
       name = DB_INPUT_COL,
       unique = false,
       nullable = true,
       updatable = true
       )
   private String input;
   @Override
   public String input()
       {
       return this.input;
       }
   @Override
   public void input(final String input)
       {
       this.input = input;
       prepare();
       }

   /**
    * The query mode (DIRECT|DISTRIBUTED).
    *
    */
   @Column(
        name = DB_MODE_COL,
        unique = false,
        nullable = false,
        updatable = true
        )
    @Enumerated(
        EnumType.STRING
        )
    private Mode mode = Mode.DIRECT ;
    @Override
    public Mode mode()
        {
        return this.mode;
        }

    /**
     * The processed ADQL query.
     *
     */
    @Type(
        type="org.hibernate.type.TextType"
        )
    @Column(
        name = DB_ADQL_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private String adql;
    @Override
    public String adql()
        {
        return this.adql;
        }
    @Override
    public void adql(final String adql)
        {
        this.adql = adql;
        }

    /**
     * The processed SQL query.
     *
     */
    @Type(
        type="org.hibernate.type.TextType"
        )
    @Column(
        name = DB_OSQL_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private String osql;
    @Override
    public String osql()
        {
        return this.osql;
        }
    @Override
    public void osql(final String ogsa)
        {
        this.osql = ogsa;
        }

    @Column(
        name = DB_SYNTAX_STATE_COL,
        unique = false,
        nullable = false,
        updatable = true
        )
    @Enumerated(
        EnumType.STRING
        )
    private Syntax.State syntax = Syntax.State.UNKNOWN ;

    @Column(
        name = DB_SYNTAX_LEVEL_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    @Enumerated(
        EnumType.STRING
        )
    private Syntax.Level level ;

    @Transient
    private final List<String> warnings = new ArrayList<String>();
    public void warning(final String warning)
        {
        warnings.add(
            warning
            );
        }

    @Type(
        type="org.hibernate.type.TextType"
        )
    @Column(
        name = DB_SYNTAX_MESSAGE_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private String message;

    @Column(
        name = DB_OGSADAI_DQP_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private String dqp ;

    @Column(
        name = DB_OGSADAI_STORE_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private String store ;

    @Column(
        name = DB_OGSADAI_ENDPOINT_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private String endpoint ;

    protected void params(final AdqlQuery.QueryParam params)
        {
        this.dqp      = params.dqp();
        this.store    = params.store();
        this.level    = params.level();
        this.endpoint = params.endpoint();
        }

    @Override
    public AdqlQuery.QueryParam params()
        {
        return new AdqlQuery.QueryParam()
            {
            @Override
            public String endpoint()
                {
                return AdqlQueryEntity.this.endpoint;
                }

            @Override
            public String dqp()
                {
                return AdqlQueryEntity.this.dqp;
                }

            @Override
            public String store()
                {
                return AdqlQueryEntity.this.store;
                }

            @Override
            public AdqlQuery.Syntax.Level level()
                {
                return AdqlQueryEntity.this.level;
                }
            };
        }

    @Override
    public Syntax syntax()
        {
        return new Syntax()
            {
            @Override
            public State state()
                {
                return AdqlQueryEntity.this.syntax;
                }
            @Override
            public String message()
                {
                return AdqlQueryEntity.this.message;
                }
            @Override
            public String friendly()
                {
                return AdqlQueryEntity.this.message;
                }
            @Override
            public Level level()
                {
                return AdqlQueryEntity.this.level;
                }
            @Override
            public void level(final Level level)
                {
                AdqlQueryEntity.this.level = level;
                }
            @Override
            public Iterable<String> warnings()
                {
                return AdqlQueryEntity.this.warnings;
                }
            };
        }

    @Override
    public void syntax(final Syntax.State syntax)
        {
        syntax(
            syntax,
            null
            );
        }

    @Override
    public void syntax(final Syntax.State syntax, final String message)
        {
        this.syntax  = syntax;
        this.message = message;
        }

    /**
     * The set of AdqlColumns used by the query.
     *
    @ManyToMany(
        fetch   = FetchType.LAZY,
        cascade = CascadeType.ALL,
        targetEntity = AdqlColumnEntity.class
        )
    @JoinTable(
        name=DB_JOIN_NAME + "AdqlColumn",
        joinColumns = {
            @JoinColumn(
                name = "adqlquery",
                nullable = false,
                updatable = false
                )
            },
        inverseJoinColumns = {
            @JoinColumn(
                name = "adqlcolumn",
                nullable = false,
                updatable = false
                )
            }
        )
     */
    @Transient
    private final Set<AdqlColumn> columns = new HashSet<AdqlColumn>();
    @Override
    public Iterable<AdqlColumn> columns()
        {
        return this.columns;
        }

    /**
     * The set of BaseResources used by the query.
     * @Transient
     *
     */
    @ManyToMany(
        fetch   = FetchType.LAZY,
        cascade = CascadeType.ALL,
        targetEntity = BaseResourceEntity.class
        )
    @JoinTable(
        name=DB_JOIN_PREFIX + "BaseResource",
        joinColumns = {
            @JoinColumn(
                name = "adqlquery",
                nullable = false,
                updatable = false
                )
            },
        inverseJoinColumns = {
            @JoinColumn(
                name = "baseresource",
                nullable = false,
                updatable = false
                )
            }
        )
    private final Set<BaseResource<?>> resources = new HashSet<BaseResource<?>>();
    @Override
    public Iterable<BaseResource<?>> resources()
        {
        return this.resources;
        }

    @Override
    public BaseResource<?> primary()
        {
        if (this.mode == Mode.DIRECT)
            {
            final Iterator<BaseResource<?>> iter = this.resources.iterator();
            if (iter.hasNext())
                {
                return iter.next();
                }
            else {
                return null ;
                }
            }
        else {
            return this.schema.resource();
            }
        }

    @Override
    public String cleaned()
        {
        //
        // Trim leading/trailing spaces.
        String result = input().trim();

        //
        // Skip /* comments */
        final Pattern p1 = Pattern.compile(
            "/\\*.*?\\*/",
            Pattern.DOTALL
            );
        final Matcher m1 = p1.matcher(result);
        if (m1.find())
            {
            result = m1.replaceAll("");
            }

        //
        // Legacy SQLServer syntax
        if (this.level == Level.LEGACY)
            {
            //
            // Replace double '..'
            final Pattern p2 = Pattern.compile(
                "\\.\\.",
                Pattern.DOTALL
                );
            final Matcher m2 = p2.matcher(result);
            if (m2.find())
                {
                result = m2.replaceAll(".");
                warning("SQLServer '..' syntax is not required");
                }

            //
            // Replace 'AS distance'.
            final Pattern p3 = Pattern.compile(
                "[Aa][Ss] +[Dd][Ii][Ss][Tt][Aa][Nn][Cc][Ee]",
                Pattern.DOTALL
                );
            final Matcher m3 = p3.matcher(result);
            if (m3.find())
                {
                result = m3.replaceAll("AS dist");
                warning("DISTANCE is an ADQL reserved word");
                }
            }

        return result;

        }

    @Override
    public Status prepare()
        {
        log.debug("prepare()");
        log.debug(" ident [{}]", ident());

        if ((status() == Status.EDITING) || (status() == Status.READY))
            {
            if (this.input == null)
                {
                return status(
                    Status.EDITING
                    );
                }
            else if (this.input.trim().length() == 0)
                {
                return status(
                    Status.EDITING
                    );
                }
            else {
                //
                // Log the start time.
                this.timings().adqlstart();
                //
                // Process as a direct query.
                // TODO - The parsers should be part of the resource/schema.
                // TODO - Either plain ADQL or SQLServer dialect SQL.
                final AdqlParser direct = this.factories().adql().parsers().create(
                    Mode.DIRECT,
                    this.schema
                    );
                direct.process(
                    this
                    );
                //
                // If the query uses multiple resources, re-process as a distributed query.
                if (this.resources.size() > 1)
                    {
                    log.debug("Query uses multiple resources");
                    log.debug("----");
                    for (final BaseResource<?> resource : this.resources())
                        {
                        log.debug("Resource [{}]", resource);
                        }
                    log.debug("----");
                    //
                    // Process as a distributed query.
                    // TODO - Either plain ADQL or DQP dialect SQL.
                    final AdqlParser distrib = this.factories().adql().parsers().create(
                        Mode.DISTRIBUTED,
                        this.schema
                        );
                    distrib.process(
                        this
                        );
                    }
                //
                // Log the end time.
                this.timings().adqldone();
                //
                // Update the status.
                if (syntax().state() == Syntax.State.VALID)
                    {
                    return status(
                        Status.READY
                        );
                    }
                else {
                    return status(
                        Status.EDITING
                        );
                    }
                }
            }
        else {
            return Status.ERROR;
            }
        }

    @Override
    public void reset(final Mode mode)
        {
        this.adql = null ;
        this.osql = null ;
        this.mode = mode ;
        this.syntax(
            Syntax.State.UNKNOWN
            );
        this.fields.clear();
        this.columns.clear();
        this.tables.clear();
        this.resources.clear();
        }

    @Transient
    private final Set<AdqlTable> tables = new HashSet<AdqlTable>();
    @Override
    public Iterable<AdqlTable> tables()
        {
        return this.tables;
        }

    @Override
    public void add(final AdqlColumn column)
        {
        this.columns.add(
            column
            );
        this.add(
            column.table()
            );
        }

    @Override
    public void add(final AdqlTable table)
        {
        this.tables.add(
            table
            );
        this.add(
            table.root().resource()
            );
        }

    protected void add(final BaseResource<?> resource)
        {
        this.resources.add(
            resource
            );
        }


    @Override
    public Status execute()
        {
        log.debug("execute()");
        log.debug("  AdqlQuery [{}]", ident());
        try {
            this.timings().querystart();

            Status result = services().executor().status(
                ident(),
                Status.RUNNING
                );

            if (result == Status.RUNNING)
                {
                log.debug("-- AdqlQuery running [{}]", ident());
                //
                // Log the start time.
                this.timings().ogsastart();
                try {
                
                    log.debug("-- AdqlQuery resolving [{}]", ident());
                    final AdqlQuery query = services().factory().select(
                            ident()
                            );
                    //
                    // Create our server client.
                    log.debug("-- Pipeline endpoint [{}]", params().endpoint());
                    final PipelineClient pipeline = new PipelineClient(
                        new URL(
                            params().endpoint()
                            )
                        );
                    log.debug("-- Pipeline [{}]", pipeline);
    
                    log.debug("-- AdqlQuery executing [{}]", ident());
                    log.debug("-- Mode     [{}]", query.mode());
                    log.debug("-- Store    [{}]", params().store());
                    log.debug("-- Endpoint [{}]", params().endpoint());
    
    
                    // TODO - Check for valid resource ident in prepare().
                    final String source = ((mode() == Mode.DIRECT) ? primary().meta().ogsa().id() : params().dqp());
                    log.debug("-- Source   [{}]", source);
                    final String tablename = query.results().jdbc().namebuilder().toString() ;
                    log.debug("-- Table    [{}]", tablename);
    
                    final PipelineResult frog = pipeline.execute(
                        new PipelineParam()
                            {
                            @Override
                            public String source()
                                {
                                return source;
                                }
                            @Override
                            public String query()
                                {
                                return query.osql();
                                }
                            @Override
                            public JdbcInsertDataClient.Param insert()
                                {
                                return new JdbcInsertDataClient.Param()
                                    {
                                    @Override
                                    public String store()
                                        {
                                        return params().store();
                                        }

                                    @Override
                                    public String table()
                                        {
                                        return tablename;
                                        }

                                    @Override
                                    public Integer first()
                                        {
                                        return null;
                                        }

                                    @Override
                                    public Integer block()
                                        {
                                        return null;
                                        }
                                    };
                                }
                            @Override
                            public DelaysClient.Param delays()
                                {
                                return query.delays();
                                }
                            @Override
                            public RownumClient.Param rows()
                                {
                                return null;
                                }
                            @Override
                            public LimitsClient.Param limits()
                                {
                                return factories().adql().queries().limits().runtime(
                                    query.limits()
                                    );
                                }
                            }
                        );
    
                    if (frog != null)
                        {
                        log.debug("-- AdqlQuery result [{}][{}]", ident(), frog.result());
    
                        if (frog.result() == PipelineResult.Result.COMPLETED)
                            {
                            result = services().executor().status(
                                ident(),
                                Status.COMPLETED
                                );
                            }
                        else {
                            result = services().executor().status(
                                ident(),
                                Status.FAILED
                                );
                            }
                        }
                    else {
                        log.debug("-- AdqlQuery [{}] NULL results", ident());
                        result = services().executor().status(
                            ident(),
                            Status.FAILED
                            );
                        }
                    }
                catch (final EntityNotFoundException ouch)
                    {
                    log.debug("Unable to find query [{}][{}]", ident(), ouch.getMessage());
                    result = Status.ERROR;
                    }
                catch (final Exception ouch)
                    {
                    log.debug("Unable to execute query [{}][{}][{}]", ident(), ouch.getClass().getName(), ouch.getMessage());
                    result = services().executor().status(
                        ident(),
                        Status.FAILED
                        );
                    }
                finally {
                    //
                    // Log the done time.
                    this.timings().ogsadone();
                    }
                }
            return result ;
            }
        finally
            {
            this.timings().querydone();
            }
        }

    @Transient
    private final Map<String, SelectField> fields = new LinkedHashMap<String, SelectField>();
    @Override
    public Iterable<SelectField > fields()
        {
        return this.fields.values();
        }

    @Override
    public void add(final SelectField field)
    throws DuplicateFieldException
        {
        log.debug("add(SelectField)");
        log.debug("  Name [{}]", field.name());
        log.debug("  Size [{}]", field.arraysize());
        log.debug("  Type [{}]", field.type());
        //
        // Clean the name.
        final String name = field.name().trim().toLowerCase();
        //
        // Check for a duplicate.
        if (fields.containsKey(name))
            {
            throw new DuplicateFieldException(
                field
                );
            }
        //
        // Add the field.
        else {
            this.fields.put(
                name,
                field
                );
            }
        }

    /**
     * Our JDBC table.
     *
     */
    @OneToOne(
        fetch = FetchType.LAZY,
        targetEntity = JdbcTableEntity.class
        )
    @JoinColumn(
        name = DB_JDBC_TABLE_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private JdbcTable jdbctable;

    /**
     * Our ADQL table.
     *
     */
    @OneToOne(
        fetch = FetchType.LAZY,
        targetEntity = AdqlTableEntity.class
        )
    @JoinColumn(
        name = DB_ADQL_TABLE_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private AdqlTable adqltable;

    /**
     * Create our result tables.
     * @throws QueryProcessingException 
     *
     */
    protected void build()
    throws QueryProcessingException
        {
        log.debug("build()");

        // TODO Delete old tables ?

        if (this.syntax == State.VALID)
            {
            //
            // Log the start time.
            this.timings().jdbcstart();
            
            final Identity identity = this.owner();
            log.debug(" Identity [{}][{}]", identity.ident(), identity.name());

// TODO
// Global shared 'default' space.
            JdbcSchema space = identity.space(true);
            //
            // Create our tables.
//TODO
//Why does the query need to know where the JdbcTable is ?
            log.debug(" Identity space [{}][{}]", space.ident(), space.name());
            this.jdbctable = space.tables().create(
                this
                );
//TODO
// Should this be FULL or THIN ?
            this.adqltable = this.schema().tables().create(
                this
                );
            //
            // Log the end time.
            this.timings().jdbcdone();
            }

        else {
            // TODO Delete old tables ?
            }
        }

    /**
     * Our result tables.
     * TODO - Which external components need access to the JdbcTable ?
     *
     */
    @Override
    public Results results()
        {
        return new Results()
            {
            @Override
            @Deprecated
            public JdbcTable jdbc()
                {
                return AdqlQueryEntity.this.jdbctable ;
                }
            @Override
            @Deprecated
            public BaseTable<?,?> base()
                {
                return AdqlQueryEntity.this.jdbctable ;
                }
            @Override
            public AdqlTable adql()
                {
                return AdqlQueryEntity.this.adqltable;
                }
            };
        }

    @Embedded
    private AdqlQueryLimits limits;

    
    @Override
    public ModifiableLimits limits()
        {
        /*
         * Need to check for null.
         * "Hibernate considers (embedded) component to be NULL if all its properties are NULL (and vice versa)."
         * http://stackoverflow.com/a/1324391
         */
        if (this.limits == null)
            {
            this.limits = new AdqlQueryLimits();
            }
        return this.limits ;
        }

    @Override
    public void limits(final Limits limits)
        {
        this.limits = new AdqlQueryLimits(
            limits
            );
        }

    public void limits(final Long rows, final Long cells, final Long time)
        {
        this.limits = new AdqlQueryLimits(
            rows,
            cells,
            time
            );
        }

    @Embedded
    private AdqlQueryDelays delays;
    
    @Override
    public Delays delays()
        {
        /*
         * Need to check for null.
         * "Hibernate considers (embedded) component to be NULL if all its properties are NULL (and vice versa)."
         * http://stackoverflow.com/a/1324391
         */
        if (this.delays == null)
            {
            this.delays = new AdqlQueryDelays();
            }
        return this.delays ;
        }

    @Embedded
    private AdqlQueryTimings stats;
    
    @Override
    public AdqlQueryTimings timings()
        {
        /*
         * Need to check for null.
         * "Hibernate considers (embedded) component to be NULL if all its properties are NULL (and vice versa)."
         * http://stackoverflow.com/a/1324391
         */
        if (this.stats== null)
            {
            this.stats= new AdqlQueryTimings();
            }
        return this.stats;
        }
    }
