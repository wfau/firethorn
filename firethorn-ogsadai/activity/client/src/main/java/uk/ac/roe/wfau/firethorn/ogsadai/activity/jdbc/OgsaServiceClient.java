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
package uk.ac.roe.wfau.firethorn.ogsadai.activity.jdbc;

import java.net.MalformedURLException;
import java.net.URL;

import uk.org.ogsadai.client.toolkit.DataRequestExecutionResource;
import uk.org.ogsadai.client.toolkit.Server;
import uk.org.ogsadai.client.toolkit.presentation.jersey.JerseyServer;
import uk.org.ogsadai.resource.ResourceID;

/**
 * An OGSA-DAI service client
 *
 */
public class OgsaServiceClient
    {
    /**
     * The default DRER resource ID.
     * @todo Make this configurable.
     *  
     */
    public static final String DRER_IDENT = "DataRequestExecutionResource" ;

    /**
     * Public constructor.
     * @param endpoint Our OGSA-DAI service endpoint URL.
     * @throws MalformedURLException 
     *
     */
    public OgsaServiceClient(final String endpoint)
    throws MalformedURLException
        {
        this(
            new URL(
                endpoint
                )
            );
        }

    /**
     * Public constructor.
     * @param endpoint Our OGSA-DAI service endpoint URL.
     *
     */
    public OgsaServiceClient(final URL endpoint)
        {
        this.endpoint = endpoint ;
        }

    /**
     * Our OGSA-DAI server endpoint URL.
     * 
     */
    private final URL endpoint ;

    /**
     * Our OGSA-DAI server proxy.
     * 
     */
    private Server server ; 

    /**
     * Our OGSA-DAI server proxy.
     * 
     */
    protected synchronized Server server()
        {
        if (this.server == null)
            {
            this.server = new JerseyServer();
            server.setDefaultBaseServicesURL(
                this.endpoint
                );
            }
        return this.server;
        }

    /**
     * Our OGSA-DAI DRER proxy.
     * 
     */
    private DataRequestExecutionResource drer ; 

    /**
     * Our OGSA-DAI DRER proxy.
     * 
     */
    protected synchronized DataRequestExecutionResource drer()
        {
        if (this.drer == null)
            {
            this.drer = server().getDataRequestExecutionResource(
                new ResourceID(
                    DRER_IDENT
                    )
                );
            }
        return this.drer;
        }
    }
