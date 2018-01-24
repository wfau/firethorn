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
package uk.ac.roe.wfau.firethorn.meta.ivoa;

import javax.annotation.PostConstruct;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.entity.AbstractEntityBuilder;
import uk.ac.roe.wfau.firethorn.entity.EntityBuilder;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumnEntity;

/**
 * {@link IvoaColumn} implementation.
 *
 */
@Slf4j
@Entity
@Access(
    AccessType.FIELD
    )
@Table(
    name = IvoaColumnEntity.DB_TABLE_NAME,
    indexes={
        @Index(
            columnList = IvoaColumnEntity.DB_PARENT_COL
            )
        },
    uniqueConstraints={
        }
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "IvoaColumn-select-parent",
            query = "FROM IvoaColumnEntity WHERE (parent = :parent) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "IvoaColumn-select-parent.ident",
            query = "FROM IvoaColumnEntity WHERE ((parent = :parent) AND (ident = :ident)) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "IvoaColumn-select-parent.name",
            query = "FROM IvoaColumnEntity WHERE ((parent = :parent) AND (name = :name)) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "IvoaColumn-search-parent.name",
            query = "FROM IvoaColumnEntity WHERE ((parent = :parent) AND (name LIKE :text)) ORDER BY ident desc"
            )
        }
    )
