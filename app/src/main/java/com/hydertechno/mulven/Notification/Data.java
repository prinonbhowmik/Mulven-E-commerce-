package com.hydertechno.mulven.Notification;

public class Data {
    private String title;
    private String body;
    private String image;
    private String toActivity;
    private String sent;

    public Data(String title, String body, String image, String toActivity, String sent) {
        this.title = title;
        this.body = body;
        this.image = image;
        this.toActivity = toActivity;
        this.sent = sent;
    }

    public Data() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getToActivity() {
        return toActivity;
    }

    public void setToActivity(String toActivity) {
        this.toActivity = toActivity;
    }

    public String getSent() {
        return sent;
    }

    public void setSent(String sent) {
        this.sent = sent;
    }
}
