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
package uk.ac.roe.wfau.firethorn.widgeon.data ;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.entity.AbstractEntity;


/**
 * Generic super class for a DataResource Entities.
 *
 * Problems with AccessType.FIELD means we still have to have get/set methods on fields we want to modify.
 * If we don't include get/set methods, then Hibernate doesn't commit changes to the database.
 *   https://forum.hibernate.org/viewtopic.php?f=1&t=1012254
 *   https://hibernate.onjira.com/browse/HHH-6581
 *   http://javaprogrammingtips4u.blogspot.co.uk/2010/04/field-versus-property-access-in.html
 *
 */
@Slf4j
@MappedSuperclass
@Access(
    AccessType.FIELD
    )
public abstract class DataComponentImpl
extends AbstractEntity
implements DataComponent
    {

    /**
     * The column name for our status value.
     *
     */
    public static final String DB_STATUS_CODE_COL = "status_code" ;

    /**
     * The column name for our status message.
     *
     */
    public static final String DB_STATUS_TEXT_COL = "status_text" ;

    /**
     * Default constructor needs to be protected not private.
     * http://kristian-domagala.blogspot.co.uk/2008/10/proxy-instantiation-problem-from.html
     *
     */
    protected DataComponentImpl()
        {
        super();
        }

    /**
     * Protected constructor, owner defaults to the current actor.
     *
     */
    protected DataComponentImpl(final String name)
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
        name = DB_STATUS_CODE_COL,
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
        this.status(
            status,
            null
            );
        }

    /**
     * The status message.
     *
     */
    @Column(
        name = DB_STATUS_TEXT_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private String message ;

    @Override
    public void status(final Status status, String message)
        {
        this.status  = status ;
        this.message = message ;
        }

    }
