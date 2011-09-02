xxx
id is ${param['id']}<br>
<h2>Month of ${param['month']}</h2>
<script>
	$put( "sample1",
		new function() {
			this.id;
			this.month;
			
			this.save = function() {
				
			}	
			
			this.changeMonth = function() {
				Hash.reload( {id: this.id, month: this.month } );
			}
			
			this.months = [
				"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
			]
		}
	);
</script>

<br>
Sample 3
<h1>OK</h1>
<input type="button" value="Save" context="sample1" name="save"/>
<input type="button" value="Back" context="sample1" name="_close"/>
<br>
<select context="sample1" name="month" items="months"></select> 
<input type="button" value="Change Month" context="sample1" name="changeMonth"/>







