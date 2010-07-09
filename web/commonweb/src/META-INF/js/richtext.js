
/***********************************************************
 *  edited by: Arnel Retiza and Jayrome Vergara
 *  inorder to be embeded in the JSF-Facelet framework
 *  (c) 2008
 ***********************************************************/

var isRichText = false;
var resourcePath;
var rng;
var currentRTE;
var allRTEs = "";

var isIE;
var isGecko;
var isSafari;
var isKonqueror;

var readOnly;
var buttons;

function init() {
    //set browser vars    
    var ua = navigator.userAgent.toLowerCase();
    isIE = ((ua.indexOf("msie") != -1) && (ua.indexOf("opera") == -1) && (ua.indexOf("webtv") == -1)); 
    isGecko = (ua.indexOf("gecko") != -1);
    isSafari = (ua.indexOf("safari") != -1);
    isKonqueror = (ua.indexOf("konqueror") != -1);   
    //check to see if designMode mode is available
    //  if (document.getElementById && document.designMode && !isSafari && !isKonqueror) {
    isRichText = true;
    // }
    
    if(isIE){
        document.onmouseover = raiseButton;
        document.onmouseout  = normalButton;
        document.onmousedown = lowerButton;
        document.onmouseup   = raiseButton;
    }
}

function writeRichText(name, value, width, height, type, resPath) {
    resourcePath = resPath
    readOnly = false; 
    buttons = true;
    if (isRichText) {
        init();
        if (allRTEs.length > 0) allRTEs += ";";
        allRTEs += name;
        
        if (readOnly) buttons = false;
        
        //adjust minimum table widths
        var tablewidth = parseInt(width);    
        if (buttons && (tablewidth < 400)) 
            tablewidth = 500;  
        
        if (! isIE) tablewidth += 4;
        var input = "";
        if(!readOnly)
            input = '<br /><input type="checkbox" id="chkSrc' + name + '" onclick="toggleHTMLSrc(\'' + name + '\');" />&nbsp;Write HTML'; 
        
        var div = document.getElementById('wrapper' + name);
        div.onmousemove = function(e) { updateRTEValues(); };
        div.innerHTML = getToolbar(name, tablewidth) +        
        '<iframe id="' + name + '" name="' + name + '" width="' + width + 'px" height="' + height + 'px" src="' + resourcePath + '/includefile/blank.html"></iframe>' +                        
        input +               
        '<input type="hidden" id="hdn' + name + '" name="' + name + '" value="">' ;    
        enableDesignMode(name, value, readOnly);
        document.getElementById('hdn' + name).value = value;         
    } else {
        if (!readOnly) {
            document.writeln('<textarea name="' + name + '" id="' + name + '" style="width: ' + width + 'px; height: ' + height + 'px;">' + html + '</textarea>');
        } else {
            document.writeln('<textarea name="' + name + '" id="' + name + '" style="width: ' + width + 'px; height: ' + height + 'px;" readonly>' + html + '</textarea>');
        }
    }
    self.focus();
}

