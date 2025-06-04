// Archivo: ProfileScreen.kt

package com.proyecto.petshopapp.profile

import androidx.compose.animation.AnimatedVisibility
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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.proyecto.petshopapp.R
import com.proyecto.petshopapp.data.models.Product
import com.proyecto.petshopapp.home.BottomNavItem
import com.proyecto.petshopapp.login.LoginViewModel
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

    val dummyProducts = listOf(
        Product(id = 1, title = "RC Kitten", price = 20.99, thumbnail = "kitten"),
        Product(id = 2, title = "RC Persian", price = 22.99, thumbnail = "persian")
    )

    LaunchedEffect(Unit) {
        profileViewModel.loadUserProfile()
    }

    LaunchedEffect(userEmail) {
        if (userEmail.isNotBlank()) {
            val snapshot = Firebase.firestore.collection("users").document(userEmail).get().await()
            val productList = snapshot["favorites"] as? List<Map<String, Any>> ?: emptyList()
            favorites = productList.mapNotNull { Product.fromMap(it) }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Toggle Profile / Seller Mode
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { isSellerMode = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (!isSellerMode) Color(0xFF8A56AC) else Color.LightGray
                    ),
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Profile", color = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { isSellerMode = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSellerMode) Color(0xFF8A56AC) else Color.LightGray
                    ),
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Seller Mode", color = Color.White)
                }
            }

            if (!isSellerMode) {
                // PROFILE MODE
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.gris),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .offset(y = 60.dp)
                    ) {
                        Image(
                            painter = painterResource(id = selectedPhoto),
                            contentDescription = "Profile Photo",
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                        )

                        IconButton(
                            onClick = { showPhotoMenu = true },
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .offset(x = (-10).dp, y = (-10).dp)
                                .size(28.dp)
                                .background(Color.White, shape = CircleShape)
                                .border(1.dp, Color.Gray, CircleShape)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                        }
                    }
                }

                // NUEVO CUADRO DE SELECCIÓN DE FOTO
                if (showPhotoMenu) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0x80000000))
                            .clickable(onClick = { showPhotoMenu = false })
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
                                Row(
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    listOf("foto1", "foto2", "foto3", "foto4").forEach { name ->
                                        val context = LocalContext.current
                                        val imageId = remember(name) {
                                            context.resources.getIdentifier(name, "drawable", context.packageName)
                                        }
                                        Image(
                                            painter = painterResource(id = imageId),
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

                Text(
                    user?.username ?: "",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 8.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = { showSaved = true }) { Text("Saved") }
                    Button(onClick = { navController.navigate("edit_profile") }) { Text("Edit Profile") }
                }

                if (showSaved) {
                    LazyRow(modifier = Modifier.padding(horizontal = 16.dp)) {
                        items(favorites) { product ->
                            ProductCard(product = product, navController = navController, loginViewModel = loginViewModel)
                        }
                    }
                }
            } else {
                // SELLER MODE
                Image(
                    painter = painterResource(R.drawable.naranja),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                )
                Image(
                    painter = painterResource(R.drawable.p),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .align(Alignment.CenterHorizontally)
                )
                Text(
                    "Pittashop",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { sellerTab = "Product" },
                        colors = ButtonDefaults.buttonColors(containerColor = if (sellerTab == "Product") Color(0xFF8A56AC) else Color.LightGray)
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
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp)
                        ) {
                            Icon(
                                Icons.Default.Warning,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("No tiene ventas realizadas.", color = Color.Gray)
                        }
                    }
                    "Stats" -> {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(modifier = Modifier.height(60.dp).width(20.dp).background(Color(0xFF8A56AC)))
                                Text("Ventas")
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(modifier = Modifier.height(40.dp).width(20.dp).background(Color(0xFF8A56AC)))
                                Text("Compras")
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(modifier = Modifier.height(30.dp).width(20.dp).background(Color(0xFF8A56AC)))
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
                        onClick = { },
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
                    onClick = { },
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


