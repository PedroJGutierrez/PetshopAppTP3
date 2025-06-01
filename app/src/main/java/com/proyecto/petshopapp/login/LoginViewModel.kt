package com.proyecto.petshopapp.login

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class LoginUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val userEmail: String? = null,
    val userType: String? = null
)

class LoginViewModel : ViewModel() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    user?.let {
                        // 🔽 Esperamos a que se obtenga el userType ANTES de cambiar el estado a isSuccess
                        db.collection("users").document(user.uid).get()
                            .addOnSuccessListener { document ->
                                val userType = document.getString("userType") ?: "Usuario Final"
                                _uiState.value = LoginUiState(
                                    isLoading = false,
                                    isSuccess = true,
                                    userEmail = user.email,
                                    userType = userType
                                )
                            }
                            .addOnFailureListener {
                                _uiState.value = LoginUiState(
                                    isLoading = false,
                                    isSuccess = false,
                                    errorMessage = "Error al obtener tipo de usuario"
                                )
                            }
                    }
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = task.exception?.message ?: "Login failed."
                    )
                }
            }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
    fun fetchUserType(uid: String) {
        _uiState.value = _uiState.value.copy(isLoading = true)
        db.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                val userType = document.getString("userType") ?: "Usuario Final"

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    userType = userType,
                    isSuccess = true
                )
            }
            .addOnFailureListener {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error al obtener tipo de usuario"
                )
            }
    }
}
