package com.proyecto.petshopapp.network

import com.proyecto.petshopapp.data.models.LoginRequest
import com.proyecto.petshopapp.data.models.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>
}
