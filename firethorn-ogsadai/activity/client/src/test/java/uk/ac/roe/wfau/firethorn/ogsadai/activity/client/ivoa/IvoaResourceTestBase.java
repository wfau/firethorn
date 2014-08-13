/**
 *
 */
package uk.ac.roe.wfau.firethorn.ogsadai.activity.client.ivoa;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.jdbc.JdbcResourceTestBase;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.jdbc.JdbcResourceTestBase.JdbcDatabase;
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
     * Configuration for an IVOA TAP service.
     *
     */
    public static class TapService
    implements IvoaCreateResourceWorkflow.Param
        {
        public TapService(final String endpoint)
            {
            this(
                null,
                endpoint,
                null
                );
            }
        public TapService(final String type, final String endpoint)
            {
            this(
                type,
                endpoint,
                null
                );
            }
        public TapService(final String type, final String endpoint, final String example)
            {
            this.type     = type;
            this.endpoint = endpoint;
            this.example  = example;
            }
        private String type;
        @Override
        public String type()
            {
            return this.type;
            }

        private String endpoint;
        @Override
        public String endpoint()
            {
            return this.endpoint;
            }

        private String example;
        public String example()
            {
            return this.example;
            }
        }

    /**
     * Configuration for an IVOA test.
     *
     */
    @Configuration
    public static class IvoaTestConfig
    extends JdbcResourceTestBase.JdbcTestConfig
        {
        /**
         * Configuration for our IVOA services.
         *
         */
        public interface IvoaServices
            {
            public Map<String, TapService> services();
            }

        /**
         * Configuration for our test databases.
         *
         */
        public IvoaServices ivoa()
            {
            return new IvoaServices()
                {
                private Map<String, TapService> services = new HashMap<String, TapService>();
                @Override
                public Map<String, TapService> services()
                    {
                    return this.services;
                    }
                private IvoaServices init()
                    {
                    services.put(
                        "CADC",
                        new TapService(
                            "http://www1.cadc-ccda.hia-iha.nrc-cnrc.gc.ca/tap/async",
                            "SELECT table_name, table_type FROM TAP_SCHEMA.tables"
                            )
                        );
                    services.put(
                        "GAIA",
                        new TapService(
                            "http://geadev.esac.esa.int/tap-dev/tap/async",
                            "SELECT TOP 1234 ra, decl FROM public.twomass_psc"
                            )
                        );
                    services.put(
                        "GAVO",
                        new TapService(
                            "http://dc.zah.uni-heidelberg.de/__system__/tap/run/tap/async",
                            "SELECT TOP 1234 RAJ2000, DEJ2000 FROM twomass.data"
                            )
                        );
                    services.put(
                        "VIZIER",
                        new TapService(
                            "http://tapvizier.u-strasbg.fr/TAPVizieR/tap/async",
                            "SELECT TOP 1234 RAJ2000, DEJ2000 FROM vizls.\"II/246/out\""
                            )
                        );
                    services.put(
                        "WFAU",
                        new TapService(
                            "http://wfaudata.roe.ac.uk/twomass-dsa/TAP/async",
                            "SELECT TOP 1234 ra, dec FROM twomass_psc"
                            )
                        );
                    return this ;
                    }
                }.init();
            }
        }
    
    @Autowired
    private IvoaTestConfig config ;
    public  IvoaTestConfig config()
        {
        return this.config;
        }
    }
