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

import java.util.UUID;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory;
import uk.ac.roe.wfau.firethorn.entity.AbstractNamedEntity;
import uk.ac.roe.wfau.firethorn.entity.exception.NotFoundException;
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
                AbstractNamedEntity.DB_NAME_COL,
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
    
    /*
    @Repository
    public static class EntityFactory
    extends AbstractEntityFactory<BaseColumn<?>>
        {
        @Override
        public Class<?> etype()
            {
            return BaseColumnEntity.class;
            }

        @Override
        public BaseColumn<?> select(UUID uuid) throws NotFoundException
            {
            // TODO Auto-generated method stub
            return null;
            }

        @Override
        public LinkFactory<BaseColumn<?>> links()
            {
            // TODO Auto-generated method stub
            return null;
            }

        @Override
        public BaseColumn.IdentFactory idents()
            {
            // TODO Auto-generated method stub
            return null;
            }
        }
    */

    protected BaseColumnEntity()
        {
        super();
        }

    protected BaseColumnEntity(final BaseTable<?,ColumnType> parent, final String name)
        {
        super(name);
        this.parent = parent;
        }

    @Override
    public StringBuilder namebuilder()
        {
        return this.table().namebuilder().append(".").append(this.name());
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
    public abstract BaseColumn<?> base();

    @Override
    public abstract BaseColumn<?> root();

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
    protected AdqlColumn.Type adqltype ;
    protected AdqlColumn.Type adqltype()
        {
        if (this.adqltype != null)
            {
            return this.adqltype;
            }
        else {
            return base().meta().adql().type();
            }
        }
    protected void adqltype(final AdqlColumn.Type type)
        {
        this.adqltype = type;
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
    protected Integer adqlsize ;
    protected Integer adqlsize()
        {
        if (this.adqlsize != null)
            {
            return this.adqlsize ;
            }
        else {
            return base().meta().adql().arraysize();
            }
        }
    protected void adqlsize(final Integer size)
        {
        this.adqlsize = size;
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_ADQL_UNITS_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    protected String adqlunits ;
    protected String adqlunits()
        {
        if (this.adqlunits != null)
            {
            return this.adqlunits ;
            }
        else {
            return base().meta().adql().units();
            }
        }
    protected void adqlunits(final String units)
        {
        this.adqlunits = units;
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_ADQL_UTYPE_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    protected String adqlutype ;
    protected String adqlutype()
        {
        if (this.adqlutype != null)
            {
            return this.adqlutype ;
            }
        else {
            return base().meta().adql().utype();
            }
        }
    protected void adqlutype(final String utype)
        {
        this.adqlutype = utype;
        }


    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_ADQL_UCD0_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    protected String adqlucd0 ;
    protected String adqlucd0()
        {
        if (this.adqlucd0 != null)
            {
            return this.adqlucd0 ;
            }
        else {
            return base().meta().adql().ucd0();
            }
        }
    protected void adqlucd0(final String ucd)
        {
        this.adqlucd0 = ucd;
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_ADQL_UCD1_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    protected String adqlucd1 ;
    protected String adqlucd1()
        {
        if (this.adqlucd1 != null)
            {
            return this.adqlucd1 ;
            }
        else {
            return base().meta().adql().ucd1();
            }
        }
    protected void adqlucd1(final String ucd)
        {
        this.adqlucd1 = ucd;
        }

    @Override
    public BaseColumn.Metadata meta()
        {
        return new BaseColumn.Metadata()
            {
            @Override
            public BaseColumn.Metadata.AdqlMetadata adql()
                {
                return adqlmeta();
                }
            };
        }

    protected BaseColumn.Metadata.AdqlMetadata adqlmeta()
        {
        return new BaseColumn.Metadata.AdqlMetadata()
            {
            @Override
            public Integer arraysize()
                {
                return adqlsize();
                }
            @Override
            public void arraysize(final Integer size)
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
            public String units()
                {
                return adqlunits();
                }
            @Override
            public void units(final String units)
                {
                adqlunits(
                    units
                    );
                }

            @Override
            public String utype()
                {
                return adqlutype();
                }
            @Override
            public void utype(final String utype)
                {
                adqlutype(
                    utype
                    );
                }

            @Override
            public String ucd0()
                {
                return adqlucd0();
                }
            @Override
            public void ucd0(final String ucd)
                {
                adqlucd0(
                    ucd
                    );
                }

            @Override
            public String ucd1()
                {
                return adqlucd1();
                }
            @Override
            public void ucd1(final String ucd)
                {
                adqlucd1(
                    ucd
                    );
                }
            };
        }
    }
