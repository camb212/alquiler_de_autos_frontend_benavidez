package com.trackpets.app.presentation.navigation

sealed class TrackPetsRoute(val route: String) {
    data object Dashboard : TrackPetsRoute("dashboard")
    data object Vehicles : TrackPetsRoute("vehicles")
    data object Reservations : TrackPetsRoute("reservations")
    data object Categories : TrackPetsRoute("categories")
    data object Users : TrackPetsRoute("users")
    data object Login : TrackPetsRoute("login")
    data object Register : TrackPetsRoute("register")
}
