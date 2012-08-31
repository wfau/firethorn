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
            Search for ADQL TAP Services
            <div>
                <form metod='GET' action='<%= paths.path("adql/services/search") %>'>
                    Text <input type='text' name='<%= ServicesController.SEARCH_TEXT_PROPERTY %>' value='<%= request.getAttribute(ServicesController.SEARCH_TEXT_PROPERTY) %>'/>
                    <input type='submit' value='Go'/>
                </form>
            </div>
        </div>
    </body>
</html>

