package com.thesua7.wq

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface MyApiService {
    @POST("/predict")
    fun postData(@Body params: HashMap<String,String>): Call<apiResponse>
}