package com.electroshock.technicaltestandroid.api

import com.electroshock.technicaltestandroid.MainActivity
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitServiceFactory{

    val cacheSize = (5 * 1024 * 1024).toLong()
    val myCache = Cache(MainActivity.getContext().cacheDir, cacheSize)

    val okHttpClient = OkHttpClient.Builder()
        .cache(myCache)
        .addInterceptor { chain ->
            var request = chain.request()
            request = if (MainActivity.hasNetwork(MainActivity.getContext())!!)
                request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
            else
                request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build()
            chain.proceed(request)
        }
        .build()
    fun createService(): DataService = Retrofit.Builder()
        .baseUrl("https://private-8ce77c-tmobiletest.apiary-mock.com/")
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .client(okHttpClient)
        .build()
        .create(DataService::class.java)
}
