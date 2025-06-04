package com.proyecto.petshopapp.data.models
data class User(
    val id: Int,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val gender: String,
    val image: String,
    val sales: Int = 0,
    val followers: Int = 0,
    val following: Int = 0,
    val photo: String = ""
)