package aadhaar.ageproof.ui.scanner.navhost

enum class Screen {
    SCAN,
    IMAGE_PICKER
}

sealed class NavigationItem(val route: String) {
    object Scan : NavigationItem(Screen.SCAN.name)
    object ImagePicker : NavigationItem(Screen.IMAGE_PICKER.name)
}