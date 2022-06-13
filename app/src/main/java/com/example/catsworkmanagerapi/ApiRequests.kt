package com.example.catsworkmanagerapi

import com.example.catsworkmanagerapi.api.CatJson
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET

interface ApiRequests {
    @GET("/facts/random")
    suspend fun getCatFacts(): Response<CatJson>
}