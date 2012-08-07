/*
 * CmsServletConstants.java
 *
 * Created on June 27, 2012, 6:13 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.anubis.web;

/**
 *
 * @author Elmo
 */
public interface CmsWebConstants {
    
    public static String CONF = "anubis.conf";
    
    public static String PATHS_IGNORED = "paths.ignored";
    public static String EXT_IGNORED = "ext.ignored";
    
    public static String SESSIONID = "SESSIONID";
    public static String PAGE_FILE_EXT = ".pg";
    public static String MEDIA_FILE_EXT = ".media";
    
    public static String LOGIN_PAGE_PATH = "/login";
    public static String LOGOUT_PAGE_PATH = "/logout";
    
    public static String IGNORED_EXT = "jsp|ico|link|htm|html|class";
    public static String IGNORED_PATHS = "js|css|themes|services|actions|res|invoke";
    
    public static String FILE_UPLOAD_UPDATE_STATUS_CMD = "upload/updateStatus";
    public static String FILE_UPLOAD_CHECK_STATUS_CMD = "upload/checkStatus";
    
}
