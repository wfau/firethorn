/*
 *  Copyright (C) 2013 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.adql.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.junit.*;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import uk.ac.roe.wfau.firethorn.test.TestBase;

/**
 *
 *
 */
@Slf4j
public class TestQueryProcessor
extends AtlasQueryTestBase
    {
    public static final int[] SERVER_LIST = new int[] {
        1,2,3,4,5,6,7,8,10,11,13
        };
    
    private static final String DRIVER_CLASS = "net.sourceforge.jtds.jdbc.Driver" ;
    
    static {
        try {
            Class.forName(
                DRIVER_CLASS
                );
            }
        catch (final Exception ouch)
            {
            log.error("Unable to load JDBC driver [{}][{}]", DRIVER_CLASS, ouch.getMessage());
            throw new RuntimeException(
                ouch
                );
            }
        }

    @Value("${wfau.sqlserver.admin.user}")
    private String adminuser ;
    @Value("${wfau.sqlserver.admin.pass}")
    private String adminpass ;
    @Value("${wfau.sqlserver.admin.name}")
    private String adminname ;

    public DatabaseConnection admindb(int servernum)
        {
        return new DatabaseConnection(
            servernum,
            adminname,
            adminuser,
            adminpass
            );
        }

    /**
     * Inner class to represent a tunnelled database connection.
     * 
     */
    public static class DatabaseConnection
        {
        public static final String BASE_URL = "jdbc:jtds:sqlserver://{host}:{port}/{name}";
        public static final String BASE_NAME = "ramses{num}" ;
        public static final int BASE_PORT = 1430 ;

        public DatabaseConnection(int servernum, String dbname, String dbuser, String dbpass)
            {
            this.servernum  = servernum ;
            this.servername = BASE_NAME.replace("{num}", String.valueOf(servernum));

            log.debug("Name [{}]", dbname);
            log.debug("User [{}]", dbuser);
            log.debug("Pass [{}]", "....");

            this.dbuser = dbuser;
            this.dbpass = dbpass;
            this.dbname = dbname;
            this.dburl  = BASE_URL.
                replace("{host}", "localhost").
                replace("{port}", String.valueOf(BASE_PORT + servernum)).
                replace("{name}", dbname);
            }

        private String servername ;
        public String servername()
            {
            return this.servername;
            }

        private int servernum ;
        public int servernum()
            {
            return this.servernum;
            }

        private String dbuser;
        private String dbpass;
        
        private String dbname;
        public String dbname()
            {
            return this.dbname;
            }

        private String dburl ;
        public String dburl()
            {
            return this.dburl ;
            }
        
        private DataSource source ;
        public DataSource source()
            {
            if (this.source == null)
                {
                try {
                    this.source = new DriverManagerDataSource(
                        dburl()
                        );
                    }
                catch (Exception ouch)
                    {
                    log.error("Unable to open JDBC DataSource [{}][{}]", this.dburl, ouch.getMessage());
                    throw new RuntimeException(
                        ouch
                        );
                    }
                }
            return this.source;
            }

        private Connection connection ;
        public Connection connection()
            {
            if (this.connection == null)
                {
                try {
                    this.connection = source().getConnection(
                        this.dbuser,
                        this.dbpass
                        );
                    }
                catch (SQLException ouch)
                    {
                    log.error("Unable to open JDBC connection [{}]", ouch.getMessage());
                    throw new RuntimeException(
                        ouch
                        );
                    }
                }
            return this.connection;
            }

        public Statement statement()
            {
            try {
                return connection().createStatement();
                }
            catch (SQLException ouch)
                {
                log.error("Unable to create JDBC statement [{}]", ouch.getMessage());
                throw new RuntimeException(
                    ouch
                    );
                }
            }

        public PreparedStatement prepare(String query)
            {
            try {
                return connection().prepareStatement(
                    query
                    );
                }
            catch (SQLException ouch)
                {
                log.error("Unable to prepare JDBC statement [{}][{}]", query, ouch.getMessage());
                throw new RuntimeException(
                    ouch
                    );
                }
            }
        
        
        public ResultSet execute(String query)
            {
            try {
                return statement().executeQuery(
                    query
                    );
                }
            catch (SQLException ouch)
                {
                log.error("Unable to execute query [{}][{}]", query, ouch.getMessage());
                throw new RuntimeException(
                    ouch
                    );
                }
            }
        }

    public void catalogs()
        {
        for (int server : SERVER_LIST)
            {
            catalogs(server);
            }
        }
    
    public void catalogs(int servernum)
        {
        DatabaseConnection database = admindb(servernum);
        log.debug("DB [{}] --------", database.servername());
        
        ResultSet results = database.execute(
            "SELECT DISTINCT dbname FROM webQueries WHERE row_count > 0 ORDER BY dbname asc"
            );

        try {
            while (results.next())
                {
                String  catalog  = results.getString("dbname");
                log.debug("[{}]", catalog);
                }
            }
        catch (final SQLException ouch)
            {
            log.error("Error processing results [{}]", ouch.getMessage());
            throw new RuntimeException(
                ouch
                );
            }
        log.debug("--------");

        }

    public void queries()
        {
        for (int server : SERVER_LIST)
            {
            queries(server);
            }
        }

    public void queries(int servernum)
        {
        DatabaseConnection database = admindb(servernum);
        log.debug("DB [{}] --------", database.servername());

        ResultSet results = database.execute(
            "SELECT dbname, row_count, time, query FROM webQueries WHERE row_count > 0 ORDER BY dbname asc"
            );

        try {
            while (results.next())
                {
                String  catalog  = results.getString("dbname");
                Integer count    = results.getInt("row_count");
                DateTime date    = new DateTime(results.getTimestamp("time").getTime()); 
                String  query    = results.getString("query");
                log.debug("[{}][{}][{}][{}]", catalog, date.toString(), count, query);
                }
            }
        catch (final SQLException ouch)
            {
            log.error("Error processing results [{}]", ouch.getMessage());
            throw new RuntimeException(
                ouch
                );
            }
        log.debug("--------");

        }

    public void queries(String match)
        {
        for (int server : SERVER_LIST)
            {
            queries(server, match);
            }
        }

    public void queries(int servernum, String match)
        {
        DatabaseConnection database = admindb(servernum);
        log.debug("DB [{}] --------", database.servername());

        PreparedStatement statement = database.prepare(
            "SELECT dbname, row_count, time, query FROM webQueries WHERE dbname LIKE ? AND row_count > 0 ORDER BY dbname asc"
            );        
        try {
            statement.setString(1, match);
            }
        catch (final SQLException ouch)
            {
            log.error("Error preparing results [{}]", ouch.getMessage());
            throw new RuntimeException(
                ouch
                );
            }
        ResultSet results ;
        try {
            results = statement.executeQuery();;
            }
        catch (final SQLException ouch)
            {
            log.error("Error executing results [{}]", ouch.getMessage());
            throw new RuntimeException(
                ouch
                );
            }

        try {
            while (results.next())
                {
                String  catalog  = results.getString("dbname");
                Integer count    = results.getInt("row_count");
                DateTime date    = new DateTime(results.getTimestamp("time").getTime()); 
                String  query    = results.getString("query");
                
                //log.debug("[{}][{}][{}][{}]", catalog, date.toString(), count, clean(query));
                log.debug("Query : {}", clean(query));
                }
            }
        catch (final SQLException ouch)
            {
            log.error("Error processing results [{}]", ouch.getMessage());
            throw new RuntimeException(
                ouch
                );
            }
        log.debug("--------");
        }
    
    //@Test
    public void test000()
        {
        catalogs();
        }

    @Test
    public void test001()
        {
        queries("ATLAS%");
        }

    public void fredric(int servernum)
        {
        DatabaseConnection database = admindb(servernum);
        log.debug("DB [{}] --------", database.servername());

        PreparedStatement statement = database.prepare(
            "SELECT dbname, row_count, time, query FROM webQueries WHERE dbname LIKE ? AND row_count > 0 ORDER BY dbname asc"
            );        
        try {
            statement.setString(1, "ATLAS%");
            }
        catch (final SQLException ouch)
            {
            log.error("Error preparing results [{}]", ouch.getMessage());
            throw new RuntimeException(
                ouch
                );
            }
        ResultSet results ;
        try {
            results = statement.executeQuery();;
            }
        catch (final SQLException ouch)
            {
            log.error("Error executing results [{}]", ouch.getMessage());
            throw new RuntimeException(
                ouch
                );
            }

        try {
            while (results.next())
                {
                String  catalog = results.getString("dbname");
                String  adql    = results.getString("query");
                log.debug("[{}][{}]", catalog, adql);

                final AdqlQuery query = this.queryspace.queries().create(
                    adql
                    );

                log.debug("[{}][{}]", catalog, query.osql());
                
                String diff = StringUtils.difference(
                    adql,
                    query.osql()
                    );
                log.debug("DIIF [{}]", diff);
                }
            }
        catch (final SQLException ouch)
            {
            log.error("Error processing results [{}]", ouch.getMessage());
            throw new RuntimeException(
                ouch
                );
            }
        log.debug("--------");
        
        }

    //@Test
    public void test002()
        {
        fredric(5);
        }

    //@Test
    public void test003()
        {
        for (int server : SERVER_LIST)
            {
            try {
                fredric(server);
                }
            catch (Exception ouch)
                {
                }
            }
        }
    }