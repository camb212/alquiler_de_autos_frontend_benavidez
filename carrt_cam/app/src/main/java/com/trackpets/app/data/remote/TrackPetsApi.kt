package com.trackpets.app.data.remote

import com.trackpets.app.data.dto.*
import retrofit2.http.*

interface TrackPetsApi {
    
 
    @POST("api/login/")
    suspend fun login(@Body request: LoginRequestDto): TokenResponseDto

    
    @GET("api/categories/")
    suspend fun getCategories(
        @Query("search") search: String? = null
    ): List<CategoryDto>
    
    @GET("api/categories/{id}/")
    suspend fun getCategory(@Path("id") id: Int): CategoryDto
    
    @POST("api/categories/")
    suspend fun createCategory(@Body category: CategoryDto): CategoryDto
    
    @PUT("api/categories/{id}/")
    suspend fun updateCategory(@Path("id") id: Int, @Body category: CategoryDto): CategoryDto
    
    @DELETE("api/categories/{id}/")
    suspend fun deleteCategory(@Path("id") id: Int)

   
    @GET("api/vehicles/")
    suspend fun getVehicles(
        @Query("search") search: String? = null,
        @Query("available") available: Boolean? = null
    ): List<VehicleDto>
    
    @GET("api/vehicles/{id}/")
    suspend fun getVehicle(@Path("id") id: Int): VehicleDto
    
    @POST("api/vehicles/")
    suspend fun createVehicle(@Body vehicle: VehicleDto): VehicleDto
    
    @PUT("api/vehicles/{id}/")
    suspend fun updateVehicle(@Path("id") id: Int, @Body vehicle: VehicleDto): VehicleDto
    
    @DELETE("api/vehicles/{id}/")
    suspend fun deleteVehicle(@Path("id") id: Int)

  
    @GET("api/reservations/")
    suspend fun getReservations(): List<ReservationDto>
    
    @GET("api/reservations/{id}/")
    suspend fun getReservation(@Path("id") id: Int): ReservationDto
    
    @POST("api/reservations/")
    suspend fun createReservation(@Body reservation: ReservationDto): ReservationDto
    
    @PATCH("api/reservations/{id}/")
    suspend fun updateReservationStatus(@Path("id") id: Int, @Body status: Map<String, String>): ReservationDto
    
    @DELETE("api/reservations/{id}/")
    suspend fun deleteReservation(@Path("id") id: Int)

    
    @GET("api/users/")
    suspend fun getUsers(
        @Query("search") search: String? = null
    ): List<UserDto>
    
    @GET("api/users/{id}/")
    suspend fun getUser(@Path("id") id: Int): UserDto
    
    @POST("api/users/")
    suspend fun createUser(@Body user: UserDto): UserDto
    
    @PUT("api/users/{id}/")
    suspend fun updateUser(@Path("id") id: Int, @Body user: UserDto): UserDto
    
    @DELETE("api/users/{id}/")
    suspend fun deleteUser(@Path("id") id: Int)

    @GET("api/reviews/")
    suspend fun getReviews(
        @Query("vehicle") vehicleId: Int? = null
    ): List<ReviewDto>
    
    @POST("api/reviews/")
    suspend fun createReview(@Body review: ReviewDto): ReviewDto
    
    @DELETE("api/reviews/{id}/")
    suspend fun deleteReview(@Path("id") id: Int)

  
    @GET("api/payments/")
    suspend fun getPayments(): List<PaymentDto>

    @GET("api/payments/{id}/")
    suspend fun getPayment(@Path("id") id: Int): PaymentDto

    @POST("api/payments/")
    suspend fun createPayment(@Body payment: PaymentDto): PaymentDto
}
