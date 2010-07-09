
package com.rameses.web.component.captcha;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.faces.context.ExternalContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CaptchaPhaseListener implements PhaseListener{
    
    public static final String CAPTCHA_PARAM = "_captchaImage";
    
    public CaptchaPhaseListener() {
    }
    
    public void afterPhase(PhaseEvent phaseEvent) {
        //no op
    }
    
    public void beforePhase(PhaseEvent event) {
        ExternalContext ec = event.getFacesContext().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) ec.getRequest();
        HttpServletResponse response = (HttpServletResponse) ec.getResponse();
        if(request.getParameter(CAPTCHA_PARAM) != null){
            ByteArrayOutputStream imgOutputStream = new ByteArrayOutputStream();
            byte[] captchaBytes = null;
            try{
                String captchaId = request.getSession().getId();
                BufferedImage challengeImage = CaptchaServiceSingleton.getInstance().getImageChallengeForID( captchaId, request.getLocale() );
                ImageIO.write(challengeImage, "jpg",imgOutputStream);
                captchaBytes = imgOutputStream.toByteArray();
                request.getSession().removeAttribute( "PassedCaptcha" );
            }catch (Exception ex){
                ex.printStackTrace();
            }
            
            response.setHeader( "Cache-Control", "no-store" );
            response.setHeader( "Pragma", "no-cache" );
            response.setDateHeader( "Expires", 0 );
            response.setContentType("image/jpg");
            OutputStream out = null;
            try {
                out = response.getOutputStream();
                out.write(captchaBytes);
                event.getFacesContext().responseComplete();
            } catch (IOException ex) {                
                ex.printStackTrace();
            } finally {
                try {out.flush();} catch(Exception ign){;}
                try {out.close();} catch(Exception ign){;}
            }
        }
        
    }
    
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }
    
}
