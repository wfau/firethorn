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
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.ProxyIdentifier;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierFormatException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaSchema;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcSchema;

/**
 *
 *
 */
@Slf4j
@Entity
@Access(
    AccessType.FIELD
    )
@Inheritance(
    strategy = InheritanceType.TABLE_PER_CLASS
    )
public abstract class BaseSchemaEntity<SchemaType extends BaseSchema<SchemaType, TableType>, TableType extends BaseTable<TableType, ?>>
extends BaseComponentEntity<SchemaType>
implements BaseSchema<SchemaType, TableType>
    {
    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_ADQL_UTYPE_COL = "adqlutype" ;

    /**
     * {@link BaseSchema.EntityResolver} implementation.
     *
     */
    @Repository
    public static class EntityResolver
    implements BaseSchema.EntityResolver
        {
        @Override
        public BaseSchema<?,?> resolve(String link)
        throws IdentifierFormatException, IdentifierNotFoundException, EntityNotFoundException
            {
            if (adql.matches(link))
                {
                return adql.resolve(
                    link
                    );
                }
            else if (jdbc.matches(link))
                {
                return jdbc.resolve(
                    link
                    );
                }
            if (ivoa.matches(link))
                {
                return ivoa.resolve(
                    link
                    );
                }
            else {
                throw new EntityNotFoundException(
                    "Unable to find match for [" + link + "]"
                    );
                }
            }

        @Autowired
        private AdqlSchema.LinkFactory adql;
        @Autowired
        private JdbcSchema.LinkFactory jdbc;
        @Autowired
        private IvoaSchema.LinkFactory ivoa;
        
        }

    /**
     * Protected constructor.
     *
     */
    protected BaseSchemaEntity()
        {
        super();
        }

    /**
     * Protected constructor.
     * @todo Remove the parent reference. 
     *
     */
    protected BaseSchemaEntity(final BaseResource<SchemaType> resource, final String name)
        {
        this(
            CopyDepth.FULL,
            resource,
            name
            );
        }

    /**
     * Protected constructor.
     * @todo Remove the parent reference. 
     *
     */
    protected BaseSchemaEntity(final CopyDepth type, final BaseResource<SchemaType> resource, final String name)
        {
        super(
            type,
            name
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
    
    @Override
    public StringBuilder namebuilder()
        {
        if (this.name() != null)
            {
            return new StringBuilder(
                this.name()
                );
            }
        else {
            return new StringBuilder();
            }
        }

    /**
     * Generate the {@link AdqlSchema.Metadata.Adql adql} metadata.
     *
     */
    protected AdqlSchema.Metadata.Adql adqlmeta()
        {
        return new AdqlSchema.Metadata.Adql()
            {
            @Override
            public String name()
                {
                return BaseSchemaEntity.this.name();
                }

            @Override
            public String text()
                {
                return BaseSchemaEntity.this.text();
                }
            };
        }

    @Override
    public AdqlSchema.Metadata meta()
        {
        return new AdqlSchema.Metadata()
            {
            @Override
            public String name()
                {
                return BaseSchemaEntity.this.name();
                }
            @Override
            public AdqlSchema.Metadata.Adql adql()
                {
                return adqlmeta();
                }
            };
        }
    }
