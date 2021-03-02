package com.hydertechno.mulven.Models;

public class Sliderimage {
    private int id;
    private String post_type;
    private String title;
    private String slider_link;
    private String slider_image;
    private int status;

    public Sliderimage(int id, String post_type, String title, String slider_link, String slider_image, int status) {
        this.id = id;
        this.post_type = post_type;
        this.title = title;
        this.slider_link = slider_link;
        this.slider_image = slider_image;
        this.status = status;
    }

    public Sliderimage() {
    }

    public int getId() {
        return id;
    }

    public String getPost_type() {
        return post_type;
    }

    public String getTitle() {
        return title;
    }

    public String getSlider_link() {
        return slider_link;
    }

    public String getSlider_image() {
        return slider_image;
    }

    public int getStatus() {
        return status;
    }
}
