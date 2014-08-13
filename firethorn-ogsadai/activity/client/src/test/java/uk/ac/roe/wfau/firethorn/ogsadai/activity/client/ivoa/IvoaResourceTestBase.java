/**
 *
 */
package uk.ac.roe.wfau.firethorn.ogsadai.activity.client.ivoa;


import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.ogsa.OgsaResourceTestBase;

/**
 * Test base for IvoaResource tests.
 *
 *
 */
 public abstract class IvoaResourceTestBase
 extends OgsaResourceTestBase
    {

    /**
     * Configuration for a TAP service.
     *
     */
    public static class TapService
        {
        public TapService(final String name, final String endpoint)
            {
            this(
                name,
                endpoint,
                null
                );
            }
        public TapService(final String name, final String endpoint, final String query)
            {
            this.name     = name;
            this.query    = query;
            this.endpoint = endpoint;
            }
        private String name;
        public String name()
            {
            return this.name;
            }
        private String query;
        public String query()
            {
            return this.query;
            }
        private String endpoint;
        public String endpoint()
            {
            return this.endpoint;
            }
        }

    /**
     * Configuration for a Map of TAP services.
     *
     */
    public static class TapServiceMap
    extends HashMap<String, TapService>
        {
        private static final long serialVersionUID = 1L;
        public TapServiceMap()
            {
            super();
            }
        public void put(final TapService service)
            {
            this.put(
                service.name(),
                service
                );
            }
        public void put(final String name, final String endpoint)
            {
            this.put(
                new TapService(
                    name,
                    endpoint
                    )
                );
            }
        public void put(final String name, final String endpoint, final String query)
            {
            this.put(
                new TapService(
                    name,
                    endpoint,
                    query
                    )
                );
            }
        }

    /**
     * Configuration for our IVOA resources.
     *
     */
    @Configuration
    public static class IvoaConfig
        {
        private TapServiceMap services = new TapServiceMap(); 
        public TapServiceMap services()
            {
            return this.services;
            }
        public IvoaConfig()
            {
            services.put(
                "CADC",
                "http://www1.cadc-ccda.hia-iha.nrc-cnrc.gc.ca/tap/"
                );
            services.put(
                "GAIA",
                "http://geadev.esac.esa.int/tap-dev/tap/"
                );
            services.put(
                "GAVO",
                "http://dc.zah.uni-heidelberg.de/__system__/tap/run/tap/"
                );
            services.put(
                "VIZIER",
                "http://tapvizier.u-strasbg.fr/TAPVizieR/tap/"
                );
            services.put(
                "WFAU",
                "http://wfaudata.roe.ac.uk/twomass-dsa/TAP/"
                );
            }
        }

    /**
     * Configuration for an IVOA resource test.
     *
     */
    @Configuration
    public static class IvoaTestConfig
    extends OgsaResourceTestBase.OgsaTestConfig
        {
        @Autowired
        private IvoaConfig ivoa;
        public IvoaConfig ivoa()
            {
            return this.ivoa;
            }
        }
    
    @Autowired
    private IvoaTestConfig config ;
    public  IvoaTestConfig config()
        {
        return this.config;
        }
    }
