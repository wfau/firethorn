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

import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;

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
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_ADQL_TYPE_COL  = "adqltype"  ;

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_ADQL_SIZE_COL  = "adqlsize"  ;

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_ADQL_UTYPE_COL = "adqlutype" ;

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_ADQL_DTYPE_COL = "adqldtype" ;

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_ADQL_UNIT_COL = "adqlunit" ;

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_ADQL_UCD_TYPE_COL  = "adqlucdtype"  ;

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_ADQL_UCD_VALUE_COL = "adqlucdvalue" ;

    /**
     * Protected constructor.
     *
     */
    protected BaseColumnEntity()
        {
        super();
        }

    /**
     * Protected constructor.
     * @todo Remove the parent reference.
     *
     */
    protected BaseColumnEntity(final BaseTable<?,ColumnType> parent, final String name)
        {
        super(name);
        }

    @Override
    public StringBuilder namebuilder()
        {
        StringBuilder builder = this.table().namebuilder();
        if (this.name() != null)
            {
            if (builder.length() > 0)
                {
                builder.append(".");
                }
            builder.append(this.name());
            }
        return builder;
        }

    @Override
    public String fullname()
        {
        return namebuilder().toString();
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
    protected AdqlColumn.AdqlType adqltype ;
    protected AdqlColumn.AdqlType adqltype()
        {
        return this.adqltype;
        }
    protected void adqltype(final AdqlColumn.AdqlType type)
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
        return this.adqlsize ;
        }
    protected void adqlsize(final Integer size)
        {
        this.adqlsize = size;
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_ADQL_UNIT_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    protected String adqlunit ;
    protected String adqlunit()
        {
        return this.adqlunit ;
        }
    protected void adqlunit(final String value)
        {
        this.adqlunit = emptystr(
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
        return this.adqlutype ;
        }
    protected void adqlutype(final String value)
        {
        this.adqlutype = emptystr(
            value
            );
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_ADQL_DTYPE_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    protected String adqldtype ;
    protected String adqldtype()
        {
        return this.adqldtype ;
        }
    protected void adqldtype(final String value)
        {
        this.adqldtype = emptystr(
            value
            );
        }
    
    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_ADQL_UCD_VALUE_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    protected String adqlucd ;
    protected String adqlucd()
        {
        return this.adqlucd ;
        }
    protected void adqlucd(final String value)
        {
        this.adqlucd = emptystr(
            value
            );
        }

    /**
     * Generate the {@link AdqlColumn.Metadata.Adql adql} metadata.
     *
     */
    protected AdqlColumn.Metadata.Adql adqlmeta()
        {
        return new AdqlColumn.Metadata.Adql()
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
            public AdqlColumn.AdqlType type()
                {
                return adqltype();
                }
            @Override
            public void type(final AdqlColumn.AdqlType type)
                {
                adqltype(
                    type
                    );
                }

            @Override
            public String units()
                {
                return adqlunit();
                }
            @Override
            public void units(final String units)
                {
                adqlunit(
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
            public String dtype()
                {
                return adqldtype();
                }
            @Override
            public void dtype(final String dtype)
                {
                adqldtype(
                    dtype
                    );
                }

            @Override
            public String ucd()
                {
                return adqlucd();
                }
            @Override
            public void ucd(final String value)
                {
                adqlucd(
                    value
                    );
                }
            @Override
            public void ucd(final String type, final String value)
                {
                adqlucd(
                    value
                    );
                }
            @Override
            public String name()
                {
                return BaseColumnEntity.this.name();
                }
            @Override
            public String text()
                {
                return BaseColumnEntity.this.text();
                }
            };
        }

    @Override
    public AdqlColumn.Metadata meta()
        {
        return new AdqlColumn.Metadata()
            {
            @Override
            public String name()
                {
                return BaseColumnEntity.this.name();
                }
            @Override
            public AdqlColumn.Metadata.Adql adql()
                {
                return adqlmeta();
                }
            };
        }
    }
