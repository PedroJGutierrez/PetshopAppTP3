package com.proyecto.petshopapp.home

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.proyecto.petshopapp.R
import com.proyecto.petshopapp.data.models.Product
import androidx.lifecycle.viewmodel.compose.viewModel
import com.proyecto.petshopapp.home.ProductViewModel
import com.proyecto.petshopapp.login.LoginViewModel
import com.proyecto.petshopapp.ui.theme.PurplePrimary
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.ui.graphics.vector.ImageVector


@Composable
fun HomeScreen(navController: NavController, loginViewModel: LoginViewModel) {
    var selectedLocation by remember { mutableStateOf("Buenos Aires") }
    var selectedCategory by remember { mutableStateOf("All") }
    var showLocationModal by remember { mutableStateOf(false) }
    var showCategoryModal by remember { mutableStateOf(false) }

    val uiState by loginViewModel.uiState.collectAsState()
    val userType = uiState.userType ?: "Usuario"

    val productViewModel: ProductViewModel = viewModel()
    val products by productViewModel.products.collectAsState()

    val context = LocalContext.current

    BackHandler {
        (context as? android.app.Activity)?.moveTaskToBack(true)
    }

    val filteredProducts = if (selectedCategory == "All") {
        products
    } else {
        products.filter { it.category.equals(selectedCategory, ignoreCase = true) }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            if (userType == "Revendedor") {
                Surface(
                    color = Color(0xFF4CAF50), // verde
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Wholesale Discounts",
                        color = Color.White,
                        modifier = Modifier.padding(12.dp),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            HeaderSection(
                location = selectedLocation,
                onLocationClick = { showLocationModal = true },
                navController = navController
            )

            Spacer(modifier = Modifier.height(20.dp))
            PromotionalBanner()
            Spacer(modifier = Modifier.height(24.dp))

            CategorySection(
                selectedCategory = selectedCategory,
                categories = listOf("All", "Food", "Toys", "Accessories"),
                onCategorySelect = { selectedCategory = it },
                onViewAllClick = { showCategoryModal = true }
            )

            Spacer(modifier = Modifier.height(24.dp))
            BestSellerSection(products = filteredProducts, navController = navController, loginViewModel = loginViewModel)


            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                Text("Cerrar sesión")
            }

            Spacer(modifier = Modifier.height(100.dp))
        }

        BottomNavigationBar(
            navController = navController,
            viewModel = loginViewModel,
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        AnimatedVisibility(
            visible = showLocationModal,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it }),
            modifier = Modifier.zIndex(1f)
        ) {
            LocationModal(
                locations = listOf("Buenos Aires"),
                selectedLocation = selectedLocation,
                onLocationSelect = {
                    selectedLocation = it
                    showLocationModal = false
                },
                onDismiss = { showLocationModal = false }
            )
        }

        AnimatedVisibility(
            visible = showCategoryModal,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it }),
            modifier = Modifier.zIndex(1f)
        ) {
            CategoryModal(
                categories = listOf("All", "Food", "Toys", "Accessories"),
                selectedCategory = selectedCategory,
                onCategorySelect = {
                    selectedCategory = it
                    showCategoryModal = false
                },
                onDismiss = { showCategoryModal = false }
            )
        }
    }
}

@Composable
fun HeaderSection(
    location: String,
    onLocationClick: () -> Unit,
    navController: NavController
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onLocationClick() }
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = location,
                fontSize = 14.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(16.dp)
            )
        }

        Row {
            IconButton(onClick = { navController.navigate("search") }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.Black
                )
            }
            IconButton(onClick = {
                navController.navigate("notifications")
            }) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = Color.Black
                )
            }
        }
    }
}


@Composable
fun PromotionalBanner() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Background Image
            Image(
                painter = painterResource(id = R.drawable.banner_background),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Product Image
                Image(
                    painter = painterResource(id = R.drawable.banner_product),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Royal Canin",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Adult Pomeranian",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Get an interesting promo here, without conditions",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun CategorySection(
    selectedCategory: String,
    categories: List<String>,
    onCategorySelect: (String) -> Unit,
    onViewAllClick: () -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Category",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            TextButton(onClick = onViewAllClick) {
                Text(
                    text = "View All",
                    color = Color(0xFF8B5CF6)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories) { category ->
                CategoryChip(
                    text = category,
                    isSelected = category == selectedCategory,
                    onClick = { onCategorySelect(category) }
                )
            }
        }
    }
}

@Composable
fun CategoryChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) Color(0xFF8B5CF6) else Color(0xFFF5F5F5)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            color = if (isSelected) Color.White else Color.Gray,
            fontSize = 14.sp
        )
    }
}

@Composable
fun BestSellerSection(
    products: List<Product>,
    navController: NavController,
    loginViewModel: LoginViewModel
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        // ...
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.height(300.dp)
        ) {
            items(products) { product ->
                ProductCard(product = product, navController = navController, loginViewModel = loginViewModel)
            }
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
                        isSelected = true,
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
                        isSelected = false,
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
                    isSelected = true,
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
                    isSelected = false,
                    onClick = { },
                    label = "Profile"
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavItem(
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    label: String
) {
    val tooltipState = rememberTooltipState()

    TooltipBox(
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
        tooltip = {
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = Color.DarkGray
            ) {
                Text(
                    text = label,
                    modifier = Modifier.padding(8.dp),
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
        },
        state = tooltipState,
        modifier = Modifier
    ) {
        IconButton(
            onClick = onClick
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) Color(0xFF8B5CF6) else Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationModal(
    locations: List<String>,
    selectedLocation: String,
    onLocationSelect: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var searchQuery by remember { mutableStateOf("") }

    // Lista filtrada según búsqueda
    val filteredLocations = locations.filter { it.contains(searchQuery, ignoreCase = true) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Select Location",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search location...") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            filteredLocations.forEach { location ->
                LocationItem(
                    location = location,
                    isSelected = location == selectedLocation,
                    onClick = { onLocationSelect(location) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryModal(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelect: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var searchQuery by remember { mutableStateOf("") }

    // Filtramos las categorías según el texto de búsqueda
    val filteredCategories = categories.filter { it.contains(searchQuery, ignoreCase = true) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Categories",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Buscador
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search...") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // Lista filtrada
            filteredCategories.forEach { category ->
                CategoryItem(
                    category = category,
                    isSelected = category == selectedCategory,
                    onClick = { onCategorySelect(category) }
                )
            }
        }
    }
}


@Composable
fun LocationItem(
    location: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            tint = if (isSelected) Color(0xFF8B5CF6) else Color.Gray
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = location,
            fontSize = 16.sp,
            color = if (isSelected) Color(0xFF8B5CF6) else Color.Black,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
        )
    }
}

@Composable
fun CategoryItem(
    category: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = category,
            fontSize = 16.sp,
            color = if (isSelected) Color(0xFF8B5CF6) else Color.Black,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
        )
    }
}

