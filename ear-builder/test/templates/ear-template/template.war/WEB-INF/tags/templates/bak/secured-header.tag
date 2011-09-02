<%@ taglib tagdir="/WEB-INF/tags/templates" prefix="t"%>
<%@ taglib tagdir="/WEB-INF/tags/ui-components" prefix="ui"%>
<%@ attribute name="username" rtexprvalue="true" %>
<%@ attribute name="contextmenu" fragment="true" %>
<table height="40px"  cellspacing="0" cellpadding="0" width="100%" border="0" style="color:white;">
	<tr>
		<td width="180px" valign="middle">
			<a href="#" >
				<img src="${pageContext.servletContext.contextPath}/img/secured-logo.png" style="border:none;">
			</a>
		</td>
		<td align="left" valign="top" style="color:white;font-size:12px;font-family:arial;padding-top:8px;">
			<jsp:invoke fragment="contextmenu" />	
		</td>
		<td style="font-size:10px;color:white;" width="250px" align="right">
			<a class="secured-link" context="session" name="showProfileMenu">
				Welcome ${username} &#9660;
			</a>
		</td>
	</tr>
</table>
	
<div id="profile-menu" style="display: none">
	<div style="padding:1px;font-size:12px;"> 
		<a href="#">Edit Profile</a><br/>
		<a href="#">Change Password</a><br/>
		<hr>
		<a context="session" name="logout">Logout</a>
	</div>
</div>

 

