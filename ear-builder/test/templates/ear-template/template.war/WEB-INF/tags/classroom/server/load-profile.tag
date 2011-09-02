<%@ tag import="javax.servlet.http.Cookie"%>
<%@ tag import="com.rameses.invoker.client.DynamicHttpInvoker" %>
<%@ tag import="java.util.*" %>

<%
	System.out.println("entering session info");
	//retrieves the users information
	Map user = (Map)request.getAttribute( "SESSION_INFO" );
	System.out.println("print session info ->" + user.get("classes"));
	if( user != null ) {
		String host = application.getInitParameter("app.host");
		String appcontext = application.getInitParameter("app.context");
		DynamicHttpInvoker dh = new DynamicHttpInvoker(host, appcontext);
	
		Map env = user;
		DynamicHttpInvoker.Action ac = dh.create("ClassMembershipService", env);
		Map activeClass = (Map)ac.invoke("getActiveClass", new Object[] {user.get("userid")} );
		
		request.setAttribute("ACTIVE_CLASS", "No Class Selected");
		request.setAttribute("CLASSES", (List)ac.invoke("getClasses", new Object[] {user.get("userid")} ) );
		
		List list = new ArrayList();
		list.add( user.get("usertype") );
		request.setAttribute( "PERMISSIONS", list );
	}
	
	
	
%>



