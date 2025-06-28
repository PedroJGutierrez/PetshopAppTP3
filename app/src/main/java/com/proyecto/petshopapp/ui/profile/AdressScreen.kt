package com.proyecto.petshopapp.ui.profile


import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.proyecto.petshopapp.ui.login.LoginViewModel
import com.proyecto.petshopapp.data.remote.NetworkClient
import com.proyecto.petshopapp.ui.theme.poppinsFont
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AddressScreen(
    onBack: () -> Unit,
    loginViewModel: LoginViewModel
) {
    val context = LocalContext.current
    val uid = FirebaseAuth.getInstance().currentUser?.uid
    val coroutineScope = rememberCoroutineScope()
    val db = Firebase.firestore

    var apiAddresses by remember { mutableStateOf<List<String>>(emptyList()) }
    var userAddresses by remember { mutableStateOf<List<String>>(emptyList()) }
    var selectedAddress by remember { mutableStateOf<String?>(null) }
    var expanded by remember { mutableStateOf(false) }

    // Cargar direcciones del usuario desde Firestore
    LaunchedEffect(uid) {
        if (uid != null) {
            db.collection("users").document(uid).get().addOnSuccessListener { doc ->
                val list = doc.get("addresses") as? List<String> ?: emptyList()
                userAddresses = list
            }
        }
    }

    // Cargar direcciones de la API
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val response = NetworkClient.userApiService.getUsers()
                apiAddresses = response.users.mapNotNull { user ->
                    user.address?.let {
                        "${it.address}, ${it.city}, ${it.state}, ${it.country}"
                    }
                }
            } catch (e: Exception) {
                apiAddresses = listOf("Error loading addresses")
            }
        }
    }

    fun guardarDireccion(address: String) {
        if (uid != null) {
            val userDocRef = db.collection("users").document(uid)
            userDocRef.get().addOnSuccessListener { document ->
                val currentAddresses = (document.get("addresses") as? List<String>) ?: emptyList()
                if (!currentAddresses.contains(address)) {
                    val updated = currentAddresses + address
                    userDocRef.set(mapOf("addresses" to updated), SetOptions.merge())
                        .addOnSuccessListener {
                            userAddresses = updated
                        }
                }
            }
        }
    }

    fun eliminarDireccion(address: String) {
        if (uid != null) {
            val updated = userAddresses - address
            db.collection("users").document(uid).update("addresses", updated).addOnSuccessListener {
                userAddresses = updated
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Address",
                fontFamily = poppinsFont,
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Select an Address",
            fontFamily = poppinsFont,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Box {
            OutlinedTextField(
                value = selectedAddress ?: "Choose address",
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true },
                readOnly = true,
                enabled = false,
                trailingIcon = {
                    Icon(Icons.Default.ArrowBack, contentDescription = null)
                }
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                apiAddresses.forEach { address ->
                    DropdownMenuItem(onClick = {
                        selectedAddress = address
                        expanded = false
                        guardarDireccion(address)
                    }) {
                        Text(address, fontFamily = poppinsFont, fontSize = 14.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Your Addresses",
            fontFamily = poppinsFont,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column {
            userAddresses.forEach { address ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = 4.dp,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(address, fontFamily = poppinsFont, modifier = Modifier.weight(1f))

                        val scale = remember { mutableStateOf(1f) }
                        val animatedScale by animateFloatAsState(
                            targetValue = scale.value,
                            animationSpec = tween(durationMillis = 150),
                            label = "deleteButtonScale"
                        )

                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    scale.value = 1.2f
                                    eliminarDireccion(address)
                                    delay(150)
                                    scale.value = 1f
                                }
                            },
                            modifier = Modifier
                                .size(36.dp)
                                .graphicsLayer {
                                    scaleX = animatedScale
                                    scaleY = animatedScale
                                }
                                .background(MaterialTheme.colors.error, RoundedCornerShape(50))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}