function getToolbar(name, tablewidth){
    var toolbar =  '<table class="rteBack" cellpadding="0" cellspacing="0" id="Buttons2_' + name + '" width="' + tablewidth + '">'+
    '	<tr> ' +                                                
    '		<td><img id="bold" class="rteImage" src="' + resourcePath + '/images/bold.gif" width="25" height="24" alt="Bold" title="Bold" onClick="rteCommand(\'' + name + '\', \'bold\', \'\')"></td>' +
    '		<td><img class="rteImage" src="' + resourcePath + '/images/italic.gif" width="25" height="24" alt="Italic" title="Italic" onClick="rteCommand(\'' + name + '\', \'italic\', \'\')"></td>'+
    '		<td><img class="rteImage" src="' + resourcePath + '/images/underline.gif" width="25" height="24" alt="Underline" title="Underline" onClick="rteCommand(\'' + name + '\', \'underline\', \'\')"></td>'+
    '		<td><img class="rteVertSep" src="' + resourcePath + '/images/blackdot.gif" width="1" height="20" border="0" alt=""></td>'+
    '		<td><img class="rteImage" src="' + resourcePath + '/images/left_just.gif" width="25" height="24" alt="Align Left" title="Align Left" onClick="rteCommand(\'' + name + '\', \'justifyleft\', \'\')"></td>'+
    '		<td><img class="rteImage" src="' + resourcePath + '/images/centre.gif" width="25" height="24" alt="Center" title="Center" onClick="rteCommand(\'' + name + '\', \'justifycenter\', \'\')"></td>'+
    '		<td><img class="rteImage" src="' + resourcePath + '/images/right_just.gif" width="25" height="24" alt="Align Right" title="Align Right" onClick="rteCommand(\'' + name + '\', \'justifyright\', \'\')"></td>'+
    '		<td><img class="rteImage" src="' + resourcePath + '/images/justifyfull.gif" width="25" height="24" alt="Justify Full" title="Justify Full" onclick="rteCommand(\'' + name + '\', \'justifyfull\', \'\')"></td>'+
    '		<td><img class="rteVertSep" src="' + resourcePath + '/images/blackdot.gif" width="1" height="20" border="0" alt=""></td>' +
    '		<td><img class="rteImage" src="' + resourcePath + '/images/numbered_list.gif" width="25" height="24" alt="Ordered List" title="Ordered List" onClick="rteCommand(\'' + name + '\', \'insertorderedlist\', \'\')"></td>'+
    '		<td><img class="rteImage" src="' + resourcePath + '/images/list.gif" width="25" height="24" alt="Unordered List" title="Unordered List" onClick="rteCommand(\'' + name + '\', \'insertunorderedlist\', \'\')"></td>'+
    '		<td><img class="rteVertSep" src="' + resourcePath + '/images/blackdot.gif" width="1" height="20" border="0" alt=""></td>'+
    '		<td><img class="rteImage" src="' + resourcePath + '/images/outdent.gif" width="25" height="24" alt="Outdent" title="Outdent" onClick="rteCommand(\'' + name + '\', \'outdent\', \'\')"></td>'+
    '		<td><img class="rteImage" src="' + resourcePath + '/images/indent.gif" width="25" height="24" alt="Indent" title="Indent" onClick="rteCommand(\'' + name + '\', \'indent\', \'\')"></td>'+
    '		<td><img class="rteVertSep" src="' + resourcePath + '/images/blackdot.gif" width="1" height="20" border="0" alt=""></td>'+
    '		<td><img class="rteImage" src="' + resourcePath + '/images/undo.gif" width="25" height="24" alt="Undo" title="Undo" onClick="rteCommand(\'' + name + '\', \'undo\')"></td>'+
    '		<td><img class="rteImage" src="' + resourcePath + '/images/redo.gif" width="25" height="24" alt="Redo" title="Redo" onClick="rteCommand(\'' + name + '\', \'redo\')"></td>'+
    '		<td width="100%"></td>'+
    '	</tr>' +
    '</table>';
    return toolbar;
    
}

