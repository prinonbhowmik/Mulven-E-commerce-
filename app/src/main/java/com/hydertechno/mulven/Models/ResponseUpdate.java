package com.hydertechno.mulven.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResponseUpdate implements Serializable {
    @SerializedName("apk_version")
    @Expose
    private String apkVersion;
    private final static long serialVersionUID = -509266359367381896L;

    public String getApkVersion() {
        return apkVersion;
    }

    public void setApkVersion(String apkVersion) {
        this.apkVersion = apkVersion;
    }
}
