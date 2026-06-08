package com.trackpets.app.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trackpets.app.domain.model.Category
import com.trackpets.app.domain.model.Reservation
import com.trackpets.app.domain.model.Vehicle
import com.trackpets.app.domain.repository.RentalRepository
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = false,
    val vehicles: List<Vehicle> = emptyList(),
    val categories: List<Category> = emptyList(),
    val reservations: List<Reservation> = emptyList(),
    val errorMessage: String? = null
)

class HomeViewModel(private val repository: RentalRepository) : ViewModel() {
    var uiState by mutableStateOf(HomeUiState())
        private set

    init {
        loadDashboardData()
    }

    fun loadDashboardData() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)
            
            val vehiclesResult = repository.getVehicles()
            val categoriesResult = repository.getCategories()
            val reservationsResult = repository.getReservations()

            if (vehiclesResult.isSuccess && categoriesResult.isSuccess && reservationsResult.isSuccess) {
                uiState = uiState.copy(
                    isLoading = false,
                    vehicles = vehiclesResult.getOrThrow(),
                    categories = categoriesResult.getOrThrow(),
                    reservations = reservationsResult.getOrThrow()
                )
            } else {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = "Error al cargar datos del panel"
                )
            }
        }
    }
}
