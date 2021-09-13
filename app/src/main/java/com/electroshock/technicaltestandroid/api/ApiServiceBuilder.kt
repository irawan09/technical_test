package com.electroshock.technicaltestandroid.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitServiceFactory{
    fun createService(): DataService = Retrofit.Builder()
        .baseUrl("https://private-8ce77c-tmobiletest.apiary-mock.com/")
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build()
        .create(DataService::class.java)
}