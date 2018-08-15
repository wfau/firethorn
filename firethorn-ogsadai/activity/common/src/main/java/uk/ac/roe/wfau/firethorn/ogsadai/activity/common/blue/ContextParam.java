/**
 * Copyright (c) 2014, ROE (http://www.roe.ac.uk/)
 * All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package uk.ac.roe.wfau.firethorn.ogsadai.activity.common.blue;

/**
 * Common interface for the pipeline Head activity.
 *
 */
public interface ContextParam
    {
    /**
     * The default Activity name, {@value}.
     * 
     */
    public static final String ACTIVITY_NAME = "uk.ac.roe.wfau.firethorn.Context" ;

    /**
     * Activity input name for the callback protocol input, {@value}.
     * 
     */
    public static final String CALLBACK_PROTOCOL_INPUT = "callback.protocol.input"  ;

    /**
     * Activity input name for the callback host name input, {@value}.
     * 
     */
    public static final String CALLBACK_HOST_INPUT = "callback.host.input"  ;

    /**
     * Activity input name for the callback port number input, {@value}.
     * 
     */
    public static final String CALLBACK_PORT_INPUT = "callback.port.input"  ;
    
    /**
     * Activity input name for the callback base path input, {@value}.
     * 
     */
    public static final String CALLBACK_BASE_INPUT = "callback.base.input"  ;
    
    /**
     * Activity input name for the ident input, {@value}.
     * 
     */
    public static final String CONTEXT_IDENT_INPUT = "context.ident.input"  ;

    /**
     * Activity input name for the pipeline input, {@value}.
     * 
     */
    public static final String CONTEXT_PIPELINE_INPUT = "context.pipeline.input"  ;

    /**
     * Activity output name for the pipeline output, {@value}.
     * 
     */
    public static final String CONTEXT_PIPELINE_OUTPUT = "context.pipeline.output"  ;

    /**
     * Activity input name for the ChaosMonkey parameter name, {@value}.
     * 
     */
    public static final String MONKEY_PARAM_NAME = "monkey.param.name"  ;

    /**
     * Activity input name for the ChaosMonkey parameter value, {@value}.
     * 
     */
    public static final String MONKEY_PARAM_DATA = "monkey.param.data"  ;
    
    }

