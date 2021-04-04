package com.hydertechno.mulven.Models;

public class UserProfile {
    private int id;
    private String full_name;
    private String phone;
    private String email;
    private String birthday;
    private String status;
    private String address;
    private String updated_at;
    private String created_at;
    private String token;

    public UserProfile(int id, String full_name, String phone, String email, String birthday, String status, String address, String updated_at, String created_at, String token) {
        this.id = id;
        this.full_name = full_name;
        this.phone = phone;
        this.email = email;
        this.birthday = birthday;
        this.status = status;
        this.address = address;
        this.updated_at = updated_at;
        this.created_at = created_at;
        this.token = token;
    }

    public UserProfile() {
    }

    public int getId() {
        return id;
    }

    public String getFull_name() {
        return full_name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getStatus() {
        return status;
    }

    public String getAddress() {
        return address;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getToken() {
        return token;
    }
}
