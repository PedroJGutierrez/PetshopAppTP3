package com.proyecto.petshopapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.proyecto.petshopapp.ui.login.LoginViewModel
import com.proyecto.petshopapp.ui.navigation.NavigationGraph
import com.proyecto.petshopapp.ui.theme.PetshopAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val loginViewModel: LoginViewModel = hiltViewModel()
            val uiState by loginViewModel.uiState.collectAsState()
            val firebaseUser = loginViewModel.getCurrentUser() // ✅ Usamos la instancia inyectada

            PetshopAppTheme {

                LaunchedEffect(firebaseUser) {
                    firebaseUser?.let {
                        loginViewModel.fetchUserType(it.uid)
                    }
                }

                LaunchedEffect(firebaseUser, uiState.userType) {
                    if (firebaseUser != null && uiState.userType != null) {
                        navController.navigate("home") {
                            popUpTo(0)
                        }
                    } else if (firebaseUser == null) {
                        navController.navigate("onboarding") {
                            popUpTo(0)
                        }
                    }
                }

                NavigationGraph(
                    navController = navController,
                    loginViewModel = loginViewModel
                )
            }
        }
    }
}
