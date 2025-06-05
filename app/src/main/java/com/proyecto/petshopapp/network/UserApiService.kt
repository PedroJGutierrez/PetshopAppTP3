package com.proyecto.petshopapp.network

import com.proyecto.petshopapp.data.models.UserResponse
import retrofit2.http.GET

interface UserApiService {
    @GET("users")
    suspend fun getUsers(): UserResponse
}
