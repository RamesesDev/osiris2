<script>
	$put(
		"useraccount_menu",
		new function() {
			
			this.logout = function() {
				$ctx('session').logout();
			}
			
		}
	);
</script>
<style>
	.useraccount_menu td {
		font-size: 10px;
	}
</style>
<table width="120" cellpadding="0" cellspacing="0" class="useraccount_menu">
	<tr>
		<td><a href="#">Edit Profile</a></td>
	</tr>
	<tr>
		<td><a href="#" context="useraccount_menu" name="logout">Logout</a></td>
	</tr>
</table>
