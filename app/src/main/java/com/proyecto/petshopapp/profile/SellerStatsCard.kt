package com.proyecto.petshopapp.profile
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.proyecto.petshopapp.data.models.User

@Composable
fun SellerStatsCard(user: User) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
            StatColumn("Sales", user.sales.toString())
            StatColumn("Followers", user.followers.toString())
            StatColumn("Following", user.following.toString())
        }
    }
}

