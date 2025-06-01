package com.proyecto.petshopapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.proyecto.petshopapp.login.LoginViewModel
import com.proyecto.petshopapp.ui.theme.PetshopAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val loginViewModel: LoginViewModel = viewModel()
            val firebaseUser = FirebaseAuth.getInstance().currentUser
            val uiState by loginViewModel.uiState.collectAsState()

            PetshopAppTheme {
                // Cargar el tipo de usuario si hay usuario logueado
                LaunchedEffect(firebaseUser) {
                    if (firebaseUser != null) {
                        loginViewModel.fetchUserType(firebaseUser.uid)
                    }
                }

                // Navegación según si hay usuario o no
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
