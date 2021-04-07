package com.hydertechno.mulven.Models;

public class OrderTimelineModel {
   private  int id;
   private String order_id;
   private String status;
   private String commants;
   private String date;
   private String time;

    public OrderTimelineModel(int id, String order_id, String status, String commants, String date, String time) {
        this.id = id;
        this.order_id = order_id;
        this.status = status;
        this.commants = commants;
        this.date = date;
        this.time = time;
    }

    public OrderTimelineModel() {
    }

    public int getId() {
        return id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public String getStatus() {
        return status;
    }

    public String getCommants() {
        return commants;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
