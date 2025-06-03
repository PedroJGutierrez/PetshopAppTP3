import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.proyecto.petshopapp.home.CartViewModel


@Composable
fun PaymentSuccessScreen(
    navController: NavHostController,
    cartViewModel: CartViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 90.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
    ) {
        Column {
            Text(
                "Payment\nSuccess!",
                style = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 40.sp,
                    lineHeight = (40.sp.value * 1.4).sp,
                    letterSpacing = 0.sp,
                ),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
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
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Justify
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        androidx.compose.material3.Button(
            onClick = { cartViewModel.clearCart()
                navController.navigate("home") },
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5CF6))
        ) {
            Text("Go Home", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}



