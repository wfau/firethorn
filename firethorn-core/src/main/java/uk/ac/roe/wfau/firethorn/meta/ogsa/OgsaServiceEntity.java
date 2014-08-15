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

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.URI;
import java.net.URISyntaxException;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

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
            name  = "OgsaService-select-all",
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
    protected static final String DB_SERVICE_ENDPOINT_COL = "endpoint";

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_SERVICE_VERSION_COL = "version";

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
     * @param name The service name.
     * @param endpoint The service endpoint.
     * @throws NameFormatException
     *
     */
    public OgsaServiceEntity(final String endpoint, final String name) throws NameFormatException
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
        name = DB_SERVICE_VERSION_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private String version ;
    @Override
    public String version()
        {
        return this.version;
        }

    @Column(
        name = DB_SERVICE_STATUS_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    @Enumerated(
        EnumType.STRING
        )
    private HttpStatus http ;
    @Override
    public HttpStatus http()
        {
        return http ;
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
                return factories().ogsa().resources().base().select(
                    OgsaServiceEntity.this
                    );
                }

            @Override
            public OgsaJdbcResource create(final JdbcResource source)
                {
                return factories().ogsa().resources().jdbc().create(
                    OgsaServiceEntity.this,
                    source
                    );
                }

            @Override
            public OgsaIvoaResource create(final IvoaResource source)
                {
                return factories().ogsa().resources().ivoa().create(
                    OgsaServiceEntity.this,
                    source
                    );
                }

            @Override
            public Iterable<OgsaJdbcResource> select(final JdbcResource source)
                {
                return factories().ogsa().resources().jdbc().select(
                    OgsaServiceEntity.this,
                    source
                    );
                }

            @Override
            public Iterable<OgsaIvoaResource> select(final IvoaResource source)
                {
                return factories().ogsa().resources().ivoa().select(
                    OgsaServiceEntity.this,
                    source
                    );
                }
            };
        }

    @Override
    public URI baseuri()
    throws URISyntaxException
        {
        if (this.endpoint() == null)
            {
            throw new URISyntaxException(
                null,
                "Null service endpoint",
                0
                );
            }
        else {
            if (this.endpoint().endsWith("/"))
                {
                return new URI(
                    this.endpoint()
                    );                
                }
            else {
                return new URI(
                    this.endpoint() + "/"
                    );                
                }
            }
        }

    /**
     *  Our local HTTP request factory.
     *
     */
    private static final ClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
    
    @Override
    public HttpStatus ping()
        {
        try {
            ClientHttpRequest request = factory.createRequest(
                baseuri().resolve(
                    "services/version"
                    ),
                HttpMethod.GET
                );

            log.debug("Service request [{}][{}]", this.ident(), request.getURI());
            ClientHttpResponse response = request.execute();

            this.http = response.getStatusCode();
            log.debug("Service response [{}][{}]", this.ident(), response.getStatusText());

            this.version = new LineNumberReader(
                new InputStreamReader(
                    response.getBody()
                    )
                ).readLine();
            log.debug("Service version [{}][{}]", this.ident(), this.version());

            }
        catch (URISyntaxException ouch)
            {
            log.warn("Problem occured parsing service endpoint [{}][{}][{}]", this.ident(), ouch.getInput(), ouch.getReason());
            this.http = HttpStatus.BAD_REQUEST;
            }
        catch (IOException ouch)
            {
            log.error("Problem occured sending service request [{}][{}][{}]", this.ident(), this.endpoint(), ouch.getMessage());
            this.http = HttpStatus.BAD_REQUEST;
            }
        return this.http ;
        }
    }
