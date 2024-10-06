package com.anthojb.simple.apirestpruebatecnica.datasource

import com.anthojb.simple.apirestpruebatecnica.model.ApiResponse
import retrofit2.http.GET

interface RestDataSource {
    @GET("products")
    suspend fun getProducts(): ApiResponse
}
 