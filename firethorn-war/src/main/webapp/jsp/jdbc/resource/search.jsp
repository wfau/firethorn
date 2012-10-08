<%@ taglib prefix="jstl-core"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring-tags" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="spring-form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="test"        uri="/WEB-INF/tlds/service-tags.xml"%>
<%@ page
    import="uk.ac.roe.wfau.firethorn.webapp.control.PathBuilder"
    import="uk.ac.roe.wfau.firethorn.webapp.control.ServletPathBuilder"
    
    import="uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcResourceController"
    import="uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcResourcesController"

    import="uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcResourceBean"

    session="true"
%><%
PathBuilder paths = new ServletPathBuilder(
    request
    );

String text = (String) request.getAttribute(
    JdbcResourcesController.SEARCH_TEXT
    );

Iterable<JdbcResourceBean> resources = (Iterable<JdbcResourceBean>) request.getAttribute(
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
            JDBC resources
            <span>[<a href='<%= paths.path(JdbcResourcesController.CONTROLLER_PATH, "search") %>'>search</a>]</span>
            <span>[<a href='<%= paths.path(JdbcResourcesController.CONTROLLER_PATH, "select") %>'>select</a>]</span>
            <span>[<a href='<%= paths.path(JdbcResourcesController.CONTROLLER_PATH, "create") %>'>create</a>]</span>
        </div>
        <div>
            <hr/>
        </div>
        <div>
            Search for JDBC Resources by name
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
                    for (JdbcResourceBean resource : resources)
                        {
                        %>
                        <tr>
                            <td><a href='<%= resource.getIdent() %>'><%= resource.getName() %></a></td>
                            <td><%= resource.getCreated() %></td>
                            <td><%= resource.getModified() %></td>
                        </tr>
                        <%
                        }
                    }
                %>
            </table>
        </div>
    </body>
</html>

