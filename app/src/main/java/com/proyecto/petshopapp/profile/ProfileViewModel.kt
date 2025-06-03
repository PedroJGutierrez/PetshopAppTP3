package com.proyecto.petshopapp.ui.profile
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.petshopapp.data.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    fun loadUserProfile(userId: String) {
        viewModelScope.launch {
            try {
                _user.value = User(
                    id = 1,
                    username = "juan",
                    email = "juan@petshop.com",
                    firstName = "Juan",
                    lastName = "Frick",
                    gender = "male",
                    image = "",
                    sales = 23,
                    followers = 180,
                    following = 12
                )
            } catch (e: Exception) {
                _user.value = null
            }
        }
    }
}