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
package uk.ac.roe.wfau.firethorn.meta.base;

import java.util.Collection;
import java.util.Collections;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.NamedQueries;

import uk.ac.roe.wfau.firethorn.entity.AbstractEntity;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn.Type;

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
    name = BaseColumnEntity.DB_TABLE_NAME,
    uniqueConstraints={
        @UniqueConstraint(
            columnNames = {
                AbstractEntity.DB_NAME_COL,
                BaseComponentEntity.DB_PARENT_COL,
                }
            )
        }
    )
@Inheritance(
    strategy = InheritanceType.JOINED
    )
@NamedQueries(
        {
        }
    )
public abstract class BaseColumnEntity<ColumnType extends BaseColumn<ColumnType>>
extends BaseComponentEntity
    implements BaseColumn<ColumnType>
    {
    /**
     * Hibernate table mapping.
     *
     */
    protected static final String DB_TABLE_NAME = "BaseColumnEntity";

    /**
     * Hibernate column mapping.
     *
     */
    protected static final String DB_ADQL_TYPE_COL  = "adqltype"  ;
    protected static final String DB_ADQL_SIZE_COL  = "adqlsize"  ;
    protected static final String DB_ADQL_UCD0_COL  = "adqlucd0"  ;
    protected static final String DB_ADQL_UCD1_COL  = "adqlucd1"  ;
    protected static final String DB_ADQL_UTYPE_COL = "adqlutype" ;
    protected static final String DB_ADQL_UNITS_COL = "adqlunits" ;

    protected static final String DB_USER_TYPE_COL  = "usertype"  ;
    protected static final String DB_USER_SIZE_COL  = "usersize"  ;
    protected static final String DB_USER_UCD0_COL  = "userucd0"  ;
    protected static final String DB_USER_UCD1_COL  = "userucd1"  ;
    protected static final String DB_USER_UTYPE_COL = "userutype" ;
    protected static final String DB_USER_UNITS_COL = "userunits" ;

    protected BaseColumnEntity()
        {
        super();
        }

    protected BaseColumnEntity(final BaseTable<?,ColumnType> parent, final String name)
        {
        super(name);
        this.parent = parent;
        }

    protected AdqlColumn.Type basetype()
        {
        return basetype(
            false
            );
        }
    protected abstract AdqlColumn.Type basetype(final boolean pull);

    protected Integer basesize()
        {
        return basesize(
            false
            );
        }
    protected abstract Integer basesize(final boolean pull);

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_USER_TYPE_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    @Enumerated(
        EnumType.STRING
        )
    private AdqlColumn.Type usertype ;
    protected AdqlColumn.Type usertype()
        {
        return this.usertype;
        }
    protected void usertype(final AdqlColumn.Type type)
        {
        this.usertype = type;
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_ADQL_TYPE_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    @Enumerated(
        EnumType.STRING
        )
    private AdqlColumn.Type adqltype ;
    protected AdqlColumn.Type adqltype()
        {
        return adqltype(
            false
            );
        }
    protected AdqlColumn.Type adqltype(final boolean pull)
        {
        if ((this.adqltype == null) || (pull))
            {
            this.adqltype = basetype(
                pull
                );
            if (null != this.usertype)
                {
                this.adqltype = this.usertype;
                }
            }
        return this.adqltype;
        }
    protected void adqltype(final AdqlColumn.Type type)
        {
        this.adqltype = type;
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_USER_SIZE_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private Integer usersize ;
    protected Integer usersize()
        {
        return this.usersize;
        }
    protected void usersize(final Integer size)
        {
        this.usersize = size;
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_ADQL_SIZE_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private Integer adqlsize ;
    protected Integer adqlsize()
        {
        return adqlsize(
            false
            );
        }
    protected Integer adqlsize(final boolean pull)
        {
        if ((this.adqlsize == null) || (pull))
            {
            this.adqlsize = basesize(
                pull
                );
            if (null != this.usersize)
                {
                this.adqlsize = this.usersize;
                }
            }
        return this.adqlsize;
        }
    protected void adqlsize(final Integer size)
        {
        this.adqlsize = size;
        }

    @Override
    public String alias()
        {
        return "BASE_" + ident();
        }

    @Override
    public StringBuilder fullname()
        {
        return this.table().fullname().append(".").append(this.name());
        }

    @Index(
        name=DB_TABLE_NAME + "IndexByParent"
        )
    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = BaseTableEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = true
        )
    private BaseTable<?,ColumnType> parent;
    @Override
    public BaseTable<?,ColumnType> table()
        {
        return this.parent;
        }
    @Override
    public BaseSchema<?,?> schema()
        {
        return this.table().schema();
        }
    @Override
    public BaseResource<?> resource()
        {
        return this.table().resource();
        }

    @Override
    public Linked linked()
        {
        return new Linked()
            {
            @Override
            public Iterable<AdqlColumn> select()
                {
                //"SELECT FROM AdqlColumn WHERE base = :base"
                return Collections.emptyList();
                }
            };
        }

    @Override
    public abstract BaseColumn<?> base();

    @Override
    public abstract BaseColumn<?> root();

    @Override
    public BaseColumn.Metadata meta()
        {
        return new BaseColumn.Metadata()
            {
            @Override
            public BaseColumn.Metadata.AdqlMeta adql()
                {
                return adqlmeta();
                }
            };
        }

    protected BaseColumn.Metadata.AdqlMeta adqlmeta()
        {
        return new BaseColumn.Metadata.AdqlMeta()
            {
            @Override
            public Integer array()
                {
                return adqlsize();
                }
            @Override
            public void array(final Integer size)
                {
                adqlsize(
                    size
                    );
                }
            @Override
            public AdqlColumn.Type type()
                {
                return adqltype();
                }
            @Override
            public void type(final Type type)
                {
                adqltype(
                    type
                    );
                }

            @Override
            public String unit()
                {
                return "units";
                }
            @Override
            public void unit(final String unit)
                {
                }

            @Override
            public String utype()
                {
                return "utype";
                }
            @Override
            public void utype(final String utype)
                {
                }

            @Override
            public String newucd()
                {
                return "new-ucd";
                }
            @Override
            public void newucd(final String ucd)
                {
                }
            @Override
            public String olducd()
                {
                return "old-ucd";
                }
            @Override
            public void olducd(final String ucd)
                {
                }
            };
        }
    }
