package com.proyecto.petshopapp.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ChangePasswordScreen(navController: NavController) {
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Cambiar contraseña", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(20.dp))
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Nueva contraseña") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = confirm, onValueChange = { confirm = it }, label = { Text("Confirmar") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { /* TODO: Cambiar contraseña */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar")
        }
    }
}