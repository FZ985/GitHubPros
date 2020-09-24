package com.whatsapp.share.natives;

import java.io.File;

public class WhatsShareContent {
    private String text;
    private String image;
    private File file;
    private boolean asImage;

    public WhatsShareContent(String image) {
        this.image = image;
        this.asImage = true;
    }

    public WhatsShareContent(File file) {
        this.file = file;
        this.asImage = true;
    }

    public WhatsShareContent() {
        this.asImage = false;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.asImage = false;
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.asImage = true;
        this.image = image;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.asImage = true;
        this.file = file;
    }

    public boolean isAsImage() {
        return asImage;
    }
}
