<%@ taglib prefix="jstl-core"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring-tags" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="spring-form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="test"        uri="/WEB-INF/tlds/service-tags.xml"%>
<%@ page
    import="uk.ac.roe.wfau.firethorn.webapp.control.PathBuilder"
    import="uk.ac.roe.wfau.firethorn.webapp.control.ServletPathBuilder"
    import="uk.ac.roe.wfau.firethorn.webapp.mallard.DataServiceController"
    import="uk.ac.roe.wfau.firethorn.webapp.mallard.DataServicesController"
    import="uk.ac.roe.wfau.firethorn.mallard.DataService"
    session="true"
%><%
PathBuilder paths = new ServletPathBuilder(
    request
    );

String name = (String) request.getAttribute(
    DataServicesController.CREATE_NAME
    );

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
            Create an ADQL TAP Service
            <div>
                <form method='POST' action='<%= paths.path(DataServicesController.CONTROLLER_PATH, DataServicesController.CREATE_PATH) %>'>
                    Name <input type='text' name='<%= DataServicesController.CREATE_NAME %>' value='<%= ((name != null) ? name : "") %>'/>
                    <input type='submit' value='Go'/>
                </form>
            </div>
        </div>
    </body>
</html>

