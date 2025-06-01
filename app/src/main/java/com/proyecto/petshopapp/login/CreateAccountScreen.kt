package com.proyecto.petshopapp.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.proyecto.petshopapp.ui.theme.PurplePrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAccountScreen(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var showUsernameError by remember { mutableStateOf(false) }
    var showEmailError by remember { mutableStateOf(false) }
    var showPasswordError by remember { mutableStateOf(false) }
    var showConfirmPasswordError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val auth = remember { FirebaseAuth.getInstance() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Create New", fontSize = 32.sp, fontWeight = FontWeight.Bold)
        Text("Account", fontSize = 32.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(8.dp))
        Text("Sign up with a username, email, and password", fontSize = 14.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(40.dp))

        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
                showUsernameError = false
                errorMessage = null
            },
            label = { Text("Username") },
            isError = showUsernameError,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PurplePrimary,
                errorBorderColor = Color.Red
            )
        )

        if (showUsernameError) Text("Username is required", color = Color.Red, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                showEmailError = false
                errorMessage = null
            },
            label = { Text("Email") },
            isError = showEmailError,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PurplePrimary,
                errorBorderColor = Color.Red
            )
        )

        if (showEmailError) Text("Email is required", color = Color.Red, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                showPasswordError = false
            },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            isError = showPasswordError,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PurplePrimary,
                errorBorderColor = Color.Red
            )
        )

        if (showPasswordError) Text("Password is required", color = Color.Red, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                showConfirmPasswordError = false
            },
            label = { Text("Confirm Password") },
            visualTransformation = PasswordVisualTransformation(),
            isError = showConfirmPasswordError,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PurplePrimary,
                errorBorderColor = Color.Red
            )
        )

        if (showConfirmPasswordError) Text("Passwords must match", color = Color.Red, fontSize = 12.sp)

        errorMessage?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(it, color = Color.Red, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                showUsernameError = username.isEmpty()
                showEmailError = email.isEmpty()
                showPasswordError = password.isEmpty()
                showConfirmPasswordError = password != confirmPassword

                if (!showUsernameError && !showEmailError && !showPasswordError && !showConfirmPasswordError) {
                    isLoading = true
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            isLoading = false
                            if (task.isSuccessful) {
                                val user = auth.currentUser
                                val profileUpdates = UserProfileChangeRequest.Builder()
                                    .setDisplayName(username)
                                    .build()

                                user?.updateProfile(profileUpdates)?.addOnCompleteListener {
                                    user.sendEmailVerification()
                                    navController.navigate("login") {
                                        popUpTo("create_account") { inclusive = true }
                                    }
                                }
                            } else {
                                errorMessage = task.exception?.message ?: "Error creating account"
                            }
                        }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
            } else {
                Text("SIGN UP", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        TextButton(onClick = { navController.navigate("login") }) {
            Text("Have an account? Login", color = Color.Gray, fontSize = 14.sp)
        }
    }
}