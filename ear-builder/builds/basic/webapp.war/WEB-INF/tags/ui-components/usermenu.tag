<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="common" %>
<%@ tag import="com.rameses.web.support.*" %>
<%@ tag import="java.util.*" %>

<%@ attribute name="target" rtexprvalue="true"%>

<style>
	.usermenu tr.unselected td { font-size: 12px; font-family:arial; padding:4px;}
	.usermenu tr.selected { background: lightgrey;  }
	.usermenu tr.selected td { font-size: 12px; font-family:arial; color: black; padding:4px; font-weight:bolder; }
</style>
<script type="text/javascript">
	$put("module_manager", 
		new function() {
			var self = this;
			this.bookmark = new Bookmarker("${target}");
			this.selected;
			
			this.onload = function() {
				this.bookmark.updateHandler = function( inv ) {
					if( inv.type == 'usermenu' ) {
						self.selected = inv;
					}
					else if ( inv.parent ) {
						self.selected = InvokerUtil.find( inv.parent );
					}
					
					if( self.menuModel.refresh )
						self.menuModel.refresh(); 
				}
				this.bookmark.load( 'usermenu' );
			}
			
			this.menuModel = {
				fetchList: function(o) {
					return InvokerUtil.lookup( "usermenu" );
				}
			}
			
			this.navigate = function() {
				this.bookmark.invokeSelected( this.selected );
			}

		}	
	);
</script>

<table width="100%" class="usermenu" context="module_manager" name="selected" model="menuModel" cellpadding="0" cellspacing="0" style="padding-top:5px;" varName="item">
	<tbody>
		<tr class="unselected" onclick="$ctx('module_manager').navigate();">
			<td>#{item.caption}</td>
		</tr>
	</tbody>
</table>


