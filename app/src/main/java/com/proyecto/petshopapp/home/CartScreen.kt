package com.proyecto.petshopapp.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.proyecto.petshopapp.R
import com.proyecto.petshopapp.local.DatabaseProvider
import com.proyecto.petshopapp.login.LoginViewModel

@Composable
fun CartScreen(navController: NavController, loginViewModel: LoginViewModel) {
    val context = LocalContext.current
    val db = remember { DatabaseProvider.provideDatabase(context) }
    val cartViewModel: CartViewModel = viewModel(factory = CartViewModel.Factory(db.cartDao()))

    val uiState by loginViewModel.uiState.collectAsState()
    val isReseller = uiState.userType == "Revendedor"

    val cartItems = cartViewModel.cartItems.collectAsState().value

    // Calcular precios con descuento aplicado si corresponde
    val subtotal = cartItems.sumOf {
        val finalPrice = if (isReseller) it.price * 0.85 else it.price
        finalPrice * it.quantity
    }
    val tax = subtotal * 0.10
    val total = subtotal + tax

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text("Cart", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        cartItems.forEach { product ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val imageId = context.resources.getIdentifier(product.thumbnail, "drawable", context.packageName)

                    Image(
                        painter = painterResource(id = if (imageId != 0) imageId else R.drawable.banner_product),
                        contentDescription = product.title,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(product.title, fontWeight = FontWeight.Bold)
                        Text(
                            if (product.title.contains("Persian", true)) "For 2-3 years" else "For 1-12 months",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Text("Qty: ${product.quantity}", fontSize = 12.sp, color = Color.Gray)

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            val finalPrice = if (isReseller) product.price * 0.85 else product.price
                            val totalItemPrice = finalPrice * product.quantity

                            Text(
                                "$${"%.2f".format(totalItemPrice).replace('.', ',')}",
                                color = Color(0xFF8B5CF6),
                                fontWeight = FontWeight.Bold
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

                    IconButton(onClick = { cartViewModel.removeFromCart(product) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Column(modifier = Modifier.fillMaxWidth()) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("${cartItems.size} items", color = Color.Gray)
                Text("$${"%.2f".format(subtotal).replace('.', ',')}", color = Color.Gray)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Tax", color = Color.Gray)
                Text("$${"%.2f".format(tax).replace('.', ',')}", color = Color.Gray)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Total", fontWeight = FontWeight.Bold)
                Text("$${"%.2f".format(total).replace('.', ',')}", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5CF6))
            ) {
                Text("Checkout", color = Color.White)
            }
        }
    }
}
