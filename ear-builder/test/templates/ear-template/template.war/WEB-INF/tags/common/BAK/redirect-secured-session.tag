<%@ tag import="javax.servlet.http.Cookie"%>
<%@ attribute name="page"%>

<%
	if( request.getAttribute("SESSIONID") != null ) {
		response.sendRedirect(page);
	}
%>

