<%@ attribute name="style" fragment="true" %>
<%@ attribute name="script" fragment="true" %>

<html>
	<head>
		<style>
			body {
				font-family: arial, verdana, tahoma;
				margin:0;
				background-color: white;
			}
			.head {
				padding-top:10px;
				background-color: rgb(54,142,205);
			}
			.foot {
				background-color: rgb(153,204,255);
			}
			#footmenu {
				font-family: tahoma, verdana, arial;
				font-size: 12px;
			}
			<jsp:invoke fragment="style"/>
		</style>
		<script src="${pageContext.servletContext.contextPath}/js/lib/jquery-all.js"></script>
		<script src="${pageContext.servletContext.contextPath}/js/lib/rameses-lib.js"></script>
		<jsp:invoke fragment="script"/>
	</head>
	<body>
		<table width="100%" height="100%" cellpadding="0" cellspacing="0">
			<tr>
				<td class="head" valign="top">&nbsp;</td>
				<td class="head" width="850" height="55" align="right" valign="top">
					<table width="100%" height="100%" cellpadding="0" cellspacing="0">
						<tr>
							<td id="topmenu" align="left" valign="top">
								<a href="/">
								<img src="img/biglogo.png" width="15%">	
								</a>
							</td>
							<td id="topmenu" align="right" valign="top">
								
							</td>
						</tr>
					</table>
				</td>
				<td class="head">&nbsp;</td>		
			</tr>
			<tr>
				<td class="middle">&nbsp;</td>
				<td class="middle" height="100%" valign="top"  width="850"  style="padding-top: 20px;">
					<jsp:doBody/>
				</td>
				<td class="middle">&nbsp;</td>		
			</tr>
			<tr>
				<td class="foot">&nbsp;</td>
				<td class="foot" height="40">
					<p id="footmenu">
						About &nbsp;&nbsp;
						Privacy &nbsp;&nbsp;
						Terms
					</p>
				</td>
				<td class="foot">&nbsp;</td>				
			</tr>
		</table>
	</body>	
</html>	
	