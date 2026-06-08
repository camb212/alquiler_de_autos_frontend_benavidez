package com.trackpets.app.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trackpets.app.domain.model.Category
import com.trackpets.app.domain.model.Vehicle
import com.trackpets.app.domain.repository.RentalRepository
import kotlinx.coroutines.launch

sealed interface VehicleUiState {
    data object Loading : VehicleUiState
    data class Success(val vehicles: List<Vehicle>) : VehicleUiState
    data class Error(val message: String) : VehicleUiState
}

class VehicleViewModel(private val repository: RentalRepository) : ViewModel() {
    var uiState: VehicleUiState by mutableStateOf(VehicleUiState.Loading)
        private set

    var searchQuery by mutableStateOf("")
        private set

    var isAdmin by mutableStateOf(false)
        private set

    var categories by mutableStateOf<List<Category>>(emptyList())
        private set

    init {
        viewModelScope.launch {
            isAdmin = repository.getUserRole() == "ADMIN"
            loadCategories()
            getVehicles()
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            repository.getCategories().onSuccess {
                categories = it
            }
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        searchQuery = newQuery
        getVehicles(newQuery)
    }

    fun getVehicles(query: String? = searchQuery) {
        viewModelScope.launch {
            uiState = VehicleUiState.Loading
            repository.getVehicles(search = query)
                .onSuccess { uiState = if (it.isEmpty()) VehicleUiState.Error("No hay vehículos") else VehicleUiState.Success(it) }
                .onFailure { uiState = VehicleUiState.Error("Error de conexión") }
        }
    }

    fun saveVehicle(vehicle: Vehicle) {
        viewModelScope.launch {
            val result = if (vehicle.id == null) repository.createVehicle(vehicle) else repository.updateVehicle(vehicle)
            result.onSuccess { getVehicles() }
        }
    }

    fun deleteVehicle(id: Int) {
        viewModelScope.launch {
            repository.deleteVehicle(id).onSuccess { getVehicles() }
        }
    }
}
