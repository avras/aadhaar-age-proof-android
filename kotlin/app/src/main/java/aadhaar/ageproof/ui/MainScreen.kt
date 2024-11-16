package aadhaar.ageproof.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
    bottomNavigationItems: List<NavigationItem>,
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
        NavigationItem.Prove,
        NavigationItem.Verify,
    )

    val currentTitle = when (currentDestination?.route) {
        NavigationItem.Verify.route -> NavigationItem.Verify.title
        else -> NavigationItem.Prove.title
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
            startDestination = NavigationItem.Prove.route,
            modifier = Modifier.padding(paddingValues = paddingValues)
        ) {
            composable(NavigationItem.Prove.route) {
                ProveScreen(
                    ageProofUiState = uiState,
                    generatePublicParameters = ageProofViewModel::generatePublicParameters,
                    setQrCodeBytes = ageProofViewModel::setQrCodeBytes,
                    resetQrCodeBytes = ageProofViewModel::resetQrCodeBytes,
                    generateProof = ageProofViewModel::generateProof,
                    resetProof = ageProofViewModel::resetProof,
                    modifier = Modifier.padding(16.dp),
                )
            }
            composable(NavigationItem.Verify.route) {
                ChildScreen("Verify")
            }
        }

    }
}