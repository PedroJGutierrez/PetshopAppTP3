package com.proyecto.petshopapp.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
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
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Forgot", fontSize = 32.sp, fontWeight = FontWeight.Bold)
            Text("Password", fontSize = 32.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Enter your email to receive a reset link",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(40.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    showEmailError = false
                    isEmailValid = true
                    firebaseError = null
                },
                label = { Text("Your Email") },
                modifier = Modifier.fillMaxWidth(),
                isError = showEmailError || !isEmailValid,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PurplePrimary,
                    focusedLabelColor = PurplePrimary,
                    errorBorderColor = Color.Red,
                    errorLabelColor = Color.Red
                )
            )

            when {
                showEmailError -> {
                    Text("Email is required", color = Color.Red, fontSize = 12.sp)
                }
                !isEmailValid -> {
                    Text("Invalid email format", color = Color.Red, fontSize = 12.sp)
                }
                firebaseError != null -> {
                    Text(firebaseError ?: "", color = Color.Red, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (email.isEmpty()) {
                        showEmailError = true
                    } else if (!validateEmail(email)) {
                        isEmailValid = false
                    } else {
                        isLoading = true
                        auth.sendPasswordResetEmail(email)
                            .addOnCompleteListener { task ->
                                isLoading = false
                                if (task.isSuccessful) {
                                    showSnackbar = true
                                } else {
                                    firebaseError = task.exception?.message
                                }
                            }
                    }
                },
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                } else {
                    Text("Reset Password", color = Color.White, fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(onClick = { navController.navigate("login") }) {
                Text("Back to Login", color = PurplePrimary, fontSize = 14.sp)
            }
        }
    }
}