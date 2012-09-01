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

String name = (String) request.getAttribute(
    ServicesController.CREATE_NAME_PROPERTY
    );

%>
<html>
    <head>
	    <title></title>
        <link href='/css/page.css' rel='stylesheet' type='text/css'/>
    </head>
    <body>
        <div>
            <span>[<a href='<%= paths.path("adql/services/search") %>'>search</a>]</span>
            <span>[<a href='<%= paths.path("adql/services/select") %>'>select</a>]</span>
            <span>[<a href='<%= paths.path("adql/services/create") %>'>create</a>]</span>
        </div>
        <div>
            Create an ADQL TAP Service
            <div>
                <form method='POST' action='<%= paths.path("adql/services/create") %>'>
                    Name <input type='text' name='<%= ServicesController.CREATE_NAME_PROPERTY %>' value='<%= ((name != null) ? name : "") %>'/>
                    <input type='submit' value='Go'/>
                </form>
            </div>
        </div>
    </body>
</html>

