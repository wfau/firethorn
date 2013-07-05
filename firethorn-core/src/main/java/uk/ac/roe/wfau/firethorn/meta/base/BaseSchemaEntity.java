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
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.NamedQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory;

/**
 *
 *
 */
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = BaseSchemaEntity.DB_TABLE_NAME,
    uniqueConstraints={
        @UniqueConstraint(
            name = BaseSchemaEntity.DB_TABLE_NAME + BaseComponentEntity.DB_PARENT_NAME_IDX,
            columnNames = {
                BaseComponentEntity.DB_NAME_COL,
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
public abstract class BaseSchemaEntity<SchemaType extends BaseSchema<SchemaType, TableType>, TableType extends BaseTable<TableType, ?>>
    extends BaseComponentEntity
    implements BaseSchema<SchemaType, TableType>
    {
    /**
     * Hibernate database table name.
     *
     */
    protected static final String DB_TABLE_NAME = "BaseSchemaEntity";

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
        super(name);
        this.parent = resource;
        }

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
    /**
     * Test method.
     *
     */
    public void resource(final BaseResource<SchemaType> parent)
        {
        this.parent = parent;
        }

    @Override
    public StringBuilder fullname()
        {
        return new StringBuilder(this.name());
        }
    }
