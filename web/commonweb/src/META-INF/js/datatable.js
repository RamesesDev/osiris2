/**
 * @author jaycverg
 */

(function() {
    
    window.DataTable = function( target, options ) {
        
        //instance field and methods
        this.selected = null;
        this.refresh = refresh;
        
        var table = $('<table class="grid"></table>');
        var tableRows;
        var columns = [];
        var buttons;
        
        var dataList = [];
        var start = 0;
        var rows = 10;
        
        //custom handlers
        var fetch = function() {};
        var onOpenItem = function() {};
        var onSelect = function() {};
        
        if( options.properties ) {
            for( var i in options.properties ) table.attr(i, options.properties[i]);    
        }
        
        //set passed parameters
        if( options.rows )      rows = options.rows;
        if( options.columns )   columns = options.columns;
        if( options.fetchList ) fetch = options.fetchList;
        if( options.onOpenItem )onOpenItem = options.onOpenItem;
        if( options.onSelect )  onSelect = options.onSelect;
        
        //initialize on dom ready
        $(function() {
            //build table rows
            init();
            
            //fetch items
            doFetchItems();
        });
        
        //-- hellper mehtods
        function init() {
            var tr = $('<tr></tr>');
            var cols = options.columns? options.columns : [];
            for( var i=0; i < cols.length; ++i ) {
                var c = cols[i];
                var caption = c.caption? c.caption : c.name;
                tr.append( $('<th>' + caption + '</th>') );
            }
            table.append( tr );
            
            tableRows = [];
            for( var ctr = 0; ctr < rows; ctr++ ) {
                tr = $('<tr></tr>').addClass( ctr%2==0? 'even' : 'odd' );
                for( var i=0; i < cols.length; ++i ) {
                    var c = cols[i];
                    var td = $('<td>&nbsp;</td>')
                    .data('column', i)
                    .data('row', ctr)
                    .click(onClick)
                    .dblclick(onOpen)
                    .appendTo(tr);
                    
                    if( c.align )  td.attr('align', c.align);
                    if( c.halign ) td.attr('align', c.halign);
                    if( c.valign ) td.attr('valign', c.valign);
                }
                table.append( tr );
                tableRows.push( tr );
            }
            
            //attach next/prev button
            buttons = $('<tr><td></td></tr>')
            .find('td')
            .attr('colspan', cols.length)
            .attr('align', 'right')
            .append($('<input type="button" value="Previous"/>').click( movePrev ) )
            .append($('<input type="button" value="Next"/>').click( moveNext ) )
            .append($('<input type="button" value="Refresh"/>').click( doFetchItems ) )
            .end().appendTo( table )
            .find('input');
            
            $( document.getElementById(target) ).append(table);
        }
        
        function doFetchItems() {
            //lock all buttons
            buttons.attr('disabled', 'disabled');
            if( prevSelected ) prevSelected.removeClass('selected');
            
            var param = { _start: start, _limit: rows+1 };
            fetch(param, handler);    
        }
        
        function handler( json ) {
            if( json instanceof Array )
                dataList = json;
            else
                dataList = [json];
            
            refresh();
        }
        
        function refresh() {
            //unlock all buttons
            buttons.removeAttr('disabled');
            
            if( !tableRows ) return;
            
            for(var i=0; i<tableRows.length; ++i) {
                var tr = tableRows[i];
                
                var data = null;
                if( i < dataList.length ) {
                    var data = dataList[i];
                    tr.find('td').each(function(index, td) {
                        var c = columns[index];
                        
                        var value = null;
                        if( data == null )
                            value = '&nbsp;';
                        else if( c.name )
                            value = Osiris2Utils.getProperty( data, c.name );
                        else
                            value = data;
                        
                        $(td).html( value? value : '&nbsp;' );
                    });    
                }
                else {
                    tr.find('td').html('&nbsp;');    
                }
                
            }
        }
        
        function movePrev(e) {
            if( start == 0 ) return;
            start -= rows;
            doFetchItems();
        }
        
        function moveNext(e) {
            if( dataList.length <= rows ) return;
            start += rows;
            doFetchItems();
        }
        
        //------ selection support ----
        
        var prevSelected;
        
        function onClick(e) {
            if( prevSelected ) prevSelected.removeClass('selected');
            prevSelected = $(e.target).parent('tr').addClass('selected');
            
            this.selected = dataList[ $(e.target).data('row') ];
            if( onSelect ) onSelect( this.selected );
        }
        
        function onOpen(e) {
            if( onOpenItem ) onOpenItem( this.selected );
        }
        
    }
    
})();
