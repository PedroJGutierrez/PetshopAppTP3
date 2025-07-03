// Archivo: ProfileScreen.kt

package com.proyecto.petshopapp.ui.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.proyecto.petshopapp.R
import com.proyecto.petshopapp.data.models.Product
import com.proyecto.petshopapp.ui.login.LoginViewModel
import com.proyecto.petshopapp.ui.theme.PurplePrimary
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await

@Composable
fun ProfileScreen(
    navController: NavController,
    loginViewModel: LoginViewModel,
    profileViewModel: ProfileViewModel = viewModel()
) {
    val user by profileViewModel.user.collectAsState()
    val selectedPhoto by profileViewModel.profileImageRes.collectAsState()
    val userEmail = loginViewModel.uiState.collectAsState().value.userEmail ?: ""

    var isSellerMode by remember { mutableStateOf(false) }
    var showPhotoMenu by remember { mutableStateOf(false) }
    var showSaved by remember { mutableStateOf(true) }
    var sellerTab by remember { mutableStateOf("Product") }
    var favorites by remember { mutableStateOf<List<Product>>(emptyList()) }
    val uiState by loginViewModel.uiState.collectAsState()
    val isReseller = uiState.userType == "Reseller"
    var nombre by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var isEditingProfile by remember { mutableStateOf(false) }
    var showSuccessIcon by remember { mutableStateOf(false) }

    var showTooltip by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        profileViewModel.loadUserProfile()
    }
    LaunchedEffect(isEditingProfile) {
        if (isEditingProfile) {
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            if (!uid.isNullOrEmpty()) {
                val snapshot = Firebase.firestore.collection("users").document(uid).get().await()
                nombre = snapshot.getString("nombre") ?: ""
                username = snapshot.getString("username") ?: ""
                email = snapshot.getString("email") ?: ""
            }
        }
    }

    LaunchedEffect(user) {
        user?.let {
            username = it.username
            nombre = it.nombre
            email = it.email
        }
    }
    val dummyProducts = listOf(
        Product(id = 1, title = "RC Kitten", price = 20.99, thumbnail = "kitten"),
        Product(id = 2, title = "RC Persian", price = 22.99, thumbnail = "persian")
    )

    fun cargarFavoritos(uid: String) {
        Firebase.firestore.collection("users").document(uid).collection("favoritos")
            .get()
            .addOnSuccessListener { snapshot ->
                favorites = snapshot.documents.mapNotNull { doc ->
                    Product.fromMap(doc.data ?: emptyMap())
                }
            }
    }

    fun guardarCambiosPerfil() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        if (nombre.isBlank() || username.isBlank()) {
            println("Cannot save an empty name or username")
            return
        }

        val userDoc = Firebase.firestore.collection("users").document(uid)
        userDoc.update(
            mapOf(
                "nombre" to nombre,
                "username" to username,
                "email" to email
            )
        ).addOnSuccessListener {
            showSuccessIcon = true
            println(" Perfil actualizado")
        }.addOnFailureListener {
            showSuccessIcon = false
            println(" Error al actualizar perfil: ${it.message}")
        }
    }

    LaunchedEffect(userEmail) {
        FirebaseAuth.getInstance().currentUser?.uid?.let { uid ->
            cargarFavoritos(uid)
        }
    }
    LaunchedEffect(showTooltip) {
        if (showTooltip) {
            delay(2000)
            showTooltip = false
        }
    }



    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(30.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            ) {
                val profileSelected = !isSellerMode
                val sellerSelected = isSellerMode

                val profileBgColor by animateColorAsState(
                    targetValue = if (profileSelected) PurplePrimary else Color(0xFFF0F0F0),
                    animationSpec = tween(300)
                )

                val sellerBgColor by animateColorAsState(
                    targetValue = if (sellerSelected) PurplePrimary else Color(0xFFF0F0F0),
                    animationSpec = tween(300)
                )

                val profileTextColor by animateColorAsState(
                    targetValue = if (profileSelected) Color.White else Color.Gray,
                    animationSpec = tween(300)
                )

                val sellerTextColor by animateColorAsState(
                    targetValue = if (sellerSelected) Color.White else Color.Gray,
                    animationSpec = tween(300)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Spacer(modifier = Modifier.weight(1f))

                    Box(
                        modifier = Modifier
                            .shadow(1.dp, RoundedCornerShape(50))
                            .background(Color(0xFFF0F0F0), RoundedCornerShape(50))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Botón Profile
                            Button(
                                onClick = { isSellerMode = false },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = profileBgColor,
                                    contentColor = profileTextColor
                                ),
                                shape = RoundedCornerShape(50),
                                elevation = null,
                                modifier = Modifier.defaultMinSize(minWidth = 100.dp)
                            ) {
                                Text("Profile", fontWeight = FontWeight.Medium)
                            }

                            // Botón Seller Mode con tooltip debajo
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Button(
                                    onClick = {
                                        if (isReseller) isSellerMode = true else showTooltip = true
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = sellerBgColor,
                                        contentColor = sellerTextColor
                                    ),
                                    shape = RoundedCornerShape(50),
                                    elevation = null,
                                    modifier = Modifier.defaultMinSize(minWidth = 100.dp)
                                ) {
                                    Text("Seller Mode", fontWeight = FontWeight.Medium)
                                }

                                if (showTooltip) {
                                    Spacer(modifier = Modifier.height(8.dp))

                                    Surface(
                                        color = Color.Black,
                                        shape = RoundedCornerShape(8.dp),
                                        tonalElevation = 4.dp,
                                        shadowElevation = 4.dp
                                    ) {
                                        Text(
                                            text = "This option is only enabled for resellers.",
                                            color = Color.White,
                                            modifier = Modifier.padding(
                                                horizontal = 12.dp,
                                                vertical = 6.dp
                                            ),
                                            fontSize = 12.sp
                                        )
                                    }

                                    Canvas(modifier = Modifier.size(12.dp)) {
                                        drawPath(
                                            path = Path().apply {
                                                moveTo(0f, size.height)
                                                lineTo(size.width / 2f, 0f)
                                                lineTo(size.width, size.height)
                                                close()
                                            },
                                            color = Color.Black
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Ícono de ajustes a la derecha
                    IconButton(
                        onClick = { navController.navigate("settings") }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Ajustes"
                        )
                    }
                }
            }


            // --- IMAGEN PERFIL ---
            if (!isSellerMode) {
                // Imagen de fondo + perfil + nombre
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .padding(horizontal = 24.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    // Banner (imagen de fondo)
                    Image(
                        painter = painterResource(R.drawable.gris),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(159.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .align(Alignment.TopCenter)
                    )

                    // Ícono para editar banner, arriba a la derecha del banner
                    if (isEditingProfile) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .align(Alignment.TopEnd)
                                .background(Color.White, shape = CircleShape)
                                .clickable { /* Acción para cambiar banner */ },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.icono_edit),
                                contentDescription = "Editar Banner",
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    // Imagen de perfil + nombre
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .offset(y = 60.dp)
                            .zIndex(1f)
                    ) {
                        Box(

                            modifier = Modifier
                                .padding(top = 50.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = selectedPhoto),
                                contentDescription = "Profile Photo",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .size(100.dp)
                            )

                            if (isEditingProfile) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .align(Alignment.TopEnd)
                                        .background(Color.White, shape = CircleShape)
                                        .clickable { showPhotoMenu = true },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.icono_edit),
                                        contentDescription = "Editar Banner",
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = username.ifBlank { "Cargando..." },
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontFamily = poppinsFont,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))


                }

                if (showPhotoMenu) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Card(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .width(300.dp)
                                .height(220.dp)
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) { /* Consume click para no cerrar al clickear dentro del Card */ },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.Top,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // Botón para cerrar arriba a la derecha
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.TopEnd
                                ) {
                                    IconButton(onClick = { showPhotoMenu = false }) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Cerrar menú"
                                        )
                                    }
                                }

                                Text(
                                    "Elegí tu foto",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(bottom = 12.dp)
                                )

                                val photoOptions = listOf(
                                    "foto1" to R.drawable.foto1,
                                    "foto2" to R.drawable.foto2,
                                    "foto3" to R.drawable.foto3,
                                    "foto4" to R.drawable.foto4
                                )

                                Row(
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    photoOptions.forEach { (name, resId) ->
                                        Image(
                                            painter = painterResource(id = resId),
                                            contentDescription = "Foto $name",
                                            modifier = Modifier
                                                .size(60.dp)
                                                .clip(CircleShape)
                                                .border(2.dp, Color.LightGray, CircleShape)
                                                .clickable {
                                                    profileViewModel.updateProfileImage(name)
                                                    showPhotoMenu = false
                                                }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(80.dp))
                if(!isEditingProfile) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Row(
                            modifier = Modifier.align(Alignment.Center),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = {
                                    showSaved = true
                                    isEditingProfile = false
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (showSaved && !isEditingProfile) PurplePrimary else Color.LightGray
                                ),
                                shape = RoundedCornerShape(30),
                                modifier = Modifier
                                    .widthIn(min = 78.dp)
                                    .height(47.dp)
                            ) {
                                Text("Saved", color = Color.White)
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Button(
                                onClick = {
                                    showSaved = true
                                    isEditingProfile = true
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isEditingProfile) PurplePrimary else Color.LightGray
                                ),
                                shape = RoundedCornerShape(30),
                                modifier = Modifier
                                    .widthIn(min = 78.dp)
                                    .height(47.dp)
                            ) {
                                Text("Edit Profile", color = Color.White)
                            }
                        }
                    }
                }

                if (isEditingProfile) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val customTextFieldColors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PurplePrimary,
                            unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f),
                            focusedTextColor = PurplePrimary,
                            unfocusedTextColor = Color.Gray,
                            unfocusedLabelColor = Color.Gray,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        )

                        OutlinedTextField(
                            value = nombre,
                            onValueChange = { nombre = it },
                            label = { Text("Nombre") },
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            textStyle = TextStyle(
                                fontSize = 14.sp,
                                color = if (nombre.isNotBlank()) PurplePrimary else Color.Gray
                            ),
                            colors = customTextFieldColors
                        )

                        OutlinedTextField(
                            value = username,
                            onValueChange = { username = it },
                            label = { Text("Usuario") },
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            textStyle = TextStyle(
                                fontSize = 14.sp,
                                color = if (username.isNotBlank()) PurplePrimary else Color.Gray
                            ),
                            colors = customTextFieldColors
                        )

                        OutlinedTextField(
                            value = email,
                            onValueChange = {},
                            label = { Text("Email") },
                            modifier = Modifier.fillMaxWidth(),
                            readOnly = true,
                            enabled = false,
                            shape = RoundedCornerShape(12.dp),
                            textStyle = TextStyle(fontSize = 14.sp, color = Color.DarkGray),
                            colors = OutlinedTextFieldDefaults.colors(
                                disabledBorderColor = Color.Gray.copy(alpha = 0.3f),
                                disabledLabelColor = Color.Gray,
                                disabledTextColor = Color.DarkGray,
                                disabledContainerColor = Color(0xFFF0F0F0)
                            )
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp, bottom = 20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = { guardarCambiosPerfil()
                                    isEditingProfile = false
                                    showSaved = true },
                                colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary),
                                shape = RoundedCornerShape(50),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(60.dp)
                            ) {
                                Text("Save Changes")
                            }
                        }
                    }
                } else if (showSaved) Column(modifier = Modifier.fillMaxWidth()) {
                    if (favorites.isEmpty()) {
                        Text(
                            text = "No saved products.",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 32.dp),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium
                        )
                    } else {
                        Spacer(modifier = Modifier.width(80.dp))

                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(favorites) { product ->
                                FavoriteProductCard(
                                    product = product,
                                    navController = navController,
                                    loginViewModel = loginViewModel,
                                    onFavoriteChanged = {
                                        FirebaseAuth.getInstance().currentUser?.uid?.let { uid ->
                                            cargarFavoritos(uid)
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            } else {
                Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .padding(horizontal = 24.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {

                    Image(
                        painter = painterResource(R.drawable.naranja),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(159.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )

                    // Imagen de perfil + nombre
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .offset(y = 60.dp)
                            .zIndex(1f)
                    ) {
                        // Fondo gris alrededor del perfil
                        Box(
                            modifier = Modifier
                                .padding(top = 50.dp)
                                .size(100.dp)
                                .background(color = Color(0xFFEDEDED), shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.p),
                                contentDescription = "Foto de perfil",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .size(50.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Pittashop",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontFamily = poppinsFont,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }

                    Spacer(modifier = Modifier.height(80.dp))

                    // MÉTRICAS
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("109", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Followers", fontSize = 12.sp, color = Color.Gray)
                        }
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("992", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Following", fontSize = 12.sp, color = Color.Gray)
                        }
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("80", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Sales", fontSize = 12.sp, color = Color.Gray)
                        }
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                // BOTONES
                    val bgColor = Color(0xFFF0F0F0)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        val isProductSelected = sellerTab == "Product"
                        val isSoldSelected = sellerTab == "Sold"
                        val isStatsSelected = sellerTab == "Stats"

                        Button(
                            onClick = { sellerTab = "Product" },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isProductSelected) PurplePrimary else bgColor
                            ),
                            shape = RoundedCornerShape(30),
                            modifier = Modifier
                                .widthIn(min = 78.dp)
                                .height(47.dp)
                        ) {
                            Text(
                                "Product",
                                color = if (isProductSelected) Color.White else Color.Gray
                            )
                        }

                        Button(
                            onClick = { sellerTab = "Sold" },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSoldSelected) PurplePrimary else bgColor
                            ),
                            shape = RoundedCornerShape(30),
                            modifier = Modifier
                                .widthIn(min = 78.dp)
                                .height(47.dp)
                        ) {
                            Text(
                                "Sold",
                                color = if (isSoldSelected) Color.White else Color.Gray
                            )
                        }

                        Button(
                            onClick = { sellerTab = "Stats" },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isStatsSelected) PurplePrimary else bgColor
                            ),
                            shape = RoundedCornerShape(30),
                            modifier = Modifier
                                .widthIn(min = 78.dp)
                                .height(47.dp)
                        ) {
                            Text(
                                "Stats",
                                color = if (isStatsSelected) Color.White else Color.Gray
                            )
                        }
                    }
            }

                when (sellerTab) {
                    "Product" -> {
                        LazyRow(modifier = Modifier.padding(horizontal = 16.dp)) {
                            items(dummyProducts) { product ->
                                ProductCard(
                                    product = product,
                                    navController = navController,
                                    loginViewModel = loginViewModel
                                )
                            }
                        }
                    }

                    "Sold" -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp)
                        ) {
                            Icon(
                                Icons.Default.Warning, contentDescription = null,
                                tint = Color.Gray, modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("No sales have been made.", color = Color.Gray)
                        }
                    }

                    "Stats" -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Box(
                                        modifier = Modifier
                                            .height(60.dp)
                                            .width(20.dp)
                                            .background(PurplePrimary)
                                    )
                                    Text("Sales")
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Box(
                                        modifier = Modifier
                                            .height(40.dp)
                                            .width(20.dp)
                                            .background(PurplePrimary)
                                    )
                                    Text("Purchases")
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Box(
                                        modifier = Modifier
                                            .height(30.dp)
                                            .width(20.dp)
                                            .background(PurplePrimary)
                                    )
                                    Text("Profit")
                                }
                            }
                        }
                    }
                }
            }
        }

        Column(modifier = Modifier.align(Alignment.BottomCenter)) {
            BottomNavigationBar(navController = navController, viewModel = loginViewModel, isEditingProfile = isEditingProfile)
        }
    }
}

