package com.proyecto.petshopapp.payment


import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.proyecto.petshopapp.data.models.PaymentMethod

@Composable
fun PaymentMethodItem(
    method: PaymentMethod,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .border(
                1.dp,
                if (isSelected) Color(0xFF8B5CF6) else Color.LightGray,
                RoundedCornerShape(12.dp)
            )
            .clickable(enabled = method.isEnabled, onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = method.name,
            modifier = Modifier.weight(1f),
            color = if (method.isEnabled) Color.Black else Color.Gray
        )
        RadioButton(
            selected = isSelected,
            onClick = { if (method.isEnabled) onClick() },
            enabled = method.isEnabled,
            colors = RadioButtonDefaults.colors(
                selectedColor = Color(0xFF8B5CF6),
                unselectedColor = Color.LightGray,
                disabledSelectedColor = Color.Gray,
                disabledUnselectedColor = Color.LightGray
            )
        )
    }
}
