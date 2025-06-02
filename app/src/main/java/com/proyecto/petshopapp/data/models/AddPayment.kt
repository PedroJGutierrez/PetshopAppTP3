package com.proyecto.petshopapp.data.models

data class AddPayment(
    val cardNumber: String = "",
    val cardName: String = "",
    val expiry: String = "",
    val cvv: String = "",
    val isFormValid: Boolean = false
)