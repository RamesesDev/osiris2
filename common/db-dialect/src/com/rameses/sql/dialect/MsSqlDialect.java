/*
 * MsSqlDialect.java
 *
 * Created on April 30, 2012, 8:49 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.sql.dialect;

import com.rameses.sql.CrudSqlBuilder;
import com.rameses.sql.SqlDialect;
import com.rameses.sql.dialect.mssql.MsSqlCrudSqlBuilder;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

/**
 *
 * @author Elmo
 * implementing paging routine for mssql server
 */
public class MsSqlDialect implements SqlDialect  {
    
    private static final Pattern FN_PATTERN = Pattern.compile("[a-zA-Z]\\w+\\(.*?\\)");
    
    public String getName() {
        return "mssql";
    }
    
    public String getPagingStatement(String sql, int start, int limit, String[] pagingKeys) {
        try {
            return doParse(sql, start, limit, pagingKeys);
        }
        catch(Exception e) {
            System.out.println("=== error parsing statement ===\n" + sql + "===========");
            throw new RuntimeException(e);
        }
    }

    private String doParse(String sql, int start, int limit, String[] pagingKeys) 
    {
        String ids = "objid";
        if( pagingKeys !=null && pagingKeys.length>0) {
            boolean firstTime = true;
            StringBuilder keys = new StringBuilder();
            for( String s: pagingKeys) {
                if(!firstTime) 
                    keys.append("+");
                else 
                    firstTime = false;
                keys.append( s );
            }
            ids = keys.toString();
        }

        int STATE_SELECT = 0;
        int STATE_COLUMNS = 1;
        int STATE_FROM = 2;
        int STATE_WHERE = 3;
        int STATE_GROUP = 4;
        int STATE_HAVING = 5;
        int STATE_ORDER = 6;

        StringBuilder selectBuilder = new StringBuilder();
        StringBuilder columnBuilder = new StringBuilder();
        StringBuilder fromBuilder = new StringBuilder();
        StringBuilder whereBuilder = new StringBuilder();
        StringBuilder groupBuilder = new StringBuilder();
        StringBuilder havingBuilder = new StringBuilder();
        StringBuilder orderBuilder = new StringBuilder();

        StringBuilder currentBuilder = null;
        Stack stack = new Stack();
        int currentState = STATE_SELECT;

        StringTokenizer st = new StringTokenizer(sql.trim());
        while(st.hasMoreElements()) {
            String s = (String)st.nextElement(); 

            if( s.equalsIgnoreCase("select") && currentState <= STATE_SELECT  ) 
            {
                selectBuilder.append( s  );
                currentBuilder = columnBuilder;
                currentState = STATE_COLUMNS;
            }
            else if( s.equalsIgnoreCase("from") && currentState == STATE_COLUMNS && stack.empty()  ) 
            {
                currentBuilder = fromBuilder;
                currentBuilder.append( " " + s );
                currentState = STATE_FROM;
            } 
            else if( s.equalsIgnoreCase("where") && currentState == STATE_FROM && stack.empty()) 
            {
                currentBuilder = whereBuilder;
                currentBuilder.append( " " + s );
                currentState = STATE_WHERE;
            }
            else if( s.equalsIgnoreCase("group") && currentState <= STATE_WHERE && currentState != STATE_COLUMNS && stack.empty() ) 
            {
                currentBuilder = groupBuilder;
                currentBuilder.append( " " + s );
                currentState = STATE_GROUP;
            }
            else if( s.equalsIgnoreCase("having") && currentState <= STATE_GROUP && currentState != STATE_COLUMNS && stack.empty() ) 
            {
                currentBuilder = havingBuilder;
                currentBuilder.append( " " + s );
                currentState = STATE_HAVING;
            }
            else if( s.equalsIgnoreCase("order") && currentState <= STATE_HAVING && currentState != STATE_COLUMNS && stack.empty() ) 
            {
                currentBuilder = orderBuilder;
                currentBuilder.append( " " + s );
                currentState = STATE_ORDER;
            }
            else if(s.equals("(") || s.trim().startsWith("(") || s.trim().endsWith("(")) 
            {
                if( currentState != STATE_WHERE ) {
                    stack.push(true);
                }
                currentBuilder.append( " " + s );
            }
            else if(s.equals(")") || s.trim().startsWith(")") || s.trim().endsWith(")")) 
            {
                if( currentState != STATE_WHERE && !FN_PATTERN.matcher(s).matches() ) {
                    stack.pop();
                }
                currentBuilder.append( " " + s );
            }
            else {
                currentBuilder.append( " " + s );
            }
        }

        StringBuilder sresult = new StringBuilder();
        sresult.append(selectBuilder.toString());
        sresult.append( " top " + limit + " ");
        sresult.append(columnBuilder.toString());
        sresult.append(fromBuilder.toString());
        if( whereBuilder.length() == 0 ) {
            sresult.append(" where ");
        }
        else {
            sresult.append(" " + whereBuilder.toString());
            sresult.append(" and ");
        }
        sresult.append( " " + ids + " not in ");

        sresult.append("( select top " +  start + " " + ids + " ");
        sresult.append( " " + fromBuilder.toString());
        sresult.append( " " + whereBuilder.toString());
        sresult.append( " " + groupBuilder.toString());
        sresult.append(" " + orderBuilder.toString());
        sresult.append(")");
        sresult.append( " " + groupBuilder.toString());
        sresult.append(orderBuilder.toString());
        
        if( "true".equals((System.getProperty("app.debugMode")+"").toLowerCase()) ) {
            System.out.println("mssql dialect debug: ");
            System.out.println( sresult );
        }
        
        return sresult.toString();
    }

    public CrudSqlBuilder createCrudSqlBuilder() {
        return new MsSqlCrudSqlBuilder();
    }
    
}
