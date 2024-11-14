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

enum class AgeProofScreen(
    val route: String,
    val title: String,
    val label: String,
    val icon: ImageVector
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
    bottomNavigationItems: List<AgeProofScreen>,
) {
    NavigationBar {
        bottomNavigationItems.forEach { navigationItem ->
            NavigationBarItem(
                selected = navigationItem.route == currentDestination?.route,
                label = { Text(navigationItem.label) },
                icon = {
                    Icon(navigationItem.icon, contentDescription = navigationItem.label)
                },
                onClick = {
                    navController.navigate(navigationItem.route) {
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

@Composable
fun MainScreen(
    ageProofViewModel: AgeProofViewModel = viewModel(factory = AgeProofViewModel.Factory)
) {
    val uiState by ageProofViewModel.uiState.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination


    val bottomNavigationItems = listOf(
        AgeProofScreen.Home,
        AgeProofScreen.Prove,
        AgeProofScreen.Verify,
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