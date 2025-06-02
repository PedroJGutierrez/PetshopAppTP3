package com.proyecto.petshopapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.proyecto.petshopapp.payment.components.BackButton
import com.proyecto.petshopapp.payment.components.PaymentOption

@Composable
fun SelectPaymentMethodScreen(navController: NavHostController) {
    var selectedMethod by remember { mutableStateOf("Paypal") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(top = 48.dp)
                .fillMaxWidth()
        ) {
            BackButton(navController)
            Spacer(modifier = Modifier.width(24.dp))
            Text(
                text = "Payment Method",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    lineHeight = (15.sp.value * 1.47).sp,
                    letterSpacing = 0.sp,
                ),
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            "Choose your Payment Method",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(24.dp))

        listOf("Paypal", "Bank Transfer").forEach { method ->
            PaymentOption(
                methodName = method,
                selected = (method == selectedMethod),
                onSelect = { selectedMethod = method }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { navController.navigate("payment_success") },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5CF6))
        ) {
            Text("Checkout", color = Color.White)
        }
    }
}
