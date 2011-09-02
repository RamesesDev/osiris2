<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ attribute name="before_rendering" fragment="true" %>
<%@ attribute name="head" fragment="true" %>
<%@ attribute name="header_middle" fragment="true" %>
<%@ attribute name="header_right" fragment="true" %>

<%@ attribute name="profile" fragment="true" %>
<%@ attribute name="taskmenu" fragment="true" %>
<%@ attribute name="usermenu" fragment="true" %>

<c:if test="${empty SESSIONID}">
	<%response.sendRedirect("authenticate.jsp");%>
</c:if>

<c:if test="${!empty SESSIONID}">
	<jsp:invoke fragment="before_rendering"/>
	<html>
		<head>
			<link href="${pageContext.servletContext.contextPath}/js/lib/css/jquery-ui/jquery.css" type="text/css" rel="stylesheet" />
			<link href="${pageContext.servletContext.contextPath}/js/lib/css/rameses-lib.css" type="text/css" rel="stylesheet" />
			<script src="${pageContext.servletContext.contextPath}/js/lib/jquery-all.js"></script>
			<script src="${pageContext.servletContext.contextPath}/js/lib/jquery-ba.bbq.js"></script>
			<script src="${pageContext.servletContext.contextPath}/js/lib/rameses-lib.js"></script>
			<link href="${pageContext.servletContext.contextPath}/css/secured.css" type="text/css" rel="stylesheet" />
			<jsp:invoke fragment="head"/>
		</head>
		<body>
			<table width="100%" cellpadding="0" cellspacing="0" height="100%">
				<tr>
					<td class="header">&nbsp;</td>
					<td class="header" width="980" height="40">
						<table width="100%" height="100%" cellpadding="0" cellspacing="0">
							<tr>
								<td width="165">
									<img src="${pageContext.servletContext.contextPath}/img/biglogo.png" height="30px">
								</td>
								<td align="left">
									<jsp:invoke fragment="header_middle"/>
								</td>
								<td align="right">
									<jsp:invoke fragment="header_right"/>
								</td>
							</tr>
						</table>
					</td>
					<td  class="header">&nbsp;</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td height="100%" style="padding-top:10px;">
						<table width="100%" height="100%" cellpadding="0" cellspacing="0">
							<tr>
								<td valign="top" width="165" height="100%">
									<table width="100%" cellpadding="0" cellspacing="0">
										<tr>
											<td valign="top">
												<jsp:invoke fragment="profile"/>
											</td>
										</tr>
										<tr>
											<td valign="top">
												<jsp:invoke fragment="taskmenu"/>
											</td>
										</tr>
										<tr>
											<td valign="top">
												<jsp:invoke fragment="usermenu"/>
											</td>
										</tr>
									</table>
									
								</td>
								<td valign="top" height="100%">
									<table class="shadowbox" width="100%" height="100%">
										<tr>
											<td id="content" height="100%" valign="top">&nbsp;</td> 
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td class="footer">&nbsp;</td>
					<td class="footer" width="980" valign="top">
						<table width="100%" cellpadding="0" cellspacing="0" class="footer">
							<tr>
								<td width="165">&nbsp;</td>
								<td>
									About Terms Privacy								
								</td>
							</tr>
						</table>
					</td>
					<td class="footer">&nbsp;</td>
				</tr>
			</table>		
		</body>
	</html>	
</c:if>
