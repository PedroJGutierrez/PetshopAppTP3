package com.proyecto.petshopapp.ui.settings
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SettingsScreen(navController: NavController) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Ajustes", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(24.dp))
        Button(onClick = { navController.navigate("change_password") }, modifier = Modifier.fillMaxWidth()) {
            Text("Cambiar contraseña")
        }
        Spacer(Modifier.height(8.dp))
        Button(onClick = { navController.navigate("change_email") }, modifier = Modifier.fillMaxWidth()) {
            Text("Cambiar email")
        }
        Spacer(Modifier.height(8.dp))
        Button(onClick = { navController.navigate("faq") }, modifier = Modifier.fillMaxWidth()) {
            Text("FAQ")
        }
        Spacer(Modifier.height(8.dp))
        Button(onClick = { navController.navigate("privacy_policy") }, modifier = Modifier.fillMaxWidth()) {
            Text("Política de Privacidad")
        }
        Spacer(Modifier.height(8.dp))
        Button(onClick = { navController.navigate("terms") }, modifier = Modifier.fillMaxWidth()) {
            Text("Términos de uso")
        }
    }
}