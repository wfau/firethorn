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

JdbcResource.JdbcCatalog catalog = (JdbcResource.JdbcCatalog) request.getAttribute(
    JdbcCatalogController.TARGET_ENTITY
    ) ;
%>
<html>
    <head>
	    <title></title>
        <link href='<%= paths.file("/css/page.css") %>' rel='stylesheet' type='text/css'/>
    </head>
    <body>
        <div>
        </div>
        <div>
            Resource
            <div>
                <table border='1'>
                    <tr>
                        <td>Ident</td>
                        <td><%= catalog.ident() %></td>
                    </tr>
                    <tr>
                        <td>Name</td>
                        <td><a href='<%= paths.link(catalog) %>'><%= catalog.name() %></a></td>
                    </tr>
                    <tr>
                        <td>Owner</td>
                        <td><%= catalog.owner().name() %></td>
                    </tr>
                    <tr>
                        <td>Type</td>
                        <td><%= catalog.getClass().getName() %></td>
                    </tr>
                </table>
            </div>
        </div>
    </body>
</html>

