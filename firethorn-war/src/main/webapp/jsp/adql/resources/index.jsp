<%@ taglib prefix="jstl-core"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring-tags" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="spring-form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="test"        uri="/WEB-INF/tlds/service-tags.xml"%>
<%@ page
    import="uk.ac.roe.wfau.firethorn.webapp.control.PathBuilderBase"
    import="uk.ac.roe.wfau.firethorn.webapp.control.ServletPathBuilder"
    import="uk.ac.roe.wfau.firethorn.webapp.widgeon.DataResourceController"
    import="uk.ac.roe.wfau.firethorn.webapp.widgeon.DataResourcesController"
    import="uk.ac.roe.wfau.firethorn.widgeon.DataResource"
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
            <span>[<a href='<%= paths.path(DataResourcesController.CONTROLLER_PATH, "search") %>'>search</a>]</span>
            <span>[<a href='<%= paths.path(DataResourcesController.CONTROLLER_PATH, "select") %>'>select</a>]</span>
            <span>[<a href='<%= paths.path(DataResourcesController.CONTROLLER_PATH, "create") %>'>create</a>]</span>
        </div>
        <div>
            Resources
            <div>
                Select a service by name
                <form method='GET' action='<%= paths.path(DataResourcesController.CONTROLLER_PATH, "select") %>'>
                    Name <input type='text' name='<%= DataResourcesController.SELECT_NAME %>' value=''/>
                    <input type='submit' value='Go'/>
                </form>
            </div>
            <div>
                Search for services with text
                <form method='GET' action='<%= paths.path(DataResourcesController.CONTROLLER_PATH, "search") %>'>
                    Text <input type='text' name='<%= DataResourcesController.SEARCH_TEXT %>' value=''/>
                    <input type='submit' value='Go'/>
                </form>
            </div>
            <div>
                Create new service
                <form method='POST' action='<%= paths.path(DataResourcesController.CONTROLLER_PATH, "create") %>'>
                    Name <input type='text' name='<%= DataResourcesController.CREATE_NAME %>' value=''/>
                    <input type='submit' value='Go'/>
                </form>
            </div>
        </div>
    </body>
</html>

