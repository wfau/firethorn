/*
 *  Copyright (C) 2014 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.meta.ogsa;

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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import uk.ac.roe.wfau.firethorn.entity.AbstractEntity;

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
public abstract class OgsaBaseResourceEntity
extends AbstractEntity
implements OgsaBaseResource
    {
    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_RESOURCE_OGSAID_COL = "ogsaid";

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_RESOURCE_STATUS_COL = "status";

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_RESOURCE_SERVICE_COL = "service";

    /**
     * Protected constructor.
     *
     */
    public OgsaBaseResourceEntity()
        {
        super();
        }

    /**
    *
    * Protected constructor.
    * @param service The parent {@link OgsaService}.
    *
    */
   protected OgsaBaseResourceEntity(final OgsaService service)
       {
       super();
       this.service = service ;
       }

   @ManyToOne(
       fetch = FetchType.LAZY,
       targetEntity = OgsaServiceEntity.class
       )
   @JoinColumn(
       name = DB_RESOURCE_SERVICE_COL,
       unique = false,
       nullable = false,
       updatable = false
       )
   private OgsaService service;
   @Override
   public OgsaService service()
       {
       return this.service;
       }

   @Basic(
       fetch = FetchType.EAGER
       )
   @Column(
       name = DB_RESOURCE_OGSAID_COL,
       unique = false,
       nullable = true,
       updatable = true
       )
   private String ogsaid;
   @Override
   public String ogsaid()
       {
       return ogsaid;
       }

   @Column(
       name = DB_RESOURCE_STATUS_COL,
       unique = false,
       nullable = false,
       updatable = true
       )
   @Enumerated(
       EnumType.STRING
       )
   private Status status = Status.UNKNOWN ;
   @Override
   public Status status()
       {
       return this.status;
       }
    }
