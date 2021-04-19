package com.hydertechno.mulven.Models;

public class ChangePasswordModel {
    private String token;
    private String old_pass;
    private String new_psss;
    private String con_pass;
    private int status;
    private String message;


    public ChangePasswordModel(String old_pass, String new_psss, String con_pass, String token, int status, String message) {
        this.old_pass = old_pass;
        this.new_psss = new_psss;
        this.con_pass = con_pass;
        this.token = token;
        this.status = status;
        this.message = message;
    }

    public ChangePasswordModel(String token, String old_pass, String new_psss, String con_pass) {
        this.token = token;
        this.old_pass = old_pass;
        this.new_psss = new_psss;
        this.con_pass = con_pass;
    }

    public ChangePasswordModel() {
    }

    public String getOld_pass() {
        return old_pass;
    }

    public void setOld_pass(String old_pass) {
        this.old_pass = old_pass;
    }

    public String getNew_pass() {
        return new_psss;
    }

    public void setNew_pass(String new_pass) {
        this.new_psss = new_pass;
    }

    public String getCon_pass() {
        return con_pass;
    }

    public void setCon_pass(String con_pass) {
        this.con_pass = con_pass;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
