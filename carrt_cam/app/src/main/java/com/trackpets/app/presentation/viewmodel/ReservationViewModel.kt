package com.trackpets.app.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trackpets.app.domain.model.Reservation
import com.trackpets.app.domain.model.Vehicle
import com.trackpets.app.domain.model.User
import com.trackpets.app.domain.repository.RentalRepository
import kotlinx.coroutines.launch

sealed interface ReservationUiState {
    data object Loading : ReservationUiState
    data class Success(val reservations: List<Reservation>) : ReservationUiState
    data class Error(val message: String) : ReservationUiState
}

class ReservationViewModel(private val repository: RentalRepository) : ViewModel() {
    var uiState: ReservationUiState by mutableStateOf(ReservationUiState.Loading)
        private set

    var vehicles by mutableStateOf<List<Vehicle>>(emptyList())
        private set

    var users by mutableStateOf<List<User>>(emptyList())
        private set

    var isAdmin by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            isAdmin = repository.getUserRole() == "ADMIN"
            loadInitialData()
            getReservations()
        }
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            repository.getVehicles().onSuccess { vehicles = it }
            if (isAdmin) {
                repository.getUsers().onSuccess { users = it }
            }
        }
    }

    fun getReservations() {
        viewModelScope.launch {
            uiState = ReservationUiState.Loading
            repository.getReservations()
                .onSuccess { 
                    uiState = if (it.isEmpty()) {
                        ReservationUiState.Error("No se encontraron reservas")
                    } else {
                        ReservationUiState.Success(it)
                    }
                }
                .onFailure { uiState = ReservationUiState.Error("Error al cargar reservas: ${it.message}") }
        }
    }

    fun deleteReservation(id: Int) {
        viewModelScope.launch {
            repository.deleteReservation(id).onSuccess { getReservations() }
        }
    }

    fun saveReservation(reservation: Reservation) {
        viewModelScope.launch {
            val result = if (reservation.id == null) {
                // Si es cliente, asignamos su propio ID si no viene uno
                val userId = if (reservation.userId == 0 || reservation.userId == null) {
                    repository.getCurrentUserId()
                } else {
                    reservation.userId
                }
                repository.createReservation(reservation.copy(userId = userId))
            } else {
                // Para actualizar estado usualmente usamos updateReservationStatus en este repo, 
                // pero si el repo tuviera un update general se usaría aquí.
                // Como el repo solo tiene updateReservationStatus, lo usaremos para el status.
                repository.updateReservationStatus(reservation.id!!, reservation.status)
            }
            
            result.onSuccess { getReservations() }
        }
    }
}
