package aadhaar.ageproof.ui.scanner.navhost

import aadhaar.ageproof.ui.scanner.screen.ImagePicker
import aadhaar.ageproof.ui.scanner.screen.ScanScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun ScannerNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = NavigationItem.Scan.route,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(NavigationItem.ImagePicker.route) {
            ImagePicker(navController)
        }
        composable(NavigationItem.Scan.route) {
            ScanScreen(
                navController,
                modifier = modifier,
            )
        }
    }
}