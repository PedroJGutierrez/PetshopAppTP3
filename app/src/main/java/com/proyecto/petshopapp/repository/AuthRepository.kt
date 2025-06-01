package com.proyecto.petshopapp.repository

import com.proyecto.petshopapp.data.models.LoginRequest
import com.proyecto.petshopapp.data.models.LoginResponse
import com.proyecto.petshopapp.network.NetworkClient

class AuthRepository {
    private val authApiService = NetworkClient.authApiService

    suspend fun login(username: String, password: String): Result<LoginResponse> {
        return try {
            val loginRequest = LoginRequest(username, password)
            val response = authApiService.login(loginRequest)

            if (response.isSuccessful) {
                response.body()?.let { loginResponse ->
                    Result.success(loginResponse)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Login failed: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}