
//@author: jaycverg

var PopupMenu = {
    show : function(e, id, pos) {
        post = (pos) ? pos : 'left';      
        var e = e || window.event;
        var obj = document.getElementById(id);
        if (!obj) {
            alert('Id ' + id + ' not found.');
            return;
        }
        
        var src = null;
        if (e.target) src = e.target;
        if (e.srcElement) src = e.srcElement;
        var menu = $(obj).find('.xmenu:first');
        menu.css('visibility', 'visible');
        var _menu = $(src).contextMenu(menu);
        e.pageX = PopupMenu.getOffsetLeft(src);
        e.pageY = PopupMenu.getOffsetTop(src) + src.offsetHeight;
        if (post == 'right')
            e.pageX -= (menu.width() - src.offsetWidth);
        _menu._showMenu(e);
    },
    
    getOffsetTop : function(elm) {
        var y = 0;
        while (elm) {
            y += elm.offsetTop;
            elm = elm.offsetParent;
        }					
        return y;
    },
    
    getOffsetLeft : function(elm) {
        var x = 0;
        while (elm) {
            x += elm.offsetLeft;
            elm = elm.offsetParent;
        }					
        return x;
    }
};