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
package uk.ac.roe.wfau.firethorn.meta.adql;

import javax.annotation.PostConstruct;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.ProxyIdentifier;
import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory.FactoryAllowCreateProtector;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierFormatException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumn;
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumnEntity;

/**
 * {@link AdqlColumn} implementation.
 *
 *
 */
@Slf4j
@Entity
@Access(
    AccessType.FIELD
    )
@Table(
    name = AdqlColumnEntity.DB_TABLE_NAME,
    indexes={
        @Index(
            name = AdqlColumnEntity.DB_INDEX_NAME + AdqlColumnEntity.DB_PARENT_COL,
            columnList = AdqlColumnEntity.DB_PARENT_COL
            ),
        @Index(
            name = AdqlColumnEntity.DB_INDEX_NAME + AdqlColumnEntity.DB_BASE_COL,
            columnList = AdqlColumnEntity.DB_BASE_COL
            )
        },
    uniqueConstraints={
        @UniqueConstraint(
            columnNames = {
                AdqlColumnEntity.DB_NAME_COL,
                AdqlColumnEntity.DB_PARENT_COL
                }
            )
        }
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "AdqlColumn-select-parent",
            query = "FROM AdqlColumnEntity WHERE parent = :parent ORDER BY ident asc"
            ),
        @NamedQuery(
            name  = "AdqlColumn-select-parent-ident",
            query = "FROM AdqlColumnEntity WHERE ((parent = :parent) AND (ident = :ident)) ORDER BY ident asc"
            ),
        @NamedQuery(
            name  = "AdqlColumn-select-parent-name",
            query = "FROM AdqlColumnEntity WHERE ((parent = :parent) AND (name = :name)) ORDER BY ident asc"
            )
        }
    )
