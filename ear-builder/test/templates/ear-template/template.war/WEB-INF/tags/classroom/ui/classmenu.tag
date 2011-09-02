<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<style>
	.classes {
		font-size: 10px;
	}
	.classes td{
		padding:1px;
	}	
	.classes #menuitem {
		padding:1px;
		padding-left: 10px;
		font-size:11px;
	}
	.selectedclass {
		color:red;font-weight:bold;
	}
</style>

<div style="color:gray;font-weight:bolder;padding-top:5px;padding-bottom:10px;font-size:11px;border-top:1px solid lightgrey">
ACTIVE CLASSES
</div>
<div class="classes">
	<c:if test="${! empty USERPROFILE.classes}">	
		<table context="classes" items="list" varName="item" name="selectedClass" class="classes" width="100%" cellpadding="0" cellspacing="0">
			<tbody>
				<tr onclick="$ctx('classes').changeClass()">
					<td id="menuitem" class="#{item.selected == 'true'? 'selectedclass' : ''}" title="#{item.classurl}">#{item.name}</td>
				</tr>
			</tbody>
		</table>
	</c:if>
	<c:if test="${empty USERPROFILE.classes}">	
		<i>No active classes</i>
	</c:if>
</div>

<c:if test="${USERPROFILE.usertype=='teacher'}">
	<div style="padding-top:5px;font-size:10px;">
		<a context="classes" name="createClass">+Add New</a>
	</div>	
</c:if>

<div style="padding-bottom:5px">&nbsp;</div>

<c:if test="${USERPROFILE.usertype=='teacher'}">
	<style>
		.class_students {
			font-size: 11px;
		}
		.class_students #menuitem {
			padding:1px;
		}
		.class_students #menuitem a{
			color: blue;
		}
	</style>
	<div style="color:gray;font-weight:bolder;padding-top:5px;padding-bottom:10px;font-size:11px;border-top:1px solid lightgrey">
	STUDENTS
	</div>
	<div>
		<table context="classroom" items="students" varName="item" name="selectedStudent" class="class_students" width="100%" cellpadding="0" cellspacing="0">
			<tbody>
				<tr onclick="$ctx('classroom').viewStudent()">
					<td width="20"><img src="img/#{item.status}.png"/></td>
					<td>#{item.lastname}, #{item.firstname}</td>
				</tr>
			</tbody>	
		</table>
	</div>
</c:if>	

<c:if test="${USERPROFILE.usertype=='student'}">
	<style>
		.teacher {
			font-size: 11px;
		}
		.teacher #menuitem {
			padding:1px;
			
		}
		
	</style>
	<div style="padding-top:10px;border-top:1px solid lightgrey;">
		<table class="teacher" width="100%" cellpadding="0" cellspacing="0">
				<tr>
					<td colspan="2" valign="top" style="color:gray;font-weight:bolder;padding-top:5px;padding-bottom:10px;font-size:11px;">TEACHER</td>
				</tr>
				<tr onclick="$ctx('classroom').viewTeacher()">
					<td id="menuitem">
						<label context="classroom">
							<img src="img/#{teacher.status}.png"/>
						</label>
					</td>
					<td id="menuitem">
						<label context="classroom">
							#{teacher.lastname},#{teacher.firstname}
						</label>	
					</td>	
				</tr>
		</table>
	</div>
</c:if>