public class IvoaColumnEntity
    extends BaseColumnEntity<IvoaColumn>
    implements IvoaColumn
    {
    /**
     * Hibernate table mapping, {@value}.
     *
     */
    protected static final String DB_TABLE_NAME = DB_TABLE_PREFIX + "IvoaColumnEntity";

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_IVOA_TYPE_COL = "ivoatype" ;

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_IVOA_SIZE_COL = "ivoasize" ;

    /**
     * The default name prefix, {@value}.
     * 
     */
    protected static final String NAME_PREFIX = "IVOA_COLUMN";

    /**
     * The default alias prefix, {@value}.
     * 
     */
    protected static final String ALIAS_PREFIX = "IVOA_COLUMN_";
    
    /**
     * {@link EntityBuilder} implementation.
     *
     */
    public static abstract class Builder
    extends AbstractEntityBuilder<IvoaColumn, IvoaColumn.Metadata>
    implements IvoaColumn.Builder
        {
        /**
         * Public constructor.
         *
         */
        public Builder(final Iterable<IvoaColumn> source)
            {
            this.init(
                source
                );
            }
        
        @Override
        protected String name(IvoaColumn.Metadata meta)
        throws ProtectionException
            {
            return meta.ivoa().name();
            }

        @Override
        protected void update(final IvoaColumn column, final IvoaColumn.Metadata meta)
        throws ProtectionException
            {
            column.update(
                meta.ivoa()
                );
            }
        }

    /**
     * {@link IvoaColumn.AliasFactory} implementation.
     *
     */
    @Component
    public static class AliasFactory
    implements IvoaColumn.AliasFactory
        {
        @Override
        public String alias(final IvoaColumn column)
            {
            return ALIAS_PREFIX.concat(
                column.ident().toString()
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
        public IvoaColumn resolve(String alias)
        throws ProtectionException, EntityNotFoundException
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
         * Our {@link IvoaColumn.IdentFactory}.
         * 
         */
        @Autowired
        private IvoaColumn.IdentFactory idents ;

        /**
         * Our {@link IvoaColumn.EntityFactory}.
         * 
         */
        @Autowired
        private IvoaColumn.EntityFactory entities;
        
        }
    
    /**
     * {@link IvoaColumn.EntityFactory} implementation.
     *
     */
    @Repository
    public static class EntityFactory
    extends BaseColumnEntity.EntityFactory<IvoaTable, IvoaColumn>
    implements IvoaColumn.EntityFactory
        {

        @Override
        public Class<?> etype()
            {
            return IvoaColumnEntity.class ;
            }

        @Override
        @CreateMethod
        public IvoaColumn create(final IvoaTable parent, final IvoaColumn.Metadata meta)
        throws ProtectionException
            {
            return this.insert(
                new IvoaColumnEntity(
                    parent,
                    meta
                    )
                );
            }
        
        @Override
        @SelectMethod
        public Iterable<IvoaColumn> select(final IvoaTable parent)
        throws ProtectionException
            {
            return super.list(
                super.query(
                    "IvoaColumn-select-parent"
                    ).setEntity(
                        "parent",
                        parent
                        )
                );
            }
        
        @Override
        @SelectMethod
        public IvoaColumn select(final IvoaTable parent, final String name)
        throws ProtectionException, NameNotFoundException
            {
            try {
                return super.single(
                    super.query(
                        "IvoaColumn-select-parent.name"
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
        public IvoaColumn search(final IvoaTable parent, final String name)
        throws ProtectionException
            {
            return super.first(
                super.query(
                    "IvoaColumn-select-parent.name"
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
    implements IvoaColumn.EntityServices
        {
        /**
         * Our singleton instance.
         * 
         */
        private static IvoaColumnEntity.EntityServices instance ; 

        /**
         * Our singleton instance.
         * 
         */
        public static EntityServices instance()
            {
            return IvoaColumnEntity.EntityServices.instance ;
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
            if (IvoaColumnEntity.EntityServices.instance == null)
                {
                IvoaColumnEntity.EntityServices.instance = this ;
                }
            else {
                log.error("Setting instance more than once");
                throw new IllegalStateException(
                    "Setting instance more than once"
                    );
                }
            }
        
        @Autowired
        private IvoaColumn.IdentFactory idents;
        @Override
        public IvoaColumn.IdentFactory idents()
            {
            return this.idents;
            }

        @Autowired
        private IvoaColumn.LinkFactory links;
        @Override
        public IvoaColumn.LinkFactory links()
            {
            return this.links;
            }

        @Autowired
        private IvoaColumn.NameFactory names;
        @Override
        public IvoaColumn.NameFactory names()
            {
            return this.names;
            }

        @Autowired
        private IvoaColumn.EntityFactory entities;
        @Override
        public IvoaColumn.EntityFactory entities()
            {
            return this.entities;
            }

        @Autowired
		private AliasFactory aliases;
		@Override
		public AliasFactory aliases()
			{
			return this.aliases;
			}
        }

    @Override
    protected IvoaColumn.EntityFactory factory()
        {
        log.debug("factory()");
        return IvoaColumnEntity.EntityServices.instance().entities() ; 
        }

    @Override
    protected IvoaColumn.EntityServices services()
        {
        log.debug("services()");
        return IvoaColumnEntity.EntityServices.instance() ; 
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
    protected IvoaColumnEntity()
        {
        }

    /**
     * Protected constructor.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    protected IvoaColumnEntity(final IvoaTable table, final IvoaColumn.Metadata meta)
    throws ProtectionException
        {
        super(
            table,
            meta.ivoa().name()
            );
        this.table = table;
        this.update(
            meta.ivoa()
            );
        }
    
    @Override
    public IvoaColumn base()
        {
        return self();
        }
    @Override
    public IvoaColumn root()
        {
        return self();
        }

    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = IvoaTableEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private IvoaTable table;
    @Override
    public IvoaTable table()
        {
        return this.table;
        }

    @Override
    public IvoaSchema schema()
    throws ProtectionException
        {
        return this.table.schema();
        }

    @Override
    public IvoaResource resource()
    throws ProtectionException
        {
        return this.table.resource();
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_IVOA_TYPE_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    @Enumerated(
        EnumType.STRING
        )
    private AdqlColumn.AdqlType ivoatype ;
    protected AdqlColumn.AdqlType ivoatype()
    throws ProtectionException
        {
        return this.ivoatype;
        }
    protected void ivoatype(final AdqlColumn.AdqlType type)
    throws ProtectionException
        {
        this.ivoatype = type;
        }
    @Override
    protected AdqlColumn.AdqlType adqltype()
    throws ProtectionException
        {
        if (super.adqltype() != null)
            {
            return super.adqltype();
            }
        else {
            return this.ivoatype;
            }
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_IVOA_SIZE_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private Integer ivoasize;
    protected Integer ivoasize()
    throws ProtectionException
        {
        return this.ivoasize;
        }
    protected void ivoasize(final Integer size)
    throws ProtectionException
        {
        this.ivoasize = size;
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
            return this.ivoasize ;
            }
        }

    @Override
    protected void scanimpl()
    throws ProtectionException
        {
        log.debug("scanimpl() for [{}][{}]", this.ident(), this.namebuilder());
        // TODO Auto-generated method stub
        }

    /**
     * Generate the IVOA metadata.
     * 
     */
    protected IvoaColumn.Modifier.Ivoa ivoameta()
        {
        return new IvoaColumn.Modifier.Ivoa()
            {
            @Override
            public String name()
            throws ProtectionException
                {
                return IvoaColumnEntity.this.name();
                }

            @Override
            public AdqlColumn.AdqlType type()
            throws ProtectionException
                {
                return IvoaColumnEntity.this.ivoatype();
                }

            @Override
            public String title()
            throws ProtectionException
                {
                return IvoaColumnEntity.this.name();
                }

            @Override
            public String text()
            throws ProtectionException
                {
                return IvoaColumnEntity.this.text();
                }

            @Override
            public String utype()
            throws ProtectionException
                {
                return IvoaColumnEntity.this.adqlutype();
                }

            @Override
            public String unit()
            throws ProtectionException
                {
                return IvoaColumnEntity.this.adqlunit();
                }

            @Override
            public Integer arraysize()
            throws ProtectionException
                {
                return IvoaColumnEntity.this.adqlsize();
                }

            @Override
            public String ucd()
            throws ProtectionException
                {
                return IvoaColumnEntity.this.adqlucd();
                }
            };
        }
    
    @Override
    public IvoaColumn.Modifier meta()
    throws ProtectionException
        {
        return new IvoaColumn.Modifier()
            {
            @Override
            public String name()
            throws ProtectionException
                {
                return IvoaColumnEntity.this.name();
                }

            @Override
            public IvoaColumn.Modifier.Ivoa ivoa()
            throws ProtectionException
                {
                return ivoameta();
                }

            @Override
            public IvoaColumn.Modifier.Adql adql()
            throws ProtectionException
                {
                return adqlmeta();
                }
            };
        }

    @Override
    public void update(IvoaColumn.Metadata.Ivoa meta)
    throws ProtectionException
        {
        if (meta.type() != null)
        	{
	        this.adqltype(
	            meta.type()
	            );
        	}
        if (meta.arraysize() != null)
        	{
	        this.adqlsize(
	            meta.arraysize()
	            );
        	}

        if (meta.text() != null)
            {
            this.text(
        		meta.text()
        		);
            }
        if (meta.ucd() != null)
            {
            this.adqlucd(
        		meta.ucd()
        		);
            }
        if (meta.unit() != null)
            {
            this.adqlunit(
        		meta.unit()
        		);
            }
        if (meta.utype() != null)
            {
            this.adqlutype(
        		meta.utype()
        		);
            }
        }
    }
