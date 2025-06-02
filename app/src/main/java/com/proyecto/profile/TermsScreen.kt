@Composable
fun TermsScreen(viewModel: PolicyViewModel = hiltViewModel()) {
    val terms by viewModel.terms.collectAsState()

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        item {
            Text(text = terms, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

