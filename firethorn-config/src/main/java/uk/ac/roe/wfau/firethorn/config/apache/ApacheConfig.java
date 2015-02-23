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

import uk.ac.roe.wfau.firethorn.config.ConfigInterface;
import uk.ac.roe.wfau.firethorn.config.ConfigInterface.ConfigException;
import uk.ac.roe.wfau.firethorn.config.ConfigInterface.Factory;

/**
 * Implementation of {@link ConfigInterface} based on {@link Configuration} from Apache Commons Configuration. 
 *
 */
public class ApacheConfig
implements ConfigInterface
    {
    private final Configuration inner ;

    public ApacheConfig(final Configuration inner)
        {
        this.inner = inner;
        }
    
    @Override
    public String getProperty(final String key)
        throws ConfigException
        {
        return inner.getString(
            key
            );
        }

    @Override
    public String getProperty(final String key, final String fallback)
        throws ConfigException
        {
        return inner.getString(
            key,
            fallback
            );
        }

    @Override
    public void setProperty(final String key, final String value)
        throws ConfigException
        {
        try {
            inner.setProperty(
                key,
                value
                );
            }
        catch (Throwable ouch)
            {
            throw new ConfigException(
                ouch
                ); 
            }
        }

    public static class Factory
    implements ConfigInterface.Factory 
        {
        public static final String DEFAULT_SOURCE = "test-config-001.xml" ;

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
        public synchronized ConfigInterface load()
        throws ConfigException
            {
            return load(
                this.source
                );
            }

        @Override
        public synchronized ConfigInterface load(final String source)
        throws ConfigException
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
    }