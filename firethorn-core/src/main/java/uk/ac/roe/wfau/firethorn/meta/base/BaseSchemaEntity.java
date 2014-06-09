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
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory;

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
public abstract class BaseSchemaEntity<SchemaType extends BaseSchema<SchemaType, TableType>, TableType extends BaseTable<TableType, ?>>
extends BaseComponentEntity<SchemaType>
implements BaseSchema<SchemaType, TableType>
    {

    /**
     * Schema resolver implementation.
     *
     */
    @Repository
    public static class Resolver<SchemaType extends BaseSchema<SchemaType, ?>>
    extends AbstractEntityFactory<SchemaType>
    implements BaseSchema.Resolver<SchemaType>
        {
        @Override
        public Class<?> etype()
            {
            return BaseSchemaEntity.class;
            }

        @Autowired
        protected BaseSchema.IdentFactory idents ;
        @Override
        public BaseSchema.IdentFactory idents()
            {
            return this.idents;
            }

        @Autowired
        protected BaseSchema.LinkFactory<SchemaType> links;
        @Override
        public BaseSchema.LinkFactory<SchemaType> links()
            {
            return this.links;
            }
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
    }
