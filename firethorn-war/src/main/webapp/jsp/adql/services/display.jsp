<%@ taglib prefix="jstl-core"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring-tags" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="spring-form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="test"        uri="/WEB-INF/tlds/service-tags.xml"%>
<%@ page
    import="uk.ac.roe.wfau.firethorn.webapp.control.PathBuilder"
    import="uk.ac.roe.wfau.firethorn.webapp.control.ServletPathBuilder"

    import="uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlServiceController"
    import="uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlServicesController"

    import="uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlServiceBean"

    session="true"
%><%

PathBuilder paths = new ServletPathBuilder(
    request
    );

AdqlServiceBean service = (AdqlServiceBean) request.getAttribute(
    AdqlServiceController.TARGET_ENTITY
    ) ;

%>
<html>
    <head>
	    <title></title>
        <link href='<%= paths.file("/css/page.css") %>' rel='stylesheet' type='text/css'/>
    </head>
    <body>
        <div>
            <span>[<a href='<%= paths.path(AdqlServicesController.CONTROLLER_PATH, AdqlServicesController.SEARCH_PATH) %>'>search</a>]</span>
            <span>[<a href='<%= paths.path(AdqlServicesController.CONTROLLER_PATH, AdqlServicesController.SELECT_PATH) %>'>select</a>]</span>
            <span>[<a href='<%= paths.path(AdqlServicesController.CONTROLLER_PATH, AdqlServicesController.CREATE_PATH) %>'>create</a>]</span>
        </div>
        <div>
            ADQL (TAP) Service
            <div>
                <table border='1'>
                <table border='1'>
                    <tr>
                        <td>Ident</td>
                        <td><%= service.getIdent() %></td>
                    </tr>
                    <tr>
                        <td>Name</td>
                        <td><a href='<%= service.getIdent() %>'><%= service.getName() %></a></td>
                    </tr>
                    <tr>
                        <td>Created</td>
                        <td><%= service.getCreated() %></td>
                    </tr>
                    <tr>
                        <td>Modified</td>
                        <td><%= service.getModified() %></td>
                    </tr>
                    <tr>
                        <td>Class</td>
                        <td><%= service.getClass().getName() %></td>
                    </tr>
                </table>
            </div>
        </div>
    </body>
</html>

