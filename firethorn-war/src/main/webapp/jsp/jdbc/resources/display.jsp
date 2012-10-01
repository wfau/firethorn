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

JdbcResource resource = (JdbcResource) request.getAttribute(
    JdbcResourceController.TARGET_ENTITY
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
            Resource
            <div>
                <table border='1'>
                    <tr>
                        <td>Ident</td>
                        <td><%= resource.ident() %></td>
                    </tr>
                    <tr>
                        <td>Name</td>
                        <td><a href='<%= paths.link(resource) %>'><%= resource.name() %></a></td>
                    </tr>
                    <tr>
                        <td>Owner</td>
                        <td><%= resource.owner().name() %></td>
                    </tr>
                    <tr>
                        <td>Type</td>
                        <td><%= resource.getClass().getName() %></td>
                    </tr>
                </table>
            </div>
        </div>
    </body>
</html>

