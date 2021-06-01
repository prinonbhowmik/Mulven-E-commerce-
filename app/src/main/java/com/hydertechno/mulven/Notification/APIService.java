package com.hydertechno.mulven.Notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA1PrBK7s:APA91bE4somucThuKNci-qrx6yUlGJwjcsOm9mrWU9vW6mQnXwBysIX8-IpbsW62Z47jHgcsRvg_Ubfqk59GANL__diWEiLsCFzEihMMIS6-7XhQ2-EJxjuD5mSBiocEZVCNA2Nnsr5I"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
