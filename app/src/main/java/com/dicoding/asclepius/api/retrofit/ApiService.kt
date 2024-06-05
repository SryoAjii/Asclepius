package com.dicoding.asclepius.api.retrofit

import com.dicoding.asclepius.api.response.HealthNewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {
    @GET("v2/top-headlines?q=cancer&category=health&language=en")
    fun getApiKey (
        @Query("apiKey") query: String
    ): Call<HealthNewsResponse>
}