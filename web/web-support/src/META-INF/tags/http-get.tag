<%@ tag import="com.rameses.server.common.*" %>
<%@ tag import="com.rameses.http.*" %>
<%@ tag import="java.util.*"%>


<%@ attribute name="host"%>
<%@ attribute name="page"%>
<%@ attribute name="var"%>
<%@ attribute name="json"%>
<%@ attribute name="params" rtexprvalue="true" type="java.lang.Object" required="false"%>
<%@ attribute name="debug"%>

<%
	
	try {
		if( host == null )
			throw new Exception( "Please provide a host for http-get");
		if( page == null )
			throw new Exception( "Please provide a page for http-get");
		Object o = null;		
		HttpClient hc = new HttpClient( host );
		
		if(params==null) {
			o = hc.get( page ); 
		}
		else if( params instanceof javax.servlet.http.HttpServletRequest) {
			javax.servlet.http.HttpServletRequest req = (javax.servlet.http.HttpServletRequest)params;
			Map m = new HashMap();
			Enumeration e = req.getParameterNames();
			while(e.hasMoreElements() ) {
				String n = (String)e.nextElement();
				m.put(n, req.getParameter(n) );
			}
			o = hc.get(page, m);
		}
		else if( params instanceof Map ) {
			o = hc.get(page, (Map)params);
		}
		else {
			o = hc.get( page ); 
		}
		
		if( json!=null && o!=null ) {
			o = JsonUtil.toString(o);	
			if( var == null ) {
					out.write( (String) o );
			}	
		}
		if(var!=null) {
				request.setAttribute(var, o);
		}
		else if ( o!=null ){
			out.write( o.toString() );
		}
	}
	catch(Exception e) {
		if("true".equals(debug)) e.printStackTrace();
		
		String errorMessage = "";
		e = com.rameses.util.ExceptionManager.getOriginal(e);
		if( e != null ) errorMessage = e.getMessage();
		
		request.setAttribute("error", errorMessage );
	} 	
%>

