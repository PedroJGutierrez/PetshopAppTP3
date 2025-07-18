package com.proyecto.petshopapp.ui.navigation

import PaymentSuccessScreen
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.proyecto.petshopapp.ui.payment.AddPaymentScreen
import com.proyecto.petshopapp.ui.screens.SelectPaymentMethodScreen
import androidx.compose.ui.platform.LocalContext
import com.proyecto.petshopapp.OnboardingScreen
import com.proyecto.petshopapp.data.local.DatabaseProvider
import com.proyecto.petshopapp.ui.home.BestSellersScreen
import com.proyecto.petshopapp.ui.home.CartScreen
import com.proyecto.petshopapp.ui.home.CartViewModel
import com.proyecto.petshopapp.ui.home.FavoritesScreen
import com.proyecto.petshopapp.ui.home.HomeScreen
import com.proyecto.petshopapp.ui.home.NotificationScreen
import com.proyecto.petshopapp.ui.home.OrdersScreen
import com.proyecto.petshopapp.ui.home.ProductDetailScreen
import com.proyecto.petshopapp.ui.home.ProductViewModel
import com.proyecto.petshopapp.ui.home.RecordsScreen
import com.proyecto.petshopapp.ui.home.SearchScreen
import com.proyecto.petshopapp.ui.profile.AddressScreen
import com.proyecto.petshopapp.ui.profile.ContactUsScreen
import com.proyecto.petshopapp.ui.profile.FAQScreen
import com.proyecto.petshopapp.ui.profile.NotificationOptionsScreen
import com.proyecto.petshopapp.ui.profile.PrivacyPolicyScreen
import com.proyecto.petshopapp.ui.profile.ProfileScreen
import com.proyecto.petshopapp.ui.profile.SecurityScreen
import com.proyecto.petshopapp.ui.profile.SettingsScreen
import com.proyecto.petshopapp.ui.login.CreateAccountScreen
import com.proyecto.petshopapp.ui.login.ForgotPasswordConfirmScreen
import com.proyecto.petshopapp.ui.login.ForgotPasswordScreen
import com.proyecto.petshopapp.ui.login.LoginScreen
import com.proyecto.petshopapp.ui.login.LoginViewModel
import com.proyecto.petshopapp.ui.login.SplashScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    startDestination: String = "splash"
) {
    val productViewModel: ProductViewModel = viewModel()
    val context = LocalContext.current
    val db = remember { DatabaseProvider.provideDatabase(context) }
    val cartViewModel: CartViewModel = viewModel(factory = CartViewModel.Factory(db.cartDao()))


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
        composable("profile") {
            ProfileScreen(navController = navController, loginViewModel = loginViewModel)
        }
        composable("forgot_password") {
            ForgotPasswordScreen(navController)
        }
        composable("security") {
            SecurityScreen(
                navController = navController,
                loginViewModel = loginViewModel
            )
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
        composable("notification_options") {
            NotificationOptionsScreen(onBack = { navController.popBackStack() })
        }
        composable("address") {
            AddressScreen(
                onBack = { navController.popBackStack() },
                loginViewModel = loginViewModel
            )
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
        composable("settings") {
            SettingsScreen(navController = navController, loginViewModel = loginViewModel)
        }
        composable("payment_success") {
            PaymentSuccessScreen(navController = navController,
                cartViewModel = cartViewModel)
        }
        composable("add_payment_method") {
            AddPaymentScreen(
                onBackPressed = { navController.popBackStack() },
                onNavigateHome = {
                    navController.navigate("home")
                }
            )
        }
        composable("best_sellers") {
            BestSellersScreen(navController, loginViewModel)
        }
        composable("faq") {
            FAQScreen(navController = navController)
        }
        composable("privacy") {
            PrivacyPolicyScreen(navController = navController)
        }
        composable("favorites") {
            FavoritesScreen(navController = navController, loginViewModel = loginViewModel)
        }
        composable("contact") {
            ContactUsScreen(onBack = { navController.popBackStack() })
        }
        composable("cart") {
            CartScreen(
                navController = navController,
                loginViewModel = loginViewModel,
                cartViewModel = cartViewModel
            )
        }
        composable("records") { RecordsScreen(onBack = { navController.popBackStack() }) }
        composable("orders") { OrdersScreen(onBack = { navController.popBackStack() }) }
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
