package com.proyecto.petshopapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.pager.*
import com.proyecto.petshopapp.ui.theme.PurplePrimary
import kotlinx.coroutines.launch

data class OnboardingPage(
    val title: String,
    val description: String,
    val imageRes: Int
)

val pages = listOf(
    OnboardingPage(
        "Meet your animal needs here",
        "Get interesting promos here, register your account immediately so you can meet your animal needs.",
        R.drawable.welcome_image
    ),
    OnboardingPage(
        "Find the best care",
        "Discover nearby services for pets and receive alerts.",
        R.drawable.ninia_gato
    ),
    OnboardingPage(
        "Start now",
        "Create your account and never miss a pet promotion again.",
        R.drawable.abuela_salchi
    )
)

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnboardingScreen(navController: NavController) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        HorizontalPager(
            count = pages.size,
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            val item = pages[page]
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = item.title,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                        .align(Alignment.Start),
                    lineHeight = 44.sp,
                )
                Image(
                    painter = painterResource(id = item.imageRes),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .padding(bottom = 24.dp)
                )
                Text(
                    text = item.description,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    lineHeight = 20.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        HorizontalPagerIndicator(
            pagerState = pagerState,
            activeColor = PurplePrimary,
            inactiveColor = Color.LightGray,
            modifier = Modifier.padding(16.dp)
        )

        Button(
            onClick = {
                if (pagerState.currentPage == pages.lastIndex) {
                    navController.navigate("login")
                } else {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary),
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            Text(
                text = if (pagerState.currentPage == pages.lastIndex) "Get Started" else "Next",
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}
