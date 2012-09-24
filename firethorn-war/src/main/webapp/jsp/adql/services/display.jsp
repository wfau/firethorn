<%@ taglib prefix="jstl-core"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring-tags" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="spring-form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="test"        uri="/WEB-INF/tlds/service-tags.xml"%>
<%@ page
    import="uk.ac.roe.wfau.firethorn.webapp.control.PathBuilder"
    import="uk.ac.roe.wfau.firethorn.webapp.control.ServletPathBuilder"
    import="uk.ac.roe.wfau.firethorn.webapp.mallard.AdqlServiceController"
    import="uk.ac.roe.wfau.firethorn.webapp.mallard.AdqlServicesController"
    import="uk.ac.roe.wfau.firethorn.mallard.AdqlService"
    session="true"
%><%

PathBuilder paths = new ServletPathBuilder(
    request
    );

DataService service = (DataService) request.getAttribute(
    DataServiceController.SERVICE_ENTITY
    ) ;

%>
<html>
    <head>
	    <title></title>
        <link href='/css/page.css' rel='stylesheet' type='text/css'/>
    </head>
    <body>
        <div>
            <span>[<a href='<%= paths.path(DataServicesController.CONTROLLER_PATH, DataServicesController.SEARCH_PATH) %>'>search</a>]</span>
            <span>[<a href='<%= paths.path(DataServicesController.CONTROLLER_PATH, DataServicesController.SELECT_PATH) %>'>select</a>]</span>
            <span>[<a href='<%= paths.path(DataServicesController.CONTROLLER_PATH, DataServicesController.CREATE_PATH) %>'>create</a>]</span>
        </div>
        <div>
            ADQL TAP Service
            <div>
                <table border='1'>
                    <tr>
                        <td>Ident</td>
                        <td><%= service.ident() %></td>
                    </tr>
                    <tr>
                        <td>Name</td>
                        <td><a href='<%= paths.link(service) %>'><%= service.name() %></a></td>
                    </tr>
                    <tr>
                        <td>Owner</td>
                        <td><%= service.owner().name() %></td>
                    </tr>
                </table>
            </div>
        </div>
    </body>
</html>

