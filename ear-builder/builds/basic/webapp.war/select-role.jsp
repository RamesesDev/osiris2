<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags/ui-components" prefix="ui" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="common" %>
<%@ taglib tagdir="/WEB-INF/tags/templates" prefix="t" %>


<c:set var="classid" value="${param['classid']}"/>
<script>
	$put("classManager",
		new function() {
			this.lookupClass = function() {
				return new DropdownOpener($('#select-school-class'));
			}
			this.changeClass = function(o) {
				if (o == null ) {
					WindowUtil.reload();					
				}
				else {
					WindowUtil.reload({classid: o });
				}	
			}
		}
	);	
</script>


<div id="select-school-class" style="display:none;">
	<table cellpadding="0" cellspacing="0" style="font-size:12px;">
		<c:forEach items="${USER.classes}" var="item">
			<c:if test="${item.objid == classid }">
				<c:set var="selectedClass" value="${item}" scope="page" />
			</c:if>
			<c:if test="${item.objid != classid}">
				<tr>
					<td>
						<a href="#" onclick="$ctx('classManager').changeClass('${item.objid}');">${item.name} ${item.school}</a>
					</td> 
				</tr>
			</c:if>
		</c:forEach>
		<c:if test="${! empty classid}">
			<tr>
				<td style="padding-top:5px;">
					<a href="#" onclick="$ctx('classManager').changeClass(null);">
						Back to Home
					</a>	
				</td>
			</tr>	
		</c:if>	
	</table>
</div>

 
<c:if test="${! empty pageScope.selectedClass}">
	<b>Class</b>: ${pageScope.selectedClass.name} 
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>School:</b> ${pageScope.selectedClass.school}
	<input class="button3" type="button" value="Change Class" context="classManager" name="lookupClass" />
</c:if>

<c:if test="${empty pageScope.selectedClass}">
	<input class="button3" type="button" value="Go to Class" context="classManager" name="lookupClass" />
</c:if>


