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
package uk.ac.roe.wfau.firethorn.adql.query.blue;

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
import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.access.Protector;
import uk.ac.roe.wfau.firethorn.adql.parser.AdqlParser;
import uk.ac.roe.wfau.firethorn.adql.parser.AdqlParserQuery;
import uk.ac.roe.wfau.firethorn.adql.parser.AdqlTranslator;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryBase;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryBase.Syntax.Level;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryBase.Syntax.State;
import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueTask.TaskState;
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
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchemaEntity;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTableEntity;
import uk.ac.roe.wfau.firethorn.meta.base.BaseResource;
import uk.ac.roe.wfau.firethorn.meta.base.BaseResourceEntity;
import uk.ac.roe.wfau.firethorn.meta.base.TreeComponent.CopyDepth;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcColumn;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcColumn.JdbcType;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcSchema;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcSchemaEntity;
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
           name  = "BlueQuery-select-source",
           query = "FROM BlueQueryEntity WHERE source = :source ORDER BY ident asc"
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
    protected static final String DB_JDBC_SCHEMA_COL  = "jdbcschema";

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
    
    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_ADQL_SCHEMA_COL   = "adqlschema";
    
    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_ADQL_RESOURCE_COL = "adqlresource";

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_SYNTAX_STATE_COL   = "syntaxstate";

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_SYNTAX_LEVEL_COL   = "syntaxlevel";

    /**
     * Hibernate column mapping, {@value}.
     *
     */
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
        public Protector protector()
            {
            return new FactoryAllowCreateProtector();
            }
        
        @Override
        public Class<?> etype()
            {
            return BlueQueryEntity.class;
            }

