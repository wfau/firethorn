<%@ taglib prefix="jstl-core"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring-tags" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="spring-form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="test"        uri="/WEB-INF/tlds/service-tags.xml"%>
<%@ page
    import="uk.ac.roe.wfau.firethorn.webapp.control.PathBuilder"
    import="uk.ac.roe.wfau.firethorn.webapp.control.ServletPathBuilder"
    import="uk.ac.roe.wfau.firethorn.webapp.control.html.adql.service.ServicesController"
    import="uk.ac.roe.wfau.firethorn.mallard.DataService"

    session="true"
%><%
PathBuilder paths = new ServletPathBuilder(
    request
    );

String name = (String) request.getAttribute(
    ServicesController.SELECT_NAME_PROPERTY
    );

Iterable<DataService> iter = (Iterable<DataService>) request.getAttribute(
    ServicesController.SERVICE_ITER_PROPERTY
    ) ;

%>
<html>
    <head>
	    <title></title>
        <link href='/css/page.css' rel='stylesheet' type='text/css'/>
    </head>
    <body>
        <div>
            Select ADQL TAP Services by name
            <div>
                <form method='GET' action='<%= paths.path("adql/services/select") %>'>
                    Name <input type='text' name='<%= ServicesController.SELECT_NAME_PROPERTY %>' value='<%= ((name != null) ? name : "" ) %>'/>
                    <input type='submit' value='Go'/>
                </form>
            </div>
        </div>
        <div>
<table border='1'>
<%
for (DataService service : iter)
    {
%>
<tr>
    <td><%= service.ident() %></td>
    <td><%= service.name() %></td>
    <td><%= service.owner() %></td>
    <td><%= service.created() %></td>
    <td><%= service.modified() %></td>
</tr>
<%
    }
%>
</table>

    <!--jstl-core:forEach var="item" items="${requestScope['adql.services.iterator'].iterator()}"-->


<table border='1'>
    <jstl-core:forEach var="item" items="${iter.iterator()}">
        <tr>
            <td>${item.ident()}</td>
            <td>${item.name()}</td>
        </tr>
    </jstl-core:forEach>
</table>

        </div>
    </body>
</html>

