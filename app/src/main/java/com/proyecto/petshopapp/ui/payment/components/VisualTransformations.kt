package com.proyecto.petshopapp.ui.payment.components


import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class CreditCardVisualTransformation : VisualTransformation {
        override fun filter(text: AnnotatedString): TransformedText {
            val originalText = text.text
            val trimmed = if (originalText.length >= 16) originalText.substring(0 until 16) else originalText

            val formatted = StringBuilder()
            val spacePositions = mutableListOf<Int>()

            trimmed.forEachIndexed { index, c ->
                formatted.append(c)
                if ((index + 1) % 4 == 0 && index != trimmed.lastIndex) {
                    formatted.append(' ')
                    spacePositions.add(formatted.length - 1)
                }
            }

            val offsetMapping = object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    var spacesBefore = 0
                    for (spaceIndex in spacePositions) {
                        if (offset + spacesBefore >= spaceIndex) {
                            spacesBefore++
                        }
                    }
                    return offset + spacesBefore
                }

                override fun transformedToOriginal(offset: Int): Int {
                    var rawOffset = offset
                    for (spaceIndex in spacePositions) {
                        if (offset > spaceIndex) {
                            rawOffset--
                        }
                    }
                    return rawOffset.coerceAtMost(trimmed.length)
                }
            }

            return TransformedText(AnnotatedString(formatted.toString()), offsetMapping)
        }
    }
