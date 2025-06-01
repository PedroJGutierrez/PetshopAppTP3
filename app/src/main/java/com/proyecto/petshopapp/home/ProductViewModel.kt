package com.proyecto.petshopapp.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.petshopapp.data.models.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> get() = _products

    init {
        loadCustomProducts()
    }

    private fun loadCustomProducts() {
        viewModelScope.launch {
            _products.value = listOf(
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
                    thumbnail = "moño-gato",
                    description = "The cat bow tie features an adjustable elastic collar and lightweight fabric design...",
                    category = "Accessories"
                )
            )
        }
    }
}
