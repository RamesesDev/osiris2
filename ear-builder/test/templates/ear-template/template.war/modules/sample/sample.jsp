
<script>
	$put( "sample",
		new function() {
			this.open = function() {
				return new DocOpener( "sample:sample1", {id:"1234", month:"January" } );
			}
			this.open1 = function() {
				return new DocOpener( "sample:sample1", {id:"4321", month: "February"} );
			}
		}
	);
</script>


Sample 1Test
<input type="button" context="sample" name="open" value="Open 1" />
<input type="button" context="sample" name="open1" value="Open 2" />








