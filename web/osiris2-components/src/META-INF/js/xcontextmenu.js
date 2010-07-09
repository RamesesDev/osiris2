
(function($){
  var defaultStyle = {
    menu: {
      listStyleType: "none",
      position:      "absolute",
      margin:        0,
      padding:       0,
      border:        "1px solid darkgray",
      cursor:        "default",
      zIndex:        1000
    },
    item: {
      color:           "black",
      backgroundColor: "white",
      padding:         "2 20 2 20",
      whiteSpace:      "nowrap"
    },
    hoverItem: {
      color:           "white",
      backgroundColor: "#3060B0"
    },
    containerItem: {      // IE 7 doesn't support "data:image/gif;base64" urls
      backgroundImage:    "url(images/jquery.n-contextmenu.arrow.gif)",
      backgroundPosition: "95%",
      backgroundRepeat:   "no-repeat"
    },
    separator: {
      borderTop: "1px solid darkgray"
    }
  }

  $.fn.contextMenu = function(menu, userStyle, onShow) {
    if (typeof userStyle == "function") {
      onShow = userStyle;
      userStyle = {};
    }
    var style = merge({}, defaultStyle, userStyle);
    var data = { menu: menu, style: style, onShow: onShow };

    this.bind("contextmenu", data, onContextMenu);
    this.bind("click", data, onClick);

	this._showMenu = function(e) {
		e.button = 0;
		e.ctrlKey = true;
		e.data = data;
		onClick(e);
	};
	
	return this;
  }

  // TODO: Replace with $.extend(true, ...) when deep copy
  // bug is fixed: http://dev.jquery.com/ticket/1562

  function merge(target) {
    if (!target) target = new Object();

    for(var j = 1; j < arguments.length; j++) {
      var source = arguments[j];

      for(var i in source) {
        if (source[i] == null) continue;
        switch(typeof source[i]) {
          case "string":
          case "number":
          case "boolean":
          case "function":
              target[i] = source[i];
              break;
          default:
              target[i] = merge(target[i], source[i]);
              break;
        }
      }
    }
    return target;
  }

  // Only one context menu should
  // be visible at a time
  var currentMenu;

  function onContextMenu(e) {
    closeCurrentMenu();

    if (e.data.onShow &&
        e.data.onShow.apply(this, [e]) == false) return;
                                  
    currentMenu = e.data.menu;
    $(document).bind("click", currentMenu, closeCurrentMenu);
    $(window).bind("blur", currentMenu, closeCurrentMenu);

    hideMenu(e.data.menu);
    showMenu(e.data.menu, e.data.style, e.pageX, e.pageY);
    return false;
  }

  function closeCurrentMenu() {
    if (!currentMenu) return;

    $(document).unbind("click", currentMenu, closeCurrentMenu);
    $(window).unbind("blur", currentMenu, closeCurrentMenu);
    hideMenu(currentMenu);
    currentMenu = null;
  }

  function onClick(e) {
	if (e.button == 0 && e.ctrlKey) {
	  return onContextMenu(e);
	}
  }

  function showMenu(menu, style, x, y) {
    menu = $(menu);
    if (menu.is(":visible")) return;

    menu.find("ul")
        .css(style.menu)
        .prev("li")
        .css(style.containerItem)
        .end()
        .parent("li")
        .css(style.containerItem);

    menu.find("li")
        .css(style.item)
        .bind("click", onItemClick)
        .bind("mouseenter", style, onItemEnter);

    menu.find("li[separator]")
        .css(style.separator);

    menu.css(style.menu)
        .css("left", x)
        .css("top", y)
        .show();
  }

  // Hide submenus in reverse order

  function hideMenu(menu) {
    var submenus = $(menu).find("ul")
                          .get()
                          .reverse();
    $(submenus).hide();
    $(menu).hide();
  }

  function getSubmenu(item) {
    item = $(item);
    var submenu = item.next();
    if (!submenu.is("ul")) {
      submenu = item.children();
    }
    if (submenu.is("ul")) {
      return submenu;
    }
  }

  // Return false for items that have submenus
  // So that clicking on such items doesn't close menu

  function onItemClick(e) {
    if (this == e.target) {
	  return true;
      //return !getSubmenu(this);
    }
  }

  function onItemEnter(e) {
    var self = this;
    var style = e.data;
    var menu = $(this).parent();

    menu.children("li").each(function() {
      var item = $(this);
      var submenu = getSubmenu(item);
      if (submenu) {
        var x = menu.width();
        var y = item.position().top - 1;
        self == this ? showMenu(submenu, style, x, y) : hideMenu(submenu);
      }
      item.css(self == this ? e.data.hoverItem : e.data.item);
    });
  }
})(jQuery);
