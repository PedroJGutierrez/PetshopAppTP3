package com.proyecto.petshopapp.data.models

data class LoginRequest(
    val username: String,
    val password: String,
    val expiresInMins: Int = 30
)