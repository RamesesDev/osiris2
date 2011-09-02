<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags/ui-components" prefix="ui" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="common" %>
<%@ taglib tagdir="/WEB-INF/tags/templates" prefix="t" %>

<common:require-secured-session /> 


<c:if test="${! empty SESSIONID}">
	<common:load-user-role/>
	<t:master>
		<jsp:attribute name="head_ext">
			<link rel="stylesheet" href="css/secured.css" type="text/css" />
			<script src="js/ext/session.js" type="text/javascript"></script>
			<ui:loadinvokers/>
		</jsp:attribute>
	
		<jsp:attribute name="header">
			<ui:useraccountmenu/>
		</jsp:attribute>

		<jsp:attribute name="footer">
			<t:footer />
		</jsp:attribute>

		<jsp:body>
			<table border="0" width="100%" height="100%" cellpadding="0" cellspacing="0" border="1">
				<tr>
					<td width="150px" valign="top" >
						<ui:usermenu target="content"/>
					</td>
		
					<td class="content" valign="top" >
						<table width="100%" cellpadding="0" cellspacing="0">
							<tr>
								<td id="content" style="padding:10px;">
								
								</td>
							</tr>
						</table>
					</td>
				</tr>	
			</table>
		</jsp:body>
	
	</t:master>
</c:if>

