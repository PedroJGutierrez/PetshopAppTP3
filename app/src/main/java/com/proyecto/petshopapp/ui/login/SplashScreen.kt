package com.proyecto.petshopapp.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.proyecto.petshopapp.R
import kotlinx.coroutines.delay
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip

@Composable
fun SplashScreen(navController: NavController, loginViewModel: LoginViewModel) {
    val uiState by loginViewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(true) {
        loginViewModel.observeFirebaseUser { uid ->
            loginViewModel.fetchUserType(uid)
        }
    }

    LaunchedEffect(uiState.userType) {
        if (uiState.userType != null) {
            delay(1000)
            navController.navigate("home") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }

    LaunchedEffect(uiState) {
        delay(2000)
        if (uiState.userType == null && !uiState.isLoading) {
            navController.navigate("onboarding") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }

    // UI
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.splash),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(300.dp)
                    .clip(CircleShape) // <- redondeado
                    .padding(bottom = 16.dp)
            )
            Text(
                text = "Petshapp!",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6A5ACD)
            )
        }
    }
}

