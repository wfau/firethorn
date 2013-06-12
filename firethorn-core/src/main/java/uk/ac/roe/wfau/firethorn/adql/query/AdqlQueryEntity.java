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
import java.util.List;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.Type;
import org.springframework.beans.factory.annotation.Value;         
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.adql.parser.AdqlParser;
import uk.ac.roe.wfau.firethorn.adql.parser.AdqlParserQuery;
import uk.ac.roe.wfau.firethorn.entity.AbstractFactory;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.NameFormatException;
import uk.ac.roe.wfau.firethorn.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.identity.Community;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.job.Job;
import uk.ac.roe.wfau.firethorn.job.JobEntity;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumnEntity;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchemaEntity;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTableEntity;
import uk.ac.roe.wfau.firethorn.meta.base.BaseResource;
import uk.ac.roe.wfau.firethorn.meta.base.BaseResourceEntity;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTable;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTableEntity;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.PipelineResult;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.StoredResultPipeline;

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
    name = AdqlQueryEntity.DB_TABLE_NAME
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
    protected static final String DB_JOIN_NAME  = "AdqlQueryJoin";
    protected static final String DB_TABLE_NAME = "AdqlQueryEntity";

    /**
     * Hibernate column mapping.
     *
     */
    protected static final String DB_MODE_COL   = "mode";
    protected static final String DB_ADQL_COL   = "adql";
    protected static final String DB_OSQL_COL   = "osql";
    protected static final String DB_INPUT_COL  = "input";
    protected static final String DB_STATUS_COL = "status";
    protected static final String DB_JDBC_TABLE_COL  = "jdbctable";
    protected static final String DB_ADQL_TABLE_COL  = "adqltable";
    protected static final String DB_ADQL_SCHEMA_COL = "adqlschema";

    /**
     * Hibernate column mapping.
     *
     */
    protected static final String DB_SYNTAX_STATE_COL   = "syntaxstate";
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
       @Value("${firethon.ogsadai.dqp}")
       private String dqp ;

       @Value("${firethon.ogsadai.store}")
       private String store ;

       @Value("${firethon.ogsadai.endpoint}")
       private String endpoint ;

        @Override
        public AdqlQuery.QueryParam current()
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
        private AdqlQuery.Resolver resolver;
        @Override
        public AdqlQuery.Resolver resolver()
            {
            return this.resolver;
            }

        @Autowired
        private AdqlQuery.Factory factory;
        @Override
        public AdqlQuery.Factory factory()
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
     */
    @Repository
    public static class Resolver
    extends AbstractFactory<AdqlQuery>
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

    /**
     * Factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<AdqlQuery>
    implements AdqlQuery.Factory
        {
        @Override
        public Class<?> etype()
            {
            return AdqlQueryEntity.class ;
            }

        @Override
        @CreateEntityMethod
        public AdqlQuery create(final AdqlSchema schema, final String input)
            {
            return create(
                schema,
                input,
                names().name()
                );
            }

        @Override
        @CreateEntityMethod
        public AdqlQuery create(final AdqlSchema schema, final String input, final String name)
            {
            log.debug("AdqlQuery create(AdqlSchema, String, String)");
            log.debug("  Schema [{}][{}]", schema.ident(), schema.name());
            log.debug("  Name   [{}]", name);

            //
            // Create the query entity.
            final AdqlQueryEntity entity = new AdqlQueryEntity(
                factories().identities().current(),
                params.current(),
                schema,
                names().name(
                    name
                    ),
                input
                );
            //
            // Make the query persistent.
            final AdqlQuery query = this.insert(
                entity
                );
            //
            // Create the query tables.
            // TODO make this automatic, triggered by tables().
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
        
        @Override
        @SelectEntityMethod
        public Iterable<AdqlQuery> select()
            {
            return super.list(
                super.query(
                    "AdqlQuery-select-all"
                    )
                );
            }

        @Override
        @SelectEntityMethod
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
        @SelectEntityMethod
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
        }

    /**
     * Protected constructor, used by factory.
     *
     */
    protected AdqlQueryEntity(final Identity owner, final AdqlQuery.QueryParam params, final AdqlSchema schema, final String name, final String input)
    throws NameFormatException
        {
        super(
            owner,
            name
            );
        this.schema = schema;
        this.input(
            input
            );
        this.params(
            params
            );
        }

    @Override
    public String link()
        {
        return factories().adql().queries().links().link(
            this
            );
        }

    @Index(
        name=DB_TABLE_NAME + "IndexBySchema"
        )
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

    protected void params(AdqlQuery.QueryParam params)
        {
        this.dqp      = params.dqp();
        this.store    = params.store();
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
     */
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
    private final Set<AdqlColumn> columns = new HashSet<AdqlColumn>();
    @Override
    public Iterable<AdqlColumn> columns()
        {
        return this.columns;
        }

    /**
     * The set of BaseResources used by the query.
     *
     */
    @ManyToMany(
        fetch   = FetchType.LAZY,
        cascade = CascadeType.ALL,
        targetEntity = BaseResourceEntity.class
        )
    @JoinTable(
        name=DB_JOIN_NAME + "BaseResource",
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
            else {
                //
                // Create the two query parsers.
                // TODO - The parsers should be part of the resource/schema.
                // TODO - Either plain ADQL or SQLServer dialect SQL.
                final AdqlParser direct = this.factories().adql().parsers().create(
                    Mode.DIRECT,
                    this.schema
                    );
                // TODO - Either plain ADQL or DQP dialect SQL.
                final AdqlParser distrib = this.factories().adql().parsers().create(
                    Mode.DISTRIBUTED,
                    this.schema
                    );
                //
                // Process as a direct query.
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
                    distrib.process(
                        this
                        );
                    }
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

        Status result = services().executor().status(
            ident(),
            Status.RUNNING
            );

        if (result == Status.RUNNING)
            {
            log.debug("-- AdqlQuery running [{}]", ident());
            try {
                log.debug("-- AdqlQuery resolving [{}]", ident());
                final AdqlQuery query = services().resolver().select(
                        ident()
                        );

                //
                // Create our server client.
                log.debug("-- Pipeline endpoint [{}]", endpoint);
                final StoredResultPipeline pipeline = new StoredResultPipeline(
                    new URL(
                        params().endpoint()
                        )
                    );
                log.debug("-- Pipeline [{}]", pipeline);

                //
                // Execute the pipleline.

                // TODO - Check for valid resource ident in prepare().
                final String target = ((mode() == Mode.DIRECT) ? primary().ogsaid() : params().dqp());
                final String tablename = query.results().jdbc().fullname().toString() ;

                log.debug("-- AdqlQuery executing [{}]", ident());
                log.debug("-- Mode     [{}]", query.mode());
                log.debug("-- Table    [{}]", tablename);
                log.debug("-- Store    [{}]", params().store());
                log.debug("-- Target   [{}]", target);
                log.debug("-- Endpoint [{}]", params().endpoint());

                final PipelineResult frog = pipeline.execute(
                    target,
                    params().store(),
                    tablename,
                    query.osql()
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
            catch (final NotFoundException ouch)
                {
                log.debug("Unable to find query [{}][{}]", ident(), ouch.getMessage());
                result = Status.ERROR;
                }
            catch (final Exception ouch)
                {
                log.debug("Unable to execute query [{}][{}]", ident(), ouch.getMessage());
                result = services().executor().status(
                    ident(),
                    Status.FAILED
                    );
                }
            }
        return result ;
        }

    @Transient
    private final List<SelectField > fields = new ArrayList<SelectField>();
    @Override
    public Iterable<SelectField > fields()
        {
        return this.fields;
        }

    @Override
    public void add(final SelectField field)
        {
        log.debug("add(SelectField)");
        log.debug("  Name [{}]", field.name());
        log.debug("  Size [{}]", field.length());
        log.debug("  Type [{}]", field.type());
        this.fields.add(
            field
            );
        }

    /**
     * Our JDBC table.
     *
     */
    @Index(
        name=DB_TABLE_NAME + "IndexByJdbcTable"
        )
    @OneToOne(
        fetch = FetchType.LAZY,
        targetEntity = JdbcTableEntity.class
        )
    @JoinColumn(
        name = DB_JDBC_TABLE_COL,
        unique = true,
        nullable = true,
        updatable = true
        )
    private JdbcTable jdbctable;

    /**
     * Our ADQL table.
     *
     */
    @Index(
        name=DB_TABLE_NAME + "IndexByAdqlTable"
        )
    @OneToOne(
        fetch = FetchType.LAZY,
        targetEntity = AdqlTableEntity.class
        )
    @JoinColumn(
        name = DB_ADQL_TABLE_COL,
        unique = true,
        nullable = true,
        updatable = true
        )
    private AdqlTable adqltable;

    /**
     *  Create our result tables.
     *
     */
    protected void build()
        {
        log.debug("create(JdbcSchema)");

        Identity identity = this.owner(); 
        log.debug(" Identity [{}][{}]", identity.ident(), identity.name());

        //
        // Locate our JDBC schema.
        // TODO Move this to a separate resolver/factory.
        if (identity.space() == null)
            {
            log.debug(" Identity space [null][null]");

            Community community = identity.community();
            log.debug(" Community [{}][{}]", community.ident(), community.name());

            JdbcResource userdata = community.space(); 
            if (userdata == null)
                {
                log.debug(" Community space [null][null]");
                userdata = factories().jdbc().resources().userdata() ;
                if (userdata  == null)
                    {
                    log.debug(" Userdata resource [null][null]");
                    }
                else {
                    log.debug(" Userdata resource [{}][{}]", userdata.ident(), userdata.name());
                    community.space(
                        userdata 
                        );                
                    }
                }

            if (userdata != null)
                {
                log.debug(" Community space [{}][{}]", userdata.ident(), userdata.name());
                log.debug(" Creating new user space");

//
// TODO Create a new schema for this user.
// Requires CreateSchema.                
                identity.space(
                    userdata.schemas().select(
                        userdata.connection().type().defschema()
                        )
                    );
                }
            }
        //
        // Create our tables.
        if (identity.space() != null)
            {
            log.debug(" Owner space [{}][{}]", identity.space().ident(), identity.space().name());
            this.jdbctable = identity.space().tables().create(
                this
                );
            this.adqltable = this.schema().tables().create(
                this
                );
            }
// Legacy no-owner fallback.
        else {
            log.error("-- NO OWNER");
/*
            this.jdbctable = services().builder().create(
                legacy,
                this
                );
 */                
            }


//TODO
//Why does the query need to know where the JdbcTable is ?
        
        }
    
    /**
     * Our result tables.
     * TODO - Which external components need access to the JdbcTable ? 
     *
     */
    @Override
    public Results results()
        {
        return new Results(){
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
    }
