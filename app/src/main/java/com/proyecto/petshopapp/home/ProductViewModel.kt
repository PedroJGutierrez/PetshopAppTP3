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
                    description = "ROYAL CANIN® Kitten is specially formulated to support the unique nutritional needs of growing kittens. During this crucial stage of development, kittens need a diet rich in nutrients to help build strong immune defenses, support healthy growth, and promote digestive health. That’s why ROYAL CANIN® Kitten contains an exclusive blend of antioxidants, highly digestible proteins, and essential vitamins and minerals to support a kitten’s natural defenses, optimal development, and overall well-being."
                ),
                Product(
                    id = 1002,
                    title = "RC Persian",
                    price = 22.99,
                    thumbnail = "persian",
                    description = "The Persian cat has the longest and densest coat of all cat breeds. This means that it typically needs to consume more skin-health focused nutrients than other cat breeds. That’s why ROYAL CANIN® Persian Adult contains an exclusive complex of nutrients to help the skin’s barrier defence role to maintain good skin and coat health."
                )
            )
        }
    }
}
