<%@ taglib tagdir="/WEB-INF/tags/templates" prefix="t" %>

<script type="text/javascript">
	$put(
		'chargemgmt_new',
		new function() {
			this.partner;
			//var svc = ProxyService.lookup("UserAdminService");
			
			this.entity;
			
			this.onload = function() {
				this.entity = {};
				this.entity.partner = this.partner;
				this.entity.charges = []; 
			}
			
			this.addItem = function() {
				var sz = this.entity.charges.length;
				var from = 0.0;
				if(sz > 0) { 
				   from = this.entity.charges[sz-1].to;
				   if(from==null || from == "" ) {
					   alert("Please indicate first a to value");
					   return null;
					}
				}
				this.entity.charges.push( {from: from} );
			}
			
			this.removeLastItem = function() {
				this.entity.charges.pop();
			}
			
			this.chargeTypes = [ 
				{id:"F", name:"FIXED"}, 
				{id:"p", name:"PERCENTAGE"}  
			];
		}
	);
</script>

<t:popup>
	<div>
		Partner: <br>
		<label context="chargemgmt_new">#{entity.partner.name}</label>
	</div>
	<div>
		Code<br>
		<input context="chargemgmt_new" name="entity.name"/>
	</div>
	<br>
	<b>Charges</b>
	<br>
	<table context="chargemgmt_new" items="entity.charges" varName="item" varStatus="stat" cellpadding="0" cellspacing="0" width="100%" border="1">
		<thead>
			<tr>	
				<td>From</td> 
				<td>To</td> 
				<td>Types</td> 
				<td>Charge</td> 
			</tr>
		</thead>
		<tbody>
			<tr>	
				<td>#{item.from}</td> 
				<td><input type="text" context="chargemgmt_new" name="entity.charges[#{stat.index}].to"/></td> 
				<td>
					<select context="chargemgmt_new" items="chargeTypes" itemLabel="name" itemKey="id" name="entity.charges[#{stat.index}].type"></select>
				</td>	
				<td><input type="text" context="chargemgmt_new" name="entity.charges[#{stat.index}].charge"/></td> 
			</tr>
		</tbody>
	</table>
    <a context="chargemgmt_new" name="addItem">Add Item</a>
	&nbsp;&nbsp;
	<a context="chargemgmt_new" name="removeLastItem">Remove Last Item</a>
</t:popup>