function enableDesignMode(name, value, readOnly) {
    var oRTE;
    var frameHtml = "<html id=\"" + name + "\">\n";
    frameHtml += "<head>\n";
    frameHtml += "<style type='text/css'> body, * ";
    frameHtml += "{ font-family: arial, verdana, times; font-size: 11pt; }</style>\n";
    frameHtml += "</head>\n";
    frameHtml += "<body>\n";
    frameHtml += value + "\n";
    frameHtml += "</body>\n";
    frameHtml += "</html>";
    if (document.all) {
        oRTE = frames[name].document;
        oRTE.open();
        oRTE.write(frameHtml);
        oRTE.close();
        if (!readOnly) oRTE.designMode = "On";
    } else {
        try {
            if (!readOnly) document.getElementById(name).contentDocument.designMode = "on";
            try {
                oRTE = document.getElementById(name).contentWindow.document;
                oRTE.open();
                oRTE.write(frameHtml);
                oRTE.close();
                if (isGecko && !readOnly) {
                    //attach a keyboard handler for gecko browsers to make keyboard shortcuts work
                    oRTE.addEventListener("keypress", kb_handler, true);
                }
            } catch (e) {
                throw Error("Error preloading content.");
            }
        } catch (e) {
            //gecko may take some time to enable design mode.
            //Keep looping until able to set.
            if (isGecko) {
                setTimeout("enableDesignMode('" + name + "', '" + value + "', " + readOnly + ");", 10);
            } else {
                return false;
            }
        }
    }  
}

function updateRTEs() {
    var vRTEs = allRTEs.split(";");
    for (var i = 0; i < vRTEs.length; i++) {
        updateRTE(vRTEs[i]);
    }
}

function updateRTEValues() {
    var vRTEs = allRTEs.split(";");
    for (var i = 0; i < vRTEs.length; i++) {
        updateRTEValue(vRTEs[i]);
    }
}

function updateRTE(name) {    
    if (!isRichText) return;
    
    //set message value
    var oHdnMessage = document.getElementById('hdn' + name);    
    var oRTE = document.getElementById(name);
    var readOnly = false;
    
    //check for readOnly mode
    if (document.all) {
        if (frames[name].document.designMode != "On") readOnly = true;
    } else {
        if (document.getElementById(name).contentDocument.designMode != "on") readOnly = true;
    }
    
    if (isRichText && !readOnly) {
        //if viewing source, switch back to design view
        if (document.getElementById("chkSrc" + name).checked) {
            document.getElementById("chkSrc" + name).checked = false;
            toggleHTMLSrc(name);
        }
        
        if (oHdnMessage.value == null) oHdnMessage.value = "";
        if (document.all) {
            oHdnMessage.value = frames[name].document.body.innerHTML;
        } else {
            oHdnMessage.value = oRTE.contentWindow.document.body.innerHTML;
        }
        
        //if there is no content (other than formatting) set value to nothing
        if (stripHTML(oHdnMessage.value.replace("&nbsp;", " ")) == "" 
        && oHdnMessage.value.toLowerCase().search("<hr") == -1
        && oHdnMessage.value.toLowerCase().search("<img") == -1) oHdnMessage.value = "";
        //fix for gecko
        if (escape(oHdnMessage.value) == "%3Cbr%3E%0D%0A%0D%0A%0D%0A") oHdnMessage.value = "";
    }
}

function updateRTEValue(name) {
    var oHdnMessage = document.getElementById('hdn' + name);    
    var oRTE = document.getElementById(name);
    
    if (oHdnMessage.value == null) oHdnMessage.value = "";
    if (document.all) {
        oHdnMessage.value = frames[name].document.body.innerHTML;
    } else {
        oHdnMessage.value = oRTE.contentWindow.document.body.innerHTML;
    }
    
    //if there is no content (other than formatting) set value to nothing
    if (stripHTML(oHdnMessage.value.replace("&nbsp;", " ")) == "" 
    && oHdnMessage.value.toLowerCase().search("<hr") == -1
    && oHdnMessage.value.toLowerCase().search("<img") == -1) oHdnMessage.value = "";
    //fix for gecko
    if (escape(oHdnMessage.value) == "%3Cbr%3E%0D%0A%0D%0A%0D%0A") oHdnMessage.value = "";
}

function rteCommand(name, command, option) {
    //function to perform command
    var oRTE;
    if (document.all) {
        oRTE = frames[name];
    } else {
        oRTE = document.getElementById(name).contentWindow;
    }
    
    try {
        oRTE.focus();
        oRTE.document.execCommand(command, false, option);
        oRTE.focus();
    } catch (e) {
        //alert(e);
        //setTimeout("rteCommand('" + name + "', '" + command + "', '" + option + "');", 10);
    }
}

