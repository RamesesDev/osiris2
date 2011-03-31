package com.rameses.web.component.tag;

import com.rameses.web.component.captcha.UICaptcha;
import com.rameses.web.component.chart.UIChart;
import com.rameses.web.component.image.UIGraphicImage;
import com.rameses.web.component.fileupload.UIFileUpload;
import com.rameses.web.component.imagecropper.UIImageCropper;
import com.rameses.web.component.progressbar.UIProgressbar;
import com.rameses.web.component.richtext.UIRichText;
import com.rameses.web.component.select.UISelectItems;
import com.rameses.web.component.simpledate.UISimpleDate;
import com.rameses.web.component.select.UIBeanList;
import com.rameses.web.component.select.UIEnumList;
import com.rameses.web.component.outputlink.UIOutputLink;
import com.rameses.web.component.resource.UIJQueryResource;
import com.rameses.web.component.suggest.UIAjaxSuggest;
import com.rameses.web.taghandler.CompositeControlHandler;
import com.sun.facelets.tag.jsf.html.AbstractHtmlLibrary;

public class RamesesTagLibrary extends AbstractHtmlLibrary {
    
    public static final String NAME_SPACE = "http://com.rameses.web.component";
    public static final RamesesTagLibrary INSTANCE = new RamesesTagLibrary();
    public static final String TAG_DIR = "META-INF/composition-tags/";
    
    
    public RamesesTagLibrary() {
        super(NAME_SPACE);
        addHtmlComponent("simpleDate", UISimpleDate.class.getName(), null);
        addComponent("beanList", UIBeanList.class.getName(), null);
        addComponent("enumList", UIEnumList.class.getName(), null);
        addComponent("selectItems", UISelectItems.class.getName(), null);
        addHtmlComponent("graphicImage", UIGraphicImage.class.getName(), null);
        addHtmlComponent("outputLink", UIOutputLink.class.getName(), null);
        addHtmlComponent("captcha", UICaptcha.class.getName(), null);
        
        //html components from jquery.jar library
        addHtmlComponent("loadresource", UIJQueryResource.class.getName(), null);
        addHtmlComponent("fileupload", UIFileUpload.class.getName(), null);
        addHtmlComponent("richtext", UIRichText.class.getName(), null);
        addHtmlComponent("progressBar", UIProgressbar.class.getName(), null);
        addHtmlComponent("imageCropper", UIImageCropper.class.getName(), null);
        addHtmlComponent("suggest", UIAjaxSuggest.class.getName(), null);
        
        addHtmlComponent("chart", UIChart.class.getName(), null);
        
        //tag handlers
        addTagHandler("compositeControl", CompositeControlHandler.class);
        
        //composition tags
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        addUserTag("calendar", loader.getResource(TAG_DIR + "calendar.xhtml"));
        addUserTag("datepicker", loader.getResource(TAG_DIR + "datepicker.xhtml"));
        addUserTag("imageViewer", loader.getResource(TAG_DIR + "imageViewer.xhtml"));
        addUserTag("ajaxFileupload", loader.getResource(TAG_DIR + "fileupload.xhtml"));
        addUserTag("popup", loader.getResource(TAG_DIR + "popup.xhtml"));
        addUserTag("titledPanel", loader.getResource(TAG_DIR + "titledpanel.xhtml"));
    }
}
