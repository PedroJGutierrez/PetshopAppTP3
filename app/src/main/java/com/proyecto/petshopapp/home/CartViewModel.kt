package com.proyecto.petshopapp.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.proyecto.petshopapp.local.CartDao
import com.proyecto.petshopapp.local.CartItemEntity
import com.proyecto.petshopapp.data.models.Product
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CartViewModel(private val cartDao: CartDao) : ViewModel() {

    val cartItems: StateFlow<List<CartItemEntity>> =
        cartDao.getCartItems().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addToCart(product: Product, quantity: Int = 1) {
        val item = CartItemEntity(
            productId = product.id,
            title = product.title,
            price = product.price,
            thumbnail = product.thumbnail,
            quantity = quantity
        )
        viewModelScope.launch {
            cartDao.insertItem(item)
        }
    }

    fun removeFromCart(item: CartItemEntity) {
        viewModelScope.launch {
            cartDao.deleteItem(item)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            cartDao.clearCart()
        }
    } companion object {
        fun Factory(cartDao: CartDao) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return CartViewModel(cartDao) as T
            }
        }
    }
}