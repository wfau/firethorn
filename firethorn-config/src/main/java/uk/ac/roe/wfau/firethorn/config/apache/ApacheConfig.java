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
package uk.ac.roe.wfau.firethorn.config.apache;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationBuilder;
import org.apache.commons.configuration.DefaultConfigurationBuilder;

import uk.ac.roe.wfau.firethorn.config.ConfigService;

/**
 * Implementation of {@link ConfigService} based on the Apache Commons Configuration {@link Configuration}.
 * @see https://commons.apache.org/proper/commons-configuration/ 
 *
 */
public class ApacheConfig
implements ConfigService
    {
    private final Configuration inner ;

    public Configuration apache()
        {
        return this.inner ;
        }

    public ApacheConfig(final Configuration inner)
        {
        this.inner = inner;
        }

    public ConfigService.Properties properties()
        {
        return new ConfigService.Properties()
            {
            @Override
            public String getProperty(final String key)
                throws ConfigService.ConfigException
                {
                return inner.getString(
                    key
                    );
                }

            @Override
            public String getProperty(final String key, final String fallback)
                throws ConfigService.ConfigException
                {
                return inner.getString(
                    key,
                    fallback
                    );
                }

            @Override
            public void setProperty(final String key, final String value)
                throws ConfigService.ConfigException
                {
                try {
                    inner.setProperty(
                        key,
                        value
                        );
                    }
                catch (Throwable ouch)
                    {
                    throw new ConfigService.ConfigException(
                        ouch
                        ); 
                    }
                }
            };
        }

    public static class Factory
    implements ConfigService.Factory 
        {
        
        public static final String DEFAULT_SOURCE = "firethorn-configuration.xml" ;

        private final String source ;

        public Factory()
            {
            this(
                DEFAULT_SOURCE
                );
            }

        public Factory(final String source)
            {
            this.source = source ;
            }

        @Override
        public synchronized ConfigService load()
        throws ConfigService.ConfigException
            {
            return load(
                this.source
                );
            }

        @Override
        public synchronized ConfigService load(final String source)
        throws ConfigService.ConfigException
            {
            try {
                ConfigurationBuilder builder = new DefaultConfigurationBuilder(
                    source
                    );
                return new ApacheConfig(
                    builder.getConfiguration()
                    );
                }
            catch (Exception ouch)
                {
                throw new ConfigException(
                    ouch
                    ); 
                }
            }
        }

    public static Factory factory()
        {
        return new Factory();
        }

    public static Factory factory(final String source)
        {
        return new Factory(
            source
            );
        }
    }