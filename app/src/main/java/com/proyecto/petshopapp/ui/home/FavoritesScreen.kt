package com.proyecto.petshopapp.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.toObject
import com.proyecto.petshopapp.R
import com.proyecto.petshopapp.data.models.Product
import com.proyecto.petshopapp.ui.login.LoginViewModel
import kotlinx.coroutines.tasks.await

@Composable
fun FavoritesScreen(navController: NavController, loginViewModel: LoginViewModel) {
    val context = LocalContext.current
    val firestore = remember { FirebaseFirestore.getInstance() }
    val currentUser = FirebaseAuth.getInstance().currentUser
    var favorites by remember { mutableStateOf<List<Product>>(emptyList()) }
    var reloadTrigger by remember { mutableStateOf(0) }

    LaunchedEffect(currentUser?.uid, reloadTrigger) {
        currentUser?.let { user ->
            val favsSnapshot = firestore.collection("users")
                .document(user.uid)
                .collection("favoritos")
                .get()
                .await()

            favorites = favsSnapshot.documents.mapNotNull { it.toObject<Product>() }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(WindowInsets.statusBars.asPaddingValues()),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Favorites",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )

            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(favorites) { product ->
                FavoriteProductCard(
                    product = product,
                    navController = navController,
                    loginViewModel = loginViewModel,
                    onFavoriteChanged = {
                        reloadTrigger++
                    }
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
    val isReseller = uiState.userType == "Reseller"
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
                            contentDescription = "View Detail",
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
                    .size(28.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icono_favorito),
                    contentDescription = "Favorite",
                    tint = if (isFavorite) Color.Red else Color.Gray,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}
