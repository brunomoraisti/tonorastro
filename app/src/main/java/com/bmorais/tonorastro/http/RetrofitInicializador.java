package com.bmorais.tonorastro.http;

import android.util.Log;

import com.bmorais.tonorastro.lib.Variaveis;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitInicializador {

    private final Retrofit retrofit;

    public RetrofitInicializador(){

        // INTERCEPTADOR DE LOG DO RETROFIT
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(new Interceptor() {
                    @NotNull
                    @Override
                    public Response intercept(@NotNull Chain chain) throws IOException {
                        Request request=chain.request().newBuilder()
                                .addHeader("Authorization", "Bearer " + getTokenJWT(null))
                                .build();
                        return chain.proceed(request);
                    }
                })
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        // INICIALIZADOR DO RETROFIT
        retrofit = new Retrofit.Builder().baseUrl("https://bmorais.com")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    public RetrofitService getRetrofitService(){
        return retrofit.create(RetrofitService.class);
    }


    public static String getTokenJWT(String data) {

        String stringJwt = "";
        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + 60000 * 60); // 1 hora

        try {
            stringJwt = Jwts.builder()
                    .signWith(SignatureAlgorithm.HS256, Variaveis.KEY_JWT.getBytes())
                    .setIssuer("bmorais.com")
                    .setSubject("Autentication access")
                    .setId("user")
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(expirationDate)
                    .claim("data", data)
                    .compact();
        } catch (JwtException ex){
            Log.e("### JWT: ",ex.getMessage());
        }
        return stringJwt;
    }
}
