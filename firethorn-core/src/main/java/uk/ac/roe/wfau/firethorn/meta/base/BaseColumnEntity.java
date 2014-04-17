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
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn.Type;

/**
 *
 *
 */
@Entity
@Access(
    AccessType.FIELD
    )
@Inheritance(
    strategy = InheritanceType.TABLE_PER_CLASS
    )
public abstract class BaseColumnEntity<ColumnType extends BaseColumn<ColumnType>>
extends BaseComponentEntity<ColumnType>
    implements BaseColumn<ColumnType>
    {
    /**
     * Hibernate column mapping.
     *
     */
    protected static final String DB_ADQL_TYPE_COL  = "adqltype"  ;
    protected static final String DB_ADQL_SIZE_COL  = "adqlsize"  ;
    protected static final String DB_ADQL_UTYPE_COL = "adqlutype" ;
    protected static final String DB_ADQL_UNITS_COL = "adqlunits" ;
    protected static final String DB_ADQL_UCD_TYPE_COL  = "adqlucdtype"  ;
    protected static final String DB_ADQL_UCD_VALUE_COL = "adqlucdvalue" ;

    protected BaseColumnEntity()
        {
        super();
        }

    protected BaseColumnEntity(final BaseTable<?,ColumnType> parent, final String name)
        {
        super(name);
        //this.parent = parent;
        }

    @Override
    public StringBuilder namebuilder()
        {
        return this.table().namebuilder().append(".").append(this.name());
        }

    /*
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
     */

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
    protected void adqlunits(final String value)
        {
        this.adqlunits = emptystr(
            value
            );
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
    protected void adqlutype(final String value)
        {
        this.adqlutype = emptystr(
            value
            );
        }

    /*
     *
    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_ADQL_UCD_VALUE_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    protected String adqlucdvalue ;
    protected String adqlucdvalue()
        {
        if (this.adqlucdvalue != null)
            {
            return this.adqlucdvalue ;
            }
        else {
            return base().meta().adql().ucdversion();
            }
        }
    protected void adqlucd0(final String value)
        {
        this.adqlucd0 = emptystr(
            value
            );
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
            return base().meta().adql().ucd();
            }
        }
    protected void adqlucd1(final String value)
        {
        this.adqlucd1 = emptystr(
            value
            );
        }
     *
     */

    @Embeddable
    @Access(
        AccessType.FIELD
        )
    public static class UCDEntity
    implements UCD
        {
        protected UCDEntity()
            {
            }

        public UCDEntity(final UCD.Type type, final String value)
            {
            this.ucdtype  = type  ;
            this.ucdvalue = value ;
            }

        @Basic(
            fetch = FetchType.EAGER
            )
        @Column(
            name = DB_ADQL_UCD_TYPE_COL,
            unique = false,
            nullable = true,
            updatable = true
            )
        @Enumerated(
            EnumType.STRING
            )
        private UCD.Type ucdtype  ;

        @Basic(
            fetch = FetchType.EAGER
            )
        @Column(
            name = DB_ADQL_UCD_VALUE_COL,
            unique = false,
            nullable = true,
            updatable = true
            )
        private String ucdvalue ;

        @Override
        public UCD.Type type()
            {
            return this.ucdtype;
            }

        @Override
        public String value()
            {
            return this.ucdvalue;
            }
        }

    @Embedded
    private UCDEntity ucdentity;
    protected UCDEntity ucdentity()
        {
        return this.ucdentity;
        }

    protected void ucdentity(final UCD.Type type, final String value)
        {
        if (value != null)
            {
            this.ucdentity = new UCDEntity(
                type,
                value
                );
            }
        else {
            this.ucdentity = null ;
            }
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
            public UCD ucd()
                {
                return ucdentity();
                }
            @Override
            public void ucd(final UCD.Type type, final String value)
                {
                ucdentity(
                    type,
                    value
                    );
                }
            };
        }
    }
