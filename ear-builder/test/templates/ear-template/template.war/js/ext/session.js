$put("session",
	new function() {
		
		this.sessionid; 
		
		this.onload = function() {
			/*
			this.sessionid = $.cookie( "sessionid" );
			if(this.sessionid==null) {
		    	window.location = "index.jsp";
		    }
			
			//store in env.
			Env.data.sessionid = this.sessionid;
			
			updateSession();
			setInterval( updateSession, 1000 * 60);
			this.notifier = new Notification(this.sessionid);
			this.notifier.start();
			*/
		}
		
		var _self = this;
		
		function updateSession() {
			var date = new Date();
			var exp = date.getTime() + (60 * 60 * 1000);
			date.setTime(exp);
			$.cookie("sessionid", _self.sessionid, { expires: date });
		}
		
		this.logout = function() {
			var svc = ProxyService.lookup('LogoutService');
			svc.logout( {sessionid: this.sessionid}  ); 
			$.cookie( "sessionid", null );
			window.location = "logout.jsp";
		}
		
		this.showProfileMenu = function() {
			var popup = new DropdownOpener( 'useraccount:useraccount_menu' );
			popup.options.position = {my: 'right top', at: 'right bottom'};
			return popup;
		}
		
		this.editProfile = function() {
			window.location.hash = "useraccount:user_profile";
			window.location.reload(true);
			return "_close";
			//return new DocOpener( "useraccount:user_profile" );
		}
		
	}
);

