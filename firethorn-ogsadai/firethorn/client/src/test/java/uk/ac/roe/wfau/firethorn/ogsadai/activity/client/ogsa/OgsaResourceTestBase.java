/**
 *
 */
package uk.ac.roe.wfau.firethorn.ogsadai.activity.client.ogsa;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 * Base class for OGSA-DAI client tests.
 *
 */
@RunWith(
    SpringJUnit4ClassRunner.class
    )
@ContextConfiguration(
    loader = AnnotationConfigContextLoader.class
    ) 
 public abstract class OgsaResourceTestBase
    {

    /**
     * Basic test configuration.
     * @todo Replace this with a non-Spring properties file.
     *
     */
    @Configuration
    @ComponentScan("uk.ac.roe.wfau.firethorn")
    @PropertySource("file:${user.home}/firethorn.properties")
    public static class OgsaTestConfig
        {
        @Autowired
        private Environment environment ;

        public String property(final String name)
            {
            return environment.getProperty(
                name
                );
            }
        public String endpoint()
            {
            return property(
                "firethorn.ogsadai.endpoint"
                );
            }
        }

    /**
     * Our test configuration.
     *
     */
    @Autowired
    private OgsaTestConfig config;

    /**
     * Our test configuration.
     *
     */
    public OgsaTestConfig config()
        {
        return config;
        }
    }
