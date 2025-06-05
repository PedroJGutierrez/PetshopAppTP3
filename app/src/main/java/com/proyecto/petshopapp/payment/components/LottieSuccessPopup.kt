package com.proyecto.petshopapp.payment.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import kotlinx.coroutines.delay

@Composable
fun LottieSuccessPopup(
    message: String,
    onDismiss: () -> Unit
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("success_check.json"))
    val progress by animateLottieCompositionAsState(composition)

    LaunchedEffect(composition) {
        if (composition != null) {
            delay(2000)
            onDismiss()
        }
    }

    if (composition != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                tonalElevation = 6.dp,
                shadowElevation = 12.dp,
                color = Color.White,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(32.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LottieAnimation(
                        composition = composition,
                        progress = progress,
                        modifier = Modifier.size(120.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
                        color = Color.Black
                    )
                }
            }
        }
    }
}
