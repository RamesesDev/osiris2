
//ajaxgraph.js
//depends: jquery-1.3.js
//@author: jaycverg

//ie console
var console = $('<textarea id="console"></textarea>').css({
    top: '0px', left: '0px', position: 'absolute', zIndex: 50000, width: 550,
    height: 200, background: '#ffffff', overflow: 'scroll', border: 'solid 1px gray'
});

$(function(){ console.appendTo('body'); });

console.log = function(msg) {
    console[0].value = console[0].value + '\n' + msg;
};

//chrome/firebug
if (!window.console) console = {};
console.log = console.log || function(){};
console.warn = console.warn || function(){};
console.error = console.error || function(){};
console.info = console.info || function(){};