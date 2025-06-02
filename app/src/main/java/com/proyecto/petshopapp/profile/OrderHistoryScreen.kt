package com.proyecto.petshopapp.ui.profile
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun OrderHistoryScreen(navController: NavController) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Historial de Pedidos", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(20.dp))
        Text("Acá se muestra la lista de compras realizadas por el usuario.")
    }
}