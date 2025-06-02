@Composable
fun SettingsScreen(navController: NavHostController) {
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        item {
            SettingsOptionItem("Account") { navController.navigate("account") }
            SettingsOptionItem("Security") { navController.navigate("security") }
            SettingsOptionItem("FAQ") { navController.navigate("faq") }
            SettingsOptionItem("Privacy Policy") { navController.navigate("privacy") }
            SettingsOptionItem("Logout") { /* TODO: Handle logout */ }
        }
    }
}