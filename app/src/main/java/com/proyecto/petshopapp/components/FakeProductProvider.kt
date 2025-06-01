package com.proyecto.petshopapp.components

import com.proyecto.petshopapp.data.models.Product

val fakeProducts = listOf(
    Product(
        id = 1,
        title = "RC Kitten",
        description = "Alimento para gatitos",
        price = 20.99,
        discountPercentage = 10.0,
        rating = 4.5,
        stock = 30,
        brand = "Royal Canin",
        category = "Food",
        thumbnail = "kitten",
        images = listOf()
    ),
    Product(
        id = 2,
        title = "RC Persian",
        description = "Alimento para gatos persas",
        price = 22.99,
        discountPercentage = 15.0,
        rating = 4.8,
        stock = 20,
        brand = "Royal Canin",
        category = "Food",
        thumbnail = "persian",
        images = listOf()
    )
)
