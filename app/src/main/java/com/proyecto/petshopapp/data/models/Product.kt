package com.proyecto.petshopapp.data.models

data class Product(
    val id: Int = 0,
    val title: String = "",
    val price: Double = 0.0,
    val thumbnail: String = "",
    val description: String = "",
    val discountPercentage: Double = 0.0,
    val rating: Double = 0.0,
    val stock: Int = 0,
    val brand: String = "",
    val category: String = "",
    val images: List<String> = emptyList()
) {
    companion object {
        fun fromMap(map: Map<String, Any>): Product? {
            return try {
                Product(
                    id = (map["id"] as? Long)?.toInt() ?: 0,
                    title = map["title"] as? String ?: "",
                    price = (map["price"] as? Number)?.toDouble() ?: 0.0,
                    thumbnail = map["thumbnail"] as? String ?: "",
                    description = map["description"] as? String ?: "",
                    discountPercentage = (map["discountPercentage"] as? Number)?.toDouble() ?: 0.0,
                    rating = (map["rating"] as? Number)?.toDouble() ?: 0.0,
                    stock = (map["stock"] as? Long)?.toInt() ?: 0,
                    brand = map["brand"] as? String ?: "",
                    category = map["category"] as? String ?: "",
                    images = map["images"] as? List<String> ?: emptyList()
                )
            } catch (e: Exception) {
                null
            }
        }
    }
}

