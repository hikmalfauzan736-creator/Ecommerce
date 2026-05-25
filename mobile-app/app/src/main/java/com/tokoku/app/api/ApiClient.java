package com.tokoku.app.api;

import android.content.Context;
import android.content.SharedPreferences;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    // Emulator  → 10.0.2.2
    // HP fisik  → ganti dengan IP komputer kamu (cek via ipconfig)
    private static final String BASE_URL = "http://192.168.100.9:8000/api/";

    public static Retrofit getClient(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("tokoku_prefs", Context.MODE_PRIVATE);
        String token = prefs.getString("token", "");

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(chain -> {
                    Request.Builder builder = chain.request().newBuilder();
                    if (!token.isEmpty()) {
                        builder.header("Authorization", "Bearer " + token);
                    }
                    return chain.proceed(builder.build());
                })
                .build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}