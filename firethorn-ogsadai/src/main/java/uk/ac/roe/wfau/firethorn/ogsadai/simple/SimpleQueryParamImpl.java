/**
 *
 *  Copyright (c) 2011, AstroDAbis
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification,
 *  are permitted provided that the following conditions are met:
 *
 *      * Redistributions of source code must retain the above copyright notice,
 *        this list of conditions and the following disclaimer.
 *      * Redistributions in binary form must reproduce the above copyright notice,
 *        this list of conditions and the following disclaimer in the documentation
 *        and/or other materials provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 *  IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 *  INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 *  BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 *  DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 *  LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 *  OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 *  OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */
package uk.ac.roe.wfau.firethorn.ogsadai.simple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL ;

/**
 * Bean implementation of SimpleQuery parameters.
 *
 */
public class SimpleQueryParamImpl
implements SimpleQueryParam
    {

    /**
     * Our debug logger.
     * 
     */
    private static Logger logger = LoggerFactory.getLogger(SimpleQueryParamImpl.class);

    /**
     * Public constructor.
     *
     */
    public SimpleQueryParamImpl(
        URL endpoint,
        String resource,
        String tablename,
        String dataset,
        String identifier,
        String statement
        ){

        this.endpoint   = endpoint   ;
        this.resource   = resource   ;
        this.tablename  = tablename  ;

        this.dataset    = dataset    ;
        this.identifier = identifier ;
        this.statement  = statement  ;

        }

    /**
     * Our OGSA-DAI service endpoint.
     *
     */
    private URL endpoint ;

    /**
     * Our OGSA-DAI service endpoint.
     *
     */
    public URL endpoint()
        {
        return this.endpoint ;
        }

    /**
     * Our OGSA-DAI dataset.
     *
     */
    private String dataset ;

    /**
     * Our OGSA-DAI dataset.
     *
     */
    public String dataset()
        {
        return this.dataset ;
        }

    /**
     * The ADQL statement.
     *
     */
    private String statement;

    /**
     * The ADQL statement.
     *
     */
    public String statement()
        {
        return this.statement ;
        }

    /**
     * The result set identifier.
     *
     */
    private String identifier ;

    /**
     * The result set identifier.
     *
     */
    public String identifier()
        {
        return this.identifier ;
        }

    /**
     * The destination resource name.
     *
     */
    private String resource ;

    /**
     * The destination table name.
     *
     */
    private String tablename ;

    /**
     * The results destination.
     *
     */
    public ResultsParam results()
        {
        return new ResultsParam()
            {
            /**
             * The OGSA-DAI resource name.
             *
             */
            public String resource()
                {
                return resource ;
                }

            /**
             * The result set table name.
             *
             */
            public String tablename()
                {
                return tablename ;
                }
            };
        }

    }
