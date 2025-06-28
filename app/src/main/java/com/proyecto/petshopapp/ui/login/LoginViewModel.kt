
package com.proyecto.petshopapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.proyecto.petshopapp.data.models.LoginRequest
import com.proyecto.petshopapp.data.remote.AuthApiService
import com.proyecto.petshopapp.data.remote.NetworkClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch



data class LoginUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val userEmail: String? = null,
    val userType: String? = null
)

data class PasswordChangeState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

data class EmailChangeState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)


class LoginViewModel(
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val authApiService: AuthApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _passwordChangeState = MutableStateFlow(PasswordChangeState())
    val passwordChangeState: StateFlow<PasswordChangeState> = _passwordChangeState.asStateFlow()

    private val _emailChangeState = MutableStateFlow(EmailChangeState())
    val emailChangeState: StateFlow<EmailChangeState> = _emailChangeState.asStateFlow()

    fun login(email: String, password: String) {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    user?.let {
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
                        errorMessage = task.exception?.message ?: "Error en el login"
                    )
                }
            }
    }

    fun loginWithDummy(emailOrUsername: String, password: String) {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            try {
                val response = authApiService.login(
                    LoginRequest(
                        username = emailOrUsername,
                        password = password
                    )
                )

                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()!!
                    _uiState.value = LoginUiState(
                        isLoading = false,
                        isSuccess = true,
                        userEmail = data.email,
                        userType = "DummyUser"
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Login Dummy fallido: ${response.message()}"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error Dummy login: ${e.localizedMessage}"
                )
            }
        }
    }

    fun changePassword(currentPassword: String, newPassword: String) {
        val user = firebaseAuth.currentUser
        if (user?.email == null) {
            _passwordChangeState.value = PasswordChangeState(
                isLoading = false,
                isSuccess = false,
                errorMessage = "Usuario no autenticado"
            )
            return
        }

        _passwordChangeState.value = PasswordChangeState(isLoading = true)

        val credential = EmailAuthProvider.getCredential(user.email!!, currentPassword)

        user.reauthenticate(credential)
            .addOnCompleteListener { reauthTask ->
                if (reauthTask.isSuccessful) {
                    user.updatePassword(newPassword)
                        .addOnCompleteListener { updateTask ->
                            _passwordChangeState.value = if (updateTask.isSuccessful) {
                                PasswordChangeState(isLoading = false, isSuccess = true)
                            } else {
                                PasswordChangeState(
                                    isLoading = false,
                                    isSuccess = false,
                                    errorMessage = updateTask.exception?.message ?: "Error al cambiar contraseña"
                                )
                            }
                        }
                } else {
                    _passwordChangeState.value = PasswordChangeState(
                        isLoading = false,
                        isSuccess = false,
                        errorMessage = "Contraseña actual incorrecta"
                    )
                }
            }
    }

    fun clearPasswordChangeState() {
        _passwordChangeState.value = PasswordChangeState()
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

    fun observeFirebaseUser(onUserAvailable: (String) -> Unit) {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                onUserAvailable(user.uid)
            }
        }
        firebaseAuth.addAuthStateListener(listener)
    }
}


class LoginViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(
                firebaseAuth = FirebaseAuth.getInstance(),
                db = FirebaseFirestore.getInstance(),
                authApiService = NetworkClient.authApiService
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
