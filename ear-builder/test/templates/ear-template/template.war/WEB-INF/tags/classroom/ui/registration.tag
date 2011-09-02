<style>
	#regtext {
		font-size: 16px; 
		font-family:arial; 
		padding-left:0; 
		color:black; 
		font-weight: bolder;
	}
	#regsubmit {
		font-size: 20px;
		font-family: arial;
		border:none;
		background-color: orange;
		color:white;
		padding: 5px;
		width: 50%;
	}
	#regform {
		background-image: url('img/pattern.jpg');
		width:350px;
		height: 390px;
		position: absolute;
		z-index: 5;
		text-align:left;
		display:none;
	}
	#regform .hint {
		font-size:16px; 
		font-family:arial;	
		color: gray;
	}
	.reginput {
		font-family: arial;
		font-size: 14px;
		width: 80%;
		height: 30px;
		text-align:left;
	}
	.regpanel {
		padding-left:40px;
	}
</style>


<script>
	$put("teacher_reg", 
		new function() {
			var svc = ProxyService.lookup("TeacherRegistrationService");
			
			this.entity = {};
			this.error;
			this.confirmpassword;
			
			this.register = function() {
				if( this.confirmpassword != this.entity.password ) {
					alert( "Your password does not seem to match the confirm password. Please check you must retype the same exact password in the confirm password box." );
					return;
				}
				
				try {
					svc.register(this.entity);
					window.location.href = "thankyou.jsp";
				}
				catch(e) {
					alert( "An error occurred when sending info to server. " + e.message );
				}
			}
		}
	);
</script>

<div id="regform" class="shadowbox">
	<div class="regpanel">
		<p id="regtext">
			If you're a teacher, instructor or somebody managing a class
			<br> <span style="font-size:18px;">Sign up. It's free.</span>
		</p>
		<input type="text" context="teacher_reg" name="entity.email" hint="Email" class="reginput" required="true" caption="Email"/>
		<br>
		<br>
		<input type="text" context="teacher_reg" name="entity.firstname" hint="Firstname" class="reginput"  required="true"  caption="Firstname"/>
		<br>
		<br>
		<input type="text" context="teacher_reg" name="entity.lastname" hint="Lastname" class="reginput"  required="true"  caption="Lastname"/>
		<br>
		<br>
		<input type="password" context="teacher_reg"  name="entity.password" hint="Password" class="reginput"  required="true"  caption="Password"/>
		<br>
		<br>
		<input type="password" context="teacher_reg" name="confirmpassword"  hint="Confirm Password" class="reginput"  required="true"  caption="Confirm Password"/>
		<br>
		<br>
		<input type="button" context="teacher_reg" name="register" value="Submit" id="regsubmit" />
	</div>
</div>

