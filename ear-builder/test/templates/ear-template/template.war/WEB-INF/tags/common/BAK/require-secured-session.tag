<%@ tag import="javax.servlet.http.Cookie"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
	if( request.getAttribute( "SESSIONID" )== null ) {
		response.sendRedirect("authenticate.jsp");
	}
%>

