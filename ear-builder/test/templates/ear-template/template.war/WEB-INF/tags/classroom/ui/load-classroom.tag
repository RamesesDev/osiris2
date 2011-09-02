<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ tag import="com.rameses.web.support.*" %>
<%@ tag import="java.util.*" %>

<!-- this script holds every data you need regarding the classroom -->
<script>

	$put("user_profile", 
		new function() {
			this.userid = "${SESSION_INFO.userid}";
			this.name="${USERPROFILE.info.firstname}";
			this.firstname="${USERPROFILE.info.firstname}";
			this.lastname="${USERPROFILE.info.lastname}";
		}
	);

	$put(
		"classes",
		new function() {
			this.list = []
			var p = null;
			<c:forEach items="${USERPROFILE.classes}" var="item">
				<c:set var="item" scope="request" value="${item}"/>
				p = <%=JsonUtil.toString( request.getAttribute("item"))%>;
				<c:if test="${! empty USERPROFILE.classinfo && item.objid == USERPROFILE.classinfo.objid}">p.selected='true';</c:if>
				this.list.push( p );		
			</c:forEach>
			
			this.selectedClass;
			this.changeClass = function() {
				WindowUtil.load("home.jsp", {classid: this.selectedClass.objid}, "bulletin:bulletin");
			}
			
			this.createClass = function() {
				var handler = function() {
					WindowUtil.reload();
				}
				return new PopupOpener("class:class_new", {handler: handler} );
			}
		}
	);
	
	
	$put( 
		"classroom",
		new function() {
			
		<c:if test="${! empty USERPROFILE.classinfo}">
			<c:set var="item" scope="request" value="${USERPROFILE.classinfo}"/>
			this.info = <%=JsonUtil.toString( request.getAttribute("item"))%>;
			this.classid = "${USERPROFILE.classinfo.objid}";
		</c:if>
		<c:if test="${empty USERPROFILE.classinfo}">
			this.info = null;
		</c:if>
		
		<c:if test="${! empty USERPROFILE.teacher}">
			<c:set var="item" scope="request" value="${USERPROFILE.teacher}"/>
			this.teacher = <%=JsonUtil.toString( request.getAttribute("item"))%>;
			this.viewTeacher = function() {
				var bookmark = $ctx('module_manager').bookmark;
				bookmark.invoke('teacher:teacher_info', {objid: this.teacher.objid });
			}
		</c:if>
			
		var self = this;
		this._controller;

			this.students = [];
			<c:forEach items="${USERPROFILE.students}" var="item">
			<c:set var="item" scope="request" value="${item}"/>
			this.students.push( <%=JsonUtil.toString( request.getAttribute("item"))%> );
			</c:forEach>

			this.selectedStudent;	
			this.viewStudent = function() {
				var bookmark = $ctx('module_manager').bookmark;
				bookmark.invoke('student:student_info', {objid: this.selectedStudent.objid});
			}	
			this.onload = function() {
				$ctx("session").notifier.handlers.classroom =  function(o) {
					if(o.msgtype == 'joinclass' ) {
						var svc = ProxyService.lookup("StudentService");
						var s = svc.getInfo( {objid:o.studentid} );
						var eo = self.students.find( function(x) { return x.objid == s.objid }  );
						if(eo!=null) {
							eo.status = "online";
						}
						else {
							self.students.push( s );
						}
					}
					else if(o.msgtype == 'leaveclass' ) {
						var eo = self.students.find( function(x) { return x.objid == o.studentid }  );
						if(eo!=null) {
							eo.status = "offline";
						}
					}
					self._controller.refresh();
				};			
			}

		
		}	
	);
</script>