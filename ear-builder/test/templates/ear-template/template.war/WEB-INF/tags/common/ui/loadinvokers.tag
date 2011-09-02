<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="common" %>
<%@ tag import="com.rameses.web.support.*" %>
<%@ tag import="java.util.*" %>

<%
	
	List perms = (List) request.getAttribute("PERMISSIONS");
	if(perms==null) perms = new ArrayList();

	List elements = ModuleUtil.getEntries( application, "invokers" );
	List newList = new ArrayList();
	Iterator iter = elements.iterator();
	while( iter.hasNext() ) {
		boolean isPermitted = true;
		Map o = (Map) iter.next();
		Object perm = o.get("permission");
		if( perm!=null) {
			isPermitted = ( perms.indexOf( perm)>=0 ); 
		}
		if(isPermitted) {
			Map m = new HashMap();
			if(o.get("id")!=null ) {
				m.putAll( o );
				String modname = (String) o.get("_name" );
				m.remove("_parent");
				
				if( m.get("page")==null ) {
					m.put( "page", o.get("id") + ".jsp" );
				}
				m.put( "page", "modules/" + modname + "/" + m.get("page" ) );
				m.put( "id", modname + ":" + m.get("id") );
				if( m.get("parent")!=null) {
					m.put( "parent", modname + ":" + m.get("parent") );
				}
				if( m.get("context")==null) {
					m.put( "context", o.get("id") );
				}
				
				m.remove("_name");
				newList.add(m);
			}
		}	
	}
	request.setAttribute("TASKLIST", newList );
%>
<script>
	<c:forEach items="${TASKLIST}" var="item">
		<c:set var="item" scope="request" value="${item}"/>
		Registry.add( <%=JsonUtil.toString( request.getAttribute("item"))%>  );
	</c:forEach>
</script>


