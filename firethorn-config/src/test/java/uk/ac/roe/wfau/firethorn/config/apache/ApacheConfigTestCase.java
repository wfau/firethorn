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

import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.config.ConfigInterface;

/**
 *
 *
 */
@Slf4j
public class ApacheConfigTestCase
    extends TestCase
    {

    @Test
    public void test000()
        {
        try {

            ConfigInterface.Factory factory = new ApacheConfig.Factory(
                "test-config-000.xml"
                );

            log.debug("OS name [{}]",    factory.load().getProperty("os.name"));
            log.debug("OS arch [{}]",    factory.load().getProperty("os.arch"));
            log.debug("OS version [{}]", factory.load().getProperty("os.version"));

            log.debug("Unknown [{}]", factory.load().getProperty("os.unknown"));
            log.debug("Unknown [{}]", factory.load().getProperty("os.unknown", "unknown"));

            }
        catch (ConfigInterface.ConfigException ouch)
            {
            log.error("Failed to load config [{}]", ouch.getCause().getMessage());
            }
        }

    @Test
    public void test001()
        {
        try {

            ConfigInterface.Factory factory = new ApacheConfig.Factory();

            log.debug("OS name [{}]",    factory.load().getProperty("os.name"));
            log.debug("OS arch [{}]",    factory.load().getProperty("os.arch"));
            log.debug("OS version [{}]", factory.load().getProperty("os.version"));

            log.debug("Unknown [{}]", factory.load().getProperty("os.unknown"));
            log.debug("Unknown [{}]", factory.load().getProperty("os.unknown", "unknown"));

            }
        catch (ConfigInterface.ConfigException ouch)
            {
            log.error("Failed to load config [{}]", ouch.getCause().getMessage());
            }
        }
    }
