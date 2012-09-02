<%@ taglib prefix="jstl-core"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring-tags" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="spring-form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="test"        uri="/WEB-INF/tlds/service-tags.xml"%>
<%@ page
    import="uk.ac.roe.wfau.firethorn.webapp.control.PathBuilder"
    import="uk.ac.roe.wfau.firethorn.webapp.control.ServletPathBuilder"
    import="uk.ac.roe.wfau.firethorn.webapp.widgeon.DataResourceController"
    import="uk.ac.roe.wfau.firethorn.webapp.widgeon.DataResourcesController"
    import="uk.ac.roe.wfau.firethorn.widgeon.DataResource"
    session="true"
%><%
PathBuilder paths = new ServletPathBuilder(
    request
    );

String name = (String) request.getAttribute(
    DataResourcesController.SELECT_NAME
    );

Iterable<DataResource> resources = (Iterable<DataResource>) request.getAttribute(
    DataResourcesController.SELECT_RESULT
    ) ;

%>
<html>
    <head>
	    <title></title>
        <link href='/css/page.css' rel='stylesheet' type='text/css'/>
    </head>
    <body>
        <div>
            <span>[<a href='<%= paths.path(DataResourcesController.CONTROLLER_PATH, "search") %>'>search</a>]</span>
            <span>[<a href='<%= paths.path(DataResourcesController.CONTROLLER_PATH, "select") %>'>select</a>]</span>
            <span>[<a href='<%= paths.path(DataResourcesController.CONTROLLER_PATH, "create") %>'>create</a>]</span>
        </div>
        <div>
            Select ADQL TAP Services by name
            <div>
                <form method='GET' action='<%= paths.path(DataResourcesController.CONTROLLER_PATH, "select") %>'>
                    Name <input type='text' name='<%= DataResourcesController.SELECT_NAME %>' value='<%= ((name != null) ? name : "" ) %>'/>
                    <input type='submit' value='Go'/>
                </form>
            </div>
        </div>
        <div>
            <table border='1'>
                <%
                if (resources != null)
                    {
                    for (DataResource resource : resources)
                        {
                        %>
                        <tr>
                            <td><a href='<%= paths.link(resource) %>'><%= resource.name() %></a></td>
                            <td><%= resource.owner().name() %></td>
                            <td><%= resource.created() %></td>
                            <td><%= resource.modified() %></td>
                        </tr>
                        <%
                        }
                    }
                %>
            </table>
        </div>
    </body>
</html>

