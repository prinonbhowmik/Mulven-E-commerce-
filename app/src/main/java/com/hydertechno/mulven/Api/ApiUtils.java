package com.hydertechno.mulven.Api;

public class ApiUtils {
    public static final String BASE_URL = "https://mulven.com/api/";

    public static ApiInterface getUserService(){
        return RetrofitClient.getClient(BASE_URL).create(ApiInterface.class);
    }
}
