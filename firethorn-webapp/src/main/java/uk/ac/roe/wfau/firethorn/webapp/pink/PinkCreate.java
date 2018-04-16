package uk.ac.roe.wfau.firethorn.webapp.pink;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PinkCreate extends HttpServlet
    {

    /**
     * Generated serial ID.
     * 
     */
    private static final long serialVersionUID = -3554089335852067214L;
    

    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
    throws ServletException, IOException    
        {
        log.debug("doPost(....)");
        final ServletContext context = getServletContext();
        final RequestDispatcher dispatcher = context.getNamedDispatcher(
            "spring-servlet"
            );
        final HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper(
            request
            ){
            public String getPathTranslated()
                {
                final String path = request.getPathTranslated();
                log.debug("getPathTranslated()");
                log.debug("  path [{}]", path);
                return path ;
                }
            }; 
        
        log.debug("including");
        dispatcher.include(
            wrapper,
            response
            );
        log.debug("included");
        
        }
    }
