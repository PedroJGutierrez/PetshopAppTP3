package com.proyecto.petshopapp.ui.home

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.proyecto.petshopapp.ui.login.LoginViewModel
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.proyecto.petshopapp.ui.theme.PurplePrimary


@Composable
fun HomeScreen(navController: NavController, loginViewModel: LoginViewModel) {
    val addressViewModel: AddressViewModel = viewModel()
    val userAddresses = addressViewModel.userAddresses

    var selectedLocation by remember { mutableStateOf("") }
    val persistedLocation by addressViewModel.selectedLocation

    LaunchedEffect(persistedLocation, userAddresses) {
        if (selectedLocation.isEmpty()) {
            selectedLocation = if (persistedLocation.isNotEmpty()) {
                persistedLocation
            } else {
                userAddresses.firstOrNull() ?: ""
            }
        }
    }

    var selectedCategory by remember { mutableStateOf("All") }
    var showLocationModal by remember { mutableStateOf(false) }
    var showCategoryModal by remember { mutableStateOf(false) }

    val uiState by loginViewModel.uiState.collectAsState()
    val userType = uiState.userType ?: "Usuario"

    val productViewModel: ProductViewModel = viewModel()
    val products by productViewModel.products.collectAsState()

    val context = LocalContext.current

    BackHandler {
        (context as? Activity)?.moveTaskToBack(true)
    }

    val filteredProducts = if (selectedCategory.isEmpty() || selectedCategory == "All") {
        products
    } else {
        products.filter { it.category.equals(selectedCategory, ignoreCase = true) }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentPadding = PaddingValues(bottom = 120.dp) // espacio para BottomNavBar fijo
        ) {
            item {
                Spacer(modifier = Modifier.height(26.dp))
                HeaderSection(
                    location = selectedLocation,
                    onLocationClick = { showLocationModal = true },
                    navController = navController
                )
                Spacer(modifier = Modifier.height(12.dp))
                PromotionalBanner()
                Spacer(modifier = Modifier.height(10.dp))
                CategorySection(
                    selectedCategory = selectedCategory,
                    categories = listOf("", "Food", "Toys", "Accessories"),
                    onCategorySelect = { selectedCategory = it },
                    onViewAllClick = { showCategoryModal = true }
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
            item {
                BestSellerSection(
                    products = filteredProducts,
                    navController = navController,
                    loginViewModel = loginViewModel,
                    onViewAllClick = { navController.navigate("best_sellers") }
                )
            }
        }

        BottomNavigationBar(
            navController = navController,
            viewModel = loginViewModel,
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        if (showLocationModal) {
            LocationModal(
                locations = userAddresses,
                selectedLocation = selectedLocation,
                onLocationSelect = {
                    selectedLocation = it
                    showLocationModal = false
                },
                onDismiss = { showLocationModal = false }
            )
        }

        if (showCategoryModal) {
            CategoryModal(
                categories = listOf("", "Food", "Toys", "Accessories"),
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
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(1f)
                .clickable { onLocationClick() }
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = location,
                fontSize = 14.sp,
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }

        Row {
            IconButton(onClick = { navController.navigate("search") }) {
                Image(
                    painter = painterResource(id = R.drawable.icono_buscar),
                    contentDescription = "Search",
                    modifier = Modifier.size(20.dp)
                )
            }
            IconButton(onClick = {
                navController.navigate("notifications")
            }) {
                Image(
                    painter = painterResource(id = R.drawable.icono_noti),
                    contentDescription = "Notifications",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}


@Composable
fun PromotionalBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .padding(horizontal = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .align(Alignment.Center)
        ) {
            Image(
                painter = painterResource(id = R.drawable.banner_background),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(0.45f))

                Column(
                    modifier = Modifier
                        .weight(0.55f)
                        .padding(start = 24.dp)
                ) {
                    Text(
                        text = "Royal Canin \nAdult Pomeranian",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Get an interesting promo here, without conditions",
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 0.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.banner_product),
                contentDescription = null,
                modifier = Modifier
                    .size(240.dp)
                    .offset(x = (-16).dp),
                contentScale = ContentScale.Fit
            )
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
                    color = Color(0xFF8B5CF6),
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(end = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(categories) { index, category ->
                CategoryChip(
                    text = category,
                    isSelected = category == selectedCategory,
                    isFirst = index == 0,
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
    isFirst: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) Color(0xFF8B5CF6) else Color(0xFFF3F4F6)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = if (isFirst) 14.dp else 20.dp, vertical = 10.dp)
                .wrapContentWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                color = if (isSelected) Color.White else Color.Gray,
                fontSize = 14.sp
            )
            if (isFirst) {
                Icon(
                    painter = painterResource(id = R.drawable.icono_swap),
                    contentDescription = "Swap icon",
                    tint = if (isSelected) Color.White else Color.Black,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun BestSellerSection(
    products: List<Product>,
    navController: NavController,
    loginViewModel: LoginViewModel,
    onViewAllClick: () -> Unit = {
        navController.navigate("best_sellers")
    }
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Best Sellers",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            TextButton(onClick = onViewAllClick) {
                Text(
                    text = "View All",
                    color = Color(0xFF8B5CF6),
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 160.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 240.dp, max = 800.dp)
        ) {
            items(products) { product ->
                ProductCard(
                    product = product,
                    navController = navController,
                    loginViewModel = loginViewModel
                )
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
    val isReseller = uiState.userType == "Reseller"
    val discountedPrice = if (isReseller) product.price * 0.85 else product.price

    Card(
        modifier = Modifier
            .width(147.dp)
            .height(200.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8)),
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
                        fontSize = 20.sp,
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
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .padding(horizontal = 6.dp),
                                contentAlignment = Alignment.TopCenter
                            ) {
                                Text(
                                    text = "15% OFF",
                                    color = Color.White,
                                    fontSize = 10.sp
                                )
                            }
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
                        contentDescription = "Add",
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
    var expanded by remember { mutableStateOf(false) }

    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route

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

