<%@ taglib prefix="jstl-core"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring-tags" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="spring-form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="test"        uri="/WEB-INF/tlds/service-tags.xml"%>
<%@ page
    import="uk.ac.roe.wfau.firethorn.webapp.control.PathBuilder"
    import="uk.ac.roe.wfau.firethorn.webapp.control.ServletPathBuilder"
    import="uk.ac.roe.wfau.firethorn.webapp.control.html.adql.service.ServiceController"
    import="uk.ac.roe.wfau.firethorn.webapp.control.html.adql.service.ServicesController"
    import="uk.ac.roe.wfau.firethorn.mallard.DataService"
    session="true"
%><%
PathBuilder paths = new ServletPathBuilder(
    request
    );

String text = (String) request.getAttribute(
    ServicesController.SEARCH_TEXT_PROPERTY
    );

Iterable<DataService> services = (Iterable<DataService>) request.getAttribute(
    ServicesController.SERVICE_ITER_PROPERTY
    ) ;

%>
<html>
    <head>
	    <title></title>
        <link href='/css/page.css' rel='stylesheet' type='text/css'/>
    </head>
    <body>
        <div>
            Search for ADQL TAP Services
            <div>
                <form method='GET' action='<%= paths.path("adql/services/search") %>'>
                    Text <input type='text' name='<%= ServicesController.SEARCH_TEXT_PROPERTY %>' value='<%= ((text != null) ? text : "" ) %>'/>
                    <input type='submit' value='Go'/>
                </form>
            </div>
        </div>
        <div>
            <table border='1'>
                <%
                for (DataService service : services)
                    {
                    %>
                    <tr>
                        <td><%= service.ident() %></td>
                        <td><a href='<%= paths.link(service) %>'><%= service.name() %></a></td>
                        <td><%= service.name() %></td>
                        <td><%= service.owner().name() %></td>
                        <td><%= service.created() %></td>
                        <td><%= service.modified() %></td>
                    </tr>
                    <%
                    }
                %>
            </table>
        </div>
    </body>
</html>

