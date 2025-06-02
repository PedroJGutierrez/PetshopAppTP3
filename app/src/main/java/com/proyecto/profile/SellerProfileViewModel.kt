@HiltViewModel
class SellerProfileViewModel @Inject constructor() : ViewModel() {
    private val _user = MutableStateFlow(User("Abduldul", 80, 109, 992))
    val user: StateFlow<User> = _user
}