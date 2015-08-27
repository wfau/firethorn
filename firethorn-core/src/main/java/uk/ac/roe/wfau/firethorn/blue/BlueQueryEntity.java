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

import lombok.extern.slf4j.Slf4j;

import org.hibernate.Session;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.adql.parser.AdqlParser;
import uk.ac.roe.wfau.firethorn.adql.parser.AdqlParserQuery;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Delays;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Limits;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Mode;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.SelectField;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.Level;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.State;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryDelays;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryLimits;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryTimings;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.UpdateMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.hibernate.HibernateConvertException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResourceEntity;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTableEntity;
import uk.ac.roe.wfau.firethorn.meta.base.BaseResource;
import uk.ac.roe.wfau.firethorn.meta.base.BaseResourceEntity;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTable;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTableEntity;

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
     * Hibernate table mapping.
     *
     */
    protected static final String DB_TABLE_NAME = DB_TABLE_PREFIX + "BlueQueryEntity";

    /**
     * Hibernate table mapping.
     *
     */
    protected static final String DB_JOIN_PREFIX  = DB_TABLE_NAME + "JoinTo";

    /**
     * Hibernate column mapping.
     *
     */
    protected static final String DB_OGSA_MODE_COL   = "ogsamode";

    /**
     * Hibernate column mapping.
     *
     */
    protected static final String DB_ADQL_INPUT_COL  = "adqlinput";

    /**
     * Hibernate column mapping.
     *
     */
    protected static final String DB_ADQL_QUERY_COL   = "adqlquery";

    /**
     * Hibernate column mapping.
     *
     */
    protected static final String DB_OSQL_QUERY_COL   = "osqlquery";

    /**
     * Hibernate column mapping.
     *
     */
    protected static final String DB_JDBC_TABLE_COL  = "jdbctable";

    /**
     * Hibernate column mapping.
     *
     */
    protected static final String DB_ADQL_TABLE_COL    = "adqltable";
    protected static final String DB_ADQL_SCHEMA_COL   = "adqlschema";
    protected static final String DB_ADQL_RESOURCE_COL = "adqlresource";

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
    protected static final String DB_RESULT_ROW_COUNT = "resultrowcount";
    
    /**
     * {@link BlueQuery.Services} implementation.
     * 
     */
    @Slf4j
    @Component
    public static class Services
    extends BlueTaskEntity.Services<BlueQuery>
    implements BlueQuery.Services
        {
        /**
         * Our singleton instance.
         * 
         */
        private static Services instance ; 

        /**
         * Our singleton instance.
         * 
         */
        public static Services instance()
            {
            log.debug("instance()");
            return instance ;
            }

        /**
         * Protected constructor.
         * 
         */
        protected Services()
            {
            log.debug("Services()");
            }
        
        /**
         * Protected initialiser.
         * 
         */
        @PostConstruct
        protected void init()
            {
            log.debug("init()");
            if (BlueQueryEntity.Services.instance == null)
                {
                BlueQueryEntity.Services.instance = this ;
                }
            else {
                log.error("Setting Services.instance more than once");
                throw new IllegalStateException(
                    "Setting Services.instance more than once"
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
        private BlueQuery.NameFactory names;
        @Override
        public BlueQuery.NameFactory names()
            {
            return this.names;
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
        public  BlueQuery.TaskRunner runner()
            {
            return this.runner;
            }
        }
    
    @Override
    protected BlueQueryEntity.Services services()
        {
        return BlueQueryEntity.Services.instance;
        }

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
        public BlueQuery create(final AdqlResource resource, final String input, final TaskState next, final Long wait)
        throws InvalidStateTransitionException, HibernateConvertException
            {
            log.debug("create(AdqlResource, String, TaskState, long");
            log.debug("  state [{}]", next);
            log.debug("  wait  [{}]", wait);

            log.debug("Creating BlueQuery");
            final BlueQuery query = services().runner().thread(
                new BlueQuery.TaskRunner.Creator()
                    {
                    @Override
                    public BlueQuery create()
                        {
                        log.debug("create(");
                        log.debug("Creating query task");
                        return insert(
                    		new BlueQueryEntity(
                				resource,
                				input,
                				services().names().name()
                				)
                    		);
                        }
                    }
                );

            log.debug("Converting BlueQuery");
            final BlueQuery result = query.current();
            
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
        throws IdentifierNotFoundException, InvalidStateTransitionException
            {
            log.debug("update(Identifier , String, TaskStatus, TaskStatus, Long)");
            log.debug("  ident [{}]", ident);
            log.debug("  prev  [{}]", prev);
            log.debug("  next  [{}]", next);
            log.debug("  wait  [{}]", wait);

            final BlueQuery query = select(
                ident
                );
// Should this be in a separate Thread
            if (input != null)
                {
                query.update(
                    input
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
        throws IdentifierNotFoundException, InvalidStateTransitionException
            {
            log.debug("callback(Identifier, CallbackEvent");
            log.debug("  ident [{}]", ident);
            log.debug("  next  [{}]", message.next());
            log.debug("  count [{}]", message.rowcount());
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
        private BlueQuery.Services services;
        public BlueQuery.Services services()
            {
            return this.services;
            }

        // Deprecated
        @Override
        public BlueQuery.IdentFactory idents()
            {
            return null;
            }

        // Deprecated
        @Override
        public BlueQuery.LinkFactory links()
            {
            return null;
            }
        }

    @Override
    protected BlueQuery.EntityFactory factory()
        {
        return services().entities();
        }

	@Override
	protected BlueQuery.TaskRunner runner()
		{
        return services().runner();
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
     */
    protected BlueQueryEntity(final AdqlResource resource, final String input)
        {
        this(
            resource,
            input,
            "BlueQuery"
            );
        }

    /**
     * Protected constructor.
     * 
     */
    protected BlueQueryEntity(final AdqlResource resource, final String input, final String name)
        {
        super(
            name
            );
        this.mode = Mode.AUTO;
        this.resource = resource;
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
    private AdqlResource resource;
    @Override
    public AdqlResource resource()
        {
        return this.resource;
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
         name = DB_RESULT_ROW_COUNT,
         unique = false,
         nullable = true,
         updatable = true
         )
     private Long rowcount ;
    
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
                return BlueQueryEntity.this.rowcount;
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
            public String input()
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
                    table.resource()
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
    
    /**
     * Update our query input.
     * Calling {@link #prepare()} in a new {@link Thread} performs the operation in a separate Hibernate {@link Session}.
     * 
     */
    @Override
    public void update(final String input)
    throws InvalidStateTransitionException
        {
        log.debug("Starting update(String)");
        log.debug("  ident [{}]", ident());
        log.debug("  state [{}]", state().name());

        if ((this.state() == TaskState.EDITING) || (this.state() == TaskState.READY))
            {
            services().runner().thread(
                new Updator<BlueQueryEntity>(this)
                    {
                    @Override
                    public TaskState execute()
                        {
                        try {
                            BlueQueryEntity query = (BlueQueryEntity) current();
                            log.debug("Before input(String)");
                            log.debug("  state [{}]", query.state().name());
                            query.prepare(
                                input
                                );
                            log.debug("After input(String)");
                            log.debug("  state [{}]", query.state().name());
                            return query.state();
                            }
                        catch (HibernateConvertException ouch)
                            {
                            log.error("ThreadConversionException [{}]", BlueQueryEntity.this.ident());
                            return TaskState.ERROR;
                            }
                        }
                    }
                );
            log.debug("Finished thread()");
            log.debug("  state [{}]", state().name());
    
            log.debug("Refreshing state");
            this.refresh();
    
            log.debug("Finished update(String)");
            log.debug("  state [{}]", state().name());
            }
        else {
            throw new InvalidStateTransitionException(
                this, 
                "Update ADQL on a read only query"
                );
            }
        }
    
    protected void prepare(final String input)
        {
        log.debug("prepare(String)");
        this.input = input;
        prepare();
        }
    
    @Override
    protected void prepare()
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
                this.resource()
                );
            final AdqlParser distrib = this.factories().adql().parsers().create(
                Mode.DISTRIBUTED,
                this.resource()
                );

            log.debug("Query mode [{}]", this.mode);

            if (this.mode == Mode.DIRECT)
                {
                log.debug("Processing as [DIRECT] query");
                direct.process(
                    this.parsable()
                    );
                //
                // Use our primary resource.
                //this.mode   = Mode.DIRECT;
                //this.source = primary().ogsa().primary().ogsaid();
                }
            else if (this.mode == Mode.DISTRIBUTED)
                {
                log.debug("Processing as [DISTRIBUTED] query");
                distrib.process(
                    this.parsable()
                    );
                //
                // Use our DQP resource.
                //this.mode   = Mode.DISTRIBUTED;
                //this.source = this.dqp;
                }
            else {
                log.debug("Processing as [DIRECT] query");
                direct.process(
                    this.parsable()
                    );
                if (this.resources.size() == 1)
                    {
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
                    this.mode = Mode.DISTRIBUTED;
                    //
                    // Use our DQP resource.
                    //this.source = this.dqp;
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
        // Create our results table.
        // Call OGSA-DAI to execute.
        try {
            log.debug("Sleeping ....");
            Thread.sleep(1000);

            log.debug("Pending ....");
            transition(TaskState.QUEUED);

            log.debug("Sleeping ....");
            Thread.sleep(1000);

            log.debug("Running ....");
            transition(TaskState.RUNNING);

            log.debug("Sleeping ....");
            Thread.sleep(1000);
            }
        catch (Exception ouch)
            {
            log.debug("Interrupted....");
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
    throws InvalidStateTransitionException
        {
        log.debug("callback(Callback");
        log.debug("  next  [{}]", message.next());
        //
        // Update our row count.
        if (message.rowcount() != null)
            {
            this.rowcount = message.rowcount();
            }
        //
        // Update our state.
        if (message.next() != null)
            {
            transition(
                message.next()
                );
            }
        //
        // Update our Handle and notify any Listeners.
        this.event();
        }

    
    
    }
