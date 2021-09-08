package com.electroshock.technicaltestandroid

import retrofit2.Response
import retrofit2.http.GET

interface DataService{

    @GET("test/home")
    suspend fun getData(): Response<PagesModel>

}
