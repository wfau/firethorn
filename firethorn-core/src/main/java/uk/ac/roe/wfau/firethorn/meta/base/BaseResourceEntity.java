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
import javax.persistence.Column;
import javax.persistence.Entity;
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
public abstract class BaseResourceEntity<ResourceType extends BaseResource<SchemaType>,  SchemaType extends BaseSchema<SchemaType,?>>
    extends BaseComponentEntity<ResourceType>
    implements BaseResource<SchemaType>
    {

    /**
     * Hibernate column mapping, {@value}.
     * 
     */
    protected static final String DB_OGSAID_COL = "ogsaid";
    
    /**
     * Protected constructor.
     *
     */
    protected BaseResourceEntity()
        {
        super();
        }

    /**
     * Protected constructor.
     *
     */
    protected BaseResourceEntity(final String name)
        {
        super(
            name
            );
        }

    @Override
    public StringBuilder namebuilder()
        {
        return new StringBuilder(
            this.name()
            );
        }

    /**
     * The OGSA-DAI resource ID.
     *
     */
    @Column(
        name = DB_OGSAID_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    protected String ogsaid;
    @Override
    public String ogsaid()
        {
        return this.ogsaid;
        }
    @Override
    public void ogsaid(final String ogsaid)
        {
        this.ogsaid = ogsaid;
        }

    /**
     * Generate the {@link BaseResource.Metadata.Ogsa ogsa} metadata.
     *
     */
    protected BaseResource.Metadata.Ogsa ogsameta()
        {
        return new BaseResource.Metadata.Ogsa()
            {
            @Override
            public String id()
                {
                return BaseResourceEntity.this.ogsaid;
                }
            };
        }
    }
