package com.proyecto.petshopapp.navigation
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.proyecto.petshopapp.ui.profile.*
import com.proyecto.petshopapp.ui.settings.*

fun NavGraphBuilder.profileGraph(navController: NavController) {
    composable("profile") { ProfileScreen(navController) }
    composable("edit_profile") { EditProfileScreen(navController) }
    composable("seller_mode") { SellerModeScreen(navController) }
    composable("order_history") { OrderHistoryScreen(navController) }
    composable("settings") { SettingsScreen(navController) }
    composable("change_password") { ChangePasswordScreen(navController) }
    composable("change_email") { ChangeEmailScreen(navController) }
    composable("faq") { FAQScreen(navController) }
    composable("privacy_policy") { PrivacyPolicyScreen(navController) }
    composable("terms") { TermsScreen(navController) }
}