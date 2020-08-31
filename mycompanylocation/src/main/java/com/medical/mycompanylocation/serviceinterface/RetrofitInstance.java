package com.medical.mycompanylocation.serviceinterface;

import android.content.Context;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

import static com.medical.mycompanylocation.utilityhelper.GlobalConstants.BASE_URL;
import static com.medical.mycompanylocation.utilityhelper.GlobalConstants.MONSTER_WEB_URL;


/**
 * Created :: Siddharth
 */

public class RetrofitInstance {

        private static Retrofit retrofit = null;
    private static OkHttpClient okHttpClient = null;


    public static ServiceInterface getCompanyInfoService() {
        if (retrofit == null) {
            okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(10, TimeUnit.SECONDS)
                    .connectTimeout(1200, TimeUnit.SECONDS).build();
            retrofit = new Retrofit
                    .Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build();
        }
        return retrofit.create(ServiceInterface.class);
    }
}