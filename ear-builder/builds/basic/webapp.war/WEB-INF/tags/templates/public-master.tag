<%@ taglib tagdir="/WEB-INF/tags/templates" prefix="t" %>

<t:master>

	<jsp:attribute name="head_ext">
		<link rel="stylesheet" href="${pageContext.servletContext.contextPath}/css/public.css" type="text/css" />
	</jsp:attribute>

	<jsp:attribute name="header">
		<t:public-header showLogin="false"/>
	</jsp:attribute>

	<jsp:attribute name="footer">
		<t:public-footer />
	</jsp:attribute>

	<jsp:body>
		<jsp:doBody/>
	</jsp:body>

</t:master>
