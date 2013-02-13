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
package uk.ac.roe.wfau.firethorn.tuesday;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

import uk.ac.roe.wfau.firethorn.common.entity.AbstractEntity;

/**
 *
 *
 */
@MappedSuperclass
@Access(
    AccessType.FIELD
    )
public abstract class BaseComponentEntity
extends AbstractEntity
    implements BaseComponent
    {
    /**
     * Hibernate column mapping.
     * 
     */
    protected static final String DB_BASE_COL   = "base";
    protected static final String DB_PARENT_COL = "parent";
    protected static final String DB_STATUS_COL = "status";
    protected static final String DB_ALIAS_COL  = "alias";
    protected static final String DB_TYPE_COL   = "datatype" ;
    protected static final String DB_SIZE_COL   = "datasize" ;
    protected static final String DB_UCD_COL    = "ucd"  ;

    protected static final String DB_NAME_IDX        = "IndexByName";
    protected static final String DB_PARENT_IDX      = "IndexByParent";
    protected static final String DB_PARENT_NAME_IDX = "IndexByParentAndName";

    protected static final String DB_RESOURCE_COL = "resource";
    protected static final String DB_SCHEMA_COL   = "schema";
    protected static final String DB_TABLE_COL    = "table";
    protected static final String DB_COLUMN_COL   = "column";


    /**
     * Default constructor needs to be protected not private.
     * http://kristian-domagala.blogspot.co.uk/2008/10/proxy-instantiation-problem-from.html
     *
     */
    protected BaseComponentEntity()
        {
        super();
        }

    /**
     * Protected constructor, owner defaults to the current actor.
     *
     */
    protected BaseComponentEntity(final String name)
        {
        super(
            name
            );
        }

    /**
     * The component status.
     *
     */
    @Column(
        name = DB_STATUS_COL,
        unique = false,
        nullable = false,
        updatable = true
        )
    @Enumerated(
        EnumType.STRING
        )
    private Status status = Status.CREATED ;

    @Override
    public Status status()
        {
        return this.status;
        }

    @Override
    public void status(final Status status)
        {
        this.status = status;
        }
    }
