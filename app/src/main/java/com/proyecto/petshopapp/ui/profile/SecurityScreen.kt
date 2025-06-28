package com.proyecto.petshopapp.ui.profile

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.draw.clip
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.proyecto.petshopapp.R
import com.proyecto.petshopapp.ui.login.LoginViewModel
import kotlinx.coroutines.delay

val poppinsFont = FontFamily(Font(R.font.poppins_regular))

@Composable
fun SecurityScreen(
    navController: NavController,
    loginViewModel: LoginViewModel = viewModel()
) {
    var currentScreen by remember { mutableStateOf("main") }

    when (currentScreen) {
        "main" -> SecurityMainScreen(
            navController = navController,
            onChangePassword = { currentScreen = "changePassword" },
            onChangeEmail = { currentScreen = "changeEmail" }
        )
        "changePassword" -> ChangePasswordScreen(
            onBack = { currentScreen = "main" },
            loginViewModel = loginViewModel
        )
        "changeEmail" -> ChangeEmailScreen(
            onBack = { currentScreen = "main" }
        )
    }
}

@Composable
private fun SecurityMainScreen(
    navController: NavController,
    onChangePassword: () -> Unit,
    onChangeEmail: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Security",
                style = TextStyle(
                    fontFamily = poppinsFont,
                    fontSize = 18.sp,
                    color = Color.Black
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Security",
            style = TextStyle(
                fontFamily = poppinsFont,
                fontSize = 14.sp,
                color = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        SecurityItem(
            icon = Icons.Default.Lock,
            label = "Change Password",
            onClick = onChangePassword
        )

        Spacer(modifier = Modifier.height(12.dp))

        SecurityItem(
            icon = Icons.Default.Email,
            label = "Change Email",
            onClick = onChangeEmail
        )
    }
}

@Composable
private fun ChangePasswordScreen(
    onBack: () -> Unit,
    loginViewModel: LoginViewModel
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var currentPasswordError by remember { mutableStateOf<String?>(null) }
    var newPasswordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }

    var showSuccessIcon by remember { mutableStateOf(false) }

    // Visibilidad de contraseñas
    var showCurrentPassword by remember { mutableStateOf(false) }
    var showNewPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    val passwordChangeState by loginViewModel.passwordChangeState.collectAsState()

    LaunchedEffect(Unit) {
        loginViewModel.clearPasswordChangeState()
        showSuccessIcon = false
    }

    LaunchedEffect(passwordChangeState.isSuccess) {
        if (passwordChangeState.isSuccess) {
            showSuccessIcon = true
            delay(2000)
            onBack()
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 24.dp, vertical = 16.dp)) {

        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("Change Password", style = TextStyle(fontFamily = poppinsFont, fontSize = 18.sp, color = Color.Black))

            if (showSuccessIcon) {
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF4CAF50)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Check, contentDescription = "Success", tint = Color.White, modifier = Modifier.size(20.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Campo actual
        Text("Current Password", style = TextStyle(fontFamily = poppinsFont, fontSize = 14.sp))
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = currentPassword,
            onValueChange = {
                currentPassword = it
                currentPasswordError = if (it.isBlank()) "Required" else null
            },
            visualTransformation = if (showCurrentPassword) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            isError = currentPasswordError != null,
            placeholder = { Text("••••••", color = Color.Gray) },
            trailingIcon = {
                IconButton(onClick = { showCurrentPassword = !showCurrentPassword }) {
                    Icon(
                        imageVector = if (showCurrentPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = "Toggle Password"
                    )
                }
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = if (currentPasswordError != null) Color.Red else Color.Gray,
                unfocusedBorderColor = if (currentPasswordError != null) Color.Red else Color.LightGray
            )
        )
        currentPasswordError?.let {
            Text(it, color = Color.Red, fontSize = 12.sp, fontFamily = poppinsFont)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Campo nueva
        Text("New Password", style = TextStyle(fontFamily = poppinsFont, fontSize = 14.sp))
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = newPassword,
            onValueChange = {
                newPassword = it
                newPasswordError = when {
                    it.isBlank() -> "Required"
                    it.length < 6 -> "Minimum 6 characters"
                    else -> null
                }
                confirmPasswordError = if (confirmPassword != it) "Passwords do not match" else null
            },
            visualTransformation = if (showNewPassword) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            isError = newPasswordError != null,
            placeholder = { Text("••••••", color = Color.Gray) },
            trailingIcon = {
                IconButton(onClick = { showNewPassword = !showNewPassword }) {
                    Icon(
                        imageVector = if (showNewPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = "Toggle Password"
                    )
                }
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = if (newPasswordError != null) Color.Red else Color.Gray,
                unfocusedBorderColor = if (newPasswordError != null) Color.Red else Color.LightGray
            )
        )
        newPasswordError?.let {
            Text(it, color = Color.Red, fontSize = 12.sp, fontFamily = poppinsFont)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Campo confirmar
        Text("Confirm Password", style = TextStyle(fontFamily = poppinsFont, fontSize = 14.sp))
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                confirmPasswordError = if (it != newPassword) "Passwords do not match" else null
            },
            visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            isError = confirmPasswordError != null,
            placeholder = { Text("••••••", color = Color.Gray) },
            trailingIcon = {
                IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                    Icon(
                        imageVector = if (showConfirmPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = "Toggle Password"
                    )
                }
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = if (confirmPasswordError != null) Color.Red else Color.Gray,
                unfocusedBorderColor = if (confirmPasswordError != null) Color.Red else Color.LightGray
            )
        )
        confirmPasswordError?.let {
            Text(it, color = Color.Red, fontSize = 12.sp, fontFamily = poppinsFont)
        }

        Spacer(modifier = Modifier.height(24.dp))

        passwordChangeState.errorMessage?.let {
            Text(it, color = Color.Red, fontSize = 12.sp, fontFamily = poppinsFont)
            Spacer(modifier = Modifier.height(16.dp))
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                currentPasswordError = if (currentPassword.isBlank()) "Required" else null
                newPasswordError = when {
                    newPassword.isBlank() -> "Required"
                    newPassword.length < 6 -> "Minimum 6 characters"
                    else -> null
                }
                confirmPasswordError = if (confirmPassword != newPassword) "Passwords do not match" else null

                if (currentPasswordError == null && newPasswordError == null && confirmPasswordError == null) {
                    loginViewModel.changePassword(currentPassword, newPassword)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF6B46C1)),
            shape = RoundedCornerShape(24.dp),
            enabled = !passwordChangeState.isLoading
        ) {
            if (passwordChangeState.isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
            } else {
                Text("Change Password", color = Color.White, fontSize = 16.sp, fontFamily = poppinsFont)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}


@Composable
fun ChangeEmailScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var newEmail by remember { mutableStateOf("") }
    var currentPassword by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    var isLoading by remember { mutableStateOf(false) }
    var showSuccessIcon by remember { mutableStateOf(false) }
    var showPassword by remember { mutableStateOf(false) }

    LaunchedEffect(showSuccessIcon) {
        if (showSuccessIcon) {
            delay(2000)
            onBack()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Change Email",
                style = TextStyle(fontFamily = poppinsFont, fontSize = 18.sp, color = Color.Black)
            )
            if (showSuccessIcon) {
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF4CAF50)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Check, contentDescription = "Success", tint = Color.White, modifier = Modifier.size(20.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Email Input
        Text("New Email", style = TextStyle(fontFamily = poppinsFont, fontSize = 14.sp, color = Color.Black))
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = newEmail,
            onValueChange = {
                newEmail = it
                emailError = null
            },
            isError = emailError != null,
            placeholder = { Text("example@email.com", color = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = if (emailError != null) Color.Red else Color.Gray,
                unfocusedBorderColor = if (emailError != null) Color.Red else Color.LightGray
            )
        )
        emailError?.let {
            Text(it, color = Color.Red, fontSize = 12.sp, fontFamily = poppinsFont)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Current Password Input
        Text("Current Password", style = TextStyle(fontFamily = poppinsFont, fontSize = 14.sp, color = Color.Black))
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = currentPassword,
            onValueChange = {
                currentPassword = it
                passwordError = null
            },
            isError = passwordError != null,
            placeholder = { Text("••••••", color = Color.Gray) },
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        imageVector = if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = "Toggle password"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = if (passwordError != null) Color.Red else Color.Gray,
                unfocusedBorderColor = if (passwordError != null) Color.Red else Color.LightGray
            )
        )
        passwordError?.let {
            Text(it, color = Color.Red, fontSize = 12.sp, fontFamily = poppinsFont)
        }

        Spacer(modifier = Modifier.weight(1f))

        // Save Button
        Button(
            onClick = {
                if (!Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
                    emailError = "Invalid email format"
                    return@Button
                }
                if (currentPassword.isBlank()) {
                    passwordError = "Enter your current password"
                    return@Button
                }

                val user = FirebaseAuth.getInstance().currentUser
                if (user?.email != null) {
                    isLoading = true

                    val credential = EmailAuthProvider.getCredential(user.email!!, currentPassword)
                    user.reauthenticate(credential)
                        .addOnSuccessListener {
                            user.updateEmail(newEmail)
                                .addOnSuccessListener {
                                    Firebase.firestore.collection("users").document(user.uid)
                                        .update("email", newEmail)
                                        .addOnSuccessListener {
                                            Toast.makeText(context, "Email updated", Toast.LENGTH_SHORT).show()
                                            showSuccessIcon = true
                                        }
                                        .addOnFailureListener {
                                            Toast.makeText(context, "Email updated in Auth, but not in Firestore", Toast.LENGTH_SHORT).show()
                                        }
                                }
                                .addOnFailureListener { ex ->
                                    val message = ex.localizedMessage ?: "Error updating email"
                                    if (message.contains("email address is already in use", ignoreCase = true)) {
                                        emailError = "Email is already in use"
                                    } else {
                                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                                    }
                                }
                        }
                        .addOnFailureListener {
                            passwordError = "Incorrect password"
                        }
                        .addOnCompleteListener {
                            isLoading = false
                        }
                } else {
                    Toast.makeText(context, "User not authenticated", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF6B46C1)),
            shape = RoundedCornerShape(24.dp),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
            } else {
                Text("Save", color = Color.White, style = TextStyle(fontFamily = poppinsFont, fontSize = 16.sp))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}



@Composable
private fun SecurityItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(40.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color.Black
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = label,
            style = TextStyle(
                fontFamily = poppinsFont,
                fontSize = 14.sp,
                color = Color.Black
            ),
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = "Arrow",
            tint = Color.Black
        )
    }
}