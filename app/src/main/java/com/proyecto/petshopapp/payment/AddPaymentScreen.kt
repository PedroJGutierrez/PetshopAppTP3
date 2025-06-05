package com.proyecto.petshopapp.payment

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.proyecto.petshopapp.payment.components.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AddPaymentScreen(
    onBackPressed: () -> Unit,
    viewModel: AddPaymentViewModel = viewModel(),
    onNavigateHome: () -> Unit
) {
    var cardNumberError by remember { mutableStateOf(false) }
    var cardNameError by remember { mutableStateOf(false) }
    var cvvError by remember { mutableStateOf(false) }
    var expiryError by remember { mutableStateOf(false) }
    var showPopup by remember { mutableStateOf(false) }

    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopBarWithBack(
                title = "Payment Method",
                onBack = onBackPressed
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "Add New Payment",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                PaymentTextField(
                    value = state.cardNumber,
                    label = "Card Number",
                    onValueChange = { input ->
                        val cleanedInput = input.replace(" ", "")
                        if (cleanedInput.length <= 16 && cleanedInput.all { it.isDigit() }) {
                            cardNumberError = false
                            viewModel.onCardNumberChange(cleanedInput)
                        } else {
                            cardNumberError = true
                        }
                    },
                    keyboardType = KeyboardType.Number,
                    isError = cardNumberError,
                    supportingText = if (cardNumberError) "Must be 16 digits" else null,
                    visualTransformation = CreditCardVisualTransformation()
                )

                PaymentTextField(
                    value = state.cardName,
                    label = "Card Name",
                    onValueChange = {
                        if (it.all { char -> char.isLetter() || char == ' ' }) {
                            cardNameError = false
                            viewModel.onCardNameChange(it)
                        } else {
                            cardNameError = true
                        }
                    },
                    isError = cardNameError,
                    supportingText = if (cardNameError) "Only letters and spaces allowed" else null
                )

                PaymentTextField(
                    value = state.expiry,
                    label = "Expiry Date",
                    onValueChange = { input ->
                        if (input.length <= 5) {
                            viewModel.onExpiryChange(input)

                            val regexFull = Regex("""^(0[1-9]|1[0-2])\/(\d{2})$""")
                            val regexPartial = Regex("""^(0[1-9]|1[0-2])\/?(\d{0,2})$""")

                            expiryError = if (input.isEmpty()) {
                                false
                            } else if (input.length < 5) {
                                !regexPartial.matches(input)
                            } else {
                                if (regexFull.matches(input)) {
                                    val month = input.substring(0, 2).toInt()
                                    val year = input.substring(3, 5).toInt()
                                    val currentYear = 25
                                    val maxYear = 40
                                    !(month in 1..12 && year in currentYear..maxYear)
                                } else {
                                    true
                                }
                            }
                        }
                    },
                    placeholder = "MM/YY",
                    isError = expiryError,
                    supportingText = if (expiryError) "Invalid format or date out of range" else null,
                    keyboardType = KeyboardType.Number
                )

                PaymentTextField(
                    value = state.cvv,
                    label = "CVV",
                    onValueChange = {
                        if (it.length <= 3 && it.all { char -> char.isDigit() }) {
                            cvvError = false
                            viewModel.onCvvChange(it)
                        } else {
                            cvvError = true
                        }
                    },
                    keyboardType = KeyboardType.NumberPassword,
                    isError = cvvError,
                    supportingText = if (cvvError) "Must be 3 digits" else null,
                    visualTransformation = PasswordVisualTransformation()
                )

                Spacer(modifier = Modifier.weight(1f))

                SaveButton(
                    enabled = state.isFormValid,
                    onClick = {
                        showPopup = true
                        coroutineScope.launch {
                            delay(1800)
                            onNavigateHome()
                        }
                    }
                )
            }

            if (showPopup) {

                    LottieSuccessPopup(
                        message = "Payment method saved!",
                        onDismiss = { showPopup = false }
                    )
                }

        }
    }
}
