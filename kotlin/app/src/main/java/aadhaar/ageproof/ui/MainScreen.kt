package aadhaar.ageproof.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

sealed class AgeProofScreen(
    val route: String,
    val title: String,
    val label: String,
) {
    object Home : AgeProofScreen(
        route = "home_route",
        title = "Aadhaar Age Proof",
        label = "Home"
    )

    object Prove : AgeProofScreen(
        route = "prove_route",
        title = "Generate Proof",
        label = "Prove"
    )

    object Verify : AgeProofScreen(
        route = "verify_route",
        title = "Verify Proof",
        label = "Verify"
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgeProofTopBar(
    title: String,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = { Text(title) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        modifier = modifier,
    )
}

@Composable
fun AgeProofBottomNavigation(
    navController: NavController,
    currentDestination: NavDestination?,
    bottomNavigationItems: List<AgeProofNavigationItem>,
) {
    NavigationBar {
        bottomNavigationItems.forEach { navigationItem ->
            NavigationBarItem(
                selected = navigationItem.screen.route == currentDestination?.route,
                label = { Text(navigationItem.screen.label) },
                icon = {
                    Icon(navigationItem.icon, contentDescription = navigationItem.screen.label)
                },
                onClick = {
                    navController.navigate(navigationItem.screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

sealed class AgeProofNavigationItem(val screen: AgeProofScreen, val icon: ImageVector) {
    object Home : AgeProofNavigationItem(AgeProofScreen.Home, Icons.Filled.Home)
    object Prove : AgeProofNavigationItem(AgeProofScreen.Prove, Icons.Filled.Edit)
    object Verify : AgeProofNavigationItem(AgeProofScreen.Verify, Icons.Filled.Check)
}


@Composable
fun MainScreen(
    ageProofViewModel: AgeProofViewModel = viewModel(factory = AgeProofViewModel.Factory)
) {
    val uiState by ageProofViewModel.uiState.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination


    val bottomNavigationItems = listOf(
        AgeProofNavigationItem.Home,
        AgeProofNavigationItem.Prove,
        AgeProofNavigationItem.Verify,
    )

    val currentTitle = when (currentDestination?.route) {
        AgeProofScreen.Home.route -> AgeProofScreen.Home.title
        AgeProofScreen.Prove.route -> AgeProofScreen.Prove.title
        AgeProofScreen.Verify.route -> AgeProofScreen.Verify.title
        else -> {
            AgeProofScreen.Home.title
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AgeProofTopBar(
                title = currentTitle,
            )
        },
        bottomBar = {
            AgeProofBottomNavigation(
                navController,
                currentDestination,
                bottomNavigationItems
            )
        },
    )
    { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = AgeProofScreen.Home.route,
            modifier = Modifier.padding(paddingValues = paddingValues)
        ) {
            composable(AgeProofScreen.Home.route) {
                HomeScreen(
                    uiState,
                    ageProofViewModel::generatePublicParameters,
                    ageProofViewModel::resetPublicParameters,
                    modifier = Modifier.padding(16.dp)
                )
            }
            composable(AgeProofScreen.Prove.route) {
                ChildScreen("Prove")
            }
            composable(AgeProofScreen.Verify.route) {
                ChildScreen("Verify")
            }
        }

    }
}