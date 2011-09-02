<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags/templates" prefix="t" %>
<%@ taglib tagdir="/WEB-INF/tags/common/ui" prefix="common" %>
<%@ taglib tagdir="/WEB-INF/tags/common/server" prefix="s" %>


<t:secured>

	<jsp:attribute name="before_rendering">
		<s:invoke service="SessionService" method="getInfo" params="${SESSIONID}" var="SESSION_INFO"/>
		<s:invoke service="ClassService" method="getOpenClasses" params="${SESSION_INFO.userid}" var="OPEN_CLASSES"/>
		<c:if test="${! empty param['classid']}">
			<s:invoke service="ClassService" method="getClassInfo" params="${param['classid']}" var="CLASS_INFO"/>
		</c:if>
	</jsp:attribute>
	
	<jsp:attribute name="head">
		<link rel="stylesheet" href="css/home.css" type="text/css" />
		<common:loadinvokers/>	
		<script>
			$put("session",
				new function() {
					var classSvc = ProxyService.lookup("ClassService");
					var self = this;
					this._controller;
					
					this.logout = function() {
						var svc = ProxyService.lookup('LogoutService');
						svc.logout( {sessionid: Session.getId() }  ); 
						Session.destroy();
						window.location = "logout.jsp";
					}
					this.showProfileMenu = function() {
						var popup = new DropdownOpener( 'useraccount:useraccount_menu' );
						popup.options.position = {my: 'right top', at: 'right bottom'};
						return popup;
					}
					this.createClass = function() {
						var saveHandler = function(id) {
							WindowUtil.reload( {classid:id}, "bulletin:bulletin" );
						}
						return new PopupOpener("class:class_new", {handler: saveHandler} );
					}
					
					this.onload = function() {
						if( !Session.getId()) window.location.reload(true);
						//very impt. store sessionid in env
						if(Session.getId()) Env.data.sessionid = Session.getId();

						Session.connect( "${SESSION_INFO.host}" );
						Session.handlers.session = function(o) {
							if( o.msgtype == "session-expired" ) {
								Session.destroy();
								window.location.reload(true);
							}	
						}
						Session.handlers.classroom = function(o) {
							if( o.msgtype == "online" || o.msgtype == "offline" ) {
								if(o.usertype=="student") {
									var student = self.studentList.find( function(x) { return x.objid == o.userid } );
									if(student) {
										student.status = o.msgtype;
									}
								}
								else if( o.usertype == "teacher" ) {
									self.teacher.status = o.msgtype;
								}
								self._controller.refresh();
							}	
							else if(o.msgtype == "addStudent") {
								self.studentList = classSvc.getStudents("${param['classid']}");
								self._controller.refresh();
							}
						}
						
						this.teacher = {
							objid: "${CLASS_INFO.teacher.objid}",
							firstname: "${CLASS_INFO.teacher.firstname}",
							lastname: "${CLASS_INFO.teacher.lastname}",
							status: "${CLASS_INFO.teacher.status}"
						}
						this.studentList = classSvc.getStudents("${param['classid']}");
						Hash.init();
					}
					
					
				}
			);	
			
			
		</script>
	</jsp:attribute>
	
	<jsp:attribute name="header_middle">
		<div style="color:white;font-weight:bolder;">
			<c:if test="${! empty CLASS_INFO}">
				<div>${CLASS_INFO.name}</div>
				<div style="font-size:10px;">${CLASS_INFO.classurl}</div>
			</c:if>
			<c:if test="${empty CLASS_INFO}">
				No active class
			</c:if>
		</div>
	</jsp:attribute>
	
	<jsp:attribute name="header_right">
		<a href="#" id="useraccountmenu" context="session" name="showProfileMenu">
			Hi ${SESSION_INFO.username}&nbsp;&nbsp;&#9660;
		</a>
	</jsp:attribute>
	
	<jsp:attribute name="profile">
		<table cellspacing="0" cellspacing="0">
			<tr>
				<td rowspan="2" valign="top"><img src="img/profilephoto.png"/></td>
				<td style="padding-left:2px;font-size:11px;">
					${SESSION_INFO.lastname}, ${SESSION_INFO.firstname}
				</td>
			</tr>	
			<tr>
				<td><a href="#useraccount:user_profile">Edit Profile</a></td>
			</tr>
		</table>
	</jsp:attribute>
	
	<jsp:attribute name="taskmenu">
		<br>
		<table width="100%" cellpadding="0" cellspacing="0" class="menu">
			<tr>
				<td class="menutitle">Tasks</td>
			</tr>
			<c:forEach items="${TASKLIST}" var="item">
				<tr>
					<td class="menuitem">
						<a class="task" href="#${item.id}">${item.caption}</a>
					</td>
				</tr>
			</c:forEach>
			<tr>
				<td class="menudivider">&nbsp;</td>
			</tr>
		
			<tr>
				<td	class="menutitle">Classes</td>
			</tr>
			<c:forEach items="${OPEN_CLASSES}" var="item">
				<tr>
					<td class="menuitem" id="${item.objid == param['classid'] ? 'selected' : 'unselected'}">
						<a href="?classid=${item.objid}#bulletin:bulletin">
							${item.name} ${item.objid == param['classid'] ? ' *' : ''}
						</a>
					</td>	
				</tr>
			</c:forEach>

			<c:if test="${SESSION_INFO.usertype=='teacher'}">
				<tr>
					<td>
						<button><a context="session" name="createClass" class="addclass">Add New Class</a></button>
					</td>
				</tr>
			</c:if>
			
			<tr>
				<td class="menudivider">&nbsp;</td>
			</tr>

			

			<c:if test="${SESSION_INFO.usertype=='student'}">	
				<tr>
					<td class="menutitle">Teacher</td>
				</tr>
				<tr>
					<td valign=center">
						<label context="session">
							<img src="img/#{teacher.status}.png"/>
							<a class="people">#{teacher.lastname},#{teacher.firstname}</a>
						</label>
					</td>
				</tr>
				<tr>
					<td class="menudivider">&nbsp;</td>
				</tr>
			</c:if>
			
			<c:if test="${SESSION_INFO.usertype=='student' or SESSION_INFO.usertype=='teacher'}">	
				<tr>
					<td class="menutitle">${SESSION_INFO.usertype=='teacher' ? 'Students' : 'Classmates'}</td>
				</tr>
				<tr>
					<td valign="top" style="padding-top:5px;">
						<table context="session" items="studentList" width="100%" cellpadding="0" cellspacing="0" varName="item">
							<tbody>
								<tr>
									<td valign="top">
										<img src="img/#{item.status}.png"/>
										<a class="people" href="#student:student_info?objid=#{item.objid}" class="menuitem">
											#{item.lastname}, #{item.firstname}
										</a>
									</td>
								</tr>
							</tbody>
						</table>	
					</td>
				</tr>
			</c:if>
		</table>
		
	</jsp:attribute>

	
</t:secured>