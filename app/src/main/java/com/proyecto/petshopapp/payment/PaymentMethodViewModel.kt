package com.proyecto.petshopapp.payment


import androidx.lifecycle.ViewModel
import com.proyecto.petshopapp.data.models.PaymentMethod
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PaymentMethodViewModel : ViewModel() {

    private val _methods = listOf(
        PaymentMethod("Paypal", isEnabled = true),
        PaymentMethod("Bank Transfer", isEnabled = false)
    )

    private val _selectedMethod = MutableStateFlow(_methods.first())
    val selectedMethod: StateFlow<PaymentMethod> = _selectedMethod

    val paymentMethods: List<PaymentMethod> = _methods

    fun selectMethod(method: PaymentMethod) {
        if (method.isEnabled) {
            _selectedMethod.value = method
        }
    }
}