@Composable
fun ProductCard(product: Product, navController: NavController, loginViewModel: LoginViewModel) {
    val context = LocalContext.current
    val imageId = remember(product.thumbnail) {
        context.resources.getIdentifier(product.thumbnail, "drawable", context.packageName)
    }

    val uiState by loginViewModel.uiState.collectAsState()
    val isReseller = uiState.userType == "Reseller"
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val cardSpacing = 12.dp
    val horizontalPadding = 16.dp * 2
    val cardWidth = (screenWidth - horizontalPadding - cardSpacing) / 2
    val finalPrice = if (isReseller) product.price * 0.85 else product.price

    Card(
        modifier = Modifier
            .width(cardWidth)
            .height(220.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Image(
                    painter = painterResource(
                        id = if (imageId != 0) imageId else R.drawable.banner_product
                    ),
                    contentDescription = product.title,
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = product.title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "$${"%.2f".format(finalPrice).replace('.', ',')}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )

                        if (isReseller) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Surface(
                                color = Color(0xFF4CAF50),
                                shape = RoundedCornerShape(4.dp),
                                modifier = Modifier.height(20.dp)
                            ) {
                                Text(
                                    text = "15% OFF",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Surface(
                        modifier = Modifier
                            .size(28.dp)
                            .clickable {
                                navController.navigate("product_detail/${product.id}")
                            },
                        shape = CircleShape,
                        color = Color(0xFF8B5CF6)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Ver Detalle",
                            tint = Color.White,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel,
    isEditingProfile: Boolean

) {
    val uiState by viewModel.uiState.collectAsState()
    val userType = uiState.userType ?: "Usuario"
    var expanded by remember { mutableStateOf(false) }

    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route
    if (!isEditingProfile) {
        if (userType == "Reseller") {
            Column(modifier = modifier.fillMaxWidth()) {
                AnimatedVisibility(visible = expanded) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF8F8F8))
                            .padding(horizontal = 32.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BottomNavItemCustom(
                            icon = Icons.Default.History,
                            isSelected = currentDestination == "records",
                            onClick = {
                                expanded = false
                                navController.navigate("records")
                            },
                            label = "Records"
                        )
                        BottomNavItemCustom(
                            icon = Icons.Outlined.LocalShipping,
                            isSelected = currentDestination == "orders",
                            onClick = {
                                expanded = false
                                navController.navigate("orders")
                            },
                            label = "Orders"
                        )
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFF8F8F8),
                    shadowElevation = 0.dp,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BottomNavItemCustom(
                            iconRes = R.drawable.icono_home,
                            isSelected = currentDestination == "home",
                            onClick = { navController.navigate("home") },
                            label = "Home"
                        )
                        BottomNavItemCustom(
                            iconRes = R.drawable.icono_favorito,
                            isSelected = currentDestination == "favorites",
                            onClick = { navController.navigate("favorites") },
                            label = "Favorites"
                        )
                        BottomNavItemCustom(
                            iconRes = R.drawable.icono_carrito,
                            isSelected = currentDestination == "cart",
                            onClick = { navController.navigate("cart") },
                            label = "Cart"
                        )
                        BottomNavItemCustom(
                            iconRes = R.drawable.icono_profile,
                            isSelected = currentDestination == "profile",
                            onClick = { navController.navigate("profile") },
                            label = "Profile"
                        )
                        BottomNavItemCustom(
                            icon = if (expanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                            isSelected = false,
                            onClick = { expanded = !expanded },
                            label = if (expanded) "Collapse" else "Expand"
                        )
                    }
                }
            }
        } else {
            Surface(
                modifier = modifier.fillMaxWidth(),
                color = Color(0xFFF8F8F8),
                shadowElevation = 0.dp,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp) // altura más grande
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BottomNavItemCustom(
                        iconRes = R.drawable.icono_home,
                        isSelected = currentDestination == "home",
                        onClick = { navController.navigate("home") },
                        label = "Home"
                    )
                    BottomNavItemCustom(
                        iconRes = R.drawable.icono_favorito,
                        isSelected = currentDestination == "favorites",
                        onClick = { navController.navigate("favorites") },
                        label = "Fa vorites"
                    )
                    BottomNavItemCustom(
                        iconRes = R.drawable.icono_carrito,
                        isSelected = currentDestination == "cart",
                        onClick = { navController.navigate("cart") },
                        label = "Cart"
                    )
                    BottomNavItemCustom(
                        iconRes = R.drawable.icono_profile,
                        isSelected = currentDestination == "profile",
                        onClick = { navController.navigate("profile") },
                        label = "Profile"
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavItemCustom(
    icon: ImageVector? = null,
    iconRes: Int? = null,
    isSelected: Boolean,
    onClick: () -> Unit,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
    ) {
        if (iconRes != null) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = label,
                tint = if (isSelected) PurplePrimary else Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        } else if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) PurplePrimary else Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(PurplePrimary, shape = CircleShape)
            )
        } else {
            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}

@Composable
fun FavoriteProductCard(
    product: Product,
    navController: NavController,
    loginViewModel: LoginViewModel,
    onFavoriteChanged: () -> Unit
) {
    val context = LocalContext.current
    val currentUser = FirebaseAuth.getInstance().currentUser
    val firestore = remember { FirebaseFirestore.getInstance() }
    val imageId = remember(product.thumbnail) {
        context.resources.getIdentifier(product.thumbnail, "drawable", context.packageName)
    }
    var isFavorite by remember { mutableStateOf(true) }

    val uiState by loginViewModel.uiState.collectAsState()
    val isReseller = uiState.userType == "Reseller"
    val finalPrice = if (isReseller) product.price * 0.85 else product.price
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val cardSpacing = 12.dp
    val horizontalPadding = 16.dp * 2
    val cardWidth = (screenWidth - horizontalPadding - cardSpacing) / 2
    Card(
        modifier = Modifier
            .width(cardWidth)
            .height(220.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Image(
                    painter = painterResource(
                        id = if (imageId != 0) imageId else R.drawable.banner_product
                    ),
                    contentDescription = product.title,
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = product.title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "$${"%.2f".format(finalPrice).replace('.', ',')}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )

                        if (isReseller) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Surface(
                                color = Color(0xFF4CAF50),
                                shape = RoundedCornerShape(4.dp),
                                modifier = Modifier.height(20.dp)
                            ) {
                                Text(
                                    text = "15% OFF",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Surface(
                        modifier = Modifier
                            .size(28.dp)
                            .clickable {
                                navController.navigate("product_detail/${product.id}")
                            },
                        shape = CircleShape,
                        color = Color(0xFF8B5CF6)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Ver Detalle",
                            tint = Color.White,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }
            }

            IconButton(
                onClick = {
                    currentUser?.let { user ->
                        val favRef = firestore.collection("users")
                            .document(user.uid)
                            .collection("favoritos")
                            .document(product.id.toString())

                        if (isFavorite) {
                            favRef.delete().addOnSuccessListener {
                                isFavorite = false
                                onFavoriteChanged()
                            }
                        } else {
                            favRef.set(product, SetOptions.merge()).addOnSuccessListener {
                                isFavorite = true
                                onFavoriteChanged()
                            }
                        }
                    }
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .size(28.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icono_favorito),
                    contentDescription = "Favorito",
                    tint = if (isFavorite) Color.Red else Color.Gray,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }

}
