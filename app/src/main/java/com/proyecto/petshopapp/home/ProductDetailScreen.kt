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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.proyecto.petshopapp.R
import com.proyecto.petshopapp.data.models.Product
import com.proyecto.petshopapp.local.DatabaseProvider

@Composable
fun ProductDetailScreen(
    product: Product,
    navController: NavController
) {
    var quantity by remember { mutableStateOf(1) }
    var isFavorite by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val db = remember { DatabaseProvider.provideDatabase(context) }
    val cartViewModel: CartViewModel = viewModel(factory = CartViewModel.Factory(db.cartDao()))

    val imageId = context.resources.getIdentifier(product.thumbnail, "drawable", context.packageName)
    val totalPrice = product.price * quantity

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
            IconButton(onClick = { isFavorite = !isFavorite }) {
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

            Text(
                "$${"%.2f".format(totalPrice).replace('.', ',')}",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
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
