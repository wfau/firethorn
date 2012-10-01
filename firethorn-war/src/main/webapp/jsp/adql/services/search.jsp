<%@ taglib prefix="jstl-core"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring-tags" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="spring-form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="test"        uri="/WEB-INF/tlds/service-tags.xml"%>
<%@ page
    import="uk.ac.roe.wfau.firethorn.webapp.control.PathBuilder"
    import="uk.ac.roe.wfau.firethorn.webapp.control.ServletPathBuilder"
    import="uk.ac.roe.wfau.firethorn.webapp.mallard.AdqlServiceController"
    import="uk.ac.roe.wfau.firethorn.webapp.mallard.AdqlServicesController"
    import="uk.ac.roe.wfau.firethorn.mallard.AdqlService"
    session="true"
%><%
PathBuilder paths = new ServletPathBuilder(
    request
    );

String text = (String) request.getAttribute(
    AdqlServicesController.SEARCH_TEXT
    );

Iterable<AdqlService> services = (Iterable<AdqlService>) request.getAttribute(
    AdqlServicesController.SEARCH_RESULT
    ) ;

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
            Search for ADQL TAP Services
            <div>
                <form method='GET' action='<%= paths.path(AdqlServicesController.CONTROLLER_PATH, AdqlServicesController.SEARCH_PATH) %>'>
                    Text <input type='text' name='<%= AdqlServicesController.SEARCH_TEXT %>' value='<%= ((text != null) ? text : "" ) %>'/>
                    <input type='submit' value='Go'/>
                </form>
            </div>
        </div>
        <div>
            <table border='1'>
                <%
                if (services != null)
                    {
                    for (AdqlService service : services)
                        {
                        %>
                        <tr>
                            <td><a href='<%= paths.link(service) %>'><%= service.name() %></a></td>
                            <td><%= service.owner().name() %></td>
                            <td><%= service.created() %></td>
                            <td><%= service.modified() %></td>
                        </tr>
                        <%
                        }
                    }
                %>
            </table>
        </div>
    </body>
</html>

