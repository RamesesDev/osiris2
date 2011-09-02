//version 1.2
function TimeWeekCalendar(){
	
	var _this=this;
                     
	var schedule={};
	var container;
	var contentPane;
	var tbl="";
	//handlers
	this.onedit;
	this.onDelete = function(){};
	this.onclick;
	
	var divId="";
	var color=[
				"CCCCCC","999999","FFFFCC","FFFF33","CCFFFF",
			  	"CCFFCC","CCFF99","CCFF66","CCFF33","99FFCC",
			  	"FFCCCC","FFCC33","CCCCFF","CCCC99","99CCFF",
			  	"00CCCC","CC9933","999900","6699FF","00FF33",
			  	"009900","CC66FF","CC6666","666FFF","3366CC",
			  	"006699","9933FF","993300","3333FF","00FF00",
			  	"00AA00","007700","001100","0000FF","440000"
			  ];
	var counter = 0;
    draw();            
        
	//display the table	
	function draw(){			
		tbl = $('<table class="innertbl" border="1px"></table>')
		           .append(
		           		'<tr class="hdr" align="left">'+
							'<th class="tblhdr"></th>'+
							'<th class="tblhdr">Mon</th>'+
							'<th class="tblhdr">Tue</th>'+
							'<th class="tblhdr">Wed</th>'+
							'<th class="tblhdr">Thr</th>'+
							'<th class="tblhdr">Fri</th>'+
							'<th class="tblhdr">Sat</th>'+
							'<th class="tblhdr">Sun</th>'+				
						'</tr>'
					);
		var time = 700;
		while( time < 2200 ) {
			tbl.append(
				'<tr class="' + ( time%2==0? 'even' : 'odd' ) + '">'+	
					(time%100==0? '<th class="tblhdr time" rowspan="4">' + ( ( time+'' ).replace( /(\d+)(\d{2})$/, '$1:$2' ) ) + ( time<1200? ' am' : ' pm' ) + ' </th>' : '' )+
					'<td class="tbltd ' + ( time%100==0? 'top ' : time%100==45? 'bottom ' : '' ) + 'mon' + time + '"></td>'+
					'<td class="tbltd ' + ( time%100==0? 'top ' : time%100==45? 'bottom ' : '' ) + 'tue' + time + '"></td>'+
					'<td class="tbltd ' + ( time%100==0? 'top ' : time%100==45? 'bottom ' : '' ) + 'wed' + time + '"></td>'+
					'<td class="tbltd ' + ( time%100==0? 'top ' : time%100==45? 'bottom ' : '' ) + 'thu' + time + '"></td>'+
					'<td class="tbltd ' + ( time%100==0? 'top ' : time%100==45? 'bottom ' : '' ) + 'fri' + time + '"></td>'+
					'<td class="tbltd ' + ( time%100==0? 'top ' : time%100==45? 'bottom ' : '' ) + 'sat' + time + '"></td>'+
					'<td class="tbltd ' + ( time%100==0? 'top ' : time%100==45? 'bottom ' : '' ) + 'sun' + time + '"></td>'+
				'</tr>'
			);
			time += (time%100==45)? 55 : 15;
		}		
	}
	
	this.init = function(div){	
	        divId = div;	        
		container = $('#'+divId).append( tbl );
		contentPane = $('<div></div>').appendTo( container );			
	}

	/**
	** add
	** item -> days[mon,tue,wed,thu,fri,sat,sun],from(700,715,730),to,content (these are constant names)
	** refresh -> to redraw (true/false)
	**/      					
	this.addItem = function(item, refresh){
                var map = item; 
                var key = "";      
                if(map.schedules != undefined){
                                                
                        for(var i=0; i<map.schedules.length; i++){//looping for the schedules
                                if(key != "")
                                        key = key + "-";
                                key = key + map.schedules[i].days + map.schedules[i].from + map.schedules[i].to;
                                
                                var xy = [];
                                for(var j=0; j<map.schedules[i].days.length; j++){//looping for the days                                        
                                        var list=[];
                                        list.push(XY(map.schedules[i].days[j] + _this.parseTime(map.schedules[i].from), "x")+.12);
                                        list.push(XY(map.schedules[i].days[j] + _this.parseTime(map.schedules[i].from), "y")+.02);                                        
                                        xy.push(list);
                                }
                                map.schedules[i].xy=xy;
                                map.schedules[i].width = $('.innertbl').width()*.124;
                                map.schedules[i].height = getHeight(_this.parseTime(map.schedules[i].from), _this.parseTime(map.schedules[i].to));                                
                        }
                        
                }
                schedule[key]=map;
		if(refresh)this.load();	  			
	}
	
	//adds a list 	
	this.addList=function(list, refresh){	                       
        for(var x=0; x<list.length; x++){										
			this.addItem(list[x],false);
			
		}
                if(refresh)this.load();
	}
	
	//display
	this.load= function(){	  		
                contentPane.html( '' );		
                var x="";
                $.each(schedule, function(key){	        
                        for(var i=0; i<schedule[key].schedules.length; i++){
                                for(var j=0; j<schedule[key].schedules[i].days.length; j++){
                                        var code=
								        $(
											'<div class="schedbox" style="background-color:'+color[counter]+'; height:'+schedule[key].schedules[i].height+'px; width:'+schedule[key].schedules[i].width+'px; left:'+schedule[key].schedules[i].xy[j][0]+'; top:'+schedule[key].schedules[i].xy[j][1]+';">'+
												'<div align="right">'+
													'<span>'+
														'<a class="edit" href="#" title="Edit">'+
															'<img class="icon" src="img/edit.png" style="border:0px">'+
														'</a>'+													
													'</span>'+
													'<span>'+
															'<a class="delete" href="#" title="Delete">'+
															'<img class="icon" src="img/trash.png" style="border:0px">'+
														'</a>'+									        
													'</span>'+
												'</div>'+
												'<div>'+
													'<div align="center"><font style="color:red; font-weight:bold;">'+schedule[key].content+'</font></div>'+
												'</div>'+
											'</div>'
										);
										code
										.appendTo( contentPane )
										.find( 'a.edit' )
										.click( function() { edit( key ); } );
										code
										.appendTo( contentPane )
										.find( 'a.delete' )
										.click( function() { remove( key, false ); } );
                                }
                        }
                        counter++;			
                });     
        }	
	
	//edit
	function edit ( key ) {	   
		_this.onedit( $.toJSON(schedule[key]) );		
	}
	
	//remove
	function remove ( key, force ){	        
	        if( onRemove ){
		        if(onRemove(schedule[key])){
			        delete schedule[key];
			        this.load();
		        }			
	        }               
	}
		
	//gets the x and y coordinates---classname==day+from
	function XY( key, axis ){
		var AX = 0;
		var obj =$('#'+divId+' .'+key)[0];
				
		if( obj.offsetParent ){
			while( obj.offsetParent ){
				if( axis=="x" )   
					AX += obj.offsetLeft;
				else
					AX += obj.offsetTop;
				obj = obj.offsetParent;
			}
		}
		else if( obj.x ){
			if( axis=="x" ) 	
				AX += obj.x;
			else
				AX += obj.y;
		}

		return AX;
	}
	
	//computes for the height of the box
	function getHeight( from, to ){		
		var height=( getRange( from,to )*11 )-2;					
		if( getRange( from,to )<=1 ) 
			height=height-2;			
		return height;
	}
	
	//computes the nos. of 15 minutes	
	function getRange( from, to ){		
		return ( ( convertToMinute( to )-convertToMinute( from ) )/15 );
	}
	
	//converts the time to minutes
	function convertToMinute(time){
		return ( ( ( ( time-( time%100 ) )/100 )*60 )+( time%100 ) );
	}
	
	this.clear = function(){
		$.each( schedule,function( key ){
			delete schedule[key];
		});
	}
	
	this.parseTime = function(time){
		return time.replace(/^0+|:/g,'');		
	}
}


