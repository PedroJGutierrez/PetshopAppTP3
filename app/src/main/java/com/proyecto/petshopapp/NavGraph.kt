package com.proyecto.petshopapp

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.proyecto.petshopapp.home.*
import com.proyecto.petshopapp.login.*

@Composable
fun NavigationGraph(
    navController: NavHostController,
    startDestination: String = "onboarding"
) {
    val productViewModel: ProductViewModel = viewModel()

    NavHost(navController = navController, startDestination = startDestination) {
        composable("onboarding") {
            OnboardingScreen(navController)
        }
        composable("login") {
            LoginScreen(navController)
        }
        composable("create_account") {
            CreateAccountScreen(navController)
        }
        composable("forgot_password") {
            ForgotPasswordScreen(navController)
        }
        composable("forgot_password_confirm") {
            ForgotPasswordConfirmScreen(navController)
        }
        composable("search") {
            SearchScreen(navController)
        }
        composable("notifications") {
            NotificationScreen(navController)
        }
        composable("home") {
            HomeScreen(navController)
        }
        composable("cart") {
            CartScreen(navController)
        }
        composable(
            "product_detail/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId")
            val productsState = productViewModel.products.collectAsState()
            val product = productsState.value.find { it.id == productId }

            if (product != null) {
                ProductDetailScreen(product = product, navController = navController)
            } else {
                Text("Producto no encontrado")
            }
        }
    }
}
