<%@ taglib tagdir="/WEB-INF/tags/templates" prefix="t" %>

<script>
	$put("user",
	   new function() {
			var svc = ProxyService.lookup("UserAdminService");
			this.entity;
			this.objid;
			this.selectedRole;
			this._controller;
			var self = this;
			
			this.onload = function() {
				this.entity = svc.read( {objid: this.objid} );
			}
			
			this.editInfo = function() {
				var saveHandler = function(o) {
					o = svc.update( o );
					self._controller.refresh();
				}
				return new PopupOpener("admin:user_edit", {saveHandler: saveHandler, entity: this.entity } );
			}
			
			this.createLogin = function() {
				var saveHandler = function(o) {
					o.objid = self.entity.objid;
					self.entity.loginaccount = svc.createLogin(o);
					self._controller.refresh();
				}
				return new PopupOpener("admin:loginaccount", {saveHandler: saveHandler, entity: {uid: this.entity.username }});
			}
			
			this.editLoginAcct = function() {
				var saveHandler = function(o) {
					svc.updateLogin(o);
					self.entity.loginaccount = o;
					self._controller.refresh();
				}
				return new PopupOpener("admin:loginaccount", {saveHandler: saveHandler, entity: this.entity.loginaccount });
			}
			
			this.removeLoginAcct = function() {
				if(confirm("Are you sure you want to remove the login account? This will disable access for this user")) {
					svc.removeLogin( this.entity.loginaccount );
					this.entity.loginaccount = null;
				}
			}

			this.addRole = function() {
				var saveHandler = function(o) {
					o.userid =  self.entity.objid;
					o.disallowed = [];
					o = svc.addUserRole( o );
					self.entity.roles.push(o);
					self._controller.refresh();
				}
				return new PopupOpener("admin:role_new", {saveHandler: saveHandler});
			}
			
			this.editPermissions = function() {
				if(this.selectedRole) {
					var role = this.selectedRole;
					var saveHandler = function(o) {
						role.disallowed = o;
						svc.updateUserRole(role);
					}
					return new PopupOpener("admin:permission_edit", {saveHandler: saveHandler, role: role });
				}	
			}
			
			this.removeRole = function() {
				if(this.selectedRole) {
					if( confirm("Are you sure to want to remove this role?") ) {
						var r = this.selectedRole;	
						svc.removeUserRole(r);
						this.entity.roles.removeAll( function(o) { return o.role == r.role && o.usergroup == r.usergroup } );	
					}	
				}
			}
	   }
	);   
</script>

<a context="user" name="_close" style="font-size:9px;">&lt;&lt;Back To List</a> 

<t:content title="User 2">
	<h2>Personal Information</h2>
	<label context="user">
		<table>
			<tr>
				<td>User Name :</td>
				<td><label context="user">#{entity.username}</label></td>
			</tr>
			<tr>
				<td>First Name : </td>
				<td><label context="user">#{entity.firstname}</label></td>
			</tr>
			<tr>
				<td>Last Name : </td>
				<td><label context="user">#{entity.lastname}</label></td>
			</tr>
		</table>	
	</label>
	<a context="user" name="editInfo">Edit</a>	
	
	<h2>Login Account</h2>
	<div context="user" visibleWhen="#{entity.loginaccount==null}">
		<i>There is no login account defined yet.</i> <a context="user" name="createLogin">Create a login account?</a>
	</div>
	<div context="user" visibleWhen="#{entity.loginaccount!=null}">
		<table>
			<tr>
				<td>Login ID:</td>
				<td><label context="user">#{entity.loginaccount.uid}</label> 
			</tr>
			<tr>
				<td>
					<a context="user" name="editLoginAcct">Edit</a>
					&nbsp;&nbsp;&nbsp;
					<a context="user" name="removeLoginAcct">Remove</a>
				</td>
			</tr>
		</table>
	</div>

	<h2>Roles</h2>
	<table class="list" cellpadding="0" cellspacing="0" context="user" name="selectedRole" items="entity.roles" varName="item">
		<thead>
			<tr>
				<td class="list-column-first" width="200">Role</td>
				<td class="list-column" width="200">User Group</td>
				<td class="list-column" width="50">&nbsp;</td>
				<td class="list-column" width="50">&nbsp;</td>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td>#{item.role}</td>
				<td>#{item.usergroup}</td>
				<td><a context="user" name="editPermissions">Edit Permissions</a></td>
				<td><a context="user" name="removeRole">Remove Role</a></td>
			</tr>
		</tbody>
	</table>
	<div context="user" visibleWhen="#{entity.roles.length == 0}">
		<i>There are no roles defined yet</i>
	</div>
	<br/>
	<a context="user" name="addRole">Add Role</a>	
</t:content>




