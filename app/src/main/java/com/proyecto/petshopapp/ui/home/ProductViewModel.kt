package com.proyecto.petshopapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.petshopapp.data.models.Product
import com.proyecto.petshopapp.data.remote.NetworkClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> get() = _products

    init {
        fetchProducts()
    }

    private fun fetchProducts() {
        viewModelScope.launch {
            try {
                val response = NetworkClient.productApiService.getProducts()
                val apiProducts = response.products
                val customizedProducts = apiProducts.mapNotNull { product ->
                    when (product.id) {
                        1 -> product.copy(
                            title = "RC Kitten",
                            description = "ROYAL CANIN® Kitten is specially formulated to support the unique nutritional needs of growing kittens...",
                            thumbnail = "kitten",
                            category = "Food",
                            price = 20.99
                        )
                        2 -> product.copy(
                            title = "RC Persian",
                            description = "The Persian cat has the longest and densest coat of all cat breeds...",
                            thumbnail = "persian",
                            category = "Food",
                            price = 22.99
                        )
                        3 -> product.copy(
                            title = "Ball with rope",
                            description = "The rope ball dog toy features multiple textured surfaces and durable twisted fibers...",
                            thumbnail = "toy_perro",
                            category = "Toys",
                            price = 8.99
                        )
                        4 -> product.copy(
                            title = "Rod with fish",
                            description = "The fishing rod cat toy features a long flexible wand and dangling prey-like attachment...",
                            thumbnail = "toy_gato",
                            category = "Toys",
                            price = 10.99
                        )
                        5 -> product.copy(
                            title = "Dog harness",
                            description = "The dog harness features a chest-distributed pressure system and padded straps...",
                            thumbnail = "pretal_perro",
                            category = "Accessories",
                            price = 18.99
                        )
                        6 -> product.copy(
                            title = "Cat bun",
                            description = "The cat bow tie features an adjustable elastic collar and lightweight fabric design...",
                            thumbnail = "monioo_gato",
                            category = "Accessories",
                            price = 12.99
                        )
                        else -> null
                    }
                }

                _products.value = customizedProducts

            } catch (e: Exception) {
                e.printStackTrace()
                _products.value = getFallbackLocalProducts()
            }
        }
    }

    private fun getFallbackLocalProducts(): List<Product> {
        return listOf(
            Product(
                id = 1001,
                title = "RC Kitten",
                price = 20.99,
                thumbnail = "kitten",
                description = "ROYAL CANIN® Kitten is specially formulated to support the unique nutritional needs of growing kittens...",
                category = "Food"
            ),
            Product(
                id = 1002,
                title = "RC Persian",
                price = 22.99,
                thumbnail = "persian",
                description = "The Persian cat has the longest and densest coat of all cat breeds...",
                category = "Food"
            ),
            Product(
                id = 1003,
                title = "Ball with rope",
                price = 8.99,
                thumbnail = "toy_perro",
                description = "The rope ball dog toy features multiple textured surfaces and durable twisted fibers...",
                category = "Toys"
            ),
            Product(
                id = 1004,
                title = "Rod with fish",
                price = 10.99,
                thumbnail = "toy_gato",
                description = "The fishing rod cat toy features a long flexible wand and dangling prey-like attachment...",
                category = "Toys"
            ),
            Product(
                id = 1005,
                title = "Dog harness",
                price = 18.99,
                thumbnail = "pretal_perro",
                description = "The dog harness features a chest-distributed pressure system and padded straps...",
                category = "Accessories"
            ),
            Product(
                id = 1006,
                title = "Cat bun",
                price = 12.99,
                thumbnail = "monioo_gato",
                description = "The cat bow tie features an adjustable elastic collar and lightweight fabric design...",
                category = "Accessories"
            )
        )
    }
}
