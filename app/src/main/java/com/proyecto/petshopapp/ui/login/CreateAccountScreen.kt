package com.proyecto.petshopapp.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.proyecto.petshopapp.ui.theme.PurplePrimary
import com.proyecto.petshopapp.ui.utils.AsteriskPasswordVisualTransformation

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
    val isFormValid =
        username.trim().isNotBlank() &&
                email.trim().isNotBlank() &&
                password.trim().isNotBlank() &&
                (password.trim() == confirmPassword.trim()) &&
                agreeToTerms

    Scaffold(containerColor = Color.White) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(scrollState),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(60.dp))

                    Text(
                        text = "Create New\n\nAccount",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        textAlign = TextAlign.Start,
                        lineHeight = 32.sp,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Water is life. Water is a basic human need. In various lines of life, humans need water.",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Username
                    OutlinedTextField(
                        value = username,
                        onValueChange = {
                            username = it
                            showUsernameError = false
                            registerViewModel.clearError()
                        },
                        label = { Text("Full name", color = Color.Gray) },
                        isError = showUsernameError,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        textStyle = TextStyle(
                            fontSize = 14.sp,
                            color = if (username.isNotBlank()) PurplePrimary else Color.Gray
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PurplePrimary,
                            unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f),
                            focusedTextColor = PurplePrimary,
                            unfocusedTextColor = Color.Gray,
                            unfocusedLabelColor = Color.Gray,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        )
                    )
                    if (showUsernameError) {
                        Text("Username is required", color = Color.Red, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Email
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            showEmailError = false
                            registerViewModel.clearError()
                        },
                        label = { Text("Email", color = Color.Gray) },
                        isError = showEmailError,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        textStyle = TextStyle(
                            fontSize = 14.sp,
                            color = if (email.isNotBlank()) PurplePrimary else Color.Gray
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PurplePrimary,
                            unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f),
                            focusedTextColor = PurplePrimary,
                            unfocusedTextColor = Color.Gray,
                            unfocusedLabelColor = Color.Gray,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        )
                    )
                    if (showEmailError) {
                        Text("Email is required", color = Color.Red, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Password
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            showPasswordError = false
                            registerViewModel.clearError()
                        },
                        label = { Text("Password", color = Color.Gray) },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else AsteriskPasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = null,
                                    tint = Color.Gray
                                )
                            }
                        },
                        isError = showPasswordError,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        textStyle = TextStyle(
                            fontSize = 14.sp,
                            color = if (password.isNotBlank()) PurplePrimary else Color.Gray
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PurplePrimary,
                            unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f),
                            focusedTextColor = PurplePrimary,
                            unfocusedTextColor = Color.Gray,
                            unfocusedLabelColor = Color.Gray,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        )
                    )
                    if (showPasswordError) {
                        Text("Password is required", color = Color.Red, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Confirm Password
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = {
                            confirmPassword = it
                            showConfirmPasswordError = false
                            registerViewModel.clearError()
                        },
                        label = { Text("Confirm Password", color = Color.Gray) },
                        visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else AsteriskPasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = {
                                confirmPasswordVisible = !confirmPasswordVisible
                            }) {
                                Icon(
                                    if (confirmPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = null,
                                    tint = Color.Gray
                                )
                            }
                        },
                        isError = showConfirmPasswordError,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        textStyle = TextStyle(
                            fontSize = 14.sp,
                            color = if (confirmPassword.isNotBlank()) PurplePrimary else Color.Gray
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PurplePrimary,
                            unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f),
                            focusedTextColor = PurplePrimary,
                            unfocusedTextColor = Color.Gray,
                            unfocusedLabelColor = Color.Gray,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        )
                    )
                    if (showConfirmPasswordError) {
                        Text("Passwords must match", color = Color.Red, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .border(
                                    width = 1.5.dp,
                                    color = if (agreeToTerms) PurplePrimary else Color.LightGray,
                                    shape = RoundedCornerShape(3.dp)
                                )
                                .clickable {
                                    agreeToTerms = !agreeToTerms
                                    showTermsError = false
                                    registerViewModel.clearError()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            if (agreeToTerms) {
                                Box(
                                    modifier = Modifier
                                        .size(9.dp)
                                        .background(PurplePrimary, shape = RoundedCornerShape(1.5.dp))
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = buildAnnotatedString {
                                append("I Agree to the ")
                                withStyle(style = SpanStyle(color = PurplePrimary, fontWeight = FontWeight.Medium)) {
                                    append("Terms of Service")
                                }
                                append(" and \n")
                                withStyle(style = SpanStyle(color = PurplePrimary, fontWeight = FontWeight.Medium)) {
                                    append("Privacy Policy")
                                }
                            },
                            fontSize = 12.sp,
                            color = Color.Gray,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    if (showTermsError) {
                        Text("You must agree to the terms", color = Color.Red, fontSize = 12.sp)
                    }

                    registerState.errorMessage?.let {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(it, color = Color.Red, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                }
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = { navController.navigate("login") },
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text("Have an account? ", color = Color.Black, fontSize = 14.sp)
                            Text("Login", color = PurplePrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }

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
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isFormValid) PurplePrimary else Color.LightGray
                        ),
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                    ) {
                        if (registerState.isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text(
                                "Get Started",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

            }
        }
    }
}
