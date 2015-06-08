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

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.adql.parser.AdqlParserQuery;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Mode;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.SelectField;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.State;
import uk.ac.roe.wfau.firethorn.blue.BlueTaskEntity.Handle;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectMethod;
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
    protected static final String DB_MODE_COL   = "mode";
    /**
     * Hibernate column mapping.
     *
     */
    protected static final String DB_ADQL_COL   = "adql";
    /**
     * Hibernate column mapping.
     *
     */
    protected static final String DB_OSQL_COL   = "osql";

    /**
     * Hibernate column mapping.
     *
     */
    protected static final String DB_INPUT_COL  = "input";

    /**
     * Hibernate column mapping.
     *
     */
    protected static final String DB_JDBC_TABLE_COL  = "jdbctable";

    /**
     * Hibernate column mapping.
     *
     */
    protected static final String DB_ADQL_TABLE_COL  = "adqltable";

    /**
     * Hibernate column mapping.
     *
     */
    protected static final String DB_ADQL_RESOURCE_COL = "adqlresource";
    
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
        }
    
    @Override
    protected BlueQueryEntity.Services services()
        {
        return BlueQueryEntity.Services.instance;
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
        public BlueQuery create(final AdqlResource resource)
            {
            return this.insert(
                new BlueQueryEntity(
                    resource,
                    null
                    )
                );
            }

        @Override
        @CreateMethod
        public BlueQuery create(final AdqlResource resource, final String input)
            {
            return this.insert(
                new BlueQueryEntity(
                    resource,
                    input
                    )
                );
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
        this.input(
            input
            );
        }

    @Override
    public String link()
        {
        return services().links().link(this);
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
        if ((this.one() == StatusOne.EDITING) || (this.one() == StatusOne.READY))
            {
            this.input = input;
            prepare();
            }
        else {
            log.warn("Attempt to change read only query");
            }
        }

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

    /**
     * The query mode (AUTO|DIRECT|DISTRIBUTED).
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
     * Wrap this query as a {@link AdqlParserQuery}.
     * 
     */
    protected AdqlParserQuery parsable()
        {
        return new AdqlParserQuery()
            {
            @Override
            public String input()
                {
                return BlueQueryEntity.this.input();
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

                // LEGACY mode parsing ..
                
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
                // TODO Auto-generated method stub
                return null;
                }

            @Override
            public void syntax(State status)
                {
                // TODO Auto-generated method stub
                }

            @Override
            public void syntax(State status, String message)
                {
                // TODO Auto-generated method stub
                }
            };
        }

    @Override
    protected void prepare()
        {
        log.debug("prepare()");
        log.debug("  ident [{}]", ident());
        log.debug("  state [{}]", one().name());

        if ((this.one() == StatusOne.EDITING) || (this.one() == StatusOne.READY))
            {
            log.debug("Status is good");
            // Check for empty query.
            if ((this.input() == null) || (this.input().trim().length() == 0))
                {
                log.debug("Query is empty");
                this.change(
                    StatusOne.EDITING
                    );
                }
            // Check for valid query.
            else {
                log.debug("Query is good");
                this.change(
                    StatusOne.READY
                    );
                }
            }
        
        else {
            log.error("Call to prepare() with invalid state [{}]", this.one().name());
            throw new IllegalStateException(
                "Call to prepare() with invalid state [" + this.one().name() + "]"
                );
            }
        }

    @Override
    protected void execute()
        {
        log.debug("execute()");
        log.debug("  ident [{}]", ident());
        log.debug("  state [{}]", one().name());

        if (this.one() != StatusOne.READY)
            {
            log.error("Call to execute() with invalid state [{}]", this.one().name());
            throw new IllegalStateException(
                "Call to execute() with invalid state [" + this.one().name() + "]"
                );
            }
        // Create our results table.
        // Call OGSA-DAI to execute.
        try {
            log.debug("Sleeping ....");
            Thread.sleep(1000);

            log.debug("Pending ....");
            change(StatusOne.PENDING);

            log.debug("Sleeping ....");
            Thread.sleep(1000);

            log.debug("Running ....");
            change(StatusOne.RUNNING);

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
    }