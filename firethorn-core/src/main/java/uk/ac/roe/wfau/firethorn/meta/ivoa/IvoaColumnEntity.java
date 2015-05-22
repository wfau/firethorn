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

import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.joda.time.Period;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.entity.AbstractEntityBuilder;
import uk.ac.roe.wfau.firethorn.entity.EntityBuilder;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumnEntity;

/**
 *
        @UniqueConstraint(
            columnNames = {
                BaseComponentEntity.DB_NAME_COL,
                BaseComponentEntity.DB_PARENT_COL
                }
            )
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
            query = "FROM IvoaColumnEntity WHERE ((parentAdql = :parent) AND (name = :name)) ORDER BY ident desc"
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
            {
            return meta.name();
            }

        @Override
        protected void update(final IvoaColumn column, final IvoaColumn.Metadata meta)
            {
            column.update(
                meta
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
            throws EntityNotFoundException
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
        throws NameNotFoundException
            {
            try
                {
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

        @Autowired
        protected IvoaColumn.IdentFactory idents;
        @Override
        public IvoaColumn.IdentFactory idents()
            {
            return this.idents;
            }

        @Autowired
        protected IvoaColumn.LinkFactory links;
        @Override
        public IvoaColumn.LinkFactory links()
            {
            return this.links;
            }

        @Autowired
        protected IvoaColumn.AliasFactory aliases;
        @Override
        public IvoaColumn.AliasFactory aliases()
            {
            return this.aliases;
            }
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
     *
     */
    protected IvoaColumnEntity(final IvoaTable table, final IvoaColumn.Metadata meta)
        {
        super(
            table,
            meta.name()
            );
        this.table = table;
        this.update(
            meta
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
        updatable = true
        )
    private IvoaTable table;
    @Override
    public IvoaTable table()
        {
        return this.table;
        }
    @Override
    public IvoaSchema schema()
        {
        return this.table().schema();
        }
    @Override
    public IvoaResource resource()
        {
        return this.table().resource();
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
        {
        return this.ivoatype;
        }
    protected void ivoatype(final AdqlColumn.AdqlType type)
        {
        this.ivoatype = type;
        }
    @Override
    protected AdqlColumn.AdqlType adqltype()
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
        {
        return this.ivoasize;
        }
    protected void ivoasize(final Integer size)
        {
        this.ivoasize = size;
        }
    @Override
    protected Integer adqlsize()
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
    public String alias()
        {
        return factories().ivoa().columns().aliases().alias(
            this
            );
        }
    @Override
    public String link()
        {
        return factories().ivoa().columns().links().link(
            this
            );
        }

    @Override
    protected void scanimpl()
        {
        // TODO Auto-generated method stub
        }

    /**
     * Generate the IVOA metadata.
     * 
     */
    protected IvoaColumn.Metadata.Ivoa ivoameta()
        {
        return new IvoaColumn.Metadata.Ivoa()
            {
            @Override
            public String name()
                {
                return IvoaColumnEntity.this.name();
                }

            @Override
            public AdqlColumn.AdqlType type()
                {
                return IvoaColumnEntity.this.ivoatype();
                }

            @Override
            public String title()
                {
                return IvoaColumnEntity.this.name();
                }

            @Override
            public String text()
                {
                return IvoaColumnEntity.this.text();
                }

            @Override
            public String utype()
                {
                return IvoaColumnEntity.this.adqlutype();
                }

            @Override
            public String unit()
                {
                return IvoaColumnEntity.this.adqlunit();
                }

            @Override
            public Integer arraysize()
                {
                return IvoaColumnEntity.this.adqlsize();
                }

            @Override
            public String ucd()
                {
                return IvoaColumnEntity.this.adqlucd();
                }
            };
        }
    
    @Override
    public IvoaColumn.Metadata meta()
        {
        return new IvoaColumn.Metadata()
            {
            @Override
            public String name()
                {
                return IvoaColumnEntity.this.name();
                }

            @Override
            public IvoaColumn.Metadata.Ivoa ivoa()
                {
                return ivoameta();
                }

            @Override
            public IvoaColumn.Metadata.Adql adql()
                {
                return adqlmeta();
                }
            };
        }

    @Override
    public void update(IvoaColumn.Metadata update)
        {
        if (update.ivoa() != null)
            {
            this.update(
                update.ivoa()
                );
            }
        }

    @Override
    public void update(IvoaColumn.Metadata.Ivoa update)
        {
        this.adqltype(
            update.type()
            );
        this.adqlsize(
            update.arraysize()
            );

        if (update.text() != null)
            {
            this.text(update.text());
            }
        if (update.ucd() != null)
            {
            this.adqlucd(update.ucd());
            }
        if (update.unit() != null)
            {
            this.adqlunit(update.unit());
            }
        if (update.utype() != null)
            {
            this.adqlutype(update.utype());
            }
        }
    }
