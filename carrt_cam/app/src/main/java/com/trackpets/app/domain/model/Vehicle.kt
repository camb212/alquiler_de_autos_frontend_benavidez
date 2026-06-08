package com.trackpets.app.domain.model

data class Vehicle(
    val id: Int? = null,
    val brand: String,
    val model: String,
    val year: Int,
    val plate: String,
    val pricePerDay: Double,
    val available: Boolean,
    val categoryId: Int? = null,
)
