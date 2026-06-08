package com.trackpets.app.domain.model

data class Reservation(
    val id: Int? = null,
    val userId: Int? = null,
    val vehicleId: Int? = null,
    val startDate: String,
    val endDate: String,
    val totalPrice: Double,
    val status: String,
    val createdAt: String,
)
