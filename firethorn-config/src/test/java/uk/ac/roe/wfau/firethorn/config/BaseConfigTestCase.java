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
package uk.ac.roe.wfau.firethorn.config;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationBuilder;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConfigurationFactory;
import org.apache.commons.configuration.DefaultConfigurationBuilder;
import org.junit.Test;

import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;

/**
 *
 *
 */
@Slf4j
public class BaseConfigTestCase
    extends TestCase
    {
    public static interface TestConfig
    extends BaseConfig
        {
        public interface System
            {
            public String name();
            public String arch();
            public String version();
            }
        public System system();
        }
    
    public static class TestConfigImpl
    implements TestConfig
        {

        private Configuration config ;

        public TestConfigImpl(final String source)
        throws ConfigurationException
            {
            this(
                new DefaultConfigurationBuilder(
                    source
                    )
                );
            }

        public TestConfigImpl(final ConfigurationBuilder builder)
        throws ConfigurationException
            {
            config = builder.getConfiguration();
            }

        @Override
        public System system()
            {
            return new System(){
                @Override
                public String name()
                    {
                    return config.getString("os.name");
                    }

                @Override
                public String arch()
                    {
                    return config.getString("os.arch");
                    }

                @Override
                public String version()
                    {
                    return config.getString("os.version");
                    }
                };
            }
        }
    
    @Test
    public void testSystemProperties()
    throws Exception
        {
        log.debug("Creating test config");
        final TestConfig one = new TestConfigImpl(
            "test-config-001.xml"
            );

        log.debug("System name [{}]", one.system().name());
        log.debug("System arch [{}]", one.system().arch());
        log.debug("System version [{}]", one.system().version());
        
        }
    
    }
