<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags/templates" prefix="t" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="common" %>


<common:redirect-secured-session /> 

<c:if test="${empty SESSIONID}">
	
	<t:master>
		<jsp:attribute name="head_ext">
			<link rel="stylesheet" href="css/public.css" type="text/css" />
			<script src="js/ext/login.js" type="text/javascript"></script>
		</jsp:attribute>
		
		<jsp:attribute name="header">
			<t:public-header showLogin="true" />
		</jsp:attribute>
		
		<jsp:attribute name="footer">
			<t:footer />
		</jsp:attribute>
		
		
		<jsp:body>
			<h1>Welcome to Gazeebu</h1>
			Your school in the cloud	
			<br>
			
			<b>Form Applications</b><br>
			<a href="${pageContext.servletContext.contextPath}/studentreg:newstudentform.jsp">New Students</a>
			
		</jsp:body>
		
	</t:master>
</c:if>
