package com.musicstream.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.musicstream.rest.service.ApiService;
import com.musicstream.utils.Constants;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Serhii Slobodyanuk on 30.03.2016.
 */
public class RestClient {
    private static OkHttpClient client;
    private static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
            .create();

    static {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
    }

    private static final Retrofit mRestAdapter = new Retrofit.Builder()
            .baseUrl(Constants.BASE_API_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build();

    private static final ApiService mApiService = mRestAdapter.create(ApiService.class);

    public static ApiService getApiService() {
        return mApiService;
    }
}
