// Archivo: ProfileScreen.kt

package com.proyecto.petshopapp.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.proyecto.petshopapp.R
import com.proyecto.petshopapp.data.models.Product
import com.proyecto.petshopapp.home.BottomNavItem
import com.proyecto.petshopapp.login.LoginViewModel
import com.proyecto.petshopapp.ui.theme.PurplePrimary
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

    var isEditingProfile by remember { mutableStateOf(false) }

    var selectedBackground by remember { mutableStateOf("gris") }
    var showBackgroundMenu by remember { mutableStateOf(false) }
    var showSuccessIcon by remember { mutableStateOf(false) }
    var nombre by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    val uiState by loginViewModel.uiState.collectAsState()
    val isReseller = uiState.userType == "Revendedor"

    var showTooltip by remember { mutableStateOf(false) }
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
            println(" No se puede guardar un nombre o username vacío")
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

    LaunchedEffect(Unit) {
        profileViewModel.loadUserProfile()
    }

    LaunchedEffect(userEmail) {
        FirebaseAuth.getInstance().currentUser?.uid?.let { uid ->
            cargarFavoritos(uid)
        }
    }
    LaunchedEffect(showTooltip) {
        if (showTooltip) {
            kotlinx.coroutines.delay(2000)
            showTooltip = false
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Row(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalArrangement = Arrangement.Center
                ) {

                    Button(
                        onClick = { isSellerMode = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (!isSellerMode) PurplePrimary else Color.LightGray
                        ),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text("Profile", color = Color.White)
                    }

                    Spacer(modifier = Modifier.width(8.dp))


                    Box(contentAlignment = Alignment.TopCenter) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Button(
                                onClick = {
                                    if (isReseller) {
                                        isSellerMode = true
                                    } else {
                                        showTooltip = true
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isSellerMode) PurplePrimary else Color.LightGray
                                ),
                                shape = RoundedCornerShape(50)
                            ) {
                                Text("Seller Mode", color = Color.White)
                            }

                            AnimatedVisibility(
                                visible = showTooltip,
                                enter = fadeIn() + slideInVertically(initialOffsetY = { -20 }),
                                exit = fadeOut() + slideOutVertically(targetOffsetY = { -20 })
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Surface(
                                        color = Color.Black,
                                        shape = RoundedCornerShape(8.dp),
                                        tonalElevation = 4.dp,
                                        shadowElevation = 4.dp
                                    ) {
                                        Text(
                                            text = "This option is only enabled for resellers.",
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                            fontSize = 12.sp
                                        )
                                    }


                                    Canvas(modifier = Modifier.size(12.dp)) {
                                        drawPath(
                                            path = androidx.compose.ui.graphics.Path().apply {
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
                }


                IconButton(
                    onClick = {
                        navController.navigate("settings")
                    },
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Ajustes"
                    )
                }
            }


            if (!isSellerMode) {

                Box(modifier = Modifier.fillMaxWidth().height(180.dp)) {
                    Image(
                        painter = painterResource(R.drawable.gris),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .offset(y = 60.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = selectedPhoto),
                            contentDescription = "Profile Photo",
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = user?.username ?: "",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(top = 4.dp)
                        )

                        if (isEditingProfile) {
                            IconButton(
                                onClick = { showPhotoMenu = true },
                                modifier = Modifier
                                    .offset(x = (40).dp, y = (-60).dp)
                                    .size(28.dp)
                                    .background(Color.White, shape = CircleShape)
                                    .border(1.dp, Color.Gray, CircleShape)
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }

                if (showPhotoMenu) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0x80000000))
                            .clickable { showPhotoMenu = false }
                    ) {
                        Card(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .width(300.dp)
                                .height(200.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("Elegí tu foto", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Spacer(modifier = Modifier.height(12.dp))

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

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    user?.username ?: "",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            showSaved = true
                            isEditingProfile = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (showSaved && !isEditingProfile) PurplePrimary else Color.LightGray
                        ),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text("Saved", color = Color.White)
                    }

                    Button(
                        onClick = {
                            showSaved = true
                            isEditingProfile = true
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isEditingProfile) PurplePrimary else Color.LightGray
                        ),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text("Edit Profile", color = Color.White)
                    }
                }

                if (isEditingProfile) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        OutlinedTextField(
                            value = nombre,
                            onValueChange = { nombre = it },
                            label = { Text("Nombre", color = Color.Black) },
                            placeholder = {
                                if (nombre.isBlank()) Text("Pon tu nombre completo", color = Color.Gray)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = LocalTextStyle.current.copy(color = Color.Black)
                        )


                        OutlinedTextField(
                            value = username,
                            onValueChange = { username = it },
                            label = { Text("Usuario", color = Color.Black) },
                            placeholder = {
                                if (username.isBlank()) Text("Ej: pittashop_user", color = Color.Gray)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = LocalTextStyle.current.copy(color = Color.Black)
                        )


                        OutlinedTextField(
                            value = email,
                            onValueChange = {},
                            label = { Text("Email", color = Color.Black) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF0F0F0), RoundedCornerShape(8.dp)),
                            readOnly = true,
                            enabled = false,
                            textStyle = LocalTextStyle.current.copy(color = Color.DarkGray)
                        )


                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = { guardarCambiosPerfil() },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Save Changes")
                            }

                            if (showSuccessIcon) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF4CAF50)), // verde
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Success",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }
                else if (showSaved) {
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        if (favorites.isEmpty()) {
                            Text(
                                text = "No hay productos guardados.",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 32.dp),
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Medium
                            )
                        } else {
                            LazyRow(
                                modifier = Modifier.padding(top = 8.dp),
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
                }


            } else {
                // SELLER MODE
                Image(painter = painterResource(R.drawable.naranja), contentDescription = null,
                    modifier = Modifier.fillMaxWidth().height(120.dp))
                Image(painter = painterResource(R.drawable.p), contentDescription = null,
                    modifier = Modifier.size(100.dp).clip(CircleShape).align(Alignment.CenterHorizontally))
                Text("Pittashop", fontSize = 20.sp, fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally))

                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("109", fontWeight = FontWeight.Bold)
                        Text("Followers")
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("992", fontWeight = FontWeight.Bold)
                        Text("Following")
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("80", fontWeight = FontWeight.Bold)
                        Text("Sales")
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { sellerTab = "Product" },
                        colors = ButtonDefaults.buttonColors(containerColor = if (sellerTab == "Product") PurplePrimary else Color.LightGray)
                    ) {
                        Text("Product", color = Color.White)
                    }
                    OutlinedButton(onClick = { sellerTab = "Sold" }) { Text("Sold") }
                    OutlinedButton(onClick = { sellerTab = "Stats" }) { Text("Stats") }
                }

                when (sellerTab) {
                    "Product" -> {
                        LazyRow(modifier = Modifier.padding(horizontal = 16.dp)) {
                            items(dummyProducts) { product ->
                                ProductCard(product = product, navController = navController, loginViewModel = loginViewModel)
                            }
                        }
                    }
                    "Sold" -> {
                        Column(horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth().padding(32.dp)) {
                            Icon(Icons.Default.Warning, contentDescription = null,
                                tint = Color.Gray, modifier = Modifier.size(48.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("No tiene ventas realizadas.", color = Color.Gray)
                        }
                    }
                    "Stats" -> {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(32.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(modifier = Modifier.height(60.dp).width(20.dp).background(PurplePrimary))
                                Text("Ventas")
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(modifier = Modifier.height(40.dp).width(20.dp).background(PurplePrimary))
                                Text("Compras")
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(modifier = Modifier.height(30.dp).width(20.dp).background(PurplePrimary))
                                Text("Ganancia")
                            }
                        }
                    }
                }
            }
        }

        Column(modifier = Modifier.align(Alignment.BottomCenter)) {
            BottomNavigationBar(navController = navController, viewModel = loginViewModel)
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
    val isReseller = uiState.userType == "Revendedor"
    val discountedPrice = if (isReseller) product.price * 0.85 else product.price

    Card(
        modifier = Modifier
            .width(147.dp)
            .height(200.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
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
                textAlign = TextAlign.Start,
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
                        text = "$${String.format("%.2f", discountedPrice).replace('.', ',')}",
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
                        contentDescription = "Agregar",
                        tint = Color.White,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val userType = uiState.userType ?: "Usuario"

    if (userType == "Revendedor") {
        var expanded by remember { mutableStateOf(false) }

        Column(modifier = modifier.fillMaxWidth()) {
            AnimatedVisibility(visible = expanded) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .background(Color.White),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    BottomNavItem(
                        icon = Icons.Default.History,
                        isSelected = false,
                        onClick = { /* TODO: Historial */ },
                        label = "Records"
                    )
                    BottomNavItem(
                        icon = Icons.Outlined.LocalShipping,
                        isSelected = false,
                        onClick = { /* TODO: Gestión pedidos */ },
                        label = "Orders"
                    )
                }
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 32.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    BottomNavItem(
                        icon = Icons.Default.Home,
                        isSelected = false,
                        onClick = { navController.navigate("home") },
                        label = "Home Page"
                    )
                    BottomNavItem(
                        icon = Icons.Default.Favorite,
                        isSelected = false,
                        onClick = { navController.navigate("favorites") },
                        label = "Favorites"
                    )
                    BottomNavItem(
                        icon = Icons.Default.ShoppingCart,
                        isSelected = false,
                        onClick = { navController.navigate("cart") },
                        label = "Cart"
                    )
                    BottomNavItem(
                        icon = Icons.Default.Person,
                        isSelected = true,
                        onClick = { },
                        label = "Profile"
                    )
                    BottomNavItem(
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
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 32.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                BottomNavItem(
                    icon = Icons.Default.Home,
                    isSelected = false,
                    onClick = { navController.navigate("home") },
                    label = "Home Page"
                )
                BottomNavItem(
                    icon = Icons.Default.Favorite,
                    isSelected = false,
                    onClick = { navController.navigate("favorites") },
                    label = "Favorites"
                )
                BottomNavItem(
                    icon = Icons.Default.ShoppingCart,
                    isSelected = false,
                    onClick = { navController.navigate("cart") },
                    label = "Cart"
                )
                BottomNavItem(
                    icon = Icons.Default.Person,
                    isSelected = true,
                    onClick = { },
                    label = "Profile"
                )
            }
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
    val isReseller = uiState.userType == "Revendedor"
    val finalPrice = if (isReseller) product.price * 0.85 else product.price

    Card(
        modifier = Modifier
            .width(147.dp)
            .height(200.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
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
                            favRef.delete()
                                .addOnSuccessListener {
                                    isFavorite = false
                                    onFavoriteChanged()
                                }
                        } else {
                            favRef.set(product, SetOptions.merge())
                                .addOnSuccessListener {
                                    isFavorite = true
                                    onFavoriteChanged()
                                }
                        }
                    }
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favorito",
                    tint = if (isFavorite) Color.Red else Color.Gray
                )
            }
        }
    }
}
