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
    import="uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcResourceCatalogsController"

    import="uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcCatalogBean"
    import="uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcCatalogController"

    session="true"
%><%
PathBuilder paths = new ServletPathBuilder(
    request
    );

JdbcResourceBean resource = (JdbcResourceBean) request.getAttribute(
    JdbcResourceController.RESOURCE_BEAN
    ) ;

JdbcCatalogBean catalog = (JdbcCatalogBean) request.getAttribute(
    JdbcCatalogController.CATALOG_BEAN
    ) ;

%>
<html>
    <head>
	    <title></title>
        <link href='<%= paths.file("/css/page.css") %>' rel='stylesheet' type='text/css'/>
    </head>
    <body>
        <div>
            Resource catalog
            <div>
                <table border='1'>
                    <tr>
                        <td>Name</td>
                        <td><a href='<%= catalog.getIdent() %>'><%= catalog.getName() %></a></td>
                    </tr>
                    <tr>
                        <td>Created</td>
                        <td><%= catalog.getCreated() %></td>
                    </tr>
                    <tr>
                        <td>Modified</td>
                        <td><%= catalog.getModified() %></td>
                    </tr>
                    <tr>
                        <td>Type</td>
                        <td><%= catalog.getClass().getName() %></td>
                    </tr>
                    <tr>
                        <td>Parent</td>
                        <td><a href='<%= resource.getIdent() %>'><%= resource.getName() %></a></td>
                    </tr>
                </table>
            </div>
        </div>
    </body>
</html>

