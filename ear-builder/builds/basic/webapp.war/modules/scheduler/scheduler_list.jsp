<%@ taglib tagdir="/WEB-INF/tags/templates" prefix="t" %>

<script type="text/javascript">
	$put(
		"scheduler_list",

		new function() {
			var svc = ProxyService.lookup("TaskScheduler");
			this.list = [];
			this.query = {}
			this.state;
			this.self = this;
			
			this.listModel = {
				rows: 5,
				fetchList : function(o) {
					return svc.getList( {} );
				}
			}

			this.open = function() {
				var e = this.listModel.getSelectedItem();
				return new DocOpener( "scheduler:task" );
			}
			
			this.create = function() {
				var p = new PopupOpener( "scheduler:task_new" ); 
				p.options.width = 500;
				p.options.height = 400;
				return p;
			}
			
		}
	);
</script>

<t:content title="Scheduled Tasks">

	<jsp:attribute name="actions">
		<input type="button" value="Add New" context="scheduler_list" name="create" />
	</jsp:attribute>
	
	<jsp:body>
		<table class="list" context="scheduler_list" model="listModel" varName="item" varStatus="status" width="100%" cellpadding="0" cellspacing="0">
			<thead>
				<tr>
					<td class="list-column-first" align="left">Id</td>
					<td class="list-column" align="left">Script Name</td>
					<td class="list-column" align="center">Method</td>
					<td class="list-column">Start Date</td>
					<td class="list-column">End Date</td>
					<td class="list-column">Duration</td>
					<td class="list-column">State</td>
				</tr>
			</thead>
			<tbody>
				<tr context="scheduler_list" action="open">
					<td class="list-row-column-first" align="left">#{item.taskid}</td>
					<td class="list-row-column" align="left">#{item.scriptname}</td>
					<td class="list-row-column" align="left">#{item.method}</td>
					<td class="list-row-column" align="left">#{item.startdate}</td>
					<td class="list-row-column" align="left">#{item.enddate}</td>
					<td class="list-row-column" align="left">#{item.duration}</td>
					<td class="list-row-column" align="left">#{item.state}</td>
				</tr>                 
			</tbody>
		</table>
	</jsp:body>
</t:content>

