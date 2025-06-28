package com.proyecto.petshopapp.ui.home

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddressViewModel : ViewModel() {

    private val _userAddresses = mutableStateListOf<String>()
    val userAddresses: List<String> = _userAddresses

    private val _selectedLocation = mutableStateOf("")
    val selectedLocation: State<String> = _selectedLocation

    init {
        loadUserAddresses()
        loadSelectedLocation()
    }

    private fun loadUserAddresses() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        Firebase.firestore.collection("users").document(uid)
            .get()
            .addOnSuccessListener { document ->
                val addresses = document.get("addresses") as? List<String>
                _userAddresses.clear()
                if (addresses != null) {
                    _userAddresses.addAll(addresses)
                }
            }
    }

    private fun loadSelectedLocation() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        Firebase.firestore.collection("users").document(uid)
            .get()
            .addOnSuccessListener { document ->
                val selected = document.getString("selectedLocation")
                if (!selected.isNullOrEmpty()) {
                    _selectedLocation.value = selected
                }
            }
    }
}
