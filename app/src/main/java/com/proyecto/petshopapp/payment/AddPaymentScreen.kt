package com.proyecto.petshopapp.payment

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.proyecto.petshopapp.payment.components.PaymentTextField
import com.proyecto.petshopapp.payment.components.SaveButton
import com.proyecto.petshopapp.payment.components.TopBarWithBack

@Composable
fun AddPaymentScreen(
    onBackPressed: () -> Unit,
    viewModel: AddPaymentViewModel = viewModel(),
    onSave: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        TopBarWithBack(
            title = "Payment Method",
            onBack = onBackPressed
        )

        Text(
            text = "Add New Payment",
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        PaymentTextField(
            value = state.cardNumber,
            label = "Card Number",
            onValueChange = viewModel::onCardNumberChange,
            keyboardType = KeyboardType.Number
        )

        Spacer(modifier = Modifier.height(12.dp))

        PaymentTextField(
            value = state.cardName,
            label = "Card Name",
            onValueChange = viewModel::onCardNameChange
        )

        Spacer(modifier = Modifier.height(12.dp))

        PaymentTextField(
            value = state.expiry,
            label = "Expired",
            onValueChange = viewModel::onExpiryChange,
            placeholder = "MM/YY"
        )

        Spacer(modifier = Modifier.height(12.dp))

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
