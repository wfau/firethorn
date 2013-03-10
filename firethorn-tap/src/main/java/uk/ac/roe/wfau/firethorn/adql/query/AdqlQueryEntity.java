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
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Future;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.adql.parser.AdqlParser;
import uk.ac.roe.wfau.firethorn.adql.parser.AdqlParserQuery;
import uk.ac.roe.wfau.firethorn.entity.AbstractEntity;
import uk.ac.roe.wfau.firethorn.entity.AbstractFactory;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.UpdateAtomicMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.NameFormatException;
import uk.ac.roe.wfau.firethorn.job.Job;
import uk.ac.roe.wfau.firethorn.job.Job.Executor.Executable;
import uk.ac.roe.wfau.firethorn.job.JobEntity;
import uk.ac.roe.wfau.firethorn.job.Job.Status;
import uk.ac.roe.wfau.firethorn.job.test.TestJob;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumnEntity;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResourceEntity;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.base.BaseResource;
import uk.ac.roe.wfau.firethorn.meta.base.BaseResourceEntity;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.PipelineResult;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.SimpleClient;
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
            name  = "AdqlQuery-select-resource",
            query = "FROM AdqlQueryEntity WHERE resource = :resource ORDER BY name asc, ident desc"
            ),
        @NamedQuery(
            name  = "AdqlQuery-select-resource.name",
            query = "FROM AdqlQueryEntity WHERE ((resource = :resource) AND (name = :name)) ORDER BY name asc, ident desc"
            ),
        @NamedQuery(
            name  = "AdqlQuery-search-resource.text",
            query = "FROM AdqlQueryEntity WHERE ((resource = :resource) AND (name LIKE :text)) ORDER BY name asc, ident desc"
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
    protected static final String DB_MODE_COL     = "mode";
    protected static final String DB_ADQL_COL     = "adql";
    protected static final String DB_OSQL_COL     = "osql";
    protected static final String DB_INPUT_COL    = "input";
    protected static final String DB_STATUS_COL   = "status";
    protected static final String DB_RESOURCE_COL = "resource";

    /**
     * Hibernate column mapping.
     * 
     */
    protected static final String DB_SYNTAX_STATUS_COL  = "syntaxstatus";
    protected static final String DB_SYNTAX_MESSAGE_COL = "syntaxmessage";

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
        private AdqlQuery.Executor executor;
        @Override
        public AdqlQuery.Executor executor()
            {
            return this.executor;
            }
        }

    //@Override
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
        public AdqlQuery create(final AdqlResource resource, final String input)
            {
            return this.insert(
                new AdqlQueryEntity(
                    resource,
                    names().name(),
                    input
                    )
                );
            }

        @Override
        @CreateEntityMethod
        public AdqlQuery create(final AdqlResource resource, final String name, final String input)
            {
            return this.insert(
                new AdqlQueryEntity(
                    resource,
                    names().name(
                        name
                        ),
                    input
                    )
                );
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
        public Iterable<AdqlQuery> select(AdqlResource resource)
            {
            return super.list(
                super.query(
                    "AdqlQuery-select-resource"
                    ).setEntity(
                        "resource",
                        resource
                        )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<AdqlQuery> search(AdqlResource resource, String text)
            {
            return super.iterable(
                super.query(
                    "AdqlQuery-search-resource.text"
                    ).setEntity(
                        "resource",
                        resource
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
    protected AdqlQueryEntity(final AdqlResource resource, final String name, final String input)
    throws NameFormatException
        {
        super(
            name
            );
        this.resource = resource;
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

    @Index(
        name=DB_TABLE_NAME + "IndexByResource"
        )
    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = AdqlResourceEntity.class
        )
    @JoinColumn(
        name = DB_RESOURCE_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private AdqlResource resource;
    @Override
    public AdqlResource resource()
        {
        return this.resource;
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
   public void input(String input)
       {
       this.input = input;
       parse();
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
        name = DB_SYNTAX_STATUS_COL,
        unique = false,
        nullable = false,
        updatable = true
        )
    @Enumerated(
        EnumType.STRING
        )
    private Syntax.Status syntax = Syntax.Status.UNKNOWN ;

    @Column(
        name = DB_SYNTAX_MESSAGE_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private String message;

    @Override
    public Syntax syntax()
        {
        return new Syntax()
            {
            @Override
            public Status status()
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
    public void syntax(Syntax.Status syntax)
        {
        syntax(
            syntax,
            null
            );
        }

    @Override
    public void syntax(Syntax.Status syntax, String message)
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
    private final Set<BaseResource<?>> targets = new HashSet<BaseResource<?>>();
    @Override
    public Iterable<BaseResource<?>> targets()
        {
        return this.targets;
        }

    @Override
    public BaseResource<?> target()
        {
        if (this.mode == Mode.DIRECT)
            {
            Iterator<BaseResource<?>> iter = this.targets.iterator();
            if (iter.hasNext())
                {
                return iter.next();
                }
            else {
                return null ;
                }
            }
        else {
            return this.resource;
            }
        }

    /**
     * Parse the query and update our properties.
     *
    protected Status parse()
        {
        return parse(
            null
            );
        }
     */

    /**
     * Parse our input and update our properties.
     *
     */
    protected Status parse()
        {
        if (this.input == null)
            {
            return inner(
                Status.EDITING
                );
            }
        else {
            //
            // Create the two query parsers.
            // TODO - The parsers should be part of the resource/workspace.
            // TODO - We could re-use the same parser by using a ThreadLocal for the mode ...
            final AdqlParser direct = this.factories().adql().parsers().create(
                Mode.DIRECT,
                this.resource
                );
            final AdqlParser distrib = this.factories().adql().parsers().create(
                Mode.DISTRIBUTED,
                this.resource
                );
            //
            // Process as a direct query.
            direct.process(
                this
                );
            //
            // If the query uses multiple resources, re-process as a distributed query.
            if (this.targets.size() > 1)
                {
                log.debug("Query uses multiple resources");
                log.debug("----");
                for (BaseResource<?> target : this.targets())
                    {
                    log.debug("Resource [{}]", target);
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
            if (syntax().status() == Syntax.Status.VALID)
                {
                return inner(
                    Status.READY
                    );
                }
            else {
                return inner(
                    Status.EDITING
                    );
                }
            }
        }

    @Override
    public void reset(Mode mode)
        {
        this.adql = null ;
        this.osql = null ;
        this.mode = mode ;
        this.syntax(
            Syntax.Status.UNKNOWN
            );
        this.columns.clear();
        this.tables.clear();
        this.targets.clear();
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
        this.targets.add(
            resource
            );
        }

    @Override
    public Status prepare()
        {
        try {
            return this.services().executor().prepare(
                new Executable()
                    {
                    @Override
                    public Status execute()
                        {
                        log.debug("prepare.Executable.execute()");
                        log.debug("  AdqlQuery [{}][{}]", ident(), name());
                        if ((status() == Status.EDITING) || (status() == Status.READY))
                            {
                            return update(
                                parse()
                                );
                            }
                        else {
                            return Status.ERROR;
                            }
    
                        }
                    }
                );
            }
        catch (Exception ouch)
            {
            log.error("Failed to prepare query [{}][{}]", ident(), ouch.getMessage());
            return Status.ERROR;
            }
        }

    /*
    //@Override
    public Status prepare(final String input)
        {
        return this.services().executor().prepare(
            new Executable()
                {
                @Override
                public Status execute()
                    {
                    return parse(
                        input
                        );
                    }
                }
            );
       }
    */

    public static final String endpoint  = "http://localhost:8081/albert/services" ;
    public static final String dqpname   = "testdqp" ;
    public static final String storename = "userdata" ;
    
    @Override
    public Future<Status> execute()
        {
        try {
            return services().executor().execute(
                new Executable()
                    {
                    @Override
                    public Status execute()
                        {
                        log.debug("execute.Executable.execute()");
                        log.debug("  AdqlQuery [{}][{}]", ident(), name());
    
                        Status result = prepare();
                        if (result != Status.READY)
                            {
                            log.error("-- AdqlQuery not ready [{}][{}]", ident(), status());
                            }
                        else {
                            log.debug("-- AdqlQuery starting[{}]", ident());
                            result = update(
                                Status.RUNNING
                                );
                            if (result == Status.RUNNING)
                                {
                                log.debug("-- AdqlQuery running [{}]", ident());
                                try {
                                    //
                                    // Create our server client.
                                    StoredResultPipeline pipeline = new StoredResultPipeline(
                                        new URL(
                                            endpoint
                                            )
                                        );
                                    //
                                    // Executethe pipleline.

                                    String tablename = "Q" + ident().toString() + "xxxx" ;
                                    
                                    PipelineResult frog = pipeline.execute(
                                        dqpname,
                                        storename,
                                        tablename,
                                        osql()
                                        );

                                    if (frog != null)
                                        {
                                        log.debug("-- AdqlQuery result [{}][{}]", ident(), frog.result());
                                        }
                                    else {
                                        log.debug("-- AdqlQuery NULL results");
                                        }
                                    
                                    log.debug("-- AdqlQuery finishing [{}][{}]", ident(), status());
                                    result = update(
                                        Status.COMPLETED
                                        );
                                    }
                                catch (Exception ouch)
                                    {
                                    log.debug("-- AdqlQuery failed [{}][{}]", ident(), ouch.getMessage());
                                    result = update(
                                        Status.FAILED
                                        );
                                    }
                                }
                            }
                        return result ;
                        }
                    }
                );
            }
        catch (Exception ouch)
            {
            log.error("Failed to execute job [{}][{}]", ident(), ouch.getMessage());
            return new AsyncResult<Status>(
                Status.ERROR
                );
            }
        }
    }
