package com.proyecto.petshopapp.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.proyecto.petshopapp.ui.theme.PurplePrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAccountScreen(navController: NavController) {
    val registerViewModel: RegisterViewModel = hiltViewModel()
    val registerState by registerViewModel.uiState.collectAsState()

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    var showUsernameError by remember { mutableStateOf(false) }
    var showEmailError by remember { mutableStateOf(false) }
    var showPasswordError by remember { mutableStateOf(false) }
    var showConfirmPasswordError by remember { mutableStateOf(false) }
    var agreeToTerms by remember { mutableStateOf(false) }
    var showTermsError by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        Text(
            text = "Create New\nAccount",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center,
            lineHeight = 32.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Sign up with a username, email, and password",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Username
        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
                showUsernameError = false
                registerViewModel.clearError()
            },
            label = { Text("Username") },
            isError = showUsernameError,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PurplePrimary,
                unfocusedBorderColor = Color.Gray
            )
        )
        if (showUsernameError) Text("Username is required", color = Color.Red, fontSize = 12.sp)

        Spacer(modifier = Modifier.height(16.dp))

        // Email
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                showEmailError = false
                registerViewModel.clearError()
            },
            label = { Text("Email") },
            isError = showEmailError,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PurplePrimary,
                unfocusedBorderColor = Color.Gray
            )
        )
        if (showEmailError) Text("Email is required", color = Color.Red, fontSize = 12.sp)

        Spacer(modifier = Modifier.height(16.dp))

        // Password
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                showPasswordError = false
                registerViewModel.clearError()
            },
            label = { Text("Password") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility, null)
                }
            },
            isError = showPasswordError,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PurplePrimary,
                unfocusedBorderColor = Color.Gray
            )
        )
        if (showPasswordError) Text("Password is required", color = Color.Red, fontSize = 12.sp)

        Spacer(modifier = Modifier.height(16.dp))

        // Confirm Password
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                showConfirmPasswordError = false
                registerViewModel.clearError()
            },
            label = { Text("Confirm Password") },
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(if (confirmPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility, null)
                }
            },
            isError = showConfirmPasswordError,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PurplePrimary,
                unfocusedBorderColor = Color.Gray
            )
        )
        if (showConfirmPasswordError) Text("Passwords must match", color = Color.Red, fontSize = 12.sp)

        Spacer(modifier = Modifier.height(16.dp))

        // Checkbox
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = agreeToTerms,
                onCheckedChange = {
                    agreeToTerms = it
                    showTermsError = false
                    registerViewModel.clearError()
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = PurplePrimary,
                    uncheckedColor = if (showTermsError) Color.Red else Color.Gray
                )
            )
            Text("I agree to the Terms and Privacy Policy")
        }

        if (showTermsError) Text("You must agree to the terms", color = Color.Red, fontSize = 12.sp)

        // Error
        registerState.errorMessage?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(it, color = Color.Red, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Create Account Button
        Button(
            onClick = {
                val trimmedUsername = username.trim()
                val trimmedEmail = email.trim()
                val trimmedPassword = password.trim()
                val trimmedConfirm = confirmPassword.trim()

                showUsernameError = trimmedUsername.isBlank()
                showEmailError = trimmedEmail.isBlank()
                showPasswordError = trimmedPassword.isBlank()
                showConfirmPasswordError = trimmedPassword != trimmedConfirm
                showTermsError = !agreeToTerms

                if (!showUsernameError && !showEmailError && !showPasswordError && !showConfirmPasswordError && !showTermsError) {
                    registerViewModel.createAccount(
                        trimmedUsername,
                        trimmedEmail,
                        trimmedPassword
                    ) {
                        navController.navigate("login") {
                            popUpTo("create_account") { inclusive = true }
                        }
                    }
                }
            },
            enabled = !registerState.isLoading,
            colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            if (registerState.isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("Get Started", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Login redirect
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            Text("Have an account? ", color = Color.Gray)
            TextButton(onClick = { navController.navigate("login") }) {
                Text("Login", color = PurplePrimary)
            }
        }
    }
}