function toggleHTMLSrc(name) {
    var oRTE;
    if (document.all) {
        oRTE = frames[name].document;
    } else {
        oRTE = document.getElementById(name).contentWindow.document;
    }
    
    if (document.getElementById("chkSrc" + name).checked) {
        showHideElement("Buttons2_" + name, "hide");
        if (document.all) {
            oRTE.body.innerText = oRTE.body.innerHTML;
        } else {
            var htmlSrc = oRTE.createTextNode(oRTE.body.innerHTML);
            oRTE.body.innerHTML = "";
            oRTE.body.appendChild(htmlSrc);
        }
    } else {
        showHideElement("Buttons2_" + name, "show");
        if (document.all) {
            //fix for IE
            var output = escape(oRTE.body.innerText);
            output = output.replace("%3CP%3E%0D%0A%3CHR%3E", "%3CHR%3E");
            output = output.replace("%3CHR%3E%0D%0A%3C/P%3E", "%3CHR%3E");
            
            oRTE.body.innerHTML = unescape(output);
        } else {
            var htmlSrc = oRTE.body.ownerDocument.createRange();
            htmlSrc.selectNodeContents(oRTE.body);
            oRTE.body.innerHTML = htmlSrc.toString();
        }
    }
}

function kb_handler(evt) {
    var name = evt.target.id;
    if (evt.ctrlKey) {
        var key = String.fromCharCode(evt.charCode).toLowerCase();
        var cmd = '';
        switch (key) {
            case 'b': cmd = "bold"; break;
            case 'i': cmd = "italic"; break;
            case 'u': cmd = "underline"; break;
        };
        
        if (cmd) {
            rteCommand(name, cmd, null);
            
            evt.preventDefault();
            evt.stopPropagation();
        }
    }
}

function showHideElement(element, showHide) {
    //function to show or hide elements
    //element variable can be string or object
    
    if (document.getElementById(element)) {
        element = document.getElementById(element);
    }
    
    if (showHide == "show") {
        element.style.visibility = "visible";
    } else if (showHide == "hide") {
        element.style.visibility = "hidden";
    }
}

function stripHTML(oldString) {
    //function to strip all html
    var newString = oldString.replace(/(<([^>]+)>)/ig,"");
    
    //replace carriage returns and line feeds
    newString = newString.replace(/\r\n/g," ");
    newString = newString.replace(/\n/g," ");
    newString = newString.replace(/\r/g," ");
    
    //trim string
    newString = newString.trim();
    
    return newString;
}

//*************************
//String Hacks - Trim function
//*************************

// Removes leading whitespaces
String.prototype.leftTrim = function() {
    var re = /\s*((\S+\s*)*)/;
    return this.replace(re, "$1");
}

// Removes ending whitespaces
String.prototype.rightTrim = function() {        
    var re = /((\s*\S+)*)\s*/;
    return this.replace(re, "$1");        
}

// Removes leading and ending whitespaces
String.prototype.trim = function() {
    return this.leftTrim().rightTrim();        
}
//end of string hack

//*****************
//IE-Only Functions
//*****************

function raiseButton(e) {
    //IE-Only Function
    var el = window.event.srcElement;
    
    className = el.className;
    if (className == 'rteImage' || className == 'rteImageLowered') {
        el.className = 'rteImageRaised';
    }
}

function normalButton(e) {
    //IE-Only Function
    var el = window.event.srcElement;
    
    className = el.className;
    if (className == 'rteImageRaised' || className == 'rteImageLowered') {
        el.className = 'rteImage';
    }
}

function lowerButton(e) {
    //IE-Only Function
    var el = window.event.srcElement;
    
    className = el.className;
    if (className == 'rteImage' || className == 'rteImageRaised') {
        el.className = 'rteImageLowered';
    }
}