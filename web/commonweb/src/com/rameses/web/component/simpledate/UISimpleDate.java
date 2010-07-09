//version 1
package com.rameses.web.component.simpledate;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

/**
 *
 * @author rameses
 */
public class UISimpleDate extends UIInput {
    private String type;
    private boolean isChecked;
    String dateFormats[] = {"ymd", "ydm", "myd", "mdy", "dym", "dmy"};
    public UISimpleDate() {
        setConverter(new SimpleDateConverter());
    }
    
    public void encodeBegin(FacesContext ctx) throws IOException {
        String month;
        String day;
        String year;
        String hour;
        String minute;
        String second;
        Map request = ctx.getExternalContext().getRequestParameterMap();
        ResponseWriter writer = ctx.getResponseWriter();
        Date dateValue = (Date) getAttributes().get("value");
        int maxYear = 0 , minYear = 0;
        String styleClass = (String) getAttributes().get("styleClass");
        styleClass = (styleClass == null) ? "" : styleClass;
        String minYearStr = (String) getAttributes().get("minYear");
        String maxYearStr = (String) getAttributes().get("maxYear");
        Object hasPresentAsObject = getAttributes().get("hasPresent");
        Object displayMonthAsObject = getAttributes().get("displayMonth");
        Object displayYearAsObject = getAttributes().get("displayYear");
        Object displayDayAsObject = getAttributes().get("displayDay");
        //code added for date time;
        Object displayHourAsObject = getAttributes().get("displayHour");
        Object displayMinuteAsObject = getAttributes().get("displayMinute");
        Object displaySecondAsObject = getAttributes().get("displaySecond");
        String strDisplayTime = (String) getAttributes().get("displayTime");
        boolean displayTime = false;
        if(strDisplayTime != null)
            displayTime = Boolean.parseBoolean(strDisplayTime);
        type = (String) getAttributes().get("type");
        if(type == null)
            type = "select";
        else if(!(type.toLowerCase().equals("text"))){
            type = "select";
        }else
            type = "text";
        if(type.equals("text")){
            if(ctx.getExternalContext().getRequestMap().get("textScript") == null){
                ctx.getExternalContext().getRequestMap().put("textScript", "true");
                try {
                    writeTextScript(writer);
                } catch (Exception ex) {
                    System.out.println("Error in writeTextScript Method");
                }
            }
        }
        writer.startElement("input", this);
        writer.writeAttribute("name", getClientId(ctx)+"type", null);
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute("value", type, null);
        writer.endElement("input");
        boolean hasPresent = false;
        if(hasPresentAsObject != null){
            if(hasPresentAsObject instanceof String){
                hasPresent = Boolean.parseBoolean(hasPresentAsObject.toString());
            }else{
                hasPresent = ((Boolean) hasPresentAsObject).booleanValue();
            }
        }
        boolean displayMonth = true;
        boolean displayYear = true;
        boolean displayDay = true;
        boolean displayHour = true;
        boolean displayMinute = true;
        boolean displaySecond = false;
        if(displayMonthAsObject != null){
            if(displayMonthAsObject instanceof String){
                displayMonth = Boolean.parseBoolean(displayMonthAsObject.toString());
            }else{
                displayMonth = ((Boolean) displayMonthAsObject).booleanValue();
            }
        }
        if(displayDayAsObject != null){
            if(displayDayAsObject instanceof String){
                displayDay = Boolean.parseBoolean(displayDayAsObject.toString());
            }else{
                displayDay = ((Boolean) displayDayAsObject).booleanValue();
            }
        }
        if(displayYearAsObject != null){
            if(displayYearAsObject instanceof String){
                displayYear = Boolean.parseBoolean(displayYearAsObject.toString());
            }else{
                displayYear = ((Boolean) displayYearAsObject).booleanValue();
            }
        }
        //code added for date/time
        if(displayHourAsObject != null){
            if(displayHourAsObject instanceof String){
                displayHour = Boolean.parseBoolean(displayHourAsObject.toString());
            }else{
                displayHour = ((Boolean) displayHourAsObject).booleanValue();
            }
        }
        if(displayMinuteAsObject != null){
            if(displayMinuteAsObject instanceof String){
                displayMinute = Boolean.parseBoolean(displayMinuteAsObject.toString());
            }else{
                displayMinute = ((Boolean) displayMinuteAsObject).booleanValue();
            }
        }
        if(displaySecondAsObject != null){
            if(displaySecondAsObject instanceof String){
                displaySecond = Boolean.parseBoolean(displaySecondAsObject.toString());
            }else{
                displaySecond = ((Boolean) displaySecondAsObject).booleanValue();
            }
        }
        ///----
        Object renderedValue = null;
        renderedValue = getAttributes().get("rendered");
        boolean rendered = true;
        if(renderedValue != null){
            if(renderedValue instanceof String)
                rendered = Boolean.parseBoolean(renderedValue.toString());
            else
                rendered = ((Boolean) renderedValue).booleanValue();
        }
        if(!rendered)
            return;
        try{
            maxYear = maxYearStr == null ? GregorianCalendar.getInstance().get(GregorianCalendar.YEAR) : Integer.parseInt(maxYearStr);
            minYear = minYearStr == null ? 1950 : Integer.parseInt(minYearStr);
        }catch(Exception e){
            maxYear = GregorianCalendar.getInstance().get(GregorianCalendar.YEAR);
            minYear = 1950;
        }
        month = (String) request.get(getClientId(ctx)+"month");
        day = (String) request.get(getClientId(ctx)+"day");
        year = (String) request.get(getClientId(ctx)+"year");
        hour = (String) request.get(getClientId(ctx)+"hour");
        minute = (String) request.get(getClientId(ctx)+"minute");
        second = (String) request.get(getClientId(ctx)+"second");
        if(month == null && day == null && year == null){
            if (dateValue != null) {
                month = new SimpleDateFormat("MM").format(dateValue);
                day = new SimpleDateFormat("dd").format(dateValue);
                year = new SimpleDateFormat("yyyy").format(dateValue);
                hour = new SimpleDateFormat("HH").format(dateValue);
                minute = new SimpleDateFormat("mm").format(dateValue);
                second = new SimpleDateFormat("ss").format(dateValue);
            }else{
                month = "0";
                day = "0";
                year = "-1";
                hour= "-1";
                minute= "-1";
                second = "-1";
            }
        }
        writer.startElement("div", null);
        //writer.writeAttribute("style", "float: left", null);
        writer.writeAttribute("class", styleClass, null);
        String dateFormat = (String) getAttributes().get("dateFormat");
        DatePatternUtil dateUtil = new DatePatternUtil
            (
            ctx,
            hasPresent,
            this,
            styleClass,
            displayDay,
            displayMonth,
            displayYear,
            isChecked,
            maxYear,
            minYear,
            month,
            day ,
            year,
            hour,
            minute,
            second,
            type
            );
        String validFormat = getValidDateFormat(dateFormat);       
        char[] arr = validFormat.toCharArray();
        for(int i = 0; i < arr.length; i++) {
            try {
                //if(i > 0) dateUtil.writeSeparator("-");
                if(arr[i] == 'y' && displayYear){
                    if(i > 0) dateUtil.writeSeparator("-");
                    dateUtil.encodeYear();
                }
                else if(arr[i] == 'm' && displayMonth){
                    if(i > 0) dateUtil.writeSeparator("-");
                    dateUtil.encodeMonth(null);
                }
                else if(arr[i] == 'M' && displayMonth){
                    if(i > 0) dateUtil.writeSeparator("-");
                    dateUtil.encodeMonth("select");
                }
                else if(arr[i] == 'd' && displayDay){
                    if(i > 0) dateUtil.writeSeparator("-");
                    dateUtil.encodeDay();
                }
            } catch (Exception ex) {
                throw new IllegalStateException(ex.getMessage(), ex);
            }
        }
        //code added for date/time
        try {
            if(displayTime){
                dateUtil.writeSeparator("&nbsp;");
                dateUtil.encodeHour(displayHour);
                if(displayMinute)
                    dateUtil.writeSeparator(":");
                dateUtil.encodeMinute(displayMinute);
                if(displaySecond)
                    dateUtil.writeSeparator(":");
                dateUtil.encodeSecond(displaySecond);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ///---
        if(hasPresent){
            displayHasPresent(ctx, writer);
        }
    }
    
    private void writeTextScript(ResponseWriter writer)throws Exception{
        writer.write("\n<script language=\"javascript\">");
        writer.write("\n\t function focusTo(e, elem, parent){");
        writer.write("\n\t\t var keycode = e.which || e.keyCode;");
        writer.write("\n\t\t if(keycode == 9 || keycode == 16){");
        writer.write("\n\t\t\t return true;");
        writer.write("\n\t\t }");
        writer.write("\n\t\t var s = elem.value;");
        writer.write("\n\t\t if( s.length == elem.maxLength) {");
        writer.write("\n\t\t\t var p = parent;");
        writer.write("\n\t\t\t while (p = p.nextSibling){");
        writer.write("\n\t\t\t\t if(p.tagName == 'SPAN'){");
        writer.write("\n\t\t\t\t\t var elements = p.getElementsByTagName('INPUT');");
        writer.write("\n\t\t\t\t\t if(elements.length > 0){");
        writer.write("\n\t\t\t\t\t\t elements[0].focus();");
        writer.write("\n\t\t\t\t\t\t break;");
        writer.write("\n\t\t\t\t\t }");
        writer.write("\n\t\t\t\t }");
        writer.write("\n\t\t\t }");
        writer.write("\n\t\t }");
        writer.write("\n\t }"); 
        writer.write("\n</script>");
    }
    private void displayHasPresent(FacesContext ctx, ResponseWriter writer) throws IOException{
        if(ctx.getExternalContext().getRequestMap().get("disableSelect") == null){
            ctx.getExternalContext().getRequestMap().put("disableSelect", "true");
            writer.write("\n<script language=\"javascript\">");
            writer.write("\n\t function disabler(id0, id1, id2, id3, id4, id5, id6, type){");
            writer.write("\n\t\t    var arr = new Array();");
            writer.write("\n\t\t    arr[0] = id1;");
            writer.write("\n\t\t    arr[1] = id2;");
            writer.write("\n\t\t    arr[2] = id3;");
            writer.write("\n\t\t    arr[3] = id4;");
            writer.write("\n\t\t    arr[4] = id5;");
            writer.write("\n\t\t    arr[5] = id6;");
            writer.write("\n\t\t    var i;");
            writer.write("\n\t\t    var check = document.getElementById(id0);");
            writer.write("\n\t\t    for(i = 0; i < arr.length; i++){");
            writer.write("\n\t\t\t      var elmnt = document.getElementById(arr[i]);");
            writer.write("\n\t\t\t      if(elmnt){");
            writer.write("\n\t\t\t\t        if(check.checked){");
            writer.write("\n\t\t\t\t\t           elmnt.disabled = true;");
            writer.write("\n\t\t\t\t\t           if(type != 'text'){");
            writer.write("\n\t\t\t\t\t\t            var option = new Option();");
            writer.write("\n\t\t\t\t\t\t            option.selected = true;");
            writer.write("\n\t\t\t\t\t\t            option.text = \"--\";");
            writer.write("\n\t\t\t\t\t\t            option.value = null;");
            writer.write("\n\t\t\t\t\t\t            elmnt.options[0] = option;");
            writer.write("\n\t\t\t\t\t           }else{");
            writer.write("\n\t\t\t\t\t\t            elmnt.value = \"\";");
            writer.write("\n\t\t\t\t\t           }");
            writer.write("\n\t\t\t\t        }else{");
            writer.write("\n\t\t\t\t\t           elmnt.disabled = false;");
            writer.write("\n\t\t\t\t        }");
            writer.write("\n\t\t\t      }");
            writer.write("\n\t\t    }");
            writer.write("\n\t }");
            writer.write("\n</script>");
        }
        writer.startElement("input", this);
        writer.writeAttribute("id", getClientId(ctx)+"checkbox", null);
        writer.writeAttribute("name", getClientId(ctx)+"checkbox", null);
        writer.writeAttribute("type", "checkbox", null);
        String idChbx = getClientId(ctx)+"checkbox";
        String idYear = getClientId(ctx) + "year";
        String idMonth = getClientId(ctx) + "month";
        String idDay = getClientId(ctx) + "day";
        String idHour = getClientId(ctx) + "hour";
        String idMinute = getClientId(ctx) + "minute";
        String idSecond = getClientId(ctx) + "second";
        writer.writeAttribute("onclick", "disabler('"+idChbx  +"','"+idYear+"'"+",'"+idMonth+"','"+ idDay +"', '"+ idHour +"', '"+idMinute+"', '"+ idSecond+ "', '"+type+"');", null);
        if(isChecked)
            writer.writeAttribute("checked", true, null);
        writer.endElement("input");
        writer.write("Present");
    }
    
    public void encodeEnd(FacesContext context) throws IOException {
        context.getResponseWriter().endElement("div");
    }
    
    @Override
    public void decode(FacesContext ctx) {
        String month;
        String day;
        String year;
        //code added fro date/time
        String hour;
        String minute;
        String second;
        ////-----
        Map map = ctx.getExternalContext().getRequestParameterMap();
        if(map.get(getClientId(ctx)+"checkbox") != null)
            isChecked = true;
        else
            isChecked = false;
        Date dateValue = null;
        Object obj = ctx.getExternalContext().getSessionMap().get(getClientId(ctx)+":dateValue");
        month = (String) map.get(getClientId(ctx) + "month");
        day = (String) map.get(getClientId(ctx) + "day") ;
        year = (String) map.get(getClientId(ctx) + "year");
        hour = (String) map.get(getClientId(ctx) + "hour");
        minute = (String) map.get(getClientId(ctx) + "minute");
        second = (String) map.get(getClientId(ctx) + "second");
        String type = (String) map.get(getClientId(ctx) + "type");
        if(month == null)
            month = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()))+"";
        if(day == null)
            day = "1";
        if(year == null)
            year = new SimpleDateFormat("yyyy").format(new Date());
        //code added for date/time
        if(hour == null)
            hour = "00";
        if (minute == null)
            minute = "00";
        if(second == null)
            second = "00";
        year = getValidValue(year);
        month = getValidValue(getValidFormat(month));
        day = getValidValue(getValidFormat(day));
        hour = getValidValue(getValidFormat(hour));
        minute = getValidValue(getValidFormat(minute));
        second = getValidValue(getValidFormat(second));
        setSubmittedValue(year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second + ";;" + isChecked);
        setValid(true);
    }
    
    private String getValidFormat(String s){
        s = s.replace("-", "");
        if(s.trim().length() == 1)
            return  "0"+s.trim();
        return s;
    }
    
    private String getValidDateFormat(String df){
        if(df == null)
            return "ymd";
        String s;
        s = df.toLowerCase();
        if(s.length() != 3)
            return "ymd";
        if(s.indexOf("y") < 0)
            return "ymd";
        if(s.indexOf("m") < 0)
            return "ymd";
        if(s.indexOf("d") < 0)
            return "ymd";
        return df;
    }
    
    private String getValidValue(String s){
        s = s.replace("-", "");
        if(s.trim().equals(""))
            return "null";
        return s;
    }
}
