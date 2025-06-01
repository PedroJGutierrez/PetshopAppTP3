package com.proyecto.petshopapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.proyecto.petshopapp.ui.theme.PetshopAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val startDestination = if (firebaseUser != null && firebaseUser.isEmailVerified) {
            "home"
        } else {
            "onboarding"
        }

        setContent {
            val navController = rememberNavController()
            PetshopAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        NavigationGraph(navController = navController, startDestination = startDestination)
                    }
                }
            }
        }
    }
}
