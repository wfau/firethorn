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
import uk.ac.roe.wfau.firethorn.ogsadai.activity.common.ivoa.IvoaResourceParam;

/**
 *
 *
 */
public interface IvoaResourceKeys
    {

    /**
     * Property key for the TAP endpoint URL, {@value}.
     *  
     */
    public static final Key IVOA_TAP_ENDPOINT = new Key(
        IvoaResourceParam.IVOA_TAP_ENDPOINT
        );

    /**
     * Property key for the UWS quick start property, {@value}.
     *  
     */
    public static final Key IVOA_UWS_QUICKSTART = new Key(
        IvoaResourceParam.IVOA_UWS_QUICKSTART
        );

    /**
     * Property key for the UWS service polling interval, {@value}.
     *  
     */
    public static final Key IVOA_UWS_INTERVAL = new Key(
        IvoaResourceParam.IVOA_UWS_INTERVAL
        );
    
    /**
     * Property key for the UWS service polling timeout, {@value}.
     *  
     */
    public static final Key IVOA_UWS_TIMEOUT = new Key(
        IvoaResourceParam.IVOA_UWS_TIMEOUT
        );
    
    }
