package aadhaar.ageproof.ui

import aadhaar.ageproof.R
import androidx.annotation.DrawableRes

enum class NavigationItem(
    val route: String,
    val label: String,
    @DrawableRes val id: Int,
) {
    Prove(
        route = "prove_route",
        label = "Prove",
        id = R.drawable.ic_update,
    ),
    Verify(
        route = "verify_route",
        label = "Verify",
        id = R.drawable.ic_verified,
    ),
}