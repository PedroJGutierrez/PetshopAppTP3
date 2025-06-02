package com.petlovers.petshopapp.ui.sellerProfile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.petlovers.petshopapp.model.User

@Composable
fun SellerProfileScreen(viewModel: SellerProfileViewModel = viewModel()) {
    val user by viewModel.user.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // TODO: Replace with real image from Figma
        Image(
            painter = painterResource(id = R.drawable.placeholder_profile),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(text = user.name, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        SellerStatsCard(user)

        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = { /* TODO: Edit profile */ }) {
            Text("Edit Profile")
        }
    }
}