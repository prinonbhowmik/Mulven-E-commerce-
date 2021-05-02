package com.hydertechno.mulven.Models;

public class CampaignModel {
    private int id;
    private String campaign_name;
    private String campaign_image;
    private String start_date;
    private String start_time;
    private int publication_status;

    public CampaignModel(int id, String campaign_name, String campaign_image, String start_date, String start_time, int publication_status) {
        this.id = id;
        this.campaign_name = campaign_name;
        this.campaign_image = campaign_image;
        this.start_date = start_date;
        this.start_time = start_time;
        this.publication_status = publication_status;
    }

    public CampaignModel() {
    }

    public int getId() {
        return id;
    }

    public String getCampaign_name() {
        return campaign_name;
    }

    public String getCampaign_image() {
        return campaign_image;
    }

    public String getStart_date() {
        return start_date;
    }

    public String getStart_time() {
        return start_time;
    }

    public int getPublication_status() {
        return publication_status;
    }
}
