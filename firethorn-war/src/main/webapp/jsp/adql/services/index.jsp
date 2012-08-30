<%@ taglib prefix="jstl-core"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring-tags" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="spring-form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="test"        uri="/WEB-INF/tlds/service-tags.xml"%>
<%@ page
    import="uk.ac.roe.wfau.firethorn.webapp.control.PathBuilder"
    import="uk.ac.roe.wfau.firethorn.webapp.control.html.adql.service.ServicesController"
    session="true"
%><%
PathBuilder paths = new PathBuilder(
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
            ADQL TAP Services
            <div>
                <form metod='GET' action='<%= paths.path("adql/services/select") %>'>
                    Select by name <input type='text' name='<%= ServicesController.SELECT_NAME_PROPERTY %>' value=''/>
                    <input type='submit' value='Go'/>
                </form>
            </div>
            <div>
                <form metod='GET' action='<%= paths.path("adql/services/search") %>'>
                    Search by text <input type='text' name='<%= ServicesController.SEARCH_TEXT_PROPERTY %>' value=''/>
                    <input type='submit' value='Go'/>
                </form>
            </div>
        </div>
    </body>
</html>

