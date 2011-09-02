<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ attribute name="redirect_session" fragment="false" %>
<%@ attribute name="tab" fragment="false" %>

<c:if test="${!empty SESSIONID and redirect_session=='true'}">
	response.sendRedirect("home.jsp");
</c:if>

<c:if test="${(empty SESSIONID) || (empty redirect_session) || (redirect_session=='false')}">
	<html>
		<head>
			<style>
				<title>Gazeebu Classroom</title>
				<meta charset="UTF-8" >
				<link rel="stylesheet" href="${pageContext.servletContext.contextPath}/js/lib/css/jquery-ui/jquery.css" type="text/css" />
				<link rel="stylesheet" href="${pageContext.servletContext.contextPath}/js/lib/css/rameses-lib.css" type="text/css" />
				<script src="${pageContext.servletContext.contextPath}/js/lib/jquery-all.js"></script>
				<script src="${pageContext.servletContext.contextPath}/js/lib/rameses-lib.js"></script>
				<link rel="stylesheet" href="${pageContext.servletContext.contextPath}/css/index.css" type="text/css" />	
			<style>
			
			<script>
				$put("login",
					new function() {
						this.username;
						this.password;
						this.onload = function() {
							Session.errorHandler = function(o) {
								window.location = "home.jsp";
							}
							Session.init();
						}
					}
				);
			</script>
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
										<img src="img/biglogo.png" style="border:none;">	
									</a>
								</td>
								<td id="topmenu" align="right" valign="top">

									<c:if test="${empty SESSIONID}">
										<form action="login.jsp" method="post">
											<table cellspacing="0" cellpadding="1" class="loginform">
												<tr>
													<td valign="top" colspan="2">
														<input type="text" name="username" hint="Email" id="logininput" context="login" />
													</td>	
													<td valign="top">
														<input type="password" name="password" hint="Password" id="logininput"  context="login"/>
													</td>
													<td valign="top" style="padding-top:3px;">
														<button type="submit" class="loginbutton">
															Login
														</button>
													</td>
												</tr>
												<tr>
													<td class="loginaid" width="20" align="center">
														<div>
														<input type="checkbox" style="margin:0;width:15px;height:13px;float:left;overflow:hidden;">
														</div>
													</td>
													<td class="loginaid">
														Keep me logged in
													</td>
													<td><a class="loginaid">Forgot Password?</a></td>
													<td valign="top">&nbsp;</td>
												</tr>
											</table>
										</form>
									</c:if>

								</td>
							</tr>
						</table>
					</td>
					<td class="head">&nbsp;</td>		
				</tr>
				
				<tr>
					<td class="head">&nbsp;</td>
					<td class="head">
						<table cellpadding="0" cellspacing="0">
							<tr>
								<td class="${ (empty tab || tab=='home') ? 'tabmenu-selected' : 'tabmenu'}" style="padding-left:25px;padding-right:25px">
									<a href="home.jsp">Home</a>
								</td>
								<td class="tabmenu-space">&nbsp;</td>
								<td class="${tab=='community' ? 'tabmenu-selected' : 'tabmenu'}">
									<a href="community.jsp">Community</a>
								</td>
								<td class="tabmenu-space">&nbsp;</td>
								<td class="${tab=='developer' ? 'tabmenu-selected' : 'tabmenu'}">
									<a href="developer.jsp">Developer</a>
								</td>
							</tr>
						</table>
					</td>
					<td class="head">&nbsp;</td>
				</tr>
				
				<tr>
					<td class="middle">&nbsp;</td>
					<td class="middle" height="320" valign="top"  style="padding-top: 20px;">
						<jsp:doBody/>
					</td>
					<td class="middle">&nbsp;</td>		
				</tr>
				
				<tr>
					<td class="foot" id="footdeco" colspan="3">&nbsp;</td>
				</tr>
				<tr>
					<td class="foot">&nbsp;</td>
					<td class="foot" height="100%">
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
</c:if>