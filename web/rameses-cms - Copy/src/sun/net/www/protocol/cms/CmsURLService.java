/*
 * CmsFileService.java
 *
 * Created on June 14, 2012, 9:03 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package sun.net.www.protocol.cms;

/**
 *
 * @author Elmo
 */
public interface CmsURLService {
    byte[] getContent(String path);
}
