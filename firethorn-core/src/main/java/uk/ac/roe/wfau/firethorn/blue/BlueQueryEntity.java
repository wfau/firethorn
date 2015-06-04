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
import org.hibernate.annotations.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.ac.roe.wfau.firethorn.adql.parser.AdqlParserQuery;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Mode;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.SelectField;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.State;
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
    implements BlueQuery.Services
        {
        /**
         * Our singleton instance.
         * Can this be in the base class ?
         * 
         */
        private static Services instance ; 

        /**
         * Our singleton instance.
         * Can this be in the base class ?
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
        private IdentFactory<BlueQuery> idents;
        @Override
        public IdentFactory<BlueQuery> idents()
            {
            return this.idents;
            }

        @Autowired
        private NameFactory<BlueQuery> names;
        @Override
        public NameFactory<BlueQuery> names()
            {
            return this.names;
            }

        @Autowired
        private LinkFactory<BlueQuery> links;
        @Override
        public LinkFactory<BlueQuery> links()
            {
            return this.links;
            }

        @Autowired
        private BlueQueryEntity.EntityFactory entities;
        @Override
        public BlueQuery.EntityFactory entities()
            {
            return this.entities;
            }
        }
    
    @Override
    public BlueQuery.Services services()
        {
        return BlueQueryEntity.Services.instance;
        }
    
    /**
     * {@link BlueQuery.EntityFactory} implementation.
     * 
     */
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
        public Iterable<BlueQuery> select()
            {
            return null;
            }

        // Deprecated
        @Override
        public IdentFactory<BlueQuery> idents()
            {
            return null;
            }

        // Deprecated
        @Override
        public LinkFactory<BlueQuery> links()
            {
            return null;
            }

        @Autowired
        private BlueQuery.Services services;
        public BlueQuery.Services services()
            {
            return this.services;
            }
        }

    @Override
    public BlueQuery.EntityFactory factory()
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
            null
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
        this.resource = resource;
        this.input(
            input
            );
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
        this.input = input;
        //prepare();
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
    
    @Override
    public String link()
        {
        return null;
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
    }
