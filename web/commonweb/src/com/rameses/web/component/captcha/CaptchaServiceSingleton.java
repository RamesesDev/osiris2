
package com.rameses.web.component.captcha;

import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;
import com.octo.captcha.service.image.ImageCaptchaService;

public class CaptchaServiceSingleton {

    private static ImageCaptchaService instance = new DefaultManageableImageCaptchaService();
    
    public CaptchaServiceSingleton() {                  
    }

    public static ImageCaptchaService getInstance() {
        return instance;
    }
    
}