public class AdqlColumnEntity
    extends BaseColumnEntity<AdqlColumn>
    implements AdqlColumn
    {
    /**
     * Hibernate table mapping, {@value}.
     *
     */
    protected static final String DB_TABLE_NAME = DB_TABLE_PREFIX + "AdqlColumnEntity";

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_JOIN_NAME  = DB_TABLE_PREFIX + "AdqlColumnJoinTo";

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_INDEX_NAME = DB_TABLE_PREFIX + "AdqlColumnIndexBy";

    /**
     * The default name prefix, {@value}.
     * 
     */
    protected static final String NAME_PREFIX = "ADQL_COLUMN";

    /**
     * The default alias prefix, {@value}.
     * 
     */
    protected static final String ALIAS_PREFIX = "ADQL_COLUMN_";

    /**
     * {@link AdqlColumn.AliasFactory} implementation.
     *
     */
    @Component
    public static class AliasFactory
    implements AdqlColumn.AliasFactory
        {
        @Override
        public String alias(final AdqlColumn entity)
            {
            return ALIAS_PREFIX.concat(
                entity.ident().toString()
                );
            }

        @Override
        public boolean matches(String alias)
            {
            return alias.startsWith(
                ALIAS_PREFIX
                );
            }
        
        @Override
        public AdqlColumn resolve(String alias)
            throws EntityNotFoundException, IdentifierFormatException, ProtectionException
            {
            return entities.select(
                idents.ident(
                    alias.substring(
                        ALIAS_PREFIX.length()
                        )
                    )
                );
            }

        /**
         * Our {@link AdqlColumn.IdentFactory}.
         * 
         */
        @Autowired
        private AdqlColumn.IdentFactory idents ;

        /**
         * Our {@link AdqlColumn.EntityFactory}.
         * 
         */
        @Autowired
        private AdqlColumn.EntityFactory entities;
        
        }

    /**
     * {@link AdqlColumn.EntityFactory} implementation.
     *
     */
    @Repository
    public static class EntityFactory
    extends BaseColumnEntity.EntityFactory<AdqlTable, AdqlColumn>
    implements AdqlColumn.EntityFactory
        {
        @Override
        public Protector protector()
            {
            return new FactoryAllowCreateProtector();
            }

        @Override
        public Class<?> etype()
            {
            return AdqlColumnEntity.class ;
            }

        @Autowired
        private AdqlTable.EntityFactory tables ;

        @Override
        @SelectMethod
        public AdqlColumn select(final Identifier ident)
        throws IdentifierNotFoundException, ProtectionException
            {
            log.debug("select(Identifier) [{}]", ident);
            if (ident instanceof ProxyIdentifier)
                {
                log.debug("-- proxy identifier");
                final ProxyIdentifier proxy = (ProxyIdentifier) ident;

                log.debug("-- parent table");
                final AdqlTable table = tables.select(
                    proxy.parent()
                    );

                log.debug("-- proxy column");
                final AdqlColumn column = table.columns().select(
                    proxy.base()
                    );

                return column ;
                }
            else {
                return super.select(
                    ident
                    );
                }
            }

        @Override
        @CreateMethod
        public AdqlColumn create(final AdqlTable parent, final BaseColumn<?> base)
        throws ProtectionException
            {
            return this.insert(
                new AdqlColumnEntity(
                    parent,
                    base
                    )
                );
            }

        @Override
        @CreateMethod
        public AdqlColumn create(final AdqlTable parent, final BaseColumn<?> base, final String name)
        throws ProtectionException
            {
            return this.insert(
                new AdqlColumnEntity(
                    parent,
                    base,
                    name
                    )
                );
            }

        @Override
        @CreateMethod
        public AdqlColumn create(final AdqlTable parent, final BaseColumn<?> base, final AdqlColumn.Metadata meta)
        throws ProtectionException
            {
            return this.insert(
                new AdqlColumnEntity(
                    parent,
                    base,
                    meta
                    )
                );
            }
        
        @Override
        @SelectMethod
        public Iterable<AdqlColumn> select(final AdqlTable parent)
        throws ProtectionException
            {
            return super.list(
                super.query(
                    "AdqlColumn-select-parent"
                    ).setEntity(
                        "parent",
                        parent
                        )
                );
            }

        @Override
        @SelectMethod
        public AdqlColumn select(final AdqlTable parent, final Identifier ident)
        throws ProtectionException, IdentifierNotFoundException
            {
            try {
                return super.single(
                    super.query(
                        "AdqlColumn-select-parent-ident"
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
        public AdqlColumn select(final AdqlTable parent, final String name)
        throws ProtectionException, NameNotFoundException
            {
            try {
                return super.single(
                    super.query(
                        "AdqlColumn-select-parent-name"
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
        public AdqlColumn search(final AdqlTable parent, final String name)
        throws ProtectionException
            {
            return super.first(
                super.query(
                    "AdqlColumn-select-parent-name"
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
    implements AdqlColumn.EntityServices
        {
        /**
         * Our singleton instance.
         * 
         */
        private static AdqlColumnEntity.EntityServices instance ; 

        /**
         * Our singleton instance.
         * 
         */
        public static EntityServices instance()
            {
            return AdqlColumnEntity.EntityServices.instance ;
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
            if (AdqlColumnEntity.EntityServices.instance == null)
                {
                AdqlColumnEntity.EntityServices.instance = this ;
                }
            else {
                log.error("Setting instance more than once");
                throw new IllegalStateException(
                    "Setting instance more than once"
                    );
                }
            }
        
        @Autowired
        private AdqlColumn.IdentFactory idents;
        @Override
        public AdqlColumn.IdentFactory idents()
            {
            return this.idents;
            }

        @Autowired
        private AdqlColumn.LinkFactory links;
        @Override
        public AdqlColumn.LinkFactory links()
            {
            return this.links;
            }

        @Autowired
        private AdqlColumn.NameFactory names;
        @Override
        public AdqlColumn.NameFactory names()
            {
            return this.names;
            }

        @Autowired
        private AdqlColumn.AliasFactory aliases;
        @Override
        public AdqlColumn.AliasFactory aliases()
            {
            return this.aliases;
            }

        @Autowired
        private AdqlColumn.EntityFactory entities;
        @Override
        public AdqlColumn.EntityFactory entities()
            {
            return this.entities;
            }
        }

    @Override
    protected AdqlColumn.EntityFactory factory()
        {
        log.debug("factory()");
        return AdqlColumnEntity.EntityServices.instance().entities() ; 
        }

    @Override
    protected AdqlColumn.EntityServices services()
        {
        log.debug("services()");
        return AdqlColumnEntity.EntityServices.instance() ; 
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
    
    protected AdqlColumnEntity()
        {
        super();
        }

    protected AdqlColumnEntity(final AdqlTable table, final BaseColumn<?> base)
        {
        this(
            table,
            base,
            (String) null
            );
        }

    protected AdqlColumnEntity(final AdqlTable table, final BaseColumn<?> base, final AdqlColumn.Metadata meta)
    throws ProtectionException
        {
        this(
            table,
            base,
            meta.adql().name()
            );
        }
    
    protected AdqlColumnEntity(final AdqlTable table, final BaseColumn<?> base, final String name)
        {
        super(
            table,
            ((name != null) ? name : base.name())
            );
        this.base  = base ;
        this.table = table;
        }

    @Override
    public String text()
    throws ProtectionException
        {
        if (super.text() == null)
            {
            return base().text();
            }
        else {
            return super.text();
            }
        }

    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = AdqlTableEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = true
        )
    private AdqlTable table;
    @Override
    public AdqlTable table()
        {
        return this.table;
        }
    @Override
    public AdqlSchema schema()
        {
        return this.table().schema();
        }
    @Override
    public AdqlResource resource()
        {
        return this.table().resource();
        }

    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = BaseColumnEntity.class
        )
    @JoinColumn(
        name = DB_BASE_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private BaseColumn<?> base ;
    @Override
    public BaseColumn<?> base()
        {
        return this.base ;
        }
    @Override
    public BaseColumn<?> root()
    throws ProtectionException
        {
        return base().root();
        }

    @Override
    protected void scanimpl()
    throws ProtectionException
        {
        log.debug("scanimpl() for [{}][{}]", this.ident(), this.namebuilder());
        }

    protected AdqlColumn.AdqlType adqltype()
    throws ProtectionException
        {
        if (super.adqltype() != null)
            {
            return super.adqltype();
            }
        else {
            return base().meta().adql().type();
            }
        }

    protected Integer adqlsize()
    throws ProtectionException
        {
        if (super.adqlsize() != null)
            {
            return super.adqlsize() ;
            }
        else {
            return base().meta().adql().arraysize();
            }
        }

    protected String adqlunit()
    throws ProtectionException
        {
        if (this.adqlunit != null)
            {
            return this.adqlunit ;
            }
        else {
            return base().meta().adql().units();
            }
        }

    protected String adqlutype()
    throws ProtectionException
        {
        if (this.adqlutype != null)
            {
            return this.adqlutype ;
            }
        else {
            return base().meta().adql().utype();
            }
        }

    protected String adqlucd()
    throws ProtectionException
        {
        if (this.adqlucd != null)
            {
            return this.adqlucd ;
            }
        else {
            return base().meta().adql().ucd();
            }
        }
    }
