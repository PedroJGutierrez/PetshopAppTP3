package com.petlovers.petshopapp.ui.sellerProfile

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.petlovers.petshopapp.model.User

class SellerProfileViewModel : ViewModel() {
    private val _user = MutableStateFlow(User("Abduldul", 80, 109, 992))
    val user: StateFlow<User> = _user
}