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

import java.net.URI;
import java.net.URISyntaxException;

import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.entity.NamedEntity;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.meta.base.BaseComponent;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaResource;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;

/**
 *
 *
 */
public interface OgsaService
extends BaseComponent
    {
    /**
     * An endpoint URL factory interface.
     * 
     */
    public static interface EndpointFactory
        {
        /**
         * Generate a new service endpoint URL.
         * @param proto The endpoint URL protocol {http|https}.
         * @param host  The endpoint URL host {localhost}.
         * @param port  The endpoint URL port {8080}.
         * @param path  The endpoint URL path {ogsadai}.
         * @return A new service endpoint URL.
         * 
         */
        public String endpoint(final String proto, final String host, final Integer port, final String path);
        }

    /**
     * {@link Entity.IdentFactory} interface.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory<OgsaService>
        {
        }

    /**
     * {@link Entity.NameFactory} interface.
     *
     */
    public static interface NameFactory
    extends NamedEntity.NameFactory<OgsaService>
        {
        }
    
    /**
     * {@link Entity.LinkFactory} interface.
     *
     */
    public static interface LinkFactory
    extends Entity.LinkFactory<OgsaService>
        {
        }

    /**
     * {@link Entity.EntityFactory} interface.
     *
     */
    public static interface EntityFactory
    extends Entity.EntityFactory<OgsaService>
        {
        /**
         * Select the primary service.
         * @return The primary {@link OgsaService}. 
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public OgsaService primary()
        throws ProtectionException;

        /**
         * Select all the services.
         * @return An {@link Iterable} set of {@link OgsaService}(s). 
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public Iterable<OgsaService> select()
        throws ProtectionException;

        /**
         * Select all the services with a particular status.
         * @return An {@link Iterable} set of {@link OgsaService}(s). 
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public Iterable<OgsaService> select(final OgStatus status)
        throws ProtectionException;

        /**
         * Create a new service.
         * @param endpoint The service endpoint URL.
         * @return A new {@link OgsaService}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public OgsaService create(final String endpoint)
        throws ProtectionException;

        /**
         * Create a new service.
         * @param name     The service name.
         * @param endpoint The service endpoint URL.
         * @return A new {@link OgsaService}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public OgsaService create(final String name, final String endpoint)
        throws ProtectionException;

        /**
         * Create a new service.
         * @param proto The service endpoint URL protocol {http|https}.
         * @param host  The service endpoint URL host {localhost}.
         * @param port  The service endpoint URL port {8080}.
         * @param path  The service endpoint URL path {ogsadai}.
         * @return A new {@link OgsaService}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public OgsaService create(final String proto, final String host, final Integer port, final String path)
        throws ProtectionException;

        /**
         * Create a new service.
         * @param name  The service name.
         * @param proto The service endpoint URL protocol {http|https}.
         * @param host  The service endpoint URL host {localhost}.
         * @param port  The service endpoint URL port {8080}.
         * @param path  The service endpoint URL path {ogsadai}.
         * @return A new {@link OgsaService}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public OgsaService create(final String name, final String proto, final String host, final Integer port, final String path)
        throws ProtectionException;

        }

    /**
     * {@link Entity.EntityServices} interface.
     * 
     */
    public static interface EntityServices
    extends NamedEntity.EntityServices<OgsaService>
        {
        /**
         * Our {@link OgsaService.EntityFactory} instance.
         *
         */
        public OgsaService.EntityFactory entities();
        
        }
    
    /**
     * Enum for the service status.
     * 
     */
    public enum OgStatus
        {
        ACTIVE(),
        INACTIVE(),
        ERROR(),
        UNKNOWN();
        } 

    /**
     * Get the service status.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     * 
     */
    public OgStatus ogstatus()
    throws ProtectionException;

    /**
     * Set the service status.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     * 
     */
    public OgStatus ogstatus(final OgStatus status)
    throws ProtectionException;

    /**
     * The OGSA-DAI service endpoint URL.
     * @return The endpoint URL.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    public String endpoint()
    throws ProtectionException;

    /**
     * The base webapp URI.
     * @return The base webapp URI.
     * @throws URISyntaxException If the endpoint is not a valid URI.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    public URI baseuri()
    throws ProtectionException, URISyntaxException ;

    /**
     * The OGSA-DAI -service version.
     * @return The service version.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    public String version()
    throws ProtectionException;

    /**
     * Access to the {@link OgsaIvoaResource}(s) provided by this {@link OgsaService}.
     *
     */
    interface OgsaIvoaResources
        {

        /**
         * Create a new {@link OgsaIvoaResource} for an {@link IvoaResource}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public OgsaIvoaResource create(final IvoaResource source)
        throws ProtectionException;

        /**
         * List all the {@link OgsaIvoaResource}(s) for this {@link OgsaService}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public Iterable<OgsaIvoaResource> select()
        throws ProtectionException;

        /**
         * List all the {@link OgsaIvoaResource}(s) for an {@link IvoaResource}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public Iterable<OgsaIvoaResource> select(final IvoaResource source)
        throws ProtectionException;
        
        }

    /**
     * Access to the {@link OgsaIvoaResource}(s) provided by this {@link OgsaService}.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    public OgsaIvoaResources ivoa()
    throws ProtectionException;

    /**
     * Access to the {@link OgsaJdbcResource}(s) provided by this {@link OgsaService}.
     *
     */
    interface OgsaJdbcResources
        {

        /**
         * Create a new {@link OgsaJdbcResource} for a {@link JdbcResource}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public OgsaJdbcResource create(final JdbcResource source)
        throws ProtectionException;

        /**
         * List all the {@link OgsaJdbcResource}(s) for this {@link OgsaService}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public Iterable<OgsaJdbcResource> select()
        throws ProtectionException;

        /**
         * List all the {@link OgsaJdbcResource}(s) for a {@link JdbcResource}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public Iterable<OgsaJdbcResource> select(final JdbcResource source)
        throws ProtectionException;

        /**
         * Get the primary {@link OgsaJdbcResource} for a {@link JdbcResource}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public OgsaJdbcResource primary(final JdbcResource source)
        throws ProtectionException;
        
        }

    /**
     * Access to the {@link OgsaJdbcResource}(s) provided by this {@link OgsaService}.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    public OgsaJdbcResources jdbc()
    throws ProtectionException;

    /**
     * Access to the {@link OgsaExecutionResource}(s) provided by this {@link OgsaService}.
     *
     */
    interface OgsaExecResources
        {

        /**
         * Create a new {@link OgsaExecResources}
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public OgsaExecResource create()
        throws ProtectionException;

        /**
         * List all the {@link OgsaExecResource}(s) for this {@link OgsaService}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public Iterable<OgsaExecResource> select()
        throws ProtectionException;

        /**
         * Get the primary {@link OgsaExecResource} for this {@link OgsaService}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public OgsaExecResource primary()
        throws ProtectionException;
        
        }

    /**
     * Access to the {@link OgsaExecResource}(s) provided by this {@link OgsaService}.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    public OgsaExecResources exec()
    throws ProtectionException;

    }
