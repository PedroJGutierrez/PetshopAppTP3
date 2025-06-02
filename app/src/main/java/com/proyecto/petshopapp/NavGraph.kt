package com.proyecto.petshopapp

import PaymentSuccessScreen
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.proyecto.petshopapp.home.*
import com.proyecto.petshopapp.login.*
import com.proyecto.petshopapp.payment.AddPaymentScreen
import com.proyecto.petshopapp.ui.screens.SelectPaymentMethodScreen


@Composable
fun NavigationGraph(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    startDestination: String = "splash"
) {
    val productViewModel: ProductViewModel = viewModel()

    NavHost(navController = navController, startDestination = startDestination) {
        composable("splash") {
            SplashScreen(navController, loginViewModel)
        }
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
            SearchScreen(navController = navController, productViewModel = productViewModel)
        }
        composable("notifications") {
            NotificationScreen(navController)
        }
        composable("home") {
            val uiState = loginViewModel.uiState.collectAsState()

            if (uiState.value.userType != null) {
                HomeScreen(navController = navController, loginViewModel = loginViewModel)
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
        composable("select_payment_method") {
            SelectPaymentMethodScreen(navController)
        }
        composable("payment_success") {
            PaymentSuccessScreen(navController)
        }
        composable("add_payment_method") {
            AddPaymentScreen(
                onBackPressed = { navController.popBackStack() },
                onSave = {
                    navController.popBackStack()
                }
            )
        }
        composable("favorites") {
            FavoritesScreen(navController = navController, loginViewModel = loginViewModel)
        }
        composable("cart") {
            CartScreen(navController = navController, loginViewModel = loginViewModel)
        }
        composable(
            "product_detail/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId")
            val productsState = productViewModel.products.collectAsState()
            val product = productsState.value.find { it.id == productId }

            if (product != null) {
                ProductDetailScreen(
                    product = product,
                    navController = navController,
                    loginViewModel = loginViewModel
                )
            } else {
                Text("Producto no encontrado")
            }
        }
    }
}
