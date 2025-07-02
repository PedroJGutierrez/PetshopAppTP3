package com.proyecto.petshopapp.ui.login

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.proyecto.petshopapp.ui.theme.PurplePrimary
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var showEmailError by remember { mutableStateOf(false) }
    var isEmailValid by remember { mutableStateOf(true) }
    var firebaseError by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var showSnackbar by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val auth = remember { FirebaseAuth.getInstance() }

    fun validateEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    LaunchedEffect(showSnackbar) {
        if (showSnackbar) {
            scope.launch {
                snackbarHostState.showSnackbar("Password reset email sent. Check your inbox.")
                navController.navigate("forgot_password_confirm")
            }
            showSnackbar = false
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.White
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(120.dp))

                    Text(
                        text = "Forgot\nPassword",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        textAlign = TextAlign.Start,
                        lineHeight = 38.sp,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Water is life. Water is a basic human need in various lines of life humans need water...",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Start,
                        lineHeight = 20.sp,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(80.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it.trim()
                            showEmailError = false
                            isEmailValid = true
                            firebaseError = null
                        },
                        label = { Text("Email") },
                        placeholder = { Text("example@gmail.com") },
                        textStyle = TextStyle(color = if (email.isNotBlank()) PurplePrimary else Color.Gray, fontSize = 16.sp),
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth(),
                        isError = showEmailError || !isEmailValid,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PurplePrimary,
                            unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f),
                            focusedTextColor = PurplePrimary,
                            unfocusedTextColor = Color.Gray,
                            unfocusedLabelColor = Color.Gray,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    when {
                        showEmailError -> Text("Email is required", color = Color.Red, fontSize = 12.sp, modifier = Modifier.align(Alignment.Start).padding(top = 4.dp))
                        !isEmailValid -> Text("Invalid email format", color = Color.Red, fontSize = 12.sp, modifier = Modifier.align(Alignment.Start).padding(top = 4.dp))
                        firebaseError != null -> Text(firebaseError ?: "", color = Color.Red, fontSize = 12.sp, modifier = Modifier.align(Alignment.Start).padding(top = 4.dp))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                }

                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = { navController.navigate("login") },
                        enabled = !isLoading,
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("Have an account? ", color = Color.Gray, fontSize = 14.sp)
                        Text("Login", color = PurplePrimary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    }
                }
                    Button(
                        onClick = {
                            when {
                                email.isBlank() -> showEmailError = true
                                !validateEmail(email) -> isEmailValid = false
                                else -> {
                                    isLoading = true
                                    auth.sendPasswordResetEmail(email)
                                        .addOnCompleteListener { task ->
                                            isLoading = false
                                            if (task.isSuccessful) {
                                                showSnackbar = true
                                            } else {
                                                firebaseError = task.exception?.message ?: "Error sending email"
                                            }
                                        }
                                }
                            }
                        },
                        enabled = !isLoading,
                        colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                        } else {
                            Text("Next", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }

            }
        }
    }
}
