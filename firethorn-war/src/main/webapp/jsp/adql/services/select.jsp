<%@ taglib prefix="jstl-core"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring-tags" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="spring-form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="test"        uri="/WEB-INF/tlds/service-tags.xml"%>
<%@ page
    import="uk.ac.roe.wfau.firethorn.webapp.control.PathBuilder"
    import="uk.ac.roe.wfau.firethorn.webapp.control.ServletPathBuilder"
    import="uk.ac.roe.wfau.firethorn.webapp.control.html.adql.service.ServicesController"
    session="true"
%><%
PathBuilder paths = new ServletPathBuilder(
    request
    );
%>
<html>
    <head>
	    <title></title>
        <link href='/css/page.css' rel='stylesheet' type='text/css'/>
    </head>
    <body>
        <div>
            Select an ADQL TAP Service by name
            <div>
                <form method='GET' action='<%= paths.path("adql/services/select") %>'>
                    Name <input type='text' name='<%= ServicesController.SELECT_NAME_PROPERTY %>' value='<%= request.getAttribute(ServicesController.SELECT_NAME_PROPERTY) %>'/>
                    <input type='submit' value='Go'/>
                </form>
            </div>
        </div>
    </body>
</html>

