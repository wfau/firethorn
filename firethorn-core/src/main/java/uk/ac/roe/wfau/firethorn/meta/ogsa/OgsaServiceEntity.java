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
import javax.persistence.NamedQuery;
import javax.persistence.NamedQueries;
import javax.persistence.Table;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.entity.AbstractNamedEntity;
import uk.ac.roe.wfau.firethorn.entity.exception.NameFormatException;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaResource;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;

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
    name = OgsaServiceEntity.DB_TABLE_NAME
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "JdbcResource-select-all",
            query = "FROM OgsaServiceEntity ORDER BY name asc, ident desc"
            ),
        }
    )
public class OgsaServiceEntity
    extends AbstractNamedEntity
    implements OgsaService
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
    protected static final String DB_SERVICE_OGSAID_COL = "ogsaid";

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_SERVICE_ENDPOINT_COL = "endpoint";

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_SERVICE_STATUS_COL = "status";
    
    /**
     * Protected constructor. 
     *
     */
    protected OgsaServiceEntity()
        {
        super();
        }

    /**
     * Public constructor.
     * @param endpoint The service endpoint.
     * @throws NameFormatException
     *
     * TODO automatic name generator.
     * 
     */
    public OgsaServiceEntity(final String endpoint) throws NameFormatException
        {
        super(true);
        this.endpoint = endpoint; 
        }

    /**
     * Public constructor.
     * @param name The service name.
     * @param endpoint The service endpoint.
     * @throws NameFormatException
     *
     */
    public OgsaServiceEntity(final String name, final String endpoint) throws NameFormatException
        {
        super(
            name
            );
        this.endpoint = endpoint; 
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_SERVICE_ENDPOINT_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private String endpoint;
    @Override
    public String endpoint()
        {
        return this.endpoint;
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_SERVICE_OGSAID_COL,
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
        name = DB_SERVICE_STATUS_COL,
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
        return status;
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
        return factories().ogsa().services().links().link(
            this
            );
        }

    @Override
    public Resources resources()
        {
        return new Resources()
            {
            @Override
            public Iterable<OgsaBaseResource> select()
                {
                return factories().ogsa().resources().select(
                    OgsaServiceEntity.this
                    );
                }

            @Override
            public OgsaJdbcResource create(final JdbcResource source)
                {
                return factories().ogsa().resources().create(
                    OgsaServiceEntity.this,
                    source
                    );
                }

            @Override
            public OgsaIvoaResource create(final IvoaResource source)
                {
                return factories().ogsa().resources().create(
                    OgsaServiceEntity.this,
                    source
                    );
                }

            @Override
            public Iterable<OgsaJdbcResource> select(final JdbcResource source)
                {
                return factories().ogsa().resources().select(
                    OgsaServiceEntity.this,
                    source
                    );
                }

            @Override
            public Iterable<OgsaIvoaResource> select(final IvoaResource source)
                {
                return factories().ogsa().resources().select(
                    OgsaServiceEntity.this,
                    source
                    );
                }
            };
        }
    }
