package com.proyecto.petshopapp.payment

import androidx.lifecycle.ViewModel
import com.proyecto.petshopapp.data.models.AddPayment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AddPaymentViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AddPayment())
    val uiState: StateFlow<AddPayment> = _uiState.asStateFlow()

    fun onCardNumberChange(value: String) {
        _uiState.value = _uiState.value.copy(cardNumber = value)
        validateForm()
    }

    fun onCardNameChange(value: String) {
        _uiState.value = _uiState.value.copy(cardName = value)
        validateForm()
    }

    fun onExpiryChange(value: String) {
        _uiState.value = _uiState.value.copy(expiry = value)
        validateForm()
    }

    fun onCvvChange(value: String) {
        _uiState.value = _uiState.value.copy(cvv = value)
        validateForm()
    }

    private fun validateForm() {
        val state = _uiState.value
        val isValid = state.cardNumber.length == 16 &&
                state.cardName.isNotBlank() &&
                Regex("""\d{2}/\d{2}""").matches(state.expiry) &&
                state.cvv.length == 3
        _uiState.value = state.copy(isFormValid = isValid)
    }
}
