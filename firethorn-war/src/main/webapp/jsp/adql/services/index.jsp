<%@ taglib prefix="jstl-core"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring-tags" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="spring-form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="test"        uri="/WEB-INF/tlds/service-tags.xml"%>
<%@ page
    import="uk.ac.roe.wfau.firethorn.webapp.control.PathBuilder"
    import="uk.ac.roe.wfau.firethorn.webapp.control.ServletPathBuilder"

    import="uk.ac.roe.wfau.firethorn.webapp.mallard.AdqlServiceController"
    import="uk.ac.roe.wfau.firethorn.webapp.mallard.AdqlServicesController"

    import="uk.ac.roe.wfau.firethorn.webapp.mallard.AdqlServiceBean"

    session="true"
%><%
PathBuilder paths = new ServletPathBuilder(
    request
    );
%>
<html>
    <head>
	    <title></title>
        <link href='<%= paths.file("/css/page.css") %>' rel='stylesheet' type='text/css'/>
    </head>
    <body>
        <div>
            <span>[<a href='<%= paths.path(AdqlServicesController.CONTROLLER_PATH, AdqlServicesController.SEARCH_PATH) %>'>search</a>]</span>
            <span>[<a href='<%= paths.path(AdqlServicesController.CONTROLLER_PATH, AdqlServicesController.SELECT_PATH) %>'>select</a>]</span>
            <span>[<a href='<%= paths.path(AdqlServicesController.CONTROLLER_PATH, AdqlServicesController.CREATE_PATH) %>'>create</a>]</span>
        </div>
        <div>
            ADQL (TAP) Services
            <div>
                Search for services with text
                <form method='GET' action='<%= paths.path(AdqlServicesController.CONTROLLER_PATH, AdqlServicesController.SEARCH_PATH) %>'>
                    Text <input type='text' name='<%= AdqlServicesController.SEARCH_TEXT %>' value=''/>
                    <input type='submit' value='Go'/>
                </form>
            </div>
            <div>
                Select a service by name
                <form method='GET' action='<%= paths.path(AdqlServicesController.CONTROLLER_PATH, AdqlServicesController.SELECT_PATH) %>'>
                    Name <input type='text' name='<%= AdqlServicesController.SELECT_NAME %>' value=''/>
                    <input type='submit' value='Go'/>
                </form>
            </div>
            <div>
                Create new service
                <form method='POST' action='<%= paths.path(AdqlServicesController.CONTROLLER_PATH, AdqlServicesController.CREATE_PATH) %>'>
                    Name <input type='text' name='<%= AdqlServicesController.CREATE_NAME %>' value=''/>
                    <input type='submit' value='Go'/>
                </form>
            </div>
        </div>
    </body>
</html>

