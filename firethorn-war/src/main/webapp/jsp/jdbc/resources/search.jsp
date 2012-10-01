<%@ taglib prefix="jstl-core"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring-tags" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="spring-form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="test"        uri="/WEB-INF/tlds/service-tags.xml"%>
<%@ page
    import="uk.ac.roe.wfau.firethorn.webapp.control.PathBuilder"
    import="uk.ac.roe.wfau.firethorn.webapp.control.ServletPathBuilder"
    import="uk.ac.roe.wfau.firethorn.webapp.widgeon.JdbcResourceController"
    import="uk.ac.roe.wfau.firethorn.webapp.widgeon.JdbcResourcesController"
    import="uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcResource"
    session="true"
%><%
PathBuilder paths = new ServletPathBuilder(
    request
    );

String text = (String) request.getAttribute(
    JdbcResourcesController.SEARCH_TEXT
    );

Iterable<JdbcResource> resources = (Iterable<JdbcResource>) request.getAttribute(
    JdbcResourcesController.SEARCH_RESULT
    ) ;

%>
<html>
    <head>
	    <title></title>
        <link href='<%= paths.file("/css/page.css") %>' rel='stylesheet' type='text/css'/>
    </head>
    <body>
        <div>
            <span>[<a href='<%= paths.path(JdbcResourcesController.CONTROLLER_PATH, "search") %>'>search</a>]</span>
            <span>[<a href='<%= paths.path(JdbcResourcesController.CONTROLLER_PATH, "select") %>'>select</a>]</span>
            <span>[<a href='<%= paths.path(JdbcResourcesController.CONTROLLER_PATH, "create") %>'>create</a>]</span>
        </div>
        <div>
            Search for Resources
            <div>
                <form method='GET' action='<%= paths.path(JdbcResourcesController.CONTROLLER_PATH, "search") %>'>
                    Text <input type='text' name='<%= JdbcResourcesController.SEARCH_TEXT %>' value='<%= ((text != null) ? text : "" ) %>'/>
                    <input type='submit' value='Go'/>
                </form>
            </div>
        </div>
        <div>
            <table border='1'>
                <%
                if (resources != null)
                    {
                    for (JdbcResource resource : resources)
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

