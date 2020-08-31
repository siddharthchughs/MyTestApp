package com.medical.mytestapp.ConnectionInterface

import android.content.Context
import com.medical.mycompanylocation.serviceinterface.ServiceInterface
import com.medical.mycompanylocation.utilityhelper.GlobalConstants.Companion.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created :: Siddharth
 */
object CompanyManager {
    private var retrofit: Retrofit? = null
    private var okHttpClient: OkHttpClient? = null

    val apiService: ServiceInterface
        get() {
            val context: Context? = null
            if (retrofit == null) {
                okHttpClient = OkHttpClient.Builder()
                        .readTimeout(50, TimeUnit.SECONDS)
                        .addInterceptor { chain ->
                            val newRequest = chain.request().newBuilder()
                                    .addHeader("Content-type", "application/json; charset=UTF-8")
                                    .build()
                            chain.proceed(newRequest)
                        }
                    .build()
                retrofit = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
            }
            return retrofit!!.create(ServiceInterface::class.java)
        }
}