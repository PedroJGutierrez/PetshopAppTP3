package com.proyecto.petshopapp.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.proyecto.petshopapp.R
import com.proyecto.petshopapp.data.models.Product
import com.proyecto.petshopapp.local.DatabaseProvider
import com.proyecto.petshopapp.login.LoginViewModel
import kotlinx.coroutines.tasks.await
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ProductDetailScreen(
    product: Product,
    navController: NavController,
    loginViewModel: LoginViewModel
) {
    var quantity by remember { mutableStateOf(1) }
    var isFavorite by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val db = remember { DatabaseProvider.provideDatabase(context) }
    val cartViewModel: CartViewModel = viewModel(factory = CartViewModel.Factory(db.cartDao()))

    val uiState by loginViewModel.uiState.collectAsState()
    val isReseller = uiState.userType == "Revendedor"

    val currentUser = FirebaseAuth.getInstance().currentUser
    val firestore = remember { FirebaseFirestore.getInstance() }

    LaunchedEffect(product.id) {
        currentUser?.let { user ->
            val favRef = firestore.collection("users")
                .document(user.uid)
                .collection("favoritos")
                .document(product.id.toString())

            val snapshot = favRef.get().await()
            isFavorite = snapshot.exists()
        }
    }

    val imageId = context.resources.getIdentifier(product.thumbnail, "drawable", context.packageName)
    val unitPrice = if (isReseller) product.price * 0.85 else product.price
    val totalPrice = unitPrice * quantity

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text("Product Detail", fontWeight = FontWeight.Bold)
            IconButton(onClick = {
                currentUser?.let { user ->
                    val favRef = firestore.collection("users")
                        .document(user.uid)
                        .collection("favoritos")
                        .document(product.id.toString())

                    if (isFavorite) {
                        favRef.delete()
                            .addOnSuccessListener { isFavorite = false }
                            .addOnFailureListener { e -> println("Error al eliminar favorito: $e") }
                    } else {
                        favRef.set(product, SetOptions.merge())
                            .addOnSuccessListener { isFavorite = true }
                            .addOnFailureListener { e -> println("Error al agregar favorito: $e") }
                    }
                }
            }) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Fav",
                    tint = if (isFavorite) Color.Red else Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = if (imageId != 0) imageId else R.drawable.banner_product),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(product.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(product.description, fontSize = 13.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { if (quantity > 1) quantity-- }) {
                    Icon(Icons.Default.Delete, contentDescription = null)
                }
                Text("$quantity", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                IconButton(onClick = { quantity++ }) {
                    Icon(Icons.Default.Add, contentDescription = null)
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "$${"%.2f".format(totalPrice).replace('.', ',')}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )

                if (isReseller) {
                    Spacer(modifier = Modifier.width(6.dp))
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
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                cartViewModel.addToCart(product, quantity)
                navController.navigate("cart")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5CF6))
        ) {
            Text("Add to Cart", color = Color.White)
        }
    }
}
