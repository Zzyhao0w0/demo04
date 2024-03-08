package com.example.demo04.http

import com.example.demo04.common.MUtil
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object HUtils {
    private val _url: String = "https://testapi.bidier.cn/"

    private var _retrofit: Retrofit

    init {
        val interceptor = Interceptor { chain ->
            val request = chain.request()
            val requestBuilder = request.newBuilder()
            val url = request.url()
            val builder = url.newBuilder()
            requestBuilder.url(builder.build())
                .method(request.method(), request.body())
                .addHeader("Time", (System.currentTimeMillis() / 1000).toString())
                .addHeader("Dr", "adU_mb0uQ1T3qKFdt64bL6NeSfdFgxXK.7YzAg5Ocp3Wr")
                .addHeader("Os","android")
                .addHeader("token","${MUtil.kv.getString("short_token","")}")
                .addHeader("Content-Type", "application/json")
            chain.proceed(requestBuilder.build())
        }
        _retrofit = Retrofit.Builder()
            .baseUrl(_url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(false)
                    .build()
            )
            .build()
    }

    fun getRetrofit(): Retrofit {
        return _retrofit
    }
}