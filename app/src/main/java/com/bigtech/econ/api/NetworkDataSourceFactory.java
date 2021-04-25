package com.bigtech.econ.api;

import com.bigtech.econ.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkDataSourceFactory {

    public static ForecastApiInterface getNetworkDataSource() {
        return new Retrofit.Builder()
                .callFactory(request -> getHttpClient().newCall(request))
                .baseUrl("http://bigtech.synology.me:8880")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ForecastApiInterface.class);
    }

    private static OkHttpClient getHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .pingInterval(2, TimeUnit.SECONDS);
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }
        return builder.build();
    }
}
