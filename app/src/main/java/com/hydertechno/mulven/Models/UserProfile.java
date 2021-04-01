package com.hydertechno.mulven.Models;

public class UserProfile {
    private int id;
    private String full_name;
    private String phone;
    private String email;
    private String birthday;
    private int status;
    private String address;
    private String created_at;
    private String access_token;
    private String message;

    public UserProfile(int id, String full_name, String phone, String email, String birthday, int status, String address, String created_at, String access_token, String message) {
        this.id = id;
        this.full_name = full_name;
        this.phone = phone;
        this.email = email;
        this.birthday = birthday;
        this.status = status;
        this.address = address;
        this.created_at = created_at;
        this.access_token = access_token;
        this.message = message;
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

    public int getStatus() {
        return status;
    }

    public String getAddress() {
        return address;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getAccess_token() {
        return access_token;
    }

    public String getMessage() {
        return message;
    }
}
