package com.proyecto.petshopapp.data.models

data class Product(
    val id: Int,
    val title: String,
    val price: Double,
    val thumbnail: String,
    val description: String,
    val discountPercentage: Double = 0.0,
    val rating: Double = 0.0,
    val stock: Int = 0,
    val brand: String = "",
    val category: String = "",
    val images: List<String> = emptyList()
)

