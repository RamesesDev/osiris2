<%@ tag import="javax.servlet.http.Cookie"%>
<%@ tag import="com.rameses.invoker.client.DynamicHttpInvoker" %>
<%@ tag import="java.util.*" %>

<%
	//retrieves the users information
	Map user = (Map)request.getAttribute( "SESSION_INFO" );
	
	//if session is in cookie, must check cache in database or memcache
	if( user != null ) {
		String host = application.getInitParameter("app.host");
		String appcontext = application.getInitParameter("app.context");
		DynamicHttpInvoker dh = new DynamicHttpInvoker(host, appcontext);
		String usertype = (String)user.get("usertype");
		String classId = request.getParameter("classid");
		
		if( usertype != null ) {
			String action = null;
			if(usertype.equals("parent")) action = "ParentSessionService";
			else if(usertype.equals("student")) action = "StudentSessionService";
			else action = "TeacherSessionService";

			Map env = user;
			DynamicHttpInvoker.Action ac = dh.create(action, env);

			Map params = new HashMap();
			params.put("objid", user.get("objid"));
			params.put("classid", classId );
			Map info = (Map)ac.invoke("getInfo", new Object[] {params} );
			request.setAttribute( "USERPROFILE", info );
		}
		
		List list = new ArrayList();
		list.add( usertype );
		request.setAttribute( "PERMISSIONS", list );
	}
	
	
	
%>



