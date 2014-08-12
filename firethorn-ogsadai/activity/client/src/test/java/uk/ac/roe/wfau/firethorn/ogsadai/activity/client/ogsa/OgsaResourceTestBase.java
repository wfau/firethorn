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
     *
     */
    @Configuration
    @ComponentScan("uk.ac.roe.wfau.firethorn")
    @PropertySource("file:${user.home}/firethorn.properties")
    public static class Config
        {
        @Autowired
        private Environment environment ;

        public String endpoint()
            {
            return environment.getProperty(
                "firethorn.ogsadai.endpoint"
                );
            }
        }

    /**
     * Our test configuration.
     *
     */
    public abstract Config config();
    
    }
