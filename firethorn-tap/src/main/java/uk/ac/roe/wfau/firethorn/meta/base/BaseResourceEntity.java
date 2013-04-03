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
import javax.persistence.Table;

import org.hibernate.annotations.NamedQueries;

/**
 *
 *
 */
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = BaseResourceEntity.DB_TABLE_NAME,
    uniqueConstraints={
        }
    )
@Inheritance(
    strategy = InheritanceType.JOINED
    )
@NamedQueries(
        {
        }
    )
public abstract class BaseResourceEntity<SchemaType extends BaseSchema<SchemaType,?>>
    extends BaseComponentEntity
    implements BaseResource<SchemaType>
    {
    /**
     * Hibernate database table name.
     *
     */
    protected static final String DB_TABLE_NAME = "BaseResourceEntity";

    protected BaseResourceEntity()
        {
        super();
        }

    protected BaseResourceEntity(final String name)
        {
        super(name);
        }

    @Override
    public StringBuilder fullname()
        {
        return new StringBuilder(this.name());
        }

    /**
     * The the OGSA-DAI resource ID.
     * @todo Move this to Jdbc and Ivoa classes - replace with error/warn on base class.
     * @todo Create a new entity for OGSA-DAI resources.
     *
    protected static final String DB_OGSA_ID_COL = "ogsaid";
    @Column(
        name = DB_OGSA_ID_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private String ogsaid;
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
     */
    @Override
    public String ogsaid()
        {
        return null;
        }
    @Override
    public void ogsaid(final String ogsaid)
        {
        }

    }
