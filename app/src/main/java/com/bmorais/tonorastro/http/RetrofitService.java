package com.bmorais.tonorastro.http;

import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetrofitService {

    /*@Headers({"Content-Type: application/json"})
    @POST("/api/toaqui-lite/app.php/registrardispositivo")
    Call<JsonObject> insertPushNotification(@Body Map<String, String> versionId);*/

    @Headers({"Content-Type: application/json"})
    @POST("/app/tonorastro/v1/rastreamento")
    Call<JsonObject> postRastreamento(@Body String body);

    @Headers({"Content-Type: application/json"})
    @GET("/app/tonorastro/v1/rastreamento/{objeto}")
    Call<JsonObject> getRastreamento(@Path("objeto") String cep);

}
