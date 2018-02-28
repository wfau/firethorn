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
package uk.ac.roe.wfau.firethorn.meta.jdbc;

import javax.annotation.PostConstruct;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.access.Protector;
import uk.ac.roe.wfau.firethorn.entity.AbstractEntityBuilder;
import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory.FactoryAllowCreateProtector;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumnEntity;

/**
 * {@link JdbcColumn} implementation.
 *
 */
@Slf4j
@Entity
@Access(
    AccessType.FIELD
    )
@EntityListeners(
    JdbcColumnEntity.EntityListener.class
    )
@Table(
    name = JdbcColumnEntity.DB_TABLE_NAME,
    indexes={
        @Index(
            columnList = JdbcColumnEntity.DB_PARENT_COL
            )
        },
    uniqueConstraints={
        @UniqueConstraint(
            columnNames = {
                JdbcColumnEntity.DB_NAME_COL,
                JdbcColumnEntity.DB_PARENT_COL
                }
            )
        }
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "JdbcColumn-select-table",
            query = "FROM JdbcColumnEntity WHERE table = :parent ORDER BY ident asc"
            ),
        @NamedQuery(
            name  = "JdbcColumn-select-parent",
            query = "FROM JdbcColumnEntity WHERE parent = :parent ORDER BY ident asc"
            ),
        @NamedQuery(
            name  = "JdbcColumn-select-parent-ident",
            query = "FROM JdbcColumnEntity WHERE ((parent = :parent) AND (ident = :ident)) ORDER BY ident asc"
            ),
        @NamedQuery(
            name  = "JdbcColumn-select-parent-name",
            query = "FROM JdbcColumnEntity WHERE ((parent = :parent) AND (name = :name)) ORDER BY ident asc"
            ),
        }
    )
