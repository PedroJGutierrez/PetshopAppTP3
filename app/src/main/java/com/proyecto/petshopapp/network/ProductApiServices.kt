package com.proyecto.petshopapp.network

import com.proyecto.petshopapp.data.models.ProductResponse
import retrofit2.http.GET

interface ProductApiService {
    @GET("products")
    suspend fun getProducts(): ProductResponse
}