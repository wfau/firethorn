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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.NamedQueries;
import javax.persistence.Table;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.entity.AbstractNamedEntity;
import uk.ac.roe.wfau.firethorn.entity.exception.NameFormatException;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResourceEntity;

/**
 *
 *
 */
@Slf4j
@Entity
@Access(
    AccessType.FIELD
    )
@Table(
    name = OgsaJdbcResourceEntity.DB_TABLE_NAME
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "OgsaJdbcResource-select-all",
            query = "FROM OgsaJdbcResourceEntity ORDER BY name asc, ident desc"
            ),
        }
    )
public class OgsaJdbcResourceEntity
    extends AbstractNamedEntity
    implements OgsaJdbcResource
    {
    /**
     * Hibernate table mapping, {@value}.
     *
     */
    protected static final String DB_TABLE_NAME = DB_TABLE_PREFIX + "OgsaServiceEntity";

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
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_RESOURCE_SOURCE_COL = "source";
    
    /**
     * Protected constructor. 
     *
     */
    public OgsaJdbcResourceEntity()
        {
        super();
        }

    /**
    *
    * Public constructor.
    * @param name The resource name.
    * @param service The parent {@link OgsaService}
    * @param source  The source {@link JdbcResource}
    * @throws NameFormatException
    *
    */
   public OgsaJdbcResourceEntity(final OgsaService service, final JdbcResource source) throws NameFormatException
       {
       this(
           service,
           source,
           "fred"
           );
       }

   /**
     *
     * Public constructor.
     * @param name The resource name.
     * @param service The parent {@link OgsaService}
     * @param source  The source {@link JdbcResource}
     * @throws NameFormatException
     *
     */
    public OgsaJdbcResourceEntity(final OgsaService service, final JdbcResource source, final String name) throws NameFormatException
        {
        super(
            name
            );
        this.source  = source  ;
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

    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = JdbcResourceEntity.class
        )
    @JoinColumn(
        name = DB_RESOURCE_SOURCE_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private JdbcResource source;
    @Override
    public JdbcResource source()
        {
        return this.source;
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
    private Status status = Status.CREATED ;
    @Override
    public Status status()
        {
        return this.status;
        }

    @Override
    public Status ping()
        {
        // TODO Auto-generated method stub
        return null;
        }

    @Override
    public String link()
        {
        return factories().ogsa().resources().jdbc().links().link(
            this
            );
        }
    }
