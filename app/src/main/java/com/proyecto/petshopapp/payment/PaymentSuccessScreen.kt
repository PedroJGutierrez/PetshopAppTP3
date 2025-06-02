import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.proyecto.petshopapp.R  // Ajusta según donde esté tu font


@Composable
fun PaymentSuccessScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 24.dp, top = 90.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            "Payment\nSuccess!",
            style = TextStyle(
                fontWeight = FontWeight.SemiBold,
                fontSize = 40.sp,
                lineHeight = (40.sp.value * 1.4).sp,
                letterSpacing = 0.sp,
            ),
            modifier = Modifier
                .width(331.dp)
                .height(112.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            "Your order is being prepared by the shop,\nthe courier will send it to your address",
            style = TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = (14.sp.value * 1.5).sp,
                letterSpacing = 0.sp,
            ),
            modifier = Modifier
                .width(327.dp)
                .height(42.dp),
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(420.dp))

        Button(
            onClick = { navController.navigate("home") },
            modifier = Modifier
                .width(327.dp)
                .height(60.dp),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text("Go Home")
        }
    }
}

