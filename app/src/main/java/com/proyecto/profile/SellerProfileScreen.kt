@Composable
fun SellerProfileScreen(viewModel: SellerProfileViewModel = hiltViewModel()) {
    val user by viewModel.user.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // TODO: Replace with actual Figma profile image
        Image(painter = painterResource(id = R.drawable.placeholder_profile), contentDescription = "Profile Picture")
        Text(text = user.name, style = MaterialTheme.typography.titleLarge)
        SellerStatsCard(user)
    }
}