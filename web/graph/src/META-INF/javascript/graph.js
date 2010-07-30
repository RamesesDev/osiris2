
//ajaxgraph.js
//depends: jquery-1.3.js
//@author: jaycverg

(function() {
    
    //do not redefine if AjaxGraph is already defined
    if (window.AjaxGraph) return;
            
    var AjaxGraph = function(img, inputId, millsec) {
        var _interval = millsec;
        var _inputId = inputId;
        var _id = img;
        var _img1 = $(document.getElementById(img));
        var _src = _img1.attr('src');
        var _img2 = _img1.clone();
        var _timer = new Timer(refresh, _interval);
        var _form = _img1.parents('form:first');
        
        init();
        
        function init() {
            _src += (_src.indexOf('?') > -1)? '&' : '?';
            
            _img2.css({display : 'none'})
            .attr('src', '')
            .insertAfter(_img1)
            .load(img2_load);
            
            _img2.started = false;
            _img2.loaded = false;
            
            _img1.load(img1_load);
            _img1.started = false;
            _img1.loaded = false;
            
            _timer.start();
        }
        
        function img1_load() {
            _img1.css({display : 'block'});
            _img2.css({display : 'none'});
            _img1.loaded = true;
            _img1.started = false;
            _timer.start();
        }
        
        function img2_load() {
            _img1.css({display : 'none'});
            _img2.css({display : 'block'});
            _img2.loaded = true;
            _img2.started = false;
            _timer.start();
        }
        
        function refresh() {
            _timer.stop();
            var formName = _form[0].id+'_SUBMIT';
            var params = 'refresh_chart=true';

            $('input', _form[0]).each(function() {
                if ( this.type && this.type != 'hidden') return;
                if ( this.name == inputId || this.name == 'jsf_sequence' || this.name == formName ) {
                    params += '&' + escape(this.name) + '=' + escape(this.value);
                }
            });

            $.ajax({
                type    : 'POST',
                url     : _form.attr('action'), 
                data    : params,
                cache   : false,
                global	: false,
                success : function(resp) {
                    update();
                } 
            });
        }
        
        function update() {
            if (_img2.loaded && !_img1.started) {
                _img1.attr('src', _src + Math.random());
                _img1.started = true;
                _img1.loaded = false;
            }
            if (_img1.loaded && !_img2.started) {
                _img2.attr('src', _src + Math.random());
                _img2.started = true;
                _img2.loaded = false;
            }
        }
    };
    
    //make AjaxGraph global
    window.AjaxGraph = AjaxGraph;
})();
