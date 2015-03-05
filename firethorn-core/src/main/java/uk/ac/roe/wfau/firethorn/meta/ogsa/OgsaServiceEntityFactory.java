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

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityServiceException;
import uk.ac.roe.wfau.firethorn.meta.ogsa.OgsaService.Status;

/**
 * {@link OgsaService.EntityFactory} implementation.
 *
 */
@Slf4j
@Repository
public class OgsaServiceEntityFactory
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
    @CreateMethod
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
    @CreateMethod
    public OgsaService create(final String endpoint)
        {
        return create(
            endpoint,
            endpoint
            );
        }

    @Override
    @CreateMethod
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
    @CreateMethod
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
    @CreateMethod
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
