/**
 * 
 */
package uk.ac.roe.wfau.firethorn.monday;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * http://static.springsource.org/spring-android/docs/1.0.x/reference/html/rest-template.html
 * http://static.springsource.org/spring/docs/3.0.x/javadoc-api/org/springframework/http/converter/json/MappingJacksonHttpMessageConverter.html
 * http://static.springsource.org/spring/docs/3.1.x/javadoc-api/org/springframework/web/client/RestOperations.html
 *
 */
@Slf4j
public class MondayTestCase
extends TestBase
    {

    public interface ClientBean
        {
        public URI getIdent();

        public URI getType();
        
        public String getName();

        public String getCreated();

        public String getModified();

        public interface Connection
            {
            public String getUrl();
            public String getUser();
            public String getStatus();
            }

        public Connection getConnection();

        }

    public static class ConnectionBeanImpl
    implements ClientBean.Connection
        {

        public ConnectionBeanImpl()
            {
            }

        private String url;
        @Override
        public String getUrl()
            {
            return this.url;
            }

        private String user;
        @Override
        public String getUser()
            {
            return this.user;
            }

        private String status;
        @Override
        public String getStatus()
            {
            return this.status;
            }
        }

    public static class ClientBeanImpl
    implements ClientBean
        {

        public ClientBeanImpl()
            {
            }

        private URI ident;
        @Override
        public URI getIdent()
            {
            return this.ident;
            }

        private URI type;
        @Override
        public URI getType()
            {
            return this.type;
            }

        private String name;
        @Override
        public String getName()
            {
            return this.name;
            }

        private String created;
        @Override
        public String getCreated()
            {
            return this.created;
            }

        private String modified;
        @Override
        public String getModified()
            {
            return this.modified;
            }

        private String status;
        public String getStatus()
            {
            return this.status;
            }

        private ConnectionBeanImpl connection;
        @Override
        public Connection getConnection()
            {
            return this.connection;
            }

        }
    
    @Test
    public void something()
    throws Exception
        {
        log.debug("testing something ....");
        List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
        converters.add(
            new MappingJacksonHttpMessageConverter()
            );
        RestTemplate rest = new RestTemplate();
        rest.setMessageConverters(
            converters
            );

        ClientBeanImpl bean = rest.getForObject(
            new URI("http://localhost:8080/firethorn/jdbc/resource/10537"),
            ClientBeanImpl.class
            );        

        log.debug("Resource [{}]", bean.getIdent());
        log.debug("Created  [{}]", bean.getCreated());
        log.debug("Modified [{}]", bean.getModified());
        log.debug("URL    [{}]", bean.getConnection().getUrl());
        log.debug("User   [{}]", bean.getConnection().getUser());
        log.debug("Status [{}]", bean.getConnection().getStatus());

        }
    }
