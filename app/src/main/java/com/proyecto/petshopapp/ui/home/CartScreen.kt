package com.proyecto.petshopapp.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.proyecto.petshopapp.R
import com.proyecto.petshopapp.data.local.DatabaseProvider
import com.proyecto.petshopapp.ui.login.LoginViewModel

@Composable
fun CartScreen(
    navController: NavController,
    loginViewModel: LoginViewModel,
    cartViewModel: CartViewModel
) {
    val context = LocalContext.current
    val db = remember { DatabaseProvider.provideDatabase(context) }
    val cartViewModel: CartViewModel = viewModel(factory = CartViewModel.Factory(db.cartDao()))

    val uiState by loginViewModel.uiState.collectAsState()
    val isReseller = uiState.userType == "Reseller"

    val cartItems = cartViewModel.cartItems.collectAsState().value

    var showDeleteForId by remember { mutableStateOf<Int?>(null) }

    val subtotal = cartItems.sumOf {
        val finalPrice = if (isReseller) it.price * 0.85 else it.price
        finalPrice * it.quantity
    }
    val tax = subtotal * 0.10
    val total = subtotal + tax
    val paymentMethods = listOf("Paypal", "Bank Transfer")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Navbar con botón volver y título centrado y más abajo
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                "Cart",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 24.dp, bottom = 12.dp)
                    .weight(2f)
            )
            Spacer(modifier = Modifier.weight(1f))
            // Para balancear el icono de volver y que quede centrado el texto
            Spacer(modifier = Modifier.size(48.dp))
        }

        Spacer(modifier = Modifier.height(8.dp))

        cartItems.forEach { product ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .shadow(1.dp, RoundedCornerShape(18.dp))
                    .clickable {
                        showDeleteForId = if (showDeleteForId == product.id) null else product.id
                    },
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color.White)
                        .clickable { showDeleteForId = if (showDeleteForId == product.id) null else product.id },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val imageId = context.resources.getIdentifier(product.thumbnail, "drawable", context.packageName)

                    // CONTENIDO PRINCIPAL
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = if (imageId != 0) imageId else R.drawable.banner_product),
                            contentDescription = product.title,
                            modifier = Modifier
                                .size(60.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
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
                    }

                    if (showDeleteForId == product.id) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .width(60.dp)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(topEnd = 18.dp, bottomEnd = 18.dp))
                                .background(Color(0xFFF5F5F5))
                                .clickable {
                                    cartViewModel.removeFromCart(product)
                                    showDeleteForId = null
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.icono_delete),
                                contentDescription = "Delete",
                                tint = Color.Red,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("${cartItems.size} items", color = Color.Gray)
                Text("$${"%.2f".format(subtotal).replace('.', ',')}", color = Color.Gray)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Tax", color = Color.Gray)
                Text("$${"%.2f".format(tax).replace('.', ',')}", color = Color.Gray)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total", fontWeight = FontWeight.Bold)
                Text("$${"%.2f".format(total).replace('.', ',')}", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (paymentMethods.isEmpty()) {
                        navController.navigate("add_payment_method")
                    } else {
                        navController.navigate("select_payment_method")
                    }
                },
                enabled = cartItems.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5CF6))
            ) {
                Text("Checkout", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
