package com.proyecto.petshopapp.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class NotificationData(val title: String, val subtitle: String, val image: Int)

@Composable
fun NotificationScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf("Activity") }
    var showSnackbar by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val activityNotifications = remember {
        mutableStateListOf(
            NotificationData("SALE 50%", "Check the details!", R.drawable.persian),
            NotificationData("FLASH DEAL", "Today only!", R.drawable.persian),
            NotificationData("EXTRA 20%", "Use code PET20", R.drawable.persian),
            NotificationData("BUY 1 GET 1", "Limited time", R.drawable.persian)
        )
    }

    val sellerNotifications = remember {
        mutableStateListOf(
            NotificationData("You Got New Order!", "Please arrange delivery", R.drawable.persian),
            NotificationData("Momon", "Liked your Product", R.drawable.foto4),
            NotificationData("Ola", "Liked your Product", R.drawable.foto2),
            NotificationData("Eli", "Liked your Product", R.drawable.foto3)
        )
    }

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
            NotificationList(
                notifications = activityNotifications,
                onItemLongPressed = { index ->
                    activityNotifications.removeAt(index)
                    showSnackbar = true
                    coroutineScope.launch {
                        delay(2500)
                        showSnackbar = false
                    }
                }
            )
        } else {
            NotificationList(
                notifications = sellerNotifications,
                onItemLongPressed = { index ->
                    sellerNotifications.removeAt(index)
                    showSnackbar = true
                    coroutineScope.launch {
                        delay(2500)
                        showSnackbar = false
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (showSnackbar) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Row(
                    modifier = Modifier
                        .background(Color(0xFFDFF7DF), RoundedCornerShape(16.dp))
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Notification deleted",
                        color = Color(0xFF2E7D32),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF4CAF50)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
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
fun NotificationList(
    notifications: List<NotificationData>,
    onItemLongPressed: (Int) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        notifications.forEachIndexed { index, notification ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onLongPress = { onItemLongPressed(index) }
                        )
                    }
                    .background(Color.White)
                    .padding(8.dp)
            ) {
                Image(
                    painter = painterResource(id = notification.image),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = notification.title, fontWeight = FontWeight.Bold)
                    Text(text = notification.subtitle, color = Color.Gray, fontSize = 13.sp)
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
