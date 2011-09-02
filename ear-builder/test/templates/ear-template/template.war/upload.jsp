<%@ page contentType="text/plain"%>
<%@ page pageEncoding="UTF-8"%>

<%@ page import="java.util.*" %>
<%@ page import="com.rameses.invoker.client.*" %>
<%@ page import="com.rameses.web.support.*" %>
<%@ page import="com.rameses.web.fileupload.*" %>
<%@ page import="org.apache.commons.fileupload.*" %>

<%    
	System.out.println("entering");
    String filename = "";
    Map resp = new HashMap();
    if( "post".equals(request.getMethod().toLowerCase()) ) {
        FileItem fi = (FileItem) request.getAttribute("FILE");
        resp.put("objid", "FILE-" + new java.rmi.server.UID());
        resp.put("filename", fi.getName());
    }
    out.write( JsonUtil.toString( resp ) );
%>
