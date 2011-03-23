/**
 * @author jaycverg
 */


var DynamicProxy = function( conf ) {
    
    var url = conf['server'];
    var service = conf.service;
    
    this.invoke = function(methodName, args, callback) {
        var strParam = toJSON( args );
        var svc = service + '.' + methodName;
        $.ajax({
            type: 'POST',
            url: url,
            data: 'service=' + svc + '&args=' + strParam,
            success: function(result) {
                var obj = null;
                try {
                    obj = eval(result);
                }
                catch(e) {
                    alert( e.message );
                    //alert( result );
                }
                callback(obj);
            }
        });
    };
    
    function toJSON( arr ) {
        var parts = [];
        var is_list = arr instanceof Array;
        
        for(var key in arr) {
            var value = arr[key];
            if(typeof value == "object") {
                if(is_list) {
                    parts.push(toJSON(value));
                } else {
                    parts[key] = toJSON(value);
                }
            } else {
                var str = "";
                if(!is_list) {
                    str = '"' + key + '":';
                }
                
                if(typeof value == "number") {
                    str += value;
                } else if(value === false) {
                    str += 'false';
                } else if(value === true) {
                    str += 'true';
                } else {
                    str += '"' + (value+"").replace(/(?=["\\])/g, '\\').replace(/\n/g,'\\n') + '"';
                }
                
                parts.push(str);
            }
        }
        
        var json = parts.join(",");
        if(is_list) {
            return '[' + json + ']';
        }
        return '{' + json + '}';
    }
    
};
