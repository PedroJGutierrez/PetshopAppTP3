@Composable
fun ChangePasswordScreen(viewModel: SecurityViewModel = hiltViewModel()) {
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("New Password") })
        OutlinedTextField(value = confirm, onValueChange = { confirm = it }, label = { Text("Confirm Password") })
        Button(onClick = {
            if (password == confirm) viewModel.updatePassword(password)
            else {/* TODO: Show error */}
        }) {
            Text("Change Password")
        }
    }
}