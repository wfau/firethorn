/*
 *  Copyright (C) 2015 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.blue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.Session;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.adql.parser.AdqlParser;
import uk.ac.roe.wfau.firethorn.adql.parser.AdqlParserQuery;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Delays;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Limits;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Mode;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.SelectField;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.Level;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.State;
import uk.ac.roe.wfau.firethorn.blue.BlueTask.TaskState;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryDelays;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryLimits;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryTimings;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.UpdateMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.hibernate.HibernateConvertException;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn.AdqlType;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResourceEntity;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTableEntity;
import uk.ac.roe.wfau.firethorn.meta.base.BaseResource;
import uk.ac.roe.wfau.firethorn.meta.base.BaseResourceEntity;
import uk.ac.roe.wfau.firethorn.meta.base.TreeComponent.CopyDepth;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcColumn;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcColumn.JdbcType;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcSchema;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTable;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTableEntity;
import uk.ac.roe.wfau.firethorn.meta.ogsa.OgsaBaseResource;
import uk.ac.roe.wfau.firethorn.meta.ogsa.OgsaService;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.blue.BlueWorkflow;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.blue.BlueWorkflowClient;
import uk.ac.roe.wfau.firethorn.spring.Context;

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
   name = BlueQueryEntity.DB_TABLE_NAME
   )
@NamedQueries(
       {
       @NamedQuery(
           name  = "BlueQuery-select",
           query = "FROM BlueQueryEntity ORDER BY ident asc"
           ),
       @NamedQuery(
           name  = "BlueQuery-select-resource",
           query = "FROM BlueQueryEntity WHERE resource = :resource ORDER BY ident asc"
           ),
       }
   )
public class BlueQueryEntity
extends BlueTaskEntity<BlueQuery>
implements BlueQuery
    {
    /**
     * Hibernate table mapping, {@value}.
     *
     */
    protected static final String DB_TABLE_NAME = DB_TABLE_PREFIX + "BlueQueryEntity";

    /**
     * Hibernate table mapping, {@value}.
     *
     */
    protected static final String DB_JOIN_PREFIX  = DB_TABLE_NAME + "JoinTo";

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_OGSA_MODE_COL   = "ogsamode";

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_ADQL_INPUT_COL  = "adqlinput";

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_ADQL_QUERY_COL   = "adqlquery";

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_OSQL_QUERY_COL   = "osqlquery";

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_JDBC_TABLE_COL  = "jdbctable";

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_ADQL_TABLE_COL    = "adqltable";
    protected static final String DB_ADQL_SCHEMA_COL   = "adqlschema";
    protected static final String DB_ADQL_RESOURCE_COL = "adqlresource";

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_SYNTAX_STATE_COL   = "syntaxstate";
    protected static final String DB_SYNTAX_LEVEL_COL   = "syntaxlevel";
    protected static final String DB_SYNTAX_MESSAGE_COL = "syntaxmessage";

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_RESULT_COUNT_COL = "resultrowcount";

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_RESULT_STATUS_COL = "resultstatus";

    /**
     * {@link BlueQuery.TaskRunner} implementation.
     * 
     */
    @Component
    public static class TaskRunner
    extends BlueTaskEntity.TaskRunner<BlueQuery>
    implements BlueQuery.TaskRunner
    	{
    	}

    /**
     * {@link BlueQuery.TaskRunner.Creator} implementation.
     * 
     */
    public abstract static class Creator
    extends BlueTaskEntity.Creator<BlueQuery>
    implements BlueQuery.TaskRunner.Creator
        {
        /**
         * Public constructor.
         *
         */
        public Creator()
            {
            super();
            }
        }

    /**
     * {@link BlueQuery.TaskRunner.Updator} implementation.
     * 
     */
    public abstract static class Updator
    extends BlueTaskEntity.Updator<BlueQuery>
    implements BlueQuery.TaskRunner.Updator 
        {
        /**
         * Public constructor.
         *
         */
        public Updator(final BlueQuery initial)
            {
            super(initial);
            }
        }
    
    /**
     * {@link BlueQuery.EntityFactory} implementation.
     * 
     */
    @Repository
    public static class EntityFactory
    extends BlueTaskEntity.EntityFactory<BlueQuery>
    implements BlueQuery.EntityFactory
        {
        @Override
        public Class<?> etype()
            {
            return BlueQueryEntity.class;
            }

        @Override
        @CreateMethod
        public BlueQuery create(final AdqlResource source, final String input, final BlueQuery.TaskState next, final Long wait)
        throws InvalidRequestException, InternalServerErrorException
            {
            return create(
                source,
                input,
                null,
                null,
                next,
                wait
                );
            }

        @Override
        @CreateMethod
        public BlueQuery create(final AdqlResource source, final String input, final AdqlQuery.Limits limits, final BlueQuery.TaskState next, final Long wait)
        throws InvalidRequestException, InternalServerErrorException
            {
            return create(
                source,
                input,
                limits,
                null,
                next,
                wait
                );
            }

        @Override
        @CreateMethod
        public BlueQuery create(final AdqlResource source, final String input, final AdqlQuery.Limits limits, final AdqlQuery.Delays delays, final BlueQuery.TaskState next, final Long wait)
        throws InvalidRequestException, InternalServerErrorException
            {
            log.debug("create(AdqlResource, String, Limits, Delays, TaskState, Long)");
            log.debug("  state [{}]", next);
            log.debug("  wait  [{}]", wait);

/*
 * Reason for doing this inside a new Thread is to force a new Hibernate session.
 * Ensuring that the new Entity is committed to the database when the method returns.
 * This is the first time we have had to do this .. because we want to do other things to the entity after it has been created.
 * 
 * Problem - anything relying on ThreadLocal, like current Operation will fail.
 * Solution - modify thread() to pass in Operation context.
 *             
 */

            final Identity outer = services().contexts().current().oper().identities().primary();
            log.debug("Outer    [{}][{}]", outer.ident(), outer.name());
            
            log.debug("Creating BlueQuery");
            final BlueQuery query = services().runner().thread(
                new BlueQuery.TaskRunner.Creator()
                    {
                    @Override
                    public BlueQuery create()
                    throws InvalidStateTransitionException, HibernateConvertException
                        {
                        log.debug("create(");
                        log.debug("Creating query task");

                        final Identity inner = outer.rebase();
                        log.debug("Inner    [{}][{}]", inner.ident(), inner.name());
                        
                        return insert(
                    		new BlueQueryEntity(
                				inner,
                				source,
                				input,
                				limits,
                				delays
                				)
                    		);
                        }
                    }
                );

            log.debug("Converting BlueQuery");
            final BlueQuery result = query.rebase();
            
            if (next != null)
	            {
	            log.debug("Advancing BlueQuery");
	            result.advance(
	        		null,
	        		next,
	        		wait
	        		);
	            }

            log.debug("Returning BlueQuery");
            return result;
            }

        @Override
        @SelectMethod
        public BlueQuery select(final Identifier ident, final TaskState prev, final TaskState next, final Long wait)
        throws IdentifierNotFoundException
            {
            log.debug("select(Identifier , TaskStatus, TaskStatus, Long)");
            log.debug("  ident [{}]", ident);
            log.debug("  prev  [{}]", next);
            log.debug("  next  [{}]", next);
            log.debug("  wait  [{}]", wait);

            final BlueQuery query = services.entities().select(
                ident
                );
            //
            // This will block the current Thread.
            if ((prev != null) || (next != null) || (wait != null))
                {
                query.waitfor(
                    prev,
                    next,
                    wait
                    );
                }
            return query ;
            }

        @Override
        @UpdateMethod
        public BlueQuery update(final Identifier ident, final String input, final TaskState prev, final TaskState next, final Long wait)
        throws IdentifierNotFoundException, InvalidStateRequestException
            {
            return update(
                ident,
                input,
                null,
                null,
                prev,
                next,
                wait
                );
            }

        @Override
        @UpdateMethod
        public BlueQuery update(final Identifier ident, final String input, final AdqlQuery.Limits limits, final TaskState prev, final TaskState next, final Long wait)
        throws IdentifierNotFoundException, InvalidStateRequestException
            {
            return update(
                ident,
                input,
                limits,
                null,
                prev,
                next,
                wait
                );
            }

        @Override
        @UpdateMethod
        public BlueQuery update(final Identifier ident, final String input, final AdqlQuery.Limits limits, final AdqlQuery.Delays delays, final TaskState prev, final TaskState next, final Long wait)
        throws IdentifierNotFoundException, InvalidStateRequestException
            {
            log.debug("update(Identifier , String, TaskStatus, TaskStatus, Long)");
            log.debug("  ident [{}]", ident);
            log.debug("  prev  [{}]", prev);
            log.debug("  next  [{}]", next);
            log.debug("  wait  [{}]", wait);

            final BlueQuery query = select(
                ident
                );
            //
            // This gets run in a separate Thread.
            if ((input != null) || (limits != null) || (delays != null))
                {
                query.update(
                    input,
                    limits,
                    delays
                    );
                }

            if (next != null)
                {
                query.advance(
                    prev,
                    next,
                    wait
                    );
                }
            //
            // This will block the current Thread.
            else if ((prev != null) || (wait != null))
                {
                query.waitfor(
                    prev,
                    next,
                    wait
                    );
                }
            return query;
            }
        
        @Override
        @UpdateMethod
        public BlueQuery callback(final Identifier ident, final BlueQuery.Callback message)
        throws IdentifierNotFoundException, InvalidStateRequestException
            {
            log.debug("callback(Identifier, CallbackEvent)");
            log.debug("  ident [{}]", ident);
            log.debug("  next  [{}]", message.state());
            log.debug("  state [{}]", message.results().state());
            log.debug("  count [{}]", message.results().count());
            final BlueQuery query = select(
                ident
                );
            query.callback(
                message
                );
            return query;
            }
        
        @Override
        @SelectMethod
        public Iterable<BlueQuery> select()
            {
            return super.list(
                super.query(
                    "BlueQuery-select"
                    )
                );
            }

        @Override
        @SelectMethod
        public Iterable<BlueQuery> select(final AdqlResource resource)
            {
            return super.list(
                super.query(
                    "BlueQuery-select-resource"
                    ).setEntity(
                        "resource",
                        resource
                        )
                );
            }

        @Autowired
        private BlueQuery.EntityServices services;
        protected BlueQuery.EntityServices services()
            {
            return this.services;
            }
        }
    
    /**
     * {@link BlueQuery.EntityServices} implementation.
     * 
     */
    @Slf4j
    @Component
    public static class EntityServices
    implements BlueQuery.EntityServices
        {
        /**
         * Our singleton instance.
         * 
         */
        private static EntityServices instance ; 

        /**
         * Our singleton instance.
         * 
         */
        public static EntityServices instance()
            {
            return BlueQueryEntity.EntityServices.instance ;
            }

        /**
         * Protected constructor.
         * 
         */
        protected EntityServices()
            {
            }
        
        /**
         * Protected initialiser.
         * 
         */
        @PostConstruct
        protected void init()
            {
            log.debug("init()");
            if (BlueQueryEntity.EntityServices.instance == null)
                {
                BlueQueryEntity.EntityServices.instance = this ;
                }
            else {
                log.error("Setting instance more than once");
                throw new IllegalStateException(
                    "Setting instance more than once"
                    );
                }
            }
        
        @Autowired
        private BlueQuery.IdentFactory idents;
        @Override
        public BlueQuery.IdentFactory idents()
            {
            return this.idents;
            }

        @Autowired
        private BlueQuery.LinkFactory links;
        @Override
        public BlueQuery.LinkFactory links()
            {
            return this.links;
            }

        @Autowired
        private BlueQuery.EntityFactory entities;
        @Override
        public BlueQuery.EntityFactory entities()
            {
            return this.entities;
            }

        @Autowired
		private BlueQuery.TaskRunner runner;
		@Override
		public BlueQuery.TaskRunner runner()
			{
			return this.runner;
			}

        @Autowired
        private Context.Factory contexts;
		@Override
        public Context.Factory contexts()
            {
            return this.contexts;
            }

		@Autowired
        private AdqlQuery.Limits.Factory limits;
        @Override
        public AdqlQuery.Limits.Factory limits()
            {
            return this.limits;
            }

        @Autowired
        private AdqlQuery.Delays.Factory delays;
        @Override
        public AdqlQuery.Delays.Factory delays()
            {
            return this.delays;
            }
        }

    @Override
    protected BlueQuery.EntityFactory factory()
        {
        log.debug("factory()");
        return BlueQueryEntity.EntityServices.instance().entities() ; 
        }

    @Override
    protected BlueQuery.EntityServices services()
        {
        log.debug("services()");
        return BlueQueryEntity.EntityServices.instance() ; 
        }

	protected BlueQuery.TaskRunner runner()
		{
        log.debug("runner()");
        return BlueQueryEntity.EntityServices.instance().runner() ; 
		}
    
    /**
     * Protected constructor.
     * 
     */
    protected BlueQueryEntity()
        {
        }

    /**
     * Protected constructor.
     * 
    protected BlueQueryEntity(final Identity owner, final AdqlResource source, final String input)
    throws InvalidStateTransitionException
        {
        this(
            owner,
            source,
            input,
            null,
            null
            );
        }
     */

    /**
     * Protected constructor.
     * 
     */
    protected BlueQueryEntity(final Identity owner, final AdqlResource source, final String input, final AdqlQuery.Limits limits, final AdqlQuery.Delays delays)
    throws InvalidStateTransitionException
        {
        super(
    		owner
            );
        this.mode = Mode.AUTO;
        this.source = source;
        this.limits(
            limits
            );
        this.delays(
            delays
            );
		this.prepare(
		    input
		    );
        }

    @Override
    public String link()
        {
        return services().links().link(this);
        }

    @Override
    public String callback()
        {
        return services().links().callback(this);
        }
    
    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = AdqlResourceEntity.class
        )
    @JoinColumn(
        name = DB_ADQL_RESOURCE_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private AdqlResource source;
    @Override
    public AdqlResource source()
        {
        return this.source;
        }
    
    @Type(
        type="org.hibernate.type.TextType"
        )
    @Column(
        name = DB_ADQL_INPUT_COL,
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
    
    @Type(
        type="org.hibernate.type.TextType"
        )
    @Column(
        name = DB_ADQL_QUERY_COL,
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

    @Type(
        type="org.hibernate.type.TextType"
        )
    @Column(
        name = DB_OSQL_QUERY_COL,
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

    /**
     * The query mode (AUTO|DIRECT|DISTRIBUTED).
     * Should we have input mode and query mode ?
     *
     */
    @Column(
         name = DB_OGSA_MODE_COL,
         unique = false,
         nullable = false,
         updatable = true
         )
     @Enumerated(
         EnumType.STRING
         )
     private Mode mode ;
     @Override
     public Mode mode()
         {
         return this.mode;
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
     * The number of rows in the results.
     *
     */
    @Column(
         name = DB_RESULT_COUNT_COL,
         unique = false,
         nullable = true,
         updatable = true
         )
    private Long resultcount ;
/*
    protected void resultcount(final Long value)
        {
        log.debug("resultcount(Long)");
        log.debug("  value [{}]", value);
        if (value != null)
            {
            this.resultcount = value;
            }
        }
 */
    /**
     * The status of the results.
     *
     */
    @Basic(
        fetch = FetchType.EAGER
        )
    @Enumerated(
        EnumType.STRING
        )
    @Column(
        name = DB_RESULT_STATUS_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private ResultState resultstate = ResultState.NONE;

    /**
     * Update the ResultState and row count.
     * 
     */
    protected void transition(final ResultState next, final Long count)
    throws InvalidStateTransitionException
        {
        final ResultState prev = this.resultstate;
        log.debug("transition(ResultState, Long)");
        log.debug("  ident [{}]", ident());
        log.debug("  state [{}][{}]", prev.name(), (next != null) ? next.name() : null);
        log.debug("  count [{}]", count);
        
        if (count != null)
            {
            if (count > this.resultcount)
                {
                this.resultcount = count ;
                }
            }

        if (next == null)
            {
            log.debug("Null ResultState, no change");
            }
        else {
            if (prev == next)
                {
                log.debug("No-op state change [{}][{}]", prev.name(), next.name());
                }
            else {
                if (prev.active())
                    {
                    if (next.ordinal() >= prev.ordinal())
                        {
                        log.debug("Forward transition, state change accepted [{}][{}]", prev.name(), next.name());
                        this.resultstate = next;
                        }
                    else {
                        log.warn("Backward transition, state change rejected [{}][{}]", prev.name(), next.name());
                        }
                    }
                else {
                    log.debug("Modifying inactive ResultState, change rejected [{}][{}]", prev.name(), next.name());
                    }
                }
            }
        }
    
    @Override
    public Results results()
        {
        return new Results()
            {
            @Override
            public JdbcTable jdbc()
                {
                return BlueQueryEntity.this.jdbctable;
                }
            @Override
            public AdqlTable adql()
                {
                return BlueQueryEntity.this.adqltable;
                }
            @Override
            public Long rowcount()
                {
                return BlueQueryEntity.this.resultcount;
                }
            @Override
            public ResultState state()
                {
                return BlueQueryEntity.this.resultstate;
                }
            };
        }

    @Transient
    private final List<SelectField> fields = new ArrayList<SelectField>();
    
    @Override
    public Fields fields()
        {
        return new Fields()
            {
            @Override
            public Iterable<SelectField > select()
                {
                return BlueQueryEntity.this.fields;
                }
            }; 
        }
    
    @Transient
    private final Set<AdqlColumn> columns = new HashSet<AdqlColumn>();
    
    @Override
    public Columns columns()
        {
        return new Columns()
            {
            @Override
            public Iterable<AdqlColumn> select()
                {
                return BlueQueryEntity.this.columns;
                }
            };
        }

    @Transient
    private final Set<AdqlTable> tables = new HashSet<AdqlTable>();

    @Override
    public Tables tables()
        {
        return new Tables()
            {
            @Override
            public Iterable<AdqlTable> select()
                {
                return BlueQueryEntity.this.tables;
                }
            };
        }

    /**
     * The set of {@link BaseResource}s used by the query.
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
    public Resources resources()
        {
        return new Resources()
            {
            @Override
            public Iterable<BaseResource<?>> select()
                {
                return BlueQueryEntity.this.resources;
                }

            @Override
            public BaseResource<?> primary()
                {
                Iterator<BaseResource<?>> iter = BlueQueryEntity.this.resources.iterator();
                if (iter.hasNext())
                    {
                    return iter.next();
                    }
                else {
                    return null ;
                    }
                }
            };
        }
    
    /**
     * Wrap this {@link BlueQuery} as an {@link AdqlParserQuery}.
     * 
     */
    protected AdqlParserQuery parsable()
        {
        return new AdqlParserQuery()
            {
            @Override
            public String cleaned()
                {
                //
                // Get the original input.
                final String original = BlueQueryEntity.this.input();
                //
                // Trim leading/trailing spaces.
                String result = original.trim();
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

                // Legacy SQLServer syntax
                // TODO This should be part of the primary AqdlResource.
                // TODO Platform specific pre-processing attached to the AdqlResource.
                if (this.syntax().level() == Level.LEGACY)
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
            public void reset(Mode mode)
                {
                BlueQueryEntity.this.adql = null ;
                BlueQueryEntity.this.osql = null ;
                BlueQueryEntity.this.fields.clear();
                BlueQueryEntity.this.columns.clear();
                BlueQueryEntity.this.tables.clear();
                BlueQueryEntity.this.resources.clear();
                this.syntax(
                    Syntax.State.UNKNOWN
                    );
                }

            @Override
            public void adql(final String adql)
                {
                BlueQueryEntity.this.adql = adql ;
                }

            @Override
            public void osql(final String osql)
                {
                BlueQueryEntity.this.osql = osql ;
                }

            @Override
            public void add(AdqlColumn column)
                {
                log.debug("add(AdqlColumn)");
                log.debug("  Name [{}]", column.name());
                BlueQueryEntity.this.columns.add(
                    column
                    );
                this.add(
                    column.table()
                    );
                }

            @Override
            public void add(AdqlTable table)
                {
                log.debug("add(AdqlTable)");
                log.debug("  Name [{}]", table.name());
                BlueQueryEntity.this.tables.add(
                    table
                    );
                this.add(
                    //table.resource() - why did this work ?
                    table.root().resource()
                    );
                }

            protected void add(final BaseResource<?> resource)
                {
                log.debug("add(BaseResource)");
                log.debug("  Name [{}]", resource.name());
                BlueQueryEntity.this.resources.add(
                    resource
                    );
                }
            
            @Override
            public void add(SelectField field)
                {
                log.debug("add(SelectField)");
                log.debug("  Name [{}]", field.name());
                log.debug("  Size [{}]", field.arraysize());
                log.debug("  Type [{}]", field.type());
                BlueQueryEntity.this.fields.add(
                    field
                    );
                }

            @Override
            public Syntax syntax()
                {
                return BlueQueryEntity.this.syntax();
                }

            @Override
            public void syntax(State syntax)
                {
				BlueQueryEntity.this.syntax = syntax;
                }

            @Override
            public void syntax(State syntax, String message)
                {
				BlueQueryEntity.this.syntax  = syntax;
				BlueQueryEntity.this.message = message ;
                }
            };
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
    
    @Override
    public Syntax syntax()
    	{
		return new Syntax()
			{
			@Override
			public Level level()
				{
				return BlueQueryEntity.this.level;
				}

			@Override
			public void level(Level level)
				{
				BlueQueryEntity.this.level = level;
				}

			@Override
			public State state()
				{
				return BlueQueryEntity.this.syntax;
				}

			@Override
			public String message()
				{
				return BlueQueryEntity.this.message;
				}

			@Override
			public String friendly()
				{
				return BlueQueryEntity.this.message;
				}

			@Override
			public Iterable<String> warnings()
				{
				return BlueQueryEntity.this.warnings;
				}
			};
    	}

    @Embedded
    private AdqlQueryLimits limits;

    @Override
    public Limits limits()
        {
        /*
         * Need to check for null.
         * "Hibernate considers (embedded) component to be NULL if all its properties are NULL (and vice versa)."
         * http://stackoverflow.com/a/1324391
         */
        if (this.limits == null)
            {
            this.limits = new AdqlQueryLimits(
                services().limits().runtime(
                    null
                    )
                );
            }
        return this.limits ;
        }

    @Override
    public void limits(final Limits that)
        {
        if (this.limits == null)
            {
            this.limits = new AdqlQueryLimits(
                services().limits().runtime(
                    that
                    )
                );
            }
        else {
            this.limits.update(
                services().limits().runtime(
                    that
                    )
                );
            }
        }

    @Override
    public void limits(final Long rows, final Long cells, final Long time)
        {
        this.limits = new AdqlQueryLimits(
            services().limits().runtime(
                services().limits().create(
                    rows,
                    cells,
                    time
                    )
                )
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

    public void delays(final AdqlQuery.Delays that)
        {
        if (this.delays == null)
            {
            this.delays = new AdqlQueryDelays(
                that
                );
            }
        else {
            this.delays.update(
                that
                );
            }
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

    @Override
    public void update(final String input)
    throws InvalidStateRequestException
        {
        update(
            input,
            null,
            null
            );
        }

    @Override
    public void update(final String input, final AdqlQuery.Limits limits)
    throws InvalidStateRequestException
        {
        update(
            input,
            limits,
            null
            );
        }

    /**
     * Update our input query and {@link AdqlQuery.Limits}.
     * This performs the update in a new {@link Thread}, forcing the creation of a new Hibernate {@link Session}.
     * 
     */
    @Override
    public void update(final String input, final AdqlQuery.Limits limits, final AdqlQuery.Delays delays)
    throws InvalidStateRequestException
        {
        log.debug("Starting update(String, Limits)");
        log.debug("  ident [{}]", ident());
        log.debug("  state [{}]", state().name());

        if ((this.state() == TaskState.EDITING) || (this.state() == TaskState.READY))
            {
            services().runner().thread(
                new Updator(this)
                    {
                    @Override
                    public TaskState execute()
                        {
                        try {
// Need to initialise current context.                        
                            BlueQueryEntity query = (BlueQueryEntity) rebase();
                            if (input != null)
                                {
                                log.debug("Before prepare(String)");
                                log.debug("  state [{}]", query.state().name());
                                query.prepare(
                                    input
                                    );
                                log.debug("After prepare(String)");
                                log.debug("  state [{}]", query.state().name());
                                }
                            if (limits != null)
                                {
                                query.limits(
                                    limits
                                    );                                
                                }
                            if (delays != null)
                                {
                                query.delays(
                                    delays
                                    );                                
                                }
                            return query.state();
                            }
                    	catch (final InvalidStateTransitionException ouch)
        	    	    	{
    	    	            log.error("InvalidStateTransitionException [{}]", BlueQueryEntity.this.ident());
                    		return TaskState.ERROR;
    	    	    	    }
                        catch (HibernateConvertException ouch)
                            {
                            log.error("HibernateConvertException [{}]", BlueQueryEntity.this.ident());
                            return TaskState.ERROR;
                            }
                        }
                    }
                );
            log.debug("Finished thread()");
            log.debug("  state [{}]", state().name());
    
            log.debug("Refreshing state");
            this.refresh();
    
            log.debug("Finished update(String, Limits)");
            log.debug("  state [{}]", state().name());
            }
        else {
            throw new InvalidStateRequestException(
                this, 
                "Attempt to modify a read only query"
                );
            }
        }
    
    protected void prepare(final String input)
    throws InvalidStateTransitionException
        {
        log.debug("prepare(String)");
        this.input = input;
        prepare();
        }
    
    @Override
    protected void prepare()
    throws InvalidStateTransitionException
    	{
        log.debug("prepare()");
        log.debug("  ident [{}]", ident());
        log.debug("  state [{}]", state().name());

        // Check for empty query.
        if ((this.input() == null) || (this.input().trim().length() == 0))
            {
            log.debug("Query is empty");
            this.transition(
                TaskState.EDITING
                );
            }
        // Check for valid query.
        else {
            //
            // Log the start time.
            this.timings().adqlstart();
            
            //
            // TODO - The parsers should be part of the resource/schema.
            final AdqlParser direct = this.factories().adql().parsers().create(
                Mode.DIRECT,
                this.source()
                );
            final AdqlParser distrib = this.factories().adql().parsers().create(
                Mode.DISTRIBUTED,
                this.source()
                );

            log.debug("Query mode [{}]", this.mode);

            if (this.mode == Mode.DIRECT)
                {
                log.debug("Processing as [DIRECT] query");
                direct.process(
                    this.parsable()
                    );
                if (syntax().state() == Syntax.State.VALID)
                	{
                    //
                    // Use our primary resource.
                    //this.mode   = Mode.DIRECT;
                    //this.source = primary().ogsa().primary().ogsaid();
                	}
                else {
                	log.debug("Query fails [DIRECT] validation.");
                	}
                }
            else if (this.mode == Mode.DISTRIBUTED)
                {
                log.debug("Processing as [DISTRIBUTED] query");
                distrib.process(
                    this.parsable()
                    );
                if (syntax().state() == Syntax.State.VALID)
                	{
                    //
                    // Use our DQP resource.
                    //this.mode   = Mode.DISTRIBUTED;
                    //this.source = this.dqp;
                	}
                else {
                	log.debug("Query fails [DISTRIBUTED] validation.");
                	}
                }
            else {
                log.debug("Processing as [DIRECT] query");
                direct.process(
                    this.parsable()
                    );
                if (syntax().state() != Syntax.State.VALID)
                	{
                    log.debug("Query fails [DIRECT] validation.");
                	}
                else if (this.resources.size() == 1)
                    {
                	// Should we have input mode and query mode ?
                	this.mode = Mode.DIRECT;
                    //
                    // Use our primary resource.
                    //this.source = primary().ogsa().primary().ogsaid();
                    }
                else {
                    //
                    // Process as a distributed query.
                    log.debug("Processing as [DISTRIBUTED] query");
                    distrib.process(
                        this.parsable()
                        );
                    if (syntax().state() == Syntax.State.VALID)
	                	{
	                	// Should we have input mode and query mode ?
	                	this.mode = Mode.DISTRIBUTED;
	                    //
	                    // Use our DQP resource.
	                    //this.source = this.dqp;
	                	}
                    else {
                    	log.debug("Query fails [DISTRIBUTED] validation.");
	                	}
                    }
                }
            //
            // Log the end time.
            this.timings().adqldone();
            //
            // Update the status.
            if (syntax().state() == Syntax.State.VALID)
                {
                transition(
            		TaskState.READY
            		);
                }
            else {
                transition(
            		TaskState.EDITING
            		);
                }
            }
        }

    @Override
    protected void execute()
    throws InvalidStateTransitionException
        {
        log.debug("execute()");
        log.debug("  ident [{}]", ident());
        log.debug("  state [{}]", state().name());

        if (this.state() != TaskState.READY)
            {
            log.error("Call to execute() with invalid state [{}]", this.state().name());
            throw new IllegalStateException(
                "Call to execute() with invalid state [" + this.state().name() + "]"
                );
            }

        //
        // Build our target resources.
        build();
        //
        // Mark our task as active.
/*
        transition(
    		TaskState.QUEUED
    		);
 */
        
        //
        // Select our target OGSA-DAI service.  
		// Assumes a valid resource list for a DIRECT query.
		// TODO fails on a DISTRIBUTED query.
		// Need a default DQP resource
        log.debug("Getting primary BaseResource");
        final BaseResource<?> base = resources().primary();
        log.debug("Found primary BaseResource [{}]", base.name());

        final OgsaBaseResource from = base.ogsa().primary();
        log.debug("Found primary OgsaBaseResource [{}]", from.name());
        
        log.debug("Getting primary OgsaService");
        final OgsaService service = from.service();
        log.debug("Found primary OgsaService [{}]", service.name());

        log.debug("Getting target table");
        final String into = BlueQueryEntity.this.jdbctable.fullname() ; 
        log.debug("Found target table [{}]", into);
        
        log.debug("Getting target OgsaBaseResource");
        final OgsaBaseResource dest = BlueQueryEntity.this.jdbctable.resource().ogsa().primary() ; 
        log.debug("Found target OgsaBaseResource [{}]", dest.name());

        //TODO Check all the resources are available through the same OgsaService.         

        //TODO push the state here

        //
        // Execute our workflow.
        final BlueWorkflow workflow = new BlueWorkflowClient(
			service.endpoint(),
			service.exec().primary().ogsaid()
    		);

        final BlueWorkflow.Result result = workflow.execute(
			new BlueWorkflow.Param()
				{
				@Override
				public String source()
					{
					return from.ogsaid();
					}
					
				@Override
				public String query()
					{
					return BlueQueryEntity.this.osql();
					}

				@Override
				public InsertParam insert()
					{
					return new InsertParam()
						{
						@Override
						public String resource()
							{
							return dest.ogsaid();
							}

						@Override
						public String table()
							{
							return BlueQueryEntity.this.jdbctable.fullname();
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
				public AdqlQuery.Limits limits()
					{
					return BlueQueryEntity.this.limits();
					}
					
				@Override
				public AdqlQuery.Delays delays()
					{
					return BlueQueryEntity.this.delays() ;
					}

				@Override
				public ContextParam context()
					{
					return new ContextParam()
						{
						@Override
						public String protocol()
							{
							return null;
							}

						@Override
						public String host()
							{
							return null;
							}

						@Override
						public String port()
							{
							return null;
							}

						@Override
						public String base()
							{
							return null;
							}

						@Override
						public String ident()
							{
							return BlueQueryEntity.this.ident().toString();
							}
						};
					}
				}
    		); 

        //TODO pull the state here
        //BUG We can have already received a callback by this point.
        //BUG Need to close and reopen the session to collect the callback results.
        
        //
        // Check the return status.
        log.debug("Workflow result [{}]", result.status());
        switch(result.status())
			{
			case RUNNING :
		        transition(
		    		TaskState.RUNNING
		    		);
	        	break ;

			case COMPLETED :
		        transition(
		    		TaskState.COMPLETED
		    		);
	        	break ;

			case CANCELLED :
		        transition(
		    		TaskState.CANCELLED
		    		);
	        	break ;

			case FAILED:
		        transition(
		    		TaskState.FAILED
		    		);
	        	break ;
			
			case UNKNOWN:
		        transition(
		    		TaskState.ERROR
		    		);
	        	break ;
			default:
	        	log.error("Unknown workflow status[{}]", result.status());
		        transition(
		    		TaskState.ERROR
		    		);
	        	break ;
			}
        }

    protected static class Handle
    extends BlueTaskEntity.Handle
    implements BlueQuery.Handle
        {
        /**
         * Protected constructor.
         * 
         */
        protected Handle(final BlueQuery query)
            {
            super(query);
            }
        }

    @Override
    protected Handle newhandle()
        {
        return new Handle(
            this
            );
        }
    
    @Override
    public void callback(final BlueQuery.Callback message)
    throws InvalidStateRequestException
        {
        log.debug("callback(Callback)");
        log.debug("  ident [{}]", this.ident());
        log.debug("  state [{}]", this.state());
        log.debug("  next  [{}]", message.state());
        log.debug("  state [{}]", message.results().state());
        log.debug("  count [{}]", message.results().count());
        services().runner().thread(
            new Updator(this)
                {
                @Override
                public TaskState execute()
                    {
                    try {
                        //
                        // Get the current instance for this Thread.
                        BlueQueryEntity entity = (BlueQueryEntity) rebase();
                        //
                        // Update the result state.
                        entity.transition(
                            message.results().state(),
                            message.results().count()
                            );
                        //
                        // Update the task state.
                        entity.transition(
                            message.state()
                            );
                        return entity.state();
                        }
                    catch (InvalidStateTransitionException ouch)
                    	{
	    	            log.error("InvalidStateTransitionException [{}]", BlueQueryEntity.this.ident());
                		return TaskState.ERROR;
                    	}
                    catch (HibernateConvertException ouch)
                        {
                        log.error("HibernateConvertException [{}]", BlueQueryEntity.this.ident());
                        return TaskState.ERROR;
                        }
                    }
                }
            );
        log.debug("Finished thread()");
        log.debug("  state [{}]", state());

        log.debug("Refreshing state");
        this.refresh();

        log.debug("Notifying listeners");
        this.event();

        log.debug("Finished callback()");
        log.debug("  state [{}]", state());
        
        }
    
    /**
     * Build our result tables.
     * 
     */
    protected void build()
    	{
        log.debug("build()");
        //
        // Log the start time.
        this.timings().jdbcstart();

        //
        // Check the conditions.
        if (this.state() != TaskState.READY)
            {
			log.error("TaskState is not READY");				
            }
		if (this.jdbctable != null)
			{
			log.error("JDBC table is not null");				
			}
		if (this.adqltable != null)
			{
			log.error("ADQL table is not null");				
			}
        
        final Identity identity = this.owner();
        log.debug(" Identity [{}]", identity);
        log.debug(" Identity [{}][{}]", identity.ident(), identity.name());

        final JdbcSchema jdbcspace = identity.spaces().jdbc().current();
        log.debug(" JDBC space [{}][{}]", jdbcspace.ident(), jdbcspace.name());

        final AdqlSchema adqlspace = identity.spaces().adql().current();
        log.debug(" ADQL space [{}][{}]", adqlspace.ident(), adqlspace.name());

        this.jdbctable = jdbcspace.tables().create();
        this.adqltable = adqlspace.tables().create(
    		CopyDepth.PARTIAL,
    		jdbctable,
    		this
            );

    	// TODO Add the row number index column.

        for(final SelectField field : this.fields)
        	{
        	// TODO Adql details depend on the field type - calculated, local Jdbc, remote Ivoa etc ..
        	final JdbcColumn jdbccol =jdbctable.columns().create(
    			new JdbcColumn.Metadata()
    				{
					@Override
					public Adql adql()
						{
						return new Adql()
							{
							@Override
							public String name()
								{
								return field.name();
								}

							@Override
							public String text()
								{
								// TODO Auto-generated method stub
								return null;
								}

							@Override
							public Integer arraysize()
								{
								return field.arraysize();
								}

							@Override
							public AdqlType type()
								{
								return field.type();
								}

							@Override
							public String units()
								{
								// TODO Auto-generated method stub
								return null;
								}

							@Override
							public String utype()
								{
								// TODO Auto-generated method stub
								return null;
								}

							@Override
							public String ucd()
								{
								// TODO Auto-generated method stub
								return null;
								}
							};
						}

					@Override
					public String name()
						{
						return null;
						}

					@Override
					public Jdbc jdbc()
						{
						return new Jdbc()
							{
							@Override
							public String name()
								{
								return null;
								}

							@Override
							public JdbcType jdbctype()
								{
								return field.type().jdbctype();
								}

							@Override
							public Integer arraysize()
								{
								return field.arraysize();
								}
							};
						}
    				}
    			);

        	// TODO Create with ADQL metadata.
        	// TODO Column create() and update() should only add fields that are different to the base.
        	final AdqlColumn adqlcol = adqltable.columns().create(
    			jdbccol,
        		field.name()    		
    			);
        	}

        //
        // Should this be part of the table ?
        log.debug("Creating JDBC table");
        jdbctable.resource().jdbcdriver().create(
    		jdbctable
    		);
        log.debug("JDBC table created");
        //
        // Update the results status.
        this.resultcount    = 0L;
        this.resultstate = ResultState.EMPTY;
        //
        // Log the end time.
        this.timings().jdbcdone();
	
    	}

    /**
     * Get the corresponding Hibernate entity for the current thread.
     * @throws HibernateConvertException 
     * @todo Move to a generic base class. 
     * @todo Is this the same as BaseComponentEntity.self()
     *
     */
    @Override
    public BlueQuery rebase()
    throws HibernateConvertException
    	{
        log.debug("Converting current instance [{}]", ident());
        try {
			return services().entities().select(
			    ident()
			    );
        	}
        catch (final IdentifierNotFoundException ouch)
        	{
        	log.error("IdentifierNotFound selecting instance [{}][{}]", this.getClass().getName(), ident());
        	throw new HibernateConvertException(
    			ident(),
    			ouch
    			);
        	}
        }
    }
