package com.proyecto.petshopapp.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
import com.proyecto.petshopapp.R
import com.proyecto.petshopapp.data.models.Product
import com.proyecto.petshopapp.data.local.DatabaseProvider
import com.proyecto.petshopapp.ui.login.LoginViewModel
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
    var selectedColor by remember { mutableStateOf("Color") }
    var expanded by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val db = remember { DatabaseProvider.provideDatabase(context) }
    val cartViewModel: CartViewModel = viewModel(factory = CartViewModel.Factory(db.cartDao()))

    val uiState by loginViewModel.uiState.collectAsState()
    val isReseller = uiState.userType == "Reseller"

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Text(
                    "Product Detail",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
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
                        painter = painterResource(id = R.drawable.icono_favorito),
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color.Red else Color.Gray,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Image(
                painter = painterResource(id = if (imageId != 0) imageId else R.drawable.banner_product),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .shadow(1.dp, RoundedCornerShape(16.dp), ambientColor = Color.Black.copy(alpha = 0.08f), spotColor = Color.Black.copy(alpha = 0.08f)),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(product.title, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(product.description, fontSize = 14.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .shadow(elevation = 0.5.dp, shape = RoundedCornerShape(18.dp), ambientColor = Color.Black.copy(alpha = 0.05f), spotColor = Color.Black.copy(alpha = 0.05f))
                        .background(Color(0xFFF8F8F8), RoundedCornerShape(18.dp))
                        .padding(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { if (quantity > 1) quantity-- }) {
                        Text("-", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    }
                    Text(
                        "$quantity",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
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

            if (product.category == "Accessories") {
                Spacer(modifier = Modifier.height(32.dp))

                Text("Seleccioná un color", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(4.dp))

                Box {
                    OutlinedButton(
                        onClick = { expanded = true },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(40.dp)
                    ) {
                        Text(selectedColor, fontSize = 14.sp)
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        listOf("Rojo", "Negro", "Naranja", "Verde").forEach { color ->
                            DropdownMenuItem(
                                text = { Text(color) },
                                onClick = {
                                    selectedColor = color
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(96.dp)) // espacio final para no tapar con el botón
        }

        Button(
            onClick = {
                cartViewModel.addToCart(product, quantity)
                navController.navigate("cart")
            },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .height(60.dp),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5CF6))
        ) {
            Text("Add to cart", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}
