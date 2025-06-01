package com.proyecto.petshopapp.home

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun SearchScreen(navController: NavController, productViewModel: ProductViewModel = viewModel()) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    val categories = listOf("Food", "Toys", "Accessories")

    val allProducts by productViewModel.products.collectAsState()

    val filteredProducts = remember(searchQuery, selectedCategory, allProducts) {
        allProducts.filter {
            (searchQuery.isNotBlank() &&
                    it.title.lowercase().startsWith(searchQuery.lowercase())) &&
                    (selectedCategory == null || it.category == selectedCategory)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // Top bar
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("Search", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search input
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search your Product") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Category filter
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.FilterList, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            categories.forEach { category ->
                val selected = selectedCategory == category
                TextButton(
                    onClick = {
                        selectedCategory = if (selected) null else category
                    },
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = if (selected) Color(0xFF8B5CF6) else Color(0xFFF5F5F5),
                        contentColor = if (selected) Color.White else Color.Gray
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.padding(horizontal = 4.dp)
                ) {
                    Text(category)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Resultados SOLO si hay texto en el input
        if (searchQuery.isNotBlank()) {
            if (filteredProducts.isNotEmpty()) {
                LazyColumn {
                    items(filteredProducts.size) { index ->
                        val product = filteredProducts[index]
                        val context = LocalContext.current
                        val imageId = remember(product.thumbnail) {
                            context.resources.getIdentifier(product.thumbnail, "drawable", context.packageName)
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate("product_detail/${product.id}")
                                }
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (imageId != 0) {
                                Image(
                                    painter = painterResource(id = imageId),
                                    contentDescription = product.title,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Pets,
                                    contentDescription = "Sin imagen",
                                    modifier = Modifier.size(40.dp),
                                    tint = Color.Gray
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(product.title, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                                Text("$${product.price}", fontSize = 14.sp, color = Color.Gray)
                            }
                        }
                    }
                }
            } else {
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("No se encontró su producto.", color = Color.Gray)
                }
            }
        }
    }
}
