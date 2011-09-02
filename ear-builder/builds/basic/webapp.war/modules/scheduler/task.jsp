<%@ taglib tagdir="/WEB-INF/tags/templates" prefix="t" %>

<script type="text/javascript">
	$put(
		"task",
		new function() {
			//var svc = ProxyService.lookup("UserAdminService");
			this.entity;
			this.objid;
		}
	);
</script>

<a context="task" name="_close" style="font-size:10px;">Back</a>
<t:content title="Task">
	<jsp:attribute name="actions"/>
	<jsp:body>
		<div>
			Task Id: <br>
			<label  context="task">#{entity.taskid}</label>
		</div>
		<div>
			Script Name<br>
			<label context="task">#{entity.scriptname}</label>
		</div>
		<div>
			Method<br>
			<label context="task">#{entity.method}</label>
		</div>		
	</jsp:body>
	
</t:content>
