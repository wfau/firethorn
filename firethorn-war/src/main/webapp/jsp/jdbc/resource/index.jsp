<%@ taglib prefix="jstl-core"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring-tags" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="spring-form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="test"        uri="/WEB-INF/tlds/service-tags.xml"%>
<%@ page
    import="uk.ac.roe.wfau.firethorn.webapp.control.PathBuilder"
    import="uk.ac.roe.wfau.firethorn.webapp.control.ServletPathBuilder"
    
    import="uk.ac.roe.wfau.firethorn.webapp.widgeon.JdbcResourceController"
    import="uk.ac.roe.wfau.firethorn.webapp.widgeon.JdbcResourcesController"

    import="uk.ac.roe.wfau.firethorn.webapp.widgeon.JdbcResourceBean"
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
            JDBC Resources
            <span>[<a href='<%= paths.path(JdbcResourcesController.CONTROLLER_PATH, "search") %>'>search</a>]</span>
            <span>[<a href='<%= paths.path(JdbcResourcesController.CONTROLLER_PATH, "select") %>'>select</a>]</span>
            <span>[<a href='<%= paths.path(JdbcResourcesController.CONTROLLER_PATH, "create") %>'>create</a>]</span>
        </div>
        <div>
            <hr/>
        </div>
        <div>
            <div>
                Select a JDBC resource by name
                <form method='GET' action='<%= paths.path(JdbcResourcesController.CONTROLLER_PATH, "select") %>'>
                    Name <input type='text' name='<%= JdbcResourcesController.SELECT_NAME %>' value=''/>
                    <input type='submit' value='Go'/>
                </form>
            </div>
            <div>
                Search for JDBC resources by name
                <form method='GET' action='<%= paths.path(JdbcResourcesController.CONTROLLER_PATH, "search") %>'>
                    Text <input type='text' name='<%= JdbcResourcesController.SEARCH_TEXT %>' value=''/>
                    <input type='submit' value='Go'/>
                </form>
            </div>
            <div>
                Create a new JDBC resource
                <form method='POST' action='<%= paths.path(JdbcResourcesController.CONTROLLER_PATH, "create") %>'>
                    Name <input type='text' name='<%= JdbcResourcesController.CREATE_NAME %>' value=''/>
                    <input type='submit' value='Go'/>
                </form>
            </div>
        </div>
    </body>
</html>