/*
 * 
        @Override
        @CreateMethod
        public BlueQuery create(final AdqlResource source, final String input)
        throws ProtectionException, InvalidRequestException, InternalServerErrorException
            {
            log.debug("create(AdqlResource, String)");
            return create(
                source,
                null,
                null,
                input,
                AdqlQueryBase.Mode.AUTO,
                AdqlQueryBase.Syntax.Level.STRICT,
                null,
                null,
                null,
                null
                );
            }
 * 
 */
        
        @Override
        @CreateMethod
        public BlueQuery create(final AdqlResource source, final JdbcSchema jdbcschema, final AdqlSchema adqlschema, final String input, final AdqlQueryBase.Mode mode, final AdqlQueryBase.Syntax.Level syntax, final AdqlQueryBase.Limits limits, final AdqlQueryBase.Delays delays, final BlueQuery.TaskState next, final Long wait)
        throws ProtectionException, InvalidRequestException, InternalServerErrorException
            {
            log.debug("-- Oi0ieNga Qua8ev1a");
            log.debug("create(AdqlResource, JdbcSchema, AdqlSchema, String, Mode, Syntax, Limits, Delays, TaskState, Long)");
            log.debug("  wait for [{}]", next);
            log.debug("  wait for [{}]", wait);

/*
 * Reason for doing this inside a new Thread is to force a new Hibernate session.
 * Ensuring that the new Entity is committed to the database when the method returns.
 *             
 */

            final Identity outerid = services().contexts().current().oper().identities().primary();
            log.debug("Owner (outer) [{}][{}]", outerid.ident(), outerid.name());
            
            log.debug("Before BlueQuery.Creator");
            log.debug("-- ieCh0naj oH2cah4A");
            final BlueQuery outerq = services().runner().thread(
                new Creator()
                    {
                    @Override
                    public BlueQuery create()
                    throws InvalidStateTransitionException, HibernateConvertException
                        {
                        log.debug("-- een6Dae1 Eshohva5 []");
                        log.debug("create.Creator.create()");
                        log.debug("Creating BlueQuery");

                        final Identity innerid = outerid.rebase();
                        log.debug("Owner (inner) [{}][{}]", innerid.ident(), innerid.name());
                        
                        return insert(
                    		new BlueQueryEntity(
                				operation().identities().primary(),
                                jdbcschema,
                                adqlschema,
                				source,
                				input,
                				mode,
                				syntax,
                				limits,
                				delays
                				)
                    		);
                        }
                    }
                );

            log.debug("After BlueQuery.Creator");
            log.debug("-- Sohkue2b Cae5Aifa [{}]", outerq.ident());
            log.debug("  ident [{}]", outerq.ident());
            log.debug("  state [{}]", outerq.state());
            
            log.debug("Calling BlueQuery.Updator");
            final TaskState after = services().runner().thread(
                new Updator(outerq)
                    {
                    final BlueQuery innerq = outerq.rebase();
                    
                    @Override
                    public Identifier ident()
                        {
                        return innerq.ident();
                        }

                    @Override
                    public TaskState update()
                    throws ProtectionException
                        {
                        log.debug("-- Joh6azi9 loolip2Y [{}]", innerq.ident());
                        log.debug("create.Updator.update()");
                        log.debug("Updating BlueQuery [{}]", innerq.ident());

                        log.debug("  from [{}]", innerq.state());
                        log.debug("  next [{}]", next);
                        
                        if ((next != null) && (next != innerq.state()))
                            {
                            log.debug("Advancing BlueQuery [{}]", innerq.ident());
                            try {
                                innerq.advance(
                                    null,
                                    next,
                                    wait
                                    );
                                }
                            catch (InvalidStateRequestException ouch)
                                {
                                log.debug("InvalidStateRequestException in update");
                                }
                            }
                        log.debug("Completing update");
                        log.debug("  state [{}]", innerq.state());
                        return innerq.state();
                        }
                    }
                );

            log.debug("-- ohx7aeRu raid3Sho [{}]", outerq.ident());
            log.debug("After BlueQuery Updator");
            log.debug("  state [{}]", outerq.state());
            final BlueQueryEntity result = (BlueQueryEntity) outerq.rebase();
            log.debug("After BlueQuery rebase");
            log.debug("  state [{}]", result.state());
            result.refresh();
            log.debug("After BlueQuery refresh");
            log.debug("  state [{}]", result.state());
            
            if (next != null)
	            {
                log.debug("Before BlueQuery wait");
                log.debug("  state [{}]", result.state());
                result.waitfor(
                    after,
                    next,
                    wait
                    );
	            log.debug("After BlueQuery wait");
                log.debug("  query  state [{}]", result.state());
                log.debug("  result state [{}]", result.results().state());
                log.debug("  result count [{}]", result.results().rowcount());
                //result = result.rebase();
                //result = result.refresh();
                //result.refresh();
	            //log.debug("After BlueQuery refresh");
	            //log.debug("  state [{}]", result.state());

                log.debug("Updating from Handle");
	            final Handle handle = (Handle) result.handle();
	            result.update(handle);
                log.debug("After Handle update");
                log.debug("  query  state [{}]", result.state());
                log.debug("  result state [{}]", result.results().state());
                log.debug("  result count [{}]", result.results().rowcount());
	            
	            }
            log.debug("-- Eethia9o Moophie1 [{}]", outerq.ident());
            log.debug("Returning BlueQuery");
            log.debug("  state [{}]", result.state());
            return result;
            }

        @Override
        @SelectMethod
        public BlueQuery select(final Identifier ident, final TaskState prev, final TaskState next, final Long wait)
        throws ProtectionException, IdentifierNotFoundException
            {
            log.debug("-- roo2Heef az9xe6Ei [{}]", ident);
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
        throws ProtectionException, IdentifierNotFoundException, InvalidStateRequestException
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
        public BlueQuery update(final Identifier ident, final String input, final AdqlQueryBase.Limits limits, final TaskState prev, final TaskState next, final Long wait)
        throws ProtectionException, IdentifierNotFoundException, InvalidStateRequestException
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
        public BlueQuery update(final Identifier ident, final String input, final AdqlQueryBase.Limits limits, final AdqlQueryBase.Delays delays, final TaskState prev, final TaskState next, final Long wait)
        throws ProtectionException, IdentifierNotFoundException, InvalidStateRequestException
            {
            log.debug("-- neiLaey4 thei5Hee [{}]", ident);
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
        public BlueQuery callback(final Identifier ident, final BlueQuery.CallbackEvent message)
        throws ProtectionException, IdentifierNotFoundException, InvalidStateRequestException
            {
            log.debug("-- Aemei3te Nahs6ahy [{}]", ident);
            log.debug("callback(Identifier, CallbackEvent)");
            log.debug("  ident [{}]", ident);
            log.debug("  event state  [{}]", message.state());
            log.debug("  result state [{}]", message.results().state());
            log.debug("  result count [{}]", message.results().count());
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
        throws ProtectionException
            {
            return super.list(
                super.query(
                    "BlueQuery-select"
                    )
                );
            }

        @Override
        @SelectMethod
        public Iterable<BlueQuery> select(final AdqlResource source)
        throws ProtectionException
            {
            return super.list(
                super.query(
                    "BlueQuery-select-source"
                    ).setEntity(
                        "source",
                        source
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
        private AdqlQueryBase.Limits.Factory limits;
        @Override
        public AdqlQueryBase.Limits.Factory limits()
            {
            return this.limits;
            }

        @Autowired
        private AdqlQueryBase.Delays.Factory delays;
        @Override
        public AdqlQueryBase.Delays.Factory delays()
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
     */
    protected BlueQueryEntity(final Identity owner, final JdbcSchema jdbcschema, final AdqlSchema adqlschema, final AdqlResource source, final String input, final AdqlQueryBase.Mode mode, final AdqlQueryBase.Syntax.Level syntax, final AdqlQueryBase.Limits limits, final AdqlQueryBase.Delays delays)
    throws InvalidStateTransitionException
        {
        super(
    		owner
            );
        this.source = source;
        this.jdbcspace = jdbcschema;
        this.adqlspace = adqlschema;
        if (mode != null)
            {
            this.mode = mode;
            }
        if (syntax != null)
            {
            this.level = syntax ;
            }
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
    private Mode mode = Mode.AUTO;
    @Override
    public Mode mode()
        {
        return this.mode;
        }
    
    /**
     * The JdbcSchema for the results.
     *
     */
    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = JdbcSchemaEntity.class
        )
    @JoinColumn(
        name = DB_JDBC_SCHEMA_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private JdbcSchema jdbcspace ;

    /**
     * The JdbcTable for the results.
     *
     */
    @ManyToOne(
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
     * The AdqlSchema for the results.
     *
     */
    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = AdqlSchemaEntity.class
        )
    @JoinColumn(
        name = DB_ADQL_SCHEMA_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private AdqlSchema adqlspace ; 
    
    /**
     * The AdqlTable for the results.
     *
     */
    @ManyToOne(
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
         nullable = false,
         updatable = true
         )
    private Long resultcount = new Long(0L);

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
        nullable = false,
        updatable = true
        )
    private ResultState resultstate = ResultState.NONE;

    /**
     * Update the ResultState and row count.
     * 
     */
    protected void transition(final ResultState next, final Long count)
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
    private List<SelectField> fields = new ArrayList<SelectField>();
    
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
            throws ProtectionException
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
            throws ProtectionException
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
            throws ProtectionException
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

            @Override
            public AdqlTranslator translator()
                {
                return BlueQueryEntity.this.resources().primary().translator();
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
    private Syntax.Level level = Syntax.Level.LEGACY;
    // TODO Make this configurable ?
    
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
				if (level != null)
				    {
				    BlueQueryEntity.this.level = level;
				    }
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

    public void delays(final AdqlQueryBase.Delays that)
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
    throws InvalidStateRequestException, ProtectionException
        {
        update(
            input,
            null,
            null
            );
        }

    @Override
    public void update(final String input, final AdqlQueryBase.Limits limits)
    throws InvalidStateRequestException, ProtectionException
        {
        update(
            input,
            limits,
            null
            );
        }

    /**
     * Update our input query and {@link AdqlQueryBase.Limits}.
     * This performs the update in a new {@link Thread}, forcing the creation of a new Hibernate {@link Session}.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     * 
     */
    @Override
    public void update(final String input, final AdqlQueryBase.Limits limits, final AdqlQueryBase.Delays delays)
    throws InvalidStateRequestException, ProtectionException
        {
        log.debug("-- Chuone1i Shokoh9w [{}]", this.ident());
        log.debug("Starting update(String, Limits)");
        log.debug("  ident [{}]", ident());
        log.debug("  state [{}]", state().name());

        if ((this.state() == TaskState.EDITING) || (this.state() == TaskState.READY))
            {
            services().runner().thread(
                new Updator(this)
                    {
                    @Override
                    public TaskState update()
                    throws ProtectionException
                        {
                        log.debug("-- jaeB0gi2 ga8Voo2O [{}]", this.ident());
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

            log.debug("-- peiduC4V eepi9Rie [{}]", this.ident());
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
        log.debug("-- EeCoo6oo Tui9Goog [{}]", this.ident());
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
    throws ProtectionException, InvalidStateTransitionException 
        {
        log.debug("-- Doo7uuc3 Da0ohphu [{}]", this.ident());
        log.debug("Starting execute()");
        log.debug("  ident [{}]", this.ident());
        log.debug("  state [{}]", this.state().name());

        history().create(
            BlueTaskLogEntry.Level.INFO, 
            "Executing query"
            );
        
        if (this.state() != TaskState.READY)
            {
            log.error("Call to execute() with invalid state [{}]", this.state().name());
            throw new IllegalStateException(
                "Call to execute() with invalid state [" + this.state().name() + "]"
                );
            }

        //
        // Build our target resources.
        //this.build();
        //
        // Push changes to the database.
        //this.flush();
        
        //
        // Build our target resources.
        // Passing in the current list of fields to build the table.
        services().runner().thread(
            new Updator(this)
                {
                @Override
                public TaskState update()
                throws ProtectionException
                    {
                    try {
                        BlueQueryEntity entity = (BlueQueryEntity) rebase();
                        entity.fields = BlueQueryEntity.this.fields;
                        entity.build();
                        return entity.state();
                        }
                    catch (HibernateConvertException ouch)
                        {
                        log.error("HibernateConvertException [{}]", BlueQueryEntity.this.ident());
                        return TaskState.ERROR;
                        }
                    }
                }
            );

        //
        // Pull changes from database.
        this.refresh();
        
        //
        // Select our target OGSA-DAI service.  
        // Assumes a valid resource list for a DIRECT query.
        // TODO fails on a DISTRIBUTED query.
        // Need a default DQP resource
        log.debug("Getting base BaseResource");
        final BaseResource<?> base = resources().primary();
        log.debug("Found base BaseResource [{}]", base.name());

        final OgsaBaseResource from ;
        if (this.mode() == Mode.DIRECT)
            {
            log.debug("Getting direct resource");
            from = base.ogsa().primary();
            log.debug("Using direct resource [{}]", from.name());
            }
        else if (this.mode() == Mode.DISTRIBUTED)
            {
            log.debug("Getting DQP resource");
            from = factories().ogsa().dqp().entities().primary();
            log.debug("Found DQP resource [{}]", from.ogsaid());
            }
        else {
            log.error("Unexpected query mode [{}]", this.mode());
            transition(
                TaskState.ERROR
                );
            return ;
            }
        
        log.debug("Getting source OgsaService");
        final OgsaService service = from.service();
        log.debug("Found source OgsaService [{}]", service.name());

        log.debug("Getting target table");
        final String into = this.jdbctable.resource().connection().operator().fullname(
                this.jdbctable
                ); 
        log.debug("Found target table [{}]", into);
        
        log.debug("Getting target OgsaBaseResource");
        final OgsaBaseResource dest = this.jdbctable.resource().ogsa().primary() ; 
        log.debug("Found target OgsaBaseResource [{}]", dest.name());

        //TODO Check all the resources are available through the same OgsaService.         

        //
        // Mark this query as RUNNING.
        log.debug("-- ci3ooN5u Ohmei0Ga --");
        log.debug("Setting state to [RUNNING]");
        transition(
            TaskState.RUNNING
            );
        //
        // Activate our event handler.
        log.debug("Activating handler");
        this.event(
            true
            );
        //
        // Execute our workflow.
        log.debug("Creating workflow ...");
        final BlueWorkflow workflow = new BlueWorkflowClient(
			service.endpoint(),
			service.exec().primary().ogsaid()
    		);

        //
        // Fix for permission issues.
        final String source = from.ogsaid();
        final String sink   = dest.ogsaid();

        log.debug("Executing workflow ...");        
        final BlueWorkflow.Result result = workflow.execute(
			new BlueWorkflow.Param()
				{
				@Override
				public String source()
					{
					return source;
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
							return sink;
							}

						@Override
						public String table()
							{
							return into;
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
				public AdqlQueryBase.Limits limits()
					{
					return BlueQueryEntity.this.limits();
					}
					
				@Override
				public AdqlQueryBase.Delays delays()
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
							// HARD CODED PORT NUMBER !!!
							return "8081";
                            //return null;
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

        log.debug("-- Meex9Lae OzoDei0b [{}]", this.ident());
        log.debug("After workflow ...");
        log.debug("  state [{}]", this.state().name());
        
        //
        // We may have already received a callback by this point.
        // Update the entity to collect the callback results.
        log.debug("Refreshing entity");
        this.refresh();    
        log.debug("After refresh");
        log.debug("  state [{}]", this.state().name());
        
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
			
			default:
	        	log.error("Unknown workflow status[{}]", result.status());
		        transition(
		    		TaskState.ERROR
		    		);
	        	break ;
			}
        log.debug("-- Fui7uiNe oVash5sh [{}]", this.ident());
        log.debug("Finishing execute()");
        log.debug("  ident [{}]", this.ident());
        log.debug("  state [{}]", this.state().name());
        }

    protected static class Handle
    extends BlueTaskEntity.Handle
    implements BlueQuery.Handle
        {
        /**
         * Protected constructor.
         * @throws ProtectionException 
         * 
         */
        protected Handle(final BlueQueryEntity query)
        throws ProtectionException
            {
            super(query);
            this.resultcount = query.resultcount;
            this.resultstate = query.resultstate;
            }

        private ResultState resultstate = ResultState.NONE;
        public ResultState resultstate()
            {
            return this.resultstate;
            }
        
        private Long resultcount = new Long(0L);
        public Long resultcount()
            {
            return this.resultcount;
            }

        /**
         * Update a Handle from a BlueQueryEntity.
         * 
         */
        public void update(final BlueQueryEntity query)
            {
            log.debug("Updating Handle [{}]", query.ident());
            log.debug("  query  state [{}]",  query.state());
            log.debug("  handle state [{}]", this.state());
            if (query.state().compareTo(this.state()) > 0)
                {
                log.debug("Adopting query state [{}][{}]", query.state(), this.state());
                this.state = query.state();
                }
            if (query.resultstate.compareTo(this.resultstate) > 0)
                {
                log.debug("Adopting result state [{}][{}]", query.resultstate, this.resultstate);
                this.resultstate = query.resultstate ;
                }
            if (query.resultcount > this.resultcount)
                {
                log.debug("Adopting result count [{}][{}]", query.resultcount, this.resultcount);
                this.resultcount = query.resultcount ;
                }
            }
        }

    /**
     * Update our BlueQuery from a Handle.
     * 
     */
    public void update(final Handle handle)
        {
        log.debug("Checking Handle status [{}]", this.ident());
        log.debug("  entity state [{}]", this.state());
        if (handle != null)
            {
            log.debug("  handle state [{}]", handle.state());
            if (handle.state().compareTo(this.state()) > 0)
                {
                log.debug("Adopting Handle state [{}][{}]", this.state(), handle.state());
                try {
                    transition(
                        handle.state()
                        );
                    }
                catch (InvalidStateTransitionException ouch)
                    {
                    log.warn("Ignoring invalid state transition [{}][{}]", this.state(), handle.state());
                    } 
                }
            transition(
                handle.resultstate,
                handle.resultcount
                );
            }
        }

    @Override
    protected Handle newhandle()
    throws ProtectionException
        {
        return new Handle(
            this
            );
        }

    
    
    @Override
    public void waitfor(final TaskState prev, final TaskState next, final Long wait)
    throws ProtectionException
        {
        log.debug("-- ohj0Toh3 Iequae3e [{}]", this.ident());
        log.debug("waitfor(TaskState, Long)");
        super.waitfor(prev, next, wait);
        }
    
    @Override
    public void callback(final BlueQuery.CallbackEvent message)
    throws InvalidStateRequestException, ProtectionException
        {
        log.debug("-- thoh3Jeu IRae8ahr [{}]", this.ident());
        log.debug("callback(Callback)");
        log.debug("  ident  [{}]", this.ident());
        log.debug("  prev   [{}]", this.state());
        log.debug("  next   [{}]", message.state());
        log.debug("  result [{}]", message.results().state());
        log.debug("  count  [{}]", message.results().count());
        services().runner().thread(
            new Updator(this)
                {
                @Override
                public TaskState update()
                throws ProtectionException
                    {
                    try {
                        //
                        // Get the current instance for this Thread.
                        BlueQueryEntity entity = (BlueQueryEntity) rebase();
                        //
                        // Add a log entry.
                        entity.history().create(
                            message.state(),
                            BlueTaskLogEntry.Level.INFO,
                            message.message()
                            );
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
                        //
                        // Update the results table.
                        final AdqlTable results = entity.results().adql();
                        // BANG !!
                        // Fails if the query hasn't been prepared yet.
                        if (results == null)
                            {
                            log.error("Callback with null results table");
                            }
                        else {
                            log.debug("Updating results table");
                            results.meta().adql().count(
                                message.results().count()
                                );
                            switch (message.results().state())
                                {
                                case PARTIAL:
                                    results.meta().adql().status(
                                        AdqlTable.TableStatus.PARTIAL
                                        );
                                    break;
                                case COMPLETED:
                                    results.meta().adql().status(
                                        AdqlTable.TableStatus.COMPLETED
                                        );
                                    break;
                                default:
                                    break;
                                }
                            }
                        //log.debug("Notifying listeners");
                        //entity.event();
                        log.debug("-- Ci0liesu Eecha4ee [{}]", entity.state());
                        log.debug("  query  state [{}]", entity.state());
                        log.debug("  result state [{}]", entity.resultstate);
                        log.debug("  result count [{}]", entity.resultcount);

                        //
                        // Update our Handle.
                        final Handle handle = (Handle) entity.handle();
                        handle.update(entity);
                        
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

        log.debug("-- Cie4Dahf Mee0aeXo [{}]", this.state());
        log.debug("Finished thread()");
        log.debug("  query  state [{}]", this.state());
        log.debug("  result state [{}]", this.resultstate);
        log.debug("  result count [{}]", this.resultcount);

        log.debug("Refreshing state");
        this.refresh();
        log.debug("  query  state [{}]", this.state());
        log.debug("  result state [{}]", this.resultstate);
        log.debug("  result count [{}]", this.resultcount);

        log.debug("Notifying listeners");
        this.event();
        log.debug("  query  state [{}]", this.state());
        log.debug("  result state [{}]", this.resultstate);
        log.debug("  result count [{}]", this.resultcount);

        log.debug("Finished callback()");
        
        }
    
    /**
     * Build our result tables.
     * @throws ProtectionException 
     * 
     */
    protected void build()
    throws ProtectionException
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

        log.debug(" JDBC space [{}]", jdbcspace);
        if (this.jdbcspace == null)
            {
            this.jdbcspace = identity.spaces().jdbc().current();
            }
// BUG fail the query if the jdbcspace is null.  
        
        log.debug(" JDBC space [{}][{}]", jdbcspace.ident(), jdbcspace.name());

        log.debug(" ADQL space [{}]", adqlspace);
        if (this.adqlspace== null)
            {
            this.adqlspace = identity.spaces().adql().current();
            }
        log.debug(" ADQL space [{}][{}]", adqlspace.ident(), adqlspace.name());

        this.jdbctable = jdbcspace.tables().create(
            this
            );
        this.adqltable = adqlspace.tables().create(
            CopyDepth.PARTIAL,
            this.jdbctable
            );

    	// TODO Add the row number index column.
        for(final SelectField field : this.fields)
        	{
        	log.debug("Adding SelectField [{}]", field.name());
        	// TODO Adql details depend on the field type - calculated, local Jdbc, remote Ivoa etc ..
        	// TODO Split this into a separate function. 
        	final JdbcColumn.Metadata meta = new JdbcColumn.Metadata()
        	    {
                @Override
                public String name()
                    {
                    return null;
                    }
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
                            return null;
                            }
                        @Override
                        public Integer arraysize()
                        throws ProtectionException
                            {
                            return field.arraysize();
                            }
                        @Override
                        public AdqlType type()
                        throws ProtectionException
                            {
                            return field.type();
                            }
                        @Override
                        public String units()
                            {
                            return null;
                            }
                        @Override
                        public String utype()
                            {
                            return null;
                            }
                        @Override
                        public String ucd()
                            {
                            return null;
                            }
                        };
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
                        throws ProtectionException
                            {
                            return field.type().jdbctype();
                            }
                        @Override
                        public Integer arraysize()
                        throws ProtectionException
                            {
                            return field.arraysize();
                            }
                        };
                    }
        	    }; 
        	
        	final JdbcColumn jdbccol = jdbctable.columns().create(
    	        meta
    			);

        	// TODO Create with ADQL metadata.
        	final AdqlColumn adqlcol = adqltable.columns().create(
    			jdbccol,
        		field.name()    		
    			);
        	}

        //
        // Should this be part of the table ?
        log.debug("Creating JDBC table");
        jdbctable.resource().connection().operator().create(
    		jdbctable
    		);
// TODO catch SQLException ..
        log.debug("JDBC table created");
        //
        // Update the results status.
        this.resultcount = 0L;
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
        catch (final ProtectionException ouch)
            {
            log.error("ProtectionException [{}][{}]", this.getClass().getName(), ident());
        	throw new HibernateConvertException(
    			ident(),
    			ouch
    			);
        	}
        }
    }
