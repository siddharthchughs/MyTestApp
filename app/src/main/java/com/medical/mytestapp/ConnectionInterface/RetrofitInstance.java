package com.medical.mytestapp.ConnectionInterface;

import android.content.Context;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.medical.mytestapp.Utility.ConstantsGlobalKt.AUTHORIZATION_KEY;
import static com.medical.mytestapp.Utility.ConstantsGlobalKt.BASE_WEB_URL;
import static com.medical.mytestapp.Utility.ConstantsGlobalKt.CONTENT_TYPE;


/**
 * Created on : Feb 25, 2019
 * Author     : AndroidWave
 */

public class RetrofitInstance {

        private static Retrofit retrofit = null;
    private static OkHttpClient okHttpClient = null;

    public static ServicesUtility getApiService() {
        Context context = null;
        if (retrofit == null) {
            okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(2500, TimeUnit.SECONDS)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request newRequest = chain.request().newBuilder()
                                    .addHeader("Content-Type", CONTENT_TYPE)
                                    .addHeader("Authorization", AUTHORIZATION_KEY)
                                    .build();
                            return chain.proceed(newRequest);
                        }
                    }).build();
            retrofit = new Retrofit
                    .Builder()
                    .baseUrl(BASE_WEB_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ServicesUtility.class);
    }
}