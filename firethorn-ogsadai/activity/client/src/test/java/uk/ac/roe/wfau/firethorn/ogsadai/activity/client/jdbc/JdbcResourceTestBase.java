/**
 *
 */
package uk.ac.roe.wfau.firethorn.ogsadai.activity.client.jdbc;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.ogsa.OgsaResourceTestBase;

/**
 * Test base for JdbcResource tests.
 *
 *
 */
public abstract class JdbcResourceTestBase
extends OgsaResourceTestBase
    {

    /**
     * Configuration for a JDBC database.
     *
     */
    public interface JdbcDatabase
    extends JdbcCreateResourceWorkflow.Param
        {
        }

    /**
     * Configuration for a JDBC test.
     *
     */
    @Configuration
    public static class JdbcTestConfig
    extends OgsaResourceTestBase.OgsaTestConfig
        {
        /**
         * Configuration for our test databases.
         *
         */
        public interface JdbcDatabases
            {
            public Map<String, JdbcDatabase> databases();
            }

        /**
         * Configuration for our test databases.
         *
         */
        public JdbcDatabases jdbc()
            {
            return new JdbcDatabases()
                {
                private Map<String, JdbcDatabase> databases = new HashMap<String, JdbcDatabase>(); 
                @Override
                public Map<String, JdbcDatabase> databases()
                    {
                    return this.databases;
                    }
                private JdbcDatabases init()
                    {
                    this.databases.put(
                        "user", 
                        new JdbcDatabase()
                            {
                            @Override
                            public String jdbcurl()
                                {
                                return property("firethorn.user.url");
                                }
                            @Override
                            public String username()
                                {
                                return property("firethorn.user.user");
                                }
                            @Override
                            public String password()
                                {
                                return property("firethorn.user.pass");
                                }
                            @Override
                            public String driver()
                                {
                                return property("firethorn.user.driver");
                                }
                            @Override
                            public boolean writable()
                                {
                                return true;
                                }
                            }            
                        );
                    this.databases.put(
                        "atlas", 
                        new JdbcDatabase()
                            {
                            @Override
                            public String jdbcurl()
                                {
                                return property("firethorn.atlas.url");
                                }
                            @Override
                            public String username()
                                {
                                return property("firethorn.atlas.user");
                                }
                            @Override
                            public String password()
                                {
                                return property("firethorn.atlas.pass");
                                }
                            @Override
                            public String driver()
                                {
                                return property("firethorn.atlas.driver");
                                }
                            @Override
                            public boolean writable()
                                {
                                return false;
                                }
                            }            
                        );
                    return this;
                    }
                }.init();
            }
        }
    
    @Autowired
    private JdbcTestConfig config ;
    public  JdbcTestConfig config()
        {
        return this.config;
        }
    }
