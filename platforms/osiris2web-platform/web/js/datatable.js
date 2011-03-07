/**
 * @author jaycverg
 */

(function() {
    
    $.fn.datatable = function( options ) {
        this.each(function(){ addTable(this); }};
    };

    function addTable( target ) {
        var $target = $(target);
        $target.html('<table></table>');
    }

})();