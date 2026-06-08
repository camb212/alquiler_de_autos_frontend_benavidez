package com.trackpets.app.data.repository

import com.trackpets.app.data.dto.LoginRequestDto
import com.trackpets.app.data.mapper.*
import com.trackpets.app.data.remote.TrackPetsApi
import com.trackpets.app.domain.model.*
import com.trackpets.app.domain.repository.RentalRepository
import android.content.Context
import android.content.SharedPreferences

class RentalRepositoryImpl(
    private val api: TrackPetsApi,
    context: Context
) : RentalRepository {

    private val prefs: SharedPreferences = context.getSharedPreferences("rental_prefs", Context.MODE_PRIVATE)

    override suspend fun login(username: String, password: String): Result<Unit> = runCatching {
        val response = api.login(LoginRequestDto(username, password))
        // Django puede devolver 'token' o 'key' según la configuración
        val token = response.token ?: response.key ?: throw Exception("No se recibió un token de acceso")
        
        prefs.edit().apply {
            putString("auth_token", token)
            putString("user_role", response.user?.role ?: "CLIENT")
            response.user?.id?.let { putInt("user_id", it) }
            apply()
        }
    }

    override suspend fun logout() {
        prefs.edit().clear().apply()
    }

    override suspend fun isUserLoggedIn(): Boolean = prefs.getString("auth_token", null) != null

    override suspend fun getUserRole(): String? = prefs.getString("user_role", "CLIENT")
    
    override suspend fun getCurrentUserId(): Int = prefs.getInt("user_id", -1)

    // --- Categorías ---
    override suspend fun getCategories(search: String?): Result<List<Category>> = runCatching {
        api.getCategories(search).map { it.toDomain() }
    }

    override suspend fun getCategory(id: Int): Result<Category> = runCatching {
        api.getCategory(id).toDomain()
    }

    override suspend fun createCategory(category: Category): Result<Category> = runCatching {
        api.createCategory(category.toDto()).toDomain()
    }

    override suspend fun updateCategory(category: Category): Result<Category> = runCatching {
        api.updateCategory(category.id!!, category.toDto()).toDomain()
    }

    override suspend fun deleteCategory(id: Int): Result<Unit> = runCatching {
        api.deleteCategory(id)
    }

    // --- Vehículos ---
    override suspend fun getVehicles(search: String?, available: Boolean?): Result<List<Vehicle>> = runCatching {
        api.getVehicles(search, available).map { it.toDomain() }
    }

    override suspend fun getVehicle(id: Int): Result<Vehicle> = runCatching {
        api.getVehicle(id).toDomain()
    }

    override suspend fun createVehicle(vehicle: Vehicle): Result<Vehicle> = runCatching {
        api.createVehicle(vehicle.toDto()).toDomain()
    }

    override suspend fun updateVehicle(vehicle: Vehicle): Result<Vehicle> = runCatching {
        api.updateVehicle(vehicle.id!!, vehicle.toDto()).toDomain()
    }

    override suspend fun deleteVehicle(id: Int): Result<Unit> = runCatching {
        api.deleteVehicle(id)
    }

    override suspend fun getReservations(): Result<List<Reservation>> = runCatching {
        api.getReservations().map { it.toDomain() }
    }

    override suspend fun getReservation(id: Int): Result<Reservation> = runCatching {
        api.getReservation(id).toDomain()
    }

    override suspend fun createReservation(reservation: Reservation): Result<Reservation> = runCatching {
        api.createReservation(reservation.toDto()).toDomain()
    }

    override suspend fun updateReservationStatus(id: Int, status: String): Result<Reservation> = runCatching {
        api.updateReservationStatus(id, mapOf("status" to status)).toDomain()
    }

    override suspend fun deleteReservation(id: Int): Result<Unit> = runCatching {
        api.deleteReservation(id)
    }

    override suspend fun getUsers(search: String?): Result<List<User>> = runCatching {
        api.getUsers(search).map { it.toUserDomain() }
    }

    override suspend fun getUser(id: Int): Result<User> = runCatching {
        api.getUser(id).toUserDomain()
    }

    override suspend fun createUser(user: User): Result<User> = runCatching {
        api.createUser(user.toUserDto()).toUserDomain()
    }

    override suspend fun updateUser(user: User): Result<User> = runCatching {
        api.updateUser(user.id!!, user.toUserDto()).toUserDomain()
    }

    override suspend fun deleteUser(id: Int): Result<Unit> = runCatching {
        api.deleteUser(id)
    }

    override suspend fun getReviews(vehicleId: Int?): Result<List<Review>> = runCatching {
        api.getReviews(vehicleId).map { it.toDomain() }
    }

    override suspend fun createReview(review: Review): Result<Review> = runCatching {
        api.createReview(review.toDto()).toDomain()
    }

    override suspend fun deleteReview(id: Int): Result<Unit> = runCatching {
        api.deleteReview(id)
    }

    override suspend fun getPayments(): Result<List<Payment>> = runCatching {
        api.getPayments().map { it.toDomain() }
    }

    override suspend fun getPayment(id: Int): Result<Payment> = runCatching {
        api.getPayment(id).toDomain()
    }

    override suspend fun createPayment(payment: Payment): Result<Payment> = runCatching {
        api.createPayment(payment.toDto()).toDomain()
    }
}
