<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="showLogin" rtexprvalue="true" %>

<table width="100%" style="height:80px;">
	<tr>
		<td width="260px">
			<a href="index.jsp">
				<img border="0" src="${pageContext.servletContext.contextPath}/img/public-logo.png"> 
			</a>
		</td>
		<c:if test="${showLogin == 'true' }">
			<td align="right" valign="top">
				<table style="color:white;font-size:10px;" cellpadding="0" cellspacing="0">
					<tr>
						<td><input type="text" size="15" context="login" name="data.username" hint="Email"></td>
						<td><input type="password" size="15" context="login" name="data.password" hint="Password"></td>
						<td><input type="button" value="Login" context="login" name="login" style="border:none;"></td>
					</tr>
				</table>
			</td>
		</c:if>	
	</tr>
</table>

