<%@ taglib prefix="jstl-core"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring-tags" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="spring-form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="test"        uri="/WEB-INF/tlds/service-tags.xml"%>
<%@ page
    import="uk.ac.roe.wfau.firethorn.webapp.control.PathBuilder"
    import="uk.ac.roe.wfau.firethorn.webapp.control.ServletPathBuilder"
    import="uk.ac.roe.wfau.firethorn.webapp.mallard.DataServiceController"
    import="uk.ac.roe.wfau.firethorn.webapp.mallard.DataServicesController"
    import="uk.ac.roe.wfau.firethorn.mallard.DataService"
    session="true"
%><%
PathBuilder paths = new ServletPathBuilder(
    request
    );

String text = (String) request.getAttribute(
    DataServicesController.SEARCH_TEXT
    );

Iterable<DataService> services = (Iterable<DataService>) request.getAttribute(
    DataServicesController.SEARCH_RESULT
    ) ;

%>
<html>
    <head>
	    <title></title>
        <link href='/css/page.css' rel='stylesheet' type='text/css'/>
    </head>
    <body>
        <div>
            <span>[<a href='<%= paths.path(DataServicesController.CONTROLLER_PATH, DataServicesController.SEARCH_PATH) %>'>search</a>]</span>
            <span>[<a href='<%= paths.path(DataServicesController.CONTROLLER_PATH, DataServicesController.SELECT_PATH) %>'>select</a>]</span>
            <span>[<a href='<%= paths.path(DataServicesController.CONTROLLER_PATH, DataServicesController.CREATE_PATH) %>'>create</a>]</span>
        </div>
        <div>
            Search for ADQL TAP Services
            <div>
                <form method='GET' action='<%= paths.path(DataServicesController.CONTROLLER_PATH, DataServicesController.SEARCH_PATH) %>'>
                    Text <input type='text' name='<%= DataServicesController.SEARCH_TEXT %>' value='<%= ((text != null) ? text : "" ) %>'/>
                    <input type='submit' value='Go'/>
                </form>
            </div>
        </div>
        <div>
            <table border='1'>
                <%
                if (services != null)
                    {
                    for (DataService service : services)
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

