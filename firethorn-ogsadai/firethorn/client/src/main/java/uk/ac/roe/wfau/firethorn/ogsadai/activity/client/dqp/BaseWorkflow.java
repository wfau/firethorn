/*
 *  Copyright (C) 2015 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.ogsadai.activity.client.dqp;

import java.net.MalformedURLException;
import java.net.URL;

import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.ogsa.OgsaServiceClient;

/**
 *
 *
 */
public class BaseWorkflow
    {
    /**
     * Public constructor.
     * @param endpoint The OGSA-DAI service endpoint URL.
     * @throws MalformedURLException 
     *
     */
    public BaseWorkflow(final String endpoint)
    throws MalformedURLException
        {
        this(
            new OgsaServiceClient(
                new URL(
                    endpoint
                    )
                )
            );
        }

    /**
     * Public constructor.
     * @param endpoint The OGSA-DAI service endpoint URL.
     *
     */
    public BaseWorkflow(final URL endpoint)
        {
        this(
            new OgsaServiceClient(
                endpoint
                )
            );
        }

    /**
     * Public constructor.
     * @param service Our {@link OgsaServiceClient} client.
     *
     */
    public BaseWorkflow(final OgsaServiceClient service)
        {
        this.service = service ;
        }

    /**
     * Our {@link OgsaServiceClient} client.
     * 
     */
    private OgsaServiceClient service ;  

    /**
     * Our {@link OgsaServiceClient} client.
     * 
     */
    public OgsaServiceClient service()
        {
        return this.service;
        }
    }
