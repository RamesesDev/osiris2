<%@ taglib tagdir="/WEB-INF/tags/templates" prefix="t" %>

<script>
	$put("loginaccount",
	   new function() {
			this.entity;
			this.saveHandler;
			this.confirmPwd;
			this.save = function() {
				if(this.confirmPwd != this.entity.password) {
					alert( "Password must be the same confirm password" );
					return;
				}	
				this.saveHandler(this.entity);
				return "_close";
			}
	   }
	);   
</script>

<t:popup>
	<jsp:attribute name="leftactions">
		<input type="button" context="loginaccount" name="save" value="Save"/>
	</jsp:attribute>
	<jsp:body>
		<table>
			<tr>
				<td>User ID :</td>
				<td><input type="text" context="loginaccount" name="entity.uid" required="true" caption="User ID"/></td>
			</tr>
			<tr>
				<td>Password :</td>
				<td><input type="password" context="loginaccount" name="entity.password"  required="true" caption="Password"/></td>
			</tr>
			<tr>
				<td>Confirm Password :</td>
				<td><input type="password" context="loginaccount" name="confirmPwd"  required="true" caption="Confirm Password"/></td>
			</tr>
		</table>
	</jsp:body>	
</t:popup>




