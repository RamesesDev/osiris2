<%@ taglib tagdir="/WEB-INF/tags/templates" prefix="t" %>

<script>
	$put("permission_edit",
	
	   new function() {
			this.role;
			this.saveHandler;
			this.permissions;
			var svc = ProxyService.lookup("RoleAdminService");
			
			this.onload = function() {
				this.permissions = svc.getRolePermissions(this.role);
				var disallowed  = this.role.disallowed;
				if(!disallowed) disallowed = [];
				this.permissions.each( 
					function(o) {
						if( disallowed.find( function(x) { return x==o.action}  ) ) {
							o.selected = false;
						}
						else {
							o.selected = true;
						}
					}
				)
			}
			
			this.save = function() {
				var disallow = [];
				this.permissions.each( 
					function(o){ 
						if( o.selected !=true ) disallow.push(o.action);
					} 
				);
				this.saveHandler( disallow );
				return "_close";
			}
	   }
	);   
</script>

<t:popup>
	<jsp:attribute name="leftactions">
		<input type="button" context="permission_edit" name="save" value="Save"/>
	</jsp:attribute>
	
	<jsp:body>
		<label context="permission_edit">#{role.role} #{role.usergroup}</label>
		<ul context="permission_edit" items="permissions" varName="item" varStatus="stat" style="list-style:none;padding-left:0;">
			<li>
				<input type="checkbox" context="permission_edit" name="permissions[#{stat.index}].selected"/>#{item.title}
			</li>
		</ul>
	</jsp:body>	
</t:popup>