public class JdbcColumnEntity
    extends BaseColumnEntity<JdbcColumn>
    implements JdbcColumn
    {
    /**
     * Hibernate table mapping, {@value}.
     *
     */
    protected static final String DB_TABLE_NAME = DB_TABLE_PREFIX + "JdbcColumnEntity";

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_JDBC_TYPE_COL = "jdbctype" ;

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_JDBC_SIZE_COL = "jdbcsize" ;

    /**
     * For the JPA EntityListener annotations to work,
     * Hibernate needs to be configured with an EntityManager
     * rather than a SessionManager.
     * 
     * http://www.baeldung.com/2011/12/13/the-persistence-layer-with-spring-3-1-and-jpa/
     * http://www.studytrails.com/frameworks/spring/spring-hibernate-jpa.jsp
     * https://stackoverflow.com/questions/25260527/obtaining-entitymanager-in-spring-hibernate-configuration
     * 
     */
    @Slf4j
    public static class EntityListener
        {
        /**
         * Public constructor.
         *
         *
         */
        public EntityListener()
            {
            log.debug("JdbcColumnEntity.EntityListener()");
            }
        
        /**
         * On load ...
         *
         */
        @PostLoad
        public void load(final JdbcColumnEntity column)
            {
            log.debug("load(JdbcColumnEntity)");
            log.debug(" ident [{}]", column.ident());
            log.debug(" name  [{}]", column.name());
            //log.debug(" auto  [{}]", entities);
            }

        /**
         * On save ...
         *
         */
        @PrePersist
        public void save(final JdbcColumnEntity column)
            {
            log.debug("save(JdbcColumnEntity)");
            log.debug(" ident [{}]", column.ident());
            log.debug(" name  [{}]", column.name());
            //log.debug(" auto  [{}]", entities);
            }
        
        //@Autowired
        //private JdbcColumn.EntityFactory entities;

        }
    
    /**
     * {@link JdbcColumn.Builder} implementation.
     *
     */
    public static abstract class Builder
    extends AbstractEntityBuilder<JdbcColumn, JdbcColumn.Metadata>
    implements JdbcColumn.Builder
        {
        /**
         * Public constructor.
         *
         */
        public Builder(final Iterable<JdbcColumn> source)
            {
            this.init(
                source
                );
            }

        @Override
        protected String name(JdbcColumn.Metadata meta)
        throws ProtectionException
            {
            return meta.jdbc().name();
            }

        @Override
        protected void update(final JdbcColumn column, final JdbcColumn.Metadata meta)
        throws ProtectionException
            {
            column.update(
                meta.jdbc()
                );
            column.update(
                meta.adql()
                );
            }
        }
    
    /**
     * {@link JdbcColumn.AliasFactory} implementation.
     *
     */
    @Component
    public static class AliasFactory
    implements JdbcColumn.AliasFactory
        {
        private static final String PREFIX = "JDBC_";

        @Override
        public String alias(final JdbcColumn column)
            {
            return PREFIX.concat(
                column.ident().toString()
                );
            }

        @Override
        public boolean matches(String alias)
            {
            return alias.startsWith(
                PREFIX
                );
            }
        
        @Override
        public JdbcColumn resolve(String alias)
        throws ProtectionException, EntityNotFoundException
            {
            return entities.select(
                idents.ident(
                    alias.substring(
                        PREFIX.length()
                        )
                    )
                );
            }

        /**
         * Our {@link JdbcColumn.IdentFactory}.
         * 
         */
        @Autowired
        private JdbcColumn.IdentFactory idents ;

        /**
         * Our {@link JdbcColumn.EntityFactory}.
         * 
         */
        @Autowired
        private JdbcColumn.EntityFactory entities;
        
        }

    /**
     * {@link JdbcColumn.EntityFactory} implementation.
     *
     */
    @Repository
    public static class EntityFactory
    extends BaseColumnEntity.EntityFactory<JdbcTable, JdbcColumn>
    implements JdbcColumn.EntityFactory
        {
        @Override
        public Protector protector()
            {
            return new FactoryAdminCreateProtector();
            }

        @Override
        public Class<?> etype()
            {
            return JdbcColumnEntity.class ;
            }

        @Override
        @CreateMethod
        public JdbcColumn create(final JdbcTable parent, final JdbcColumn.Metadata meta)
        throws ProtectionException
            {
            return create(
                parent,
                meta.jdbc().name(),
                meta.jdbc().jdbctype(),
                meta.jdbc().arraysize()
                );
            }

        @Override
        @CreateMethod
        public JdbcColumn create(final JdbcTable parent, final String name, final JdbcColumn.JdbcType type, final Integer size)
        throws ProtectionException
            {
            return this.insert(
                new JdbcColumnEntity(
                    parent,
                    name,
                    type,
                    size
                    )
                );
            }
        
        @Override
        @SelectMethod
        public Iterable<JdbcColumn> select(final JdbcTable parent)
        throws ProtectionException
            {
            return super.list(
                super.query(
                    "JdbcColumn-select-parent"
                    ).setEntity(
                        "parent",
                        parent
                        )
                );
            }

        @Override
        @SelectMethod
        public JdbcColumn select(final JdbcTable parent, final Identifier ident)
        throws ProtectionException, IdentifierNotFoundException
            {
            try {
                return super.single(
                    super.query(
                        "JdbcColumn-select-parent-ident"
                        ).setEntity(
                            "parent",
                            parent
                        ).setSerializable(
                            "ident",
                            ident.value()
                        )
                    );
                }
            catch (final EntityNotFoundException ouch)
                {
                log.debug("Unable to locate column [{}][{}]", parent.namebuilder().toString(), ident);
                throw new IdentifierNotFoundException(
                        ident,
                    ouch
                    );
                }
            }

        @Override
        @SelectMethod
        public JdbcColumn select(final JdbcTable parent, final String name)
        throws ProtectionException, NameNotFoundException
            {
            try {
                return super.single(
                    super.query(
                        "JdbcColumn-select-parent-name"
                        ).setEntity(
                            "parent",
                            parent
                        ).setString(
                            "name",
                            name
                        )
                    );
                }
            catch (final EntityNotFoundException ouch)
                {
                log.debug("Unable to locate column [{}][{}]", parent.namebuilder().toString(), name);
                throw new NameNotFoundException(
                    name,
                    ouch
                    );
                }
            }
        
        @Override
        @SelectMethod
        public JdbcColumn search(final JdbcTable parent, final String name)
        throws ProtectionException
            {
            return super.first(
                super.query(
                    "JdbcColumn-select-parent-name"
                    ).setEntity(
                        "parent",
                        parent
                    ).setString(
                        "name",
                        name
                    )
                );
            }
        }

    /**
     * {@link Entity.EntityServices} implementation.
     * 
     */
    @Slf4j
    @Component
    public static class EntityServices
    implements JdbcColumn.EntityServices
        {
        /**
         * Our singleton instance.
         * 
         */
        private static JdbcColumnEntity.EntityServices instance ; 

        /**
         * Our singleton instance.
         * 
         */
        public static EntityServices instance()
            {
            return JdbcColumnEntity.EntityServices.instance ;
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
            if (JdbcColumnEntity.EntityServices.instance == null)
                {
                JdbcColumnEntity.EntityServices.instance = this ;
                }
            else {
                log.error("Setting instance more than once");
                throw new IllegalStateException(
                    "Setting instance more than once"
                    );
                }
            }
        
        @Autowired
        private JdbcColumn.IdentFactory idents;
        @Override
        public JdbcColumn.IdentFactory idents()
            {
            return this.idents;
            }

        @Autowired
        private JdbcColumn.LinkFactory links;
        @Override
        public JdbcColumn.LinkFactory links()
            {
            return this.links;
            }

        @Autowired
        private JdbcColumn.NameFactory names;
        @Override
        public JdbcColumn.NameFactory names()
            {
            return this.names;
            }

        @Autowired
        private JdbcColumn.EntityFactory entities;
        @Override
        public JdbcColumn.EntityFactory entities()
            {
            return this.entities;
            }

        @Autowired
		private JdbcColumn.AliasFactory aliases;
		@Override
		public JdbcColumn.AliasFactory aliases()
			{
			return this.aliases;
			}
        }

    @Override
    protected JdbcColumn.EntityFactory factory()
        {
        log.debug("factory()");
        return JdbcColumnEntity.EntityServices.instance().entities() ; 
        }

    @Override
    protected JdbcColumn.EntityServices services()
        {
        log.debug("services()");
        return JdbcColumnEntity.EntityServices.instance() ; 
        }

    @Override
    public String link()
        {
        return services().links().link(
            this
            );
        }

    @Override
    public String alias()
    throws ProtectionException
        {
        return services().aliases().alias(
            this
            );
        }
    
    /**
     * Protected constructor.
     *
     */
    protected JdbcColumnEntity()
        {
        super();
        }

    /**
     * Protected constructor.
     *
     */
    protected JdbcColumnEntity(final JdbcTable table, final JdbcColumn.Metadata meta)
    throws ProtectionException
        {
        this(
            table,
            meta.jdbc().name(),
            meta.jdbc().jdbctype(),
            meta.jdbc().arraysize()
            );
        this.update(
            meta.adql()
            );
        }

    /**
     * Protected constructor.
     *
     */
    protected JdbcColumnEntity(final JdbcTable table, final String name, final JdbcColumn.JdbcType type, final Integer size)
        {
        super(
            table,
            name
            );
        log.debug("JdbcColumnEntity [{}][{}][{}]", name, type, size);

        this.table    = table;
        this.jdbctype = type ;

        if (type.isarray())
            {
            this.jdbcsize = size ;
            }
        else {
            this.jdbcsize = NON_ARRAY_SIZE ;
            }
        }

    @Override
    public JdbcColumn base()
        {
        return self() ;
        }
    @Override
    public JdbcColumn root()
        {
        return self() ;
        }

    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = JdbcTableEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private JdbcTable table;
    @Override
    public JdbcTable table()
        {
        return this.table;
        }
    @Override
    public JdbcSchema schema()
    throws ProtectionException
        {
        return this.table.schema();
        }
    @Override
    public JdbcResource resource()
    throws ProtectionException
        {
        return this.table.resource();
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_JDBC_TYPE_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    @Enumerated(
        EnumType.STRING
        )
    private JdbcColumn.JdbcType jdbctype ;
    protected JdbcColumn.JdbcType jdbctype()
    throws ProtectionException
        {
        return this.jdbctype;
        }
    protected void jdbctype(final JdbcColumn.JdbcType type)
    throws ProtectionException
        {
        this.jdbctype = type;
        }
    @Override
    protected AdqlColumn.AdqlType adqltype()
    throws ProtectionException
        {
        if (super.adqltype() != null)
            {
            return super.adqltype();
            }
        else if (this.jdbctype != null)
            {
            return this.jdbctype.adqltype();
            }
        else {
            return null;
            }
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_JDBC_SIZE_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private Integer jdbcsize;
    protected Integer jdbcsize()
    throws ProtectionException
        {
        return this.jdbcsize;
        }
    protected void jdbcsize(final Integer size)
    throws ProtectionException
        {
        this.jdbcsize = size;
        }

    @Override
    protected Integer adqlsize()
    throws ProtectionException
        {
        if (super.adqlsize() != null)
            {
            return super.adqlsize() ;
            }
        else {
            if (this.jdbctype.isarray())
                {
                return this.jdbcsize ;
                }
            else {
                return AdqlColumn.NON_ARRAY_SIZE;
                }
            }
        }

    @Override
    public void scanimpl()
    throws ProtectionException
        {
        log.debug("scanimpl() for [{}][{}]", this.ident(), this.namebuilder());
        }
    
    protected JdbcColumn.Modifier.Jdbc jdbcmeta()
    throws ProtectionException
        {
        return new JdbcColumn.Modifier.Jdbc()
            {
            @Override
            public String name()
            throws ProtectionException
                {
                return JdbcColumnEntity.this.name();
                }
            @Override
            public Integer arraysize()
            throws ProtectionException
                {
                return JdbcColumnEntity.this.jdbcsize();
                }
            @Override
            public JdbcColumn.JdbcType jdbctype()
            throws ProtectionException
                {
                return JdbcColumnEntity.this.jdbctype();
                }
            };
        }

    @Override
    public JdbcColumn.Modifier meta()
    throws ProtectionException
        {
        return new JdbcColumn.Modifier()
            {
            @Override
            public String name()
            throws ProtectionException
                {
                return JdbcColumnEntity.this.name();
                }
            @Override
            public AdqlColumn.Modifier.Adql adql()
            throws ProtectionException
                {
                return adqlmeta();
                }
            @Override
            public JdbcColumn.Modifier.Jdbc jdbc()
            throws ProtectionException
                {
                return jdbcmeta();
                }
            };
        }

    @Override
    public void update(final JdbcColumn.Metadata.Jdbc meta)
    throws ProtectionException
    	{
    	// TODO ...
    	throw new UnsupportedOperationException();
    	}
    }
