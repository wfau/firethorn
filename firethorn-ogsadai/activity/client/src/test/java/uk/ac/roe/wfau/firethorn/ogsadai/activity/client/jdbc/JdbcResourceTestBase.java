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
 * Test base for IvoaResource tests.
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
    public interface JdbcParam
    extends JdbcCreateResourceWorkflow.Param
        {
        }

    /**
     * Configuration for our JDBC databases.
     *
     */
    public class JdbcConfig
        {
        }

    /**
     * Configuration for a JDBC resource test.
     *
     */
    @Configuration
    public static class JdbcTestConfig
    extends OgsaResourceTestBase.OgsaTestConfig
        {

        private Map<String, JdbcParam> databases = new HashMap<String, JdbcParam>(); 
        public Map<String, JdbcParam> databases()
            {
            return this.databases;
            }
    
        public JdbcTestConfig()
            {
            this.databases.put(
                "user", 
                new JdbcParam()
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
                new JdbcParam()
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
            }
        }
    
    @Autowired
    private JdbcTestConfig config ;
    public  JdbcTestConfig config()
        {
        return this.config;
        }
    }
