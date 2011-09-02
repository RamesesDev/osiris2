<%@ taglib tagdir="/WEB-INF/tags/templates" prefix="t" %>

<script>
	$put("role_new",
	   new function() {
			var svc = ProxyService.lookup("RoleAdminService");
	   
			this.entity = {disallowed:[]};
			this.saveHandler;
			this.roles;
			this.selectedRole;
			
			this.onload = function() {
				this.roles = svc.getRoleList({});
			}
			
			this.add = function() {
				this.entity.role = this.selectedRole.role;
				this.saveHandler(this.entity);
				return "_close";
			}

			this.groups = function() {
				return ["B1", "B2"];
			}
	   }
	);   
</script>

<t:popup>
	<jsp:attribute name="leftactions">
		<input type="button" context="role_new" name="add" value="Save"/>
	</jsp:attribute>
	<jsp:body>
		<table>
			<tr>
				<td>Select a Role</td>
				<td><select context="role_new" items="roles" itemLabel="role" name="selectedRole"></select></td>
			</tr>
			<tr>
				<td>Select a Group</td>
				<td><select context="role_new" name="entity.usergroup" items="groups()" depends="entity.role"></select></td>
			</tr>
		</table>
	</jsp:body>	
</t:popup>



