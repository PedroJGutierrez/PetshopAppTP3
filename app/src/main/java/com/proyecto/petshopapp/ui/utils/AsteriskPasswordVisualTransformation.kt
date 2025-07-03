package com.proyecto.petshopapp.ui.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class AsteriskPasswordVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val maskedText = AnnotatedString("*".repeat(text.text.length))
        return TransformedText(maskedText, OffsetMapping.Identity)
    }
}