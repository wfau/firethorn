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

import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.config.ConfigService.ConfigException;
import uk.ac.roe.wfau.firethorn.config.apache.ApacheConfig;

/**
 *
 *
 */
@Slf4j
public class TestConfigTestCase
    extends TestCase
    {
    
    public static interface TestConfig
        {
        public static interface System
            {
            public String name();
            public String arch();
            public String version();
            }
        public System system();

        public interface Factory
            {
            public TestConfig load()
            throws ConfigException;

            public TestConfig load(final String source)
            throws ConfigException;
            }
        }
    
    public static class TestConfigImpl
    implements TestConfig
        {
        
        public static Factory factory()
            {
            return new Factory(
                ApacheConfig.factory()
                );
            }

        public static Factory factory(final String source)
            {
            return new Factory(
                ApacheConfig.factory(
                    source
                    )
                );
            }

        public static Factory factory(final ConfigService.Factory inner)
            {
            return new Factory(
                inner
                );
            }
        
        public static class Factory
        implements TestConfig.Factory
            {
            private final ConfigService.Factory inner ;

            public Factory(final ConfigService.Factory inner)
                {
                this.inner = inner;
                }

            public TestConfig load()
            throws ConfigException
                {
                return new TestConfigImpl(
                    inner.load()
                    );
                }

            public TestConfig load(final String source)
            throws ConfigException
                {
                return new TestConfigImpl(
                    inner.load(
                        source
                        )
                    );
                }
            }

        private TestConfigImpl(final ConfigService inner)
            {
            this(
                inner.properties()
                );
            }

        private TestConfigImpl(final ConfigService.Properties inner)
            {
            this.inner = inner ;
            }
        
        private final ConfigService.Properties inner ;
        
        @Override
        public System system()
            {
            return new System(){
                @Override
                public String name()
                    {
                    try {
                        return inner.getProperty("os.name");
                        }
                    catch (ConfigException ouch)
                        {
                        return null ;
                        }
                    }

                @Override
                public String arch()
                    {
                    try {
                        return inner.getProperty("os.arch");
                        }
                    catch (ConfigException ouch)
                        {
                        return null ;
                        }
                    }

                @Override
                public String version()
                    {
                    try {
                        return inner.getProperty("os.version");
                        }
                    catch (ConfigException ouch)
                        {
                        return null ;
                        }
                    }
                };
            }
        }

    @Test
    public void test000()
    throws Exception
        {
        final TestConfig.Factory factory = TestConfigImpl.factory();

        log.debug("System name [{}]",    factory.load().system().name());
        log.debug("System arch [{}]",    factory.load().system().arch());
        log.debug("System version [{}]", factory.load().system().version());

        }

    @Test
    public void test001()
    throws Exception
        {
        final TestConfig.Factory factory = TestConfigImpl.factory(
            "test-config-001.xml"
            );

        log.debug("System name [{}]",    factory.load().system().name());
        log.debug("System arch [{}]",    factory.load().system().arch());
        log.debug("System version [{}]", factory.load().system().version());

        }

    @Test
    public void test002()
    throws Exception
        {
        final TestConfig.Factory factory = TestConfigImpl.factory();
        final TestConfig config = factory.load();

        log.debug("System name [{}]",    config.system().name());
        log.debug("System arch [{}]",    config.system().arch());
        log.debug("System version [{}]", config.system().version());

        }

    @Test
    public void test003()
    throws Exception
        {
        final TestConfig.Factory factory = TestConfigImpl.factory();
        final TestConfig config = factory.load(
            "test-config-001.xml"
            );

        log.debug("System name [{}]",    config.system().name());
        log.debug("System arch [{}]",    config.system().arch());
        log.debug("System version [{}]", config.system().version());

        }

    @Test
    public void test004()
    throws Exception
        {
        final TestConfig config = TestConfigImpl.factory(
            ApacheConfig.factory()
            ).load();

        log.debug("System name [{}]",    config.system().name());
        log.debug("System arch [{}]",    config.system().arch());
        log.debug("System version [{}]", config.system().version());

        }

    @Test
    public void test005()
    throws Exception
        {
        final TestConfig config = TestConfigImpl.factory(
            ApacheConfig.factory(
                "test-config-001.xml"
                )
            ).load();

        log.debug("System name [{}]",    config.system().name());
        log.debug("System arch [{}]",    config.system().arch());
        log.debug("System version [{}]", config.system().version());

        }

    @Test
    public void test006()
    throws Exception
        {
        final TestConfig config = TestConfigImpl.factory(
            ApacheConfig.factory(
                "test-config-001.xml"
                )
            ).load();

        log.debug("System name [{}]",    config.system().name());
        log.debug("System arch [{}]",    config.system().arch());
        log.debug("System version [{}]", config.system().version());

        }
    }
