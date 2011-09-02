<%@ attribute name="leftactions" fragment="true" %>
<%@ attribute name="rightactions" fragment="true" %>
<%@ attribute name="head" fragment="true" %>

<jsp:invoke fragment="head"/>

<table width="100%" height="100%">
	<tr>
		<td colspan="2" valign="top">
			<jsp:doBody/>
		</td>
	</tr>
	<tr>
		<td align="left" height="40" valign="top">
			<jsp:invoke fragment="leftactions"/>
		</td>
		<td align="right" height="40" valign="top">
			<jsp:invoke fragment="rightactions"/>
		</td>
	</tr>
</table>
