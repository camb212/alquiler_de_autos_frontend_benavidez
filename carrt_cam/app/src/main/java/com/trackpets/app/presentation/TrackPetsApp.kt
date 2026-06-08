package com.trackpets.app.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.trackpets.app.TrackPetsApplication
import com.trackpets.app.presentation.navigation.TrackPetsRoute
import com.trackpets.app.presentation.screens.*
import com.trackpets.app.presentation.viewmodel.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackPetsApp() {
    val context = LocalContext.current
    val appContainer = (context.applicationContext as TrackPetsApplication).container
    val repository = appContainer.rentalRepository
    val navController = rememberNavController()
    
    var isLoggedIn by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        isLoggedIn = repository.isUserLoggedIn()
    }

    if (!isLoggedIn) {
        NavHost(navController, startDestination = TrackPetsRoute.Login.route) {
            composable(TrackPetsRoute.Login.route) {
                LoginScreen(
                    repository = repository,
                    onLoginSuccess = { isLoggedIn = true },
                    onGoRegister = { navController.navigate(TrackPetsRoute.Register.route) }
                )
            }
            composable(TrackPetsRoute.Register.route) {
                RegisterScreen(
                    repository = repository,
                    onRegisterSuccess = { 
                        navController.navigate(TrackPetsRoute.Login.route) {
                            popUpTo(TrackPetsRoute.Register.route) { inclusive = true }
                        }
                    },
                    onBack = { navController.popBackStack() }
                )
            }
        }
    } else {
        val items = listOf(
            Triple(TrackPetsRoute.Dashboard.route, "Inicio", Icons.Default.Home),
            Triple(TrackPetsRoute.Vehicles.route, "Vehículos", Icons.Default.TimeToLeave),
            Triple(TrackPetsRoute.Categories.route, "Categorías", Icons.Default.Category),
            Triple(TrackPetsRoute.Reservations.route, "Reservas", Icons.Default.ReceiptLong),
            Triple(TrackPetsRoute.Users.route, "Usuario", Icons.Default.Person)
        )

        Scaffold(
            bottomBar = {
                NavigationBar {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    items.forEach { (route, label, icon) ->
                        NavigationBarItem(
                            icon = { Icon(icon, contentDescription = label) },
                            label = { Text(label) },
                            selected = currentDestination?.hierarchy?.any { it.route == route } == true,
                            onClick = {
                                navController.navigate(route) {
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
        ) { innerPadding ->
            NavHost(
                navController, 
                startDestination = TrackPetsRoute.Dashboard.route, 
                Modifier.padding(innerPadding)
            ) {
                composable(TrackPetsRoute.Dashboard.route) { 
                    val viewModel = remember { HomeViewModel(repository) }
                    HomeScreen(viewModel = viewModel, paddingValues = innerPadding) 
                }
                composable(TrackPetsRoute.Vehicles.route) { 
                    val viewModel = remember { VehicleViewModel(repository) }
                    VehiclesScreen(viewModel = viewModel) 
                }
                composable(TrackPetsRoute.Categories.route) { 
                    val viewModel = remember { CategoryViewModel(repository) }
                    CategoriesScreen(viewModel = viewModel) 
                }
                composable(TrackPetsRoute.Reservations.route) { 
                    val viewModel = remember { ReservationViewModel(repository) }
                    ReservationsScreen(viewModel = viewModel) 
                }
                composable(TrackPetsRoute.Users.route) {
                    val viewModel = remember { UserViewModel(repository) }
                    UsersScreen(
                        viewModel = viewModel,
                        onLogout = { isLoggedIn = false }
                    )
                }
            }
        }
    }
}
