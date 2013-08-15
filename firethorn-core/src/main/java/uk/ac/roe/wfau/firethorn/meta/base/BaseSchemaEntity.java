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
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory;

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
    extends BaseComponentEntity
    implements BaseSchema<SchemaType, TableType>
    {

    /**
     * Table resolver implementation.
     *
     */
    @Repository
    public static class Resolver
    extends AbstractEntityFactory<BaseSchema<?,?>>
    implements BaseSchema.Resolver
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
        protected BaseSchema.LinkFactory links;
        @Override
        public BaseSchema.LinkFactory links()
            {
            return this.links;
            }
        }

    protected BaseSchemaEntity()
        {
        super();
        }

    protected BaseSchemaEntity(final BaseResource<SchemaType> resource, final String name)
        {
        this(
            CopyDepth.FULL,
            resource,
            name
            );
        }

    protected BaseSchemaEntity(final CopyDepth type, final BaseResource<SchemaType> resource, final String name)
        {
        super(
            type,
            name
            );
        //this.parent = resource;
        }

    /*
    @Index(
        name=DB_TABLE_NAME + "IndexByParent"
        )
    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = BaseResourceEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = true // TODO - false
        )
    private BaseResource<SchemaType> parent;
    @Override
    public BaseResource<SchemaType> resource()
        {
        return this.parent;
        }
     */

    /**
     * Test method.
     *
    public void resource(final BaseResource<SchemaType> parent)
        {
        this.parent = parent;
        }
     */

    @Override
    public StringBuilder namebuilder()
        {
        return new StringBuilder(
            this.name()
            );
        }
    }
