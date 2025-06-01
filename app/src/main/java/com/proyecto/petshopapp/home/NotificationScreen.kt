package com.proyecto.petshopapp.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.proyecto.petshopapp.R

@Composable
fun NotificationScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf("Activity") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("Notification", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F5F5), RoundedCornerShape(24.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TabButton("Activity", selectedTab == "Activity") {
                selectedTab = "Activity"
            }
            TabButton("Seller Mode", selectedTab == "Seller Mode") {
                selectedTab = "Seller Mode"
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (selectedTab == "Activity") {
            ActivityNotifications()
        } else {
            SellerNotifications()
        }
    }
}

@Composable
fun TabButton(text: String, selected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) Color(0xFF8B5CF6) else Color.Transparent,
            contentColor = if (selected) Color.White else Color.Gray
        ),
        shape = RoundedCornerShape(20.dp),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
        elevation = null
    ) {
        Text(text = text)
    }
}

@Composable
fun ActivityNotifications() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        repeat(4) {
            SwipeableNotificationItem(
                title = "SALE 50%",
                subtitle = "Check the details!",
                image = R.drawable.persian,
                message = "Offer only today 50% on all food!"
            )
        }
    }
}

@Composable
fun SellerNotifications() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        val users = listOf(
            null, // Order
            "Momon", "Ola", "Raul",
            null, null,
            "Vito"
        )
        users.forEach { user ->
            if (user == null) {
                SwipeableNotificationItem(
                    title = "You Got New Order!",
                    subtitle = "Please arrange delivery",
                    image = R.drawable.persian,
                    message = "The order is on the way!"
                )
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.persian),
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = user, fontWeight = FontWeight.Bold)
                        Text(text = "Liked your Product", color = Color.Gray, fontSize = 13.sp)
                    }
                    Image(
                        painter = painterResource(id = R.drawable.persian),
                        contentDescription = null,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                    )
                }
            }
        }
    }
}

@Composable
fun SwipeableNotificationItem(title: String, subtitle: String, image: Int, message: String) {
    var revealed by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    if (dragAmount < -50) revealed = true
                    if (dragAmount > 50) revealed = false
                }
            }
            .background(Color.White)
    ) {
        if (revealed) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(Color(0xFFF1F1F1))
                    .padding(16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(text = message, fontSize = 14.sp, color = Color.Black)
            }
        }

        AnimatedVisibility(visible = !revealed) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Image(
                    painter = painterResource(id = image),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = title, fontWeight = FontWeight.Bold)
                    Text(text = subtitle, color = Color.Gray, fontSize = 13.sp)
                }
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }
        }
    }
}