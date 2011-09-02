$put("session",
	new function() {
		
		this.sessionid; 
		
		this.onload = function() {
			this.sessionid = $.cookie( "sessionid" );
			if(this.sessionid==null) {
		    	window.location = "index.jsp";
		    }
			updateSession();
			setInterval( updateSession, 1000 * 60);
		}
		
		var _self = this;
		
		function updateSession() {
			var date = new Date();
			var exp = date.getTime() + (60 * 60 * 1000);
			date.setTime(exp);
			$.cookie("sessionid", _self.sessionid, { expires: date });
		}
		
		this.logout = function() {
			var svc = ProxyService.lookup('SessionService');
			svc.logout( {sessionid: this.sessionid}  ); 
			$.cookie( "sessionid", null );
			window.location = "logout.jsp";
		}
		
		this.showProfileMenu = function() {
			var popup = new DropdownOpener( 'useraccount:useraccount_menu' );
			popup.options.position = {my: 'right top', at: 'right bottom'};
			return popup;
		}
	}
);

