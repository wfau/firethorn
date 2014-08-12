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
package uk.ac.roe.wfau.firethorn.ogsadai.activity.server.ivoa;

import uk.org.ogsadai.config.Key;

/**
 *
 *
 */
public interface IvoaActivityParam
    {

    /**
     * Configuration property name for the TAP service endpoint URL.
     *  
     */
    public static final Key TAP_ENDPOINT_URL_KEY = new Key("ivoa.tap.endpoint.url");

    /**
     * Configuration property name for the UWS service polling interval.
     *  
     */
    public static final Key UWS_POLL_INTERVAL_KEY = new Key("ivoa.tap.poll.interval");

    /**
     * Configuration property name for the UWS service polling timeout.
     *  
     */
    public static final Key UWS_POLL_TIMEOUT_KEY  = new Key("ivoa.tap.poll.timeout");

    /**
     * Configuration property name for additional TAP request parameters.
     *  
     */
    public static final String TAP_PARAMETER_PREFIX = "ivoa.tap.param";

    }
