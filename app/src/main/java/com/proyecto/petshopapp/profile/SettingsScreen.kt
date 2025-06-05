package com.proyecto.petshopapp.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.proyecto.petshopapp.login.LoginViewModel

@Composable
fun SettingsScreen(
    navController: NavController,
    loginViewModel: LoginViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = "Settings Page",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Account", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Gray)

        SettingsItem(
            icon = Icons.Default.Person,
            label = "Account",
            onClick = { navController.navigate("profile") }
        )

        SettingsItem(
            icon = Icons.Default.Home,
            label = "Address",
            onClick = { navController.navigate("address") }
        )

        SettingsItem(
            icon = Icons.Default.Notifications,
            label = "Notification",
            onClick = { navController.navigate("notification_options") }
        )

        SettingsItem(
            icon = Icons.Default.CreditCard,
            label = "Payment Method",
            onClick = { navController.navigate("add_payment_method") }
        )

        SettingsItem(
            icon = Icons.Default.PrivacyTip,
            label = "Privacy",
            onClick = { navController.navigate("privacy") }
        )

        SettingsItem(
            icon = Icons.Default.Security,
            label = "Security",
            onClick = { navController.navigate("security") }
        )


        Spacer(modifier = Modifier.height(24.dp))

        Text("Help", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Gray)

        SettingsItem(
            icon = Icons.Default.Call,
            label = "Contact Us",
            onClick = { navController.navigate("contact") }
        )

        SettingsItem(
            icon = Icons.Default.Info,
            label = "FAQ",
            onClick = { navController.navigate("faq") }
        )

        Spacer(modifier = Modifier.height(40.dp))


        Button(
            onClick = {
                FirebaseAuth.getInstance().signOut()
                navController.navigate("login") {
                    popUpTo(0)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp)
                .height(50.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color(0xFF6A5AE0)
            ),
            border = ButtonDefaults.outlinedButtonBorder
        ) {
            Text("Log Out", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp)
            .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color(0xFFF0F0F0), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = Color.Black)
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = label,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )

        Icon(Icons.Default.KeyboardArrowRight, contentDescription = null)
    }
}
