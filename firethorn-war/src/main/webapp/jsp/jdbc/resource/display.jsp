<%@ taglib prefix="jstl-core"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring-tags" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="spring-form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="test"        uri="/WEB-INF/tlds/service-tags.xml"%>
<%@ page
    import="uk.ac.roe.wfau.firethorn.webapp.control.PathBuilder"
    import="uk.ac.roe.wfau.firethorn.webapp.control.ServletPathBuilder"
    
    import="uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcResourceBean"
    import="uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcResourceController"
    import="uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcResourcesController"

    session="true"
%><%
PathBuilder paths = new ServletPathBuilder(
    request
    );

JdbcResourceBean resource = (JdbcResourceBean) request.getAttribute(
    JdbcResourceController.RESOURCE_BEAN
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
            JDBC Resource
            <div>
                <table border='1'>
                    <tr>
                        <td>Ident</td>
                        <td><%= resource.getIdent() %></td>
                    </tr>
                    <tr>
                        <td>Name</td>
                        <td><a href='<%= resource.getIdent() %>'><%= resource.getName() %></a></td>
                    </tr>
                    <tr>
                        <td>Created</td>
                        <td><%= resource.getCreated() %></td>
                    </tr>
                    <tr>
                        <td>Modified</td>
                        <td><%= resource.getModified() %></td>
                    </tr>
                    <tr>
                        <td>Class</td>
                        <td><%= resource.getClass().getName() %></td>
                    </tr>
                    <tr>
                        <td>Catalogs</td>
                        <td><a href='<%= resource.getIdent() %>/catalogs'>catalogs</a></td>
                    </tr>
                </table>
            </div>
        </div>
    </body>
</html>

