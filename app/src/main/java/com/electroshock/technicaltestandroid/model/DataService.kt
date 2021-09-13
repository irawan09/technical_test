package com.electroshock.technicaltestandroid.model

import com.electroshock.technicaltestandroid.PagesModel
import retrofit2.Response
import retrofit2.http.GET

interface DataService{

    @GET("test/home")
    suspend fun getData(): Response<PagesModel>

}
