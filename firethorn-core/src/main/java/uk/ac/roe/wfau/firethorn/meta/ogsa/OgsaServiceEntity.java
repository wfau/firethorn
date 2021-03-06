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

import javax.annotation.PostConstruct;
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

import org.joda.time.Period;
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

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.access.Protector;
import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory;
import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory.FactoryAllowCreateProtector;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityServiceException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameFormatException;
import uk.ac.roe.wfau.firethorn.meta.base.BaseComponentEntity;
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
            query = "FROM OgsaServiceEntity WHERE ogstatus = :ogstatus ORDER BY ident DESC"
            )
        }
    )
public class OgsaServiceEntity
extends BaseComponentEntity<OgsaService>
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
    protected static final String DB_SERVICE_OGSTATUS_COL = "ogstatus";

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_HTTP_STATUS_COL = "http";

    /**
     * The default re-scan interval.
     * 
     */
    protected static final Period DEFAULT_SCAN_PERIOD = new Period(0, 10, 0, 0);
    
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
        public Protector protector()
            {
            return new FactoryAdminCreateProtector();
            }
        
        @Override
        public Class<?> etype()
            {
            return OgsaServiceEntity.class ;
            }

        @Override
        @SelectMethod
        public Iterable<OgsaService> select()
        throws ProtectionException
            {
            return super.iterable(
                super.query(
                    "OgsaService-select-all"
                    )
                );
            }

        @Override
        @SelectMethod
        public Iterable<OgsaService> select(final OgsaService.OgStatus ogstatus)
        throws ProtectionException
            {
            return super.iterable(
                super.query(
                    "OgsaService-select-status"
                    ).setEntity(
                        "ogstatus",
                        ogstatus
                        )
                );
            }

        @Value("${firethorn.ogsadai.endpoint}")
        private String endpoint ;

        @Override
        @CreateMethod
        public OgsaService primary()
        throws ProtectionException
            {
            log.debug("primary()");
            // Really really simple - just get the first ACTIVE service.
            OgsaService service = super.first(
                super.query(
                    "OgsaService-select-status"
                    ).setString(
                        "ogstatus",
                        OgsaService.OgStatus.ACTIVE.name()
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
                        service = factories().ogsa().services().entities().create(
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
                    if (service.ogstatus() != OgStatus.ACTIVE)
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
        @CreateMethod
        public OgsaService create(final String endpoint)
        throws ProtectionException
            {
            log.debug("create(String)");
            return create(
                endpoint,
                endpoint
                );
            }

        @Override
        @CreateMethod
        public OgsaService create(final String name, final String endpoint)
        throws ProtectionException
            {
            log.debug("create(String, String)");
            log.debug("  name [{}]", name);
            log.debug("  endpoint [{}]", endpoint);
            return super.insert(
                new OgsaServiceEntity(
                    name,
                    endpoint
                    )
                );
            }

        @Override
        @CreateMethod
        public OgsaService create(final String proto, final String host, final Integer port, final String path)
        throws ProtectionException
            {
            log.debug("create(String, String, Integer, String)");
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
        @CreateMethod
        public OgsaService create(final String name, final String proto, final String host, final Integer port, final String path)
        throws ProtectionException
            {
            log.debug("create(String, String, String, Integer, String)");
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
     * {@link Entity.EntityServices} implementation.
     * 
     */
    @Slf4j
    @Component
    public static class EntityServices
    implements OgsaService.EntityServices
        {
        /**
         * Our singleton instance.
         * 
         */
        private static OgsaServiceEntity.EntityServices instance ; 

        /**
         * Our singleton instance.
         * 
         */
        public static EntityServices instance()
            {
            return OgsaServiceEntity.EntityServices.instance ;
            }

        /**
         * Protected constructor.
         * 
         */
        protected EntityServices()
            {
            }
        
        /**
         * Protected initialiser.
         * 
         */
        @PostConstruct
        protected void init()
            {
            log.debug("init()");
            if (OgsaServiceEntity.EntityServices.instance == null)
                {
                OgsaServiceEntity.EntityServices.instance = this ;
                }
            else {
                log.error("Setting instance more than once");
                throw new IllegalStateException(
                    "Setting instance more than once"
                    );
                }
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

        @Autowired
        private OgsaService.NameFactory names;
        @Override
        public OgsaService.NameFactory names()
            {
            return this.names;
            }

        @Autowired
        private OgsaService.EntityFactory entities;
        @Override
        public OgsaService.EntityFactory entities()
            {
            return this.entities;
            }
        }

    @Override
    protected OgsaService.EntityFactory factory()
        {
        log.debug("factory()");
        return OgsaServiceEntity.EntityServices.instance().entities() ; 
        }

    @Override
    protected OgsaService.EntityServices services()
        {
        log.debug("services()");
        return OgsaServiceEntity.EntityServices.instance() ; 
        }

    @Override
    public String link()
        {
        return services().links().link(
            this
            );
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
    public OgsaServiceEntity(final String name, final String endpoint)
    throws NameFormatException
        {
        super(
            name
            );
        this.ogstatus = OgStatus.ACTIVE;
        this.endpoint = endpoint; 
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_SERVICE_OGSTATUS_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    @Enumerated(
        EnumType.STRING
        )
    private OgStatus ogstatus ;
    @Override
    public OgStatus ogstatus()
        {
        return this.ogstatus ;
        }
    @Override
    public OgStatus ogstatus(final OgStatus ogstatus)
        {
        this.ogstatus = ogstatus ;
        return this.ogstatus ;
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
    protected HttpStatus http()
        {
        return http ;
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
    
    protected HttpStatus ping()
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
    throws ProtectionException
        {
        return new OgsaIvoaResources()
            {
            @Override
            public OgsaIvoaResource create(IvoaResource source)
            throws ProtectionException
                {
                return factories().ogsa().ivoa().entities().create(
                    OgsaServiceEntity.this,
                    source
                    );
                }

            @Override
            public Iterable<OgsaIvoaResource> select()
            throws ProtectionException
                {
                return factories().ogsa().ivoa().entities().select(
                    OgsaServiceEntity.this
                    );
                }

            @Override
            public Iterable<OgsaIvoaResource> select(IvoaResource source)
            throws ProtectionException
                {
                return factories().ogsa().ivoa().entities().select(
                    OgsaServiceEntity.this,
                    source
                    );
                }
            };
        }

    @Override
    public OgsaJdbcResources jdbc()
    throws ProtectionException
        {
        return new OgsaJdbcResources()
            {
            @Override
            public OgsaJdbcResource create(JdbcResource source)
            throws ProtectionException
                {
                return factories().ogsa().jdbc().entities().create(
                    OgsaServiceEntity.this,
                    source
                    );
                }

            @Override
            public Iterable<OgsaJdbcResource> select()
            throws ProtectionException
                {
                return factories().ogsa().jdbc().entities().select(
                    OgsaServiceEntity.this
                    );
                }

            @Override
            public Iterable<OgsaJdbcResource> select(JdbcResource source)
            throws ProtectionException
                {
                return factories().ogsa().jdbc().entities().select(
                    OgsaServiceEntity.this,
                    source
                    );
                }

            @Override
            public OgsaJdbcResource primary(JdbcResource source)
            throws ProtectionException
                {
                return factories().ogsa().jdbc().entities().primary(
                    OgsaServiceEntity.this,
                    source
                    );
                }
            };
        }

    @Override
    protected void scanimpl()
        {
        log.debug("Scanning OgsaService [{}][{}]", this.name(), this.endpoint());
        final HttpStatus http = ping(); 
        switch(http)
            {
            case OK :
                this.ogstatus = OgStatus.ACTIVE;
                break ;
 
            default :
                this.ogstatus = OgStatus.ERROR;
                break ;
            }
        }

	@Override
	public OgsaExecResources exec()
		{
		return new OgsaExecResources()
			{
			@Override
			public OgsaExecResource create()
				{
                return factories().ogsa().exec().entities().create(
                    OgsaServiceEntity.this
                    );
				}

			@Override
			public Iterable<OgsaExecResource> select()
				{
                return factories().ogsa().exec().entities().select(
                    OgsaServiceEntity.this
                    );
				}

			@Override
			public OgsaExecResource primary()
				{
                return factories().ogsa().exec().entities().primary(
                    OgsaServiceEntity.this
                    );
				}
			};
		}
    }
