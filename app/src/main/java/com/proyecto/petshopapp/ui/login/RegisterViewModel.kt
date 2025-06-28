package com.proyecto.petshopapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegisterUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun createAccount(username: String, email: String, password: String, onSuccess: () -> Unit) {
        _uiState.value = RegisterUiState(isLoading = true)

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val user = result.user
                if (user == null) {
                    _uiState.value = RegisterUiState(isLoading = false, errorMessage = "Error creando usuario")
                    return@addOnSuccessListener
                }

                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(username)
                    .build()

                user.updateProfile(profileUpdates)
                    .addOnSuccessListener {
                        val userData = hashMapOf(
                            "uid" to user.uid,
                            "username" to username,
                            "email" to email,
                            "userType" to "Reseller"
                        )

                        db.collection("users").document(user.uid)
                            .set(userData)
                            .addOnSuccessListener {
                                _uiState.value = RegisterUiState(isSuccess = true)
                                onSuccess()
                            }
                            .addOnFailureListener {
                                _uiState.value = RegisterUiState(isLoading = false, errorMessage = "Error al guardar en Firestore")
                            }
                    }
                    .addOnFailureListener {
                        _uiState.value = RegisterUiState(isLoading = false, errorMessage = "Error actualizando perfil")
                    }

            }
            .addOnFailureListener {
                _uiState.value = RegisterUiState(isLoading = false, errorMessage = it.message ?: "Error desconocido")
            }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
