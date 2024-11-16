package aadhaar.ageproof.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.ui.graphics.vector.ImageVector

enum class NavigationItem(
    val route: String,
    val title: String,
    val label: String,
    val icon: ImageVector,
) {
    Prove(
        route = "prove_route",
        title = "Generate Age Proof",
        label = "Prove",
        icon = Icons.Filled.Edit,
    ),
    Verify(
        route = "verify_route",
        title = "Verify Age Proof",
        label = "Verify",
        icon = Icons.Filled.Check,
    ),
}