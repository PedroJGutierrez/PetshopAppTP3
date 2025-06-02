package com.proyecto.petshopapp.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.proyecto.petshopapp.R
import com.proyecto.petshopapp.data.models.User

@Composable
fun ProfileScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel = viewModel()
) {
    val user by profileViewModel.user.collectAsState()

    LaunchedEffect(Unit) {
        profileViewModel.loadUserProfile("1") // TODO: pasar el id real del usuario logueado
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(20.dp))
        Image(
            painter = painterResource(id = R.drawable.placeholder_profile), // TODO: Figma profile image
            contentDescription = null,
            modifier = Modifier.size(96.dp).clip(CircleShape)
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = user?.firstName ?: "Nombre",
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
            fontSize = 22.sp
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = user?.email ?: "email@petshop.com",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(20.dp))
        ProfileStatsCard(user = user)
        Spacer(Modifier.height(28.dp))
        ProfileOptionItem("Editar Perfil", Icons.Default.Edit) { navController.navigate("edit_profile") }
        ProfileOptionItem("Modo Vendedor / Estadísticas", Icons.Default.Edit) { navController.navigate("seller_mode") }
        ProfileOptionItem("Historial de Pedidos", Icons.Default.Edit) { navController.navigate("order_history") }
        ProfileOptionItem("Ajustes", Icons.Default.Edit) { navController.navigate("settings") }
        Spacer(Modifier.height(40.dp))
        Button(
            onClick = {
                navController.navigate("login") { popUpTo("home") { inclusive = true } }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Icon(Icons.Default.Logout, contentDescription = null, tint = MaterialTheme.colorScheme.onError)
            Spacer(Modifier.width(8.dp))
            Text("Cerrar Sesión", color = MaterialTheme.colorScheme.onError)
        }
    }
}