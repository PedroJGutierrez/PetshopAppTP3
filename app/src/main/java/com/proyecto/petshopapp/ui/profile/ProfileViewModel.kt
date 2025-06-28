package com.proyecto.petshopapp.ui.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.proyecto.petshopapp.R
import com.proyecto.petshopapp.data.models.User
import dagger.hilt.android.internal.Contexts.getApplication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProfileViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    private val _profileImageRes = MutableStateFlow(R.drawable.foto1)
    val profileImageRes: StateFlow<Int> = _profileImageRes.asStateFlow()

    fun loadUserProfile() {
        val email = auth.currentUser?.email ?: return

        db.collection("users").document(email)
            .addSnapshotListener { snapshot, _ ->
                val userData = snapshot?.data ?: return@addSnapshotListener

                val username = userData["username"] as? String ?: ""
                val nombre = userData["nombre"] as? String ?: ""
                val firstName = userData["firstName"] as? String ?: ""
                val lastName = userData["lastName"] as? String ?: ""
                val gender = userData["gender"] as? String ?: ""
                val image = userData["image"] as? String ?: "foto1"
                val sales = (userData["sales"] as? Long)?.toInt() ?: 0
                val followers = (userData["followers"] as? Long)?.toInt() ?: 0
                val following = (userData["following"] as? Long)?.toInt() ?: 0

                _user.value = User(
                    id = 0,
                    username = username,
                    email = email,
                    firstName = firstName,
                    lastName = lastName,
                    gender = gender,
                    image = image,
                    sales = sales,
                    followers = followers,
                    nombre = nombre,
                            following = following
                )

                _profileImageRes.value = when (image) {
                    "foto1" -> R.drawable.foto1
                    "foto2" -> R.drawable.foto2
                    "foto3" -> R.drawable.foto3
                    "foto4" -> R.drawable.foto4
                    else -> R.drawable.foto1
                }
            }
    }

    fun updateProfileImage(imageName: String) {
        val email = auth.currentUser?.email ?: return

        viewModelScope.launch {
            try {
                val userDocRef = db.collection("users").document(email)

                val snapshot = userDocRef.get().await()
                if (snapshot.exists()) {
                    userDocRef.update("image", imageName).await()
                } else {
                    // Si no existe el documento, lo creamos con la imagen
                    userDocRef.set(mapOf("image" to imageName)).await()
                }

                _profileImageRes.value = when (imageName) {
                    "foto1" -> R.drawable.foto1
                    "foto2" -> R.drawable.foto2
                    "foto3" -> R.drawable.foto3
                    "foto4" -> R.drawable.foto4
                    else -> R.drawable.foto1
                }

            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error al actualizar imagen de perfil", e)
            }
        }
    }



}
