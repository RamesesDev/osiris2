<%@ taglib tagdir="/WEB-INF/tags/templates" prefix="t"%>
<%@ taglib tagdir="/WEB-INF/tags/ui-components" prefix="ui"%>
<%@ attribute name="selection" fragment="false" %>

<html>
	<head>
		<style>
			body {
				font-family: arial, verdana, tahoma;
				margin:0;
				background-color: rgb(204,255,255);
			}
			.head {
				padding-top:10px;
				background-color: rgb(54,142,205);
			}
			.middle {
				background-color: rgb(153,204,255);
			}
			.tabmenu {
				padding:8px;
				background-color: rgb(71,184,184);
				color:white;
				font-size: 15px;
				font-family: arial;
				color:lightgrey;
			}
			.tabmenu-space {
				padding:2px;
			}
			.tabmenu-selected {
				padding:8px;
				background-color: rgb(153,204,255);
				color:rgb(0,128,128);
				font-weight:bolder;
				font-size: 15px;
				font-family: arial;
			}
			
			#footdeco {
				background-image: url('img/footdeco.jpg');
				background-repeat: repeat-x;
			}
			
			.shadowbox {
				-webkit-border-radius:5px;
				-moz-border-radius:5px; 
				background:white; 
				border:1px solid lightgrey;
				-moz-box-shadow: 5px 5px 5px #ccc;
				-webkit-box-shadow: 5px 5px 5px #ccc;
				box-shadow: 5px 5px 5px #ccc;
				padding:10px;
			}
			
			#subtitle {
				font-size:30px; color:green;font-family:tahoma; font-weight:bolder;
			}
			#description {
				padding-top:20px;
				padding-left: 20px;
				font-size: 20px; color:darkslateblue; 
				font-family:arial, tahoma;
				width:350px;
			}
			#features {
				font-size: 16px;
				font-family: arial, verdana;
				color:darkslateblue;
			}
			
			#learnmore {
				padding-top: 20px;
				font-size: 30px;
				padding-right: 80px;
				text-align: right;
				font-family: arial;
				color:brown;
				font-weight: bolder;
			}
			
			#footmenu {
				font-family: arial;
				font-size: 14px;
			}
			
			#logininput {
				font-size:11px; 
				font-family:tahoma;	
				width:150px;
				height:22px;
			}
			.loginform .hint {
				font-size:11px; 
				font-family:tahoma;	
				color: gray;
			}
			.loginbutton {
				font-size:11px; 
				font-family:tahoma;	
				height:20px;
				width:50px;
				border:1px solid lightgrey;
			}
			.loginaid {
				font-size:11px; font-family:tahoma;	color:white;
			}
		</style>
		
		<script src="js/lib/jquery-all.js"></script>
		<script src="js/lib/rameses-lib.js"></script>
		
		<script>
			$(function() {
				var func = function() {
					var pos = $('#regcontainer').css({width:'350px'}).position();
					$('#regform').css( {top:pos.top, left:pos.left} ).show();
				}
				func();
				$(window).resize(func);	
			});
			
			$put("login",
				new function() {
					this.data = {};
					var submitting = false;
					
					this.login = function() {
						try {
							if(submitting) throw Error("form is still submitting");
							submitting = true;
							var svc = ProxyService.lookup('LoginService');
							var  id = svc.login(this.data);
							Session.create(id.sessionid);
							return true;
						}
						catch(e) {
							alert(e.message);
							return false;
						}
						finally {
							submitting = false;
						}
					}
					
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
								<common:login/>
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
							<td class="tabmenu-selected" style="padding-left:25px;padding-right:25px">Home</td>
							<td class="tabmenu-space">&nbsp;</td>
							<td class="tabmenu">Community</td>
							<td class="tabmenu-space">&nbsp;</td>
							<td class="tabmenu">Developer</td>
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
