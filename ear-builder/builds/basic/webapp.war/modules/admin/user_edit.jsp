<%@ taglib tagdir="/WEB-INF/tags/templates" prefix="t" %>

<script>
	$put("user_edit",
	   new function() {
			this.entity;
			this.saveHandler;
			this.save = function() {
				this.saveHandler( this.entity );
				return "_close";
			}
	   }
	);   
</script>

<t:popup>
	<jsp:attribute name="leftactions">
		<input type="button" context="user_edit" name="save" value="Save"/>
	</jsp:attribute>
	<jsp:body>
		<table>
			<tr>
				<td>User Name :</td>
				<td><label context="user_edit">#{entity.username}</label>
			</tr>
			<tr>
				<td>First Name :</td>
				<td><input type="text" context="user_edit" name="entity.firstname"  required="true" caption="First Name"/>
			</tr>
			<tr>
				<td>Last Name :</td>
				<td><input type="text" context="user_edit" name="entity.lastname"  required="true" caption="Last Name"/>
			</tr>
		</table>
	</jsp:body>
</t:popup>
