package aadhaar.ageproof.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

enum class NavigationItem(
    val route: String,
    val title: String,
    val label: String,
    val icon: ImageVector,
) {
    Home(
        route = "home_route",
        title = "Aadhaar Age Proof",
        label = "Home",
        icon = Icons.Filled.Home,
    ),
    Prove(
        route = "prove_route",
        title = "Generate Proof",
        label = "Prove",
        icon = Icons.Filled.Edit,
    ),
    Verify(
        route = "verify_route",
        title = "Verify Proof",
        label = "Verify",
        icon = Icons.Filled.Check,
    ),
    Scan(
        route = "scan_route",
        title = "Scan QR Code",
        label = "Scan",
        icon = Icons.Filled.Build,
    ),
    ImagePicker(
        route = "image_route",
        title = "Choose QR Code Image",
        label = "Choose Image",
        icon = Icons.Filled.Person,
    )

}