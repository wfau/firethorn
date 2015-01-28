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

import java.net.MalformedURLException;
import java.net.URL;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.entity.exception.NameFormatException;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResourceEntity;
import uk.ac.roe.wfau.firethorn.meta.ogsa.OgsaBaseResource.Status;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.SimpleResourceWorkflowResult;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.WorkflowResult;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.jdbc.JdbcCreateResourceWorkflow;

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
            query = "FROM OgsaJdbcResourceEntity ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "OgsaJdbcResource-select-service",
            query = "FROM OgsaJdbcResourceEntity WHERE service = :service ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "OgsaJdbcResource-select-source",
            query = "FROM OgsaJdbcResourceEntity WHERE source = :source ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "OgsaJdbcResource-select-service-source",
            query = "FROM OgsaJdbcResourceEntity WHERE service = :service AND source = :source ORDER BY ident desc"
            )
        }
    )
public class OgsaJdbcResourceEntity
extends OgsaBaseResourceEntity
implements OgsaJdbcResource
    {
    /**
     * Hibernate table mapping, {@value}.
     *
     */
    protected static final String DB_TABLE_NAME = DB_TABLE_PREFIX + "OgsaJdbcResource";
    
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
     * @param service The parent {@link OgsaService}
     * @param source  The source {@link JdbcResource}
     *
     */
    public OgsaJdbcResourceEntity(final OgsaService service, final JdbcResource source)
        {
        super(
            service
            );
        this.source = source  ;
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

    @Override
    public String link()
        {
        return factories().ogsa().factories().jdbc().links().link(
            this
            );
        }

    @Override
    public String ogsaid()
        {
        if (this.ogsaid == null)
            {
            this.init();
            }
        return this.ogsaid;
        }

    @Override
    public Status connect()
        {
        if (this.ogsaid != null)
            {
            return status() ;
            }
        else {
            return this.init();
            }
        }
        
    protected Status init()
        {
        JdbcCreateResourceWorkflow workflow = null;
        try {
            workflow = new JdbcCreateResourceWorkflow(
                service().endpoint()
                );
            }
        catch (MalformedURLException ouch)
            {
            return status(
                Status.ERROR
                );
            }

        final SimpleResourceWorkflowResult response = workflow.execute(
            new JdbcCreateResourceWorkflow.Param()
                {
                @Override
                public String jdbcurl()
                    {
                    return source.connection().uri();
                    }
                @Override
                public String username()
                    {
                    return source.connection().user();
                    }
                @Override
                public String password()
                    {
                    return source.connection().pass();
                    }
                @Override
                public String driver()
                    {
                    return source.connection().driver();
                    }
                @Override
                public boolean writable()
                    {
                    return false;
                    }
                }
            );

        log.debug("Status  [{}]", response.status());
        log.debug("Created [{}]", response.result());

        if (response.status() == WorkflowResult.Status.COMPLETED)
            {
            ogsaid(
                response.result().toString()
                );
            return status(
                Status.ACTIVE
                );
            }

        else {
            return status(
                Status.ERROR
                );
            }
        }

    @Override
    public Status release()
        {
        throw new UnsupportedOperationException(
            "Release not implemented yet"
            );
        }
    }
        
