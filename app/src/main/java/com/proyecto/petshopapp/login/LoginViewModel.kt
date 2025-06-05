package com.proyecto.petshopapp.login

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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

class LoginViewModel : ViewModel() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _passwordChangeState = MutableStateFlow(PasswordChangeState())
    val passwordChangeState: StateFlow<PasswordChangeState> = _passwordChangeState.asStateFlow()

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

    fun setDummySuccess(email: String, userType: String) {
        _uiState.value = LoginUiState(
            isLoading = false,
            isSuccess = true,
            userEmail = email,
            userType = userType
        )
    }

    fun setDummyError(message: String) {
        _uiState.value = LoginUiState(
            isLoading = false,
            isSuccess = false,
            errorMessage = message
        )
    }
    fun loginWithDummy(emailOrUsername: String, password: String) {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)


        kotlinx.coroutines.GlobalScope.launch {
            try {
                val response = com.proyecto.petshopapp.network.NetworkClient.authApiService.login(
                    com.proyecto.petshopapp.data.models.LoginRequest(
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
                        isSuccess = false,
                        errorMessage = "Dummy login failed: ${response.message()}"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSuccess = false,
                    errorMessage = "Dummy login error: ${e.localizedMessage}"
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
                            if (updateTask.isSuccessful) {
                                _passwordChangeState.value = PasswordChangeState(
                                    isLoading = false,
                                    isSuccess = true,
                                    errorMessage = null
                                )
                            } else {
                                _passwordChangeState.value = PasswordChangeState(
                                    isLoading = false,
                                    isSuccess = false,
                                    errorMessage = updateTask.exception?.message ?: "Error al cambiar la contraseña"
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

    fun observeFirebaseUser(onUserAvailable: (String) -> Unit) {
        val auth = FirebaseAuth.getInstance()
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                onUserAvailable(user.uid)
            }
        }
        auth.addAuthStateListener(listener)
    }
    data class EmailChangeState(
        val isLoading: Boolean = false,
        val isSuccess: Boolean = false,
        val errorMessage: String? = null
    )

    private val _emailChangeState = MutableStateFlow(EmailChangeState())
    val emailChangeState: StateFlow<EmailChangeState> = _emailChangeState.asStateFlow()

    fun changeEmail(currentPassword: String, newEmail: String) {
        val user = firebaseAuth.currentUser
        if (user?.email == null) {
            _emailChangeState.value = EmailChangeState(
                isLoading = false,
                isSuccess = false,
                errorMessage = "Usuario no autenticado"
            )
            return
        }

        _emailChangeState.value = EmailChangeState(isLoading = true)

        val credential = EmailAuthProvider.getCredential(user.email!!, currentPassword)

        user.reauthenticate(credential)
            .addOnCompleteListener { reauthTask ->
                if (reauthTask.isSuccessful) {
                    user.updateEmail(newEmail)
                        .addOnSuccessListener {
                            db.collection("users").document(user.uid)
                                .update("email", newEmail)
                                .addOnSuccessListener {
                                    _emailChangeState.value = EmailChangeState(
                                        isLoading = false,
                                        isSuccess = true
                                    )
                                }
                                .addOnFailureListener {
                                    _emailChangeState.value = EmailChangeState(
                                        isLoading = false,
                                        isSuccess = false,
                                        errorMessage = "Email actualizado en Auth pero no en Firestore"
                                    )
                                }
                        }
                        .addOnFailureListener {
                            _emailChangeState.value = EmailChangeState(
                                isLoading = false,
                                isSuccess = false,
                                errorMessage = it.localizedMessage ?: "Error al actualizar email"
                            )
                        }
                } else {
                    _emailChangeState.value = EmailChangeState(
                        isLoading = false,
                        isSuccess = false,
                        errorMessage = "Contraseña incorrecta"
                    )
                }
            }
    }
    fun clearEmailChangeState() {
        _emailChangeState.value = EmailChangeState()
    }
}