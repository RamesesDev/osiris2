
package com.rameses.web.common;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ByteArrayImageCropper implements ImageCropper {
    
    private int clipX;
    private int clipY;
    private int clipWidth;
    private int clipHeight;
    private byte[] sourceImage;
    private String fileType;
    private String contentType;
    
    public ByteArrayImageCropper() {
        setClip(0, 0, 1, 1);
    }

    public byte[] crop() throws IOException {
        BufferedImage image = ImageIO.read( new ByteArrayInputStream( sourceImage ) );
        BufferedImage cropped = image.getSubimage(clipX, clipY, clipWidth, clipHeight);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(cropped, fileType , baos);
        return baos.toByteArray();        
    }

    public void setClip(int x, int y, int width, int height) {
        clipX = x;
        clipY = y;
        clipWidth = width;
        clipHeight = height;
    }
    
    public int getClipX() {
        return clipX;
    }

    public void setClipX(int clipX) {
        this.clipX = clipX;
    }

    public int getClipY() {
        return clipY;
    }

    public void setClipY(int clipY) {
        this.clipY = clipY;
    }

    public int getClipWidth() {
        return clipWidth;
    }

    public void setClipWidth(int clipWidth) {
        this.clipWidth = clipWidth;
    }

    public int getClipHeight() {
        return clipHeight;
    }

    public void setClipHeight(int clipHeight) {
        this.clipHeight = clipHeight;
    }

    public byte[] getSourceImage() {
        return sourceImage;
    }

    public void setSourceImage(byte[] sourceImage) {
        this.sourceImage = sourceImage;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    
}
