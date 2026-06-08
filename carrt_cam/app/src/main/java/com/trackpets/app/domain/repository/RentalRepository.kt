package com.trackpets.app.domain.repository

import com.trackpets.app.domain.model.*

interface RentalRepository {
    // --- Autenticación ---
    suspend fun login(username: String, password: String): Result<Unit>
    suspend fun logout()
    suspend fun isUserLoggedIn(): Boolean
    suspend fun getUserRole(): String?
    suspend fun getCurrentUserId(): Int

    // --- Categorías ---
    suspend fun getCategories(search: String? = null): Result<List<Category>>
    suspend fun getCategory(id: Int): Result<Category>
    suspend fun createCategory(category: Category): Result<Category>
    suspend fun updateCategory(category: Category): Result<Category>
    suspend fun deleteCategory(id: Int): Result<Unit>

    // --- Vehículos ---
    suspend fun getVehicles(search: String? = null, available: Boolean? = null): Result<List<Vehicle>>
    suspend fun getVehicle(id: Int): Result<Vehicle>
    suspend fun createVehicle(vehicle: Vehicle): Result<Vehicle>
    suspend fun updateVehicle(vehicle: Vehicle): Result<Vehicle>
    suspend fun deleteVehicle(id: Int): Result<Unit>

    // --- Reservaciones ---
    suspend fun getReservations(): Result<List<Reservation>>
    suspend fun getReservation(id: Int): Result<Reservation>
    suspend fun createReservation(reservation: Reservation): Result<Reservation>
    suspend fun updateReservationStatus(id: Int, status: String): Result<Reservation>
    suspend fun deleteReservation(id: Int): Result<Unit>

    // --- Usuarios ---
    suspend fun getUsers(search: String? = null): Result<List<User>>
    suspend fun getUser(id: Int): Result<User>
    suspend fun createUser(user: User): Result<User>
    suspend fun updateUser(user: User): Result<User>
    suspend fun deleteUser(id: Int): Result<Unit>

    // --- Reseñas ---
    suspend fun getReviews(vehicleId: Int? = null): Result<List<Review>>
    suspend fun createReview(review: Review): Result<Review>
    suspend fun deleteReview(id: Int): Result<Unit>

    // --- Pagos ---
    suspend fun getPayments(): Result<List<Payment>>
    suspend fun getPayment(id: Int): Result<Payment>
    suspend fun createPayment(payment: Payment): Result<Payment>
}
