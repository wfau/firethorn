<%@ taglib prefix="jstl-core"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring-tags" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="spring-form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="test"        uri="/WEB-INF/tlds/service-tags.xml"%>
<%@ page
    import="uk.ac.roe.wfau.firethorn.webapp.control.PathBuilder"
    import="uk.ac.roe.wfau.firethorn.webapp.control.ServletPathBuilder"

    import="uk.ac.roe.wfau.firethorn.webapp.widgeon.JdbcResourceController"
    import="uk.ac.roe.wfau.firethorn.webapp.widgeon.JdbcResourcesController"
    import="uk.ac.roe.wfau.firethorn.webapp.widgeon.JdbcResourceCatalogsController"

    import="uk.ac.roe.wfau.firethorn.webapp.widgeon.JdbcCatalogController"

    import="uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcResource"
    import="uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcResource.JdbcCatalog"

    session="true"
%><%
PathBuilder paths = new ServletPathBuilder(
    request
    );

String name = (String) request.getAttribute(
    JdbcResourcesController.CREATE_NAME
    );
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
            Create a (base)  Resource
            <div>
                <form method='POST' action='<%= paths.path(JdbcResourcesController.CONTROLLER_PATH, "create") %>'>
                    Name <input type='text' name='<%= JdbcResourcesController.CREATE_NAME %>' value='<%= ((name != null) ? name : "") %>'/>
                    <input type='submit' value='Go'/>
                </form>
            </div>
        </div>
    </body>
</html>

