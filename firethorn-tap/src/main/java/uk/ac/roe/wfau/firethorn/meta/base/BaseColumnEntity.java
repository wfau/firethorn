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
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumnInfo;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumnType;

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
    protected static final String DB_ADQL_TYPE_COL = "adqltype" ;
    protected static final String DB_ADQL_SIZE_COL = "adqlsize" ;
    protected static final String DB_ADQL_UCD1_COL = "adqlucd1"  ;

    protected static final String DB_USER_TYPE_COL = "usertype" ;
    protected static final String DB_USER_SIZE_COL = "usersize" ;
    protected static final String DB_USER_UCD1_COL = "userucd1"  ;


    protected BaseColumnEntity()
        {
        super();
        }

    protected BaseColumnEntity(final BaseTable<?,ColumnType> parent, final String name)
        {
        super(name);
        this.parent = parent;
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Enumerated(
        EnumType.STRING
        )
    @Column(
        name = DB_ADQL_TYPE_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    protected AdqlColumn.Type adqltype ;

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_USER_TYPE_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    protected AdqlColumn.Type usertype ;

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_ADQL_SIZE_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    protected Integer adqlsize ;

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_USER_SIZE_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    protected Integer usersize ;

    @Override
    public String alias()
        {
        return "base_column_" + ident();
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
        fetch = FetchType.EAGER,
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
                return null;
                }
            };
        }

    @Override
    public abstract BaseColumn<?> base();

    @Override
    public abstract BaseColumn<?> root();

    @Override
    public AdqlColumn.Info info()
        {
        return new AdqlColumn.Info()
            {
            @Override
            public AdqlColumn.Info.Meta adql()
                {
                return new AdqlColumn.Info.Meta()
                    {
                    @Override
                    public Integer size()
                        {
                        if (BaseColumnEntity.this.usersize != null)
                            {
                            return BaseColumnEntity.this.usersize;
                            }
                        else {
                            return BaseColumnEntity.this.adqlsize;
                            }
                        }

                    @Override
                    public void size(final Integer size)
                        {
                        BaseColumnEntity.this.usersize = size ;
                        if (size != null)
                            {
                            BaseColumnEntity.this.adqlsize = size ;
                            }
                        else {
                            BaseColumnEntity.this.adqlsize = base().info().adql().size();
                            }
                        }

                    @Override
                    public AdqlColumn.Type type()
                        {
                        if (BaseColumnEntity.this.usertype != null)
                            {
                            return BaseColumnEntity.this.usertype;
                            }
                        else {
                            return BaseColumnEntity.this.adqltype;
                            }
                        }

                    @Override
                    public void type(final AdqlColumn.Type type)
                        {
                        BaseColumnEntity.this.usertype = type ;
                        if (type != null)
                            {
                            BaseColumnEntity.this.adqltype = type ;
                            }
                        else {
                            BaseColumnEntity.this.adqltype = base().info().adql().type();
                            }
                        }
                    };
                }
            };
        }
    }
