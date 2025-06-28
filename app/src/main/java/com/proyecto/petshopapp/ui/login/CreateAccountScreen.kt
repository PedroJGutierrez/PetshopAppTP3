package com.proyecto.petshopapp.ui.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.proyecto.petshopapp.ui.theme.PurplePrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAccountScreen(navController: NavController) {
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
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    // Terms and Privacy Policy checkbox
    var agreeToTerms by remember { mutableStateOf(false) }
    var showTermsError by remember { mutableStateOf(false) }

    val auth = remember { FirebaseAuth.getInstance() }
    val db = Firebase.firestore

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

        // Title
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

        // Username Field
        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
                showUsernameError = false
                errorMessage = null
            },
            label = { Text("Username") },
            placeholder = { Text("JohnDoe") },
            textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
            isError = showUsernameError,
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PurplePrimary,
                focusedLabelColor = PurplePrimary,
                unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f),
                errorBorderColor = Color.Red,
                errorLabelColor = Color.Red,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )
        if (showUsernameError) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Text("Username is required", color = Color.Red, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Email Field
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                showEmailError = false
                errorMessage = null
            },
            label = { Text("Email") },
            placeholder = { Text("example@gmail.com") },
            textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
            isError = showEmailError,
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PurplePrimary,
                focusedLabelColor = PurplePrimary,
                unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f),
                errorBorderColor = Color.Red,
                errorLabelColor = Color.Red,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )
        if (showEmailError) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Text("Email is required", color = Color.Red, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Password Field
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                showPasswordError = false
            },
            label = { Text("Password") },
            placeholder = { Text("••••••••") },
            textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val icon = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = icon,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password",
                        tint = Color.Gray
                    )
                }
            },
            isError = showPasswordError,
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PurplePrimary,
                focusedLabelColor = PurplePrimary,
                unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f),
                errorBorderColor = Color.Red,
                errorLabelColor = Color.Red,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )
        if (showPasswordError) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Text("Password is required", color = Color.Red, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Confirm Password Field
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                showConfirmPasswordError = false
            },
            label = { Text("Confirm Password") },
            placeholder = { Text("••••••••") },
            textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val icon = if (confirmPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(
                        imageVector = icon,
                        contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password",
                        tint = Color.Gray
                    )
                }
            },
            isError = showConfirmPasswordError,
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PurplePrimary,
                focusedLabelColor = PurplePrimary,
                unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f),
                errorBorderColor = Color.Red,
                errorLabelColor = Color.Red,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )
        if (showConfirmPasswordError) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Text("Passwords must match", color = Color.Red, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Terms and Privacy Policy Checkbox
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = agreeToTerms,
                onCheckedChange = {
                    agreeToTerms = it
                    showTermsError = false
                },
                enabled = !isLoading,
                colors = CheckboxDefaults.colors(
                    checkedColor = PurplePrimary,
                    uncheckedColor = if (showTermsError) Color.Red else Color.Gray
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = buildString {
                        append("I Agree to the ")
                    },
                    fontSize = 14.sp,
                    color = if (showTermsError) Color.Red else Color.Gray,
                    lineHeight = 20.sp
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = { /* Handle Terms of Service click */ },
                        enabled = !isLoading,
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "Terms of Service",
                            fontSize = 14.sp,
                            color = PurplePrimary,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Text(
                        text = " and ",
                        fontSize = 14.sp,
                        color = if (showTermsError) Color.Red else Color.Gray
                    )

                    TextButton(
                        onClick = { /* Handle Privacy Policy click */ },
                        enabled = !isLoading,
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "Privacy Policy",
                            fontSize = 14.sp,
                            color = PurplePrimary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        if (showTermsError) {
            Text(
                text = "You must agree to the Terms of Service and Privacy Policy",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }

        // Error Message
        errorMessage?.let { message ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.fillMaxWidth()
            )
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
                    isLoading = true
                    errorMessage = null

                    auth.createUserWithEmailAndPassword(trimmedEmail, trimmedPassword)
                        .addOnSuccessListener { result ->
                            val user = result.user
                            if (user == null) {
                                isLoading = false
                                errorMessage = "Error creando usuario"
                                return@addOnSuccessListener
                            }

                            val profileUpdates = UserProfileChangeRequest.Builder()
                                .setDisplayName(trimmedUsername)
                                .build()

                            user.updateProfile(profileUpdates)
                                .addOnSuccessListener {
                                    val userData = hashMapOf(
                                        "uid" to user.uid,
                                        "username" to trimmedUsername,
                                        "email" to trimmedEmail,
                                        "userType" to "Reseller" // Always create as Reseller
                                    )

                                    db.collection("users").document(user.uid)
                                        .set(userData)
                                        .addOnSuccessListener {
                                            isLoading = false
                                            navController.navigate("login") {
                                                popUpTo("create_account") { inclusive = true }
                                            }
                                        }
                                        .addOnFailureListener { e ->
                                            isLoading = false
                                            errorMessage = "Error al guardar en Firestore: ${e.message}"
                                        }
                                }
                                .addOnFailureListener { e ->
                                    isLoading = false
                                    errorMessage = "Error actualizando perfil: ${e.message}"
                                }
                        }
                        .addOnFailureListener { e ->
                            isLoading = false
                            errorMessage = "Error creando cuenta: ${e.message}"
                        }
                }
            },
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
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

        Spacer(modifier = Modifier.height(32.dp))

        // Login Link
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 40.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                "Have an account? ",
                color = Color.Gray,
                fontSize = 14.sp
            )
            TextButton(
                onClick = { navController.navigate("login") },
                enabled = !isLoading,
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    "Login",
                    color = PurplePrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}