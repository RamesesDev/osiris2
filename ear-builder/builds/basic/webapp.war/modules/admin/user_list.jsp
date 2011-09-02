<%@ taglib tagdir="/WEB-INF/tags/templates" prefix="t" %>

<script>
	$put("user_list", 
		new function() {
			var svc = ProxyService.lookup("UserAdminService");
			
			this.search;
			var self = this;
			this._controller;
			
			this.showSearch = function() {
				this.listModel.load();
			}
			
			this.listModel = {
				rows: 20,
				fetchList: function(o) {
					o.search = self.search;
					return svc.getList( o );
				}
			}
		
			this.open = function() {
				if( !this.listModel.getSelectedItem() ) return;
				var id = this.listModel.getSelectedItem().objid;
				return new DocOpener("admin:user", {objid:id} );
			}
			
			this.create = function() {
				var saveHandler = function(o) {
					o = svc.create( o );
					self._controller.navigate( new DocOpener("admin:user", {objid: o.objid}) );
				}
				return new PopupOpener("admin:user_new", {saveHandler: saveHandler} );
			}
		}
	);
</script>


<t:content title="Users">
	<jsp:attribute name="actions">
		Search <input context="user_list" type="text" name="search" />
		<input type="button" value="Go" context="user_list" name="showSearch"/>
		<input type="button" value="Add New +" context="user_list" name="create"/>
	</jsp:attribute>
	<jsp:body>
		<table context="user_list" model="listModel" cellpadding="0" cellspacing="0" varName="item" varStatus="stat" width="100%">
			<thead>
				<tr>
					<td class="list-column-first">User Name</td>
					<td class="list-column">Last Name</td>
					<td class="list-column">First Name</td>
				</tr>
			</thead>
			<tbody>
				<tr class="#{stat.index % 2 == 0? 'list-row-even' : 'list-row-odd' }" context="user_list" action="open">
					<td>#{item.username}</td>
					<td>#{item.lastname}</td>
					<td>#{item.firstname}</td>
				</tr>
			</tbody>
			<tfoot>
				<tr>
					<td colspan="3">
						<input type="button" context="user_list" name="listModel.movePrev" value="&lt;&lt;"/>
						<input type="button" context="user_list" name="listModel.moveNext" value="&gt;&gt;"/>
					</td>
				</tr>
			</tfoot>
		</table>
	</jsp:body>
</t:content>
