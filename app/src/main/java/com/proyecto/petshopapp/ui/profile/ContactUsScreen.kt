package com.proyecto.petshopapp.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proyecto.petshopapp.R



@Composable
fun ContactUsScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // Header
        val poppinsFont = FontFamily(Font(R.font.poppins_regular))
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Contact Us",
                style = TextStyle(
                    fontFamily = poppinsFont,
                    fontSize = 20.sp,
                    color = Color.Black
                )
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Contactos:",
            style = TextStyle(
                fontFamily = poppinsFont,
                fontSize = 14.sp,
                color = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Pedro Gutierrez - pedrojgutierrez65@gmail.com",
            style = TextStyle(
                fontFamily = poppinsFont,
                fontSize = 14.sp,
                color = Color.DarkGray
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Juan Martín Frick - juanmartinfrick@gmail.com",
            style = TextStyle(
                fontFamily = poppinsFont,
                fontSize = 14.sp,
                color = Color.DarkGray
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Nicolas Lell - Nicolell390@gmail.com",
            style = TextStyle(
                fontFamily = poppinsFont,
                fontSize = 14.sp,
                color = Color.DarkGray
            )
        )
    }
}
