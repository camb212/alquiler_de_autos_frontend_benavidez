package com.trackpets.app.domain.model

data class Review(
    val id: Int? = null,
    val vehicleId: Int,
    val userId: Int? = null,
    val rating: Int,
    val comment: String,
    val createdAt: String? = null
)
