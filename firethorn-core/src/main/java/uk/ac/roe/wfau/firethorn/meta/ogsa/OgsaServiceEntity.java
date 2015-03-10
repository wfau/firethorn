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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory;
import uk.ac.roe.wfau.firethorn.entity.AbstractNamedEntity;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateAtomicMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityServiceException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameFormatException;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaResource;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;

/**
 * {@link OgsaService} implementation.
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
            query = "FROM OgsaServiceEntity ORDER BY ident DESC"
            ),
        @NamedQuery(
            name  = "OgsaService-select-status",
            query = "FROM OgsaServiceEntity WHERE status = :status ORDER BY ident DESC"
            )
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
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_HTTP_STATUS_COL = "http";

    /**
     * {@link OgsaService.EntityFactory} implementation.
     *
     */
    @Component
    @Repository
    public static class OgsaServiceEntityFactory
    extends AbstractEntityFactory<OgsaService>
    implements OgsaService.EntityFactory, OgsaService.EndpointFactory
        {
        @Override
        public Class<?> etype()
            {
            return OgsaServiceEntity.class ;
            }

        @Autowired
        private OgsaService.IdentFactory idents;
        @Override
        public OgsaService.IdentFactory idents()
            {
            return this.idents;
            }

        @Autowired
        private OgsaService.LinkFactory links;
        @Override
        public OgsaService.LinkFactory links()
            {
            return this.links;
            }

        @Override
        @SelectMethod
        public Iterable<OgsaService> select()
            {
            return super.iterable(
                super.query(
                    "OgsaService-select-all"
                    )
                );
            }

        @Override
        @SelectMethod
        public Iterable<OgsaService> select(final Status status)
            {
            return super.iterable(
                super.query(
                    "OgsaService-select-status"
                    ).setEntity(
                        "status",
                        status
                        )
                );
            }

        @Value("${firethorn.ogsadai.endpoint}")
        private String endpoint ;

        @Override
        @CreateAtomicMethod
        public OgsaService primary()
            {
            log.debug("primary()");
            // Really really simple - just get the first ACTIVE service.
            OgsaService service = super.first(
                super.query(
                    "OgsaService-select-status"
                    ).setString(
                        "status",
                        OgsaService.Status.ACTIVE.name()
                        )
                );
            
            // If we don't have an ACTIVE service, create a new one.
            // Bug - this can create multiple broken services.
            if (service == null)
                {
                log.debug("-------------------------------------------------");
                log.debug("No primary OgsaService found - creating a new one");
                if (endpoint == null)
                    {
                    throw new PrimaryServiceException(
                        "Primary OGSA-DAI service endpoint is null"
                        ); 
                    }
                else {
                    try {
                      service = create(
                      //service = factories().ogsa().services().create(
                            endpoint
                            );
                        }
                    catch (Exception ouch)
                        {
                        throw new PrimaryServiceException(
                            "Unable to create primary OGSA-DAI service",
                            ouch
                            ); 
                        }
                    if (service.status() != Status.ACTIVE)
                        {
                        throw new PrimaryServiceException(
                            "Unable to create ACTIVE OGSA-DAI service"
                            ); 
                        }
                    }
                }
            return service ;
            }

        @Override
        @CreateAtomicMethod
        public OgsaService create(final String endpoint)
            {
            return create(
                endpoint,
                endpoint
                );
            }

        @Override
        @CreateAtomicMethod
        public OgsaService create(final String name, final String endpoint)
            {
            return super.insert(
                new OgsaServiceEntity(
                    name,
                    endpoint
                    )
                );
            }

        @Override
        @CreateAtomicMethod
        public OgsaService create(final String proto, final String host, final Integer port, final String path)
            {
            return this.create(
                endpoint(
                    proto,
                    host,
                    port,
                    path
                    )
                );
        }

        @Override
        @CreateAtomicMethod
        public OgsaService create(final String name, final String proto, final String host, final Integer port, final String path)
            {
            return this.create(
                name,
                endpoint(
                    proto,
                    host,
                    port,
                    path
                    )
                );
            }
        
        @Value("${ogsadai.endpoint.proto:http}")
        private String DEFAULT_PROTO ;

        @Value("${ogsadai.endpoint.host:localhost}")
        private String DEFAULT_HOST ;
        
        @Value("${ogsadai.endpoint.port:8080}")
        private Integer DEFAULT_PORT ;

        @Value("${ogsadai.endpoint.path:ogsadai}")
        private String DEFAULT_PATH ;

        @Override
        public String endpoint(final String proto, final String host, final Integer port, final String path)
            {
            String endpoint = "{proto}://{host}:{port}/{path}";

            if (proto != null)
                {
                endpoint = endpoint.replace("{proto}", proto.trim());
                }
            else {
                endpoint = endpoint.replace("{proto}", DEFAULT_PROTO);
                }

            if (host != null)
                {
                endpoint = endpoint.replace("{host}", host.trim());
                }
            else {
                endpoint = endpoint.replace("{host}", DEFAULT_HOST);
                }

            if (port != null)
                {
                endpoint = endpoint.replace("{port}", port.toString());
                }
            else {
                endpoint = endpoint.replace("{port}", DEFAULT_PORT.toString());
                }

            if (path != null)
                {
                endpoint = endpoint.replace("{path}", path.trim());
                }
            else {
                endpoint = endpoint.replace("{path}", DEFAULT_PATH);
                }

            return endpoint;
            }

        
        public static class PrimaryServiceException
        extends EntityServiceException
            {
            /**
             * Generated serialzable version UID.
             *
             */
            private static final long serialVersionUID = 8735295340800854889L;

            /**
             * Protected constructor.
             * 
             */
            protected PrimaryServiceException(final String message)
                {
                super(message);
                }

            /**
             * Protected constructor.
             * 
             */
            protected PrimaryServiceException(final String message, final Throwable cause)
                {
                super(
                    message,
                    cause
                    );
                }
            }
        }
    
    
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
     * @todo Start status as CREATED, explicitly set to ACTIVE.
     *
     */
    public OgsaServiceEntity(final String name, final String endpoint) throws NameFormatException
        {
        super(
            name
            );
        this.status = Status.ACTIVE;
        this.endpoint = endpoint; 
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_SERVICE_STATUS_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    @Enumerated(
        EnumType.STRING
        )
    private Status status ;
    @Override
    public Status status()
        {
        return this.status ;
        }
    @Override
    public Status status(final Status value)
        {
        this.status = value ;
        return this.status ;
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

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_HTTP_STATUS_COL,
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
                    "version"
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

    @Override
    public OgsaIvoaResources ivoa()
        {
        return new OgsaIvoaResources()
            {
            @Override
            public OgsaIvoaResource create(IvoaResource source)
                {
                return factories().ogsa().factories().ivoa().create(
                    OgsaServiceEntity.this,
                    source
                    );
                }

            @Override
            public Iterable<OgsaIvoaResource> select()
                {
                return factories().ogsa().factories().ivoa().select(
                    OgsaServiceEntity.this
                    );
                }

            @Override
            public Iterable<OgsaIvoaResource> select(IvoaResource source)
                {
                return factories().ogsa().factories().ivoa().select(
                    OgsaServiceEntity.this,
                    source
                    );
                }
            };
        }

    @Override
    public OgsaJdbcResources jdbc()
        {
        return new OgsaJdbcResources()
            {
            @Override
            public OgsaJdbcResource create(JdbcResource source)
                {
                return factories().ogsa().factories().jdbc().create(
                    OgsaServiceEntity.this,
                    source
                    );
                }

            @Override
            public Iterable<OgsaJdbcResource> select()
                {
                return factories().ogsa().factories().jdbc().select(
                    OgsaServiceEntity.this
                    );
                }

            @Override
            public Iterable<OgsaJdbcResource> select(JdbcResource source)
                {
                return factories().ogsa().factories().jdbc().select(
                    OgsaServiceEntity.this,
                    source
                    );
                }

            @Override
            public OgsaJdbcResource primary(JdbcResource source)
                {
                return factories().ogsa().factories().jdbc().primary(
                    OgsaServiceEntity.this,
                    source
                    );
                }
            };
        }
    }
