package com.proyecto.petshopapp.payment.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.proyecto.petshopapp.R

@Composable
fun BackButton(navController: NavHostController) {
    Image(
        painter = painterResource(id = R.drawable.back),
        contentDescription = "Back",
        modifier = Modifier
            .size(50.dp)
            .clickable { navController.popBackStack() }
    )
}
