<%@ taglib prefix="jstl-core"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring-tags" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="spring-form" uri="http://www.springframework.org/tags/form"%>
<%@ page
    import="java.util.Iterator"
    session="true"
%><%
//
// Java code before page is generated
//
%>
<html>
    <head>
	    <title></title>
        <link href='/css/page.css' rel='stylesheet' type='text/css'/>
    </head>
    <body>
        <div>
            Hello world :-)
        </div>

        <div>
	        Some simple math: ${2+2}
		    <br/>
		    Some simple math with c:out: <jstl-core:out value="${2+2}"/>
        </div>

    </body>
</html>

