<%@ tag import="org.apache.tools.ant.*" %>
<%@ tag import="java.io.*" %>
<%@ tag import="java.util.*" %>


<%@ attribute name="filename" %>
<%@ attribute name="params" rtexprvalue="true" type="java.lang.Object" %>

<%
	try {
		File build = new File(filename);
		Project p = new Project();
		p.setUserProperty( "ant.file", build.getAbsolutePath() );
		
		if( params != null ) {
			if( params instanceof javax.servlet.http.HttpServletRequest) {
				javax.servlet.http.HttpServletRequest req = (javax.servlet.http.HttpServletRequest)params;
				Enumeration e = req.getParameterNames();
				while(e.hasMoreElements() ) {
					String n = (String)e.nextElement();
					p.setUserProperty( n, req.getParameter(n) );
				}
			}
			else if( params instanceof Map ) {
				Map map = (Map)params;
				Iterator iter = map.entrySet().iterator();
				while( iter.hasNext() ) {
					Map.Entry me = (Map.Entry)iter.next();
					p.setUserProperty( me.getKey()+"", me.getValue()+"" );
				}
			}
		}
		
		//so we can monitor logs in the console
		DefaultLogger logger = new DefaultLogger();
		logger.setOutputPrintStream( System.out );
		logger.setErrorPrintStream(System.err);
		logger.setMessageOutputLevel(Project.MSG_INFO);
		p.addBuildListener(logger);
		
		try {
			p.fireBuildStarted();
			p.init();
			ProjectHelper helper = ProjectHelper.getProjectHelper();
			p.addReference("ant.projectHelper", helper);
			helper.parse(p, build);
			p.executeTarget(p.getDefaultTarget());
			p.fireBuildFinished(null);
		} catch (BuildException e) {
			e.printStackTrace();
			p.fireBuildFinished(e);
			request.setAttribute( "error", "Failed to install the server. Error Message: " + e.getMessage() );
		}
	}
	catch(Exception e) {
		e.printStackTrace();
		throw e;
	}
%>

