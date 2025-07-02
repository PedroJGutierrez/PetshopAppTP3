package com.proyecto.petshopapp.ui.login

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import com.proyecto.petshopapp.R

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
    var agreeToTerms by remember { mutableStateOf(false) }
    var showTermsError by remember { mutableStateOf(false) }
    val auth = remember { FirebaseAuth.getInstance() }
    val db = Firebase.firestore

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(60.dp))
                    Text("Create New\n\nAccount", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = Color.Black, textAlign = TextAlign.Start, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Water is life. Water is a basic human need. In various lines of life, humans need water.", fontSize = 14.sp, color = Color.Gray, textAlign = TextAlign.Start, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(40.dp))

                    // Username
                    OutlinedTextField(
                        value = username,
                        onValueChange = {
                            username = it
                            showUsernameError = false
                            errorMessage = null
                        },
                        label = { Text("Username") },
                        placeholder = { Text("JohnDoe") },
                        isError = showUsernameError,
                        enabled = !isLoading,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        textStyle = TextStyle(color = if (username.isNotBlank()) PurplePrimary else Color.Gray, fontSize = 16.sp),
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
                    if (showUsernameError) Text("Username is required", color = Color.Red, fontSize = 12.sp, modifier = Modifier.align(Alignment.Start).padding(top = 4.dp))

                    Spacer(modifier = Modifier.height(16.dp))

                    // Email
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            showEmailError = false
                            errorMessage = null
                        },
                        label = { Text("Email") },
                        placeholder = { Text("example@gmail.com") },
                        isError = showEmailError,
                        enabled = !isLoading,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        textStyle = TextStyle(color = if (email.isNotBlank()) PurplePrimary else Color.Gray, fontSize = 16.sp),
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
                    if (showEmailError) Text("Email is required", color = Color.Red, fontSize = 12.sp, modifier = Modifier.align(Alignment.Start).padding(top = 4.dp))

                    Spacer(modifier = Modifier.height(16.dp))

                    // Password
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            showPasswordError = false
                        },
                        label = { Text("Password") },
                        placeholder = { Text("••••••••") },
                        isError = showPasswordError,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val icon = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(icon, contentDescription = null, tint = Color.Gray)
                            }
                        },
                        enabled = !isLoading,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        textStyle = TextStyle(color = if (password.isNotBlank()) PurplePrimary else Color.Gray, fontSize = 16.sp),
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
                    if (showPasswordError) Text("Password is required", color = Color.Red, fontSize = 12.sp, modifier = Modifier.align(Alignment.Start).padding(top = 4.dp))

                    Spacer(modifier = Modifier.height(16.dp))

                    // Confirm Password
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = {
                            confirmPassword = it
                            showConfirmPasswordError = false
                        },
                        label = { Text("Confirm Password") },
                        placeholder = { Text("••••••••") },
                        isError = showConfirmPasswordError,
                        visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val icon = if (confirmPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                            IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                Icon(icon, contentDescription = null, tint = Color.Gray)
                            }
                        },
                        enabled = !isLoading,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        textStyle = TextStyle(color = if (confirmPassword.isNotBlank()) PurplePrimary else Color.Gray, fontSize = 16.sp),
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
                    if (showConfirmPasswordError) Text("Passwords must match", color = Color.Red, fontSize = 12.sp, modifier = Modifier.align(Alignment.Start).padding(top = 4.dp))

                    Spacer(modifier = Modifier.height(24.dp))

                    // Terms
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
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
                        Text("I Agree to the Terms of Service and Privacy Policy", color = if (showTermsError) Color.Red else Color.Gray, fontSize = 14.sp)
                    }
                    if (showTermsError) Text("You must agree to the Terms of Service and Privacy Policy", color = Color.Red, fontSize = 12.sp, modifier = Modifier.align(Alignment.Start).padding(top = 4.dp))

                    errorMessage?.let {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(it, color = Color.Red, fontSize = 12.sp, modifier = Modifier.fillMaxWidth())
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Have an account? ", color = Color.Gray, fontSize = 14.sp)
                    TextButton(onClick = { navController.navigate("login") }, enabled = !isLoading) {
                        Text("Login", color = PurplePrimary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
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
                            isLoading = true
                            errorMessage = null
                            auth.createUserWithEmailAndPassword(trimmedEmail, trimmedPassword)
                                .addOnSuccessListener { result ->
                                    val user = result.user ?: return@addOnSuccessListener
                                    val profileUpdates = UserProfileChangeRequest.Builder().setDisplayName(trimmedUsername).build()
                                    user.updateProfile(profileUpdates)
                                        .addOnSuccessListener {
                                            val userData = hashMapOf("uid" to user.uid, "username" to trimmedUsername, "email" to trimmedEmail, "userType" to "Reseller")
                                            db.collection("users").document(user.uid).set(userData)
                                                .addOnSuccessListener {
                                                    isLoading = false
                                                    navController.navigate("login") { popUpTo("create_account") { inclusive = true } }
                                                }
                                                .addOnFailureListener { e ->
                                                    isLoading = false
                                                    errorMessage = "Error saving user: ${e.message}"
                                                }
                                        }
                                        .addOnFailureListener { e ->
                                            isLoading = false
                                            errorMessage = "Profile update error: ${e.message}"
                                        }
                                }
                                .addOnFailureListener { e ->
                                    isLoading = false
                                    errorMessage = "Error creating account: ${e.message}"
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
                        Text("Get Started", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}