package com.proyecto.petshopapp.payment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import com.proyecto.petshopapp.payment.components.SaveButton
import com.proyecto.petshopapp.payment.components.PaymentTextField






@Composable
fun AddPaymentScreen(
    viewModel: AddPaymentViewModel = viewModel(),
    onBackPressed: () -> Unit = {},
    onSave: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackPressed) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("Payment Method", style = MaterialTheme.typography.titleMedium)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Add New Payment", style = MaterialTheme.typography.titleSmall)

        Spacer(modifier = Modifier.height(16.dp))

        PaymentTextField(
            value = state.cardNumber,
            label = "Card Number",
            onValueChange = viewModel::onCardNumberChange,
            keyboardType = KeyboardType.Number
        )

        PaymentTextField(
            value = state.cardName,
            label = "Card Name",
            onValueChange = viewModel::onCardNameChange
        )

        PaymentTextField(
            value = state.expiry,
            label = "Expired",
            onValueChange = viewModel::onExpiryChange,
            placeholder = "MM/YY"
        )

        PaymentTextField(
            value = state.cvv,
            label = "CVV",
            onValueChange = viewModel::onCvvChange,
            keyboardType = KeyboardType.NumberPassword
        )

        Spacer(modifier = Modifier.weight(1f))

        SaveButton(
            enabled = state.isFormValid,
            onClick = onSave
        )
    }
}
